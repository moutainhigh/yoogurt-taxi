package com.yoogurt.taxi.pay.service.impl;

import com.yoogurt.taxi.common.constant.CacheKey;
import com.yoogurt.taxi.common.enums.MessageQueue;
import com.yoogurt.taxi.common.helper.RedisHelper;
import com.yoogurt.taxi.common.utils.RandomUtils;
import com.yoogurt.taxi.dal.bo.BaseNotify;
import com.yoogurt.taxi.dal.bo.TaskInfo;
import com.yoogurt.taxi.pay.doc.Event;
import com.yoogurt.taxi.pay.doc.EventTask;
import com.yoogurt.taxi.pay.doc.Payment;
import com.yoogurt.taxi.pay.mq.TaskSender;
import com.yoogurt.taxi.pay.service.NotifyService;
import com.yoogurt.taxi.pay.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class NotifyServiceImpl implements NotifyService {

    @Autowired
    private PayService payService;

    @Autowired
    private TaskSender<EventTask> eventTaskSender;

    @Autowired
    private RedisHelper redis;


    /**
     * 提交一个回调任务
     *
     * @param event 回调事件
     * @return EventTask。如果提交失败，将会返回null
     */
    @Override
    public EventTask submit(Event<? extends BaseNotify> event) {

        try {
            return doSubmit(event, null, false);
        } catch (Exception e) {
            log.error("提交回调任务失败, {}", e);
        }
        return null;
    }

    /**
     * 获取一个回调任务
     *
     * @param taskId 任务id
     * @return EventTask
     */
    @Override
    public EventTask getTask(String taskId) {

        Object o = redis.getMapValue(CacheKey.NOTIFY_MAP, CacheKey.TASK_HASH_KEY + taskId);
        if (o == null) {
            return null;
        }
        return (EventTask) o;
    }

    /**
     * 获取回调任务执行结果。
     *
     * @param taskId 任务id
     * @return 回调对象
     */
    @Override
    public <T extends BaseNotify> Event<T> queryResult(String taskId) {

        Object o = redis.getMapValue(CacheKey.NOTIFY_MAP, CacheKey.EVENT_HASH_KEY + taskId);
        if (o == null) {
            return null;
        }
        return (Event<T>) o;
    }

    /**
     * 取消回调任务
     *
     * @param taskId 任务id
     * @return EventTask
     */
    @Override
    public EventTask cancel(String taskId) {
        return null;
    }

    /**
     * 任务重试
     *
     * @param taskId 任务id
     * @return EventTask
     */
    @Override
    public EventTask retry(String taskId) {

        try {
            return doSubmit(null, taskId, true);
        } catch (Exception e) {
            log.error("回调任务重试异常,{}", e);
        }
        return null;
    }

    private EventTask doSubmit(Event<? extends BaseNotify> event, String taskId, boolean isRetry) {
        final EventTask eventTask;
        if (isRetry && StringUtils.isNoneBlank(taskId)) {
            eventTask = getTask(taskId);
            if(eventTask != null) {
                eventTask.getTask().doRetry();
            }
        } else {
            Map metadata = event.getData().getMetadata();
            taskId = "pt_" + RandomUtils.getPrimaryKey();
            TaskInfo task = new TaskInfo(taskId);
            task.setMessageQueue(buildMessageQueue(metadata));

            eventTask = new EventTask(taskId);
            eventTask.setEvent(event);
            eventTask.setTask(task);
            eventTask.setPayment(buildPayment(metadata));
        }
        //重新设置任务信息缓存
        redis.put(CacheKey.NOTIFY_MAP, CacheKey.TASK_HASH_KEY + taskId, eventTask);
        eventTaskSender.send(eventTask);
        return eventTask;
    }

    private Payment buildPayment(Map metadata) {
        if(metadata == null || metadata.isEmpty()) {
            return null;
        }
        if (metadata.get("payId") != null) {
            String payId = metadata.get("payId").toString();
            return payService.getPayment(payId);
        }
        return null;
    }

    private MessageQueue buildMessageQueue(Map metadata) {
        if(metadata == null || metadata.isEmpty()) {
            return null;
        }
        if (metadata.get("biz") != null) {
            String biz = metadata.get("biz").toString();
            return MessageQueue.getEnumsByBiz(biz);
        }
        return null;
    }
}

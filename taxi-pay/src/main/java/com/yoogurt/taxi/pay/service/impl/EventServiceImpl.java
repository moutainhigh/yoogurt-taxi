package com.yoogurt.taxi.pay.service.impl;

import com.yoogurt.taxi.pay.dao.EventDao;
import com.yoogurt.taxi.pay.doc.Event;
import com.yoogurt.taxi.pay.service.EventService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EventServiceImpl extends EventTaskServiceImpl implements EventService {

    @Autowired
    private EventDao eventDao;

    @Override
    public Event getEvent(String eventId) {
        if (StringUtils.isBlank(eventId)) {
            return null;
        }
        return eventDao.findOne(eventId);
    }

    @Override
    public Event addEvent(Event event) {
        if (event == null) {
            return null;
        }
        return eventDao.insert(event);
    }

    @Override
    public Event saveEvent(Event event) {
        if (event == null) {
            return null;
        }
        return eventDao.save(event);
    }

    @Override
    public boolean deleteEvent(String eventId) {
        if (StringUtils.isBlank(eventId)) {
            return false;
        }
        try {
            eventDao.delete(eventId);
        } catch (Exception e) {
            log.error("删除回调事件异常, {}", e);
            return false;
        }
        return true;
    }
}

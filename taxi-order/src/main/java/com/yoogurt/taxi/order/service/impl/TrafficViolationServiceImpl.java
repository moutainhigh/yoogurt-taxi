package com.yoogurt.taxi.order.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yoogurt.taxi.common.constant.Constants;
import com.yoogurt.taxi.common.enums.StatusCode;
import com.yoogurt.taxi.common.factory.PagerFactory;
import com.yoogurt.taxi.common.pager.Pager;
import com.yoogurt.taxi.common.vo.ResponseObj;
import com.yoogurt.taxi.dal.beans.OrderAcceptInfo;
import com.yoogurt.taxi.dal.beans.OrderInfo;
import com.yoogurt.taxi.dal.beans.OrderTrafficViolationInfo;
import com.yoogurt.taxi.dal.condition.order.TrafficViolationListCondition;
import com.yoogurt.taxi.dal.enums.TrafficStatus;
import com.yoogurt.taxi.dal.enums.UserType;
import com.yoogurt.taxi.order.dao.TrafficViolationDao;
import com.yoogurt.taxi.order.form.TrafficViolationForm;
import com.yoogurt.taxi.order.service.AcceptService;
import com.yoogurt.taxi.order.service.OrderInfoService;
import com.yoogurt.taxi.order.service.TrafficViolationService;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.entity.Example;

@Repository
public class TrafficViolationServiceImpl implements TrafficViolationService {

    @Autowired
    private TrafficViolationDao trafficViolationDao;

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private AcceptService acceptService;

    @Autowired
    private PagerFactory appPagerFactory;

    @Autowired
    private PagerFactory webPagerFactory;

    @Override
    public Pager<OrderTrafficViolationInfo> getTrafficViolations(TrafficViolationListCondition condition) {
        Example ex = buildExample(condition);
        PageHelper.startPage(condition.getPageNum(), condition.getPageSize(), "happen_time DESC");
        Page<OrderTrafficViolationInfo> page = (Page<OrderTrafficViolationInfo>) trafficViolationDao.selectByExample(ex);
        if (condition.isFromApp()) {
            return appPagerFactory.generatePager(page);
        }
        return webPagerFactory.generatePager(page);
    }

    @Override
    public OrderTrafficViolationInfo getTrafficViolationInfo(Long id) {
        if (id == null || id <= 0) return null;
        return trafficViolationDao.selectById(id);
    }

    @Override
    public OrderTrafficViolationInfo addTrafficViolation(OrderTrafficViolationInfo trafficViolation) {
        if (trafficViolation == null) return null;
        if (trafficViolationDao.insertSelective(trafficViolation) == 1) return trafficViolation;
        return null;
    }

    @Override
    public ResponseObj buildTrafficViolation(TrafficViolationForm form) {
        ResponseObj validateResult = isAllowed(form.getOrderId(), form.getUserId());
        //校验不成功，直接返回校验结果
        if(!validateResult.isSuccess()) return validateResult;

        OrderTrafficViolationInfo traffic = new OrderTrafficViolationInfo();
        BeanUtils.copyProperties(form, traffic);
        OrderInfo orderInfo = orderInfoService.getOrderInfo(form.getOrderId(), form.getUserId());
        if (orderInfo == null) return ResponseObj.fail(StatusCode.BIZ_FAILED, "订单不存在");
        traffic.setCarId(orderInfo.getCarId());
        traffic.setPlateNumber(orderInfo.getPlateNumber());
        traffic.setVin(orderInfo.getVin());
        traffic.setDriverId(orderInfo.getAgentDriverId());
        traffic.setDriverName(orderInfo.getAgentDriverName());
        traffic.setMobile(orderInfo.getAgentDriverPhone());
        traffic.setUserType(UserType.USER_APP_AGENT.getCode());
        traffic.setStatus(TrafficStatus.PENDING.getCode());
        return ResponseObj.success(traffic);
    }

    /**
     * 更改违章处理状态，状态不能倒退。
     *
     * @param id     违章记录ID
     * @param status 修改后的状态
     */
    @Override
    public OrderTrafficViolationInfo modifyStatus(Long id, TrafficStatus status) {
        if (id == null || id <= 0 || status == null) return null;
        OrderTrafficViolationInfo traffic = getTrafficViolationInfo(id);
        if (traffic == null) return null;
        if (status.getCode() < traffic.getStatus()) return null;//状态不能倒退
        if (status.getCode().equals(traffic.getStatus())) return traffic;
        traffic.setStatus(status.getCode());
        if (trafficViolationDao.updateByIdSelective(traffic) == 1) return traffic;
        return null;
    }

    /**
     * 判断是否可以录入违章信息：交易结束后20天内可以提交
     *
     * @param orderId 订单ID
     * @param userId  用户ID
     * @return 是否允许录入违章信息
     */
    @Override
    public ResponseObj isAllowed(Long orderId, Long userId) {
        OrderAcceptInfo acceptInfo = acceptService.getAcceptInfo(orderId);
        if (acceptInfo == null) return ResponseObj.fail(StatusCode.BIZ_FAILED, "订单暂未结束");
        int days = Days.daysBetween(new DateTime(), new DateTime((acceptInfo.getGmtCreate()))).getDays();
        return days <= Constants.MAX_INTERVAL_DAYS ? ResponseObj.success() : ResponseObj.fail(StatusCode.BIZ_FAILED, "交易结束后" + Constants.MAX_INTERVAL_DAYS + "天内才能提交违章记录");
    }

    private Example buildExample(TrafficViolationListCondition condition) {
        Example ex = new Example(OrderTrafficViolationInfo.class);
        Example.Criteria criteria = ex.createCriteria().andEqualTo("isDeleted", Boolean.FALSE);
        if (condition.getOrderId() != null) {
            criteria.andEqualTo("orderId", condition.getOrderId());
        }

        if (condition.getUserId() != null) {
            criteria.andEqualTo("userId", condition.getUserId());
        }

        if (condition.getStatus() != null) {
            criteria.andEqualTo("status", condition.getStatus());
        }

        if (StringUtils.isNoneBlank(condition.getPlateNumber())) {
            criteria.andEqualTo("driverName", condition.getPlateNumber());
        }

        if (StringUtils.isNoneBlank(condition.getVin())) {
            criteria.andEqualTo("mobile", condition.getVin());
        }

        if (condition.getStartTime() != null) {
            criteria.andGreaterThanOrEqualTo("happenTime", condition.getStartTime());
        }

        if (condition.getEndTime() != null) {
            criteria.andLessThanOrEqualTo("happenTime", condition.getEndTime());
        }
        return ex;
    }
}
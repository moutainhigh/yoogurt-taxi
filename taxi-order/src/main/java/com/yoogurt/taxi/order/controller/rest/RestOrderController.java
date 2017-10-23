package com.yoogurt.taxi.order.controller.rest;

import com.yoogurt.taxi.common.vo.RestResult;
import com.yoogurt.taxi.dal.beans.CommentTagStatistic;
import com.yoogurt.taxi.dal.beans.OrderInfo;
import com.yoogurt.taxi.dal.beans.OrderStatistic;
import com.yoogurt.taxi.dal.enums.OrderStatus;
import com.yoogurt.taxi.order.service.CommentTagStatisticService;
import com.yoogurt.taxi.order.service.OrderInfoService;
import com.yoogurt.taxi.order.service.OrderStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rest/order")
public class RestOrderController {

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private OrderStatisticService orderStatisticService;

    @Autowired
    private CommentTagStatisticService commentTagStatisticService;

    @RequestMapping(value = "/statistics/userId/{userId}", method = RequestMethod.GET, produces = {"application/json;charset=utf-8"})
    public RestResult<Map<String, Object>> statistics(@PathVariable(name = "userId") Long userId) {

        //订单统计信息
        OrderStatistic orderStatistics = orderStatisticService.getOrderStatistics(userId);
        if (orderStatistics.getScore() != null){
            orderStatistics.setScore(new BigDecimal(Math.round(orderStatistics.getScore().doubleValue())));
        }
        //评价标签统计信息
        List<CommentTagStatistic> commentTagStatistics = commentTagStatisticService.getStatistic(userId);

        Map<String, Object> result = new HashMap<>();
        result.put("order", orderStatistics);
        result.put("comment", commentTagStatistics);
        return RestResult.success(result);
    }

    @RequestMapping(value = "/statistics/unfinished/userId/{userId}/userType/{userType}", method = RequestMethod.GET, produces = {"application/json;charset=utf-8"})
    public RestResult<List<OrderInfo>> unfinishedOrders(@PathVariable(name = "userId") Long userId, @PathVariable(name = "userType") Integer userType) {
        //未完成订单数量
        List<OrderInfo> orderList = orderInfoService.getOrderList(userId, userType, OrderStatus.HAND_OVER.getCode(), OrderStatus.PICK_UP.getCode(), OrderStatus.GIVE_BACK.getCode(), OrderStatus.ACCEPT.getCode());
        return RestResult.success(orderList);
    }

    @RequestMapping(value = "/statistics/complete/userId/{userId}/userType/{userType}", method = RequestMethod.GET, produces = {"application/json;charset=utf-8"})
    public RestResult<List<OrderInfo>> orderStatistics(@PathVariable(name = "userId") Long userId, @PathVariable(name = "userType") Integer userType) {
        //最近一笔已完成订单时间
        List<OrderInfo> orderList = orderInfoService.getOrderList(userId, userType, OrderStatus.FINISH.getCode());
        return RestResult.success(orderList);
    }
}

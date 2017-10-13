package com.yoogurt.taxi.order.controller.mobile;

import com.yoogurt.taxi.common.controller.BaseController;
import com.yoogurt.taxi.common.enums.StatusCode;
import com.yoogurt.taxi.common.vo.ResponseObj;
import com.yoogurt.taxi.dal.beans.OrderInfo;
import com.yoogurt.taxi.order.service.CancelRuleService;
import com.yoogurt.taxi.order.service.GiveBackRuleService;
import com.yoogurt.taxi.order.service.HandoverRuleService;
import com.yoogurt.taxi.order.service.OrderInfoService;
import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mobile/order")
public class DisobeyRuleController extends BaseController {

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private CancelRuleService cancelRuleService;

    @Autowired
    private HandoverRuleService handoverRuleService;

    @Autowired
    private GiveBackRuleService giveBackRuleService;

    @RequestMapping(value = "/disobey/descriptions/{type}", method = RequestMethod.GET, produces = {"application/json;charset=utf-8"})
    public ResponseObj getDescription(@PathVariable(name = "type") String type) {

        if ("cancel".equalsIgnoreCase(type)) {
            return ResponseObj.success(cancelRuleService.getIntroduction());
        } else if ("handover".equalsIgnoreCase(type)) {
            return ResponseObj.success(handoverRuleService.getIntroduction());
        } else if ("giveback".equalsIgnoreCase(type)) {
            return ResponseObj.success(giveBackRuleService.getIntroduction());
        } else {
            return ResponseObj.fail();
        }
    }

    @RequestMapping(value = "/disobey/rules/{orderId}/{type}", method = RequestMethod.GET, produces = {"application/json;charset=utf-8"})
    public ResponseObj getRule(@PathVariable(name = "orderId") Long orderId, @PathVariable(name = "type") String type) {
        OrderInfo orderInfo = orderInfoService.getOrderInfo(orderId, super.getUserId());
        if(orderInfo == null) return ResponseObj.fail(StatusCode.BIZ_FAILED, "订单不存在");

        Integer time = Minutes.minutesBetween(new DateTime(), new DateTime(orderInfo.getHandoverTime())).getMinutes();
        if ("cancel".equalsIgnoreCase(type)) {
            time = Hours.hoursBetween(new DateTime(), new DateTime(orderInfo.getHandoverTime())).getHours();
            return ResponseObj.success(cancelRuleService.getRuleInfo(time, "HOURS"));
        } else if ("handover".equalsIgnoreCase(type)) {
            return ResponseObj.success(handoverRuleService.getRuleInfo(time, "MINUTES"));
        } else if ("giveback".equalsIgnoreCase(type)) {
            return ResponseObj.success(giveBackRuleService.getRuleInfo(time, "MINUTES"));
        } else {
            return ResponseObj.fail();
        }
    }
}

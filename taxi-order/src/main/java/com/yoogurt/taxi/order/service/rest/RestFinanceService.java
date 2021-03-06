package com.yoogurt.taxi.order.service.rest;

import com.yoogurt.taxi.common.vo.RestResult;
import com.yoogurt.taxi.dal.vo.PaymentVo;
import com.yoogurt.taxi.order.service.rest.hystrix.RestFinanceServiceImpl;
import com.yoogurt.taxi.pay.doc.Payment;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "taxi-finance", fallback = RestFinanceServiceImpl.class)
public interface RestFinanceService {

    @RequestMapping(value = "/rest/finance/payment", method = RequestMethod.PUT, produces = {"application/json;charset=utf-8"})
    RestResult<Payment> updatePayment(@RequestBody PaymentVo paymentVo);
}

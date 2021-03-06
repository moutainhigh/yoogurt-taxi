package com.yoogurt.taxi.order;

import com.yoogurt.taxi.common.pager.BasePager;
import com.yoogurt.taxi.dal.beans.OrderDisobeyInfo;
import com.yoogurt.taxi.dal.beans.OrderInfo;
import com.yoogurt.taxi.dal.condition.order.DisobeyListCondition;
import com.yoogurt.taxi.dal.enums.DisobeyType;
import com.yoogurt.taxi.dal.enums.UserType;
import com.yoogurt.taxi.order.service.DisobeyService;
import com.yoogurt.taxi.order.service.OrderInfoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class DisobeyTests {

    @Autowired
    private DisobeyService disobeyService;

    @Autowired
    private OrderInfoService orderInfoService;

    @Test
    public void addDisobeyTest() {
        OrderInfo orderInfo = orderInfoService.getOrderInfo("17101612021383517", "17101612021383517");
        OrderDisobeyInfo disobeyInfo = disobeyService.buildDisobeyInfo(
                orderInfo,
                UserType.USER_APP_OFFICE,
                DisobeyType.OFFICIAL_DRIVER_HANDOVER_TIMEOUT,
                1L, new BigDecimal(120), "违约记录");
        Assert.assertNotNull("添加违约记录失败", disobeyService.addDisobey(disobeyInfo));
    }

    @Test
    public void modifyStatusTest() {
        boolean status = true;
        OrderDisobeyInfo disobeyInfo = disobeyService.modifyStatus(1L, status);
        Assert.assertEquals("状态修改不成功", status, disobeyInfo.getStatus());
    }

    @Test
    public void getDisobeyListTest() {

        DisobeyListCondition condition = new DisobeyListCondition();
        condition.setOrderId("17101612021383517");
        BasePager<OrderDisobeyInfo> disobeyList = disobeyService.getDisobeyList(condition);
        Assert.assertEquals(1, disobeyList.getDataList().size());
    }
}

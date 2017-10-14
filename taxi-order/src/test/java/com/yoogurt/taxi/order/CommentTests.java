package com.yoogurt.taxi.order;

import com.yoogurt.taxi.common.vo.ResponseObj;
import com.yoogurt.taxi.dal.beans.OrderCommentInfo;
import com.yoogurt.taxi.order.form.CommentForm;
import com.yoogurt.taxi.order.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class CommentTests {

    @Autowired
    private CommentService commentService;

    @Test
    public void doCommentTest() {
        CommentForm commentForm = new CommentForm();
        commentForm.setOrderId(17092615073534929L);
        commentForm.setDriverId(17092815473439032L);
        commentForm.setUserId(8888L);
        commentForm.setUserType(20);
        commentForm.setScore(5);
        commentForm.setTagId(new Long[]{1L, 2L});
        commentForm.setTagName(new String[]{"驾驶平稳", "爱护车辆"});
        commentForm.setRemark("xxx");
        ResponseObj o = commentService.doComment(commentForm);
        Assert.assertTrue(o.getMessage(), o.isSuccess());
    }
}

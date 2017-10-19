package com.yoogurt.taxi.order;

import com.yoogurt.taxi.order.mq.NotificationSender;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class MQTests {

    @Autowired
    private NotificationSender sender;

    @Test
    public void sendTest() {
        sender.send();
    }
}

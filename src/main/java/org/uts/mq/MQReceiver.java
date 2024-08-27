package org.uts.mq;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.rabbitmq.client.Channel;
import org.uts.global.constant.MessageType;
import org.uts.mq.handler.OrderHandler;
import org.uts.mq.message.AddOrderResultMessage;
import org.uts.mq.message.MQMessage;

import java.util.Date;

/**
 * @Description 向MQ接收消息
 * @Author codBoy
 * @Date 2024/8/24 20:11
 */
@Component
@Slf4j
public class MQReceiver {

    private static final String UTS_EVENT_BUSINESS_QUEUE = "UTS_EVENT_BUSINESS_QUEUE";

    @Autowired
    private OrderHandler orderHandler;

    /**
     * 接收BUSINESS_QUEUE的消息,这里返回值必须为void
     */
    @RabbitListener(queues = UTS_EVENT_BUSINESS_QUEUE)
    public void receiveMessage(Message message, Channel channel) {
        String msg = new String(message.getBody());
        log.info("{}，接收到MQ消息，消息内容：{}", new Date().toString(), msg);
        MQMessage mqMessage = JSON.parseObject(msg, MQMessage.class);
        if(MessageType.ADD_ORDER_RESULT_MESSAGE_TYPE.equals(mqMessage.getType())) {
            orderHandler.dealWithAddOrderResult(JSON.parseObject(mqMessage.getBody(), AddOrderResultMessage.class));
        }
    }
}

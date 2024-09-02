package org.uts.mq.handler;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.uts.WebsocketServer;
import org.uts.mq.MqEnum;
import org.uts.mq.message.AddOrderResultMessage;
import org.uts.mq.message.MQMessage;
import org.uts.utils.SnowflakeUtils;

import javax.websocket.Session;

/**
 * @Description 订单MQ消息处理类
 * @Author codBoy
 * @Date 2024/8/24 21:09
 */
@Component
@Slf4j
public class OrderHandler {

    private final SnowflakeUtils snowflakeUtils = new SnowflakeUtils(1, 1);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /*
     处理新增订单消息
     WS适用于服务端和客户端之间的场景。因此发送创建订单结果消息到MQ,由事件中心服务接收MQ消息，然后发送WS消息,由前端接收
     */
    public void dealWithAddOrderResult(AddOrderResultMessage addOrderResultMsg) {
        //发送WS消息到客户端
        String addOrderResultMessage = JSON.toJSONString(addOrderResultMsg);
        try {
            int tryTime = 0;
            do {
                Session session = WebsocketServer.SESSION_MAP.get(addOrderResultMsg.getUserId());
                if(session != null){
                    //通过session对象，将数据写到前端
                    session.getBasicRemote().sendText(addOrderResultMessage);
                    return;
                }
                log.info("[订单结果] 第{}次查询session对象获取失败，准备下一次获取...", tryTime + 1);
                //睡一会，等待前端WS连接建立好
                Thread.sleep(300);
            } while (tryTime++ < 5);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     处理订单超时消息:当接收到订单超时消息时，将消息发送到订单服务和秒杀服务，订单服务做更新订单状态操作，秒杀服务做回退操作
     */
    public void dealWithOrderDelayResult(MQMessage orderTimeoutMessage) {
        //向秒杀服务发送订单超时消息
        rabbitTemplate.convertAndSend(
                MqEnum.UTS_SECKILL_QUEUE.getExchange(),
                MqEnum.UTS_SECKILL_QUEUE.getRoutingKey(),
                JSON.toJSONString(orderTimeoutMessage)
        );
        //向订单服务发送订单超时消息
        rabbitTemplate.convertAndSend(
                MqEnum.UTS_ORDER_QUEUE.getExchange(),
                MqEnum.UTS_ORDER_QUEUE.getRoutingKey(),
                JSON.toJSONString(orderTimeoutMessage)
        );
    }
}

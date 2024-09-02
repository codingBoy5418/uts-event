package org.uts.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description 交换机 配置类
 * @Author codBoy
 * @Date 2024/8/24 19:55
 */
@Configuration
public class MQConfig {

    //业务类交换机
    private static final String BUSINESS_EXCHANGE = "BUSINESS_EXCHANGE";
    //死信交换机
    private static final String DEAD_EXCHANGE = "DEAD_EXCHANGE";

    //事件中心业务队列
    private static final String UTS_EVENT_BUSINESS_QUEUE = "UTS_EVENT_BUSINESS_QUEUE";
    //订单队列
    private static final String UTS_ORDER_QUEUE = "UTS_ORDER_QUEUE";
    //秒杀队列
    private static final String UTS_SECKILL_QUEUE = "UTS_SECKILL_QUEUE";


    //正常队列，用于接收延时消息
    private static final String UTS_GENERAL_QUEUE = "UTS_GENERAL_QUEUE";
    //死信队列，用于和死信交换机绑定
    private static final String UTS_DEAD_QUEUE = "UTS_DEAD_QUEUE";


    //事件中业务心路由键
    private static final String UTS_EVENT_BUSINESS_ROUTING_KEY = "uts_event_business";
    //订单路由键
    private static final String UTS_ORDER_ROUTING_KEY = "uts_order";
    //秒杀路由键
    private static final String UTS_SECKILL_ROUTING_KEY = "uts_seckill";

    //正常队列和正常交换机的路由键
    private static final String UTS_GENERAL_ROUTING_KEY = "uts_general";
    //死信队列和死信交换机的路邮键
    private static final String UTS_DEAD_ROUTING_KEY = "uts_dead";

    //声明事件中心队列，用于事件中心服务
    @Bean(UTS_EVENT_BUSINESS_QUEUE)
    public Queue createEventBusinessQueue(){
        return QueueBuilder.durable(UTS_EVENT_BUSINESS_QUEUE).build();
    }
    //声明订单队列，用于订单服务
    @Bean(UTS_ORDER_QUEUE)
    public Queue createOrderQueue(){
        return QueueBuilder.durable(UTS_ORDER_QUEUE).build();
    }
    //声明秒杀队列，用于订单服务
    @Bean(UTS_SECKILL_QUEUE)
    public Queue removeSecKillQueue(){
        return QueueBuilder.durable(UTS_SECKILL_QUEUE).build();
    }


    //声明正常队列，用于接收延时消息
    @Bean(UTS_GENERAL_QUEUE)
    public Queue generalQueue() {
        Map<String, Object> args = new HashMap<>();

        //设置消息过期时间，此方法是在队列的颗粒度设置，比较局限，所以在消息上设置过期时间
        //args.put("x-message-ttl", 1000 * 5);
        //设置死信交换机
        args.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        //设置死信 routing_key
        args.put("x-dead-letter-routing-key", UTS_DEAD_ROUTING_KEY);

        return new Queue(UTS_GENERAL_QUEUE, true, false, false, args);
    }
    //声明死信队列
    @Bean(UTS_DEAD_QUEUE)
    public Queue deadQueue() {
        return QueueBuilder.durable(UTS_DEAD_QUEUE).build();
    }

    //声明业务交换机
    @Bean(BUSINESS_EXCHANGE)
    public Exchange businessExchange(){
        return new DirectExchange(BUSINESS_EXCHANGE);
    }
    //声明死信交换机
    @Bean(DEAD_EXCHANGE)
    public Exchange deadExchange(){
        return new DirectExchange(DEAD_EXCHANGE);
    }

    //绑定事件中心业务队列和业务交换机
    @Bean
    public Binding eventBusinessQueueBindExchange(@Qualifier(UTS_EVENT_BUSINESS_QUEUE) Queue businessQueue, @Qualifier(BUSINESS_EXCHANGE) DirectExchange directExchange){
        return BindingBuilder.bind(businessQueue).to(directExchange).with(UTS_EVENT_BUSINESS_ROUTING_KEY);
    }
    //绑定订单队列和业务交换机
    @Bean
    public Binding orderQueueBindExchange(@Qualifier(UTS_ORDER_QUEUE) Queue businessQueue, @Qualifier(BUSINESS_EXCHANGE) DirectExchange directExchange){
        return BindingBuilder.bind(businessQueue).to(directExchange).with(UTS_ORDER_ROUTING_KEY);
    }
    //绑定秒杀队列和业务交换机
    @Bean
    public Binding secKillQueueBindExchange(@Qualifier(UTS_SECKILL_QUEUE) Queue businessQueue, @Qualifier(BUSINESS_EXCHANGE) DirectExchange directExchange){
        return BindingBuilder.bind(businessQueue).to(directExchange).with(UTS_SECKILL_ROUTING_KEY);
    }

    //绑定正常队列和正常交换机
    @Bean
    public Binding generalQueueBindExchange(@Qualifier(UTS_GENERAL_QUEUE) Queue businessQueue, @Qualifier(BUSINESS_EXCHANGE) DirectExchange directExchange){
        return BindingBuilder.bind(businessQueue).to(directExchange).with(UTS_GENERAL_ROUTING_KEY);
    }
    //绑定死信队列和死信交换机
    @Bean
    public Binding deadQueueBindExchange(@Qualifier(UTS_DEAD_QUEUE) Queue businessQueue, @Qualifier(DEAD_EXCHANGE) DirectExchange directExchange){
        return BindingBuilder.bind(businessQueue).to(directExchange).with(UTS_DEAD_ROUTING_KEY);
    }
}

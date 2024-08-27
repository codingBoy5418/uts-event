package org.uts.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description 交换机 配置类
 * @Author codBoy
 * @Date 2024/8/24 19:55
 */
@Configuration
public class MQConfig {

    //业务类交换机
    private static final String BUSINESS_EXCHANGE = "BUSINESS_EXCHANGE";

    //事件中心业务队列
    private static final String UTS_EVENT_BUSINESS_QUEUE = "UTS_EVENT_BUSINESS_QUEUE";

    //订单队列
    private static final String UTS_ORDER_QUEUE = "UTS_ORDER_QUEUE";

    //秒杀队列
    private static final String UTS_SECKILL_QUEUE = "UTS_SECKILL_QUEUE";

    //事件中业务心路由键
    private static final String UTS_EVENT_BUSINESS_ROUTING_KEY = "uts_event_business";

    //订单路由键
    private static final String UTS_ORDER_ROUTING_KEY = "uts_order";

    //秒杀路由键
    private static final String UTS_SECKILL_ROUTING_KEY = "uts_seckill";


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

    //声明业务交换机
    @Bean(BUSINESS_EXCHANGE)
    public Exchange exchange(){
        return new DirectExchange(BUSINESS_EXCHANGE);
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
}

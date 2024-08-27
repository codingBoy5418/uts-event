package org.uts.global.constant;

/**
 * @Author 86180
 * @Date 2022/12/22 15:15
 * @Version 1.0
 * @Description: MQ交换机 常量类
 **/
public enum MQExchangeEnum {

    //创建订单队列
    CREATE_ORDER_QUEUE("BUSINESS_EXCHANGE", "CREATE_ORDER_QUEUE", "create_order"),

    //创建订单结果队列
    CREATE_ORDER_RESULT_QUEUE("BUSINESS_EXCHANGE", "CREATE_ORDER_RESULT_QUEUE", "create_order_result"),

    ;



    //业务交换机
    public String exchange;
    //队列
    public String queue;
    //路邮键
    public String routingKey;

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    MQExchangeEnum(String exchange, String queue, String routingKey) {
        this.exchange = exchange;
        this.queue = queue;
        this.routingKey = routingKey;
    }
}

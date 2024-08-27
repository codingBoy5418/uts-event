package org.uts.mq;

/**
 * @Author 86180
 * @Date 2022/12/22 15:15
 * @Version 1.0
 * @Description: mq常量类
 **/
public enum MqEnum {

    BUSINESS_EXCHANGE("BUSINESS_EXCHANGE", "BUSINESS_QUEUE", "business"),

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

    MqEnum(String exchange, String queue, String routingKey) {
        this.exchange = exchange;
        this.queue = queue;
        this.routingKey = routingKey;
    }
}

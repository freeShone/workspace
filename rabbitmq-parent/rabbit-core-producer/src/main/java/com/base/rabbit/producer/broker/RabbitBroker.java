package com.base.rabbit.producer.broker;

import com.base.rabbit.api.Message;

/**
 * 具体发送不同类型消息的接口
 */
public interface RabbitBroker {

    void rapidSend(Message message);

    void confirmSend(Message message);

    void reliantSend(Message message);

    void sendMessages();
}

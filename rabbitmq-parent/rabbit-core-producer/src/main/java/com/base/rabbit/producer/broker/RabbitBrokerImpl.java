package com.base.rabbit.producer.broker;

import com.base.rabbit.api.Message;
import com.base.rabbit.api.MessageType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * 真正发送消息的实现类
 */
@Component
@Slf4j
public class RabbitBrokerImpl implements RabbitBroker {

    private RabbitTemplate rabbitTemplate;

    /**
     * 发送迅速消息
     * @param message
     */
    @Override
    public void rapidSend(Message message) {
        message.setMessageType(MessageType.RAPID);
        sendKernel(message);
    }

    /**
     * 发送消息的核心方法
     * @param message
     */
    private void sendKernel(Message message) {
        AsyncBaseQueue.submit(()->{
            CorrelationData correlationData = new CorrelationData(String.format("%s#%s",message.getMessageId(),System.currentTimeMillis()));
            rabbitTemplate.convertAndSend(message.getTopic(),message.getRoutingKey(),message,correlationData);
            log.info("#RabbitBrokerImpl.sendKernel# send to rabbitmq,messageId: {}",message.getMessageId());
        });

    }

    @Override
    public void confirmSend(Message message) {

    }

    @Override
    public void reliantSend(Message message) {

    }

    @Override
    public void sendMessages() {

    }
}

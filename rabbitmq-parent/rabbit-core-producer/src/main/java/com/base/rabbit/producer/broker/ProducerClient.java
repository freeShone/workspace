package com.base.rabbit.producer.broker;

import com.base.rabbit.api.Message;
import com.base.rabbit.api.MessageProducer;
import com.base.rabbit.api.MessageType;
import com.base.rabbit.api.SendCallback;
import com.base.rabbit.api.exception.MessageRunTimeException;
import com.google.common.base.Preconditions;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * ProducerClient 发送消息的实际实现类
 */
@Component
public class ProducerClient implements MessageProducer {

    private RabbitBroker rabbitBroker;

    /**
     *
     * @param message
     * @throws MessageRunTimeException
     */
    @Override
    public void send(Message message) throws MessageRunTimeException {
        Preconditions.checkNotNull(message.getTopic());
        String messageType = message.getMessageType();
        switch (messageType){
            case MessageType.RAPID:
                rabbitBroker.rapidSend(message);
                break;
            case MessageType.CONFIRM:
                rabbitBroker.confirmSend(message);
                break;
            case MessageType.RELIANT:
                rabbitBroker.reliantSend(message);
                break;
            default:
                break;
        }

    }

    @Override
    public void send(List<Message> messages) throws MessageRunTimeException {

    }

    @Override
    public void send(Message message, SendCallback sendCallback) throws MessageRunTimeException {

    }
}

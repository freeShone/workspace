package com.base.rabbit.producer.broker;

import com.base.rabbit.api.Message;
import com.base.rabbit.api.MessageType;
import com.base.rabbit.producer.constant.BrokerMessageConst;
import com.base.rabbit.producer.constant.BrokerMessageStatus;
import com.base.rabbit.producer.entity.BrokerMessage;
import com.base.rabbit.producer.service.MessageStoreService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 真正发送消息的实现类
 */
@Component
@Slf4j
public class RabbitBrokerImpl implements RabbitBroker {

    @Autowired
    private RabbitTemplateContainer rabbitTemplateContainer;

    @Autowired
    private MessageStoreService messageStoreService;

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
            RabbitTemplate template = rabbitTemplateContainer.getTemplate(message);
            template.convertAndSend(message.getTopic(),message.getRoutingKey(),message,correlationData);
            log.info("#RabbitBrokerImpl.sendKernel# send to rabbitmq,messageId: {}",message.getMessageId());
        });
    }

    @Override
    public void confirmSend(Message message) {
        message.setMessageType(MessageType.CONFIRM);
        sendKernel(message);
    }

    @Override
    public void reliantSend(Message message) {
        //1.把数据库的消息发送日志记录
        Date  now = new Date();
        BrokerMessage brokerMessage = new BrokerMessage();
        brokerMessage.setMessageId(message.getMessageId());
        brokerMessage.setStatus(BrokerMessageStatus.SENDING.getCode());
        // tryCount 在最开始时不需要设置
        brokerMessage.setNextRetry(DateUtils.addMinutes(now, BrokerMessageConst.TIMEOUT));
        brokerMessage.setCreateTime(now);
        brokerMessage.setUpdateTime(now);
        brokerMessage.setMessage(message);
        messageStoreService.insert(brokerMessage);
        //2.执行真正发送逻辑
        sendKernel(message);


    }

    @Override
    public void sendMessages() {

    }
}

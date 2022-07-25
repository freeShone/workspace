package com.base.rabbit.producer.broker;

import com.base.rabbit.api.Message;
import com.base.rabbit.api.MessageType;
import com.base.rabbit.api.exception.MessageRunTimeException;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import com.rabbit.common.convert.GenericMessageConverter;
import com.rabbit.common.convert.RabbitMessageConverter;
import com.rabbit.common.serializer.Serializer;
import com.rabbit.common.serializer.SerializerFactory;
import com.rabbit.common.serializer.impl.JacksonSerializerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 池化RabbitTemplate封装
 * 1.每一个topic对应一个RabbitTemplate
 * 2.可以根据不同的需求制定不同的RabbitTemplate
 */
@Component
@Slf4j
public class RabbitTemplateContainer implements RabbitTemplate.ConfirmCallback{

    private SerializerFactory serializerFactory = JacksonSerializerFactory.INSTANCE;

    private Map<String, RabbitTemplate>  rabbitMap = Maps.newConcurrentMap();

    @Autowired
    private ConnectionFactory connectionFactory;

    private final Splitter splitter = Splitter.on("#");

    public RabbitTemplate getTemplate(Message message) throws MessageRunTimeException {
        Preconditions.checkNotNull(message);
        String topic = message.getTopic();
        RabbitTemplate rabbitTemplate = rabbitMap.get(topic);
        if(rabbitTemplate!=null){
            return rabbitTemplate;
        }
        log.info("#RabbitTemplateContainer.getTemplate# topic: {}",topic);
        RabbitTemplate newRabbitTemplate = new RabbitTemplate(connectionFactory);
        newRabbitTemplate.setExchange(topic);
        newRabbitTemplate.setRetryTemplate(new RetryTemplate());
        newRabbitTemplate.setRoutingKey(message.getRoutingKey());
        //添加序列化和反序列化对象
        GenericMessageConverter gmc = new GenericMessageConverter(serializerFactory.create());
        RabbitMessageConverter rmc = new RabbitMessageConverter(gmc);
        // 序列化
        newRabbitTemplate.setMessageConverter(rmc);
        String messageType = message.getMessageType();
        if(!MessageType.RAPID.equals(messageType)){
            newRabbitTemplate.setConfirmCallback(this);
        }
        rabbitMap.put(topic,newRabbitTemplate);
        return rabbitMap.get(topic);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        //具体的消息应答
        List<String> strings = splitter.splitToList(correlationData.getId());
        String messageId = strings.get(0);
        long sendTime = Long.parseLong(strings.get(1));
        if(ack){
            log.info("send message is OK, confirm messageId: {}, sendTime: {}",messageId,sendTime);
        }else{
            log.error("send message is Fail, confirm messageId: {}, sendTime: {}",messageId,sendTime);
        }
    }
}

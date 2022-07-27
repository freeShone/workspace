package com.base.rabbit.producer.service;

import com.base.rabbit.producer.constant.BrokerMessageStatus;
import com.base.rabbit.producer.entity.BrokerMessage;
import com.base.rabbit.producer.mapper.BrokerMessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class MessageStoreService {

    @Autowired
    private BrokerMessageMapper brokerMessageMapper;

    public void insert(BrokerMessage brokerMessage){

        brokerMessageMapper.insert(brokerMessage);
    }

    public void success(String messageId) {
        brokerMessageMapper.changeBrokerMessageStatus(messageId,
                BrokerMessageStatus.SEND_OK.getCode(),
                new Date());
    }
}

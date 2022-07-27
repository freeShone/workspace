package com.base.rabbit.producer.constant;

/**
 * 消息的发送状态
 */
public enum  BrokerMessageStatus {

    SENDING("0"),
    SEND_OK("1"),
    SEND_FAIL("2");

    private String code;

    private BrokerMessageStatus(String code){
        this.code = code;
    }

    public String getCode(){
        return this.code;
    }

}

package com.base.rabbit.api;

public final class MessageType {

    /**
     * 迅速消息：不需要保障消息的可靠性，也不需要做Confirm确认
     */
    public final static String RAPID = "0";

    /**
     * 确认消息：不需要保障消息的可靠性，但是会做消息的confirm确认
     */
    public final static String CONFIRM = "1";

    /**
     * 可靠性消息：一定保障消息的100%可靠性投递，不允许消息丢失
     * PS：保障数据库和所发的消息是原子性的
     */
    public final static String RELIANT = "2";
}

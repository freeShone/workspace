package com.rabbit.common.serializer;

/**
 * 序列化与反序列化接口
 */
public interface Serializer {

    byte[] serializeRaw(Object data);

    String serialize(Object data);

    <T>  T deserialize(String content);

    <T>  T deserialize(byte[] content);

}

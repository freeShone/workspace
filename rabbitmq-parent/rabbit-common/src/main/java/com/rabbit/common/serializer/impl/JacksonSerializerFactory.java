package com.rabbit.common.serializer.impl;

import com.base.rabbit.api.Message;
import com.rabbit.common.serializer.Serializer;
import com.rabbit.common.serializer.SerializerFactory;

public class JacksonSerializerFactory implements SerializerFactory {

    public static final JacksonSerializerFactory INSTANCE  =  new JacksonSerializerFactory();

    @Override
    public Serializer create() {
        return JacksonSerializer.createParametricType(Message.class);
    }
}

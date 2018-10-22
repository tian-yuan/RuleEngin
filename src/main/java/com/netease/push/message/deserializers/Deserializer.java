package com.netease.push.message.deserializers;


/**
 * Created by hzliuzebo on 2018/9/20.
 */
public interface Deserializer {
    /**
     * deserialize message to KafkaMessage
     * @param message
     * @return
     */
    public <T> T deserialize(Class<T> destinationClass, String message);
}

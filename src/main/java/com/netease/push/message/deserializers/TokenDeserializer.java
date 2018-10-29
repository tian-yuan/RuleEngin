package com.netease.push.message.deserializers;


import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by hzliuzebo on 2018/9/20.
 */
public class TokenDeserializer implements Deserializer{
    @Override
    public <T> T deserialize(Class<T> destinationClass, String message) {
        T result = (T) deserializeInternal(destinationClass, message);
        return result;
    }

    public Object deserializeInternal(Type valueType, String message) {
        // resolve a parameterized type to a class
        Class<?> valueClass = valueType instanceof Class<?> ? (Class<?>) valueType : null;
        if (valueType instanceof ParameterizedType) {
            valueClass = (Class<?>) ((ParameterizedType) valueType).getRawType();
        }
        // Void means skip
        if (valueClass == Void.class) {
            return null;
        }

        Object newInstance = TokenDeserializer.newInstance(valueClass);
        if (newInstance == null) {
            return null;
        }
        String[] items = message.split("\n");
//        String[] items = message.split(";");
        int itemsCount = items.length;
        for (Field field : valueClass.getDeclaredFields()) {
            Key key = field.getAnnotation(Key.class);
            if (key==null) {
                continue;
            }
            int index = key.value();
            if (index > itemsCount) {
                continue;
            }
            try {
                field.set(newInstance, items[index]);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return newInstance;
    }

    public static <T> T newInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
}

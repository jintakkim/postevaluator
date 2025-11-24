package com.jintakkim.postevaluator.test;

import java.lang.reflect.Field;

public final class TestRefectionUtils {
    private TestRefectionUtils() {}

    public static <O,V> void setField(Class<O> objectType, O object, String fieldNameToChange, V fieldValue) {
        try {
            Field modelsField = objectType.getDeclaredField(fieldNameToChange);
            modelsField.setAccessible(true);
            modelsField.set(object, fieldValue);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}

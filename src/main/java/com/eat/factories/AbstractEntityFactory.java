package com.eat.factories;

public abstract class AbstractEntityFactory {

    public abstract <T extends Object> T createInstance(String objectRaw, Class<T> clazz);

}
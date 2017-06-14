package com.eat.logic.b2c;


public interface EventablePool {

    void handleEvent();

    void addToPool(Poolable poolable);

    Poolable removeFromPool(Poolable poolable);

    Poolable removeFromPool(Long id);

    void dropPool();

    void updatePool();

}

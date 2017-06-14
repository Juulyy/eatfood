package com.eat.logic.suggesters;

import com.eat.logic.rankers.wrappers.Rankable;

import java.util.Set;

public interface Advice<T> {

    Set<Rankable<T>> getAdvices();

}

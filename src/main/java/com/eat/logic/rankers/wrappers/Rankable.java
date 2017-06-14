package com.eat.logic.rankers.wrappers;

import java.util.Comparator;

public interface Rankable<K> extends Comparator<Rankable>, Comparable<Rankable> {

    void setObjectToRank(K object);

    K getRankableObject();

    Double getRank();

    void increaseRank(Double rate);

    void decreaseRank(Double rate);

}

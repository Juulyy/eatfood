package com.eat.logic.rankers.wrappers;


import java.util.Comparator;

public interface RankablePair<K, V> extends Comparator<RankablePair<K, V>> {

    void setFirst(K firstObject);

    K getFirst();

    void setSecond(V secondObject);

    V getSecond();

    Double getRank();

    void setRank(Double rank);

    void calculateRank();

    void increaseRank(Double rate);

    void decreaseRank(Double rate);

}

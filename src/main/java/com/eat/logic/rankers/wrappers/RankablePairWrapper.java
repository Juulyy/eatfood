package com.eat.logic.rankers.wrappers;

public class RankablePairWrapper<K, V> implements RankablePair<K, V>{

    private K first;
    private V second;
    private Double rank;

    @Override
    public void setFirst(K firstObject) {
        this.first = firstObject;
    }

    @Override
    public K getFirst() {
        return first;
    }

    @Override
    public void setSecond(V secondObject) {
        this.second = secondObject;
    }

    @Override
    public V getSecond() {
        return second;
    }

    @Override
    public Double getRank() {
        return rank;
    }

    @Override
    public void setRank(Double rank) {
        this.rank = rank;
    }


    @Override
    public void calculateRank() {

    }

    @Override
    public void increaseRank(Double rate) {

    }

    @Override
    public void decreaseRank(Double rate) {

    }

    @Override
    public int compare(RankablePair<K, V> o1, RankablePair<K, V> o2) {
        return 0;
    }
}

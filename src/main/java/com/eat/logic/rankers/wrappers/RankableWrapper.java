package com.eat.logic.rankers.wrappers;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

import java.math.BigDecimal;

@EqualsAndHashCode
@Builder(builderMethodName = "of", buildMethodName = "create")
public class RankableWrapper<K> implements Rankable<K> {

    private K rankedObject;

    private Double rank;

    @Override
    public void setObjectToRank(K object) {
        rankedObject = object;
    }

    @Override
    public K getRankableObject() {
        return rankedObject;
    }

    @Override
    public Double getRank() {
        return rank;
    }

    @Override
    public void increaseRank(Double rate) {
        this.rank += rate;
    }

    @Override
    public void decreaseRank(Double rate) {
        this.rank -= rate;
    }

    @Override
    public int compareTo(Rankable obj) {
        return DefaultTypeTransformation.compareTo(this.getRank(), obj.getRank());
    }

    @Override
    public int compare(Rankable fstObj, Rankable sndObj) {
        return DefaultTypeTransformation.compareTo(fstObj.getRank(), sndObj.getRank());
    }

}
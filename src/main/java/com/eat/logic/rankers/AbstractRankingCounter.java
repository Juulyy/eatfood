package com.eat.logic.rankers;

import com.eat.logic.rankers.wrappers.Rankable;
import com.eat.models.common.Tag;
import com.eat.models.common.enums.TagType;
import com.eat.models.recommender.RankingRate;
import com.eat.models.recommender.RankingType;

import java.math.BigDecimal;
import java.util.List;

public abstract class AbstractRankingCounter<T> implements RankingCounter<T> {

    private RankingCounter nextRankerCounter;

    private List<RankingRate> rankingRates;

    public AbstractRankingCounter() {
    }

    public AbstractRankingCounter(List<RankingRate> rankingRates) {
        this.rankingRates = rankingRates;
    }

    @Override
    public void setNextCounter(RankingCounter counter) {
        this.nextRankerCounter = counter;
    }

    @Override
    public RankingCounter getNextCounter() {
        return nextRankerCounter;
    }

    public void proceed(Rankable<T> rankable){
        if(nextRankerCounter != null){
            nextRankerCounter.countRank(rankable);
        }
    }

    @Override
    public BigDecimal getTagRankingRate(Tag tag) {
        TagType tagType = tag.getType();

        BigDecimal tagRate;
        if(tagType == TagType.ATMOSPHERE){
            tagRate = getRateByType(RankingType.ATMOSPHERE);
        }else if(tagType == TagType.CUISINE){
            tagRate = getRateByType(RankingType.CUISINE);
        }else if(tagType == TagType.FEATURE){
            tagRate = getRateByType(RankingType.FEATURE);
        }else if(tagType == TagType.INTERIOR){
            tagRate = getRateByType(RankingType.INTERIOR_DESIGN);
        }else if(tagType == TagType.MENU_ITEM){
            tagRate = getRateByType(RankingType.MENU_ITEM);
        }else if(tagType == TagType.MUSIC){
            tagRate = getRateByType(RankingType.MUSIC);
        }else{
            tagRate = BigDecimal.ZERO;
        }

        return tagRate;
    }


    @Override
    public BigDecimal getRateByType(RankingType rankingType){

        List<RankingRate> rankingRates = this.rankingRates;
        for (RankingRate rankingRate : rankingRates) {
            if(rankingRate.getType() == rankingType){
                return rankingRate.getRate();
            }
        }

        return BigDecimal.ZERO;
    }

}

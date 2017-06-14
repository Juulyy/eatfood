package com.eat.logic.rankers;

import com.eat.logic.suggesters.AdviceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RankerCounterChainFactory {

    @Autowired
    private RankRatesHolder rankRatesHolder;

    public RankingCounter getRankerChain(AdviceType chainType){
        if(chainType == AdviceType.PLACE){
            return setUpPlaceRankerChain();
        }else if(chainType == AdviceType.OFFER){
            return setUpOfferRankerChain();
        }else {
            return null;
        }
    }

    private RankingCounter setUpOfferRankerChain() {

        RankingCounter tagsBasedOfferRanker = new TagsBasedOfferRanker(rankRatesHolder.getTagsRates());
        tagsBasedOfferRanker.setNextCounter(new FollowBasedOfferRanker(rankRatesHolder.getFollowRates()));

        return tagsBasedOfferRanker;
    }

    private RankingCounter setUpPlaceRankerChain() {

        RankingCounter tagsBasedPlaceRanker = new TagsBasedPlaceRanker(rankRatesHolder.getTagsRates());
        tagsBasedPlaceRanker.setNextCounter(new FollowBasedPlaceRanker(rankRatesHolder.getFollowRates()));

        return tagsBasedPlaceRanker;
    }
}

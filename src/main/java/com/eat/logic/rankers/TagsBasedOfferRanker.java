package com.eat.logic.rankers;

import com.eat.logic.rankers.wrappers.Rankable;
import com.eat.models.b2b.Offer;
import com.eat.models.recommender.RankingRate;

import java.util.List;

public class TagsBasedOfferRanker extends AbstractRankingCounter<Offer>{

    public TagsBasedOfferRanker(List<RankingRate> rankingRates) {
        super(rankingRates);
    }

    @Override
    public void countRank(Rankable<Offer> rankable) {

    }
}

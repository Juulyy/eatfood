package com.eat.logic.rankers;


import com.eat.logic.rankers.wrappers.Rankable;
import com.eat.models.common.Tag;
import com.eat.models.recommender.RankingType;

import java.math.BigDecimal;

public interface RankingCounter<T> {

    void setNextCounter(RankingCounter counter);

    RankingCounter getNextCounter();

    void countRank(Rankable<T> rankable);

    BigDecimal getTagRankingRate(Tag tag);

    BigDecimal getRateByType(RankingType rankingType);
}
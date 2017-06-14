package com.eat.logic.suggesters;


import com.eat.logic.rankers.wrappers.RankableWrapper;
import com.eat.logic.rankers.RankerCounterChainFactory;
import com.eat.logic.rankers.RankingCounter;
import com.eat.models.b2b.Place;
import com.eat.models.b2c.AppUser;
import com.eat.services.b2b.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Component
@Scope(value = "prototype")
public class PlaceSuggester implements Suggester {

    @Autowired
    private RankerCounterChainFactory counterChainFactory;

    @Autowired
    private PlaceService placeService;

    @Override
    public Advice<Place> makeSuggestion(AppUser user) {

        RankingCounter rankerChain = counterChainFactory.getRankerChain(AdviceType.PLACE);

        Set<RankableWrapper<Place>> advices = new TreeSet<>();
        for (Place currentPlace : getPlaces()) {

            RankableWrapper<Place> rankablePlace =
                    RankableWrapper.<Place>of()
                            .rankedObject(currentPlace)
                            .rank(Double.valueOf(0))
                            .create();

            rankerChain.countRank(rankablePlace);

            advices.add(rankablePlace);
        }

        return UserAdvice.<Place>of()
                .user(user)
                .advices(advices)
                .create();
    }

    private List<Place> getPlaces() {
        List<Place> places = placeService.findAll();
        return places;
    }
}
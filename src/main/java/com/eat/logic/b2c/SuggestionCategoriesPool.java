package com.eat.logic.b2c;

import com.eat.logic.rankers.wrappers.Rankable;
import com.eat.logic.rankers.wrappers.RankablePair;
import com.eat.models.b2b.Place;
import com.eat.models.recommender.SuggestionCategory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class SuggestionCategoriesPool implements EventablePool {

    private static ConcurrentHashMap<Long, Poolable> suggestionCategories = new ConcurrentHashMap<>();

    private static ConcurrentHashMap<Long, List<RankablePair<Rankable<SuggestionCategory>, List<Rankable<Place>>>>> cachedUserResults = new ConcurrentHashMap<>();

    @Override
    public void handleEvent() {

    }

    @Override
    public void addToPool(Poolable poolable) {

    }

    @Override
    public Poolable removeFromPool(Poolable poolable) {
        return null;
    }

    @Override
    public Poolable removeFromPool(Long id) {
        return null;
    }

    @Override
    public void dropPool() {

    }

    @Override
    public void updatePool() {

    }
}

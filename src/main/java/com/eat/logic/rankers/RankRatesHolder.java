package com.eat.logic.rankers;

import com.eat.models.recommender.RankingRate;
import com.eat.services.recommender.RankingRateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class RankRatesHolder {

    @Autowired
    private RankingRateService rateService;

    private List<RankingRate> rankingRates;

    @PostConstruct
    private void initialize(){
        try {
            rankingRates = rateService.findAll();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public List<RankingRate> getTagsRates(){
        List<RankingRate> tagsRates = new ArrayList<>();

        for (RankingRate rankingRate : rankingRates) {
            if(rankingRate.getType().isTagType()){
                tagsRates.add(rankingRate);
            }
        }

        return tagsRates;
    }

    public List<RankingRate> getFollowRates(){
        List<RankingRate> tagsRates = new ArrayList<>();

        for (RankingRate rankingRate : rankingRates) {
            if(rankingRate.getType().isFollowType()){
                tagsRates.add(rankingRate);
            }
        }

        return tagsRates;
    }

    public List<RankingRate> getCollectionRates(){
        List<RankingRate> tagsRates = new ArrayList<>();

        for (RankingRate rankingRate : rankingRates) {
            if(rankingRate.getType().isFollowType()){
                tagsRates.add(rankingRate);
            }
        }

        return tagsRates;
    }
}
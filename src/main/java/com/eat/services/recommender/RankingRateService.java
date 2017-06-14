package com.eat.services.recommender;

import com.eat.models.recommender.RankingRate;
import com.eat.models.recommender.RankingType;
import com.eat.repositories.sql.recommender.RankingRateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class RankingRateService {

    @Autowired
    private RankingRateRepository repository;

    public RankingRate findById(Long id) {
        return repository.findOne(id);
    }

    public RankingRate save(RankingRate rankingRate) {
        return repository.save(rankingRate);
    }

    public List<RankingRate> saveAll(List<RankingRate> rankingRates) {
        return repository.save(rankingRates);
    }

    public RankingRate update(RankingRate rankingRate) {
        return repository.save(rankingRate);
    }

    public void delete(RankingRate rankingRate) {
        repository.delete(rankingRate);
    }

    public void delete(Long id) {
        repository.delete(id);
    }

    public List<RankingRate> findAll() {
        return repository.findAll();
    }

    public List<RankingRate> getRatesByType(List<RankingType> rankingTypes){
        return null; //repository.findRankingRateByTypes(rankingTypes);
    }
}

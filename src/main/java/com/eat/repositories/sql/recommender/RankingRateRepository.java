package com.eat.repositories.sql.recommender;

import com.eat.models.recommender.RankingRate;
import com.eat.models.recommender.RankingType;
import com.eat.repositories.sql.b2b.BaseB2BRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RankingRateRepository extends BaseB2BRepository<RankingRate, Long> {

    @Query("select r from RankingRate r where r.type in (:types)")
    List<RankingRate> findRankingRateByTypes(@Param("types") List<RankingType> rankingTypes);

}

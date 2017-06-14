package com.eat.controllers.recommender;

import com.eat.models.recommender.RankingRate;
import com.eat.services.recommender.RankingRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/ranking-rate")
public class RankingRateController {

    @Autowired
    private RankingRateService service;

    @GetMapping(value = "/{id}", produces = "application/json")
    public RankingRate getRankingRate(@PathVariable Long id){
        return service.findById(id);
    }

    @GetMapping(value = "/", produces = "application/json")
    public List<RankingRate> getRankingRates(){
        return service.findAll();
    }

    @PostMapping(value = "/", consumes = "application/json", produces = "application/json")
    public RankingRate addRankingRate(@RequestBody RankingRate rankingRate) {
        return service.save(rankingRate);
    }

    @PatchMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public RankingRate updateRankingRate(@PathVariable Long id, @RequestBody RankingRate rankingRate) {
        return service.update(rankingRate);
    }

    @DeleteMapping(value = "/{id}")
    public HttpStatus deleteRankingRate(@PathVariable Long id) {
        service.delete(id);
        return HttpStatus.OK;
    }
}

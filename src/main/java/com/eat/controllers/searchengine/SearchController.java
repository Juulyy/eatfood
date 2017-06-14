package com.eat.controllers.searchengine;

import com.eat.models.elasticsearch.ESRankablePlace;
import com.eat.services.elasticsearch.ESRankablePlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    @Autowired
    private ESRankablePlaceService placeService;

    @GetMapping(value = "/places", produces = "application/json")
    public Iterable<ESRankablePlace> getPlaces(@RequestParam("name") String name) {
        return placeService.getPlacesNameMatching(name);
    }
}


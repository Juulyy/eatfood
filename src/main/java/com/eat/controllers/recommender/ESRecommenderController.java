package com.eat.controllers.recommender;


import com.eat.models.b2b.Place;
import com.eat.models.b2c.AppUser;
import com.eat.services.elasticsearch.recommender.ESRecommenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/es-recommendations")
public class ESRecommenderController {

    @Autowired
    private ESRecommenderService esRecommenderService;

    @RequestMapping(value = "/simple-match-recommendations", method = RequestMethod.GET)
    @ResponseBody
    public Map<Long, Double> simpleMatch(@RequestParam long id) {

        Map<Place, Double> places = esRecommenderService.getSimpleMatchRecommendations(id);
        Map<Long, Double> result = new HashMap<>();

        for (Map.Entry<Place, Double> entry: places.entrySet()) {
            result.put(entry.getKey().getId(), entry.getValue());
        }
        return result;
    }

    @RequestMapping(value = "/similar-users", method = RequestMethod.GET)
    @ResponseBody
    public Map<Long, Double> similarUsers(@RequestParam long id) {

        Map<AppUser, Double> places = esRecommenderService.getSimilarUsers(id);
        Map<Long, Double> result = new HashMap<>();

        for (Map.Entry<AppUser, Double> entry: places.entrySet()) {
            result.put(entry.getKey().getId(), entry.getValue());
        }
        return result;
    }

    @RequestMapping(value = "/similar-places", method = RequestMethod.GET)
    @ResponseBody
    public Map<Long, Double> similarPlaces(@RequestParam long id) {

        Map<Place, Double> places = esRecommenderService.getSimilarPlaces(id);
        Map<Long, Double> result = new HashMap<>();

        for (Map.Entry<Place, Double> entry: places.entrySet()) {
            result.put(entry.getKey().getId(), entry.getValue());
        }
        return result;
    }

    @RequestMapping(value = "/simple-match-places", method = RequestMethod.GET)
    @ResponseBody
    public Map<Long, Double> simpleMatchPlaces(@RequestParam long id) {

        Map<Place, Double> places = esRecommenderService.getSimpleMatchPlaces(id);
        Map<Long, Double> result = new HashMap<>();

        for (Map.Entry<Place, Double> entry: places.entrySet()) {
            result.put(entry.getKey().getId(), entry.getValue());
        }
        return result;
    }

    @RequestMapping(value = "/recommended-places", method = RequestMethod.GET)
    @ResponseBody
    public Map<Long, Double> recommendedPlaces(@RequestParam long id) {

        Map<Place, Double> places = esRecommenderService.getRecommendedPlaces(id);
        Map<Long, Double> result = new HashMap<>();

        for (Map.Entry<Place, Double> entry: places.entrySet()) {
            result.put(entry.getKey().getId(), entry.getValue());
        }
        return result;
    }

    @RequestMapping(value = "/recompute", method = RequestMethod.POST)
    @ResponseBody
    public void recompute() {
        esRecommenderService.recomputeAll();
    }
}

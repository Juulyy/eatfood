package com.eat.services.elasticsearch.match;

import com.eat.models.elasticsearch.ESPlace;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ESMatchService {

    @JsonIgnore
    /** Unsorted result */
    private Map<Long, Integer> placesRating;

    @JsonProperty
    @Getter
    /** Sorted result */
    private Map<Long, Integer> result;

    public ESMatchService() {
        placesRating = new HashMap<>();
        result = new LinkedHashMap<>();
    }

    /**
     * This method is used to count unique merchants
     * @param places list of merchants
     */
    public void addMerchants(List<ESPlace> places) {
        for (ESPlace place : places) {
            if (!placesRating.containsKey(place.getId())) {
                placesRating.put(place.getId(), 1);
            } else {
                Integer count = placesRating.get(place.getId());
                placesRating.put(place.getId(), ++count);
            }
        }
        result.clear();
        placesRating.entrySet().stream()
                .sorted(Map.Entry.<Long, Integer>comparingByValue().reversed())
                .forEachOrdered(x -> result.put(x.getKey(), x.getValue()));
    }
}

package com.eat.controllers.recommender;

import com.eat.dto.recommender.SuggestionCategoryDto;
import com.eat.logic.b2c.SuggestionCategoryPool;
import com.eat.logic.manager.InheritanceTagRemover;
import com.eat.logic.rankers.wrappers.Rankable;
import com.eat.models.b2b.Place;
import com.eat.models.mongo.enums.WeatherIcon;
import com.eat.models.recommender.SuggestionCategory;
import com.eat.utils.converters.dto.SuggestionCategoryDtoConverter;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/suggestion")
public class SuggestionController {

    @Autowired
    private SuggestionCategoryPool categoryPool;

    @Autowired
    private SuggestionCategoryDtoConverter categoryDtoConverter;

    @PutMapping(value = "/update-category-pool", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateCategoryPool() {
        categoryPool.updateSuggestionCategories();

        return ResponseEntity.ok("Suggestion category pool successfully updated");
    }

    @GetMapping(value = "/get-all-categories", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Pair<Rankable<SuggestionCategory>, List<Rankable<Place>>>> getUserCategories() {

        List<Pair<Rankable<SuggestionCategory>, List<Rankable<Place>>>> relevantCategoriesForUser = categoryPool.getAllCategories();
        return relevantCategoriesForUser;
    }

    @GetMapping(value = "/all-categories", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<SuggestionCategoryDto> showAllCategoriesDto() {
        List<Pair<Rankable<SuggestionCategory>, List<Rankable<Place>>>> allCategories = categoryPool.getAllCategories();
        List<SuggestionCategoryDto> dtoList = categoryDtoConverter.toSuggestionCategoryDtoList(allCategories);
        dtoList.stream()
                .map(SuggestionCategoryDto::getTags)
                .flatMap(Collection::stream)
                .forEach(InheritanceTagRemover::removeTagInheritance);
        return dtoList;
    }

    @GetMapping(value = "/get-user-categories", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Pair<Rankable<SuggestionCategory>, List<Rankable<Place>>>> getUserCategories(@RequestParam("userId") Long userId,
                                                                                             @RequestParam(value = "category_number",
                                                                                                     required = false) Integer categoryNumber,
                                                                                             LocalDateTime dateTime, Double temp, WeatherIcon icon) {

        List<Pair<Rankable<SuggestionCategory>, List<Rankable<Place>>>> relevantCategoriesForUser =
                categoryPool.getRelevantCategoriesForUserWithFilters(userId,
                        categoryNumber == null ? 6 : categoryNumber, null, dateTime, temp, icon);

        return relevantCategoriesForUser;
    }

    @GetMapping(value = "/get-user-categories-with-geo", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Pair<Rankable<SuggestionCategory>, List<Rankable<Place>>>> getUserCategoriesWithFilters(@RequestParam("userId") Long userId,
                                                                                                        @RequestParam("lat") Double lat,
                                                                                                        @RequestParam("lon") Double lon,
                                                                                                        @RequestParam("radius") Double radius,
                                                                                                        @RequestParam(value = "time",
                                                                                                                required = false) LocalDateTime dateTime,
                                                                                                        @RequestParam(value = "category_number",
                                                                                                                required = false) Integer categoryNumber,
                                                                                                        Double temp, WeatherIcon weatherIcon) {

        List<Pair<Rankable<SuggestionCategory>, List<Rankable<Place>>>> relevantCategoriesForUser =
                categoryPool.getRelevantCategoriesForUserWithFilters(userId, categoryNumber == null ? 6 : categoryNumber,
                        new Circle(new Point(lat, lon), new Distance(radius, Metrics.KILOMETERS)), dateTime == null ? LocalDateTime.now() : dateTime, temp, weatherIcon);

        return relevantCategoriesForUser;
    }

}
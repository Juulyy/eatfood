package com.eat.dto.recommender;

import com.eat.models.common.Image;
import com.eat.models.common.Tag;
import com.eat.models.recommender.DayTimePeriod;
import com.eat.models.recommender.SuggestionCategoryDatePeriod;
import com.eat.models.recommender.SuggestionCategoryTagRate;
import com.eat.models.recommender.WeatherOption;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.Set;

@Data
@Builder(builderMethodName = "of", buildMethodName = "create")
public class SuggestionCategoryDto {

    @NonNull
    @JsonProperty(value = "id")
    private Long id;

    @JsonProperty(value = "description")
    private String description;

    @JsonProperty(value = "useDayTimePeriod")
    private boolean useDayTimePeriod;

    @JsonProperty(value = "dayTimes")
    private Set<DayTimePeriod> dayTimes;

    @JsonProperty(value = "useDatePeriod")
    private boolean useDatePeriod;

    @JsonProperty(value = "dates")
    private Set<SuggestionCategoryDatePeriod> dates;

    @JsonProperty(value = "useWeatherParam")
    private boolean useWeatherParam;

    @JsonProperty(value = "weatherOptions")
    private Set<WeatherOption> weatherOptions;

    @JsonProperty(value = "useTagFilter")
    private boolean useTagFilter;

    @JsonProperty(value = "image")
    private Image image;

    @JsonProperty(value = "tags")
    private Set<Tag> tags;

    @JsonProperty(value = "tagRates")
    private Set<SuggestionCategoryTagRate> tagRates;

    /*@JsonProperty(value = "placePoolSize")
    private Integer placePoolSize;*/

    @JsonProperty(value = "minCuratorRecommendations")
    private Integer minCuratorRecommendations;

    @JsonProperty(value = "placeNumber")
    private Integer placeNumber;

    /*@JsonProperty(value = "categoryRank")
    private Double categoryRank;*/

    /*@JsonProperty(value = "places")
    private List<PlaceDto> places;*/

}
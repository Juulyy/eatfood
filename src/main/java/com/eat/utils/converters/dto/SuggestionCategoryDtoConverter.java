package com.eat.utils.converters.dto;

import com.eat.dto.recommender.SuggestionCategoryDto;
import com.eat.logic.rankers.wrappers.Rankable;
import com.eat.logic.rankers.wrappers.RankablePlaceWrapper;
import com.eat.logic.rankers.wrappers.RankableSuggestionCategoryWrapper;
import com.eat.models.b2b.Place;
import com.eat.models.recommender.SuggestionCategory;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class SuggestionCategoryDtoConverter {

    @Autowired
    private PlaceDtoConverter placeConverter;

    public List<SuggestionCategoryDto> toSuggestionCategoryDtoList(List<Pair<Rankable<SuggestionCategory>,
            List<Rankable<Place>>>> categories) {

        return categories.stream()
                .map(rankableListPair -> toDto(rankableListPair.getKey(), rankableListPair.getValue()))
                .collect(Collectors.toList());
    }

    public SuggestionCategoryDto toDto(Rankable<SuggestionCategory> categoryWrapper, List<Rankable<Place>> places) {

        return SuggestionCategoryDto.of()
                .id(categoryWrapper.getRankableObject().getId())
                .description(categoryWrapper.getRankableObject().getDescription())
                .dayTimes(categoryWrapper.getRankableObject().getDayTimes())
                .useDatePeriod(categoryWrapper.getRankableObject().isUseDatePeriod())
                .dates(categoryWrapper.getRankableObject().getDates())
                .useWeatherParam(categoryWrapper.getRankableObject().isUseWeatherParam())
                .weatherOptions(categoryWrapper.getRankableObject().getWeatherOptions())
                .useTagFilter(categoryWrapper.getRankableObject().isUseTagFilter())
                .image(categoryWrapper.getRankableObject().getImage())
                .tags(categoryWrapper.getRankableObject().getTags())
                .tagRates(categoryWrapper.getRankableObject().getTagRates())
//                .placePoolSize(categoryWrapper.getRankableObject().getPlacePoolSize())
                .minCuratorRecommendations(categoryWrapper.getRankableObject().getMinCuratorRecommendations())
                .placeNumber(places.size())
//                .categoryRank(categoryWrapper.getRank())
//                .places(places.stream()
//                        .map(placeConverter::toPlaceDto)
//                        .collect(Collectors.toList()))
                .create();

    }


    public SuggestionCategoryDto toDto(RankableSuggestionCategoryWrapper categoryWrapper,
                                       List<RankablePlaceWrapper> places) {

        return SuggestionCategoryDto.of()
                .id(categoryWrapper.getCategory().getId())
                .description(categoryWrapper.getCategory().getDescription())
                .dayTimes(categoryWrapper.getCategory().getDayTimes())
                .useDatePeriod(categoryWrapper.getCategory().isUseDatePeriod())
                .dates(categoryWrapper.getCategory().getDates())
                .useWeatherParam(categoryWrapper.getCategory().isUseWeatherParam())
                .weatherOptions(categoryWrapper.getCategory().getWeatherOptions())
                .useTagFilter(categoryWrapper.getCategory().isUseTagFilter())
                .image(categoryWrapper.getCategory().getImage())
                .tags(categoryWrapper.getCategory().getTags())
                .tagRates(categoryWrapper.getCategory().getTagRates())
//                .placePoolSize(categoryWrapper.getCategory().getPlacePoolSize())
                .minCuratorRecommendations(categoryWrapper.getCategory().getMinCuratorRecommendations())
                .placeNumber(places.size())
//                .categoryRank(categoryWrapper.getRank())
//                .places(places.stream()
//                        .map(placeConverter::toPlaceDto)
//                        .collect(Collectors.toList()))
                .create();
    }

}
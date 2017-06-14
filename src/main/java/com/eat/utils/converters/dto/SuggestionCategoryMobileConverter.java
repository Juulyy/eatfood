package com.eat.utils.converters.dto;

import com.eat.dto.recommender.SuggestionCategoryMobileDto;
import com.eat.logic.rankers.wrappers.Rankable;
import com.eat.models.b2b.Place;
import com.eat.models.recommender.SuggestionCategory;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class SuggestionCategoryMobileConverter {

    @Autowired
    private PlaceMobileConverter placeMobConverter;

    public List<SuggestionCategoryMobileDto> toSuggestionCategoryMobileDtoList
            (List<Pair<Rankable<SuggestionCategory>, List<Rankable<Place>>>> list) {
        return list.stream()
                .filter(Objects::nonNull)
                .map(this::toSuggestionCategoryMobileDto).collect(Collectors.toList());
    }

    public SuggestionCategoryMobileDto toSuggestionCategoryMobileDto
            (Pair<Rankable<SuggestionCategory>, List<Rankable<Place>>> categoryPlaces) {
        SuggestionCategory rankableObject = categoryPlaces.getLeft().getRankableObject();
        return SuggestionCategoryMobileDto.of()
                .id(rankableObject.getId())
                .image(rankableObject.getImage())
                .name(rankableObject.getDescription())
                .places(placeMobConverter.toPlaceMobileDtoList(categoryPlaces.getRight()))
                .create();
    }

}
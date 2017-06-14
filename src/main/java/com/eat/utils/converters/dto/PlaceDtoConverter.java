package com.eat.utils.converters.dto;

import com.eat.dto.b2b.PlaceDto;
import com.eat.logic.rankers.wrappers.Rankable;
import com.eat.logic.rankers.wrappers.RankablePlaceWrapper;
import com.eat.models.b2b.Place;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class PlaceDtoConverter {

    public List<PlaceDto> toPlaceDtoList(List<Place> places) {
        return places.stream()
                .map(this::toPlaceDto)
                .collect(Collectors.toList());
    }

    public List<PlaceDto> fromWrapperToPlaceDtoList(List<RankablePlaceWrapper> places) {
        return places.stream()
                .map(this::toPlaceDto)
                .collect(Collectors.toList());
    }

    public List<PlaceDto> fromRankableToPlaceDtoList(List<Rankable<Place>> places) {
        return places.stream()
                .map(this::toPlaceDto)
                .collect(Collectors.toList());
    }

    public PlaceDto toPlaceDto(Place place) {
        return PlaceDto.of()
                .id(place.getId())
                .isActive(place.getIsActive())
                .name(place.getName())
                .businessPlan(place.getBusinessPlan())
                .b2bAdminLoginNames(getFakeB2bAdminLoginNames())
                .creationDate(place.getCreationDate())
                .usersRecCount(0)
                .curatorsRecCount(0)
                .visitsCount(0)
                .create();
    }

    public PlaceDto toPlaceDto(RankablePlaceWrapper placeWrapper) {
        return PlaceDto.of()
                .id(placeWrapper.getPlace().getId())
                .isActive(placeWrapper.getPlace().getIsActive())
                .name(placeWrapper.getPlace().getName())
                .businessPlan(placeWrapper.getPlace().getBusinessPlan())
                .b2bAdminLoginNames(getFakeB2bAdminLoginNames())
                .creationDate(placeWrapper.getPlace().getCreationDate())
                .rank(placeWrapper.getRank())
                .usersRecCount(0)
                .curatorsRecCount(0)
                .visitsCount(0)
                .create();
    }

    public PlaceDto toPlaceDto(Rankable<Place> placeWrapper) {
        return PlaceDto.of()
                .id(placeWrapper.getRankableObject().getId())
                .isActive(placeWrapper.getRankableObject().getIsActive())
                .name(placeWrapper.getRankableObject().getName())
                .businessPlan(placeWrapper.getRankableObject().getBusinessPlan())
                .b2bAdminLoginNames(getFakeB2bAdminLoginNames())
                .creationDate(placeWrapper.getRankableObject().getCreationDate())
                .rank(placeWrapper.getRank())
                .usersRecCount(0)
                .curatorsRecCount(0)
                .visitsCount(0)
                .create();
    }

    private Set<String> getFakeB2bAdminLoginNames() {
        return Stream.of("B2BAdmin1@gmail.com", "B2BAdmin2@gmail.com")
                .collect(Collectors.toSet());
    }

}
package com.eat.utils.converters.dto;

import com.eat.dto.b2b.PlaceMobileDto;
import com.eat.logic.rankers.wrappers.Rankable;
import com.eat.models.b2b.Place;
import com.eat.models.b2b.PlaceDetail;
import com.eat.models.b2b.enums.PlaceDetailType;
import com.eat.models.common.Image;
import com.eat.models.common.enums.ImageType;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Component
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class PlaceMobileConverter {

    public List<PlaceMobileDto> toPlaceMobileDtoList(List<Rankable<Place>> places) {
        return places.stream()
                .map(this::toPlaceMobileDto)
                .collect(Collectors.toList());
    }

    public PlaceMobileDto toPlaceMobileDto(Rankable<Place> place) {
        Place rankableObject = place.getRankableObject();
        return PlaceMobileDto.of()
                .id(rankableObject.getId())
                .image(getCategoryImage(rankableObject))
                .name(rankableObject.getName())
                .placeType(getFirstPlaceType(rankableObject))
                .priceLevel(rankableObject.getPriceLevel())
                .longtitude(rankableObject.getLongtitude())
                .latitude(rankableObject.getLatitude())
                .create();
    }

    private Image getCategoryImage(Place place) {
        if (CollectionUtils.isEmpty(place.getImages())) {
            return null;
        }
        Optional<Image> optional = place.getImages().stream()
                .filter(image -> findPaidCategoryImages.or(findFreeCategoryImages).test(image))
                .findFirst();
        return optional.orElse(null);
    }

    private Predicate<Image> findPaidCategoryImages = image -> image.getType().equals(ImageType.PAID_PLACE_CATEGORY);

    private Predicate<Image> findFreeCategoryImages = image -> image.getType().equals(ImageType.FREE_PLACE_CATEGORY);

    private PlaceDetail getFirstPlaceType(Place place) {
        return place.getPlaceDetails().stream()
                .filter(placeDetail -> placeDetail.getPlaceDetailType().equals(PlaceDetailType.PLACE_TYPE))
                .findFirst()
                .orElseGet(emptyPlaceDetailSupplier);
    }

    private Supplier<PlaceDetail> emptyPlaceDetailSupplier = () -> PlaceDetail.of()
            .name("Empty")
            .placeDetailType(PlaceDetailType.PLACE_TYPE)
            .create();

}
package com.eat.utils.converters;

import com.eat.models.b2b.Place;
import com.eat.models.b2b.PlaceDetail;
import com.eat.models.b2b.enums.PlaceDetailType;
import com.eat.models.common.Tag;
import com.eat.models.elasticsearch.ESRankablePlace;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class PlaceConverter {

    public static ESRankablePlace toSearchablePlace(Place place) {
        if (place.getId() == null) {
            return null;
        }

        return ESRankablePlace.of()
                .id(place.getId().toString())
                .name(place.getName())
                .location(new GeoPoint(place.getLatitude(), place.getLongtitude()))
                .placeTypes(collectPlaceDetailsByType(place, PlaceDetailType.PLACE_TYPE))
                .cuisines(collectPlaceDetailsByType(place, PlaceDetailType.CUISINE))
                .atmospheres(collectPlaceDetailsByType(place, PlaceDetailType.ATMOSPHERE))
                .musics(collectPlaceDetailsByType(place, PlaceDetailType.MUSIC))
                .interiors(collectPlaceDetailsByType(place, PlaceDetailType.INTERIOR))
                .features(collectPlaceDetailsByType(place, PlaceDetailType.FEATURE))
                .additionalOptions(collectPlaceDetailsByType(place, PlaceDetailType.OPTION))
                .placeDetailTags(collectPlaceDetailTags(place))
                .menuItemsTags(collectMenuItemsTags(place))
                .create();
    }

    private static Set<String> collectPlaceDetailsByType(Place place, PlaceDetailType type) {
        return place.getPlaceDetails().stream()
                .map(Objects::requireNonNull)
                .filter(detail -> detail.isFittedPlaceType(type))
                .map(PlaceDetail::getName)
                .collect(Collectors.toCollection(HashSet::new));
    }

    private static Set<String> collectPlaceDetailTags(Place place) {
        return place.getTags().stream()
                .map(Objects::requireNonNull)
                .filter(Tag::isPlaceDetailTag)
                .map(Tag::getName)
                .collect(Collectors.toCollection(HashSet::new));
    }

    private static Set<String> collectMenuItemsTags(Place place) {
        return place.getMenus().stream()
                .map(Objects::requireNonNull)
                .flatMap(menu -> menu.getMenuItems().stream())
                .map(Objects::requireNonNull)
                .flatMap(menuItem -> menuItem.getTags().stream())
                .map(Objects::requireNonNull)
                .map(Tag::getName)
                .collect(Collectors.toCollection(HashSet::new));
    }

}
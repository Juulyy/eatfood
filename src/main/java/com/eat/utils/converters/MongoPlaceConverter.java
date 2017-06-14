package com.eat.utils.converters;

import com.eat.models.b2b.Place;
import com.eat.models.b2b.enums.PlaceDetailType;
import com.eat.models.common.Tag;
import com.eat.models.mongo.MongoDayOpenTime;
import com.eat.models.mongo.MongoPlace;
import com.eat.services.b2b.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class MongoPlaceConverter {

    @Autowired
    private PlaceService placeService;

    public MongoPlace toMongoPlace(Place place) {
        MongoPlace mongoPlace = MongoPlace.of()
                .sqlEntityId(place.getId())
                .location(new double[]{place.getLatitude(), place.getLongtitude()})
                .name(place.getName())
                .tags(collectTagNames(place))
                .placeDetailTags(collectPlaceDetailNames(place))
                .menuItemsTags(collectMenuItemsTagNames(place))
                .allergies(collectAllergyTagNames(place))
                .diets(collectDietTagNames(place))
                .atmospheres(collectPlaceDetailNames(place, PlaceDetailType.ATMOSPHERE))
                .cuisines(collectPlaceDetailNames(place, PlaceDetailType.CUISINE))
                .musics(collectPlaceDetailNames(place, PlaceDetailType.MUSIC))
                .interiors(collectPlaceDetailNames(place, PlaceDetailType.INTERIOR))
                .features(collectPlaceDetailNames(place, PlaceDetailType.FEATURE))
                .cuisines(collectPlaceDetailNames(place, PlaceDetailType.CUISINE))
                .placeTypes(collectPlaceDetailNames(place, PlaceDetailType.PLACE_TYPE))
                .additionalOptions(collectPlaceDetailNames(place, PlaceDetailType.OPTION))
                .curatorRecommendations(placeService.getPlaceRecommendationsCount(place.getId()))
                .create();
        if (place.getSchedule() != null) {
            mongoPlace.setSchedule(place.getSchedule().getOpenTimes().stream()
                    .map(dayOpenTime -> MongoDayOpenTime.of()
                            .day(dayOpenTime.getDay())
                            .openFrom(dayOpenTime.getFrom().getHour())
                            .openTo(dayOpenTime.getTo().getHour())
                            .create())
                    .collect(Collectors.toList()));
        }
        return mongoPlace;
    }

    private Set<String> collectTagNames(Place place) {
        HashSet<String> tags = place.getTags().stream()
                .filter(Objects::nonNull)
                .map(tag -> tag.getName().toLowerCase())
                .collect(Collectors.toCollection(HashSet::new));

        Set<String> menuItemTags = collectMenuItemsTagNames(place);

        tags.addAll(menuItemTags);

        return tags;
    }

    private Set<String> collectPlaceDetailNames(Place place) {
        return place.getPlaceDetails().stream()
                .filter(Objects::nonNull)
                .map(placeDetail -> placeDetail.getName().toLowerCase())
                .collect(Collectors.toCollection(HashSet::new));
    }

    private Set<String> collectPlaceDetailNames(Place place, PlaceDetailType type) {
        return place.getPlaceDetails().stream()
                .filter(Objects::nonNull)
                .filter(detail -> detail.getPlaceDetailType().equals(type))
                .map(placeDetail -> placeDetail.getName().toLowerCase())
                .collect(Collectors.toCollection(HashSet::new));
    }

    private Set<String> collectMenuItemsTagNames(Place place) {
        return getMenuItemsTagStream(place)
                .filter(Tag::isMenuTag)
                .map(tag -> tag.getName().toLowerCase())
                .collect(Collectors.toSet());
    }

    private Set<String> collectAllergyTagNames(Place place) {
        return getMenuItemsTagStream(place)
                .filter(Tag::isAllergyTag)
                .map(tag -> tag.getName().toLowerCase())
                .collect(Collectors.toSet());
    }

    private Set<String> collectDietTagNames(Place place) {
        return getMenuItemsTagStream(place)
                .filter(Tag::isDietTag)
                .map(tag -> tag.getName().toLowerCase())
                .collect(Collectors.toSet());
    }

//    TODO need to be refactored optional get in menuItems
    private Stream<Tag> getMenuItemsTagStream(Place place) {
        return place.getMenus().stream()
                .filter(Objects::nonNull)
                .map(menu -> Optional.ofNullable(menu.getMenuItems()))
                .filter(Optional::isPresent)
                .flatMap(menuItems -> menuItems.get().stream())
                .map(menuItem -> Optional.ofNullable(menuItem.getTags()))
                .filter(Optional::isPresent)
                .flatMap(tags -> tags.get().stream());
    }

}
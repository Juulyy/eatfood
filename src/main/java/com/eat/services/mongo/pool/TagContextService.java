package com.eat.services.mongo.pool;

import com.eat.models.b2b.Place;
import com.eat.models.b2c.AppUser;
import com.eat.models.b2c.AppUserCollection;
import com.eat.models.b2c.VisitedPlaces;
import com.eat.models.common.Tag;
import com.eat.models.mongo.pool.PoolContextType;
import com.eat.models.mongo.pool.TagContext;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.eat.models.mongo.pool.PoolContextRate.*;
import static com.eat.models.mongo.pool.PoolContextType.MENU_ITEM;
import static com.eat.models.mongo.pool.PoolContextType.MENU_ITEM_CATEGORY;

@Service
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class TagContextService {

    public Set<TagContext> tasteAndPrefTagContextFilling(AppUser appUser) {
        Set<TagContext> set = new HashSet<>();
        Set<Tag> tasteTags = new HashSet<>(appUser.getUserPreferences().getTasteTags());

        tasteTags.stream().filter(Objects::nonNull).filter(Tag::isMenuItemGroupTag).forEach(tag ->
                set.add(TagContext.of()
                        .tagName(tag.getName())
                        .type(PoolContextType.MENU_ITEM_GROUP)
                        .weight(REG_PREF.getWeight())
                        .create()));

        tasteTags.stream().filter(Objects::nonNull).filter(Tag::isMenuItemCategoryTag).forEach(tag -> {
            set.add(TagContext.of()
                    .tagName(tag.getName())
                    .type(MENU_ITEM_CATEGORY)
                    .weight(REG_PREF.getWeight())
                    .create());
            if (tag.getParent() != null) {
                Optional<TagContext> context = set.stream()
                        .filter(tagContext -> tagContext.getTagName().equals(tag.getParent().getName()))
                        .findAny();
                if (context.isPresent()) {
                    context.get().setWeight(context.get().getWeight() + REG_PREF.getWeight() / 2);
                } else {
                    set.add(TagContext.of()
                            .tagName(tag.getParent().getName())
                            .type(PoolContextType.MENU_ITEM_GROUP)
                            .weight(REG_PREF.getWeight() / 2)
                            .create());
                }
            }
        });

        tasteTags.stream().filter(Objects::nonNull).filter(Tag::isMenuItemTag).forEach(tag -> {
            set.add(TagContext.of()
                    .tagName(tag.getName())
                    .type(MENU_ITEM)
                    .weight(REG_PREF.getWeight())
                    .create());

            if (tag.getParent() != null) {
                Optional<TagContext> context = set.stream()
                        .filter(tagContext -> tagContext.getTagName().equals(tag.getParent().getName()))
                        .findAny();
                if (context.isPresent()) {
                    context.get().setWeight(context.get().getWeight() + REG_PREF.getWeight() / 2);
                } else {
                    set.add(TagContext.of()
                            .tagName(tag.getParent().getName())
                            .type(PoolContextType.MENU_ITEM_CATEGORY)
                            .weight(REG_PREF.getWeight() / 2)
                            .create());
                }
                if (tag.getParent().getParent() != null) {
                    context = set.stream()
                            .filter(tagContext -> tagContext.getTagName().equals(tag.getParent().getParent().getName()))
                            .findAny();
                    if (context.isPresent()) {
                        context.get().setWeight(context.get().getWeight() + REG_PREF.getWeight() / 4);
                    } else {
                        set.add(TagContext.of()
                                .tagName(tag.getParent().getParent().getName())
                                .type(PoolContextType.MENU_ITEM_GROUP)
                                .weight(REG_PREF.getWeight() / 4)
                                .create());
                    }
                }
            }
        });

        tasteTags.stream().filter(Objects::nonNull).filter(Tag::isFeatureTag).forEach(tag ->
                set.add(TagContext.of()
                        .tagName(tag.getName())
                        .type(PoolContextType.FEATURE)
                        .weight(REG_PREF.getWeight())
                        .create()
                ));

        tasteTags.stream().filter(Objects::nonNull).filter(Tag::isCuisineTag).forEach(tag ->
                set.add(TagContext.of()
                        .tagName(tag.getName())
                        .type(PoolContextType.CUISINE)
                        .weight(REG_PREF.getWeight())
                        .create()
                ));

        tasteTags.stream().filter(Objects::nonNull).filter(Tag::isAtmosphereTag).forEach(tag ->
                set.add(TagContext.of()
                        .tagName(tag.getName())
                        .type(PoolContextType.ATMOSPHERE)
                        .weight(REG_PREF.getWeight())
                        .create()
                ));

        tasteTags.stream().filter(Objects::nonNull).filter(Tag::isMusicTag).forEach(tag ->
                set.add(TagContext.of()
                        .tagName(tag.getName())
                        .type(PoolContextType.MUSIC)
                        .weight(REG_PREF.getWeight())
                        .create()
                ));

        tasteTags.stream().filter(Objects::nonNull).filter(Tag::isInteriorTag).forEach(tag ->
                set.add(TagContext.of()
                        .tagName(tag.getName())
                        .type(PoolContextType.INTERIOR)
                        .weight(REG_PREF.getWeight())
                        .create()
                ));

        return set;
    }

    //    TODO try to refactor this impl
    public Set<TagContext> initialTagContextFilling(AppUser appUser) {
        Set<TagContext> set = new HashSet<>();
        /*Set<Tag> tasteTags = new HashSet<>(appUser.getUserPreferences().getTasteTags());*/
        List<AppUserCollection> collections = appUser.getUserPreferences().getCollections();
        Set<VisitedPlaces> visitedPlaces = appUser.getUserPreferences().getVisitedPlaces();

        set.addAll(tasteAndPrefTagContextFilling(appUser));

        if (!collections.isEmpty()) {
            collections.stream()
                    .filter(AppUserCollection::isRecFeaturesCollection)
                    .map(AppUserCollection::getFeatures)
                    .flatMap(Collection::stream)
                    .forEach(feature -> {
                        Optional<TagContext> context = set.stream()
                                .filter(tagContext -> tagContext.getTagName().equals(feature.getName()))
                                .findAny();
                        if (context.isPresent()) {
                            context.get().setWeight(context.get().getWeight() + REC_FEATURE.getWeight());
                        } else {
                            set.add(TagContext.of()
                                    .tagName(feature.getName())
                                    .type(PoolContextType.FEATURE)
                                    .weight(REC_FEATURE.getWeight())
                                    .create());
                        }
                    });

            collections.stream()
                    .filter(AppUserCollection::isRecMenuItemsCollection)
                    .map(AppUserCollection::getMenuItems)
                    .flatMap(Collection::stream)
                    .forEach(menuItem -> {
                        Optional<TagContext> context = set.stream()
                                .filter(tagContext -> tagContext.getTagName().equals(menuItem.getName()))
                                .findAny();
                        if (context.isPresent()) {
                            context.get().setWeight(context.get().getWeight() + REC_MENU_ITEM.getWeight());
                        } else {
                            set.add(TagContext.of()
                                    .tagName(menuItem.getName())
                                    .type(PoolContextType.MENU_ITEM)
                                    .weight(REC_MENU_ITEM.getWeight())
                                    .create());
                        }
                    });

            Set<Tag> followedPlacesTags = collections.stream()
                    .filter(AppUserCollection::isFollowedPlacesCollection)
                    .map(AppUserCollection::getPlaces)
                    .flatMap(Collection::stream)
                    .map(Place::getTags)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toSet());

            followedPlacesTags.stream().filter(Tag::isMenuItemCategoryTag).forEach(menuItemCategoryTag -> {
                Optional<TagContext> context = set.stream()
                        .filter(tagContext -> tagContext.getTagName().equals(menuItemCategoryTag.getName()))
                        .findAny();
                if (context.isPresent()) {
                    context.get().setWeight(context.get().getWeight() + FOLLOWED_PLACE_MENU_ITEM.getWeight());
                } else {
                    set.add(TagContext.of()
                            .tagName(menuItemCategoryTag.getName())
                            .type(PoolContextType.MENU_ITEM_CATEGORY)
                            .weight(FOLLOWED_PLACE_MENU_ITEM.getWeight())
                            .create());
                }
            });

            followedPlacesTags.stream().filter(Tag::isCuisineTag).forEach(cuisineTag -> {
                Optional<TagContext> context = set.stream()
                        .filter(tagContext -> tagContext.getTagName().equals(cuisineTag.getName()))
                        .findAny();
                if (context.isPresent()) {
                    context.get().setWeight(context.get().getWeight() + FOLLOWED_PLACE.getWeight());
                } else {
                    set.add(TagContext.of()
                            .tagName(cuisineTag.getName())
                            .type(PoolContextType.CUISINE)
                            .weight(FOLLOWED_PLACE.getWeight())
                            .create());
                }
            });

            followedPlacesTags.stream().filter(Tag::isFeatureTag).forEach(featureTag -> {
                Optional<TagContext> context = set.stream()
                        .filter(tagContext -> tagContext.getTagName().equals(featureTag.getName()))
                        .findAny();
                if (context.isPresent()) {
                    context.get().setWeight(context.get().getWeight() + FOLLOWED_PLACE.getWeight());
                } else {
                    set.add(TagContext.of()
                            .tagName(featureTag.getName())
                            .type(PoolContextType.FEATURE)
                            .weight(FOLLOWED_PLACE.getWeight())
                            .create());
                }
            });

            followedPlacesTags.stream().filter(Tag::isAtmosphereTag).forEach(atmoTag -> {
                Optional<TagContext> context = set.stream()
                        .filter(tagContext -> tagContext.getTagName().equals(atmoTag.getName()))
                        .findAny();
                if (context.isPresent()) {
                    context.get().setWeight(context.get().getWeight() + FOLLOWED_PLACE.getWeight());
                } else {
                    set.add(TagContext.of()
                            .tagName(atmoTag.getName())
                            .type(PoolContextType.ATMOSPHERE)
                            .weight(FOLLOWED_PLACE.getWeight())
                            .create());
                }
            });

            followedPlacesTags.stream().filter(Tag::isMusicTag).forEach(musicTag -> {
                Optional<TagContext> context = set.stream()
                        .filter(tagContext -> tagContext.getTagName().equals(musicTag.getName()))
                        .findAny();
                if (context.isPresent()) {
                    context.get().setWeight(context.get().getWeight() + FOLLOWED_PLACE.getWeight());
                } else {
                    set.add(TagContext.of()
                            .tagName(musicTag.getName())
                            .type(PoolContextType.MUSIC)
                            .weight(FOLLOWED_PLACE.getWeight())
                            .create());
                }
            });

            followedPlacesTags.stream().filter(Tag::isInteriorTag).forEach(interiorTag -> {
                Optional<TagContext> context = set.stream()
                        .filter(tagContext -> tagContext.getTagName().equals(interiorTag.getName()))
                        .findAny();
                if (context.isPresent()) {
                    context.get().setWeight(context.get().getWeight() + FOLLOWED_PLACE.getWeight());
                } else {
                    set.add(TagContext.of()
                            .tagName(interiorTag.getName())
                            .type(PoolContextType.INTERIOR)
                            .weight(FOLLOWED_PLACE.getWeight())
                            .create());
                }
            });

            followedPlacesTags.stream().filter(Tag::isConcreteMenuGroupTag).forEach(itemGroupTag -> {
                Optional<TagContext> context = set.stream()
                        .filter(tagContext -> tagContext.getTagName().equals(itemGroupTag.getName()))
                        .findAny();
                if (context.isPresent()) {
                    context.get().setWeight(context.get().getWeight() + FOLLOWED_PLACE_MENU_ITEM.getWeight() / 2);
                } else {
                    set.add(TagContext.of()
                            .tagName(itemGroupTag.getName())
                            .type(PoolContextType.MENU_ITEM_GROUP)
                            .weight(FOLLOWED_PLACE_MENU_ITEM.getWeight() / 2)
                            .create());
                }
            });

            Set<Tag> recPlacesTags = collections.stream()
                    .filter(AppUserCollection::isRecPlacesCollection)
                    .map(AppUserCollection::getPlaces)
                    .flatMap(Collection::stream)
                    .map(Place::getTags)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toSet());

            recPlacesTags.stream().filter(Tag::isPlaceTypeTag).forEach(placeTypeTag -> {
                Optional<TagContext> context = set.stream()
                        .filter(tagContext -> tagContext.getTagName().equals(placeTypeTag.getName()))
                        .findAny();
                if (context.isPresent()) {
                    context.get().setWeight(context.get().getWeight() + REC_PLACE.getWeight());
                } else {
                    set.add(TagContext.of()
                            .tagName(placeTypeTag.getName())
                            .type(PoolContextType.PLACE_TYPE)
                            .weight(REC_PLACE.getWeight())
                            .create());
                }
            });

            recPlacesTags.stream().filter(Tag::isCuisineTag).forEach(cuisineTag -> {
                Optional<TagContext> context = set.stream()
                        .filter(tagContext -> tagContext.getTagName().equals(cuisineTag.getName()))
                        .findAny();
                if (context.isPresent()) {
                    context.get().setWeight(context.get().getWeight() + REC_PLACE.getWeight());
                } else {
                    set.add(TagContext.of()
                            .tagName(cuisineTag.getName())
                            .type(PoolContextType.CUISINE)
                            .weight(REC_PLACE.getWeight())
                            .create());
                }
            });

            recPlacesTags.stream().filter(Tag::isFeatureTag).forEach(featureTag -> {
                Optional<TagContext> context = set.stream()
                        .filter(tagContext -> tagContext.getTagName().equals(featureTag.getName()))
                        .findAny();
                if (context.isPresent()) {
                    context.get().setWeight(context.get().getWeight() + REC_PLACE.getWeight());
                } else {
                    set.add(TagContext.of()
                            .tagName(featureTag.getName())
                            .type(PoolContextType.FEATURE)
                            .weight(REC_PLACE.getWeight())
                            .create());
                }
            });

            recPlacesTags.stream().filter(Tag::isAtmosphereTag).forEach(atmoTag -> {
                Optional<TagContext> context = set.stream()
                        .filter(tagContext -> tagContext.getTagName().equals(atmoTag.getName()))
                        .findAny();
                if (context.isPresent()) {
                    context.get().setWeight(context.get().getWeight() + REC_PLACE.getWeight());
                } else {
                    set.add(TagContext.of()
                            .tagName(atmoTag.getName())
                            .type(PoolContextType.ATMOSPHERE)
                            .weight(REC_PLACE.getWeight())
                            .create());
                }
            });

            recPlacesTags.stream().filter(Tag::isMusicTag).forEach(musicTag -> {
                Optional<TagContext> context = set.stream()
                        .filter(tagContext -> tagContext.getTagName().equals(musicTag.getName()))
                        .findAny();
                if (context.isPresent()) {
                    context.get().setWeight(context.get().getWeight() + REC_PLACE.getWeight());
                } else {
                    set.add(TagContext.of()
                            .tagName(musicTag.getName())
                            .type(PoolContextType.MUSIC)
                            .weight(REC_PLACE.getWeight())
                            .create());
                }
            });

            recPlacesTags.stream().filter(Tag::isMusicTag).forEach(interiorTag -> {
                Optional<TagContext> context = set.stream()
                        .filter(tagContext -> tagContext.getTagName().equals(interiorTag.getName()))
                        .findAny();
                if (context.isPresent()) {
                    context.get().setWeight(context.get().getWeight() + REC_PLACE.getWeight());
                } else {
                    set.add(TagContext.of()
                            .tagName(interiorTag.getName())
                            .type(PoolContextType.INTERIOR)
                            .weight(REC_PLACE.getWeight())
                            .create());
                }
            });

            recPlacesTags.stream().filter(Tag::isConcreteMenuCategoryTag).forEach(itemCategoryTag -> {
                Optional<TagContext> context = set.stream()
                        .filter(tagContext -> tagContext.getTagName().equals(itemCategoryTag.getName()))
                        .findAny();
                if (context.isPresent()) {
                    context.get().setWeight(context.get().getWeight() + REC_PLACE_MENU_ITEM.getWeight());
                } else {
                    set.add(TagContext.of()
                            .tagName(itemCategoryTag.getName())
                            .type(PoolContextType.MENU_ITEM_CATEGORY)
                            .weight(REC_PLACE_MENU_ITEM.getWeight())
                            .create());
                }
            });

            recPlacesTags.stream().filter(Tag::isConcreteMenuGroupTag).forEach(itemGroupTag -> {
                Optional<TagContext> context = set.stream()
                        .filter(tagContext -> tagContext.getTagName().equals(itemGroupTag.getName()))
                        .findAny();
                if (context.isPresent()) {
                    context.get().setWeight(context.get().getWeight() + REC_PLACE_MENU_ITEM.getWeight() / 2);
                } else {
                    set.add(TagContext.of()
                            .tagName(itemGroupTag.getName())
                            .type(PoolContextType.MENU_ITEM_GROUP)
                            .weight(REC_PLACE_MENU_ITEM.getWeight() / 2)
                            .create());
                }
            });
        }
//        TODO visited places by plan impl

        if (!visitedPlaces.isEmpty()) {
            Set<Tag> visitedPlacesTags = visitedPlaces.stream()
                    .map(VisitedPlaces::getPlace)
                    .map(Place::getTags)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toSet());

            visitedPlacesTags.stream().filter(Tag::isCuisineTag).forEach(cuisineTag -> {
                Optional<TagContext> context = set.stream()
                        .filter(tagContext -> tagContext.getTagName().equals(cuisineTag.getName()))
                        .findAny();
                if (context.isPresent()) {
                    context.get().setWeight(context.get().getWeight() + VISITED_PLACE_AUTOCHECK.getWeight());
                } else {
                    set.add(TagContext.of()
                            .tagName(cuisineTag.getName())
                            .type(PoolContextType.CUISINE)
                            .weight(VISITED_PLACE_AUTOCHECK.getWeight())
                            .create());
                }
            });

            visitedPlacesTags.stream().filter(Tag::isFeatureTag).forEach(featureTag -> {
                Optional<TagContext> context = set.stream()
                        .filter(tagContext -> tagContext.getTagName().equals(featureTag.getName()))
                        .findAny();
                if (context.isPresent()) {
                    context.get().setWeight(context.get().getWeight() + VISITED_PLACE_AUTOCHECK.getWeight());
                } else {
                    set.add(TagContext.of()
                            .tagName(featureTag.getName())
                            .type(PoolContextType.FEATURE)
                            .weight(VISITED_PLACE_AUTOCHECK.getWeight())
                            .create());
                }
            });

            visitedPlacesTags.stream().filter(Tag::isAtmosphereTag).forEach(atmoTag -> {
                Optional<TagContext> context = set.stream()
                        .filter(tagContext -> tagContext.getTagName().equals(atmoTag.getName()))
                        .findAny();
                if (context.isPresent()) {
                    context.get().setWeight(context.get().getWeight() + VISITED_PLACE_AUTOCHECK.getWeight());
                } else {
                    set.add(TagContext.of()
                            .tagName(atmoTag.getName())
                            .type(PoolContextType.ATMOSPHERE)
                            .weight(VISITED_PLACE_AUTOCHECK.getWeight())
                            .create());
                }
            });

            visitedPlacesTags.stream().filter(Tag::isMusicTag).forEach(musicTag -> {
                Optional<TagContext> context = set.stream()
                        .filter(tagContext -> tagContext.getTagName().equals(musicTag.getName()))
                        .findAny();
                if (context.isPresent()) {
                    context.get().setWeight(context.get().getWeight() + VISITED_PLACE_AUTOCHECK.getWeight());
                } else {
                    set.add(TagContext.of()
                            .tagName(musicTag.getName())
                            .type(PoolContextType.MUSIC)
                            .weight(VISITED_PLACE_AUTOCHECK.getWeight())
                            .create());
                }
            });

            visitedPlacesTags.stream().filter(Tag::isInteriorTag).forEach(interiorTag -> {
                Optional<TagContext> context = set.stream()
                        .filter(tagContext -> tagContext.getTagName().equals(interiorTag.getName()))
                        .findAny();
                if (context.isPresent()) {
                    context.get().setWeight(context.get().getWeight() + VISITED_PLACE_AUTOCHECK.getWeight());
                } else {
                    set.add(TagContext.of()
                            .tagName(interiorTag.getName())
                            .type(PoolContextType.INTERIOR)
                            .weight(VISITED_PLACE_AUTOCHECK.getWeight())
                            .create());
                }
            });

            visitedPlacesTags.stream().filter(Tag::isMenuItemCategoryTag).forEach(menuCategoryTag -> {
                Optional<TagContext> context = set.stream()
                        .filter(tagContext -> tagContext.getTagName().equals(menuCategoryTag.getName()))
                        .findAny();
                if (context.isPresent()) {
                    context.get().setWeight(context.get().getWeight() + VISITED_PLACE_AUTOCHECK_MENU_CATEGORY.getWeight());
                } else {
                    set.add(TagContext.of()
                            .tagName(menuCategoryTag.getName())
                            .type(PoolContextType.MENU_ITEM_CATEGORY)
                            .weight(VISITED_PLACE_AUTOCHECK_MENU_CATEGORY.getWeight())
                            .create());
                }
                if (menuCategoryTag.getParent() != null) {
                    context = set.stream()
                            .filter(tagContext -> tagContext.getTagName().equals(menuCategoryTag.getParent().getName()))
                            .findAny();
                    if (context.isPresent()) {
                        context.get().setWeight(context.get().getWeight() + VISITED_PLACE_AUTOCHECK_MENU_CATEGORY.getWeight() / 2);
                    } else {
                        set.add(TagContext.of()
                                .tagName(menuCategoryTag.getParent().getName())
                                .type(PoolContextType.MENU_ITEM_GROUP)
                                .weight(VISITED_PLACE_AUTOCHECK_MENU_CATEGORY.getWeight() / 2)
                                .create());
                    }
                }

            });
        }
        return set;
    }

    public PoolContextType getContextType(Tag tag) {
        switch (tag.getType()) {
            case MENU_ITEM_GROUP:
                return PoolContextType.MENU_ITEM_GROUP;
            case MENU_ITEM_CATEGORY:
                return PoolContextType.MENU_ITEM_CATEGORY;
            case MENU_ITEM:
                return PoolContextType.MENU_ITEM;
            case FEATURE:
                return PoolContextType.FEATURE;
            case CUISINE:
                return PoolContextType.CUISINE;
            case INTERIOR:
                return PoolContextType.INTERIOR;
            case ATMOSPHERE:
                return PoolContextType.ATMOSPHERE;
            case MUSIC:
                return PoolContextType.MUSIC;
            case PLACE_TYPE:
                return PoolContextType.PLACE_TYPE;
            case ALLERGY:
                return PoolContextType.ALLERGY;
            case DIET:
                return PoolContextType.DIET;
            /*case CUSTOM:
                return PoolContextType.CUSTOM;*/
            default:
                return null;
        }
    }

    public Comparator<TagContext> comparator() {
        return Comparator.comparingDouble(TagContext::getWeight);
    }

}
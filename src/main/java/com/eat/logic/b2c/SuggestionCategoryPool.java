package com.eat.logic.b2c;

import com.eat.logic.rankers.wrappers.Rankable;
import com.eat.logic.rankers.wrappers.RankablePlaceWrapper;
import com.eat.logic.rankers.wrappers.RankableSuggestionCategoryWrapper;
import com.eat.models.b2b.Place;
import com.eat.models.common.Tag;
import com.eat.models.common.enums.TagType;
import com.eat.models.mongo.MongoPlace;
import com.eat.models.mongo.enums.WeatherIcon;
import com.eat.models.recommender.SuggestionCategory;
import com.eat.services.b2b.PlaceService;
import com.eat.services.b2c.AppUserPreferenceService;
import com.eat.services.mongo.b2b.MongoPlaceService;
import com.eat.services.mongo.pool.BehaviourPoolService;
import com.eat.services.recommender.SuggestionCategoryService;
import com.google.common.collect.Maps;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SuggestionCategoryPool {

    private static ConcurrentMap<Long, RankableSuggestionCategoryWrapper> suggestionCategories = new ConcurrentHashMap<>();

    private static ConcurrentMap<Long, List<Pair<Rankable<SuggestionCategory>, List<Rankable<Place>>>>> cachedUserResults = new ConcurrentHashMap<>();

    @Autowired
    private SuggestionCategoryService categoryService;
    @Autowired
    private PlaceService placeService;
    @Autowired
    private MongoPlaceService mongoPlaceService;
    @Autowired
    private BehaviourPoolService behaviourPoolService;
    @Autowired
    private WeatherFilter weatherFilter;
    @Autowired
    private AppUserPreferenceService preferenceService;

    private final double ONE_HUNDRED_PERCENT = 100.0;

    @PostConstruct
    public void postInitialize() {
        updateSuggestionCategories();
    }

    public void updateSuggestionCategories() {
        List<SuggestionCategory> categories = categoryService.findAll();

        if (!categories.isEmpty()) {
            ConcurrentMap<Long, RankableSuggestionCategoryWrapper> collect = categories.stream()
                    .map(category -> new ImmutablePair<>(category, mongoPlaceService.getPlacesByCategoryOptions(category)))
//                    .filter(categoryPlaces -> !CollectionUtils.isEmpty(categoryPlaces.getValue()))
                    .map(this::getRankableSuggestionCategoryWrapper)
                    .collect(Collectors.toConcurrentMap(wrapper -> wrapper.getCategory().getId(), Function.identity()));

            suggestionCategories = collect;

        }
    }

    public Double calculatePlaceRank(SuggestionCategory category, MongoPlace mongoPlace) {

        return category.getTags().stream()
                .filter(mongoPlace::containsTag)
                .mapToDouble(category::getTagRank)
                .sum();
    }

    /*public Double getTagMatchingRate(SuggestionCategory category, MongoPlace mongoPlace) {
        Double rate = 0D;

        if (category.getTags().isEmpty()) {
            return rate;
        }

        double tagMatchStep = ONE_HUNDRED_PERCENT / category.getTags().size();
        int roundedUpTagMatchStep = DoubleMath.roundToInt(tagMatchStep, RoundingMode.UP);

        double matchedTags = category.getTags().stream()
                .filter(mongoPlace::containsTag)
                .count();

        rate = roundedUpTagMatchStep * matchedTags;

        return rate;
    }*/

    private List<Pair<Rankable<SuggestionCategory>, List<Rankable<Place>>>> getRelevantCategoriesForUser(@NonNull Set<ImmutablePair<Tag, Double>> userTopTags,
                                                                                                         @NonNull MultiValuedMap<TagType, Tag> userAllergiesAndDiets,
                                                                                                         @NonNull LocalDateTime dateTime,
                                                                                                         Circle circle, int categoryNumber, Double temp, WeatherIcon icon) {

        List<Rankable<SuggestionCategory>> rankedCategories = suggestionCategories.values().stream()
                .filter(categoryWrapper -> categoryWrapper.isRelevantByAllergiesAndDiets(userAllergiesAndDiets))
                .filter(categoryWrapper -> weatherFilter.filterCategories(categoryWrapper.getCategory(), temp, icon))
                .map(categoryWrapper -> categoryWrapper.getRankedCategoryForTagSet(userTopTags))
                .filter(catRankable -> catRankable.getRank() > 0D)
                .sorted(Collections.reverseOrder())
                .limit(categoryNumber)
                .collect(Collectors.toList());

        List<Pair<Rankable<SuggestionCategory>, List<Rankable<Place>>>> userTopCategories = rankedCategories.stream()
                .filter(categoryWrapper ->
                        suggestionCategories.containsKey(categoryWrapper.getRankableObject().getId()))
                .map(categoryWrapper -> MutablePair.of(categoryWrapper,
                        suggestionCategories.get(categoryWrapper.getRankableObject().getId())))
                .map(pair -> MutablePair.of(pair.getLeft(), getRankablePlaces(pair.getRight(), userTopTags, userAllergiesAndDiets, dateTime, circle)))
                .collect(Collectors.toList());

        return userTopCategories;
    }

    public List<Pair<Rankable<SuggestionCategory>, List<Rankable<Place>>>> getRelevantCategoriesForUserWithFilters(Long userId, int categoryNumber,
                                                                                                                   Circle circle, LocalDateTime dateTime, Double temp, WeatherIcon icon) {

        List<Pair<Rankable<SuggestionCategory>, List<Rankable<Place>>>> relevantCategoriesForUser;

//        if (cachedUserResults.containsKey(userId)) {
//            relevantCategoriesForUser = cachedUserResults.get(userId);
//        } else {

        Set<ImmutablePair<Tag, Double>> topUserTags = behaviourPoolService.findTopUserTagsWithRates(userId);
        MultiValuedMap<TagType, Tag> userAllergiesAndDiets = preferenceService.getUserAllergiesAndDiets(userId);

        relevantCategoriesForUser = getRelevantCategoriesForUser(topUserTags, userAllergiesAndDiets, dateTime, circle, categoryNumber, temp, icon);

//        cacheResult(userId, relevantCategoriesForUser);
//        }


        return relevantCategoriesForUser.stream()
                .filter(categoryPlaces -> categoryPlaces.getLeft().getRankableObject()
                        .timeInCategoryPeriod(LocalTime.of(dateTime.getHour(), dateTime.getMinute())))
                .filter(categoryPlaces -> !categoryPlaces.getRight().isEmpty())
                .collect(Collectors.toList());
    }

    public List<Pair<Rankable<SuggestionCategory>, List<Rankable<Place>>>> getAllCategories() {

        List<Rankable<SuggestionCategory>> rankedCategories = suggestionCategories.values().stream()
                .map(RankableSuggestionCategoryWrapper::getRankedCategory)
                .sorted(Collections.reverseOrder())
                .collect(Collectors.toList());

        return rankedCategories.stream()
                .map(categoryWrapper -> MutablePair.of(categoryWrapper, suggestionCategories.get(
                        categoryWrapper.getRankableObject().getId()).getRankedPlaces()))
                .collect(Collectors.toList());
    }

    public void clearCachedUserResults(Long userId) {
        cachedUserResults.remove(userId);
    }

    public void addPlaceToCategoriesPools(Place place) {

        suggestionCategories.values().stream()
                .map(categoryWrapper -> MutablePair.of(categoryWrapper,
                        mongoPlaceService.getPlaceByCategoryOptionsAndSqlId(categoryWrapper.getCategory(), place.getId())))
                .filter(pair -> pair.getValue().isPresent())
                .forEach(pair -> pair.getKey().addPlaceToPool(place, pair.getValue().get()));
    }

    public void addPlacesToCategoriesPools(Place... places) {

        suggestionCategories.forEach((aLong, categoryWrapper) -> Arrays.asList(places).stream()
                .map(place -> Triple.of(categoryWrapper, place,
                        mongoPlaceService.getPlaceByCategoryOptionsAndSqlId(categoryWrapper.getCategory(), place.getId())))
                .filter(infoToAdd -> infoToAdd.getRight().isPresent())
                .forEach(infoToAdd -> infoToAdd.getLeft().addPlaceToPool(infoToAdd.getMiddle(), infoToAdd.getRight().get())));
    }

    public void updatePlacesInCategoryPools(Place... places) {

        suggestionCategories.forEach((aLong, categoryWrapper) -> Arrays.stream(places)
                .filter(place -> categoryWrapper.getPlacesPool().get(place.getId()) == null)
                .forEach(place -> categoryWrapper.updatePlaceInPool(
                        place, mongoPlaceService.findBySqlEntityId(place.getId()))));
    }

    public void removePlacesFromCategoryPools(Place... places) {

        suggestionCategories.forEach(
                (aLong, rankableSuggestionCategoryWrapper) ->
                        Arrays.asList(places).forEach(place ->
                                rankableSuggestionCategoryWrapper.removePlaceFromPool(place.getId())));
    }

    private RankableSuggestionCategoryWrapper getRankableSuggestionCategoryWrapper(Pair<SuggestionCategory, List<MongoPlace>> categoryPlaces) {

        return RankableSuggestionCategoryWrapper.of()
                .category(categoryPlaces.getLeft())
                .placesPool(getRankedPlacesPool(categoryPlaces.getLeft(), categoryPlaces.getRight()))
                .rank(calculateCategoryRank(categoryPlaces.getLeft()))
                .create();
    }

    private RankableSuggestionCategoryWrapper getRankableSuggestionCategoryWrapper(SuggestionCategory category, List<MongoPlace> mongoPlaces) {

        return RankableSuggestionCategoryWrapper.of()
                .category(category)
                .placesPool(getRankedPlacesPool(category, mongoPlaces))
                .rank(calculateCategoryRank(category))
                .create();
    }

    private Map<Long, RankablePlaceWrapper> getRankedPlacesPool(SuggestionCategory category, List<MongoPlace> mongoPlaces) {

        if (mongoPlaces.isEmpty()) {
            return Maps.newHashMap();
        }

        List<RankablePlaceWrapper> rankablePlaces = mongoPlaces.stream()
                .filter(mongoPlace -> calculatePlaceRank(category, mongoPlace) > 0)
//                .filter(mongoPlace -> getTagMatchingRate(category, mongoPlace) >= category.getKoeff())
                .map(mongoPlace -> RankablePlaceWrapper.of()
                        .id(mongoPlace.getSqlEntityId())
                        .mongoPlace(mongoPlace)
                        .rank(calculatePlaceRank(category, mongoPlace))
                        .create())
                .sorted(Collections.reverseOrder())
                .collect(Collectors.toList());

        if (rankablePlaces.isEmpty()) {
            return Maps.newHashMap();
        }

        Map<Long, Place> placesMap =
                placeService.getPlacesMapById(rankablePlaces.stream()
                        .map(RankablePlaceWrapper::getId)
                        .collect(Collectors.toList()));

        return rankablePlaces.stream()
                .peek(wrapper -> wrapper.setPlace(placesMap.get(wrapper.getId()).dropAdditionalInfo()))
                .collect(Collectors.toConcurrentMap(RankablePlaceWrapper::getId, Function.identity()));

    }

    private void cacheResult(Long userId, List<Pair<Rankable<SuggestionCategory>, List<Rankable<Place>>>> userTopCategories) {
        if (userTopCategories.size() > 0) {
            cachedUserResults.put(userId, userTopCategories);
        }
    }

    private List<Rankable<SuggestionCategory>> getTopCategoriesNumber(int categoryNumber, List<Rankable<SuggestionCategory>> rankedCategories) {

        if (rankedCategories.size() > categoryNumber - 1) {
            List<Rankable<SuggestionCategory>> choosenRankedCategories = new ArrayList<>();

            for (int i = 0; i < categoryNumber; i++) {
                choosenRankedCategories.add(rankedCategories.get(i));
            }
            rankedCategories = choosenRankedCategories;
        }
        return rankedCategories;
    }

    private List<Rankable<Place>> getRankablePlaces(RankableSuggestionCategoryWrapper categoryWrapper,
                                                    Set<ImmutablePair<Tag, Double>> topUserTags,
                                                    MultiValuedMap<TagType, Tag> userAllergiesAndDiets,
                                                    LocalDateTime dateTime, Circle circle) {

        List<Tag> allergies = new ArrayList<>(userAllergiesAndDiets.get(TagType.ALLERGY));

        List<Tag> diets = new ArrayList<>(userAllergiesAndDiets.get(TagType.DIET));

        List<RankablePlaceWrapper> rankablePlaces = categoryWrapper.getPlacesPool().values().stream()
                //.filter(rankablePlaceWrapper -> rankablePlaceWrapper.isRelevantForDayOfWeek(dateTime.getDayOfWeek()))
                .collect(Collectors.toList());

        if (!allergies.isEmpty()) {
            rankablePlaces = rankablePlaces.stream()
                    .filter(rankablePlaceWrapper -> isRelevantByAllergies(allergies, rankablePlaceWrapper.getMongoPlace()))
                    .collect(Collectors.toList());
        }

        if (!diets.isEmpty()) {
            rankablePlaces = rankablePlaces.stream()
                    .filter(wrapper -> isRelevantByDiets(diets, wrapper.getMongoPlace()))
                    .collect(Collectors.toList());
        }

        if (circle != null) {

            List<MongoPlace> mongoPlaces = rankablePlaces.stream()
                    .filter(placeWrapper -> placeWrapper.geoPositionInRadius(circle))
                    .map(RankablePlaceWrapper::getMongoPlace)
                    .collect(Collectors.toList());

            return categoryWrapper.getRankedPlacesInListForTagSet(topUserTags, mongoPlaces);
        } else {
            return categoryWrapper.getRankedPlacesForTagSet(topUserTags, rankablePlaces);
        }
    }

    private boolean isRelevantByDiets(List<Tag> diets, MongoPlace mongoPlace) {
        for (Tag diet : diets) {
            if (mongoPlace.getDiets().contains(diet.getName().toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private boolean isRelevantByAllergies(List<Tag> allergies, MongoPlace mongoPlace) {
        for (Tag allergy : allergies) {
            if (mongoPlace.getAllergies().contains(allergy.getName().toLowerCase())) {
                return false;
            }
        }
        return true;
    }

    /*private List<Rankable<Place>> getRankablePlaces(RankableSuggestionCategoryWrapper categoryWrapper) {

        return Optional.ofNullable(circle)
                .map(geo -> categoryWrapper.getRankedPlacesInListForTagSet(topUserTags,
                        mongoPlaceService.findByLocationWithin(geo,
                                categoryWrapper.getPlacesPool().values().stream()
                                        .map(RankablePlaceWrapper::getId)
                                        .collect(Collectors.toList()))))
                .orElse(categoryWrapper.getRankedPlacesForTagSet(topUserTags))

    }*/

    private Double calculateCategoryRank(SuggestionCategory category) {

        return (category.isUseDatePeriod() ? 1d : 0d)
                + (category.isUseTagFilter() ? 1d +
                category.getTags().stream()
                        .mapToDouble(tag -> 0.1d)
                        .sum() : 0d);
    }

    public void addCategoryToPool(List<SuggestionCategory> suggestionCategories) {

        suggestionCategories.forEach(this::addCategoryToPool);
    }

    public void updateCategoryInPool(SuggestionCategory suggestionCategory) {

        if (suggestionCategory.getId() == null) {
            log.warn("Try to update aren't persisted category! " + suggestionCategory.toString());
            return;
        }

        RankableSuggestionCategoryWrapper categoryWrapper = suggestionCategories.get(suggestionCategory.getId());
        List<MongoPlace> placesByCategoryOptions = mongoPlaceService.getPlacesByCategoryOptions(suggestionCategory);

        suggestionCategories.put(suggestionCategory.getId(), getRankableSuggestionCategoryWrapper(suggestionCategory, placesByCategoryOptions));
    }

    public void addCategoryToPool(SuggestionCategory suggestionCategory) {

        if (suggestionCategory.getId() == null) {
            log.warn("Try to add aren't persisted category! " + suggestionCategory.toString());
            return;
        }

        List<MongoPlace> placesByCategoryOptions = mongoPlaceService.getPlacesByCategoryOptions(suggestionCategory);
        suggestionCategories.put(suggestionCategory.getId(), getRankableSuggestionCategoryWrapper(suggestionCategory, placesByCategoryOptions));
    }

}
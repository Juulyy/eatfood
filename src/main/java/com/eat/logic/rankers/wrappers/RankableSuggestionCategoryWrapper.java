package com.eat.logic.rankers.wrappers;

import com.eat.models.b2b.Place;
import com.eat.models.common.Tag;
import com.eat.models.common.enums.TagType;
import com.eat.models.mongo.MongoPlace;
import com.eat.models.recommender.SuggestionCategory;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Data
@Builder(builderMethodName = "of", buildMethodName = "create")
public class RankableSuggestionCategoryWrapper
        implements Comparable<RankableSuggestionCategoryWrapper>, Comparator<RankableSuggestionCategoryWrapper> {

    private SuggestionCategory category;

    private Map<Long, RankablePlaceWrapper> placesPool = new ConcurrentHashMap();

    private Double rank = 0d;

    public Double getCategoryRankForTagSet(Set<ImmutablePair<Tag, Double>> tags) {

        return tags.stream()
                .filter(tag -> category.getTags().contains(tag.getKey()))
                .mapToDouble(tag -> tag.getValue())
                .sum();
    }

    public Rankable<SuggestionCategory> getRankedCategoryForTagSet(Set<ImmutablePair<Tag, Double>> tags) {

        return RankableWrapper.<SuggestionCategory>of()
                .rankedObject(getCategory())
                .rank(getCategoryRankForTagSet(tags))
                .create();
    }

    public Rankable<SuggestionCategory> getRankedCategory() {

        return RankableWrapper.<SuggestionCategory>of()
                .rankedObject(getCategory())
                .rank(rank)
                .create();
    }

    public List<Rankable<Place>> getRankedPlacesForTagSet(Set<ImmutablePair<Tag, Double>> ratedTags, List<RankablePlaceWrapper> rankablePlaces) {

        return rankablePlaces.stream()
                .map(categoryWrapper -> categoryWrapper.getRankedPlaceForTagSet(ratedTags))
                .sorted(Collections.reverseOrder())
                .filter(rankable -> rankable.getRank() > 0)
                .collect(Collectors.toList());
    }

    public List<Rankable<Place>> getRankedPlacesInListForTagSet(Set<ImmutablePair<Tag, Double>> ratedTags, List<MongoPlace> places) {

        return places.stream()
                .map(mongoPlace -> placesPool.get(mongoPlace.getSqlEntityId()))
                .filter(Objects::nonNull)
                .map(categoryWrapper -> categoryWrapper.getRankedPlaceForTagSet(ratedTags))
                .filter(rankable -> rankable.getRank() > 0)
                .sorted(Collections.reverseOrder())
                .collect(Collectors.toList());
    }

    public RankablePlaceWrapper getRankablePlace(Long id) {
        return placesPool.get(id);
    }

    public List<Rankable<Place>> getRankedPlaces() {

        return placesPool.values().stream()
                .map(RankablePlaceWrapper::getRankedPlace)
                .sorted(Collections.reverseOrder())
                .collect(Collectors.toList());
    }

    public RankableSuggestionCategoryWrapper addPlaceToPool(Place place, MongoPlace mongoPlace) {

        placesPool.put(place.getId(), RankablePlaceWrapper.of()
                .place(place.dropAdditionalInfo())
                .mongoPlace(mongoPlace)
                .rank(calculatePlaceRank(mongoPlace))
                .create());

        return this;
    }

    public RankableSuggestionCategoryWrapper addPlacesToPool(List<Pair<Place, MongoPlace>> places) {

        places.forEach(place ->
                addPlaceToPool(place.getKey().dropAdditionalInfo(), place.getValue()));

        return this;
    }

    public RankableSuggestionCategoryWrapper updatePlaceInPool(Place place, MongoPlace mongoPlace) {

        RankablePlaceWrapper placeWrapper = placesPool.get(place.getId());
        placeWrapper.setPlace(place.dropAdditionalInfo());
        placeWrapper.setMongoPlace(mongoPlace);
        placeWrapper.setRank(calculatePlaceRank(mongoPlace));

        return this;
    }

    public RankableSuggestionCategoryWrapper updatePlacesInPool(List<Pair<Place, MongoPlace>> places) {

        places.forEach(updateInfo ->
                addPlaceToPool(updateInfo.getKey().dropAdditionalInfo(), updateInfo.getValue()));

        return this;
    }

    public RankableSuggestionCategoryWrapper removePlaceFromPool(Long id) {
        placesPool.remove(id);
        return this;
    }

    public RankableSuggestionCategoryWrapper removePlacesFromPool(List<Long> placeIds) {
        placeIds.forEach(id -> placesPool.remove(id));
        return this;
    }

    public Double calculatePlaceRank(MongoPlace mongoPlace) {

        return getCategory().getTags().stream()
                .filter(mongoPlace::containsTag)
                .mapToDouble(tag -> getCategory().getTagRank(tag))
                .sum();
    }

    @Override
    public int compareTo(RankableSuggestionCategoryWrapper obj) {
        return Double.compare(this.getRank() == null ? 0 : this.getRank(), obj.getRank() == null ? 0 : obj.getRank());
    }

    @Override
    public int compare(RankableSuggestionCategoryWrapper o1, RankableSuggestionCategoryWrapper o2) {
        return Double.compare(o1.getRank() == null ? 0 : o1.getRank(), o2.getRank() == null ? 0 : o2.getRank());
    }

    public boolean isRelevantByAllergiesAndDiets(MultiValuedMap<TagType, Tag> userAllergiesAndDiets) {

        Collection<Tag> userAllergies = userAllergiesAndDiets.get(TagType.ALLERGY);

        if (!userAllergies.isEmpty()) {
            for (Tag tag : userAllergies) {
                if (category.getTags().contains(tag)) {
                    return false;
                }
            }
        }

        Collection<Tag> userDiet = userAllergiesAndDiets.get(TagType.DIET);
        if (!userDiet.isEmpty()) {
            for (Tag tag : userDiet) {
                if (category.getTags().contains(tag)) {
                    return true;
                }
            }
            return false;
        } else {
            return true;
        }

    }

}
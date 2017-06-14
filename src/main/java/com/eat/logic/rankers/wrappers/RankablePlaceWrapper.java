package com.eat.logic.rankers.wrappers;

import com.eat.models.b2b.Place;
import com.eat.models.common.Tag;
import com.eat.models.common.enums.TagType;
import com.eat.models.mongo.MongoPlace;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.geo.Circle;

import java.time.DayOfWeek;
import java.util.Collection;
import java.util.Comparator;
import java.util.Set;

@Data
@Builder(builderMethodName = "of", buildMethodName = "create")
public class RankablePlaceWrapper implements Comparator<RankablePlaceWrapper>, Comparable<RankablePlaceWrapper> {

    private long id;

    private Place place;

    private MongoPlace mongoPlace;

    private Double rank = 0d;

    public Double getPlaceRankForTagSet(Set<ImmutablePair<Tag, Double>> tags) {

        return tags.stream()
                .filter(tag -> mongoPlace.containsTag(tag.getKey()))
                .mapToDouble(Pair::getValue)
                .sum();
    }

    public Rankable<Place> getRankedPlaceForTagSet(Set<ImmutablePair<Tag, Double>> ratedTags) {

        return RankableWrapper.<Place>of()
                .rankedObject(getPlace())
                .rank(getPlaceRankForTagSet(ratedTags))
                .create();
    }

    public Rankable<Place> getRankedPlace() {

        return RankableWrapper.<Place>of()
                .rankedObject(getPlace())
                .rank(getRank())
                .create();
    }

    public boolean isRelevantByAllergiesAndDiets(MultiValuedMap<TagType, Tag> userAllergiesAndDiets) {

        Collection<Tag> userAllergies = userAllergiesAndDiets.get(TagType.ALLERGY);

        if (userAllergies != null) {
            for (Tag tag : userAllergies) {
                if (mongoPlace.getAllergies().contains(tag.getName().toLowerCase())) {
                    return false;
                }
            }
        }

        Collection<Tag> userDiet = userAllergiesAndDiets.get(TagType.DIET);
        if (userDiet != null) {
            for (Tag tag : userDiet) {
                if (mongoPlace.getDiets().contains(tag.getName().toLowerCase())) {
                    return true;
                }
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public int compare(RankablePlaceWrapper o1, RankablePlaceWrapper o2) {
        return Double.compare(o1 == null ? 0 : o1.getRank(),
                o2 == null ? 0 : o2.getRank());
    }

    @Override
    public int compareTo(RankablePlaceWrapper obj) {
        return Double.compare(this.getRank() == null ? 0 : this.getRank(),
                obj == null ? 0 : obj.getRank());
    }

    public boolean isRelevantForDayOfWeek(DayOfWeek dayOfWeek) {

        if (place != null) {
            return place.getSchedule().getOpenTimes().stream()
                    .anyMatch(dayOpenTime -> dayOpenTime.getDay() == dayOfWeek);
        } else if (mongoPlace != null) {
            return mongoPlace.getSchedule().stream()
                    .anyMatch(mongoDayOpenTime -> mongoDayOpenTime.getDay() == dayOfWeek);
        } else {
            return false;
        }
    }

    public boolean geoPositionInRadius(Circle circle) {

        double placeLatitude = 0;
        double placeLongtitude = 0;
        if(place != null){
            placeLatitude = place.getLatitude();
            placeLongtitude = place.getLongtitude();
        }else if(mongoPlace != null){
            placeLatitude = mongoPlace.getLocation()[0];
            placeLongtitude = mongoPlace.getLocation()[1];
        }else {
            return false;
        }

        double userLatitude = circle.getCenter().getX();
        double userLongtitude = circle.getCenter().getY();
        double userRadius = circle.getRadius().getValue() * 1000;

        final int R = 6371;

        Double latDistance = Math.toRadians(placeLatitude - userLatitude);
        Double lonDistance = Math.toRadians(placeLongtitude - userLongtitude);

        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(userLatitude)) * Math.cos(Math.toRadians(placeLatitude))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        //double height = el1 - el2;
        double height = 0;
        distance = Math.sqrt(Math.pow(distance, 2) + Math.pow(height, 2));

        return userRadius >= distance;
    }
}

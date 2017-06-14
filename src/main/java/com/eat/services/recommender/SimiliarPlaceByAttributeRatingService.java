package com.eat.services.recommender;

import com.eat.models.b2b.Place;
import com.eat.models.b2b.PlaceDetail;
import com.eat.models.b2b.enums.PlaceDetailType;
import com.eat.models.common.Tag;
import com.eat.models.recommender.SimiliarPlaceByAttributeRating;
import com.eat.repositories.sql.b2b.PlaceRepository;
import com.eat.repositories.sql.recommender.SimiliarPlaceByAttributeRatingRepository;
import com.eat.services.b2b.PlaceDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SimiliarPlaceByAttributeRatingService {

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private SimiliarPlaceByAttributeRatingRepository similiarPlaceRepository;

    @Autowired
    private PlaceDetailService detailService;

    public void addSimiliarPlacesToAllPlaces() {

        addSimiliarPlaces(placeRepository.findAll());

    }

    public void addSimiliarPlaces(List<Place> places) {

        List<Place> toPlaces = placeRepository.findAll();
        List<SimiliarPlaceByAttributeRating> similiarPlaces = new ArrayList<>();

        for (Place place : places) {
            for (Place toPlace : toPlaces) {
                if(place.equals(toPlace)){
                    continue;
                }
                Integer ratingSimiliarity = defineRatingSimiliarity(place, toPlace);
                if (ratingSimiliarity < 5) {
                    continue;
                }
                similiarPlaces.add(SimiliarPlaceByAttributeRating.of()
                        .place(place)
                        .similiarTo(toPlace)
                        .similiarityRatio(ratingSimiliarity)
                        .create());
            }
        }

        similiarPlaceRepository.save(similiarPlaces);
    }

    public void addSimiliarPlaces(Place place) {

        List<Place> places = placeRepository.findAll();
        List<SimiliarPlaceByAttributeRating> similiarPlaces = new ArrayList<>();

        for (Place currentPlace : places) {
            Integer ratingSimiliarity = defineRatingSimiliarity(place, currentPlace);
            if (ratingSimiliarity == 0) {
                continue;
            }
            similiarPlaces.add(SimiliarPlaceByAttributeRating.of()
                    .place(place)
                    .similiarTo(currentPlace)
                    .similiarityRatio(ratingSimiliarity)
                    .create());
        }

        similiarPlaceRepository.save(similiarPlaces);
    }

    public List<Place> getSimilarByProfilePlaces(Place place, double lowestSimiliariryRatio){
        
        List<Place> similiarPlaces = new ArrayList<>();
        
        List<Place> places = placeRepository.findAll();

        for (Place currentPlace : places) {
            if(placeSimiliarityRatioIsAbove(place, currentPlace, lowestSimiliariryRatio)){
                similiarPlaces.add(currentPlace);
            }
        }

        return similiarPlaces;
    }
    
    protected boolean placeSimiliarityRatioIsAbove(Place place, Place currentPlace, double lowestSimiliariryRatio){
        Integer similiarityAtrributeRating = defineRatingSimiliarity(place, currentPlace);
        if(similiarityAtrributeRating == 0){
            return false;
        }
        long placeTypes = place.getPlaceDetails().parallelStream()
                .filter(detail -> detail.getPlaceDetailType().equals(PlaceDetailType.PLACE_TYPE)).count();

        long placeCuisines = place.getPlaceDetails().parallelStream()
                .filter(detail -> detail.getPlaceDetailType().equals(PlaceDetailType.CUISINE)).count();

        long placeAtrributeNumber = placeTypes + placeCuisines + place.getTags().size();

        return new Double(similiarityAtrributeRating) / (double) placeAtrributeNumber >= lowestSimiliariryRatio;
    }

    private Integer defineRatingSimiliarity(Place place, Place similiarPlace) {
        int rating = 0;

        List<PlaceDetail> placeTypes = detailService.findAllByType(PlaceDetailType.PLACE_TYPE);
        for (PlaceDetail placeType : placeTypes) {
            for (PlaceDetail similiarPlaceType : similiarPlace.getPlaceDetails().stream()
                    .filter(detail -> detail.getPlaceDetailType().equals(PlaceDetailType.PLACE_TYPE))
                    .collect(Collectors.toList())) {
                if (placeType.equals(similiarPlaceType)) {
                    rating++;
                }
            }
        }

        List<PlaceDetail> cuisines = detailService.findAllByType(PlaceDetailType.CUISINE);
        for (PlaceDetail cuisine : cuisines) {
            for (PlaceDetail similiarPlaceCuisine : similiarPlace.getPlaceDetails().stream()
                    .filter(detail -> detail.getPlaceDetailType().equals(PlaceDetailType.CUISINE))
                    .collect(Collectors.toList())) {
                if (cuisine.equals(similiarPlaceCuisine)) {
                    rating++;
                }
            }
        }

        Set<Tag> tags = place.getTags();
        for (Tag tag : tags) {
            for (Tag similiarPlaceTag : similiarPlace.getTags()) {
                if(tag.getName().equals(similiarPlaceTag.getName())){
                    rating++;
                }
            }
        }

        return rating;
    }


}

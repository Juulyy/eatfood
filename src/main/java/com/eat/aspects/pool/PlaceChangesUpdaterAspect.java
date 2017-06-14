package com.eat.aspects.pool;

import com.eat.logic.b2c.SuggestionCategoryPool;
import com.eat.models.b2b.Place;
import com.eat.models.mongo.MongoPlace;
import com.eat.services.b2b.PlaceService;
import com.eat.services.common.ImageService;
import com.eat.services.mongo.b2b.MongoPlaceService;
import com.eat.utils.converters.MongoPlaceConverter;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PlaceChangesUpdaterAspect {

    @Autowired
    private MongoPlaceService mongoPlaceService;

    @Autowired
    private MongoPlaceConverter mongoConverter;

    @Autowired
    private SuggestionCategoryPool suggestionCategoryPool;

    @Autowired
    private ImageService imageService;

    @Autowired
    private PlaceService placeService;

    @After(value = "execution(* com.eat.services.common.ImageService.uploadFreePlaceCategoryImage(..))")
    public void afterManualFreePlaceCategorySetting() {
        suggestionCategoryPool.updateSuggestionCategories();
    }

    @After(value = "execution(* com.eat.services.b2b.PlaceService.save(..))")
    public void afterAddingPlace(JoinPoint joinPoint) {
        Place place = (Place) joinPoint.getArgs()[0];
        mongoPlaceService.save(mongoConverter.toMongoPlace(place));
//        suggestionCategoryPool.addPlacesToCategoriesPools(place);
        imageService.setPlaceCategoryImage(place.getId());

        suggestionCategoryPool.updateSuggestionCategories();
    }

    @After(value = "execution(* com.eat.services.b2b.PlaceService.delete(..))")
    public void afterDeletePlace(JoinPoint joinPoint) {
        MongoPlace mongoPlace;
        try {
            Place place = (Place) joinPoint.getArgs()[0];
            mongoPlace = mongoPlaceService.findBySqlEntityId(place.getId());
        } catch (ClassCastException e) {
            Long placeId = (Long) joinPoint.getArgs()[0];
            mongoPlace = mongoPlaceService.findBySqlEntityId(placeId);
        }
        mongoPlaceService.delete(mongoPlace);
//        suggestionCategoryPool.removePlacesFromCategoryPools(place);
        suggestionCategoryPool.updateSuggestionCategories();
    }

    @After(value = "execution(* com.eat.services.b2b.MenuService.updateMenu(..))")
    public void afterUpdatePlaceMenu(JoinPoint joinPoint) {
        Long placeId = (Long) joinPoint.getArgs()[1];
        Place place = placeService.findById(placeId);
        MongoPlace mongoPlace = mongoPlaceService.findBySqlEntityId(placeId);
        MongoPlace persistedMongoPlace = mongoConverter.toMongoPlace(place);
        persistedMongoPlace.setId(mongoPlace.getId());
        mongoPlaceService.save(persistedMongoPlace);
        suggestionCategoryPool.updateSuggestionCategories();
    }

    @After(value = "execution(* com.eat.services.b2b.PlaceService.updatePlacePageChanges(..))")
    public void afterUpdatePlace(JoinPoint joinPoint) {
        Long placeId = (Long) joinPoint.getArgs()[0];
        Place place = placeService.findById(placeId);
        MongoPlace mongoPlace = mongoPlaceService.findBySqlEntityId(placeId);
        MongoPlace persistedMongoPlace = mongoConverter.toMongoPlace(place);
        persistedMongoPlace.setId(mongoPlace.getId());
        mongoPlaceService.save(persistedMongoPlace); //changed method from mongoTemplate to MongoRepo
//        suggestionCategoryPool.updatePlacesInCategoryPools(place);
        suggestionCategoryPool.updateSuggestionCategories();
    }

}
package com.eat.controllers.recommender;

import com.eat.models.b2b.Place;
import com.eat.models.b2c.AppUser;
import com.eat.models.common.Tag;
import com.eat.models.elasticsearch.ESPlace;
import com.eat.models.elasticsearch.ESUser;
import com.eat.models.elasticsearch.recommender.ESUserFavouritePlace;
import com.eat.models.recommender.UserFavoritePlace;
import com.eat.repositories.sql.b2b.PlaceRepository;
import com.eat.repositories.sql.b2c.AppUserRepository;
import com.eat.repositories.sql.recommender.UserFavoritePlaceRepository;
import com.eat.services.elasticsearch.ESService;
import com.eat.services.elasticsearch.recommender.ESRecommenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * THIS CONTROLLER IS ONLY FOR FILLING ELASTICSEARCH FROM DATABASE!!!!!!!!
 * DO NOT USE IT TWICE!
 */
@Component
@RequestMapping(value = "/es/autofill")
public class AutofillController {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private UserFavoritePlaceRepository favoritePlaceRepository;

    @Autowired
    private ESService service;

    @Autowired
    private ESRecommenderService esRecommenderService;

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    @ResponseBody
    public String fillUsers() {
        int count = 0;
        List<AppUser> users = appUserRepository.findAll();
        System.out.println("There are " + users.size() + " users");
        for (AppUser user : users) {
            List<String> tags = new ArrayList<>();
            for (Tag tag : user.getUserPreferences().getTasteTags())
                tags.add(tag.getName());
            ESUser esUser = new ESUser(user.getId(), tags);
            try {
                service.indexNewObject(esUser, ESUser.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            count++;
        }
        System.out.println(count + " users added");
        return count + " users added";
    }

    @RequestMapping(value = "/places", method = RequestMethod.POST)
    @ResponseBody
    public String fillPlaces() {
        int count = 0;
        List<Place> places = placeRepository.findAll();
        System.out.println("There are " + places.size() + " places");
        for (Place place : places) {
            List<String> tags = new ArrayList<>();
            for (Tag tag : place.getTags())
                tags.add(tag.getName());
            ESPlace esPlace = new ESPlace(place.getId(), tags);
            try {
                service.indexNewObject(esPlace, ESPlace.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            count++;
        }
        System.out.println(count + " places added");
        return count + " places added";
    }

    @RequestMapping(value = "/favourites", method = RequestMethod.POST)
    @ResponseBody
    public String fillFavourites() {
        int count = 0;
        List<UserFavoritePlace> places = favoritePlaceRepository.findAll();
        System.out.println("There are " + places.size() + " likes");
        for (UserFavoritePlace place : places) {

            ESUserFavouritePlace esPlace = new ESUserFavouritePlace(place.getUser().getId(),
                    place.getPlace().getId(),
                    place.getPreferenceRatio());
            try {
                esRecommenderService.indexNewUserFavouritePlace(esPlace);
            } catch (IOException e) {
                e.printStackTrace();
            }
            count++;
        }
        System.out.println(count + " places added");
        return count + " places added";
    }
}

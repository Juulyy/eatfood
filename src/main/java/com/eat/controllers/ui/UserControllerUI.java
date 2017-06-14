package com.eat.controllers.ui;

import com.eat.models.b2b.Place;
import com.eat.models.b2c.AppUser;
import com.eat.services.b2c.AppUserService;
import com.eat.services.elasticsearch.recommender.ESRecommenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping(value = "/ui/users")
public class UserControllerUI {

    @Autowired
    private AppUserService userService;

    @Autowired
    private ESRecommenderService esRecommenderService;

    @GetMapping(value = "/show-all")
    public String showUsers(Model model) {
        model.addAttribute("userList", userService.findAll());
        return "users";
    }

    @GetMapping(value = "/details/{id}")
    public String editClientDetails(Model model, @PathVariable("id") Long id) {
        AppUser appUser = userService.findById(id);
        model.addAttribute("userDetails", appUser);
        //model.addAttribute("places", favoritePlaceService.getUserPlaces(appUser));
        return "userDetails";
    }

    /*@GetMapping(value = "/favourite_places")
    public String editClientDetails(Model model) {
        model.addAttribute("favouritePlaces", appUserRepository);
        return "favouritePlaces";
    }*/

    @GetMapping(value = "/recommendations/{id}")
    public String showUserRecommendations(Model model, @PathVariable("id") Long id) {
        AppUser appUser = userService.findById(id);
        model.addAttribute("userDetails", appUser);
        model.addAttribute("similarUsers", esRecommenderService.getSimilarUsers(id));
        Map<Place, Double> simpleMatchRecommendations = esRecommenderService.getSimpleMatchRecommendations(id);
        Map<Place, Double> recommendedPlaces = esRecommenderService.getRecommendedPlaces(id);
        model.addAttribute("simpleMatchPlaces", simpleMatchRecommendations);
        model.addAttribute("places", esRecommenderService.filterRecommendedPlaces(simpleMatchRecommendations, recommendedPlaces));
        return "userRecommendations";
    }

}

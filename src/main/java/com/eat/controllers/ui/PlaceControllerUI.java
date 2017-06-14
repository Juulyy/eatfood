package com.eat.controllers.ui;

import com.eat.models.b2b.Place;
import com.eat.services.b2b.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(value = "/ui/places")
public class PlaceControllerUI {

    @Autowired
    private PlaceService placeService;

    @GetMapping(value = "/show-all")
    public String showPlaces(Model model) {
        List<Place> placeList;
        placeList = placeService.findAll();
        model.addAttribute("placeList", placeList);
        return "places";
    }

    @GetMapping(value = "/details/{id}")
    public String showPlaceDetails(Model model, @PathVariable("id") Long id) {
        Place place = placeService.findById(id);
        model.addAttribute("placeDetails", place);
//        model.addAttribute("users", favoritePlaceService.getPlaceUsers(place));
        return "placeDetails";
    }


}

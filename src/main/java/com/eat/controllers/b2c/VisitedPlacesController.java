package com.eat.controllers.b2c;

import com.eat.models.b2c.VisitedPlaces;
import com.eat.services.b2c.VisitedPlacesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/place/visited")
public class VisitedPlacesController {

    @Autowired
    private VisitedPlacesService service;

    @PostMapping(value = "user/{id}/add", consumes = "application/json", produces = "application/json")
    public VisitedPlaces addPlace(@PathVariable("id") Long userId, @RequestParam("placeId") Long placeId) {
        return service.add(userId, placeId);
    }

    @GetMapping(value = "user/{id}/history", consumes = "application/json", produces = "application/json")
    public List<VisitedPlaces> findAllHistoryByPlace(@PathVariable("id") Long userId,
                                                     @RequestParam("placeId") Long placeId) {
        return service.findAllByUserAndPlace(userId, placeId);
    }

    @GetMapping(value = "user/{id}/find-all", consumes = "application/json", produces = "application/json")
    public List<VisitedPlaces> findAllUserVisitedPlaces(@PathVariable("id") Long userId) {
        return service.findAllByUser(userId);
    }

    @GetMapping(value = "/find-all", consumes = "application/json", produces = "application/json")
    public List<VisitedPlaces> findAll() {
        return service.findAll();
    }

}

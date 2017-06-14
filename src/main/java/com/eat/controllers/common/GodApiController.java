package com.eat.controllers.common;

import com.eat.controllers.foursquare.FoursquareController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/god")
public class GodApiController {

    @Autowired
    private AdministratorApiController administratorApiController;

    @Autowired
    private FoursquareController foursquareController;

    @GetMapping(value = "/fill")
    public HttpStatus baseDataFilling() {
        administratorApiController.fillPlaceDetails();
        administratorApiController.tagsFilling();
        foursquareController.fillBerlinPlaces(50);
        administratorApiController.baseDataFilling();
        return HttpStatus.OK;
    }

}
package com.eat.controllers.foursquare;

import com.eat.services.b2b.PlaceDetailService;
import com.eat.services.common.TagService;
import com.eat.utils.foursquare.FoursquarePlacesService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/foursquare")
@ApiModel(value = "to fill out data from Foursquare app")
public class FoursquareController {

    @Autowired
    private FoursquarePlacesService placesService;

    @Autowired
    private PlaceDetailService detailService;

    @Autowired
    private TagService tagService;

    /*@PostMapping(value = "/new", consumes = "application/json", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "location", value = "concrete location", required = true, dataType = "string",
                    paramType = "query"),
            @ApiImplicitParam(name = "size", value = "size of downloaded desired data", required = true,
                    dataType = "integer", paramType = "query")
    })
    public ResponseEntity<String> fill(@RequestParam String location, @RequestParam Integer size) {
        if (detailService.findAllByType(PlaceDetailType.PLACE_TYPE).size() == 0) {
            return new ResponseEntity<>("Before this, please - fill place type details!", HttpStatus.BAD_REQUEST);
        }
        return placesService.getAndSaveByLocation(location, size);
    }*/

    @GetMapping(value = "/fill-berlin-places", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Saved <quantity> EAT places!"),
            @ApiResponse(code = 404, message = "There are no places for this category!"),
            @ApiResponse(code = 403, message = "Error!")})
    public ResponseEntity<String> fillBerlinPlaces(@RequestParam(value = "size", required = false) Integer size) {
        if (detailService.findAll().isEmpty() || tagService.findAll().isEmpty()) {
            return new ResponseEntity<>("Before this, please - fill place type details & tags!", HttpStatus.BAD_REQUEST);
        }
        return placesService.getAndSaveByLocation("52.516067,13.376975", size);
    }

    @GetMapping(value = "/fill-berlin-places-by-location", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Saved <quantity> EAT places!"),
            @ApiResponse(code = 404, message = "There are no places for this category!"),
            @ApiResponse(code = 403, message = "Error!")})
    public ResponseEntity<String> fillBerlinPlacesByLocation(@RequestParam(value = "size", required = false) Integer size,
                                                             @RequestParam(value = "long") String longtitude,
                                                             @RequestParam(value = "lat") String latitude) {
        if (detailService.findAll().isEmpty() || tagService.findAll().isEmpty()) {
            return new ResponseEntity<>("Before this, please - fill place type details & tags!", HttpStatus.BAD_REQUEST);
        }
        return placesService.getAndSaveByLocation(longtitude.concat(",").concat(latitude), size);
    }

}
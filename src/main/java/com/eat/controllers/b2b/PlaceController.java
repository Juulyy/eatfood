package com.eat.controllers.b2b;

import com.eat.dto.b2b.PlaceDto;
import com.eat.dto.b2b.PlaceRecommendationsDto;
import com.eat.factories.EntityFactory;
import com.eat.models.b2b.Place;
import com.eat.models.common.Tag;
import com.eat.models.mongo.MongoPlace;
import com.eat.services.b2b.PlaceService;
import com.eat.services.common.MenuItemsGeneratorService;
import com.eat.services.mongo.b2b.MongoPlaceService;
import com.eat.utils.converters.dto.PlaceDtoConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(value = "/api/place")
public class PlaceController {

    @Autowired
    private PlaceService placeService;

    @Autowired
    private EntityFactory entityFactory;

    @Autowired
    private PlaceDtoConverter converter;

    @Autowired
    private MenuItemsGeneratorService menuItemsGeneratorService;

    @Autowired
    private MongoPlaceService mongoPlaceService;

    @GetMapping(value = "/find-all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Place> listAllCustomers() {
        return placeService.findAll();
    }

    @GetMapping(value = "find-all-v2", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PlaceDto> findAllPlaces() {
        return converter.toPlaceDtoList(placeService.findAll());
    }

    /**
     * POST method to find Place by ID
     *
     * @param id - Place entity ID
     * @return http://localhost:8090/api/establishment/details/5
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Place findById(@PathVariable("id") Long id) {
        return placeService.findById(id);
    }

    /**
     * POST secured method to add new Place
     *
     * @param placeRaw - request json raw for Place entity
     */
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Place addPlace(@RequestBody String placeRaw) {
        Place place = placeService.save(entityFactory.createInstance(placeRaw, Place.class));
        menuItemsGeneratorService.processMenuGeneration(place);
        placeService.fillTagsFromPlaceDetailsAfterMenuGeneration(place);
        return place;
    }

    @PatchMapping(value = "/{id}/page", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updatePlacePage(@PathVariable(value = "id") Long placeId, @RequestBody String placeRaw) {
        placeService.updatePlacePageChanges(placeId, entityFactory.createInstance(placeRaw, Place.class));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deletePlace(@PathVariable(value = "id") Long placeId) {
        placeService.delete(placeId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/get-place-recommendation-number/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Integer getPlaceRecommendationsCount(@PathVariable(value = "id") Long placeId){
        return placeService.getPlaceRecommendationsCount(placeId);
    }

    @GetMapping(value = "/get-places-recommendation-number", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PlaceRecommendationsDto> getPlacesRecommendationsCount(@RequestParam(value = "ids") List<Long> ids){
        return placeService.getPlacesRecommendationsCount(ids);
    }

    @PostMapping(value = "/find-by-tags", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<PlaceDto> findAllByTags(@RequestBody List<Tag> tags) {
        List<MongoPlace> mongoPlaces = mongoPlaceService.findByTags(tags);
        List<Long> placeIds = mongoPlaces.stream()
                .map(MongoPlace::getSqlEntityId)
                .collect(Collectors.toList());
        return converter.toPlaceDtoList(placeService.findPlacesByIds(placeIds));
    }

}
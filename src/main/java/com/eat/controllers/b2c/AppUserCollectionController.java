package com.eat.controllers.b2c;

import com.eat.models.b2c.AppUserCollection;
import com.eat.models.b2c.enums.AppUserCollectionType;
import com.eat.models.common.enums.CrudMethod;
import com.eat.services.b2c.AppUserCollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/collections/")
public class AppUserCollectionController {

    @Autowired
    private AppUserCollectionService collectionService;

    @GetMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public AppUserCollection getById(@PathVariable("id") Long collectionId) {
        return collectionService.findById(collectionId);
    }

    @GetMapping(value = "/find-all", consumes = "application/json", produces = "application/json")
    public List<AppUserCollection> getAll() {
        return collectionService.findAll();
    }

    @PostMapping(value = "/custom/create", consumes = "application/json", produces = "application/json")
    public AppUserCollection addCustomCollection(@RequestParam("userId") Long userId,
                                                 @RequestParam("name") String name) {
        return collectionService.createCollection(userId, AppUserCollectionType.CUSTOM, name);
    }

    @DeleteMapping(value = "/custom/remove/{id}", consumes = "application/json", produces = "application/json")
    public HttpStatus removeCustomCollection(@RequestParam("userId") Long userId,
                                             @PathVariable("id") Long collectionId) {
        collectionService.removeCustomCollection(userId, collectionId);
        return HttpStatus.OK;
    }

    @PatchMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public AppUserCollection updateCollection(@RequestBody AppUserCollection collection) {
        return collectionService.update(collection);
    }

    @PutMapping(value = "/recommend/offer/{id}", consumes = "application/json", produces = "application/json")
    public HttpStatus recommendOffer(@PathVariable("id") Long offerId, @RequestParam("userId") Long userId) {
        collectionService.recommendOffer(offerId, userId);
        return HttpStatus.OK;
    }

    @PutMapping(value = "/recommend/place/{id}", consumes = "application/json", produces = "application/json")
    public HttpStatus recommendPlace(@PathVariable("id") Long placeId, @RequestParam("userId") Long userId) {
        collectionService.recommendPlace(placeId, userId);
        return HttpStatus.OK;
    }

    @PutMapping(value = "/recommend/feature/{id}", consumes = "application/json", produces = "application/json")
    public HttpStatus recommendFeature(@PathVariable("id") Long featureId, @RequestParam("userId") Long userId) {
        collectionService.recommendFeature(featureId, userId);
        return HttpStatus.OK;
    }

    @PutMapping(value = "/recommend/menu-item/{id}", consumes = "application/json", produces = "application/json")
    public HttpStatus recommendMenuItem(@PathVariable("id") Long menuItemId, @RequestParam("userId") Long userId) {
        collectionService.recommendMenuItem(menuItemId, userId);
        return HttpStatus.OK;
    }

    @PutMapping(value = "/follow/place/{id}", consumes = "application/json", produces = "application/json")
    public HttpStatus followPlace(@PathVariable("id") Long placeId, @RequestParam("userId") Long userId) {
        collectionService.followingPlacesManagement(placeId, userId, CrudMethod.PUT);
        return HttpStatus.OK;
    }

    @DeleteMapping(value = "/unfollow/place/{id}", consumes = "application/json", produces = "application/json")
    public HttpStatus unfollowPlace(@PathVariable("id") Long placeId, @RequestParam("userId") Long userId) {
        collectionService.followingPlacesManagement(placeId, userId, CrudMethod.DELETE);
        return HttpStatus.OK;
    }

    @PutMapping(value = "/follow/collection/{id}", consumes = "application/json", produces = "application/json")
    public HttpStatus followCollection(@PathVariable("id") Long collId, @RequestParam("userId") Long userId) {
        collectionService.followCollection(collId, userId);
        return HttpStatus.OK;
    }

    @DeleteMapping(value = "/unfollow/collection/{id}", consumes = "application/json", produces = "application/json")
    public HttpStatus unfollowCollection(@PathVariable("id") Long collId, @RequestParam("userId") Long userId) {
        collectionService.unfollowCollection(collId, userId);
        return HttpStatus.OK;
    }

    @PutMapping(value = "/add/place/{id}", consumes = "application/json", produces = "application/json")
    public HttpStatus addPlace(@PathVariable("id") Long placeId, @RequestParam("collId") Long collId) {
        collectionService.addPlace(placeId, collId);
        return HttpStatus.OK;
    }

    @DeleteMapping(value = "/remove/place/{id}", consumes = "application/json", produces = "application/json")
    public HttpStatus removePlace(@PathVariable("id") Long placeId, @RequestParam("collId") Long collId) {
        collectionService.removePlace(placeId, collId);
        return HttpStatus.OK;
    }

    @PutMapping(value = "/add/offer/{id}", consumes = "application/json", produces = "application/json")
    public HttpStatus addOffer(@PathVariable("id") Long offerId, @RequestParam("collId") Long collId) {
        collectionService.addOffer(offerId, collId);
        return HttpStatus.OK;
    }

    @DeleteMapping(value = "/remove/offer/{id}", consumes = "application/json", produces = "application/json")
    public HttpStatus removeOffer(@PathVariable("id") Long offerId, @RequestParam("collId") Long collId) {
        collectionService.removeOffer(offerId, collId);
        return HttpStatus.OK;
    }

}

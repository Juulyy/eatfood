package com.eat.services.b2c;

import com.eat.controllers.exceptions.DataNotFoundException;
import com.eat.models.b2b.MenuItem;
import com.eat.models.b2b.Offer;
import com.eat.models.b2b.Place;
import com.eat.models.b2b.PlaceDetail;
import com.eat.models.b2c.AppUser;
import com.eat.models.b2c.AppUserCollection;
import com.eat.models.b2c.enums.AppUserCollectionType;
import com.eat.models.b2c.enums.PrivacyType;
import com.eat.models.common.enums.CrudMethod;
import com.eat.repositories.sql.b2c.AppUserCollectionRepository;
import com.eat.services.b2b.MenuItemService;
import com.eat.services.b2b.OfferService;
import com.eat.services.b2b.PlaceDetailService;
import com.eat.services.b2b.PlaceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AppUserCollectionService {

    @Autowired
    private AppUserCollectionRepository repository;

    @Autowired
    private AppUserService userService;

    @Autowired
    private OfferService offerService;

    @Autowired
    private PlaceService placeService;

    @Autowired
    private PlaceDetailService placeDetailService;

    @Autowired
    private MenuItemService menuItemService;

    public void recommendFeature(Long userId, Long featureId) {
        PlaceDetail feature = placeDetailService.findById(featureId);
        AppUserCollection recFeatures = findAppUserCollectionByType(userId, AppUserCollectionType.RECOMMENDED_FEATURES);
        if (recFeatures != null && feature != null && feature.isFeature()) {
            recFeatures.getFeatures().add(feature);
            update(recFeatures);
        }
    }

    public void recommendMenuItem(Long userId, Long menuItemId) {
        MenuItem menuItem = menuItemService.findById(menuItemId);
        AppUserCollection recMenuItems = findAppUserCollectionByType(userId, AppUserCollectionType.RECOMMENDED_MENU_ITEMS);
        if (recMenuItems != null && menuItem != null) {
            recMenuItems.getMenuItems().add(menuItem);
            update(recMenuItems);
        }
    }

    public void recommendOffer(Long offerId, Long userId) {
        Offer offer = offerService.findById(offerId);
        AppUserCollection recOffers = findAppUserCollectionByType(userId, AppUserCollectionType.RECOMMENDED_OFFERS);
        if (offer != null && recOffers != null) {
            recOffers.getOffers().add(offer);
            update(recOffers);
        }
    }

    public void recommendPlace(Long userId, Long placeId) {
        Place place = placeService.findById(placeId);
        AppUserCollection recPlaces = findAppUserCollectionByType(userId, AppUserCollectionType.RECOMMENDED_PLACES);
        if (recPlaces != null && place != null) {
            recPlaces.getPlaces().add(place);
            update(recPlaces);
        }
    }

    public void followingPlacesManagement(Long placeId, Long userId, CrudMethod method) {
        Place place = placeService.findById(placeId);
        AppUserCollection recPlaces = findAppUserCollectionByType(userId, AppUserCollectionType.FOLLOWED_PLACES);
        if (place != null && recPlaces != null) {
            switch (method) {
                case PUT:
                    recPlaces.getPlaces().add(place);
                    break;
                case DELETE:
                    recPlaces.getPlaces().remove(place);
                    break;
            }
            update(recPlaces);
        }
        throw new DataNotFoundException();
    }

/*    public void followingCollectionsManagement(Long collId, Long userId, CrudMethod method) {
        AppUserCollection collection = findById(collId);
        AppUserCollection followedCollections = findAppUserCollectionByType(userId,
                AppUserCollectionType.FOLLOWED_COLLECTIONS);
        if (collection != null) {

        }
    }*/

    public void followCollection(Long collId, Long userId) {
        AppUserCollection collection = findById(collId);
        AppUserCollection followedCollections = findAppUserCollectionByType(userId,
                AppUserCollectionType.FOLLOWED_COLLECTIONS);
        if (collection != null && followedCollections != null) {
            followedCollections.getFollowingCollections().add(collection);
            update(followedCollections);
        }
    }

    public void unfollowCollection(Long collId, Long userId) {
        AppUserCollection collection = findById(collId);
        AppUserCollection followedCollections = findAppUserCollectionByType(userId,
                AppUserCollectionType.FOLLOWED_COLLECTIONS);
        if (collection != null && followedCollections != null) {
            followedCollections.getFollowingCollections().remove(collection);
            update(followedCollections);
        }
    }

    public void addPlace(Long placeId, Long collId) {
        Place place = placeService.findById(placeId);
        AppUserCollection collection = findById(collId);
        if (place != null && collection != null) {
            collection.getPlaces().add(place);
            update(collection);
        }
    }

    public void removePlace(Long placeId, Long collId) {
        Place place = placeService.findById(placeId);
        AppUserCollection collection = findById(collId);
        if (place != null && collection != null) {
            collection.getPlaces().remove(place);
            update(collection);
        }
    }

    public void addOffer(Long offerId, Long collId) {
        Offer offer = offerService.findById(offerId);
        AppUserCollection collection = findById(collId);
        if (offer != null && collection != null) {
            collection.getOffers().add(offer);
            update(collection);
        }
    }

    public void removeOffer(Long offerId, Long collId) {
        Offer offer = offerService.findById(offerId);
        AppUserCollection collection = findById(collId);
        if (offer != null && collection != null) {
            collection.getOffers().remove(offer);
            update(collection);
        }
    }

    public AppUserCollection createCollection(Long userId, AppUserCollectionType type, String name) {
        AppUser user = userService.findById(userId);
        if (user != null) {
            AppUserCollection collection = null;
            switch (type) {
                case RECOMMENDED_PLACES:
                    collection = AppUserCollection.of()
                            .collectionName(AppUserCollectionType.RECOMMENDED_PLACES.getType())
                            .collectionType(AppUserCollectionType.RECOMMENDED_PLACES)
                            .create();
                    break;
                case RECOMMENDED_OFFERS:
                    collection = AppUserCollection.of()
                            .collectionName(AppUserCollectionType.RECOMMENDED_OFFERS.getType())
                            .collectionType(AppUserCollectionType.RECOMMENDED_OFFERS)
                            .create();
                    break;
                case RECOMMENDED_FEATURES:
                    collection = AppUserCollection.of()
                            .collectionName(AppUserCollectionType.RECOMMENDED_FEATURES.getType())
                            .collectionType(AppUserCollectionType.RECOMMENDED_FEATURES)
                            .create();
                    break;
                case RECOMMENDED_MENU_ITEMS:
                    collection = AppUserCollection.of()
                            .collectionName(AppUserCollectionType.RECOMMENDED_MENU_ITEMS.getType())
                            .collectionType(AppUserCollectionType.RECOMMENDED_MENU_ITEMS)
                            .create();
                    break;
                case FOLLOWED_PLACES:
                    collection = AppUserCollection.of()
                            .collectionName(AppUserCollectionType.FOLLOWED_PLACES.getType())
                            .collectionType(AppUserCollectionType.FOLLOWED_PLACES)
                            .create();
                    break;
                case FOLLOWED_COLLECTIONS:
                    collection = AppUserCollection.of()
                            .collectionName(AppUserCollectionType.FOLLOWED_COLLECTIONS.getType())
                            .collectionType(AppUserCollectionType.FOLLOWED_COLLECTIONS)
                            .create();
                    break;
                case CUSTOM:
                    collection = AppUserCollection.of()
                            .collectionName(name)
                            .collectionType(AppUserCollectionType.CUSTOM)
                            .create();
                    break;
            }
            collection.setAppUserPreference(user.getUserPreferences());
            collection.setCollectionPrivacy(PrivacyType.MUTUAL_FOLLOWING);
            return repository.save(collection);
        }
        throw new DataNotFoundException();
    }

    /**
     * can only delete collections with custom & followed_collections type
     */
    public void removeCustomCollection(Long userId, Long collectionId) {
        AppUserCollection collection = findAppUserCollectionById(userId, collectionId);
        if (collection != null && collection.getCollectionType().equals(AppUserCollectionType.CUSTOM)) {
            delete(collection);
        }
    }

    public AppUserCollection findAppUserCollectionByType(Long userId, AppUserCollectionType type) {
        AppUser appUser = userService.findById(userId);
        if (appUser != null) {
            return repository.findAppUserCollectionByType(appUser.getUserPreferences(), type);
        }
        throw new DataNotFoundException();
    }

    public List<AppUserCollection> findAppUserCollectionsByType(Long userId, AppUserCollectionType type) {
        AppUser appUser = userService.findById(userId);
        if (appUser != null) {
            return repository.findAppUserCollectionsByType(appUser.getUserPreferences(), type);
        }
        throw new DataNotFoundException();
    }

    public List<AppUserCollection> findAll() {
        return repository.findAll();
    }

    public AppUserCollection findAppUserCollectionById(Long userId, Long collectionId) {
        AppUser appUser = userService.findById(userId);
        if (appUser != null) {
            return repository.findAppUserCollectionById(collectionId, appUser.getUserPreferences());
//            TODO try and test this impl with Optional
        }
        throw new DataNotFoundException();
    }

    public AppUserCollection findById(Long id) {
        return repository.findOne(id);
    }

    public AppUserCollection save(AppUserCollection appUserCollection) {
        return repository.save(appUserCollection);
    }

    public void save(List<AppUserCollection> appUserCollections) {
        repository.save(appUserCollections);
    }

    public AppUserCollection update(AppUserCollection appUserCollection) {
        return repository.save(appUserCollection);
    }

    public void update(List<AppUserCollection> appUserCollections) {
        repository.save(appUserCollections);
    }

    public void delete(AppUserCollection appUserCollection) {
        repository.delete(appUserCollection);
    }

    public void delete(Long id) {
        repository.delete(id);
    }

}
package com.eat.models.mongo;

import com.eat.models.b2b.Place;
import com.eat.models.mongo.enums.ChangeRequestType;

@FunctionalInterface
public interface Convertible {

    default AbstractPlaceChangeRequest toRequestObject(final Place place, final ChangeRequestType type) {
        AbstractPlaceChangeRequest object = null;
        switch (type) {
            case MENUS:
                object = PlaceMenusChangeRequest.of()
                        .menus(place.getMenus())
                        .create();
                break;
            case OFFERS:
                object = PlaceOffersChangeRequest.of()
                        .offers(place.getOffers())
                        .create();
                break;
            case PLACE_PAGE:
                object = PlacePageChangeRequest.of()
                        .name(place.getName())
                        .longtitude(place.getLongtitude())
                        .latitude(place.getLatitude())
                        .contacts(place.getContacts())
                        .schedule(place.getSchedule())
                        .placeDetails(place.getPlaceDetails())
                        .priceLevel(place.getPriceLevel())
                        .businessPlan(place.getBusinessPlan())
                        .create();
                break;
            case PHOTO_SHOOT:
                break;
        }
        return object;
    }

    Place toPlace();

}
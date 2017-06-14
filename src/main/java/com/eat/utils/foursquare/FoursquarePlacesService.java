package com.eat.utils.foursquare;

import com.eat.models.b2b.Contact;
import com.eat.models.b2b.ContactType;
import com.eat.models.b2b.Place;
import com.eat.models.b2b.PlaceDetail;
import com.eat.models.b2b.enums.BusinessPlan;
import com.eat.models.b2b.enums.PlaceDetailType;
import com.eat.models.common.Tag;
import com.eat.models.common.enums.TagType;
import com.eat.services.b2b.ContactTypeService;
import com.eat.services.b2b.PlaceDetailService;
import com.eat.services.b2b.PlaceService;
import com.eat.services.common.TagService;
import fi.foyt.foursquare.api.FoursquareApi;
import fi.foyt.foursquare.api.FoursquareApiException;
import fi.foyt.foursquare.api.Result;
import fi.foyt.foursquare.api.entities.CompactVenue;
import fi.foyt.foursquare.api.entities.VenuesSearchResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Component
public class FoursquarePlacesService {

    @Autowired
    private PlaceService placeService;

    @Autowired
    private ContactTypeService contactTypeService;

    @Autowired
    private FoursquareApi foursquareApi;

    @Autowired
    private TagService tagService;

    @Autowired
    private PlaceDetailService detailService;

    public static final String CLIENT_ID = "DDUFTWU5OZKPPACDK0JDU5LU1XSNTB5P2VAWCFEJETRUBH10";
    public static final String CLIENT_SECRET = "4CPUGOANOH5H0I4UULHHHL4TI1A3JIMDEFMD4J2OZJKVOXFO";

    public final String FOURSQUARE_BLANK = "Foursquare blank";

    private final String ADDRESS_CONTACT_TYPE_NAME = "address";
    private final String PHONE_CONTACT_TYPE_NAME = "phone";

    private Integer counter;

    private Set<PlaceDetail> cachedDetails;

    public PlaceDetail getBlankPlaceType() {
        return PlaceDetail.of()
                .name(FOURSQUARE_BLANK)
                .placeDetailType(PlaceDetailType.PLACE_TYPE)
                .create();
    }

    public ContactType getAddressContactType() {
        ContactType contactType = contactTypeService.findByName(ADDRESS_CONTACT_TYPE_NAME);
        if (contactType == null) {
            return contactTypeService.save(ContactType.of()
                    .contactAspect(ContactType.ContactAspect.ADDRESS)
                    .name(ADDRESS_CONTACT_TYPE_NAME)
                    .create());
        }
        return contactType;
    }

    public ContactType getPhoneContactType() {
        ContactType contactType = contactTypeService.findByName(PHONE_CONTACT_TYPE_NAME);
        if (contactType == null) {
            return contactTypeService.save(ContactType.of()
                    .contactAspect(ContactType.ContactAspect.PHONE)
                    .name(PHONE_CONTACT_TYPE_NAME)
                    .create());
        }
        return contactType;
    }

    public List<PlaceDetail> createOrGetPlaceType(CompactVenue venue, String name) {
        PlaceDetail placeType;
        if (venue.getCategories().length > 0) {
            placeType = PlaceDetail.of()
                    .name(name)
                    .placeDetailType(PlaceDetailType.PLACE_TYPE)
                    .create();
        } else {
            placeType = getBlankPlaceType();
        }

        PlaceDetail detailPlaceType = detailService.findByName(name);
        if (detailPlaceType == null) {
            detailPlaceType = detailService.saveForFoursquare(placeType);
        }
        return Arrays.asList(detailPlaceType);
    }

    public Place setPlaceTypeAndCuisine(CompactVenue venue, Place place, PlaceCategoryTemplate template) {
        place.setPlaceDetails(new HashSet<>());

        place.getPlaceDetails().addAll(createOrGetPlaceType(venue, template.getPlaceType()));

        place.setTags(getPlacesTags(template));

//        TODO add placeDetails

        template.getInteriorTags()
                .forEach(interiorTag -> place.getPlaceDetails()
                        .add(createOrGetPlaceDetail(interiorTag, PlaceDetailType.INTERIOR)));

        template.getFeatureTags()
                .forEach(featureTag -> place.getPlaceDetails()
                        .add(createOrGetPlaceDetail(featureTag, PlaceDetailType.FEATURE)));

        template.getCuisineTags()
                .forEach(cuisineTag -> place.getPlaceDetails()
                        .add(createOrGetPlaceDetail(cuisineTag, PlaceDetailType.CUISINE)));

        return place;
    }

    public PlaceDetail createOrGetPlaceDetail(String name, PlaceDetailType detailType) {
        PlaceDetail detail;
        if (cachedDetails.size() > 0) {
            for (PlaceDetail businessDetail : cachedDetails) {
                if (businessDetail.getName().equals(name) && businessDetail.getPlaceDetailType().equals(detailType)) {
                    return businessDetail;
                }
            }
        }
        detail = createOrGetDetailType(name, detailType);
        cachedDetails.add(detail);
        return detail;
    }

    public PlaceDetail createOrGetDetailType(String detailName, PlaceDetailType detailType) {
        PlaceDetail detail = detailService.findByName(detailName);
        if (detail != null) {
            return detail;
        }
        return detailService.saveForFoursquare(PlaceDetail.of()
                .name(detailName)
                .placeDetailType(detailType)
                .create());
    }

    public Set<Tag> getPlacesTags(PlaceCategoryTemplate template) {
        Set<Tag> tags = new HashSet<>();
        Set<Tag> cachedTags = tagService.getAll();

        tags.add(createOrGetPlaceTag(template.getPlaceType(), TagType.PLACE_TYPE, cachedTags));

        for (String tagName : template.getCuisineTags()) {
            tags.add(createOrGetPlaceTag(tagName, TagType.CUISINE, cachedTags));
        }
        for (String tagName : template.getFeatureTags()) {
            tags.add(createOrGetPlaceTag(tagName, TagType.FEATURE, cachedTags));
        }
        for (String tagName : template.getInteriorTags()) {
            tags.add(createOrGetPlaceTag(tagName, TagType.INTERIOR, cachedTags));
        }
        return tags;
    }

    public Tag createOrGetPlaceTag(String tagName, TagType type, Set<Tag> cachedTags) {
        Tag tag = null;
        for (Tag cachedTag : cachedTags) {
            if (cachedTag.getType() == type && cachedTag.getName().equals(tagName)) {
                tag = cachedTag;
            }
        }

        if (tag == null) {
            tag = tagService.save(Tag.of()
                    .name(tagName)
                    .type(type)
                    .create());

            cachedTags.add(tag);
        }
        return tag;
    }

    public String parseLocation(CompactVenue venue) {
        if (venue.getLocation().getCity() == null || venue.getLocation().getAddress() == null) {
            return FOURSQUARE_BLANK;
        }
        return venue.getLocation().getCity().concat(", ").concat(venue.getLocation().getAddress());
    }

    public String parsePhone(CompactVenue venue) {
        if (venue.getContact().getPhone() == null) {
            return FOURSQUARE_BLANK;
        }
        return venue.getContact().getPhone();
    }

    public Set<Contact> getContactList(CompactVenue venue) {
        Set<Contact> contacts = new HashSet<>();
        contacts.add(Contact.of()
                .type(getAddressContactType())
                .contactDetails(parseLocation(venue))
                .create());
        contacts.add(Contact.of()
                .type(getPhoneContactType())
                .contactDetails(parsePhone(venue))
                .create());
        return contacts;
    }

    public void buildPlace(CompactVenue venue, PlaceCategoryTemplate placeTemplate) {
        Place place = Place.of()
                .isActive(true)
                .name(venue.getName())
                .latitude(venue.getLocation().getLat())
                .longtitude(venue.getLocation().getLng())
                .creationDate(LocalDateTime.now())
                .businessPlan(BusinessPlan.FREE)
                .isLuxury(false)
                .contacts(getContactList(venue))
                .create();
        savePlace(setPlaceTypeAndCuisine(venue, place, placeTemplate));
    }

    public void savePlace(Place place) {
        Place placeFromDB = placeService.findByNameAndLatitudeAndLongtitude(place);
        if (placeFromDB == null) {
            placeService.saveForFoursquare(place);
            counter++;
        }
    }

    public ResponseEntity<String> getAndSaveByLocation(String location, Integer size) {
        cachedDetails = new HashSet<>(detailService.findAll());
        counter = 0;
        Result<VenuesSearchResult> result;
        Map<String, PlaceCategoryTemplate> categoriesMap = PlaceCategoryTemplate.getPlaceCategoryTemplates();
        for (String categoryId : categoriesMap.keySet()) {
            PlaceCategoryTemplate placeTemplate = categoriesMap.get(categoryId);
            try {
                result = foursquareApi.venuesSearch(location, null, null, null, null, 50,
                        null, categoryId, null, null, null, 100000, null);
            } catch (FoursquareApiException e) {
                e.printStackTrace();
                return new ResponseEntity<>("Error! ".concat(e.getMessage()), HttpStatus.BAD_REQUEST);
            }
            if (result.getResult() == null) {
                return new ResponseEntity<>("There are no places for this category!", HttpStatus.NOT_FOUND);
            }
            CompactVenue[] venues = result.getResult().getVenues();
            for (int i = 0; i < venues.length; i++) {
                buildPlace(venues[i], placeTemplate);
                if (size != null && Objects.equals(size, counter)) {
                    return new ResponseEntity<>("Saved ".concat(counter.toString()).concat(" EAT places!"), HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>("Saved ".concat(counter.toString()).concat(" EAT places!"), HttpStatus.OK);
    }

}

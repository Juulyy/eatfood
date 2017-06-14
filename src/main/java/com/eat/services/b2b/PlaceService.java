package com.eat.services.b2b;

import com.clearspring.analytics.util.Lists;
import com.eat.dto.b2b.PlaceRecommendationsDto;
import com.eat.models.b2b.Menu;
import com.eat.models.b2b.MenuItem;
import com.eat.models.b2b.Place;
import com.eat.models.b2b.PlaceDetail;
import com.eat.models.common.Image;
import com.eat.models.common.Tag;
import com.eat.repositories.sql.b2b.PlaceRepository;
import com.eat.services.common.TagService;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PlaceService {

    @Autowired
    private PlaceRepository repository;

    @Autowired
    private TagService tagService;

    @PersistenceContext
    private EntityManager entityManager;

    public Long count() {
        return repository.count();
    }

    public Place findById(Long id) {
        return repository.findOne(id);
    }

    public Place saveForFoursquare(Place place) {
        return repository.save(place);
    }

    private Image setDefaultPlaceCategoryImage() {
        return null;
    }

    public void fillTagsFromPlaceDetailsAfterMenuGeneration(Place place) {
        HashSet<String> tagNames = Sets.newHashSet();

        Set<String> detailNames = place.getPlaceDetails().stream()
                .map(PlaceDetail::getName)
                .collect(Collectors.toSet());

        tagNames.addAll(detailNames);

        Set<String> menuItemTagNames = place.getMenus().stream()
                .map(Menu::getMenuItems)
                .flatMap(Collection::stream)
                .map(MenuItem::getTags)
                .flatMap(Collection::stream)
                .map(Tag::getName)
                .collect(Collectors.toSet());

        tagNames.addAll(menuItemTagNames);

        Set<Tag> detailTags = tagService.findByNameIn(tagNames);
        place.getTags().addAll(detailTags);
        update(place);
    }

    public Place save(Place place) {
        place.setCreationDate(LocalDateTime.now());
        Set<String> detailNames = place.getPlaceDetails().stream()
                .map(PlaceDetail::getName)
                .collect(Collectors.toSet());
        Set<Tag> detailTags = tagService.findByNameIn(detailNames);
        place.setImages(new HashSet<>());
        place.setTags(detailTags);
        if (place.getSchedule() != null) {
            place.getSchedule().setPlace(place);
        }
        place.setMenus(Sets.newHashSet(Menu.of()
                .name("Main")
                .place(place)
                .create()));
        return repository.save(place);
    }

    public List<Place> getPlacesInRadius(Double latitude, Double longtitude, Integer radius) {
        return repository.getPlacesInRadius(latitude, longtitude, radius);
    }

    public Place update(Place place) {
        return repository.save(place);
    }

    public void updateAll(List<Place> places) {
        repository.save(places);
    }

    public void delete(Place place) {
        repository.delete(place);
    }

    public void delete(Long id) {
        repository.delete(id);
    }

    public List<Place> findAll() {
        return repository.findAll();
    }

    public Place findByNameAndLatitudeAndLongtitude(Place place) {
        return repository.findByNameAndLatitudeAndLongtitude(place.getName(), place.getLatitude(), place.getLongtitude());
    }

    @Transactional
    public void updatePlacePageChanges(Long sqlObjectId, Place updatingPlaceInfo) {
        Set<String> updatedPlaceDetailTagNames = updatingPlaceInfo.getPlaceDetails().stream()
                .map(PlaceDetail::getName)
                .collect(Collectors.toSet());

        Set<Tag> newTags = tagService.findByNameIn(updatedPlaceDetailTagNames);

        Place placeForUpdate = findById(sqlObjectId);
        placeForUpdate.setName(updatingPlaceInfo.getName());
        placeForUpdate.setLongtitude(updatingPlaceInfo.getLongtitude());
        placeForUpdate.setLatitude(updatingPlaceInfo.getLatitude());

        if (updatingPlaceInfo.getSchedule() != null) {
            updatingPlaceInfo.getSchedule().setPlace(placeForUpdate);
        }
        placeForUpdate.setSchedule(updatingPlaceInfo.getSchedule());

        placeForUpdate.setPriceLevel(updatingPlaceInfo.getPriceLevel());
        placeForUpdate.setBusinessPlan(updatingPlaceInfo.getBusinessPlan());

        placeForUpdate.getContacts().clear();
        placeForUpdate.getContacts().addAll(updatingPlaceInfo.getContacts());

        /*if (updatingPlaceInfo.getImages() != null) {
            placeForUpdate.getImages().clear();
            placeForUpdate.getImages().addAll(updatingPlaceInfo.getImages());
//          TODO add impl of deleting images on S3
        }*/

        Set<Tag> placeDetailTags = placeForUpdate.getTags().stream()
                .filter(Tag::isPlaceDetailTag)
                .collect(Collectors.toSet());
        placeForUpdate.getTags().removeAll(placeDetailTags);
        placeForUpdate.getPlaceDetails().clear();
        placeForUpdate.getPlaceDetails().addAll(updatingPlaceInfo.getPlaceDetails());

        placeForUpdate.getTags().addAll(newTags);

        entityManager.merge(placeForUpdate);
    }

    public void updatePlaceMenusChanges(Long sqlObjectId, Place updatingPlaceInfo) {
        Place placeForUpdate = findById(sqlObjectId);

        Set<Tag> menuTags = placeForUpdate.getTags().stream()
                .filter(Tag::isMenuItemTag)
                .collect(Collectors.toSet());
//        TODO deprecated idea, do this with MenuController, but better try to think better
        placeForUpdate.getTags().removeAll(menuTags);
        placeForUpdate.getMenus().clear();
        placeForUpdate.getMenus().addAll(updatingPlaceInfo.getMenus());
        Set<Tag> newMenusTags = updatingPlaceInfo.getMenus().stream()
                .flatMap(menu -> menu.getMenuItems().stream()
                        .flatMap(menuItem -> menuItem.getTags().stream())).collect(Collectors.toSet());
        placeForUpdate.getTags().addAll(newMenusTags);

        update(placeForUpdate);
    }

    public void updatePlaceOffersChanges(Long sqlObjectId, Place updatingPlaceInfo) {
        Place placeForUpdate = findById(sqlObjectId);

        placeForUpdate.getOffers().clear();
        placeForUpdate.getOffers().addAll(updatingPlaceInfo.getOffers());

        update(placeForUpdate);
    }

    public List<Place> getPlacesWithoutSchedule() {
        return repository.findPlacesWithoutSchedule();
    }

    public List<Place> findPlacesByIds(List<Long> ids) {
        return repository.findPlacesByIdIn(ids);
    }

    public Map<Long, Place> getPlacesMapById(List<Long> ids) {
        return repository.findPlacesByIdIn(ids).stream()
                .collect(Collectors.toMap(Place::getId, Function.identity()));
    }

    public Integer getPlaceRecommendationsCount(Long placeId) {
        return repository.getPlaceRecommendationsCount(placeId);
    }

    public List<PlaceRecommendationsDto> getPlacesRecommendationsCount(List<Long> ids) {

        List<PlaceRecommendationsDto> placeRecommendations = Lists.newArrayList();
        Object[] recommendations = repository.getPlacesRecommendationsCount(ids);

        for (Object recommendation : recommendations) {
            try {
                Object[] recArray = (Object[]) recommendation;

                placeRecommendations.add(PlaceRecommendationsDto.of()
                        .id(Long.valueOf(recArray[0].toString()))
                        .amount(Integer.valueOf(recArray[1].toString()))
                        .create());
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        return placeRecommendations;
    }

}
package com.eat.services.b2c;

import com.eat.controllers.exceptions.DataNotFoundException;
import com.eat.models.b2b.Place;
import com.eat.models.b2c.AppUser;
import com.eat.models.b2c.VisitedPlaces;
import com.eat.repositories.sql.b2c.VisitedPlacesRepository;
import com.eat.services.b2b.PlaceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class VisitedPlacesService {

    @Autowired
    private VisitedPlacesRepository repository;

    @Autowired
    private AppUserService userService;

    @Autowired
    private PlaceService placeService;

    public VisitedPlaces add(Long userId, Long placeId) {
        AppUser appUser = userService.findById(userId);
        Place place = placeService.findById(placeId);
        if (appUser != null && place != null) {
            VisitedPlaces visitedPlaces = VisitedPlaces.of()
                    .appUserPreference(appUser.getUserPreferences())
                    .place(place)
                    .date(LocalDateTime.now())
                    .day(LocalDate.now().getDayOfWeek())
                    .create();

            return repository.save(visitedPlaces);
        }
        throw new DataNotFoundException();
    }

    public List<VisitedPlaces> findAllByUserAndPlace(Long userId, Long placeId) {
        AppUser appUser = userService.findById(userId);
        Place place = placeService.findById(placeId);
        if (appUser != null && place != null) {
            return repository.findAllByUserAndPlace(appUser, place);
        }
        throw new DataNotFoundException();
    }

    public List<VisitedPlaces> findAllByUser(Long userId) {
        AppUser appUser = userService.findById(userId);
        if (appUser != null) {
            return repository.findAllByUser(appUser);
        }
        throw new DataNotFoundException(userId);
    }

    public VisitedPlaces findById(Long id) {
        return repository.findOne(id);
    }

    public VisitedPlaces save(VisitedPlaces visitedPlace) {
        return repository.save(visitedPlace);
    }

    public void update(VisitedPlaces visitedPlace) {
        repository.save(visitedPlace);
    }

    public void delete(VisitedPlaces visitedPlace) {
        repository.delete(visitedPlace);
    }

    public void delete(Long id) {
        repository.delete(id);
    }

    public List<VisitedPlaces> findAll() {
        return repository.findAll();
    }

}

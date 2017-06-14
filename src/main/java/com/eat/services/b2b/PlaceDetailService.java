package com.eat.services.b2b;

import com.eat.models.b2b.Place;
import com.eat.models.b2b.PlaceDetail;
import com.eat.models.b2b.enums.PlaceDetailType;
import com.eat.repositories.sql.b2b.PlaceDetailRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PlaceDetailService {

    @Autowired
    private PlaceDetailRepository repository;

    public List<PlaceDetail> findAll(){
        return repository.findAll();
    }

    public PlaceDetail findById(Long id) {
        return repository.findOne(id);
    }

    public PlaceDetail findByName(String name) {
        return repository.findByName(name);
    }

    public List<PlaceDetail> findAllByType(PlaceDetailType type) {
        return repository.findAllByType(type);
    }

    public void delete(Long id) {
        repository.delete(id);
    }

    public PlaceDetail saveForFoursquare(PlaceDetail placeDetail) {
        return repository.save(placeDetail);
    }

    public PlaceDetail save(PlaceDetail placeDetail) {
        return repository.save(placeDetail);
    }

    public void saveAll(List<PlaceDetail> details) {
        repository.save(details);
    }

    public PlaceDetail update(PlaceDetail placeDetail) {
        return repository.save(placeDetail);
    }

    public void updateList(List<PlaceDetail> details) {
        repository.save(details);
    }

    public Set<PlaceDetail> findAllPlaceDetailsInPlaceByType(Place place, PlaceDetailType type) {
        return place.getPlaceDetails().stream()
                .filter(placeDetail -> placeDetail.getPlaceDetailType().equals(type))
                .collect(Collectors.toSet());
    }

}
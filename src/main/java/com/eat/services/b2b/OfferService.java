package com.eat.services.b2b;

import com.eat.controllers.exceptions.DataNotFoundException;
import com.eat.models.b2b.BusinessUser;
import com.eat.models.b2b.Offer;
import com.eat.models.b2b.Place;
import com.eat.repositories.sql.b2b.OfferRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
public class OfferService {

    @Autowired
    private OfferRepository repository;

    @Autowired
    private PlaceService placeService;

    @Autowired
    private BusinessUserService businessUserService;

    public Offer addOffer(Long userId, Long placeId, Offer offer) {
        BusinessUser user = businessUserService.findById(userId);
        Place place = placeService.findById(placeId);
        if (offer != null && user != null && place != null) {
            repository.save(offer);
            place.getOffers().add(offer);
            placeService.update(place);
            return offer;
        }
        throw new DataNotFoundException();
    }

    public void deleteOffer(Long userId, Long placeId, Long offerId) {
        BusinessUser user = businessUserService.findById(userId);
        Place place = placeService.findById(placeId);
        Offer offer = repository.findOne(offerId);
        if (offer != null && user != null && place != null) {
            place.getOffers().remove(offer);
            placeService.update(place);
            delete(offer);
        }
        throw new DataNotFoundException();
    }

    public Offer updateOffer(Long userId, Long placeId, Offer offer) {
        BusinessUser user = businessUserService.findById(userId);
        Place place = placeService.findById(placeId);
        if (offer != null && user != null && place != null) {
            update(offer);
            return offer;
        }
        throw new DataNotFoundException();
    }

    public Offer findById(Long id) {
        return repository.findOne(id);
    }

    public void delete(Long id) {
        repository.delete(id);
    }

    public void delete(Offer offer) {
        repository.delete(offer);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public Collection<Offer> getAll() {
        return repository.findAll();
    }

    public Offer save(Offer offer) {
        return repository.save(offer);
    }

    public Offer update(Offer offer) {
        return repository.save(offer);
    }

    public List<Offer> findAllCurrentOffers() {
        return repository.findAllCurrentOffers(LocalDateTime.now());
    }

}
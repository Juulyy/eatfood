package com.eat.services.mongo.b2b;

import com.eat.models.mongo.PlaceChangeRequest;
import com.eat.models.mongo.enums.ChangeRequestStatus;
import com.eat.repositories.mongo.b2b.PlaceChangeRequestRepository;
import com.eat.services.b2b.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaceChangeRequestService {

    @Autowired
    private PlaceChangeRequestRepository repository;

    @Autowired
    private PlaceService placeService;

    public PlaceChangeRequest findById(String id) {
        return repository.findOne(id);
    }

    public PlaceChangeRequest save(PlaceChangeRequest request) {
        return repository.save(request);
    }

    public PlaceChangeRequest update(PlaceChangeRequest request) {
        return repository.save(request);
    }

    public void delete(PlaceChangeRequest request) {
        repository.delete(request);
    }

    public void delete(String id) {
        repository.delete(id);
    }

    public List<PlaceChangeRequest> findAll() {
        return repository.findAll();
    }

    public List<PlaceChangeRequest> findByStatusIn(ChangeRequestStatus... statuses) {
        return repository.findByStatusIn(statuses);
    }

    public List<PlaceChangeRequest> findAllByPlaceId(Long placeId){
        return repository.findBySqlObjectId(placeId);
    }

    public void approvePlacePageChanges(PlaceChangeRequest request) {
        placeService.updatePlacePageChanges(request.getSqlObjectId(), request.toPlace());
        request.setStatus(ChangeRequestStatus.APPROVED);
        update(request);
    }

    public void approvePlaceMenusChanges(PlaceChangeRequest request) {
        placeService.updatePlaceMenusChanges(request.getSqlObjectId(), request.toPlace());
        request.setStatus(ChangeRequestStatus.APPROVED);
        update(request);
    }

    public void approvePlaceOfferssChanges(PlaceChangeRequest request) {
        placeService.updatePlaceOffersChanges(request.getSqlObjectId(), request.toPlace());
        request.setStatus(ChangeRequestStatus.APPROVED);
        update(request);
    }

}
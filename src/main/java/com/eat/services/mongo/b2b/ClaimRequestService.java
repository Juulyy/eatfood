package com.eat.services.mongo.b2b;

import com.eat.models.mongo.ClaimRequest;
import com.eat.models.mongo.enums.ClaimRequestStatus;
import com.eat.repositories.mongo.b2b.ClaimRequestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ClaimRequestService {

    @Autowired
    private ClaimRequestRepository repository;

    public ClaimRequest findById(String id) {
        return repository.findOne(id);
    }

    public ClaimRequest save(ClaimRequest claimRequest) {
        return repository.save(claimRequest);
    }

    public ClaimRequest update(ClaimRequest claimRequest) {
        return repository.save(claimRequest);
    }

    public void delete(ClaimRequest claimRequest) {
        repository.delete(claimRequest);
    }

    public void delete(String id) {
        repository.delete(id);
    }

    public List<ClaimRequest> findAll() {
        return repository.findAll();
    }

    public List<ClaimRequest> findAllByStatus(ClaimRequestStatus status) {
        return repository.findAllByStatus(status);
    }

    public List<ClaimRequest> findAllByUserId(String userId){
        return repository.findAllByUserId(userId);
    }

    public List<ClaimRequest> findAllByPlaceId(String placeId){
        return repository.findAllByPlaceId(placeId);
    }

    public List<ClaimRequest> findAllByContainingText(String text) {
        return repository.findAllBy(TextCriteria.forDefaultLanguage().matchingAny(text));
    }

}
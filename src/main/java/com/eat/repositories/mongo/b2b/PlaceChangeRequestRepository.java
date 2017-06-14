package com.eat.repositories.mongo.b2b;

import com.eat.models.mongo.PlaceChangeRequest;
import com.eat.models.mongo.enums.ChangeRequestStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceChangeRequestRepository extends MongoRepository<PlaceChangeRequest, String> {

    List<PlaceChangeRequest> findByStatusIn(ChangeRequestStatus... statuses);

    List<PlaceChangeRequest> findBySqlObjectId(Long placeId);

}
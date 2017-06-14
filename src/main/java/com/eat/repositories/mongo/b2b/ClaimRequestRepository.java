package com.eat.repositories.mongo.b2b;

import com.eat.models.mongo.ClaimRequest;
import com.eat.models.mongo.enums.ClaimRequestStatus;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClaimRequestRepository extends MongoRepository<ClaimRequest, String> {

    List<ClaimRequest> findAllByStatus(ClaimRequestStatus status);

    List<ClaimRequest> findAllByUserId(String userId);

    List<ClaimRequest> findAllByPlaceId(String PlaceId);

    List<ClaimRequest> findAllBy(TextCriteria criteria);
}

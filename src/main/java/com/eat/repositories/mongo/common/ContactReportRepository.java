package com.eat.repositories.mongo.common;

import com.eat.models.mongo.ContactReport;
import com.eat.models.mongo.enums.ContactReportType;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactReportRepository extends MongoRepository<ContactReport, String> {

    List<ContactReport> findAllByType(ContactReportType type);

    List<ContactReport> findAllByUserId(String userId);

    List<ContactReport> findAllByPlaceId(String PlaceId);

    List<ContactReport> findAllBy(TextCriteria criteria);
}

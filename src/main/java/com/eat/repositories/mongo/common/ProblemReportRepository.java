package com.eat.repositories.mongo.common;

import com.eat.models.mongo.ProblemReport;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProblemReportRepository extends MongoRepository<ProblemReport, String> {

}

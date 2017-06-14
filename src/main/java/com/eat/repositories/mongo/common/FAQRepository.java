package com.eat.repositories.mongo.common;

import com.eat.models.mongo.FAQ;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FAQRepository extends MongoRepository<FAQ, String> {

    @Query(value = "{'type' : ?0}")
    List<FAQ> findAllByType(FAQ.FAQType type);

    List<FAQ> findAllBy(TextCriteria criteria);

}
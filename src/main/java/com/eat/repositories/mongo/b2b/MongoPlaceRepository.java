package com.eat.repositories.mongo.b2b;

import com.eat.models.mongo.MongoPlace;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MongoPlaceRepository extends MongoRepository<MongoPlace, String> {

    List<MongoPlace> findByLocationWithin(Circle c);

    List<MongoPlace> findByLocationWithinAndSqlEntityIdIn(Circle c, List<Long> ids);

    MongoPlace findBySqlEntityId(Long sqlEntityId);

    List<MongoPlace> findAllByOrderByScoreDesc(TextCriteria criteria);

    List<MongoPlace> findByLocationNearAndSqlEntityIdIn(Point point, Distance distance, List<Long> ids);

    List<MongoPlace> findAllBySqlEntityIdIn(List<Long> ids);

}
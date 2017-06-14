package com.eat.services.mongo.b2c;

import com.eat.models.mongo.Weather;
import com.eat.repositories.mongo.b2c.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class WeatherService {

    @Autowired
    private WeatherRepository repository;

    @Autowired
    private MongoOperations mongoOperations;

    public Weather findLatest() {
        Query query = new Query();
        query.limit(1);
        query.with(new Sort(Sort.Direction.DESC, "systemDateTime"));
        return mongoOperations.findOne(query, Weather.class);
    }

    public Weather save(Weather weather) {
        return repository.save(weather);
    }

}
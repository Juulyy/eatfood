package com.eat.services.spark;

import com.mongodb.spark.MongoSpark;
import com.mongodb.spark.config.ReadConfig;
import com.mongodb.spark.rdd.api.java.JavaMongoRDD;
import org.bson.Document;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaceSparkOperationsService extends JavaSparkContextInjectionImpl {

    private static final long serialVersionUID = 5200125369253264121L;

    public List<Document> getAllPlaceDocuments() {
        ReadConfig readConfig = ReadConfig.create(getJavaSparkContext()).withOption("collection", "ranked_places");
        JavaMongoRDD<Document> placesRdd = MongoSpark.load(getJavaSparkContext(), readConfig);
        return placesRdd.collect();
    }

}
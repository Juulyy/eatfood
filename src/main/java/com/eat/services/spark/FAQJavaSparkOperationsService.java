package com.eat.services.spark;

import com.mongodb.spark.MongoSpark;
import com.mongodb.spark.config.ReadConfig;
import com.mongodb.spark.rdd.api.java.JavaMongoRDD;
import org.bson.Document;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FAQJavaSparkOperationsService extends JavaSparkContextInjectionImpl {

    private static final long serialVersionUID = 5200125369253264121L;

    public List<Document> getAllFAQDocuments() {
        ReadConfig readConfig = ReadConfig.create(getJavaSparkContext()).withOption("collection", "faqs");
        JavaMongoRDD<Document> faqsRdd = MongoSpark.load(getJavaSparkContext(), readConfig);
        return faqsRdd.collect();
    }

}
package com.eat.services.spark;

import com.eat.models.mongo.pool.BehaviourPool;
import com.google.gson.Gson;
import com.mongodb.spark.MongoSpark;
import com.mongodb.spark.config.WriteConfig;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.bson.Document;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BehaviourPoolSparkOperationsService extends JavaSparkContextInjectionImpl {

    private static final long serialVersionUID = -1484431799177923026L;

    public void persist(List<BehaviourPool> poolSet) {
        WriteConfig writeConfig = WriteConfig
                .create(getJavaSparkContext())
                .withOption("collection", "behaviour_pool_stats");

        JavaRDD<BehaviourPool> poolJavaRDD = getJavaSparkContext().parallelize(poolSet);
        JavaRDD<Document> map = poolJavaRDD.map((Function<BehaviourPool, Document>) v1 -> Document.parse(toJson(v1)));
        MongoSpark.save(map, writeConfig);
    }

    public String toJson(BehaviourPool behaviourPool) {
        Gson gson = new Gson();
        return gson.toJson(behaviourPool);
    }

}
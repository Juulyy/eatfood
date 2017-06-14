package com.eat.configs.other;

import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class SparkConfiguration {

    private final String MONGO_PREFIX = "mongodb://";
    private final String MONGO_INPUT_COLLECTION = "faqs";

    @Value(value = "${spring.data.mongodb.name}")
    private String mongoName;

    @Value(value = "${spring.data.mongodb.net.bindIp}")
    private String mongoHost;

    @Bean
    public SparkSession sparkSession() {
        return SparkSession.builder()
                .master("local[*]")
                .appName("eat-spark-cluster")
                .config("spark.app.id", "Eat")
                .config("spark.mongodb.input.uri", MONGO_PREFIX.concat(mongoHost).concat("/"))
                .config("spark.mongodb.output.uri", MONGO_PREFIX.concat(mongoHost).concat("/"))
                .config("spark.mongodb.input.database", mongoName)
                .config("spark.mongodb.output.database", mongoName)
                .config("spark.mongodb.input.collection", MONGO_INPUT_COLLECTION)
                .config("spark.mongodb.output.collection", MONGO_INPUT_COLLECTION)
                .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
                .getOrCreate();
    }

    @Bean
    public JavaSparkContext javaSparkContext() {
        return JavaSparkContext.fromSparkContext(sparkSession().sparkContext());
    }

}
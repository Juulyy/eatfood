package com.eat.services.spark;

import org.apache.spark.api.java.JavaSparkContext;

import java.io.Serializable;

public interface JavaSparkContextInjection extends Serializable {

    JavaSparkContext getJavaSparkContext();

}
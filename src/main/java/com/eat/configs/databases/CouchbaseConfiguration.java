//package com.eat.configs;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
//import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;
//import org.springframework.data.couchbase.repository.support.IndexManager;
//
//import java.util.Arrays;
//import java.util.List;
//
//@Configuration
//@EnableCouchbaseRepositories
//public class CouchbaseConfig extends AbstractCouchbaseConfiguration {
//
//    @Override
//    protected List<String> getBootstrapHosts() {
//        return Arrays.asList("46.219.112.90");
//    }
//
//    @Override
//    protected String getBucketName() {
//        return "default";
//    }
//
//    @Override
//    protected String getBucketPassword() {
//        return "";
//    }
//
//    @Override
//    public IndexManager indexManager() {
//        return new IndexManager(true, true, false);
//    }
//
//
//}

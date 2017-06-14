package com.eat.models.mongo;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class MongoPlaceTagsTest {

    private final String CAFE = "café";
    private final String MODERN = "modern";
    private final String LOCAL_BUSINESSES = "businesses";
    private final String MID_CENTURY_MODERN = "century";

    @Resource
    MongoOperations mongoOperations;

    @Test
    public void searchByTagsTest() {
        Query query = TextQuery.queryText(
                TextCriteria.forDefaultLanguage().matchingAny(MID_CENTURY_MODERN)).sortByScore();
        mongoOperations.find(query, MongoPlace.class);
        log.info("done");
    }

}

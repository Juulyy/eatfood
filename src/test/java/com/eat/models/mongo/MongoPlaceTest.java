package com.eat.models.mongo;

import com.eat.repositories.mongo.b2b.MongoPlaceRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class MongoPlaceTest {

    @Resource
    MongoPlaceRepository repository;

    @Test
    public void geoSearchTest() {
        Point point = new Point(52.5070794649812, 13.3326601982117);
        Circle circle = new Circle(point, new Distance(10, Metrics.KILOMETERS));
        List<MongoPlace> list = repository.findByLocationWithin(circle);
        log.info("List size = " + list.size());
    }

}
/*
package com.eat.services.b2c;

import com.eat.models.mongo.Weather;
import com.eat.services.mongo.b2c.WeatherService;
import com.eat.tasks.b2c.WeatherTask;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertNotNull;

@Slf4j
@SpringBootTest
@WebAppConfiguration
@RunWith(SpringRunner.class)
public class WeatherServiceTest {

    @Resource
    WeatherService weatherService;

    @Resource
    MongoOperations mongoOperations;

    @Resource
    WeatherTask weatherTask;

    @Before
    public void fillWeather() {
        for (int i = 0; i < 5; i++) {
            weatherTask.parse();
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            i++;
        }
    }

    @After
    public void dropCollection() {
        mongoOperations.dropCollection(Weather.class);
    }

    @Test
    public void sortBySystemDateTimeTest_Success() {
        Weather latestWeather = weatherService.findLatest();
        assertNotNull(latestWeather);
    }

}*/

package com.eat.models.b2b;

import com.eat.services.b2b.DayOpenTimeService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalTime;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@WebAppConfiguration
public class DayOpenTimeTest {

    @Autowired
    private DayOpenTimeService service;

    @Test
    @Ignore
    public void simpleTest() {
        LocalTime timeFrom = LocalTime.of(8,30);
        LocalTime timeTo = LocalTime.of(23, 0);

        service.save(DayOpenTime.of()
                .day(DayOfWeek.FRIDAY)
                .from(timeFrom)
                .to(timeTo)
                .create());

        System.out.println(service.getAll());

        service.delete(1l);
    }

}
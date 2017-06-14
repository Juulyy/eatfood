package com.eat.models.common;

import com.eat.models.mongo.ProblemReport;
import com.eat.repositories.mongo.common.ProblemReportRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class ProblemReportTest {

    @Autowired
    private ProblemReportRepository repository;

    @Test
    @Rollback(value = false)
    public void save_onlySuccess() {
        repository.deleteAll();

        repository.save(ProblemReport.of()
                .firstName("volodymyr")
                .lastName("maystrovyy")
                .email("maystrovyy@gmail.com")
                .description("IOS application doesn't working! How I can fix this problem?")
                .time(LocalDateTime.now())
                .create());

        List<ProblemReport> reports = repository.findAll();
        assertEquals(reports.size(), 1);
    }

}
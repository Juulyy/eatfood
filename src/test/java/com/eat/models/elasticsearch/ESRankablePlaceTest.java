package com.eat.models.elasticsearch;

import com.eat.models.b2b.Place;
import com.eat.repositories.elasticsearch.PlaceESRepository;
import com.eat.services.b2b.PlaceService;
import com.eat.utils.converters.PlaceConverter;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;

import static org.junit.Assert.assertEquals;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class ESRankablePlaceTest {

    @Resource
    PlaceESRepository esRepository;

    @Autowired
    PlaceService placeService;

    @Before
    @After
    public void deleteAll() {
        esRepository.deleteAll();
        log.info("Deleted all!");
        assertEquals(Lists.newArrayList(esRepository.findAll()).size(), 0);
    }

    @Test
    @Ignore
    public void saveOnly_success() {
        Place domainPlace = placeService.findById(1L);
        ESRankablePlace ESRankablePlace = PlaceConverter.toSearchablePlace(domainPlace);

        esRepository.save(ESRankablePlace);
    }

}
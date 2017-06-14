package com.eat.services.mongo.pool;

import com.eat.models.common.Tag;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Set;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@WebAppConfiguration
public class BehaviourPoolServiceTest {

    @Resource
    BehaviourPoolService poolService;

    @Test
    @Ignore
    public void test() {
        Set<Tag> top2UserTags = poolService.findTopUserTags(2L);
        Set<ImmutablePair<Tag, Double>> topUserTagsWithRates = poolService.findTopUserTagsWithRates(2L);
        assertEquals(top2UserTags.size(), topUserTagsWithRates.size());
    }

}
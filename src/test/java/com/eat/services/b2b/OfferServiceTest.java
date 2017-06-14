package com.eat.services.b2b;

import com.eat.models.b2b.Offer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class OfferServiceTest {

    @Resource
    OfferService offerService;

    @Before
    @After
    public void clearTable() {
        offerService.deleteAll();
    }

    @Test
    @Rollback
    public void findAllCurrentOffers() {
        offerService.save(Offer.of()
                .name("test1")
                .expirationDate(LocalDateTime.of(2017, 11, 29, 20, 0))
                .create());

        offerService.save(Offer.of()
                .name("test2")
                .expirationDate(LocalDateTime.of(2017, 6, 6, 20, 0))
                .create());

        offerService.save(Offer.of()
                .name("test3")
                .expirationDate(LocalDateTime.of(2016, 11, 29, 20, 0))
                .create());

        List<Offer> allCurrentOffers = offerService.findAllCurrentOffers();
        assertThat(allCurrentOffers, hasSize(2));
    }

}
package com.eat.services.mongo.b2b;

import com.eat.models.b2b.Place;
import com.eat.models.mongo.PlaceChangeRequest;
import com.eat.models.mongo.enums.ChangeRequestStatus;
import com.eat.models.mongo.enums.ChangeRequestType;
import com.eat.repositories.mongo.b2b.PlaceChangeRequestRepository;
import com.eat.services.b2b.PlaceService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@WebAppConfiguration
public class PlaceChangeRequestServiceTest {

    @Resource
    PlaceService placeService;

    @Resource
    PlaceChangeRequestService service;

    @Resource
    PlaceChangeRequestRepository repository;

    @Before
    @Ignore
    public void deleteAll() {
        repository.deleteAll();
    }

    @Test
    @Ignore
    public void savePlacePageChangeRequestFromPlace_success() {
        Place place1 = placeService.findById(1L);
        PlaceChangeRequest placeChangeRequest = PlaceChangeRequest.of()
                .place(place1)
                .requestType(ChangeRequestType.PLACE_PAGE)
                .create();
        repository.save(placeChangeRequest);

        Place place2 = placeService.findById(2L);
        PlaceChangeRequest menusChangeRequest = PlaceChangeRequest.of()
                .requestType(ChangeRequestType.MENUS)
                .place(place2)
                .create();
        repository.save(menusChangeRequest);

        Place place3 = placeService.findById(3L);
        PlaceChangeRequest offersChangeRequest = PlaceChangeRequest.of()
                .requestType(ChangeRequestType.OFFERS)
                .place(place3)
                .create();
        repository.save(offersChangeRequest);

        Place place4 = placeService.findById(4L);
        PlaceChangeRequest photoShootRequest = PlaceChangeRequest.of()
                .requestType(ChangeRequestType.PHOTO_SHOOT)
                .place(place4)
                .create();
        repository.save(photoShootRequest);

        List<PlaceChangeRequest> list = repository.findByStatusIn(ChangeRequestStatus.PENDING,
                ChangeRequestStatus.IN_PROGRESS);
        assertThat(list, hasSize(4));

        assertThat(repository.findBySqlObjectId(1L), hasSize(1));
    }

}
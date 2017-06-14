package com.eat.services.b2c;

import com.eat.controllers.exceptions.DataNotFoundException;
import com.eat.models.b2c.*;
import com.eat.models.b2c.enums.AppUserCollectionType;
import com.eat.models.b2c.enums.PrivacyType;
import com.eat.models.common.enums.RoleType;
import com.eat.repositories.sql.b2c.AppUserCollectionRepository;
import com.eat.services.b2b.OfferService;
import com.eat.services.common.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@WebAppConfiguration
public class AppUserCollectionServiceTest {

    @Autowired
    private RoleService roleService;

    @Autowired
    private AppUserService userService;

    @Autowired
    private OfferService offerService;

    @Autowired
    private AppUserCollectionRepository repository;

    public void fillAppUser() {
        userService.save(AppUser.of()
                .firstName("test")
                .lastName("test")
                .login("test")
                .email("maystrovyy@gmail.com")
                .password("testasdasd123AZ")
                .userPreferences(AppUserPreference.of().create())
                .gender(AppUser.Gender.FEMALE)
                .role(roleService.getRoleByRoleType(RoleType.ROLE_APP_USER))
                .create());
    }

    public AppUserCollection findAppUserCollectionByType_test(Long userId, AppUserCollectionType type) {
        AppUser appUser = userService.findById(userId);
        if (Optional.ofNullable(appUser).isPresent()) {
            return Optional.ofNullable(repository.findAppUserCollectionByType(appUser.getUserPreferences(), type))
                    .orElse(repository.save(
                            AppUserCollection.of()
                            .appUserPreference(appUser.getUserPreferences())
                            .collectionName(type.getType())
                            .collectionPrivacy(PrivacyType.MUTUAL_FOLLOWING)
                            .collectionType(type)
                            .create()));
        }
        throw new DataNotFoundException();
    }

    @Test
    @Ignore
    public void successfullySave_onEmptyCollection() {
        fillAppUser();
        AppUser user = userService.findById(1L);
        AppUserCollection recommendedPlaces = findAppUserCollectionByType_test(user.getId(),
                AppUserCollectionType.RECOMMENDED_PLACES);
        assertNotNull(recommendedPlaces);
        assertEquals(repository.findAppUserCollectionsByType(user.getUserPreferences(),
                AppUserCollectionType.RECOMMENDED_PLACES).size(), 1);
    }

}
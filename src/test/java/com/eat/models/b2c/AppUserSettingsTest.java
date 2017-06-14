package com.eat.models.b2c;

import com.eat.models.b2c.enums.PrivacyType;
import com.eat.services.b2c.AppUserService;
import com.eat.services.b2c.AppUserSettingsService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@WebAppConfiguration
public class AppUserSettingsTest {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private AppUserSettingsService settingsService;

    @Test
    @Ignore
    public void save_onlySuccess() {
        AppUser user = appUserService.findById(1l);
        AppUser user1 = appUserService.findById(2l);
        AppUser user2 = appUserService.findById(3l);
        AppUser user3 = appUserService.findById(4l);
        List<AppUser> blocked = new ArrayList<>();
        blocked.add(user1);
        blocked.add(user2);
        blocked.add(user3);
        List<AppUserSettings.TransitType> types = new ArrayList<>();
        types.add(AppUserSettings.TransitType.BIKE);
        types.add(AppUserSettings.TransitType.PUBLIC);
        settingsService.save(AppUserSettings.of()
                .user(user)
                .rvp(true)
                .recommendations(true)
                .smvnFollowing(true)
                .friendJoin(true)
                .smvnPlanInvited(true)
                .smvnRecommend(true)
                .radius(40)
                .transitTypes(types)
                .blockedUsers(blocked)
                .language(AppUserSettings.Language.GERMAN)
                .collectionView(PrivacyType.MUTUAL_FOLLOWING)
                .placeHistoryView(PrivacyType.MUTUAL_FOLLOWING)
                .placeProofView(PrivacyType.MUTUAL_FOLLOWING)
                .create());
        settingsService.findAll();
    }

}
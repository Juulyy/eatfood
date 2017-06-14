package com.eat.services.b2c;

import com.eat.controllers.exceptions.DataNotFoundException;
import com.eat.models.b2c.AppUser;
import com.eat.models.b2c.AppUserSettings;
import com.eat.models.b2c.enums.PrivacyType;
import com.eat.models.common.enums.CrudMethod;
import com.eat.repositories.sql.b2c.AppUserSettingsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class AppUserSettingsService {

    @Autowired
    private AppUserSettingsRepository repository;

    @Autowired
    private AppUserService userService;

    public AppUserSettings findById(Long id) {
        return repository.findOne(id);
    }

    public AppUserSettings save(AppUserSettings settings) {
        return repository.save(settings);
    }

    public AppUserSettings update(AppUserSettings settings) {
        return repository.save(settings);
    }

    public void delete(AppUserSettings settings) {
        repository.delete(settings);
    }

    public void delete(Long id) {
        repository.delete(id);
    }

    public List<AppUserSettings> findAll() {
        return repository.findAll();
    }

    public List<AppUser> findBlockedUsers(Long id) {
        return findById(id).getBlockedUsers();
    }

    public AppUserSettings manageBlockedUsers(Long id, Long blockedUserId, CrudMethod crudMethod) {
        AppUserSettings userSettings = findById(id);
        AppUser blockedUser = userService.findById(blockedUserId);
        if (userSettings != null && blockedUser != null && !id.equals(blockedUserId)) {
            switch (crudMethod) {
                case CREATE:
                    userSettings.getBlockedUsers().add(blockedUser);
                    break;
                case DELETE:
                    userSettings.getBlockedUsers().remove(blockedUser);
                    break;
            }
            return save(userSettings);
        }
        throw new DataNotFoundException();
    }

    public List<AppUserSettings.TransitType> getDefaultTransitType() {
        return Stream.of(AppUserSettings.TransitType.WALK).collect(Collectors.toList());
    }

    public void createDefaultSettings(AppUser appUser) {
        save(AppUserSettings.of()
                .user(appUser)
                .rvp(true)
                .recommendations(true)
                .smvnFollowing(true)
                .friendJoin(true)
                .smvnPlanInvited(true)
                .smvnRecommend(true)
                .radius(10)
                .transitTypes(getDefaultTransitType())
                .planInvitation(PrivacyType.MUTUAL_FOLLOWING)
                .recommendationView(PrivacyType.MUTUAL_FOLLOWING)
                .collectionView(PrivacyType.MUTUAL_FOLLOWING)
                .subscriptionsView(PrivacyType.MUTUAL_FOLLOWING)
                .placeHistoryView(PrivacyType.MUTUAL_FOLLOWING)
                .placeProofView(PrivacyType.MUTUAL_FOLLOWING)
                .followingView(PrivacyType.MUTUAL_FOLLOWING)
                .blockedUsers(null)
                .language(AppUserSettings.Language.ENGLISH)
                .create());
    }

}
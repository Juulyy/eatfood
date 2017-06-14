package com.eat.services.b2c;

import com.eat.controllers.exceptions.DataNotFoundException;
import com.eat.models.b2c.AppUser;
import com.eat.models.b2c.AppUserPreference;
import com.eat.models.common.Tag;
import com.eat.models.common.enums.CrudMethod;
import com.eat.models.common.enums.TagType;
import com.eat.repositories.sql.b2c.AppUserPreferenceRepository;
import com.eat.services.common.TagService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AppUserPreferenceService {

    @Autowired
    private AppUserPreferenceRepository preferenceRepository;
    @Autowired
    private AppUserService appUserService;
    @Autowired
    private TagService tagService;

    public void createDefaultPreference(AppUser appUser) {
        save(AppUserPreference.of()
                .user(appUser)
                .create());
    }

    public AppUserPreference userTagsManagement(Long userId, Long tagId, CrudMethod crudMethod) {
        AppUser appUser = appUserService.findById(userId);
        Tag tag = tagService.findById(tagId);
        if (appUser != null && tag != null) {
            switch (crudMethod) {
                case CREATE:
                    appUser.getUserPreferences().getTasteTags().add(tag);
                    break;
                case DELETE:
//                    NOTE: this method is not used in EAT! ver 1
                    appUser.getUserPreferences().getTasteTags().remove(tag);
                    break;
            }
            return update(appUser.getUserPreferences());
        }
        throw new DataNotFoundException();
    }

    public void followersAndFollowingManagement(Long userId, Long followerId, CrudMethod crudMethod) {
        AppUser appUser = appUserService.findById(userId);
        AppUser follower = appUserService.findById(followerId);
        if (appUser != null && follower != null) {
            switch (crudMethod) {
                case CREATE:
                        appUser.getUserPreferences().getFollowers().add(follower);
                        follower.getUserPreferences().getFollowing().add(appUser);
                        break;
                case DELETE:
                        appUser.getUserPreferences().getFollowers().remove(follower);
                        follower.getUserPreferences().getFollowing().remove(appUser);
                        break;
            }
            update(appUser.getUserPreferences());
            update(follower.getUserPreferences());
        }
        throw new DataNotFoundException();
    }

    public MultiValuedMap<TagType, Tag> getUserAllergiesAndDiets(Long userId){

        MultiValuedMap userTags = new HashSetValuedHashMap();
        Object[] tagInfos = preferenceRepository.findUserPreferencesAllergieAndDietTags(userId);

        for (Object object : tagInfos) {
            Object[] currentInfo = (Object[]) object;
            Tag tag = Tag.of()
                    .id(Long.valueOf(currentInfo[0].toString()))
                    .name(currentInfo[1].toString())
                    .type(TagType.valueOf(currentInfo[2].toString()))
                    .create();

            userTags.put(tag.getType(), tag);
        }

        return userTags;
    }

    public List<AppUserPreference> findAll() {
        return preferenceRepository.findAll();
    }

    public AppUserPreference save(AppUserPreference preference) {
        return preferenceRepository.save(preference);
    }

    public void save(List<AppUserPreference> preferences) {
        preferenceRepository.save(preferences);
    }

    public AppUserPreference updateForDemo(AppUserPreference preference) {
        return preferenceRepository.save(preference);
    }

    public AppUserPreference update(AppUserPreference preference) {
        return preferenceRepository.save(preference);
    }

    public void updateList(List<AppUserPreference> preferences) {
        preferenceRepository.save(preferences);
    }

    public void delete(AppUserPreference preference) {
        preferenceRepository.delete(preference);
    }

    public void delete(Long id) {
        preferenceRepository.delete(id);
    }

}

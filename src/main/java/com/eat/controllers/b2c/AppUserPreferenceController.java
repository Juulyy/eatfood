package com.eat.controllers.b2c;

import com.eat.models.b2c.AppUser;
import com.eat.models.common.AuthoritiesConstants;
import com.eat.models.common.Tag;
import com.eat.models.common.enums.CrudMethod;
import com.eat.models.common.enums.TagType;
import com.eat.services.b2c.AppUserPreferenceService;
import com.eat.services.b2c.AppUserService;
import org.apache.commons.collections4.MultiValuedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user/preference")
@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.BUSINESS_MANAGER, AuthoritiesConstants.BUSINESS_USER})
public class AppUserPreferenceController {

    @Autowired
    private AppUserPreferenceService preferenceService;

    @Autowired
    private AppUserService userService;

    @PostMapping(value = "/{id}/followers/add", consumes = "application/json", produces = "application/json")
    public ResponseEntity addFollower(@PathVariable("id") Long userId, @RequestParam("followerId") Long followerId) {
        preferenceService.followersAndFollowingManagement(userId, followerId, CrudMethod.CREATE);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{id}/followers/remove", consumes = "application/json", produces = "application/json")
    public ResponseEntity removeFollower(@PathVariable("id") Long userId, @RequestParam("followerId") Long followerId) {
        preferenceService.followersAndFollowingManagement(userId, followerId, CrudMethod.DELETE);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{id}/mutual-followers", consumes = "application/json", produces = "application/json")
    public List<AppUser> getMutualFollowers(@PathVariable("id") Long id) {
        return userService.getMutualFollowers(id);
    }

    @PutMapping(value = "/{id}/tags/add", consumes = "application/json", produces = "application/json")
    public ResponseEntity addTag(@PathVariable("id") Long userId, @RequestParam("tagId") Long tagId) {
        preferenceService.userTagsManagement(userId, tagId, CrudMethod.CREATE);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{id}/tags/remove", consumes = "application/json", produces = "application/json")
    public ResponseEntity deleteTag(@PathVariable("id") Long userId, @RequestParam("tagId") Long tagId) {
        preferenceService.userTagsManagement(userId, tagId, CrudMethod.DELETE);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{id}/alrg-diets", produces = "application/json")
    public ResponseEntity<Set<Tag>> getUserAllergiesDiets(@PathVariable("id") Long userId) {
        MultiValuedMap<TagType, Tag> allergiesAndDiets = preferenceService.getUserAllergiesAndDiets(userId);

        return ResponseEntity.ok(allergiesAndDiets.keys().stream()
                .flatMap(key -> allergiesAndDiets.get(key).stream())
                .collect(Collectors.toSet()));
    }

}

package com.eat.controllers.demo;

import com.eat.controllers.recommender.SuggestionController;
import com.eat.dto.b2c.AppUserGreetingDto;
import com.eat.dto.demo.DemoAppParamDto;
import com.eat.dto.demo.DemoSuggestionCategoryDto;
import com.eat.dto.recommender.SuggestionCategoryMobileDto;
import com.eat.logic.b2c.AppUserGreetingPool;
import com.eat.logic.rankers.wrappers.Rankable;
import com.eat.models.b2b.Place;
import com.eat.models.b2c.AppUser;
import com.eat.models.b2c.AppUserGreeting;
import com.eat.models.common.Tag;
import com.eat.models.common.enums.TagType;
import com.eat.models.demo.DemoAppParam;
import com.eat.models.mongo.enums.WeatherIcon;
import com.eat.models.recommender.SuggestionCategory;
import com.eat.services.b2c.AppUserPreferenceService;
import com.eat.services.common.TagService;
import com.eat.services.demo.DemoAppParamService;
import com.eat.services.demo.DemoAppUserBehaviourTagUpdater;
import com.eat.services.demo.DemoBehaviourContainer;
import com.eat.utils.converters.dto.AppUserGreetingConverter;
import com.eat.utils.converters.dto.SuggestionCategoryMobileConverter;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

import static org.springframework.format.annotation.DateTimeFormat.ISO;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/demo")
public class DemoAppParamController {

    @Autowired
    private DemoAppParamService userDemoService;

    @Autowired
    private DemoBehaviourContainer behaviourContainer;

    @Autowired
    private TagService tagService;

    @Autowired
    private SuggestionController suggestionController;

    @Autowired
    private AppUserPreferenceService prefService;

    @Autowired
    private DemoAppUserBehaviourTagUpdater behaviourTagUpdater;

    @Autowired
    private SuggestionCategoryMobileConverter mobileConverter;

    @Autowired
    private AppUserGreetingPool greetingPool;

    @Autowired
    private AppUserGreetingConverter greetingConverter;

    @PostMapping(value = "/", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public DemoAppParam createDemoUser(@RequestBody DemoAppParamDto appUserDto) {
        return userDemoService.addDemoUser(appUserDto);
    }

    @PutMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public DemoAppParam updateDemoUser(@PathVariable(value = "id") Long demoId,
                                       @RequestParam(value = "time") @DateTimeFormat(iso = ISO.TIME) LocalTime time,
                                       @RequestParam(value = "temp", required = false) Double temp,
                                       @RequestParam(value = "icon", required = false) WeatherIcon icon,
                                       @RequestParam(value = "longtitude") Double longtitude,
                                       @RequestParam(value = "latitude") Double latitude) {
        return userDemoService.updateParams(demoId, time, temp, icon, longtitude, latitude);
    }

    @PutMapping(value = "/{id}/tag", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity addTag(@PathVariable(value = "id") Long demoId, @RequestParam(value = "tagId") Long tagId) {
        DemoAppParam demoAppParam = userDemoService.findById(demoId);
        AppUser appUser = demoAppParam.getAppUser();
        Tag tag = tagService.findById(tagId);
        appUser.getUserPreferences().getTasteTags().add(tag);
        prefService.update(appUser.getUserPreferences());
        behaviourTagUpdater.addTagIntoBehaviourPool(appUser.getId(), tag);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{id}/tag", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity deleteTag(@PathVariable(value = "id") Long demoId, @RequestParam(value = "tagId") Long tagId) {
        DemoAppParam demoAppParam = userDemoService.findById(demoId);
        AppUser appUser = demoAppParam.getAppUser();
        Tag tag = tagService.findById(tagId);
        appUser.getUserPreferences().getTasteTags().remove(tag);
        prefService.update(appUser.getUserPreferences());
        behaviourTagUpdater.removeTagIntoBehaviourPool(appUser.getId(), tag);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity deleteDemoUser(@PathVariable(value = "id") Long demoUserId) {
        userDemoService.remove(demoUserId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{id}/categories", produces = APPLICATION_JSON_VALUE)
    public DemoSuggestionCategoryDto getSuggestionCategories(@PathVariable(value = "id") Long demoId) {
        DemoAppParam demoAppParam = userDemoService.findById(demoId);
        AppUser appUser = demoAppParam.getAppUser();

        List<Pair<Rankable<SuggestionCategory>, List<Rankable<Place>>>> currentCatList;
        List<Pair<Rankable<SuggestionCategory>, List<Rankable<Place>>>> futureCatList;
        List<SuggestionCategoryMobileDto> currentCatDtoList;
        List<SuggestionCategoryMobileDto> futureCatDtoList;

        if (demoAppParam.getLatitude() == 0 && demoAppParam.getLongtitude() == 0) {
            currentCatList = suggestionController.getUserCategories(appUser.getId(),
                    50, demoAppParam.getDateTime(),  demoAppParam.getTemp(), demoAppParam.getIcon());
            futureCatList = suggestionController.getUserCategories(appUser.getId(),
                    50, demoAppParam.getDateTimeWithShift(4), demoAppParam.getTemp(), demoAppParam.getIcon());
        } else {
            currentCatList = suggestionController.getUserCategoriesWithFilters(appUser.getId(),
                    demoAppParam.getLatitude(), demoAppParam.getLongtitude(),
                    5D, demoAppParam.getDateTime(), 50, demoAppParam.getTemp(), demoAppParam.getIcon());
            futureCatList = suggestionController.getUserCategoriesWithFilters(appUser.getId(),
                    demoAppParam.getLatitude(), demoAppParam.getLongtitude(),
                    5D, demoAppParam.getDateTimeWithShift(4), 50, demoAppParam.getTemp(), demoAppParam.getIcon());
        }

        if (!currentCatList.isEmpty()) {
            currentCatDtoList = mobileConverter.toSuggestionCategoryMobileDtoList(currentCatList);
        } else {
            currentCatDtoList = Lists.newArrayList();
        }

        if (!futureCatList.isEmpty()) {
            futureCatDtoList = mobileConverter.toSuggestionCategoryMobileDtoList(futureCatList);
        } else {
            futureCatDtoList = Lists.newArrayList();
        }

        return DemoSuggestionCategoryDto.of()
                .currentCategories(currentCatDtoList)
                .futureCategories(futureCatDtoList)
                .create();
    }

    @GetMapping(value = "/{id}/greeting", produces = APPLICATION_JSON_VALUE)
    public AppUserGreetingDto getGreeting(@PathVariable(value = "id") Long demoId) {
        DemoAppParam demoAppParam = userDemoService.findById(demoId);
        AppUser appUser = demoAppParam.getAppUser();

        AppUserGreeting greetingMessage = greetingPool.getGreeting(appUser, demoAppParam.getTime());

        return greetingConverter.toAppUserGreetingDto(greetingMessage, appUser.getFirstName());
    }

    @GetMapping(value = "/{id}/contextual", produces = APPLICATION_JSON_VALUE)
    public AppUserGreetingDto getContextual(@PathVariable(value = "id") Long demoId) {
        DemoAppParam demoAppParam = userDemoService.findById(demoId);
        AppUser appUser = demoAppParam.getAppUser();

        AppUserGreeting greetingMessage = greetingPool.getContextSpecificForDemo(appUser, demoAppParam.getTime(), demoAppParam.getTemp(), demoAppParam.getIcon());

        return greetingConverter.toAppUserGreetingDto(greetingMessage, null);
    }

    @PutMapping(value = "/{id}/all-cuisines", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity addAllCuisines(@PathVariable(value = "id") Long demoId) {
        DemoAppParam demoAppParam = userDemoService.findById(demoId);
        AppUser appUser = demoAppParam.getAppUser();
        List<Tag> cuisines = tagService.findAllByType(TagType.CUISINE);
        appUser.getUserPreferences().getTasteTags().addAll(cuisines);
        prefService.update(appUser.getUserPreferences());
        behaviourTagUpdater.addTagsIntoBehaviourPool(appUser.getId(), cuisines);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{id}/all-cuisines", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity removeAllCuisines(@PathVariable(value = "id") Long demoId) {
        DemoAppParam demoAppParam = userDemoService.findById(demoId);
        AppUser appUser = demoAppParam.getAppUser();
        List<Tag> cuisines = tagService.findAllByType(TagType.CUISINE);
        appUser.getUserPreferences().getTasteTags().removeAll(cuisines);
        prefService.update(appUser.getUserPreferences());
        behaviourTagUpdater.removeTagsIntoBehaviourPool(appUser.getId(), cuisines);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/{id}/all-features", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity addAllFeatures(@PathVariable(value = "id") Long demoId) {
        DemoAppParam demoAppParam = userDemoService.findById(demoId);
        AppUser appUser = demoAppParam.getAppUser();
        List<Tag> features = tagService.findAllByType(TagType.FEATURE);
        appUser.getUserPreferences().getTasteTags().addAll(features);
        prefService.update(appUser.getUserPreferences());
        behaviourTagUpdater.addTagsIntoBehaviourPool(appUser.getId(), features);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{id}/all-features", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity removeAllFeatures(@PathVariable(value = "id") Long demoId) {
        DemoAppParam demoAppParam = userDemoService.findById(demoId);
        AppUser appUser = demoAppParam.getAppUser();
        List<Tag> features = tagService.findAllByType(TagType.FEATURE);
        appUser.getUserPreferences().getTasteTags().removeAll(features);
        prefService.update(appUser.getUserPreferences());
        behaviourTagUpdater.removeTagsIntoBehaviourPool(appUser.getId(), features);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/{id}/all-menus", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity addAllMenus(@PathVariable(value = "id") Long demoId) {
        DemoAppParam demoAppParam = userDemoService.findById(demoId);
        AppUser appUser = demoAppParam.getAppUser();
        List<Tag> menus = tagService.findAllByType(TagType.MENU_ITEM_CATEGORY);
        appUser.getUserPreferences().getTasteTags().addAll(menus);
        prefService.update(appUser.getUserPreferences());
        behaviourTagUpdater.addTagsIntoBehaviourPool(appUser.getId(), menus);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{id}/all-menus", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity removeAllMenus(@PathVariable(value = "id") Long demoId) {
        DemoAppParam demoAppParam = userDemoService.findById(demoId);
        AppUser appUser = demoAppParam.getAppUser();
        List<Tag> menus = tagService.findAllByType(TagType.MENU_ITEM_CATEGORY);
        appUser.getUserPreferences().getTasteTags().removeAll(menus);
        prefService.update(appUser.getUserPreferences());
        behaviourTagUpdater.removeTagsIntoBehaviourPool(appUser.getId(), menus);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/{id}/behaviour", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity addBehaviour(@PathVariable(value = "id") Long demoId,
                                       @RequestParam(value = "behId") Integer behId) {
        DemoAppParam demoAppParam = userDemoService.findById(demoId);
        AppUser appUser = demoAppParam.getAppUser();
        switch (behId) {
            case 1:
                behaviourContainer.processFirstBehaviour(appUser.getId());
                break;
            case 2:
                behaviourContainer.processSecondBehaviour(appUser.getId());
                break;
            case 3:
                behaviourContainer.processThirdBehaviour(appUser.getId());
                break;
            case 5:
                behaviourContainer.processMixedBehaviour(appUser.getId());
                break;
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{id}/behaviour", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity removeBehaviour(@PathVariable(value = "id") Long demoId) {
        DemoAppParam demoAppParam = userDemoService.findById(demoId);
        Long userId = demoAppParam.getAppUser().getId();
        behaviourContainer.processResetDemoBehaviour(userId);
        return ResponseEntity.ok().build();
    }

}
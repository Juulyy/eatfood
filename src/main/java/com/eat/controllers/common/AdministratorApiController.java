package com.eat.controllers.common;

import com.eat.logic.b2c.SuggestionCategoryPool;
import com.eat.logic.common.TagPool;
import com.eat.models.b2b.Place;
import com.eat.models.b2b.PlaceDetail;
import com.eat.models.b2b.Schedule;
import com.eat.models.b2c.*;
import com.eat.models.b2c.enums.AppUserCollectionType;
import com.eat.models.b2c.enums.AppUserGreetingType;
import com.eat.models.b2c.localization.LocalizedGreeting;
import com.eat.models.common.Tag;
import com.eat.models.common.enums.ImageType;
import com.eat.models.common.enums.RoleType;
import com.eat.models.common.enums.TagType;
import com.eat.models.elasticsearch.ESRankablePlace;
import com.eat.models.mongo.FAQ;
import com.eat.models.mongo.MongoPlace;
import com.eat.models.mongo.ProblemReport;
import com.eat.models.mongo.enums.WeatherIcon;
import com.eat.models.recommender.*;
import com.eat.repositories.elasticsearch.PlaceESRepository;
import com.eat.repositories.mongo.b2b.MongoPlaceRepository;
import com.eat.repositories.mongo.common.FAQRepository;
import com.eat.repositories.mongo.common.ProblemReportRepository;
import com.eat.services.b2b.PlaceDetailService;
import com.eat.services.b2b.PlaceService;
import com.eat.services.b2b.ScheduleService;
import com.eat.services.b2c.AppUserGreetingService;
import com.eat.services.b2c.AppUserPreferenceService;
import com.eat.services.b2c.AppUserService;
import com.eat.services.common.ImageService;
import com.eat.services.common.MenuItemsGeneratorService;
import com.eat.services.common.RoleService;
import com.eat.services.common.TagService;
import com.eat.services.demo.DemoAppUserConsumer;
import com.eat.services.mongo.b2b.MongoPlaceService;
import com.eat.services.recommender.RankingRateService;
import com.eat.services.recommender.SimiliarPlaceByAttributeRatingService;
import com.eat.services.recommender.SuggestionCategoryService;
import com.eat.services.spark.FAQJavaSparkOperationsService;
import com.eat.services.spark.PlaceSparkOperationsService;
import com.eat.services.spark.ProblemReportSparkOperationsService;
import com.eat.utils.B2BTestHelper;
import com.eat.utils.B2CTestHelper;
import com.eat.utils.MenuItemTagsHelper;
import com.eat.utils.TagsHelper;
import com.eat.utils.converters.MongoPlaceConverter;
import com.eat.utils.converters.PlaceConverter;
import com.eat.utils.helpers.GreetingHelper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.data.util.CloseableIterator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(value = "/api/admin")
public class AdministratorApiController {

    @Autowired
    private MenuItemsGeneratorService menuItemsGeneratorService;
    @Autowired
    private ApplicationContext context;
    @Autowired
    private MongoOperations mongoOperations;
    @Autowired
    private PlaceService placeService;
    @Autowired
    private AppUserService userService;
    @Autowired
    private SimiliarPlaceByAttributeRatingService similiarPlacesService;
    @Autowired
    private TagService tagService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private AppUserGreetingService greetingService;
    @Autowired
    private PlaceDetailService detailService;
    @Autowired
    private AppUserPreferenceService appUserPreferenceService;

    /**
     * To use this method make sure that in the db there are any places or
     * use method "fillBerlinPlaces()" in FoursquareController to filling
     */
    @GetMapping(value = "/fill-base-data")
    public HttpStatus baseDataFilling() {
        if (placeService.findAll().size() == 0) {
            log.error("Fill places!");
            return HttpStatus.BAD_REQUEST;
        }
        defaultUserFilling(100);
        fillRecommendedPlaces();
        placeBaseDataFilling();
        userTagsFilling();
        return HttpStatus.OK;
    }

    /**
     * Fill tags to all users
     */
    @GetMapping(value = "/fill-user-taste-tags")
    public HttpStatus userTagsFilling() {
        List<AppUser> appUsers = userService.findAll();
        List<Tag> tags = tagService.findAll();
        if (appUsers.size() == 0 || tags.size() == 0) {
            log.error("Fill users or tags!");
            return HttpStatus.BAD_REQUEST;
        }
        List<AppUser> users = B2CTestHelper.fillUserTasteTags(appUsers, tags);
        userService.updateAll(users);
        return HttpStatus.OK;
    }

    /**
     * Fill menu items, tags & place details
     */
    @GetMapping(value = "/fill-place-base-data")
    public HttpStatus placeBaseDataFilling() {
        List<Place> allPlaces = placeService.findAll();
        List<Tag> tags = tagService.findAll();
        List<PlaceDetail> placeDetails = detailService.findAll();
        List<Place> places = MenuItemTagsHelper.setMenu(allPlaces, tags, placeDetails);
        placeService.updateAll(places);
        return HttpStatus.OK;
    }

    @GetMapping(value = "/fill-place-details")
    public HttpStatus fillPlaceDetails() {
        detailService.saveAll(TagsHelper.getPlaceDetails());
        return HttpStatus.OK;
    }

    /**
     * Fill all tags
     */
    @GetMapping(value = "/fill-tags")
    public HttpStatus tagsFilling() {
        menuItemsGeneratorService.deleteAllMenuTags();
        tagService.saveAll(MenuItemTagsHelper.getParentTags());
        tagService.saveAll(MenuItemTagsHelper.setChildTagsToParentTags(tagService.findAllByType(TagType.MENU_ITEM_GROUP)));
        //tagService.saveAll(TagsHelper.getAllPlaceDetailsTags());
        //tagService.saveAll(TagsHelper.getDietsAndAllergiesTags());
        return HttpStatus.OK;
    }

    @GetMapping(value = "/fill-curators")
    public HttpStatus curatorsFilling() {
        List<AppUser> appUsers = userService.findAll();
        if (appUsers.size() == 0) {
            log.error("Fill users!");
            return HttpStatus.BAD_REQUEST;
        }
        List<AppUser> curators = B2CTestHelper.getCurators(appUsers,
                roleService.getRoleByRoleType(RoleType.ROLE_APP_CURATOR));
        userService.updateAll(curators);
        return HttpStatus.OK;
    }

    @GetMapping(value = "/fill-followers-and-following")
    public HttpStatus fillFollowersAndFollowing() {
        List<AppUser> appUsers = userService.findAll();
        if (appUsers.size() == 0) {
            log.error("Fill users!");
            return HttpStatus.BAD_REQUEST;
        }
        List<AppUser> users = B2CTestHelper.fillFollowersAndFollowing(appUsers);
        userService.updateAll(users);
        return HttpStatus.OK;
    }

    @GetMapping(value = "/set-place-categories-image")
    public HttpStatus setPlaceCategories() {
        ImageService imageService = context.getBean(ImageService.class);
        List<Place> places = placeService.findAll();
        places.forEach(place -> {
            boolean isFreePlaceCatImage = place.getImages().stream()
                    .anyMatch(image -> image.getType().equals(ImageType.FREE_PLACE_CATEGORY));
            if (!isFreePlaceCatImage) {
                imageService.setPlaceCategoryImage(place.getId());
            }
        });
        return HttpStatus.OK;
    }

    @GetMapping(value = "/fill-favorite-places")
    public HttpStatus fillRecommendedPlaces() {
        List<Place> places = placeService.findAll();
        List<AppUser> usersList = userService.findAll();
        if (places.size() == 0 || usersList.size() == 0) {
            log.error("Fill places or users!");
            return HttpStatus.BAD_REQUEST;
        }
        List<AppUser> users = B2CTestHelper.fillRecommendedPlaces(usersList, places);
        userService.updateAll(users);
        return HttpStatus.OK;
    }

    /**
     * Fill users without taste tags
     * To fill user taste tags use method "userTagsFilling()" or "baseDataFilling()"
     */
    @GetMapping("/fill-users")
    public HttpStatus defaultUserFilling(@RequestParam("amount") Integer userNumber) {
        if (userNumber == null) {
            userNumber = 1000;
        }

        List<AppUser> users = B2CTestHelper.getUsers(userNumber, roleService.getRoleByRoleType(RoleType.ROLE_APP_USER));
        userService.saveAll(users);

//        TODO add generation of recommended places

        return HttpStatus.OK;
    }

    @GetMapping("/fill-similiar-places")
    public HttpStatus defaultPlaceSimiliarityFilling() {
        similiarPlacesService.addSimiliarPlacesToAllPlaces();

        return HttpStatus.OK;
    }

    @GetMapping("/fill-user-pref-ratings")
    public HttpStatus setUserPreferencesRatings() {
        //userFavoritePlaceService.setUserPraferencesRatings();
        return HttpStatus.OK;
    }

    @GetMapping("/fill-demo-contextual")
    public HttpStatus fillDemoContextual() {
        List<AppUserGreeting> contextGreetings = GreetingHelper.getContextGreetings();
        greetingService.saveAll(contextGreetings);
        return HttpStatus.OK;
    }

    @GetMapping(value = "/update-tags-pool")
    public HttpStatus updateTagsPool() {
        TagPool tagPool = context.getBean(TagPool.class);
        tagPool.updateTagsPool();
        return HttpStatus.OK;
    }

    @GetMapping(value = "/revert-place-tags")
    public HttpStatus revertPlaceTags() {
        List<Place> places = placeService.findAll();
        places.forEach(place -> {
            Set<String> detailNames = place.getPlaceDetails().stream()
                    .map(PlaceDetail::getName)
                    .collect(Collectors.toSet());
            Set<Tag> detailTags = tagService.findByNameIn(detailNames);
            place.setTags(detailTags);
        });
        return HttpStatus.OK;
    }

    @GetMapping("/fill-greetings")
    public HttpStatus fillUserGreetings() {

        Map<Locale, LocalizedGreeting> goodWeatherGreetings = new HashMap<>();
        goodWeatherGreetings.put(Locale.ENGLISH, LocalizedGreeting.of()
                .locale(Locale.ENGLISH)
                .greeting("Good morning #user_name, today is perfect weather!")
                .create());
        goodWeatherGreetings.put(Locale.GERMAN, LocalizedGreeting.of()
                .locale(Locale.GERMAN)
                .greeting("Guten Morgen #user_name, heute ist perfektes Wetter!")
                .create());

        Set<AppUserGreetingDatePeriod> dates = new HashSet<>();
        dates.add(AppUserGreetingDatePeriod.of()
                .fromDate(LocalDate.of(2017, 02, 01))
                .toDate(LocalDate.of(2017, 03, 01))
                .create());
        dates.add(AppUserGreetingDatePeriod.of()
                .fromDate(LocalDate.of(2017, 04, 01))
                .toDate(LocalDate.of(2017, 05, 01))
                .create());

        Set<DayOfWeek> days = new HashSet<>();
        days.add(DayOfWeek.FRIDAY);
        days.add(DayOfWeek.SUNDAY);
        days.add(DayOfWeek.SATURDAY);

        Set<Month> months = new HashSet<>();
        months.add(Month.JUNE);
        months.add(Month.OCTOBER);
        months.add(Month.DECEMBER);

        Set<AppUserGreetingTimePeriod> times = new HashSet<>();
        times.add(AppUserGreetingTimePeriod.of()
                .fromTime(LocalTime.of(6, 30))
                .toTime(LocalTime.of(11, 00))
                .create());

        AppUserGreeting appUserGreeting = AppUserGreeting.of()
                .name("Good morning good weather")
                .type(AppUserGreetingType.GENERAL)
                .greetingText(goodWeatherGreetings)
                .isDatePeriod(true)
                .dates(dates)
                .isActive(false)
                .isTimePeriod(true)
                .times(times)
                .create();

        greetingService.save(appUserGreeting);


        Map<Locale, LocalizedGreeting> coldMorningGreeting = new HashMap<>();
        coldMorningGreeting.put(Locale.ENGLISH, LocalizedGreeting.of()
                .locale(Locale.ENGLISH)
                .greeting("Brrr it's freezing today, hot cup of #drink?")
                .create());
        coldMorningGreeting.put(Locale.GERMAN, LocalizedGreeting.of()
                .locale(Locale.GERMAN)
                .greeting("Brrr friert es heute heißen Tasse #drink?")
                .create());

        Set<AppUserGreetingTimePeriod> coldTimes = new HashSet<>();
        coldTimes.add(AppUserGreetingTimePeriod.of()
                .fromTime(LocalTime.of(6, 30))
                .toTime(LocalTime.of(11, 00))
                .create());

        Set<WeatherOption> weatherOptions = new HashSet<>();
        weatherOptions.add(WeatherOption.of()
                .fromTemperature(-50D)
                .toTemperature(0D)
                .icon(WeatherIcon.CLEAR_DAY)
                .icon(WeatherIcon.CLEAR_NIGHT)
                .create());
        weatherOptions.add(WeatherOption.of()
                .fromTemperature(-50D)
                .toTemperature(0D)
                .icon(WeatherIcon.CLOUDY)
                .create());
        weatherOptions.add(WeatherOption.of()
                .fromTemperature(-50D)
                .toTemperature(0D)
                .icon(WeatherIcon.FOG)
                .create());

        Set<DayTimePeriod> coldDayOpenTimes = new HashSet<>();
        coldDayOpenTimes.add(DayTimePeriod.of()
                .dayOfWeek(DayOfWeek.MONDAY)
                .fromTime(LocalTime.of(8, 30))
                .toTime(LocalTime.of(23, 00))
                .create());
        coldDayOpenTimes.add(DayTimePeriod.of()
                .dayOfWeek(DayOfWeek.THURSDAY)
                .fromTime(LocalTime.of(8, 30))
                .toTime(LocalTime.of(23, 00))
                .create());
        coldDayOpenTimes.add(DayTimePeriod.of()
                .dayOfWeek(DayOfWeek.WEDNESDAY)
                .fromTime(LocalTime.of(9, 00))
                .toTime(LocalTime.of(23, 00))
                .create());

        AppUserGreeting coldAppUserGreeting = AppUserGreeting.of()
                .name("It's freezing today")
                .type(AppUserGreetingType.CONTEXT_SPECIFIC)
                .greetingText(coldMorningGreeting)
                .isDatePeriod(false)
                .isTimePeriod(true)
                .isActive(true)
                .times(coldTimes)
                .isContextSpecific(true)
                .create();

        coldAppUserGreeting.setSpecificOption(AppUserGreetingContextSpecific.of()
                .weatherOptions(weatherOptions)
                .periods(coldDayOpenTimes)
                .appUserGreeting(coldAppUserGreeting)
                .create());

        greetingService.save(coldAppUserGreeting);

        return HttpStatus.OK;
    }

    @GetMapping("/fill-places-elastic")
    public HttpStatus fillPlacesElastic() throws Exception {
        try {
            List<Place> places = placeService.findAll();

            List<ESRankablePlace> ESRankablePlaces = places.stream()
                    .map(PlaceConverter::toSearchablePlace)
                    .collect(Collectors.toList());

            PlaceESRepository placeRepository = context.getBean(PlaceESRepository.class);
            placeRepository.save(ESRankablePlaces);
        } catch (Throwable e) {
            throw new Exception(e.getMessage(), e);
        }
        return HttpStatus.OK;
    }

    @GetMapping("/fill-ranks")
    public HttpStatus defaultRankingRateFilling() {

        RankingRateService rateService = context.getBean(RankingRateService.class);

        RankingType[] rankingTypes = RankingType.values();
        List<RankingRate> rankingRates = new ArrayList<>();

        for (RankingType rankType : rankingTypes) {
            rankingRates.add(RankingRate.of()
                    .id(rankType.getId())
                    .name(rankType.getName())
                    .type(rankType)
                    .rate(BigDecimal.valueOf(rankType.getDefaultRate()))
                    .create());
        }

        rateService.saveAll(rankingRates);

        return HttpStatus.OK;
    }

    @GetMapping("/fill-places-mongo")
    public HttpStatus fillMongoPlaces() {
        List<Place> places = placeService.findAll();

        MongoPlaceConverter mongoPlaceConverter = context.getBean(MongoPlaceConverter.class);
        List<MongoPlace> mongoPlaces = places.stream()
                .map(mongoPlaceConverter::toMongoPlace)
                .collect(Collectors.toList());

        MongoPlaceRepository mongoPlaceRepository = context.getBean(MongoPlaceRepository.class);
        mongoPlaceRepository.save(mongoPlaces);

        return HttpStatus.OK;
    }

    @GetMapping("/delete-places-mongo")
    public ResponseEntity<String> deleteMongoPlaces() {

        MongoPlaceRepository mongoPlaceRepository = context.getBean(MongoPlaceRepository.class);
        long count = mongoPlaceRepository.count();
        mongoPlaceRepository.deleteAll();

        return ResponseEntity.ok(String.valueOf(count) + " places successfully deleted!");
    }

    @GetMapping(value = "/find-places-by-user-tag-mongo/{userId}")
    public List<MongoPlace> findPlaceByTags(@PathVariable Long userId) {

        MongoPlaceRepository mongoPlaceRepository = context.getBean(MongoPlaceRepository.class);

        AppUser appUser = userService.findById(userId);
        Set<Tag> tasteTags = appUser.getUserPreferences().getTasteTags();

        String tags = tasteTags.stream()
                .map(tag -> tag.getName())
                .collect(Collectors.joining(" "));

        List<MongoPlace> mongoPlacesWithRate = mongoPlaceRepository
                .findAllByOrderByScoreDesc(TextCriteria.forDefaultLanguage().matching(tags));

        TextQuery textQuery = TextQuery.queryText(TextCriteria.forDefaultLanguage()
                .matchingAny(tags))
                .includeScore("place_tags")
                .includeScore("place_detail_tags")
                .includeScore("menu_item_tags")
                .sortByScore();

        List<MongoPlace> mongoPlaces = mongoOperations.find(textQuery, MongoPlace.class);
        CloseableIterator<MongoPlace> stream = mongoOperations.stream(textQuery, MongoPlace.class);
        System.out.println("done");

        return mongoPlacesWithRate;
    }

    @GetMapping(value = "spark-mongo-test", produces = "application/json")
    public List<Document> sparkMongoTest() {

        List<Document> documents = new ArrayList<>();

        FAQJavaSparkOperationsService faqBean = context.getBean(FAQJavaSparkOperationsService.class);
        documents.addAll(faqBean.getAllFAQDocuments());

        PlaceSparkOperationsService placeBean = context.getBean(PlaceSparkOperationsService.class);
        documents.addAll(placeBean.getAllPlaceDocuments());

        ProblemReportSparkOperationsService problemReportBean = context.getBean(ProblemReportSparkOperationsService.class);
        documents.addAll(problemReportBean.getAllProblemReportDocuments());

        return documents;
    }

    @GetMapping(value = "fill-faq-report", produces = "application/json")
    public ResponseEntity<HttpStatus> fillFAQ() {

        List<FAQ> faqs = new ArrayList<>();

        faqs.add(FAQ.of()
                .question("How create plan")
                .answer("Go to time planning and create new plan")
                .type(FAQ.FAQType.B2C)
                .create());

        faqs.add(FAQ.of()
                .question("How search places")
                .answer("Tap smart search and tipe smth like stake in da house")
                .type(FAQ.FAQType.B2C)
                .create());

        FAQRepository faqRepository = context.getBean(FAQRepository.class);
        faqRepository.save(faqs);

        List<ProblemReport> reports = new ArrayList<>();
        reports.add(ProblemReport.of()
                .firstName("Vasya")
                .lastName("Pupkin")
                .email("vasya.pupkin@gmail.com")
                .description("Хьюстон, у нас проблема")
                .create());

        ProblemReportRepository reportRepository = context.getBean(ProblemReportRepository.class);
        reportRepository.save(reports);

        return ResponseEntity.ok().build();
    }


    @GetMapping(value = "fill-suggestion-category", produces = "application/json")
    public ResponseEntity<List<SuggestionCategory>> fillSuggestionCategory() {

        SuggestionCategoryService categoryService = context.getBean(SuggestionCategoryService.class);

        Map<String, SuggestionCategory> suggestionCategories = B2CTestHelper.createSuggestionCategories();

        Set<SuggestionCategory> categorySet = suggestionCategories.entrySet().stream()
                .map(entry -> entry.getValue())
                .peek(category -> category.setTags(
                        tagService.findDictinctByNames(category.getTags())))
                .collect(Collectors.toSet());

        List<SuggestionCategory> categories = categoryService.saveAll(suggestionCategories.entrySet().stream()
                .map(x -> x.getValue())
                .collect(Collectors.toList()));

        return ResponseEntity.ok().body(categories);
    }


    @GetMapping(value = "fill-schedules", produces = "application/json")
    public ResponseEntity<HttpStatus> fillSchedules() {

        List<Schedule> schedules = Lists.newArrayList();

        for (int i = 1; i < 10; i++) {
            CollectionUtils.addIgnoreNull(schedules,
                    B2BTestHelper.createSchedule(B2BTestHelper.chooseSchedule(i)));
        }

        ScheduleService scheduleService = context.getBean(ScheduleService.class);
        scheduleService.saveAll(schedules);

        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "fill-places-schedules", produces = "application/json")
    public ResponseEntity<HttpStatus> fillPlacesSchedules() {

        ScheduleService scheduleService = context.getBean(ScheduleService.class);
        List<Place> places = placeService.getPlacesWithoutSchedule();

        for (Place place : places) {
            Schedule schedule = scheduleService.findOneByName(B2BTestHelper.chooseSchedule(RandomUtils.nextInt(1, 9)));
            place.setSchedule(schedule);
        }

        placeService.updateAll(places);

        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "update-sgst-pool", produces = "application/json")
    public ResponseEntity<HttpStatus> updateSuggestionPool() {

        SuggestionCategoryPool categoryPool = context.getBean(SuggestionCategoryPool.class);
        categoryPool.updateSuggestionCategories();

        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "mongo-aggr", produces = "application/json")
    public ResponseEntity<HttpStatus> getPlacesWithMongoAggregation() {

        SuggestionCategoryService categoryService = context.getBean(SuggestionCategoryService.class);
        List<SuggestionCategory> suggestionCategories = categoryService.findAll();

        for (SuggestionCategory suggestionCategory : suggestionCategories) {
            MongoPlaceService mongoPlaceService = context.getBean(MongoPlaceService.class);
            List<MongoPlace> byParams = mongoPlaceService.getPlacesByCategoryOptions(suggestionCategory);
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/generate-menu-items")
    public HttpStatus generateMenuItems() {
        menuItemsGeneratorService.processMenuGeneration();
        return HttpStatus.OK;
    }

    @GetMapping(value = "/update-place-tags-demo")
    public HttpStatus updatePlaceTagsDemo() {
        List<Place> places = placeService.findAll();
        places.forEach(place -> placeService.fillTagsFromPlaceDetailsAfterMenuGeneration(place));
        return HttpStatus.OK;
    }

    @GetMapping(value = "/recommend-50-places")
    public HttpStatus recommend50Places() {
        List<Place> places = placeService.findAll();
        MongoPlaceService mongoPlaceService = context.getBean(MongoPlaceService.class);
        Collections.shuffle(places);
        if (places.size() > 50) {
            List<Place> recommendedPlaces = places.subList(0, 50);
            AppUserCollection recPlacesCollection = AppUserCollection.of()
                    .collectionName(AppUserCollectionType.RECOMMENDED_PLACES.name())
                    .collectionType(AppUserCollectionType.RECOMMENDED_PLACES)
                    .places(new HashSet<>(recommendedPlaces))
                    .create();

            AppUser appUser = AppUser.of()
                    .userPreferences(AppUserPreference.of()
                            .collection(recPlacesCollection)
                            .tasteTags(new HashSet<>(tagService.findAll().subList(0, 3)))
                            .create())
                    .create();
            RoleService roleService = context.getBean(RoleService.class);
            appUser.getUserPreferences().setUser(appUser);
            appUser.getUserPreferences().getCollections().get(0).setAppUserPreference(appUser.getUserPreferences());
            appUser.setRole(roleService.findByRoleType(RoleType.ROLE_APP_CURATOR));
            DemoAppUserConsumer.autoFieldsGeneratorCuratorConsumer.accept(appUser);

            List<Long> ids = recommendedPlaces.stream()
                    .map(Place::getId)
                    .collect(Collectors.toList());

            List<MongoPlace> recMongoPlaces = mongoPlaceService.findAllBySqlEntityIdIn(ids);
            userService.save(appUser);
            recMongoPlaces.forEach(mongoPlace ->
                    mongoPlace.setCuratorRecommendations(mongoPlace.getCuratorRecommendations() + 1));
            mongoPlaceService.save(recMongoPlaces);
        } else {
            throw new IndexOutOfBoundsException("For recommend 50 places size must be greater than 50!");
        }
        return HttpStatus.OK;
    }

}
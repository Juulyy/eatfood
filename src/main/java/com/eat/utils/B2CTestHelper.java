package com.eat.utils;

import com.eat.models.b2b.Place;
import com.eat.models.b2c.*;
import com.eat.models.b2c.enums.AppUserCollectionType;
import com.eat.models.b2c.enums.PrivacyType;
import com.eat.models.common.Image;
import com.eat.models.common.Role;
import com.eat.models.common.Tag;
import com.eat.models.common.enums.ImageType;
import com.eat.models.recommender.*;
import com.eat.models.common.enums.TagType;
import com.google.common.collect.Sets;
import lombok.extern.log4j.Log4j;
import org.ajbrown.namemachine.Gender;
import org.ajbrown.namemachine.Name;
import org.ajbrown.namemachine.NameGenerator;
import org.apache.commons.lang3.RandomUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Log4j
public class B2CTestHelper {

    private static String loginFormat = "%s%s";
    private static String emailFormat = "%s_%s@gmail.com";

    public static List<Tag> filterTagsByTagType(List<Tag> tags, TagType type) {
        return tags.parallelStream()
                .filter(tag -> tag.getType().equals(type))
                .collect(Collectors.toList());
    }

    public static List<AppUser> fillUserTasteTags(List<AppUser> users, List<Tag> tags) {

        List<Tag> cuisinesTags = filterTagsByTagType(tags, TagType.CUISINE);
        List<Tag> atmosphereTags = filterTagsByTagType(tags, TagType.ATMOSPHERE);
        List<Tag> featureTags = filterTagsByTagType(tags, TagType.FEATURE);
        List<Tag> interiorTags = filterTagsByTagType(tags, TagType.INTERIOR);
        List<Tag> menuItemTags = filterTagsByTagType(tags, TagType.MENU_ITEM);
        List<Tag> allergyTags = filterTagsByTagType(tags, TagType.ALLERGY);
        List<Tag> dietTags = filterTagsByTagType(tags, TagType.DIET);
        List<Tag> musicTags = filterTagsByTagType(tags, TagType.MUSIC);

        users.forEach(appUser -> {

            appUser.getUserPreferences().setTasteTags(new HashSet<>()); // error with deserialize, getTasteTags == null

            int size = RandomUtils.nextInt(1, 5);
            for (int i = 0; i < size; i++) {
                appUser.getUserPreferences().getTasteTags()
                        .add(cuisinesTags.get(RandomUtils.nextInt(0, cuisinesTags.size() - 1)));
            }

            size = RandomUtils.nextInt(1, 5);
            for (int i = 0; i < size; i++) {
                appUser.getUserPreferences().getTasteTags()
                        .add(atmosphereTags.get(RandomUtils.nextInt(0, atmosphereTags.size() - 1)));
            }

            size = RandomUtils.nextInt(1, 5);
            for (int i = 0; i < size; i++) {
                appUser.getUserPreferences().getTasteTags()
                        .add(featureTags.get(RandomUtils.nextInt(0, featureTags.size() - 1)));
            }

            size = RandomUtils.nextInt(1, 5);
            for (int i = 0; i < size; i++) {
                appUser.getUserPreferences().getTasteTags()
                        .add(interiorTags.get(RandomUtils.nextInt(0, interiorTags.size() - 1)));
            }

            size = RandomUtils.nextInt(1, 3);
            for (int i = 0; i < size; i++) {
                appUser.getUserPreferences().getTasteTags()
                        .add(musicTags.get(RandomUtils.nextInt(0, musicTags.size() - 1)));
            }

            size = RandomUtils.nextInt(4, 10);
            for (int i = 0; i < size; i++) {
                Tag tag = menuItemTags.get(RandomUtils.nextInt(0, menuItemTags.size() - 1));
                if (tag.getParent() != null) {
                    appUser.getUserPreferences().getTasteTags().add(tag.getParent());
                    if (tag.getParent().getParent() != null) {
                        appUser.getUserPreferences().getTasteTags().add(tag.getParent().getParent());
                    }
                }
                appUser.getUserPreferences().getTasteTags().add(tag);
            }

            size = RandomUtils.nextInt(1, 3);
            int chance = RandomUtils.nextInt(1, 11);

            if (chance % 2 == 0) {
                for (int i = 0; i < size; i++) {
                    appUser.getUserPreferences().getTasteTags()
                            .add(allergyTags.get(RandomUtils.nextInt(0, allergyTags.size() - 1)));
                }
            }

            if (chance % 3 == 0) {
                for (int i = 0; i < size; i++) {
                    appUser.getUserPreferences().getTasteTags()
                            .add(dietTags.get(RandomUtils.nextInt(0, dietTags.size() - 1)));
                }
            }

        });
        return users;
    }

    public static List<AppUser> fillRecommendedPlaces(List<AppUser> users, List<Place> places) {
        users.forEach(appUser -> getFavoritePlaces(appUser, places));
        return users;
    }

    public static AppUser getFavoritePlaces(AppUser appUser, List<Place> places) {
        int size = RandomUtils.nextInt(1, 10);
        AppUserCollection recommendedPlaces = AppUserCollection.of()
                .collectionName(AppUserCollectionType.RECOMMENDED_PLACES.getType())
                .collectionType(AppUserCollectionType.RECOMMENDED_PLACES)
                .collectionPrivacy(PrivacyType.MUTUAL_FOLLOWING)
                .appUserPreference(appUser.getUserPreferences())
                .places(new HashSet<>())
                .create();
        appUser.getUserPreferences().setCollections(new ArrayList<>()); //error with deserialize getCollections == null
        appUser.getUserPreferences().getCollections().add(recommendedPlaces);
        while (size != 0) {
            int placeId = RandomUtils.nextInt(1, places.size() - 1);
            Place place = places.get(placeId);
            recommendedPlaces.getPlaces().add(place);
            size--;
        }
        return appUser;
    }

    public static List<AppUser> fillFollowersAndFollowing(List<AppUser> users) {
        users.forEach(appUser -> getUserFollowers(appUser, users));
        return users;
    }

    public static AppUser getUserFollowers(AppUser appUser, List<AppUser> allUsers) {
        int size = RandomUtils.nextInt(1, 10);
        while (size != 0) {
            int userId = RandomUtils.nextInt(1, allUsers.size() - 1);
            AppUser follower = allUsers.get(userId);
            if (appUser.getId() != userId) {
                appUser.getUserPreferences().getFollowers().add(follower);
                follower.getUserPreferences().getFollowing().add(appUser);
            }
            size--;
        }
        return appUser;
    }

    public static List<AppUser> getCurators(List<AppUser> users, Role role) {
        List<AppUser> curators = new ArrayList<>();
        users.forEach(appUser -> {
            switch (RandomUtils.nextInt(1, 9)) {
                case 2:
                    appUser.setRole(role);
                    curators.add(appUser);
                    break;
                case 5:
                    appUser.setRole(role);
                    curators.add(appUser);
                    break;
                case 8:
                    appUser.setRole(role);
                    curators.add(appUser);
                    break;
            }
        });
        return curators;
    }

    public static Map<Gender, List<Name>> genarateUserNames(int number) {

        Map<Gender, List<Name>> names = new HashMap<>();

        NameGenerator generator = new NameGenerator();
        names.put(Gender.FEMALE, generator.generateNames(number / 2, Gender.FEMALE));
        names.put(Gender.MALE, generator.generateNames(number - number / 2, Gender.MALE));

        return names;
    }

    public static List<AppUser> getUsers(int number, Role userRole) {

        Map<Gender, List<Name>> userNames = genarateUserNames(number);
        List<AppUser> users = new ArrayList<>();
        List<Name> femaleNames = userNames.get(Gender.FEMALE);

        for (Name femaleName : femaleNames) {
            users.add(createUser(femaleName, Gender.FEMALE, userRole));
        }

        List<Name> maleNames = userNames.get(Gender.MALE);
        for (Name maleName : maleNames) {
            users.add(createUser(maleName, Gender.MALE, userRole));
        }

        return users;
    }

    public static AppUser createUser(Name userName, Gender gender, Role role) {

        return AppUser.of()
                .firstName(userName.getFirstName())
                .lastName(userName.getLastName())
                .gender(gender == Gender.FEMALE ? AppUser.Gender.FEMALE : AppUser.Gender.MALE)
                .role(role)
                .login(String.format(loginFormat, userName.getFirstName().substring(0, 1), userName.getLastName()))
                .email(String.format(emailFormat, userName.getLastName(), userName.getFirstName()))
                .appleId(UUID.randomUUID().toString())
                .userPreferences(AppUserPreference.of().create())
                .password("dfW7ks43Rf*")
                .photoUrl("http://amazonphotos.com/album/")
                .create();
    }

    public static Set<Tag> getUserTags() {
        Set<Tag> userTags = new HashSet<>();

        List<Tag> placeDetailsTags = TagsHelper.getAllPlaceDetailsTags();
//        List<Tag> restaurantItemTags = TagsHelper.getAllRestaurantItemTags();

        int placeTagsNumber = RandomUtils.nextInt(7, 15);
        int maxPlaceIndex = placeDetailsTags.size() - 1;
        while (placeTagsNumber > 0) {
            if (addIfNotExist(placeDetailsTags, userTags, maxPlaceIndex)) {
                maxPlaceIndex--;
            }
            placeTagsNumber--;
        }

        return new HashSet<>(userTags);
    }

    private static boolean addIfNotExist(List<Tag> tagCollection, Set<Tag> userTags, int maxIndex) {
        int index = RandomUtils.nextInt(0, maxIndex);
        Tag tag = tagCollection.get(index);

        if (userTags.contains(tag)) {
            return false;
        }
        userTags.add(tag);
        return true;
    }


    public static List<UserFavoritePlace> getUserPreferences(List<AppUser> users, List<Place> places) {

        Set<UserFavoritePlace> userFavoritePlaces = new HashSet<>();

        for (AppUser currentUser : users) {
            Set<Place> favoritePlaces = new HashSet<>();
            int likedPlaces = RandomUtils.nextInt(5, 15);

            List<Place> preferedPlaces = getPlacesByUserPreferences(currentUser, places);

            while (likedPlaces > 0) {

                int placeIndex = RandomUtils.nextInt(0, preferedPlaces.size());
                if (!favoritePlaces.contains(places.get(placeIndex))) {

                    userFavoritePlaces.add(UserFavoritePlace.of()
                            .user(currentUser)
                            .place(places.get(placeIndex))
                            .create());

                    likedPlaces--;
                }
            }

        }

        return new ArrayList<>(userFavoritePlaces);
    }

    private static List<Place> getPlacesByUserPreferences(AppUser user, List<Place> places) {

        TreeMap<Integer, List<Place>> mostMatchingPlaces = new TreeMap<>();

        Set<Tag> tasteTags = user.getUserPreferences().getTasteTags();
        for (Place place : places) {
            Set<Tag> placeTags = place.getTags();

            int matching = 0;
            for (Tag tasteTag : tasteTags) {
                for (Tag placeTag : placeTags) {
                    if (tasteTag.getName().equals(placeTag.getName())) {
                        matching++;
                    }
                }
            }

            if (matching == 0) {
                continue;
            }

            if (mostMatchingPlaces.containsKey(matching)) {
                mostMatchingPlaces.get(matching).add(place);
            } else {
                ArrayList<Place> list = new ArrayList<>();
                list.add(place);
                mostMatchingPlaces.put(matching, list);
            }
        }

        List<Place> placeList = new ArrayList<>();
        NavigableMap<Integer, List<Place>> integerListNavigableMap = mostMatchingPlaces.descendingMap();
        for (Map.Entry<Integer, List<Place>> currentEntry : integerListNavigableMap.entrySet()) {
            if (placeList.size() == 50) {
                break;
            }
            placeList.addAll(currentEntry.getValue());
        }

        return placeList;
    }


    public static Map<String, SuggestionCategory> createSuggestionCategories() {

        Map<String, SuggestionCategory> categoryMap = new HashMap<>();

        categoryMap.put("breakfasts",
                SuggestionCategory.of()
                        .description("Best breakfast in the city")
                        .useDatePeriod(true)
                        .dates(Sets.newHashSet(
                                SuggestionCategoryDatePeriod.of()
                                        .fromDate(LocalDate.of(2017, 01, 01))
                                        .toDate(LocalDate.of(2017, 04, 01))
                                        .create(),
                                SuggestionCategoryDatePeriod.of()
                                        .fromDate(LocalDate.of(2017, 10, 01))
                                        .toDate(LocalDate.of(2017, 12, 31))
                                        .create()))
                        .dayTimes(createScheduleByOptions(6, 30, 10, 30,
                                DayOfWeek.MONDAY, DayOfWeek.TUESDAY,
                                DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY))
                        .useTagFilter(true)
                        .tags(getTags("Ravioli", "Pasta", "Croissant", "Coffee"))
                        .tagRates(getTagRates())
                        .image(Image.of()
                                .imageUrl("https://s3.eu-central-1.amazonaws.com/eat-maystrovyy/addtext_com_MTIzNTA5ODY4Nzk.jpg")
                                .type(ImageType.CATEGORY)
                                .create())
                        .create());


        categoryMap.put("breakfastsCityView",
                SuggestionCategory.of()
                        .description("Bbreakfasts on terrace")
                        .useDatePeriod(true)
                        .dates(Sets.newHashSet(
                                SuggestionCategoryDatePeriod.of()
                                        .fromDate(LocalDate.of(2017, 5, 1))
                                        .toDate(LocalDate.of(2017, 9, 30))
                                        .create()))
                        .dayTimes(createScheduleByOptions(6, 30, 10, 30,
                                DayOfWeek.MONDAY, DayOfWeek.TUESDAY,
                                DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY))
                        .useTagFilter(true)
                        .tags(getTags("Ravioli", "Pasta", "Croissant", "Coffee", "City view", "Summer terrace"))
                        .tagRates(getTagRates())
                        .create());


        categoryMap.put("pubs",
                SuggestionCategory.of()
                        .description("Best pubs in city")
                        .useDatePeriod(true)
                        .dates(Sets.newHashSet(
                                SuggestionCategoryDatePeriod.of()
                                        .fromDate(LocalDate.of(2017, 01, 01))
                                        .toDate(LocalDate.of(2017, 05, 01))
                                        .create(),
                                SuggestionCategoryDatePeriod.of()
                                        .fromDate(LocalDate.of(2017, 10, 01))
                                        .toDate(LocalDate.of(2017, 12, 31))
                                        .create()))
                        .dayTimes(createScheduleByOptions(18, 0, 23, 0,
                                DayOfWeek.MONDAY, DayOfWeek.TUESDAY,
                                DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY))
                        .useTagFilter(true)
                        .tags(getTags("Beer", "Vodka", "Gin", "Whisky"))
                        .tagRates(getTagRates())
                        .create());


        categoryMap.put("nightClubs",
                SuggestionCategory.of()
                        .description("Best nightclubs")
                        .useDatePeriod(true)
                        .dates(Sets.newHashSet(
                                SuggestionCategoryDatePeriod.of()
                                        .fromDate(LocalDate.of(2017, 01, 01))
                                        .toDate(LocalDate.of(2017, 05, 01))
                                        .create(),
                                SuggestionCategoryDatePeriod.of()
                                        .fromDate(LocalDate.of(2017, 10, 01))
                                        .toDate(LocalDate.of(2017, 12, 31))
                                        .create()))
                        .dayTimes(createScheduleByOptions(20, 0, 3, 0,
                                DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY))
                        .useTagFilter(true)
                        .tags(getTags("Coctail", "DJ", "Beer", "Henessy", "Electronic"))
                        .tagRates(getTagRates())
                        .create());


        categoryMap.put("summerPubs",
                SuggestionCategory.of()
                        .description("Best pubs with city view")
                        .useDatePeriod(true)
                        .dates(Sets.newHashSet(
                                SuggestionCategoryDatePeriod.of()
                                        .fromDate(LocalDate.of(2017, 5, 1))
                                        .toDate(LocalDate.of(2017, 9, 30))
                                        .create()))
                        .dayTimes(createScheduleByOptions(18, 0, 23, 0,
                                DayOfWeek.MONDAY, DayOfWeek.TUESDAY,
                                DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY))
                        .useTagFilter(true)
                        .tags(getTags("Beer", "Vodka", "Gin", "Whisky", "City view", "Summer terrace"))
                        .tagRates(getTagRates())
                        .create());


        return categoryMap;

    }

    private static Set<SuggestionCategoryTagRate> getTagRates() {

        return Sets.newHashSet(
                SuggestionCategoryTagRate.of()
                        .tagType(TagType.MENU_ITEM)
                        .rate(1d)
                        .create(),

                SuggestionCategoryTagRate.of()
                        .tagType(TagType.MENU_ITEM_CATEGORY)
                        .rate(1d)
                        .create(),

                SuggestionCategoryTagRate.of()
                        .tagType(TagType.MENU_ITEM_CATEGORY)
                        .rate(1d)
                        .create(),

                SuggestionCategoryTagRate.of()
                        .tagType(TagType.CUISINE)
                        .rate(0.9d)
                        .create(),

                SuggestionCategoryTagRate.of()
                        .tagType(TagType.ATMOSPHERE)
                        .rate(0.8d)
                        .create(),

                SuggestionCategoryTagRate.of()
                        .tagType(TagType.FEATURE)
                        .rate(0.7d)
                        .create(),

                SuggestionCategoryTagRate.of()
                        .tagType(TagType.MUSIC)
                        .rate(0.6d)
                        .create()
        );


    }

    private static Set<DayTimePeriod> createScheduleByOptions(int hourFrom, int minuteFrom,
                                                              int hourTo, int minuteTo,
                                                              DayOfWeek... dayOfWeeks) {

        HashSet<DayTimePeriod> schedule = Sets.newHashSet();

        for (DayOfWeek dayOfWeek : dayOfWeeks) {
            schedule.add(DayTimePeriod.of()
                    .dayOfWeek(dayOfWeek)
                    .fromTime(LocalTime.of(hourFrom, minuteFrom))
                    .toTime(LocalTime.of(hourTo, minuteTo))
                    .create());
        }

        return schedule;
    }

    private static Set<Tag> getTags(String... tagNames) {

        HashSet<Tag> tags = Sets.newHashSet();

        for (String tagName : tagNames) {
            tags.add(Tag.of()
                    .name(tagName)
                    .create());
        }

        return tags;
    }
}

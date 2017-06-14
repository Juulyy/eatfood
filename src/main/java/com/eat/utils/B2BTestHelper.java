package com.eat.utils;

import com.eat.models.b2b.*;
import com.eat.models.b2b.enums.Day;
import com.eat.models.b2b.enums.PlaceDetailType;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomUtils;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Slf4j
public class B2BTestHelper {

    public static final Double BERLIN_MAX_LATITUDE = 52.57;
    public static final Double BERLIN_MIN_LATITUDE = 52.43;
    public static final Double BERLIN_MAX_LONGITUDE = 13.53;
    public static final Double BERLIN_MIN_LONGITUDE = 13.30;

    public static Schedule getSchedule() {
        String scheduleName = chooseSchedule(RandomUtils.nextInt(1, 4));
        return Schedule.of()
                .name(scheduleName)
                .openTimes(getDayOpenTime(scheduleName))
                .create();
    }

    public static List<DayOpenTime> getDayOpenTime(String name) {
        List<DayOpenTime> dayOpenTimes = new ArrayList<>();
        String[] times = name.split("-");
        LocalTime timeFrom = LocalTime.of(8, 30);
        LocalTime timeTo = LocalTime.of(23, 0);

        /*for (Day currentDay : Day.values()) {
            dayOpenTimes.add(DayOpenTime.of()
                    .day(currentDay)
                    .from(timeFrom)
                    .to(timeTo)
                    .create());
        }*/

        return dayOpenTimes;
    }

    public static ContactType getContactType() {
        return ContactType.of()
                .name(chooseContactType(RandomUtils.nextInt(1, 3)))
                .create();
    }

    private static String chooseContactType(int number) {
        switch (number) {
            case 1:
                return "email";
            case 2:
                return "phone";
            case 3:
                return "fax";
            case 4:
                return "address";
            default:
                return "address";
        }
    }

    public static MenuType getMenuType() {
        return MenuType.of()
                .name(chooseMenuType(RandomUtils.nextInt(1, 3)))
                .create();
    }

    public static Set<Menu> getMenus() {

        Set<Menu> menus = new HashSet<>();

        int menusNumber = RandomUtils.nextInt(1, 3);
        for (int i = 0; i <= menusNumber; i++) {
            menus.add(Menu.of()
                    .name(chooseMenu(RandomUtils.nextInt(1, 5)))
                    .menuType(getMenuType())
                    .menuItems(getDishes())
                    .create());
        }
        return menus;
    }

    /*public static MealType getDishType() {
        return MealType.of()
                .name(chooseDishType(RandomUtils.nextInt(1, 5)))
                .create();
    }*/

    public static List<MenuItem> getDishes() {

        Set<MenuItem> menuItems = new HashSet<>();

        int dishesNumber = RandomUtils.nextInt(1, 3);
        for (int i = 0; i <= dishesNumber; i++) {
            menuItems.add(MenuItem.of()
                    .name(chooseDish(RandomUtils.nextInt(1, 5)))
//                    .mealType(getDishType())
                    .price(RandomUtils.nextDouble(1.0, 150.000))
                    .create());
        }
        return Lists.newArrayList(menuItems);
    }

    public static OfferType getOfferType() {
        return OfferType.of()
                .name(chooseOfferType(RandomUtils.nextInt(1, 2)))
                .create();
    }

    public static Set<Offer> getOffers() {
        Set<Offer> offers = new HashSet<>();

        int offersNumber = RandomUtils.nextInt(1, 3);
        for (int i = 0; i <= offersNumber; i++) {
            offers.add(Offer.of()
                    .name(chooseOffer(RandomUtils.nextInt(1, 2)))
                    .offerType(getOfferType())
                    .expirationDate(LocalDateTime.of(RandomUtils.nextInt(2017, 2019), RandomUtils.nextInt(1, 12), RandomUtils.nextInt(1, 28), RandomUtils.nextInt(1, 23), RandomUtils.nextInt(1, 59)))
                    .create());
        }
        return offers;
    }

    public static List<PlaceNetwork> getEstablishmentNetworks() {

        Set<PlaceNetwork> placeNetworks = new HashSet<>();

        int establishmentNetworksNumber = RandomUtils.nextInt(1, 3);
        for (int i = 0; i <= establishmentNetworksNumber; i++) {
            placeNetworks.add(PlaceNetwork.of()
                    .name(chooseEstablishmentNetwork(RandomUtils.nextInt(1, 7)))
                    .places(getEstablishments())
                    .create());
        }
        initEstablishmentNetwork(placeNetworks);
        return Lists.newArrayList(placeNetworks);
    }

    public static void initEstablishmentNetwork(Set<PlaceNetwork> placeNetworks) {
        placeNetworks.forEach(establishmentNetwork ->
                establishmentNetwork.getPlaces()
                        .forEach(establishment -> establishment.setPlaceNetwork(establishmentNetwork)));
    }

    public static void initBusinesses(Set<BusinessUser> businesses) {
        businesses.forEach(business -> business.getPlaceNetworks().forEach(
                establishmentNetwork -> establishmentNetwork.setBusinessUser(business)));

        businesses.forEach(
                business -> business.getPlaceNetworks().
                        forEach(establishmentNetwork -> {
                                    establishmentNetwork.setBusinessUser(business);
                                    establishmentNetwork.getPlaces()
                                            .forEach(establishment -> establishment.getBusinessUsers().add(business));
                                }
                        )
        );

//        for (BusinessUser business : businesses) {
//            List<Place> places = business.getPlaces();
//            if(places == null){
//                continue;
//            }
//            for (Place place : places) {
//                place.setBusinessUser(business);
//            }
//        }
    }

    public static List<PlaceNetwork> getEstablishmentNetworksWithOffer() {

        Set<PlaceNetwork> placeNetworks = new HashSet<>();

        int establishmentNetworksNumber = RandomUtils.nextInt(1, 3);
        for (int i = 0; i <= establishmentNetworksNumber; i++) {
            placeNetworks.add(PlaceNetwork.of()
                    .name(chooseEstablishmentNetwork(RandomUtils.nextInt(1, 7)))
                    .places(getEstablishments())
                    .create());
        }
        return Lists.newArrayList(placeNetworks);
    }

    public static List<PlaceNetwork> getEstablishmentNetworksWithMenu() {

        Set<PlaceNetwork> placeNetworks = new HashSet<>();

        int establishmentNetworksNumber = RandomUtils.nextInt(1, 3);
        for (int i = 0; i <= establishmentNetworksNumber; i++) {
            placeNetworks.add(PlaceNetwork.of()
                    .name(chooseEstablishmentNetwork(RandomUtils.nextInt(1, 7)))
                    .places(getEstablishments())
                    .create());
        }
        return Lists.newArrayList(placeNetworks);
    }

    public static List<PlaceNetwork> getEstablishmentNetworksWithSchedule() {

        Set<PlaceNetwork> placeNetworks = new HashSet<>();

        int establishmentNetworksNumber = RandomUtils.nextInt(1, 3);
        for (int i = 0; i <= establishmentNetworksNumber; i++) {
            placeNetworks.add(PlaceNetwork.of()
                    .name(chooseEstablishmentNetwork(RandomUtils.nextInt(1, 7)))
                    .places(getEstablishments())
                    .create());
        }
        return Lists.newArrayList(placeNetworks);
    }

    public static BusinessUser getBusiness() {

        BusinessUser business = BusinessUser.of()
//                .name(chooseBusiness(RandomUtils.nextInt(1, 7)))
                .create();

        return business;
    }

    public static List<BusinessUser> getBusinessWithEstablishment() {

        Set<BusinessUser> businesses = new HashSet<>();

        int businessesNumber = RandomUtils.nextInt(1, 3);
        for (int i = 0; i <= businessesNumber; i++) {
            businesses.add(BusinessUser.of()
//                    .name(chooseBusiness(RandomUtils.nextInt(1, 7)))
//                    .place(getEstablishments())
                    .create());
        }

        return Lists.newArrayList(businesses);
    }

    public static List<BusinessUser> getBusinessWithEstablishmentNetwork() {

        Set<BusinessUser> businesses = new HashSet<>();

        int businessesNumber = RandomUtils.nextInt(1, 3);
        for (int i = 0; i <= businessesNumber; i++) {
            businesses.add(BusinessUser.of()
//                    .name(chooseBusiness(RandomUtils.nextInt(1, 7)))
                    .placeNetworks(getEstablishmentNetworks())
                    .create());
        }
        initBusinesses(businesses);
        return Lists.newArrayList(businesses);
    }

    public static String generateEstablishmentName(PlaceDetail type) {

        String EstablishmentName = "";

        if ("Restaurant".equals(type.getName()) || "Barbecue".equals(type.getName()) || "Café".equals(type.getName())) {
            EstablishmentName = chooseRestuarantAndCafeName(RandomUtils.nextInt(1, 25));
        } else if ("Cafeteria".equals(type.getName()) || "Brasserie / bistro".equals(type.getName())
                || "Pizza place".equals(type.getName()) || "Coffeehouse".equals(type.getName())) {
            EstablishmentName = chooseFastFoodAndCoffeeHouseName(RandomUtils.nextInt(1, 25));
        } else if ("Pub / bar".equals(type.getName()) || "Nightclub".equals(type.getName())) {
            EstablishmentName = chooseBarAndClubName(RandomUtils.nextInt(1, 25));
        }

        return EstablishmentName;
    }

    public static List<Place> getEstablishments() {
        return getEstablishments(true, true, true);
    }

    public static List<Place> getEstablishments(boolean withOffer) {
        return getEstablishments(withOffer, true, true);
    }

    public static List<Place> getEstablishments(boolean withOffer, boolean withMenu, boolean withSchedule) {

        Set<Place> places = new HashSet<>();

        int establishmentsNumber = RandomUtils.nextInt(1, 3);
        for (int i = 0; i <= establishmentsNumber; i++) {
            Set<PlaceDetail> placeTypes = getEstablishmentType();

            places.add(Place.of()
                    .placeDetails(placeTypes)
//                    .name(generateEstablishmentName(placeTypes.get(0)))
                    .isActive(true)
                    .latitude(RandomUtils.nextDouble(BERLIN_MIN_LATITUDE, BERLIN_MAX_LATITUDE))
                    .longtitude(RandomUtils.nextDouble(BERLIN_MIN_LONGITUDE, BERLIN_MAX_LONGITUDE))
                    .placeDetails(getBusinessDetails())
                    .placeDetails(getCuisines())
                    .businessUser(getBusiness())
                    .offers(withOffer ? getOffers() : null)
                    .schedule(withSchedule ? getSchedule() : null)
                    .menus(withMenu ? getMenus() : null)
                    .create());
        }
        return Lists.newArrayList(places);

    }

    public static Set<PlaceDetail> getEstablishmentType() {
        HashSet set = new HashSet();
        set.add(PlaceDetail.of()
                .name(chooseEstablishmentType(RandomUtils.nextInt(1, 9)))
                .placeDetailType(PlaceDetailType.PLACE_TYPE)
                .create());
        return set;
    }

    public static Set<PlaceDetail> getCuisines() {
        Set<PlaceDetail> cuisines = new HashSet<>();

        int cuisinesNumber = RandomUtils.nextInt(1, 3);
        for (int i = 0; i <= cuisinesNumber; i++) {
            cuisines.add(PlaceDetail.of()
                    .name(chooseCuisine(RandomUtils.nextInt(1, 11)))
                    .placeDetailType(PlaceDetailType.CUISINE)
                    .create());
        }
        return cuisines;
    }

    public static Set<PlaceDetail> getBusinessDetails() {
        Set<PlaceDetail> details = new HashSet<>();
        CollectionUtils.addAll(details, getAtmospheres());
        CollectionUtils.addAll(details, getInteriorDesign());
        CollectionUtils.addAll(details, getFeatures());
        CollectionUtils.addAll(details, getMusics());
        return details;
    }

    public static List<PlaceDetail> getFeatures() {
        Set<PlaceDetail> features = new HashSet<>();
        int featureNumber = RandomUtils.nextInt(1, 3);
        for (int i = 0; i <= featureNumber; i++) {
            features.add(PlaceDetail.of()
                    .name(chooseFeature(RandomUtils.nextInt(1, 9)))
                    .placeDetailType(PlaceDetailType.FEATURE)
                    .create());
        }
        return Lists.newArrayList(features);
    }

    public static List<PlaceDetail> getMusics() {
        Set<PlaceDetail> musics = new HashSet<>();
        int musicNumber = RandomUtils.nextInt(1, 3);
        for (int i = 0; i <= musicNumber; i++) {
            musics.add(PlaceDetail.of()
                    .name(chooseMusic(RandomUtils.nextInt(1, 7)))
                    .placeDetailType(PlaceDetailType.MUSIC)
                    .create());
        }
        return Lists.newArrayList(musics);
    }

    public static Set<PlaceDetail> getAtmospheres() {
        Set<PlaceDetail> atmospheres = new HashSet<>();
        int atmNumber = RandomUtils.nextInt(1, 3);
        for (int i = 0; i <= atmNumber; i++) {
            atmospheres.add(PlaceDetail.of()
                    .name(chooseAtmosphere(RandomUtils.nextInt(1, 11)))
                    .placeDetailType(PlaceDetailType.ATMOSPHERE)
                    .create());
        }
        return atmospheres;
    }

    public static List<PlaceDetail> getInteriorDesign() {
        return Arrays.asList(PlaceDetail.of()
                .name(chooseInteriorDesign(RandomUtils.nextInt(1, 11)))
                .placeDetailType(PlaceDetailType.INTERIOR)
                .create());
    }

    public static String chooseEstablishmentType(int number) {
        switch (number) {
            case 1:
                return "Restaurant";
            case 2:
                return "Barbecue";
            case 3:
                return "Café";
            case 4:
                return "Coffeehouse";
            case 5:
                return "Cafeteria";
            case 6:
                return "Brasserie / bistro";
            case 7:
                return "Pizza place";
            case 8:
                return "Pub / bar";
            case 9:
                return "Nightclub";
            default:
                return "Café";
        }
    }

    public static String chooseEstablishmentNetwork(int number) {
        switch (number) {
            case 1:
                return "700 Grad";
            case 2:
                return "Wilson’s";
            case 3:
                return "Cocolo Ramen";
            case 4:
                return "Block House";
            case 5:
                return "Weihenstephaner";
            case 6:
                return "Tommi’s Burger Joint";
            case 7:
                return "Vapiano";
            default:
                return "Jim Block";
        }
    }

    public static String chooseBusiness(int number) {
        switch (number) {
            case 1:
                return "Delicious food inc";
            case 2:
                return "Wilson’s inc";
            case 3:
                return "Fast food restaurant business";
            case 4:
                return "Starbaks inc";
            case 5:
                return "White star inc";
            case 6:
                return "Subway inc";
            case 7:
                return "Vapiano inc";
            default:
                return "Jim Block inc";
        }
    }

    public static String chooseMenuType(int number) {
        switch (number) {
            case 1:
                return "Buffet Style";
            case 2:
                return "Menu à La carte";
            case 3:
                return "Tourist menu";
            default:
                return "A parte";
        }
    }

    public static String chooseMenu(int number) {
        switch (number) {
            case 1:
                return "Banquet menu";
            case 2:
                return "Menu baby food";
            case 3:
                return "Menu daily diet";
            default:
                return "Menu diet";
        }
    }

    private static String chooseDishType(int number) {
        switch (number) {
            case 1:
                return "Cold snack";
            case 2:
                return "Hot snack";
            case 3:
                return "Salads";
            case 4:
                return "Entrees";
            case 5:
                return "Garnish";
            default:
                return "The second hot menuItems (main)";
        }
    }

    private static String chooseDish(int number) {
        switch (number) {
            case 1:
                return "Alforno";
            case 2:
                return "Pizza";
            case 3:
                return "Pasta";
            case 4:
                return "Soup with cabbage";
            case 5:
                return "Bolonese";
            default:
                return "Borch";
        }
    }

    public static String chooseOfferType(int number) {
        switch (number) {
            case 1:
                return "Event";
            case 2:
                return "Deal";
            default:
                return "Event";
        }
    }

    public static String chooseOffer(int number) {
        switch (number) {
            case 1:
                return "Business lunch 50% discount";
            case 2:
                return "Live rock music evening";
            default:
                return "Wine unlimited";
        }
    }

    public static String chooseCuisine(int number) {
        switch (number) {
            case 1:
                return "Tex-Mex";
            case 2:
                return "Molecular gastronomy";
            case 3:
                return "Vegan/Vegetarian cuisine";
            case 4:
                return "Chinese";
            case 5:
                return "Japanese";
            case 6:
                return "Indian";
            case 7:
                return "Vietnamese";
            case 8:
                return "Thai";
            case 9:
                return "Moroccan";
            case 10:
                return "Russian";
            case 11:
                return "Turkish";
            case 12:
                return "Caucasian (Armenian, Georgian, Azerbaijani)";
            case 13:
                return "Arab";
            case 14:
                return "Halal";
            case 15:
                return "Kosher";
            case 16:
                return "Jewish";
            case 17:
                return "German";
            case 18:
                return "French";
            case 19:
                return "Spanish";
            case 20:
                return "Italian";
            case 21:
                return "Greek";
            case 22:
                return "Irish";
            case 23:
                return "British";
            case 24:
                return "American";
            case 25:
                return "Mexican";

            default:
                return "Café";
        }
    }

    public static String chooseAtmosphere(int number) {
        switch (number) {
            case 1:
                return "Casual";
            case 2:
                return "Warm";
            case 3:
                return "Enlightened";
            case 4:
                return "Quite";
            case 5:
                return "Upbeat";
            case 6:
                return "Nostalgic";
            case 7:
                return "Crowded";
            case 8:
                return "Boring";
            case 9:
                return "Posh";
            case 10:
                return "Family - friendly";
            case 11:
                return "Party - friendly";
            default:
                return "Party - friendly";
        }
    }

    public static String chooseInteriorDesign(int number) {
        switch (number) {
            case 1:
                return "Modern";
            case 2:
                return "Contemporary";
            case 3:
                return "Minimalist";
            case 4:
                return "Industrial";
            case 5:
                return "Mid-century modern";
            case 6:
                return "Traditional";
            case 7:
                return "Transitional";
            case 8:
                return "Country";
            case 9:
                return "Asian";
            case 10:
                return "Rustic";
            case 11:
                return "Shabby chic";
            default:
                return "Traditional";
        }
    }

    public static String chooseFeature(int number) {
        switch (number) {
            case 1:
                return "Kids-room";
            case 2:
                return "Summer terrace";
            case 3:
                return "Outdoor seating";
            case 4:
                return "Vegetarian-friendly";
            case 5:
                return "Local business";
            case 6:
                return "Skyline view";
            case 7:
                return "Banquets";
            case 8:
                return "City view";
            case 9:
                return "Open 24 hours";
            default:
                return "Banquets";
        }
    }

    public static String chooseMusic(int number) {
        switch (number) {
            case 1:
                return "Radio";
            case 2:
                return "DJ";
            case 3:
                return "Electronic";
            case 4:
                return "Classic";
            case 5:
                return "Pop";
            case 6:
                return "Indie";
            case 7:
                return "Rock";
            default:
                return "Pop";
        }
    }

    public static String chooseOption(int number) {
        switch (number) {
            case 1:
                return "Reservations";
            case 2:
                return "Wi-Fi";
            case 3:
                return "Delivery";
            case 4:
                return "Accept credit cards";
            default:
                return "Reservations";
        }
    }

    public static String chooseBarAndClubName(int number) {
        switch (number) {
            case 1:
                return "Bar Norfolk";
            case 2:
                return "Crystal On The Plaza";
            case 3:
                return "Fado Irish Pub";
            case 4:
                return "Corn Bar";
            case 5:
                return "Dean S. Karlan";
            case 6:
                return "Andy's Crossroads Liquor";
            case 7:
                return "Fox News Channel";
            case 8:
                return "Bouy Bar";
            case 9:
                return "Total Wine And Spirits";
            case 10:
                return "Southside Night Club";
            case 11:
                return "Dnhs";
            case 12:
                return "901 Pub";
            case 13:
                return "Chemically Imbalanced Comedy";
            case 14:
                return "Met Lounge";
            case 15:
                return "Lunkers";
            case 16:
                return "Monkey Hill Bar";
            case 17:
                return "Villa";
            case 18:
                return "Flex";
            case 19:
                return "Great Head Beer";
            case 20:
                return "Creekside Inn";
            case 21:
                return "Bailey's Lounge";
            case 22:
                return "Club Passim";
            case 23:
                return "Carlie's";
            case 24:
                return "Shakers Night Club";
            case 25:
                return "Mexicanian";

            default:
                return "89th Club";
        }
    }

    public static String chooseRestuarantAndCafeName(int number) {
        switch (number) {
            case 1:
                return "Arizona Kitchen";
            case 2:
                return "Nicola's";
            case 3:
                return "Knead Cafe";
            case 4:
                return "Barbecue Pit Rib House & Grill";
            case 5:
                return "Longhorn Cafe";
            case 6:
                return "Froggy's French Cafe";
            case 7:
                return "Patrizio Plano Restaurant";
            case 8:
                return "Pace's Steak House";
            case 9:
                return "Le Viet Restaurant";
            case 10:
                return "Abigail's Restaurant";
            case 11:
                return "Backyard Grill";
            case 12:
                return "Pete Miller's Seafood & Steak";
            case 13:
                return "Cathay Temple";
            case 14:
                return "Teatone Chinese Restaurant";
            case 15:
                return "Carlos Brazilian Intl Cuisine";
            case 16:
                return "Lattecafeky.com";
            case 17:
                return "MT Athos Restaurant & Cafe";
            case 18:
                return "Hana Yori Of Japan";
            case 19:
                return "Amore Victoria Ristorante";
            case 20:
                return "Sage French Cafe";
            case 21:
                return "Sabatino's Italian Restaurant";
            case 22:
                return "Miyabi Kyoto Japanese Steakhse";
            case 23:
                return "Rexroat's Distinctive Dining";
            case 24:
                return "Bacchus-Bartolotta Restaurant";
            case 25:
                return "George's Fine Steaks & Spirits";

            default:
                return "Village Tavern Restaurant";
        }
    }

    public static String chooseFastFoodAndCoffeeHouseName(int number) {
        switch (number) {
            case 1:
                return "Capri Pizza";
            case 2:
                return "Deli & Marshall";
            case 3:
                return "Paganos Pizzeria";
            case 4:
                return "Lighthouse Bistro";
            case 5:
                return "Two Chefs Enterprises Llc";
            case 6:
                return "Gyro & Kabob Express";
            case 7:
                return "Brook Street Bbq";
            case 8:
                return "Classic Cup";
            case 9:
                return "Paradise Ice Cream";
            case 10:
                return "Karl Long Burgger";
            case 11:
                return "Deuels Diner";
            case 12:
                return "Creative Croissants";
            case 13:
                return "Istanbul Cebab";
            case 14:
                return "Ricardo's Burgger";
            case 15:
                return "Capuccino ";
            case 16:
                return "Four B's Restaurant";
            case 17:
                return "Twig's Pizza";
            case 18:
                return "Rocket Star Coffee";
            case 19:
                return "Sirius Coffee Connection";
            case 20:
                return "Cafe Bien";
            case 21:
                return "Road House Bbq Llc";
            case 22:
                return "Daily Grind West Llc";
            case 23:
                return "Joey Demaio's Taco";
            case 24:
                return "Loui Pizza City";
            case 25:
                return "Bourbon St Pizzabar & Grill";

            default:
                return "MasonLin Chinese Restaurant";
        }
    }

    public static String chooseSchedule(int number) {
        switch (number) {
            case 1:
                return "8-21";
            case 2:
                return "11-24";
            case 3:
                return "10-23";
            case 4:
                return "7-19";
            case 5:
                return "6-20";
            case 6:
                return "9-22";
            case 7:
                return "12-2";
            case 8:
                return "18-8";
            case 9:
                return "0-24";
            default:
                return "9-22";
        }
    }

    public static Schedule createSchedule(String scheduleName) {

        String[] times = scheduleName.split("-");

        Integer hoursFrom = Integer.valueOf(times[0]);
        Integer minutesFrom = 0;
        if (hoursFrom > 23) {
            hoursFrom = 0;
        }
        Integer hoursTo = Integer.valueOf(times[1]);
        Integer minutesTo = 0;
        if (hoursTo > 23) {
            hoursTo = 23;
            minutesTo = 59;
        }

        boolean toDaysPeriod = false;
        List<DayOpenTime> openTimes = new ArrayList<>();
        DayOfWeek[] dayOfWeeks = DayOfWeek.values();

        if(hoursFrom > hoursTo) {
            LocalTime fromTimeFP = LocalTime.of(0, 0);
            LocalTime toTimeFP = LocalTime.of(hoursTo, minutesTo);

            LocalTime fromTimeSP = LocalTime.of(hoursFrom, minutesFrom);
            LocalTime toTimeSP = LocalTime.of(23, 59);

            for (DayOfWeek currentDay : dayOfWeeks) {
                openTimes.add(DayOpenTime.of()
                        .day(currentDay)
                        .from(fromTimeFP)
                        .to(toTimeFP)
                        .create());

                openTimes.add(DayOpenTime.of()
                        .day(currentDay)
                        .from(fromTimeSP)
                        .to(toTimeSP)
                        .create());
            }

        }else {
            try {
                LocalTime fromTime = LocalTime.of(hoursFrom, minutesFrom);
                LocalTime toTime = LocalTime.of(hoursTo, minutesTo);

                for (DayOfWeek currentDay : dayOfWeeks) {
                    openTimes.add(DayOpenTime.of()
                            .day(currentDay)
                            .from(fromTime)
                            .to(toTime)
                            .create());
                }

            } catch (Exception e) {
                log.error("Failed create schedule with name " + scheduleName);
                log.error(e.getMessage());
            }
        }

        return Schedule.of()
                .name(scheduleName)
                .openTimes(openTimes)
                .create();
    }

    public static List<Place> getPlacesDetails(List<Place> places) {

        for (Place place : places) {
            place.setPlaceDetails(getAtmospheres());

        }
        return null;
    }
}

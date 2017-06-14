package com.eat.utils;

import com.eat.models.b2b.PlaceDetail;
import com.eat.models.b2b.enums.PlaceDetailType;
import com.eat.models.common.Tag;
import com.eat.models.common.enums.TagType;
import lombok.val;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TagsHelper {

    public static List<Tag> getDietsAndAllergiesTags() {
        val tags = new ArrayList<Tag>();
        tags.addAll(getDietTags());
        tags.addAll(getAllergiesTags());
        return tags;
    }

    public static List<Tag> getAllPlaceDetailsTags() {
        List<Tag> tags = new ArrayList<>();
        tags.addAll(getCuisineTags());
        tags.addAll(getAtmosphereTags());
        tags.addAll(getFeaturesTags());
        tags.addAll(getMusicTags());
        tags.addAll(getInteriorTags());
        tags.addAll(getPlaceTypeTags());
        tags.addAll(getPlaceOptionTags());
        return tags;
    }

    public static List<Tag> getDietTags() {
        val tags = new ArrayList<Tag>();
        getDietsNames().forEach(dietName -> tags.add(Tag.of()
                .name(dietName)
                .type(TagType.DIET)
                .create()));
        return tags;
    }

    public static List<Tag> getAllergiesTags() {
        val tags = new ArrayList<Tag>();
        getAllergyNames().forEach(allergyName -> tags.add(Tag.of()
                .name(allergyName)
                .type(TagType.ALLERGY)
                .create()));
        return tags;
    }

    public static List<Tag> getCuisineTags() {
        List<String> tagNames = getCuisineNames();
        List<Tag> tags = new ArrayList<>();
        for (String tagName : tagNames) {
            tags.add(Tag.of()
                    .name(tagName)
                    .type(TagType.CUISINE)
                    .create());
        }

        return tags;
    }

    public static List<Tag> getAtmosphereTags() {
        List<String> tagNames = getAtmosphereNames();
        List<Tag> tags = new ArrayList<>();
        for (String tagName : tagNames) {
            tags.add(Tag.of()
                    .name(tagName)
                    .type(TagType.ATMOSPHERE)
                    .create());
        }

        return tags;
    }

    public static List<Tag> getInteriorTags() {
        List<String> tagNames = getInteriorNames();
        List<Tag> tags = new ArrayList<>();
        for (String tagName : tagNames) {
            tags.add(Tag.of()
                    .name(tagName)
                    .type(TagType.INTERIOR)
                    .create());
        }

        return tags;
    }

    public static List<Tag> getMusicTags() {
        List<String> tagNames = getMusicNames();
        List<Tag> tags = new ArrayList<>();
        for (String tagName : tagNames) {
            tags.add(Tag.of()
                    .name(tagName)
                    .type(TagType.MUSIC)
                    .create());
        }
        return tags;
    }

    public static List<Tag> getFeaturesTags() {
        List<String> tagNames = getFeaturesNames();
        List<Tag> tags = new ArrayList<>();
        for (String tagName : tagNames) {
            tags.add(Tag.of()
                    .name(tagName)
                    .type(TagType.FEATURE)
                    .create());
        }
        return tags;
    }

    public static List<Tag> getPlaceOptionTags() {
        List<String> tagNames = getOptionsNames();
        List<Tag> tags = new ArrayList<>();
        for (String tagName : tagNames) {
            tags.add(Tag.of()
                    .name(tagName)
                    .type(TagType.OPTION)
                    .create());
        }
        return tags;
    }

    public static List<PlaceDetail> filterPlaceDetailsByTagType(List<PlaceDetail> details, PlaceDetailType type) {
        return details.parallelStream()
                .filter(detail -> detail.getPlaceDetailType().equals(type))
                .collect(Collectors.toList());
    }

    public static List<PlaceDetail> getPlaceDetails() {
        val placeDetails = new ArrayList<PlaceDetail>();

        getPlaceTypeNames().forEach(tagName -> placeDetails.add(PlaceDetail.of()
                .name(tagName)
                .placeDetailType(PlaceDetailType.PLACE_TYPE)
                .create()));

        getFeaturesNames().forEach(featureName -> placeDetails.add(PlaceDetail.of()
                .name(featureName)
                .placeDetailType(PlaceDetailType.FEATURE)
                .create()));

        getAtmosphereNames().forEach(atmosphereName -> placeDetails.add(PlaceDetail.of()
                .name(atmosphereName)
                .placeDetailType(PlaceDetailType.ATMOSPHERE)
                .create()));

        getCuisineNames().forEach(cuisineName -> placeDetails.add(PlaceDetail.of()
                .name(cuisineName)
                .placeDetailType(PlaceDetailType.CUISINE)
                .create()));

        getInteriorNames().forEach(interiorName -> placeDetails.add(PlaceDetail.of()
                .name(interiorName)
                .placeDetailType(PlaceDetailType.INTERIOR)
                .create()));

        getMusicNames().forEach(musicName -> placeDetails.add(PlaceDetail.of()
                .name(musicName)
                .placeDetailType(PlaceDetailType.MUSIC)
                .create()));

        getOptionsNames().forEach(optionName -> placeDetails.add(PlaceDetail.of()
                .name(optionName)
                .placeDetailType(PlaceDetailType.OPTION)
                .create()));

        return placeDetails;
    }

    public static List<Tag> getPlaceTypeTags() {
        val placeTypeTags = new ArrayList<Tag>();
        getPlaceTypeNames().forEach(tagName -> placeTypeTags.add(Tag.of()
                .name(tagName)
                .type(TagType.PLACE_TYPE)
                .create()));
        return placeTypeTags;
    }


    public static List<String> getMiddleEasternRestaurantTags() {
        return Arrays.asList("meat", "fish", "coffe", "hummus", "falafel", "dolma", "kebab");
    }

    public static List<String> getGermanRestaurantTags() {
        return Arrays.asList("pork", "beef", "duck", "goose", "hare", "sausage", "beer", "goulash", "radler", "potato salad", "stollen");
    }

    public static List<String> getJapaneseRestaurantTags() {
        return Arrays.asList("rols", "sushi", "tofu", "miso soup", "japanese chiken", "japanese noodle", "japanese seafood");
    }

    public static List<String> getChineeseRestaurantTags() {
        return Arrays.asList("jiaozuo", "chinese chiken", "chinese duck", "pekin duck", "chinese pork", "chinese beef", "chinese noodle");
    }

    public static List<String> getThaiRestaurantTags() {
        return Arrays.asList("thai soup", "thai chiken", "tom soups", "thai seafood", "thai curry", "hot", "thai noodle");
    }

    public static List<String> getKoreanRestaurantTags() {
        return Arrays.asList("kimchi", "korean-noodle", "crab sticks", "korean-beef", "korean-seafood", "funchoza");
    }

    public static List<String> getIndianRestaurantTags() {
        return Arrays.asList("curry", "curry-chicken", "hot", "curry-vegetables", "mulligatawny", "masurdal", "chan masala", "indian mutton", "pilaf");
    }

    public static List<String> getAsianRestaurantTags() {
        return Arrays.asList("spicy", "fish", "rice", "shrimp", "calamaro", "noodle");
    }

    public static List<String> getRussianRestaurantTags() {
        return Arrays.asList("dumplings", "cabbage soup", "pelmeni", "fish in aspic", "meat in aspic", "fish-soup", "pork", "beef");
    }

    public static List<String> getVegetarianRestaurantTags() {
        return Arrays.asList("vegetables-noodle", "vegetables soup", "vegetables ragout", "eggplants", "mushrooms", "fresh vegetables", "soy");
    }

    public static List<String> getSteakhouseTags() {
        return Arrays.asList("steak", "salads", "pork-steak", "beef-steak", "veal-steak", "pork", "beef", "veal", "meat");
    }

    public static List<String> getPubTags() {
        return Arrays.asList("beer", "ale", "porter", "beer snacks");
    }

    public static List<String> getPizzeriaTags() {
        return Arrays.asList("pizza", "beer");
    }

    public static List<String> getFastFoodTags() {
        return Arrays.asList("hamburger", "cheeseburger", "french fries", "cola", "chicken wings");
    }

    public static List<String> getFrenchRestaurantTags() {
        return Arrays.asList("onion soup", "oysters", "french wine", "cream-soup", "french cheeses", "foie gras", "french veal", "croissant");
    }

    public static List<String> getItalianRestaurantTags() {
        return Arrays.asList("pasta", "spagetti", "pizza", "risotto", "bolognese", "lasagna", "cotoletta", "italy wine");
    }

    public static List<String> getAustralianRestaurantTags() {
        return Arrays.asList("lobster", "prawn", "tuna", "salmon", "abalone", "balmain bug", "seafood", "fish");
    }

    public static List<String> getFalafelTags() {
        return Arrays.asList("falafel");
    }

    public static List<String> getDonerTags() {
        return Arrays.asList("doner", "kebab");
    }

    public static List<String> getCafeTags() {
        return Arrays.asList("hot drinks", "coffee", "tea", "capuccino", "espresso", "dessert", "latte", "frappuccino", "ice tea");
    }

    public static List<String> getMexicanRestaurantTags() {
        return Arrays.asList("taco", "burritos", "mexican rice", "ceviche", "camote", "calabaza", "chilaquiles", "chimichangas");
    }

    public static List<String> getTurkishRestaurantTags() {
        return Arrays.asList("turkish coffee", "sucuk", "doner", "kofte", "kokorec", "menemen", "kıymalı", "hookah", "baklava");
    }

    public static List<String> getGreekRestaurantTags() {
        return Arrays.asList("eliopsomo", "tiganita", "greek salad", "spanakopita", "kolokythoanthoi", "tuna salad", "lakerda", "skordalia");
    }

    public static List<String> getBakeryTags() {
        return Arrays.asList("apple pie", "croissant", "bun", "cupcake", "muffin", "cake", "stollen");
    }

    public static List<String> getBarTags() {
        return Arrays.asList("shot", "whisky", "vodka", "gin", "beer", "wine", "cocktail", "juice",
                "smoozy", "hookah", "shisha", "coffee", "latte", "frappuccino", "ice tea");
    }

    public static List<String> getBeerGardenTags() {
        return Arrays.asList("beer", "appetizers", "meat", "steak", "plates", "potato salad", "sandwich");
    }

    public static List<String> getBistroTags() {
        return Arrays.asList("soup", "pasta", "burger", "sandwich", "salad", "french fries", "juice", "beer", "water");
    }

    public static List<String> getBreweryTags() {
        return Arrays.asList("snacks", "burger", "sandwich", "steak", "beer");
    }

    public static List<String> getRestaurantTags() {
        return Arrays.asList("salad", "fish", "meat", "cocktail", "steak", "juice", "whisky", "vodka",
                "wine", "coffee", "latte", "frappuccino", "ice tea");
    }

    public static List<String> getLoungeTags() {
        return Arrays.asList("shot", "hookah", "shisha", "cocktail", "coffee", "latte", "ice tea", "beer", "dessert");
    }

    public static List<String> getGastropubTags() {
        return Arrays.asList("fish", "meat", "cocktail", "steak", "juice", "whisky", "vodka", "wine", "beer", "burger");
    }

    public static List<String> getDinnerTags() {
        return Arrays.asList("soup", "pasta", "burger", "sandwich", "salad", "french fries", "juice");
    }

    public static List<String> getCuisineNames() {
        return Arrays.asList("Vegan/ vegetarian", "Chinese", "Fusion", "Japanese", "Thai", "Russian", "German",
                "Turkish", "French", "Spanish", "British", "European", "Mexican", "American", "Indian", "Arab", "Mediterranean");
    }

    public static List<String> getInteriorNames() {
        return Arrays.asList("Modern", "Contemporary", "Minimalist", "Industrial", "Mid-century modern", "Traditional",
                "Transitional", "Country", "Asian", "Rustic", "Shabby chic");
    }

    public static List<String> getFeaturesNames() {
        return Arrays.asList("Outdoor seating", "Landmarks view", "Take away", "Business lunch", "Local business",
                "Kids’ room", "Hotel location", "Good for tourists", "Good for chilling out", "Good for business meetings",
                "Good for quick meals", "Good for families with kids", "Good for large groups", "Good for special occasions");
    }

    public static List<String> getAtmosphereNames() {
        return Arrays.asList("Casual", "Warm", "Enlightened", "Quiet", "Upbeat", "Nostalgic", "Crowded", "Luxury",
                "Lounge", "Family-friendly", "Party-friendly");
    }

    public static List<String> getMusicNames() {
        return Arrays.asList("Radio", "Dj", "Electronic", "Classic", "Pop", "Indie");
    }

    public static List<String> getDietsNames() {
        return Arrays.asList("Vegan", "Vegetarian", "Low-calorie");
    }

    public static List<String> getAllergyNames() {
        return Arrays.asList("Shellfish", "Milk", "Wheat");
    }

    public static List<String> getPlaceTypeNames() {
        return Arrays.asList("Restaurant", "Bakery", "Beer Garden", "Bistro", "Brewery", "Café", "Fastfood",
                "Gastropub", "Sandwich Bar", "Pizza Place", "Pub", "Sushi Restaurant", "Steakhouse", "Coffee Shop",
                "Burger Joint", "Cocktail Bar", "Breakfast Spot", "Donut Shop", "Ice Cream Shop", "Salad Bar");
    }

    public static List<String> getOptionsNames() {
        return Arrays.asList("Reservations", "WiFi", "Delivery", "Accepted credit cards",
                "Accepted EC cards");
    }

}
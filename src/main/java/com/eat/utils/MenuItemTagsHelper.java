package com.eat.utils;

import com.eat.controllers.exceptions.DataNotFoundException;
import com.eat.models.b2b.*;
import com.eat.models.common.Tag;
import com.eat.models.common.enums.TagType;
import lombok.val;
import org.apache.commons.lang3.RandomUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MenuItemTagsHelper {

    public static List<Place> setMenu(List<Place> places, List<Tag> tags, List<PlaceDetail> details) {

        List<Tag> cuisinesTags = B2CTestHelper.filterTagsByTagType(tags, TagType.CUISINE);
        List<Tag> atmosphereTags = B2CTestHelper.filterTagsByTagType(tags, TagType.ATMOSPHERE);
        List<Tag> featureTags = B2CTestHelper.filterTagsByTagType(tags, TagType.FEATURE);
        List<Tag> interiorTags = B2CTestHelper.filterTagsByTagType(tags, TagType.INTERIOR);
        List<Tag> itemTags = B2CTestHelper.filterTagsByTagType(tags, TagType.MENU_ITEM);
        List<Tag> allergyTags = B2CTestHelper.filterTagsByTagType(tags, TagType.ALLERGY);
        List<Tag> dietTags = B2CTestHelper.filterTagsByTagType(tags, TagType.DIET);
        List<Tag> placeTypeTags = B2CTestHelper.filterTagsByTagType(tags, TagType.PLACE_TYPE);
        List<Tag> mealTypes = B2CTestHelper.filterTagsByTagType(tags, TagType.MENU_ITEM_CATEGORY);

        /*List<PlaceDetail> cuisineDetails = TagsHelper.filterPlaceDetailsByTagType(details, PlaceDetailType.CUISINE);
        List<PlaceDetail> atmosphereDetails = TagsHelper.filterPlaceDetailsByTagType(details, PlaceDetailType.ATMOSPHERE);
        List<PlaceDetail> featureDetails = TagsHelper.filterPlaceDetailsByTagType(details, PlaceDetailType.FEATURE);
        List<PlaceDetail> interiorDetails = TagsHelper.filterPlaceDetailsByTagType(details, PlaceDetailType.INTERIOR);
        List<PlaceDetail> placeTypeDetails = TagsHelper.filterPlaceDetailsByTagType(details, PlaceDetailType.PLACE_TYPE);*/

        places.forEach(place -> {
            val menus = new HashSet<Menu>();
            Menu menu = getMenu(itemTags, mealTypes, dietTags, allergyTags, place);
            menus.add(menu);
            place.setMenus(menus);
            menu.getMenuItems()
                    .forEach(menuItem -> menuItem.getTags()
                            .forEach(tag -> place.getTags().add(tag)));

            for (int i = 0; i < RandomUtils.nextInt(1, 3); i++) {
                Tag cuisineTag = cuisinesTags.get(RandomUtils.nextInt(0, cuisinesTags.size() - 1));
                place.getTags().add(cuisineTag);
                place.getPlaceDetails().add(findPlaceDetailByTag(details, cuisineTag));
            }

            for (int i = 0; i < RandomUtils.nextInt(1, 3); i++) {
                Tag atmoTag = atmosphereTags.get(RandomUtils.nextInt(0, atmosphereTags.size() - 1));
                place.getTags().add(atmoTag);
                place.getPlaceDetails().add(findPlaceDetailByTag(details, atmoTag));
            }

            for (int i = 0; i < RandomUtils.nextInt(1, 3); i++) {
                Tag featureTag = featureTags.get(RandomUtils.nextInt(0, featureTags.size() - 1));
                place.getTags().add(featureTag);
                place.getPlaceDetails().add(findPlaceDetailByTag(details, featureTag));
            }

            for (int i = 0; i < RandomUtils.nextInt(1, 3); i++) {
                Tag interiorTag = interiorTags.get(RandomUtils.nextInt(0, interiorTags.size() - 1));
                place.getTags().add(interiorTag);
                place.getPlaceDetails().add(findPlaceDetailByTag(details, interiorTag));
            }

            for (int i = 0; i < RandomUtils.nextInt(1, 3); i++) {
                Tag placeTypeTag = placeTypeTags.get(RandomUtils.nextInt(0, placeTypeTags.size() - 1));
                place.getTags().add(placeTypeTag);
                place.getPlaceDetails().add(findPlaceDetailByTag(details, placeTypeTag));
            }

        });
        return places;
    }

    public static PlaceDetail findPlaceDetailByTag(List<PlaceDetail> details, Tag tag) {
        return details.parallelStream().
                filter(detail -> detail.getName().equals(tag.getName()))
                .findFirst()
                .orElseThrow(DataNotFoundException::new);
    }

    public static Tag getMealType(String name, List<Tag> mealTypes) {
        return mealTypes.stream()
                .filter(mealType -> mealType.getName().equals(name))
                .findFirst()
                .get();
    }

    public static Menu getMenu(List<Tag> itemTags, List<Tag> mealTypes, List<Tag> dietTags,
                               List<Tag> allergyTags, Place place) {
        Menu menu = Menu.of()
                .name("test")
                .place(place)
                .menuItems(new ArrayList<>())
                .create();

        menu.getMenuItems().addAll(getMenuItems(itemTags, menu, dietTags, allergyTags));

        return menu;
    }

    public static List<MenuItem> getMenuItems(List<Tag> itemTags, Menu menu, List<Tag> dietTags,
                                              List<Tag> allergyTags) {
        List<MenuItem> menuItems = new ArrayList<>();
        int size = RandomUtils.nextInt(1, 20);

        for (int i = 0; i < size; i++) {
            int id = RandomUtils.nextInt(1, itemTags.size());
            Tag tag = itemTags.get(id);
            if (tag.getParent() == null) {
                continue;
            }
            MenuItem menuItem = MenuItem.of()
                    .name(getName(tag))
                    .menu(menu)
                    .tag(tag.getParent().getParent())
                    .tag(tag.getParent())
                    .tag(tag)
                    .create();

            menuItem.setTags(new HashSet<>(menuItem.getTags()));

            int chance = RandomUtils.nextInt(1, 11);
            if (chance % 2 == 0) {
                for (int j = 0; j < RandomUtils.nextInt(1, 3); j++) {
                    menuItem.getTags().add(dietTags.get(RandomUtils.nextInt(0, dietTags.size() - 1)));
                }
            }

            if (chance % 3 == 0) {
                for (int j = 0; j < RandomUtils.nextInt(1, 3); j++) {
                    menuItem.getTags().add(allergyTags.get(RandomUtils.nextInt(0, allergyTags.size() - 1)));
                }
            }

            menuItems.add(menuItem);
        }

        return menuItems;
    }

    public static String getName(Tag parent) {

        List<String> colors = getColors();
        String color = colors.get(RandomUtils.nextInt(0, colors.size()));
        if (parent.getParent() != null) {
            switch (parent.getParent().getName()) {
                case "Pizza":
                case "Steak":
                case "Lasagne":
                case "Ravioli":
                case "Burger":
                case "Tea":
                case "Cognac":
                case "Juice":
                case "Lemonade":
                case "Water":
                case "Smoozy":
                case "Vodka":
                case "Rum":
                case "Gin":
                case "Liqueur":
                case "Tequila":
                case "Wine":
                case "Cider":
                case "Coctail":
                case "Pie":
                case "Marmalade":
                case "Ice cream":
                case "Pudding":
                case "Sweet soup":
                case "Sauce":
                    return color + " " + parent.getName() + " " + parent.getParent().getName();
                default:
                    return color + " " + parent.getName();
            }
        }

        return color + " " + "Blank";
    }

    /*public static List<MealType> getMealTypes() {
        List<MealType> mealTypes = new ArrayList<>();
        mealTypes.add(MealType.of().name("Pizza").create());
        mealTypes.add(MealType.of().name("Steak").create());
        mealTypes.add(MealType.of().name("Pasta").create());
        mealTypes.add(MealType.of().name("Lasagne").create());
        mealTypes.add(MealType.of().name("Ravioli").create());
        mealTypes.add(MealType.of().name("Meat").create());
        mealTypes.add(MealType.of().name("Fish").create());
        mealTypes.add(MealType.of().name("Burger").create());
        mealTypes.add(MealType.of().name("Soup").create());
        mealTypes.add(MealType.of().name("Sushi").create());
        mealTypes.add(MealType.of().name("Tea").create());
        mealTypes.add(MealType.of().name("Cognac").create());
        mealTypes.add(MealType.of().name("Juice").create());
        mealTypes.add(MealType.of().name("Lemonade").create());
        mealTypes.add(MealType.of().name("Soda").create());
        mealTypes.add(MealType.of().name("Water").create());
        mealTypes.add(MealType.of().name("Smoozy").create());
        mealTypes.add(MealType.of().name("Whisky").create());
        mealTypes.add(MealType.of().name("Vodka").create());
        mealTypes.add(MealType.of().name("Rum").create());
        mealTypes.add(MealType.of().name("Gin").create());
        mealTypes.add(MealType.of().name("Coffee").create());
        mealTypes.add(MealType.of().name("Liqueur").create());
        mealTypes.add(MealType.of().name("Tequila").create());
        mealTypes.add(MealType.of().name("Wine").create());
        mealTypes.add(MealType.of().name("Beer").create());
        mealTypes.add(MealType.of().name("Cider").create());
        mealTypes.add(MealType.of().name("Coctail").create());
        mealTypes.add(MealType.of().name("Energetic drinks").create());
        mealTypes.add(MealType.of().name("Cookies").create());
        mealTypes.add(MealType.of().name("Cake").create());
        mealTypes.add(MealType.of().name("Pie").create());
        mealTypes.add(MealType.of().name("Marmalade").create());
        mealTypes.add(MealType.of().name("Pastry").create());
        mealTypes.add(MealType.of().name("Ice cream").create());
        mealTypes.add(MealType.of().name("Fruit salad").create());
        mealTypes.add(MealType.of().name("Pudding").create());
        mealTypes.add(MealType.of().name("Sweet soup").create());
        mealTypes.add(MealType.of().name("Sandwich").create());
        mealTypes.add(MealType.of().name("Appetizer").create());
        mealTypes.add(MealType.of().name("Sauce").create());
        return mealTypes;
    }*/

    public static List<Tag> getParentTags() {
        List<Tag> parentTags = new ArrayList<>();
        parentTags.add(Tag.of()
                .name("Main Dishes")
                .type(TagType.MENU_ITEM_GROUP)
                .create());
        parentTags.add(Tag.of()
                .name("Drinks")
                .type(TagType.MENU_ITEM_GROUP)
                .create());
        parentTags.add(Tag.of()
                .name("Deserts")
                .type(TagType.MENU_ITEM_GROUP)
                .create());
        parentTags.add(Tag.of()
                .name("Snacks")
                .type(TagType.MENU_ITEM_GROUP)
                .create());
        return parentTags;
    }

    public static List<Tag> setChildTagsToParentTags(List<Tag> parentTags) {
        List<Tag> childTags = new ArrayList<>();
        parentTags.forEach(tag -> {
            switch (tag.getName()) {
                case "Main Dishes":
                    childTags.addAll(getMainDishesChildTags(tag));
                    break;
                case "Drinks":
                    childTags.addAll(getDrinksChildTags(tag));
                    break;
                case "Deserts":
                    childTags.addAll(getDesertsChildTags(tag));
                    break;
                case "Snacks":
                    childTags.addAll(getSnacksChildTags(tag));
                    break;
            }
        });
        return childTags;
    }

    public static List<Tag> setGrandChildTagsToParentTags(List<Tag> parentTags) {
        List<Tag> childTags = new ArrayList<>();
        parentTags.forEach(parent -> {
            switch (parent.getName()) {
                case "Pizza":
                    childTags.addAll(getPizzaTags(parent));
                    break;
                case "Steak":
                    childTags.addAll(getSteakTags(parent));
                    break;
                case "Pasta":
                    childTags.addAll(getPastaTags(parent));
                    break;
                case "Lasagne":
                    childTags.addAll(getLasagneTags(parent));
                    break;
                case "Ravioli":
                    childTags.addAll(getRavioliTags(parent));
                    break;
                case "Meat":
                    childTags.addAll(getMeatTags(parent));
                    break;
                case "Fish":
                    childTags.addAll(getFishTags(parent));
                    break;
                case "Burger":
                    childTags.addAll(getBurgerTags(parent));
                    break;
                case "Soup":
                    childTags.addAll(getSoupTags(parent));
                    break;
                case "Sushi":
                    childTags.addAll(getSushiTags(parent));
                    break;
                case "Tea":
                    childTags.addAll(getTeaTags(parent));
                    break;
                case "Cognac":
                    childTags.addAll(getCognacTags(parent));
                    break;
                case "Juice":
                    childTags.addAll(getJuiceTags(parent));
                    break;
                case "Lemonade":
                    childTags.addAll(getLemonadeTags(parent));
                    break;
                case "Soda":
                    childTags.addAll(getSodaTags(parent));
                    break;
                case "Water":
                    childTags.addAll(getWaterTags(parent));
                    break;
                case "Smoozy":
                    childTags.addAll(getSmoozyTags(parent));
                    break;
                case "Whisky":
                    childTags.addAll(getWhiskyTags(parent));
                    break;
                case "Vodka":
                    childTags.addAll(getVodkaTags(parent));
                    break;
                case "Rum":
                    childTags.addAll(getRumTags(parent));
                    break;
                case "Gin":
                    childTags.addAll(getGinTags(parent));
                    break;
                case "Coffee":
                    childTags.addAll(getCoffeeTags(parent));
                    break;
                case "Liqueur":
                    childTags.addAll(getLiqueurTags(parent));
                    break;
                case "Tequila":
                    childTags.addAll(getTequilaTags(parent));
                    break;
                case "Wine":
                    childTags.addAll(getWineTags(parent));
                    break;
                case "Beer":
                    childTags.addAll(getBeerTags(parent));
                    break;
                case "Cider":
                    childTags.addAll(getCiderTags(parent));
                    break;
                case "Coctail":
                    childTags.addAll(getCoctailTags(parent));
                    break;
                case "Energetic drinks":
                    childTags.addAll(getEnergeticTags(parent));
                    break;
                case "Cookies":
                    childTags.addAll(getCookiesTags(parent));
                    break;
                case "Cake":
                    childTags.addAll(getCakeTags(parent));
                    break;
                case "Pie":
                    childTags.addAll(getPieTags(parent));
                    break;
                case "Marmalade":
                    childTags.addAll(getMarmaladeTags(parent));
                    break;
                case "Pastry":
                    childTags.addAll(getPastryTags(parent));
                    break;
                case "Ice cream":
                    childTags.addAll(getIceCreamTags(parent));
                    break;
                case "Fruit salad":
                    childTags.addAll(getFruitSaladTags(parent));
                    break;
                case "Pudding":
                    childTags.addAll(getPuddingTags(parent));
                    break;
                case "Sweet soup":
                    childTags.addAll(getSweetSoupTags(parent));
                    break;
                case "Sandwich":
                    childTags.addAll(getSandwichTags(parent));
                    break;
                case "Appetizer":
                    childTags.addAll(getAppetizerTags(parent));
                    break;
                case "Sauce":
                    childTags.addAll(getSauceTags(parent));
                    break;
                case "Salad":
                    childTags.addAll(getSaladTags(parent));
                    break;
                case "Plate":
                    childTags.addAll(getPlateTags(parent));
                    break;
            }
        });
        return childTags;
    }

    public static List<Tag> getMainDishesChildTags(Tag parent) {
        List<Tag> childTags = new ArrayList<>();
        childTags.add(Tag.of()
                .name("BBQ")
                .create());
        childTags.add(Tag.of()
                .name("Bean Soup")
                .create());
        childTags.add(Tag.of()
                .name("Beef Burger")
                .create());
        childTags.add(Tag.of()
                .name("Beef Soup")
                .create());
        childTags.add(Tag.of()
                .name("Borsch")
                .create());
        childTags.add(Tag.of()
                .name("Cereal")
                .create());
        childTags.add(Tag.of()
                .name("Cheese Burger")
                .create());
        childTags.add(Tag.of()
                .name("Cheese Soup")
                .create());
        childTags.add(Tag.of()
                .name("Chicken Burger")
                .create());
        childTags.add(Tag.of()
                .name("Chicken Soup")
                .create());
        childTags.add(Tag.of()
                .name("Cream Soup")
                .create());
        childTags.add(Tag.of()
                .name("Curry Soup")
                .create());
        childTags.add(Tag.of()
                .name("Eggs")
                .create());
        childTags.add(Tag.of()
                .name("Fish")
                .create());
        childTags.add(Tag.of()
                .name("Fish Burger")
                .create());
        childTags.add(Tag.of()
                .name("Goulash")
                .create());
        childTags.add(Tag.of()
                .name("Hot Dogs")
                .create());
        childTags.add(Tag.of()
                .name("Kebab")
                .create());
        childTags.add(Tag.of()
                .name("Lasagne")
                .create());
        childTags.add(Tag.of()
                .name("Meat")
                .create());
        childTags.add(Tag.of()
                .name("Minestrone Soup")
                .create());
        childTags.add(Tag.of()
                .name("Miso Soup")
                .create());
        childTags.add(Tag.of()
                .name("Noodles")
                .create());
        childTags.add(Tag.of()
                .name("Okroshka")
                .create());
        childTags.add(Tag.of()
                .name("Omelette")
                .create());
        childTags.add(Tag.of()
                .name("Pasta")
                .create());
        childTags.add(Tag.of()
                .name("Phat Thai")
                .create());
        childTags.add(Tag.of()
                .name("Pizza")
                .create());
        childTags.add(Tag.of()
                .name("Ramen")
                .create());
        childTags.add(Tag.of()
                .name("Ravioli")
                .create());
        childTags.add(Tag.of()
                .name("Rolls")
                .create());
        childTags.add(Tag.of()
                .name("Sausage")
                .create());
        childTags.add(Tag.of()
                .name("Steak")
                .create());
        childTags.add(Tag.of()
                .name("Stew")
                .create());
        childTags.add(Tag.of()
                .name("Sushi")
                .create());
        childTags.add(Tag.of()
                .name("Sushimi")
                .create());
        childTags.add(Tag.of()
                .name("Temaki")
                .create());
        childTags.add(Tag.of()
                .name("Toast")
                .create());
        childTags.add(Tag.of()
                .name("Tom Yum Soup")
                .create());
        childTags.add(Tag.of()
                .name("Vegetarian Burger")
                .create());
        childTags.add(Tag.of()
                .name("Vegetarian Pizza")
                .create());
        childTags.add(Tag.of()
                .name("Yogurt")
                .create());
        childTags.add(Tag.of()
                .name("Sushi")
                .create());
        childTags.add(Tag.of()
                .name("Sushimi")
                .create());
        childTags.forEach(tags -> {
            tags.setParent(parent);
            tags.setType(TagType.MENU_ITEM_CATEGORY);
        });
        return childTags;
    }

    public static List<Tag> getDrinksChildTags(Tag parent) {
        List<Tag> childTags = new ArrayList<>();
        childTags.add(Tag.of()
                .name("Alcohol")
                .create());
        childTags.add(Tag.of()
                .name("Americano")
                .create());
        childTags.add(Tag.of()
                .name("Beer")
                .create());
        childTags.add(Tag.of()
                .name("Burn")
                .create());
        childTags.add(Tag.of()
                .name("Cappuccino")
                .create());
        childTags.add(Tag.of()
                .name("Cider")
                .create());
        childTags.add(Tag.of()
                .name("Cocktail")
                .create());
        childTags.add(Tag.of()
                .name("Coffee")
                .create());
        childTags.add(Tag.of()
                .name("Espresso")
                .create());
        childTags.add(Tag.of()
                .name("Frappuccino")
                .create());
        childTags.add(Tag.of()
                .name("Gin")
                .create());
        childTags.add(Tag.of()
                .name("Juice")
                .create());
        childTags.add(Tag.of()
                .name("Latte")
                .create());
        childTags.add(Tag.of()
                .name("Lemonade")
                .create());
        childTags.add(Tag.of()
                .name("Liqueur")
                .create());
        childTags.add(Tag.of()
                .name("Milkshake")
                .create());
        childTags.add(Tag.of()
                .name("Non Stop")
                .create());
        childTags.add(Tag.of()
                .name("Red Bull")
                .create());
        childTags.add(Tag.of()
                .name("Rum")
                .create());
        childTags.add(Tag.of()
                .name("Sake")
                .create());
        childTags.add(Tag.of()
                .name("Schnapps")
                .create());
        childTags.add(Tag.of()
                .name("Smoozy")
                .create());
        childTags.add(Tag.of()
                .name("Soda")
                .create());
        childTags.add(Tag.of()
                .name("Tea")
                .create());
        childTags.add(Tag.of()
                .name("Tequila")
                .create());
        childTags.add(Tag.of()
                .name("Vodka")
                .create());
        childTags.add(Tag.of()
                .name("Water")
                .create());
        childTags.add(Tag.of()
                .name("Whiskey")
                .create());
        childTags.add(Tag.of()
                .name("Wine")
                .create());
        childTags.forEach(tags -> {
            tags.setParent(parent);
            tags.setType(TagType.MENU_ITEM_CATEGORY);
        });
        return childTags;
    }

    public static List<Tag> getDesertsChildTags(Tag parent) {
        List<Tag> childTags = new ArrayList<>();
        childTags.add(Tag.of()
                .name("Madeleine Cookies")
                .create());
        childTags.add(Tag.of()
                .name("Cannoli")
                .create());
        childTags.add(Tag.of()
                .name("Eclair")
                .create());
        childTags.add(Tag.of()
                .name("Macaron")
                .create());
        childTags.add(Tag.of()
                .name("Mille-feuille")
                .create());
        childTags.add(Tag.of()
                .name("Pancakes")
                .create());
        childTags.add(Tag.of()
                .name("Chocolate Chip Cookie")
                .create());
        childTags.add(Tag.of()
                .name("Pop-corn")
                .create());
        childTags.add(Tag.of()
                .name("Pumpkin Pie")
                .create());
        childTags.add(Tag.of()
                .name("Donut")
                .create());
        childTags.add(Tag.of()
                .name("Gingerbread")
                .create());
        childTags.add(Tag.of()
                .name("Cake")
                .create());
        childTags.add(Tag.of()
                .name("Cherry Pie")
                .create());
        childTags.add(Tag.of()
                .name("Muffin")
                .create());
        childTags.add(Tag.of()
                .name("Donut")
                .create());
        childTags.add(Tag.of()
                .name("Cheese Pie")
                .create());
        childTags.add(Tag.of()
                .name("Cinnamon Roll")
                .create());
        childTags.add(Tag.of()
                .name("Bagel")
                .create());
        childTags.add(Tag.of()
                .name("Waffle")
                .create());
        childTags.add(Tag.of()
                .name("Cheese Pie")
                .create());
        childTags.add(Tag.of()
                .name("Gelato")
                .create());
        childTags.add(Tag.of()
                .name("Ice Cream")
                .create());
        childTags.add(Tag.of()
                .name("Chocolate")
                .create());
        childTags.add(Tag.of()
                .name("Marmalade")
                .create());
        childTags.add(Tag.of()
                .name("Cupcake")
                .create());
        childTags.add(Tag.of()
                .name("Croissant")
                .create());
        childTags.add(Tag.of()
                .name("Tartalette")
                .create());
        childTags.add(Tag.of()
                .name("Apple Strudel")
                .create());
        childTags.add(Tag.of()
                .name("Oat Cookies")
                .create());
        childTags.add(Tag.of()
                .name("Cinnamon Roll")
                .create());
        childTags.add(Tag.of()
                .name("Pudding")
                .create());
        childTags.forEach(tags -> {
            tags.setParent(parent);
            tags.setType(TagType.MENU_ITEM_CATEGORY);
        });
        return childTags;
    }

    public static List<Tag> getSnacksChildTags(Tag parent) {
        List<Tag> childTags = new ArrayList<>();
        childTags.add(Tag.of()
                .name("Bruschetta")
                .create());
        childTags.add(Tag.of()
                .name("Burritos")
                .create());
        childTags.add(Tag.of()
                .name("Caesar Salad")
                .create());
        childTags.add(Tag.of()
                .name("Calamary Rings")
                .create());
        childTags.add(Tag.of()
                .name("Fruit Salad")
                .create());
        childTags.add(Tag.of()
                .name("Caprese Salad")
                .create());
        childTags.add(Tag.of()
                .name("Carpaccio")
                .create());
        childTags.add(Tag.of()
                .name("Cheese Plate")
                .create());
        childTags.add(Tag.of()
                .name("Chicken Salad")
                .create());
        childTags.add(Tag.of()
                .name("Chips")
                .create());
        childTags.add(Tag.of()
                .name("Corn")
                .create());
        childTags.add(Tag.of()
                .name("Focaccia")
                .create());
        childTags.add(Tag.of()
                .name("French Fries")
                .create());
        childTags.add(Tag.of()
                .name("Fruit Plate")
                .create());
        childTags.add(Tag.of()
                .name("Garlic Bread")
                .create());
        childTags.add(Tag.of()
                .name("Greek Salad")
                .create());
        childTags.add(Tag.of()
                .name("Grilled Vegetables Salad")
                .create());
        childTags.add(Tag.of()
                .name("Grissini")
                .create());
        childTags.add(Tag.of()
                .name("Jamon Plate")
                .create());
        childTags.add(Tag.of()
                .name("Meat Plate")
                .create());
        childTags.add(Tag.of()
                .name("Nachos")
                .create());
        childTags.add(Tag.of()
                .name("Nicoise Salad")
                .create());
        childTags.add(Tag.of()
                .name("Olives")
                .create());
        childTags.add(Tag.of()
                .name("Onion Rings")
                .create());
        childTags.add(Tag.of()
                .name("Parma Ham Salad")
                .create());
        childTags.add(Tag.of()
                .name("Potato Salad")
                .create());
        childTags.add(Tag.of()
                .name("Prawns Salad")
                .create());
        childTags.add(Tag.of()
                .name("Pretzel")
                .create());
        childTags.add(Tag.of()
                .name("Sandwich")
                .create());
        childTags.add(Tag.of()
                .name("Seafood Salad")
                .create());
        childTags.add(Tag.of()
                .name("Shrimps Salad")
                .create());
        childTags.add(Tag.of()
                .name("Tuna Salad")
                .create());
        childTags.add(Tag.of()
                .name("Vegetables Plate")
                .create());
        childTags.forEach(tags -> {
            tags.setParent(parent);
            tags.setType(TagType.MENU_ITEM_CATEGORY);
        });
        return childTags;
    }

    public static List<String> getColors() {
        List<String> colors = new ArrayList<>();
        colors.add("Blue");
        colors.add("Black");
        colors.add("Purple");
        colors.add("Pink");
        colors.add("Red");
        colors.add("Orange");
        colors.add("Yellow");
        colors.add("White");
        colors.add("Green");
        colors.add("Turquoise");
        colors.add("Grey");
        colors.add("Amaranth");
        colors.add("Amber");
        colors.add("Azure");
        colors.add("Beige");
        colors.add("Coral");
        colors.add("Olive");
        colors.add("Bronze");
        colors.add("Golden");
        colors.add("Silver");
        colors.add("Camel");
        colors.add("Violet");
        return colors;
    }

    public static List<Tag> getPizzaTags(Tag parent) {
        List<Tag> names = new ArrayList<>();
        names.add(Tag.of().name("Neapolitana pizza").create());
        names.add(Tag.of().name("Meat pizza").create());
        names.add(Tag.of().name("Margaritta pizza").create());
        names.add(Tag.of().name("Zuchetta pizza").create());
        names.add(Tag.of().name("Papperoni pizza").create());
        names.add(Tag.of().name("Bavaria pizza").create());
        names.add(Tag.of().name("Basilicata pizza").create());
        names.add(Tag.of().name("Toscana pizza").create());
        names.add(Tag.of().name("Bergamo pizza").create());
        names.add(Tag.of().name("Melanzana pizza").create());
        names.add(Tag.of().name("Quattro pizza").create());
        names.add(Tag.of().name("Formaggi pizza").create());
        names.add(Tag.of().name("BBQ pizza").create());
        names.add(Tag.of().name("DiavolaDiablo pizza").create());
        names.forEach(tags -> {
            tags.setParent(parent);
            tags.setType(TagType.MENU_ITEM);
        });
        return names;
    }

    public static List<Tag> getSteakTags(Tag parent) {
        List<Tag> names = new ArrayList<>();
        names.add(Tag.of().name("Chunk-eye steak").create());
        names.add(Tag.of().name("New York steak").create());
        names.add(Tag.of().name("Rib-eye steak").create());
        names.add(Tag.of().name("T-bone steak").create());
        names.add(Tag.of().name("Top loin steak").create());
        names.forEach(tags -> {
            tags.setParent(parent);
            tags.setType(TagType.MENU_ITEM);
        });
        return names;
    }

    public static List<Tag> getPastaTags(Tag parent) {
        List<Tag> names = new ArrayList<>();
        names.add(Tag.of().name("Tagliatelle carbonara").create());
        names.add(Tag.of().name("Spaghetti bolognese").create());
        names.add(Tag.of().name("Papardelle with mushrooms").create());
        names.add(Tag.of().name("Tagliatelle with salmon").create());
        names.add(Tag.of().name("Papardelle with mussels").create());
        names.forEach(tags -> {
            tags.setParent(parent);
            tags.setType(TagType.MENU_ITEM);
        });
        return names;
    }

    public static List<Tag> getRavioliTags(Tag parent) {
        List<Tag> names = new ArrayList<>();
        names.add(Tag.of().name("Pesto ravioli").create());
        names.add(Tag.of().name("Tomato ravioli").create());
        names.add(Tag.of().name("Meat ravioli").create());
        names.forEach(tags -> {
            tags.setParent(parent);
            tags.setType(TagType.MENU_ITEM);
        });
        return names;
    }

    public static List<Tag> getLasagneTags(Tag parent) {
        List<Tag> names = new ArrayList<>();
        names.add(Tag.of().name("Bolognese lasagne").create());
        names.forEach(tags -> {
            tags.setParent(parent);
            tags.setType(TagType.MENU_ITEM);
        });
        return names;
    }

    public static List<Tag> getMeatTags(Tag parent) {
        List<Tag> names = new ArrayList<>();
        names.add(Tag.of().name("Chicken with Aubergines").create());
        names.add(Tag.of().name("Turkey breast with mashed potatoes").create());
        names.add(Tag.of().name("Pork neck in red wine").create());
        names.add(Tag.of().name("Ossobuco").create());
        names.add(Tag.of().name("Duck leg with celeriac and apples").create());
        names.forEach(tags -> {
            tags.setParent(parent);
            tags.setType(TagType.MENU_ITEM);
        });
        return names;
    }

    public static List<Tag> getFishTags(Tag parent) {
        List<Tag> names = new ArrayList<>();
        names.add(Tag.of().name("Salmon under salsa with mango").create());
        names.add(Tag.of().name("Tuna casserolle").create());
        names.add(Tag.of().name("Trout with cranberry").create());
        names.add(Tag.of().name("Pot of mussels in wine").create());
        names.add(Tag.of().name("Sea bass with potatoes").create());
        names.forEach(tags -> {
            tags.setParent(parent);
            tags.setType(TagType.MENU_ITEM);
        });
        return names;
    }

    public static List<Tag> getBurgerTags(Tag parent) {
        List<Tag> names = new ArrayList<>();
        names.add(Tag.of().name("African burger").create());
        names.add(Tag.of().name("Thai burger").create());
        names.add(Tag.of().name("Mexican burger").create());
        names.add(Tag.of().name("Vegetarian burger").create());
        names.add(Tag.of().name("Uncle Sam's burger").create());
        names.add(Tag.of().name("Diablo burger").create());
        names.forEach(tags -> {
            tags.setParent(parent);
            tags.setType(TagType.MENU_ITEM);
        });
        return names;
    }

    public static List<Tag> getSoupTags(Tag parent) {
        List<Tag> names = new ArrayList<>();
        names.add(Tag.of().name("Minestrone Soup").create());
        names.add(Tag.of().name("Borsch").create());
        names.add(Tag.of().name("Cream soup").create());
        names.add(Tag.of().name("Miso soup").create());
        names.add(Tag.of().name("Okroshka").create());
        names.add(Tag.of().name("Bean and Pea Soup").create());
        names.add(Tag.of().name("Beef Soup").create());
        names.add(Tag.of().name("Chicken Soup").create());
        names.add(Tag.of().name("Curry Soup").create());
        names.forEach(tags -> {
            tags.setParent(parent);
            tags.setType(TagType.MENU_ITEM);
        });
        return names;
    }

    public static List<Tag> getSushiTags(Tag parent) {
        List<Tag> names = new ArrayList<>();
        names.add(Tag.of().name("Nigiri sushi").create());
        names.add(Tag.of().name("Maki").create());
        names.add(Tag.of().name("Temaki").create());
        names.add(Tag.of().name("Sushimi").create());
        names.add(Tag.of().name("California Roll").create());
        names.add(Tag.of().name("Philadelphia Roll").create());
        names.add(Tag.of().name("Tuna Roll").create());
        names.add(Tag.of().name("Dragon Roll").create());
        names.forEach(tags -> {
            tags.setParent(parent);
            tags.setType(TagType.MENU_ITEM);
        });
        return names;
    }

    public static List<Tag> getTeaTags(Tag parent) {
        List<Tag> names = new ArrayList<>();
        names.add(Tag.of().name("Black tea").create());
        names.add(Tag.of().name("Green tea").create());
        names.add(Tag.of().name("Fruit tea").create());
        names.add(Tag.of().name("Herbal tea").create());
        names.forEach(tags -> {
            tags.setParent(parent);
            tags.setType(TagType.MENU_ITEM);
        });
        return names;
    }

    public static List<Tag> getCognacTags(Tag parent) {
        List<Tag> names = new ArrayList<>();
        names.add(Tag.of().name("Henessy cognac").create());
        names.add(Tag.of().name("Martell cognac").create());
        names.add(Tag.of().name("Monnet cognac").create());
        names.add(Tag.of().name("Baron cognac").create());
        names.add(Tag.of().name("Otard cognac").create());
        names.add(Tag.of().name("Camus cognac").create());
        names.add(Tag.of().name("Courvoisier cognac").create());
        names.forEach(tags -> {
            tags.setParent(parent);
            tags.setType(TagType.MENU_ITEM);
        });
        return names;
    }

    public static List<Tag> getJuiceTags(Tag parent) {
        List<Tag> names = new ArrayList<>();
        names.add(Tag.of().name("Apple juice").create());
        names.add(Tag.of().name("Orange juice").create());
        names.add(Tag.of().name("Multi-fruit juice").create());
        names.forEach(tags -> {
            tags.setParent(parent);
            tags.setType(TagType.MENU_ITEM);
        });
        return names;
    }

    public static List<Tag> getLemonadeTags(Tag parent) {
        List<Tag> names = new ArrayList<>();
        names.add(Tag.of().name("Mint and raspberry lemonade").create());
        names.add(Tag.of().name("Lemon and ginger lemonade").create());
        names.add(Tag.of().name("Orange and basil lemonade").create());
        names.forEach(tags -> {
            tags.setParent(parent);
            tags.setType(TagType.MENU_ITEM);
        });
        return names;
    }

    public static List<Tag> getSodaTags(Tag parent) {
        List<Tag> names = new ArrayList<>();
        names.add(Tag.of().name("Coca-Cola").create());
        names.add(Tag.of().name("Tonic").create());
        names.add(Tag.of().name("Jizz").create());
        names.add(Tag.of().name("Pepsi").create());
        names.add(Tag.of().name("Sprite").create());
        names.add(Tag.of().name("Schweppes").create());
        names.add(Tag.of().name("Fanta").create());
        names.forEach(tags -> {
            tags.setParent(parent);
            tags.setType(TagType.MENU_ITEM);
        });
        return names;
    }

    public static List<Tag> getWaterTags(Tag parent) {
        List<Tag> names = new ArrayList<>();
        names.add(Tag.of().name("Sparkling water").create());
        names.add(Tag.of().name("Still water").create());
        names.forEach(tags -> {
            tags.setParent(parent);
            tags.setType(TagType.MENU_ITEM);
        });
        return names;
    }

    public static List<Tag> getSmoozyTags(Tag parent) {
        List<Tag> names = new ArrayList<>();
        names.add(Tag.of().name("Strawberry with mint, banana and chocolate smoozy").create());
        names.add(Tag.of().name("Mango and pineapple smoozy").create());
        names.add(Tag.of().name("Apple, orange and vanilla ice-cream smoozy").create());
        names.add(Tag.of().name("Pear, lime and apple smoozy").create());
        names.forEach(tags -> {
            tags.setParent(parent);
            tags.setType(TagType.MENU_ITEM);
        });
        return names;
    }

    public static List<Tag> getWhiskyTags(Tag parent) {
        List<Tag> names = new ArrayList<>();
        names.add(Tag.of().name("Bourbon").create());
        names.add(Tag.of().name("Tennessee").create());
        names.add(Tag.of().name("Rye").create());
        names.add(Tag.of().name("Irish whisky").create());
        names.add(Tag.of().name("Scotch").create());
        names.add(Tag.of().name("Canadian").create());
        names.forEach(tags -> {
            tags.setParent(parent);
            tags.setType(TagType.MENU_ITEM);
        });
        return names;
    }

    public static List<Tag> getVodkaTags(Tag parent) {
        List<Tag> names = new ArrayList<>();
        names.add(Tag.of().name("Absolut 100 vodka").create());
        names.add(Tag.of().name("Absolut Blue vodka").create());
        names.add(Tag.of().name("Absolut Citron vodka").create());
        names.add(Tag.of().name("Absolut Elyx vodka").create());
        names.add(Tag.of().name("Finlandia 101 vodka").create());
        names.add(Tag.of().name("Finlandia Blackcurrant Flavoured vocka").create());
        names.add(Tag.of().name("Grey Goose vodka").create());
        names.add(Tag.of().name("Smirnoff vodka").create());
        names.add(Tag.of().name("Nemiroff vodka").create());
        names.add(Tag.of().name("Aurora vodka").create());
        names.add(Tag.of().name("Beluga Allure vodka").create());
        names.add(Tag.of().name("Beluga Gold Line vodka").create());
        names.add(Tag.of().name("Bismarck vodka").create());
        names.add(Tag.of().name("Blavod Pure vodka").create());
        names.add(Tag.of().name("Danzka Cranraz vodka").create());
        names.forEach(tags -> {
            tags.setParent(parent);
            tags.setType(TagType.MENU_ITEM);
        });
        return names;
    }

    public static List<Tag> getRumTags(Tag parent) {
        List<Tag> names = new ArrayList<>();
        names.add(Tag.of().name("Bacardi 8 YO rum").create());
        names.add(Tag.of().name("Bacardi Carta Blanca rum").create());
        names.add(Tag.of().name("Captain Morgan Spiced Gold rum").create());
        names.add(Tag.of().name("Havana Club rum").create());
        names.add(Tag.of().name("Appleton Estate rum").create());
        names.add(Tag.of().name("Angostura rum").create());
        names.forEach(tags -> {
            tags.setParent(parent);
            tags.setType(TagType.MENU_ITEM);
        });
        return names;
    }

    public static List<Tag> getGinTags(Tag parent) {
        List<Tag> names = new ArrayList<>();
        names.add(Tag.of().name("Beefeater London Dry gin").create());
        names.add(Tag.of().name("Bombay Sapphire East gin").create());
        names.add(Tag.of().name("Bombay Star of Bombay gin").create());
        names.add(Tag.of().name("Bulldog gin").create());
        names.add(Tag.of().name("Citadelle gin").create());
        names.add(Tag.of().name("Filliers Dry gin").create());
        names.add(Tag.of().name("Gordon's London Dry gin").create());
        names.add(Tag.of().name("Tanqueray's London ginrumy").create());
        names.add(Tag.of().name("The Botanist Islay gin").create());
        names.add(Tag.of().name("The Duke gin").create());
        names.forEach(tags -> {
            tags.setParent(parent);
            tags.setType(TagType.MENU_ITEM);
        });
        return names;
    }

    public static List<Tag> getCoffeeTags(Tag parent) {
        List<Tag> names = new ArrayList<>();
        names.add(Tag.of().name("Latte").create());
        names.add(Tag.of().name("Espresso").create());
        names.add(Tag.of().name("Frapuchino").create());
        names.add(Tag.of().name("Americano").create());
        names.add(Tag.of().name("Capuchino").create());
        names.forEach(tags -> {
            tags.setParent(parent);
            tags.setType(TagType.MENU_ITEM);
        });
        return names;
    }

    public static List<Tag> getLiqueurTags(Tag parent) {
        List<Tag> names = new ArrayList<>();
        names.add(Tag.of().name("Baileys liqueur").create());
        names.add(Tag.of().name("Amaretto liqueur").create());
        names.add(Tag.of().name("Bottega Limoncino liqueur").create());
        names.add(Tag.of().name("Cointreau liqueur").create());
        names.add(Tag.of().name("Kahlúa liqueur").create());
        names.add(Tag.of().name("Malibu Coconut liqueur").create());
        names.add(Tag.of().name("Mozart Gold liqueur").create());
        names.add(Tag.of().name("Sambuca Extra Molinari liqueur").create());
        names.add(Tag.of().name("Sheridan's liqueur").create());
        names.add(Tag.of().name("Irish cream liqueur").create());
        names.add(Tag.of().name("Cherry liqueur").create());
        names.add(Tag.of().name("Amarula liqueur").create());
        names.forEach(tags -> {
            tags.setParent(parent);
            tags.setType(TagType.MENU_ITEM);
        });
        return names;
    }

    public static List<Tag> getTequilaTags(Tag parent) {
        List<Tag> names = new ArrayList<>();
        names.add(Tag.of().name("Corzo Silver").create());
        names.add(Tag.of().name("Don Julio").create());
        names.add(Tag.of().name("Don Julio Silver").create());
        names.add(Tag.of().name("Don Julio Anejo").create());
        names.add(Tag.of().name("Patron").create());
        names.add(Tag.of().name("Patron XO Cafe").create());
        names.add(Tag.of().name("Patron Silver").create());
        names.add(Tag.of().name("Patron Anejo").create());
        names.add(Tag.of().name("Olmeca Blanco").create());
        names.add(Tag.of().name("Olmeca Gold").create());
        names.add(Tag.of().name("Olmeca Anejo").create());
        names.add(Tag.of().name("Olmeca Altos Plata").create());
        names.add(Tag.of().name("Olmeca Altos Reposado").create());
        names.forEach(tags -> {
            tags.setParent(parent);
            tags.setType(TagType.MENU_ITEM);
        });
        return names;
    }

    public static List<Tag> getWineTags(Tag parent) {
        List<Tag> names = new ArrayList<>();
        names.add(Tag.of().name("Caberne Sauvignon wine").create());
        names.add(Tag.of().name("Merlot wine").create());
        names.add(Tag.of().name("Riesling wine").create());
        names.add(Tag.of().name("Sauvignon Blanc wine").create());
        names.add(Tag.of().name("Gewurtztraminer wine").create());
        names.add(Tag.of().name("Vihno Verde wine").create());
        names.add(Tag.of().name("Syrah wine").create());
        names.add(Tag.of().name("Tempranillo wine").create());
        names.add(Tag.of().name("Ice wine").create());
        names.add(Tag.of().name("Pinotage wine").create());
        names.add(Tag.of().name("Chardonnay wine").create());
        names.forEach(tags -> {
            tags.setParent(parent);
            tags.setType(TagType.MENU_ITEM);
        });
        return names;
    }

    public static List<Tag> getBeerTags(Tag parent) {
        List<Tag> names = new ArrayList<>();
        names.add(Tag.of().name("Weissbier").create());
        names.add(Tag.of().name("Pilsner").create());
        names.add(Tag.of().name("Dark").create());
        names.add(Tag.of().name("Lager").create());
        names.add(Tag.of().name("Ale").create());
        names.add(Tag.of().name("Unfiltered").create());
        names.forEach(tags -> {
            tags.setParent(parent);
            tags.setType(TagType.MENU_ITEM);
        });
        return names;
    }

    public static List<Tag> getCiderTags(Tag parent) {
        List<Tag> names = new ArrayList<>();
        names.add(Tag.of().name("Apple cider").create());
        names.add(Tag.of().name("Plum cider").create());
        names.add(Tag.of().name("Cranberry cider").create());
        names.add(Tag.of().name("Pear cider").create());
        names.add(Tag.of().name("Cherry cider").create());
        names.add(Tag.of().name("Apple with elder cider").create());
        names.forEach(tags -> {
            tags.setParent(parent);
            tags.setType(TagType.MENU_ITEM);
        });
        return names;
    }

    public static List<Tag> getCoctailTags(Tag parent) {
        List<Tag> names = new ArrayList<>();
        names.add(Tag.of().name("Margarita coctail").create());
        names.add(Tag.of().name("Long Island coctail").create());
        names.add(Tag.of().name("Old fashioned coctail").create());
        names.add(Tag.of().name("Daiquiri coctail").create());
        names.add(Tag.of().name("Mai Tai coctail").create());
        names.add(Tag.of().name("Negroni coctail").create());
        names.add(Tag.of().name("Mojito coctail").create());
        names.add(Tag.of().name("Gin Martini coctail").create());
        names.add(Tag.of().name("Whiskey Sauer coctail").create());
        names.add(Tag.of().name("Pina Colada coctail").create());
        names.add(Tag.of().name("Bellini coctail").create());
        names.forEach(tags -> {
            tags.setParent(parent);
            tags.setType(TagType.MENU_ITEM);
        });
        return names;
    }

    public static List<Tag> getEnergeticTags(Tag parent) {
        List<Tag> names = new ArrayList<>();
        names.add(Tag.of().name("Burn").create());
        names.add(Tag.of().name("Red Bull").create());
        names.add(Tag.of().name("Non Stop").create());
        names.add(Tag.of().name("Monster Assault").create());
        names.forEach(tags -> {
            tags.setParent(parent);
            tags.setType(TagType.MENU_ITEM);
        });
        return names;
    }

    public static List<Tag> getCookiesTags(Tag parent) {
        List<Tag> names = new ArrayList<>();
        names.add(Tag.of().name("Chocolate chip").create());
        names.add(Tag.of().name("Madeleine").create());
        names.add(Tag.of().name("Oat cookies with cranberry").create());
        names.add(Tag.of().name("Gingerbread with icing").create());
        names.forEach(tags -> {
            tags.setParent(parent);
            tags.setType(TagType.MENU_ITEM);
        });
        return names;
    }

    public static List<Tag> getCakeTags(Tag parent) {
        List<Tag> names = new ArrayList<>();
        names.add(Tag.of().name("Carrot cake").create());
        names.add(Tag.of().name("Tiramisu").create());
        names.add(Tag.of().name("Red Velvet cake").create());
        names.add(Tag.of().name("Angel cake").create());
        names.add(Tag.of().name("Banana bread").create());
        names.add(Tag.of().name("Swarzwald").create());
        names.add(Tag.of().name("Brownie").create());
        names.add(Tag.of().name("Cheesecake").create());
        names.add(Tag.of().name("Esterhazi").create());
        names.add(Tag.of().name("Dobos").create());
        names.add(Tag.of().name("Kiev cake").create());
        names.add(Tag.of().name("Napoleon cake").create());
        names.add(Tag.of().name("Opera cake").create());
        names.add(Tag.of().name("Sacher").create());
        names.forEach(tags -> {
            tags.setParent(parent);
            tags.setType(TagType.MENU_ITEM);
        });
        return names;
    }

    public static List<Tag> getPieTags(Tag parent) {
        List<Tag> names = new ArrayList<>();
        names.add(Tag.of().name("Apple pie").create());
        names.add(Tag.of().name("Raspberry and rhubarb pie").create());
        names.add(Tag.of().name("Cherry pie").create());
        names.forEach(tags -> {
            tags.setParent(parent);
            tags.setType(TagType.MENU_ITEM);
        });
        return names;
    }

    public static List<Tag> getMarmaladeTags(Tag parent) {
        List<Tag> names = new ArrayList<>();
        names.add(Tag.of().name("Plum marmalade").create());
        names.add(Tag.of().name("Pear marmalade").create());
        names.add(Tag.of().name("Orange marmalade").create());
        names.add(Tag.of().name("Grapefruit marmalade").create());
        names.add(Tag.of().name("Strawberry with mint, banana and chocolate marmalade").create());
        names.add(Tag.of().name("Apple marmalade").create());
        names.add(Tag.of().name("Cherry marmalade").create());
        names.forEach(tags -> {
            tags.setParent(parent);
            tags.setType(TagType.MENU_ITEM);
        });
        return names;
    }

    public static List<Tag> getPastryTags(Tag parent) {
        List<Tag> names = new ArrayList<>();
        names.add(Tag.of().name("Babka").create());
        names.add(Tag.of().name("Baumkuchen").create());
        names.add(Tag.of().name("Muffin").create());
        names.add(Tag.of().name("Cupcake").create());
        names.add(Tag.of().name("Croissant").create());
        names.add(Tag.of().name("Tartalette").create());
        names.add(Tag.of().name("Apple strudel").create());
        names.add(Tag.of().name("Cannoli").create());
        names.add(Tag.of().name("Cinnamon roll").create());
        names.add(Tag.of().name("Eclair").create());
        names.add(Tag.of().name("Kifli").create());
        names.add(Tag.of().name("Vatrushka").create());
        names.add(Tag.of().name("Macaron").create());
        names.add(Tag.of().name("Mille-feuille").create());
        names.forEach(tags -> {
            tags.setParent(parent);
            tags.setType(TagType.MENU_ITEM);
        });
        return names;
    }

    public static List<Tag> getIceCreamTags(Tag parent) {
        List<Tag> names = new ArrayList<>();
        names.add(Tag.of().name("Vanilla ice cream").create());
        names.add(Tag.of().name("Choclate ice cream").create());
        names.add(Tag.of().name("Strawberry ice cream").create());
        names.add(Tag.of().name("Coconut ice cream").create());
        names.add(Tag.of().name("Mint and raspberry ice cream").create());
        names.add(Tag.of().name("Blueberry ice cream").create());
        names.add(Tag.of().name("Mango ice cream").create());
        names.add(Tag.of().name("Lemon ice cream").create());
        names.forEach(tags -> {
            tags.setParent(parent);
            tags.setType(TagType.MENU_ITEM);
        });
        return names;
    }

    public static List<Tag> getFruitSaladTags(Tag parent) {
        List<Tag> names = new ArrayList<>();
        names.add(Tag.of().name("Season fruit salad").create());
        names.forEach(tags -> {
            tags.setParent(parent);
            tags.setType(TagType.MENU_ITEM);
        });
        return names;
    }

    public static List<Tag> getPuddingTags(Tag parent) {
        List<Tag> names = new ArrayList<>();
        names.add(Tag.of().name("Vanilla pudding").create());
        names.add(Tag.of().name("Chocolate pudding").create());
        names.add(Tag.of().name("Berry pudding").create());
        names.add(Tag.of().name("Citrus pudding").create());
        names.forEach(tags -> {
            tags.setParent(parent);
            tags.setType(TagType.MENU_ITEM);
        });
        return names;
    }

    public static List<Tag> getSweetSoupTags(Tag parent) {
        List<Tag> names = new ArrayList<>();
        names.add(Tag.of().name("Strawberry sweet soup").create());
        names.forEach(tags -> {
            tags.setParent(parent);
            tags.setType(TagType.MENU_ITEM);
        });
        return names;
    }

    public static List<Tag> getSaladTags(Tag parent) {
        List<Tag> names = new ArrayList<>();
        names.add(Tag.of().name("Shrimps salad").create());
        names.add(Tag.of().name("Grilled Vegetables salad").create());
        names.add(Tag.of().name("Ceasar salad").create());
        names.add(Tag.of().name("Tuna salad").create());
        names.add(Tag.of().name("Greek salad").create());
        names.add(Tag.of().name("Caprese salad").create());
        names.add(Tag.of().name("Nicoise salad").create());
        names.add(Tag.of().name("Parma Ham salad").create());
        names.forEach(tags -> {
            tags.setParent(parent);
            tags.setType(TagType.MENU_ITEM);
        });
        return names;
    }

    public static List<Tag> getPlateTags(Tag parent) {
        List<Tag> names = new ArrayList<>();
        names.add(Tag.of().name("Cheese plate").create());
        names.add(Tag.of().name("Fruit plate").create());
        names.add(Tag.of().name("Hamon plate").create());
        names.add(Tag.of().name("Meat plate").create());
        names.add(Tag.of().name("Vegetables plate").create());
        names.forEach(tags -> {
            tags.setParent(parent);
            tags.setType(TagType.MENU_ITEM);
        });
        return names;
    }

    public static List<Tag> getSandwichTags(Tag parent) {
        List<Tag> names = new ArrayList<>();
        names.add(Tag.of().name("Egg Sandwich").create());
        names.add(Tag.of().name("Club Sandwich").create());
        names.add(Tag.of().name("New York bagel").create());
        names.add(Tag.of().name("Chicken Marsala").create());
        names.add(Tag.of().name("Salmon Sandwich").create());
        names.add(Tag.of().name("Tuna Sandwich").create());
        names.forEach(tags -> {
            tags.setParent(parent);
            tags.setType(TagType.MENU_ITEM);
        });
        return names;
    }

    public static List<Tag> getAppetizerTags(Tag parent) {
        List<Tag> names = new ArrayList<>();
        names.add(Tag.of().name("Onion Rings").create());
        names.add(Tag.of().name("Nachos").create());
        names.add(Tag.of().name("Buritos").create());
        names.add(Tag.of().name("Bruschetto").create());
        names.add(Tag.of().name("Carpaccio").create());
        names.add(Tag.of().name("Foccacia").create());
        names.add(Tag.of().name("French fries").create());
        names.add(Tag.of().name("Calamary Rings").create());
        names.add(Tag.of().name("Chips").create());
        names.add(Tag.of().name("Garlic Bread").create());
        names.add(Tag.of().name("Cheese Garlic Bread").create());
        names.forEach(tags -> {
            tags.setParent(parent);
            tags.setType(TagType.MENU_ITEM);
        });
        return names;
    }

    public static List<Tag> getSauceTags(Tag parent) {
        List<Tag> names = new ArrayList<>();
        names.add(Tag.of().name("Sweet'n sour sauce").create());
        names.add(Tag.of().name("Honey mustard sauce").create());
        names.add(Tag.of().name("Chipotle BBQ sauce").create());
        names.add(Tag.of().name("Tangy BBQ sauce").create());
        names.add(Tag.of().name("Spicy buffalo sauce").create());
        names.add(Tag.of().name("Sweet chili sauce").create());
        names.add(Tag.of().name("Hot mustard sauce").create());
        names.add(Tag.of().name("Chreamy ranch sauce").create());
        names.add(Tag.of().name("Thousand Island sauce").create());
        names.forEach(tags -> {
            tags.setParent(parent);
            tags.setType(TagType.MENU_ITEM);
        });
        return names;
    }

}
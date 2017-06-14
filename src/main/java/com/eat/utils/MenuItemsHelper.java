package com.eat.utils;

import com.eat.models.b2b.MenuItem;
import com.eat.models.b2b.PlaceDetail;

import java.util.*;

/**
 * Created by ksolodkyy@gmail.com
 * 19.04.17
 */
public class MenuItemsHelper {

    public static List<MenuItem> getMenuItemsByPlace(PlaceDetail placeType, PlaceDetail placeCuisine) {
        if (placeCuisine == null ) {
            placeCuisine = PlaceDetail.of().name("European").create();
        }
        switch (placeType.getName()) {
            case "Restaurant" :
                switch (placeCuisine.getName()) {
                    case "Vegan/ vegetarian" :
                    case "Vegetarian" :
                        return getRandomMenuItemsByNames(getVeganRestaurantMenuItemNames());
                    case "Thai" :
                    case "Chinese" :
                        return getRandomMenuItemsByNames(getThaiRestaurantMenuItemNames());
                    case "Mexican" :
                    case "Indian" :
                        return getRandomMenuItemsByNames(getMexicanRestaurantMenuItemNames());
                    case "European" :
                        return getRandomMenuItemsByNames(getEuropeanRestaurantMenuItemNames());
                    case "Fusion" :
                        return getRandomMenuItemsByNames(getFusionRestaurantMenuItemNames());
                    case "Russian" :
                        return getRandomMenuItemsByNames(getRussianRestaurantMenuItemNames());
                    case "German" :
                        return getRandomMenuItemsByNames(getGermanRestaurantMenuItemNames());
                    case "French" :
                        return getRandomMenuItemsByNames(getFrenchRestaurantMenuItemNames());
                    case "British" :
                    case "American" :
                        return getRandomMenuItemsByNames(getBritishRestaurantMenuItemNames());
                    case "Spanish" :
                    case "Mediterranean" :
                        return getRandomMenuItemsByNames(getSpanishRestaurantMenuItemNames());
                    case "Arab" :
                    case "Turkish" :
                        return getRandomMenuItemsByNames(getArabRestaurantMenuItemNames());
                }
                break;
            case "Bakery" :
            case "Breakfast Spot" :
                return getRandomMenuItemsByNames(getBakeryMenuItemNames());
            case "Beer Garden" :
            case "Brewery" :
            case "Pub" :
                return getRandomMenuItemsByNames(getBeerGardenMenuItemNames());
            case "Bistro" :
            case "Gastropub" :
            case "Fastfood" :
            case "Sandwich Bar" :
                return getRandomMenuItemsByNames(getBistroMenuItemNames());
            case "Café" :
            case "Coffee Shop" :
                return getRandomMenuItemsByNames(getCafeMenuItemNames());
            case "Pizza Place" :
                return getRandomMenuItemsByNames(getPizzaPlaceMenuItemNames());
            case "Sushi Restaurant" :
                return getRandomMenuItemsByNames(getSushiRestaurantMenuItemNames());
            case "Steakhouse" :
                return getRandomMenuItemsByNames(getSteakhouseMenuItemNames());
            case "Burger Joint" :
                return getRandomMenuItemsByNames(getBurgerJointMenuItemNames());
            case "Cocktail Bar" :
                return getRandomMenuItemsByNames(getCocktailBarMenuItemNames());
            case "Donut Shop" :
                return getRandomMenuItemsByNames(getDonutShopMenuItemNames());
            case "Ice Cream Shop" :
                return getRandomMenuItemsByNames(getIceCreamShopMenuItemNames());
            case "Salad Bar" :
                return getRandomMenuItemsByNames(getSaladBarMenuItemNames());
        }
        return new ArrayList<>();
    }

    private static List<String> getVeganRestaurantMenuItemNames() {
        return Arrays.asList("Vegetarian Pizza", "Fish", "Fish Burger", "Vegetarian Burger", "Bean Soup",
                "Tea", "Espresso", "Juice", "Lemonade", "Water", "Vegetables Plate", "Fruit Salad", "Shrimps Salad", "Prawns Salad");
    }

    private static List<String> getThaiRestaurantMenuItemNames() {
        return Arrays.asList("Noodles", "Tom Yum Soup", "Phat Thai", "Juice", "Water");
    }

    private static List<String> getMexicanRestaurantMenuItemNames() {
        return Arrays.asList("Meat", "Curry Soup", "Water", "Juice", "Tequila", "Burritos", "Nachos");
    }

    private static List<String> getEuropeanRestaurantMenuItemNames() {
        return Arrays.asList("Lasagne", "Pasta", "Latte", "Espresso", "Cappuccino", "Frappuccino", "Wine",
                "Caesar Salad", "Parma Ham Salad", "Caprese Salad", "Bruschetta", "Carpaccio", "Focaccia");
    }

    private static List<String> getFusionRestaurantMenuItemNames() {
        return Arrays.asList("Ravioli", "Cream Soup", "Wine", "Cheese Plate", "Fruit Plate", "Jamon Plate");
    }

    private static List<String> getRussianRestaurantMenuItemNames() {
        return Arrays.asList("Ravioli", "Meat", "Borsch", "Okroshka", "Vodka", "Meat Plate");
    }

    private static List<String> getGermanRestaurantMenuItemNames() {
        return Arrays.asList("Meat", "Sausage", "Schnapps", "Lemonade", "Water", "Beer", "Pretzel", "Potato Salad", "French Fries");
    }

    private static List<String> getFrenchRestaurantMenuItemNames() {
        return Arrays.asList("Cheese Soup", "Omelette", "Latte", "Espresso", "Coffee", "Cognac", "Wine", "Madeleine Cookies",
                "Cannoli", "Eclair", "Macaron", "Mille-feuille", "Cheese Plate", "Nicoise Salad");
    }

    private static List<String> getBritishRestaurantMenuItemNames() {
        return Arrays.asList("Pizza", "Steak", "Cheese Burger", "Beef Burger", "Chicken Burger", "Hot Dogs",
                "BBQ", "Tea", "Latte", "Espresso", "Americano", "Juice", "Lemonade", "Soda", "Smoozy", "Milkshake",
                "Pancakes", "Chocolate Chip Cookie", "Pop-corn", "Pumpkin Pie", "Donut", "Chips");
    }

    private static List<String> getSpanishRestaurantMenuItemNames() {
        return Arrays.asList("Ravioli", "Meat", "Fish", "Minestrone Soup", "Latte", "Espresso", "Frappuccino",
                "Juice", "Lemonade", "Water", "Wine", "Shrimps Salad", "Jamon Plate", "Calamary Rings", "Garlic Bread");
    }

    private static List<String> getArabRestaurantMenuItemNames() {
        return Arrays.asList("Goulash", "Meat", "Kebab", "Beef Soup", "Water", "Tea", "Espresso");
    }

    private static List<String> getBistroMenuItemNames() {
        return Arrays.asList("Stew", "Beef Burger", "Chicken Burger", "Cheese Burger", "Chicken Soup", "Tea",
                "Latte", "Americano", "Coffee", "Lemonade", "Water", "Cheese Pie", "Muffin", "French Fries", "Sandwich");
    }

    private static List<String> getSaladBarMenuItemNames() {
        return Arrays.asList("Tea", "Latte", "Water", "Espresso", "Juice", "Lemonade", "Fruit Salad", "Chicken Salad",
                "Tuna Salad", "Greek Salad", "Seafood Salad");
    }

    private static List<String> getBakeryMenuItemNames() {
        return Arrays.asList("Gingerbread", "Cake", "Cherry Pie", "Cheese Pie", "Cupcake", "Muffin", "Cinnamon Roll",
                "Bagel", "Waffle", "Pancakes", "Cereal", "Eggs", "Yogurt", "Toast", "Tea", "Latte", "Espresso", "Cappuccino",
                "Coffee");
    }

    private static List<String> getCocktailBarMenuItemNames() {
        return Arrays.asList("Espresso", "Juice", "Water", "Rum", "Gin", "Whiskey", "Liqueur", "Wine", "Beer",
                "Cider", "Cocktail", "Burn", "Red Bull", "Non Stop", "Alcohol", "Carpaccio", "Grissini", "Olives");
    }

    private static List<String> getBeerGardenMenuItemNames() {
        return Arrays.asList("Chicken Burger", "Alcohol", "Lemonade", "Water", "Beer", "Cider", "Cheese Pie",
                "Meat Plate", "Corn", "Onion Rings", "Calamary Rings", "Chips", "Garlic Bread");
    }

    private static List<String> getPizzaPlaceMenuItemNames() {
        return Arrays.asList("Pizza", "Americano", "Lemonade", "Juice", "Water");
    }

    private static List<String> getSteakhouseMenuItemNames() {
        return Arrays.asList("Steak", "Meat", "Coffee", "Juice", "Lemonade", "Water", "Whiskey", "Wine", "Tequila",
                "Vodka", "Grilled Vegetables Salad");
    }

    private static List<String> getSushiRestaurantMenuItemNames() {
        return Arrays.asList("Fish", "Temaki", "Sushimi", "Rolls", "Sushi", "Miso Soup", "Ramen", "Tea", "Juice",
                "Wine", "Sake");
    }

    private static List<String> getBurgerJointMenuItemNames() {
        return Arrays.asList("Beef Burger", "Chicken Burger", "Cheese Burger", "Lemonade", "Water", "Juice",
                "Cider", "French Fries", "Chips");
    }

    private static List<String> getDonutShopMenuItemNames() {
        return Arrays.asList("Tea", "Latte", "Espresso", "Frappuccino", "Americano", "Cappuccino", "Coffee",
                "Juice", "Lemonade", "Water", "Donut");
    }

    private static List<String> getIceCreamShopMenuItemNames() {
        return Arrays.asList("Tea", "Latte", "Espresso", "Frappuccino", "Cappuccino", "Coffee", "Lemonade",
                "Gelato", "Ice Cream");
    }

    private static List<String> getCafeMenuItemNames() {
        return Arrays.asList("Tea", "Latte", "Espresso", "Frappuccino", "Cappuccino", "Americano", "Coffee",
                "Juice", "Lemonade", "Water", "Chocolate", "Marmalade", "Muffin", "Cupcake", "Croissant", "Donut",
                "Tartalette", "Apple Strudel", "Oat Cookies", "Cinnamon Roll", "Eclair", "Pudding");
    }

    private static List<MenuItem> getRandomMenuItemsByNames(List<String> names) {
        Collections.shuffle(names);
        List<String> finalListOfNames = names.subList(0,  4 + new Random().nextInt(names.size() - 4));
        List<MenuItem> result = new ArrayList<>();
        for (String name : finalListOfNames) {
            MenuItem menuItem = MenuItem.of()
                    .name(name)
                    .create();
            result.add(menuItem);
        }
        return result;
    }
}

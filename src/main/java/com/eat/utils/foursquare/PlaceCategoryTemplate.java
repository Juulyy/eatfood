package com.eat.utils.foursquare;

import com.eat.utils.TagsHelper;
import lombok.*;
import org.apache.commons.lang3.RandomUtils;

import java.util.*;

@Getter
@Setter
@Builder(builderMethodName = "of", buildMethodName = "create")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class PlaceCategoryTemplate {

    private String placeType;

    @Singular
    private List<String> interiorTags;

    private List<String> featureTags;

    @Singular
    private List<String> cuisineTags;

    public static Map<String, PlaceCategoryTemplate> getPlaceCategoryTemplates() {
        Map<String, PlaceCategoryTemplate> categories = new HashMap<>();

        categories.put("4bf58dd8d48988d142941735", of()
                .placeType("Restaurant")
                .interiorTags(getRandomInteriorTags())
                .featureTags(getRandomFeatureTags())
                .cuisineTags(Arrays.asList("Asian", "Japanese", "Chinese", "Thai"))
                .cuisineTags(getRandomCuisineTags())
                .create());

        categories.put("4bf58dd8d48988d169941735", of()
                .placeType("Restaurant")
                .interiorTags(getRandomInteriorTags())
                .featureTags(getRandomFeatureTags())
                .cuisineTag("Australian")
                .cuisineTags(getRandomCuisineTags())
                .create());

        categories.put("4bf58dd8d48988d16a941735", of()
                .placeType("Bakery")
                .interiorTags(getRandomInteriorTags())
                .featureTags(getRandomFeatureTags())
                .cuisineTags(getRandomCuisineTags())
                .create());

        categories.put("4bf58dd8d48988d116941735", of()
                .placeType("Bar")
                .interiorTags(getRandomInteriorTags())
                .featureTags(getRandomFeatureTags())
                .cuisineTags(getRandomCuisineTags())
                .create());

        categories.put("4bf58dd8d48988d117941735", of()
                .placeType("Beer Garden")
                .interiorTags(getRandomInteriorTags())
                .featureTags(getRandomFeatureTags())
                .cuisineTags(getRandomCuisineTags())
                .create());

        categories.put("52e81612bcbc57f1066b79f1", of()
                .placeType("Bistro")
                .interiorTags(getRandomInteriorTags())
                .featureTags(getRandomFeatureTags())
                .cuisineTags(getRandomCuisineTags())
                .create());

        categories.put("50327c8591d4c4b30a586d5d", of()
                .placeType("Brewery")
                .interiorTags(getRandomInteriorTags())
                .featureTags(getRandomFeatureTags())
                .cuisineTags(getRandomCuisineTags())
                .create());

        categories.put("4bf58dd8d48988d16d941735", of()
                .placeType("Café")
                .interiorTags(getRandomInteriorTags())
                .featureTags(getRandomFeatureTags())
                .cuisineTags(getRandomCuisineTags())
                .create());

        categories.put("4bf58dd8d48988d145941735", of()
                .placeType("Restaurant")
                .interiorTags(getRandomInteriorTags())
                .featureTags(getRandomFeatureTags())
                .cuisineTag("Chinese")
                .cuisineTags(getRandomCuisineTags())
                .create());

        categories.put("4bf58dd8d48988d147941735", of()
                .placeType("Diner")
                .interiorTags(getRandomInteriorTags())
                .featureTags(getRandomFeatureTags())
                .cuisineTags(getRandomCuisineTags())
                .create());

        categories.put("5283c7b4e4b094cb91ec88d8", of()
                .placeType("Doner Restaurant")
                .interiorTags(getRandomInteriorTags())
                .featureTags(getRandomFeatureTags())
                .cuisineTags(getRandomCuisineTags())
                .create());

        categories.put("4bf58dd8d48988d10b941735", of()
                .placeType("Falafel Restaurant")
                .interiorTags(getRandomInteriorTags())
                .featureTags(getRandomFeatureTags())
                .cuisineTags(getRandomCuisineTags())
                .create());

        categories.put("4bf58dd8d48988d16e941735", of()
                .placeType("Fast Food Restaurant")
                .interiorTags(getRandomInteriorTags())
                .featureTags(getRandomFeatureTags())
                .cuisineTags(getRandomCuisineTags())
                .create());

        categories.put("4bf58dd8d48988d10c941735", of()
                .placeType("Restaurant")
                .interiorTags(getRandomInteriorTags())
                .featureTags(getRandomFeatureTags())
                .cuisineTag("French")
                .cuisineTags(getRandomCuisineTags())
                .create());

        categories.put("4bf58dd8d48988d155941735", of()
                .placeType("Gastropub")
                .interiorTags(getRandomInteriorTags())
                .featureTags(getRandomFeatureTags())
                .cuisineTags(getRandomCuisineTags())
                .create());

        categories.put("4bf58dd8d48988d10d941735", of()
                .placeType("Restaurant")
                .interiorTags(getRandomInteriorTags())
                .featureTags(getRandomFeatureTags())
                .cuisineTags(getRandomCuisineTags())
                .create());

        categories.put("4bf58dd8d48988d10e941735", of()
                .placeType("Restaurant")
                .interiorTags(getRandomInteriorTags())
                .featureTags(getRandomFeatureTags())
                .cuisineTag("Greek")
                .cuisineTags(getRandomCuisineTags())
                .create());

        categories.put("4bf58dd8d48988d10f941735", of()
                .placeType("Restaurant")
                .interiorTags(getRandomInteriorTags())
                .featureTags(getRandomFeatureTags())
                .cuisineTag("Indian")
                .cuisineTags(getRandomCuisineTags())
                .create());

        categories.put("4bf58dd8d48988d110941735", of()
                .placeType("Restaurant")
                .interiorTags(getRandomInteriorTags())
                .featureTags(getRandomFeatureTags())
                .cuisineTag("Italian")
                .cuisineTags(getRandomCuisineTags())
                .create());

        categories.put("4bf58dd8d48988d113941735", of()
                .placeType("Restaurant")
                .interiorTags(getRandomInteriorTags())
                .featureTags(getRandomFeatureTags())
                .cuisineTag("Korean")
                .cuisineTags(getRandomCuisineTags())
                .create());

        categories.put("4bf58dd8d48988d121941735", of()
                .placeType("Lounge")
                .interiorTags(getRandomInteriorTags())
                .featureTags(getRandomFeatureTags())
                .cuisineTags(getRandomCuisineTags())
                .create());

        categories.put("4bf58dd8d48988d1c0941735", of()
                .placeType("Restaurant")
                .interiorTags(getRandomInteriorTags())
                .featureTags(getRandomFeatureTags())
                .cuisineTags(Arrays.asList("Mediterranean", "Greek", "Italian", "French", "Spanish"))
                .cuisineTags(getRandomCuisineTags())
                .create());

        categories.put("4bf58dd8d48988d1c1941735", of()
                .placeType("Restaurant")
                .interiorTags(getRandomInteriorTags())
                .featureTags(getRandomFeatureTags())
                .cuisineTag("Mexican")
                .cuisineTags(getRandomCuisineTags())
                .create());

        categories.put("4bf58dd8d48988d115941735", of()
                .placeType("Restaurant")
                .interiorTags(getRandomInteriorTags())
                .featureTags(getRandomFeatureTags())
                .cuisineTags(getRandomCuisineTags())
                .create());

        categories.put("4bf58dd8d48988d1ca941735", of()
                .placeType("Pizza Place")
                .interiorTags(getRandomInteriorTags())
                .featureTags(getRandomFeatureTags())
                .cuisineTags(getRandomCuisineTags())
                .create());

        categories.put("4bf58dd8d48988d11b941735", of()
                .placeType("Pub")
                .interiorTags(getRandomInteriorTags())
                .featureTags(getRandomFeatureTags())
                .cuisineTags(getRandomCuisineTags())
                .create());

        categories.put("4bf58dd8d48988d1c4941735", of()
                .placeType("Restaurant")
                .interiorTags(getRandomInteriorTags())
                .featureTags(getRandomFeatureTags())
                .cuisineTags(getRandomCuisineTags())
                .create());

        categories.put("5293a7563cf9994f4e043a44", of()
                .placeType("Restaurant")
                .interiorTags(getRandomInteriorTags())
                .featureTags(getRandomFeatureTags())
                .cuisineTag("Russian")
                .cuisineTags(getRandomCuisineTags())
                .create());

        categories.put("4bf58dd8d48988d1cc941735", of()
                .placeType("Steakhouse")
                .interiorTags(getRandomInteriorTags())
                .featureTags(getRandomFeatureTags())
                .cuisineTags(getRandomCuisineTags())
                .create());

        categories.put("4bf58dd8d48988d1d2941735", of()
                .placeType("Sushi Restaurant")
                .interiorTags(getRandomInteriorTags())
                .featureTags(getRandomFeatureTags())
                .cuisineTag("Japanese")
                .cuisineTags(getRandomCuisineTags())
                .create());

        categories.put("4bf58dd8d48988d149941735", of()
                .placeType("Restaurant")
                .interiorTags(getRandomInteriorTags())
                .featureTags(getRandomFeatureTags())
                .cuisineTag("Thai")
                .cuisineTags(getRandomCuisineTags())
                .create());

        categories.put("4f04af1f2fb6e1c99f3db0bb", of()
                .placeType("Restaurant")
                .interiorTags(getRandomInteriorTags())
                .featureTags(getRandomFeatureTags())
                .cuisineTag("Turkish")
                .cuisineTags(getRandomCuisineTags())
                .create());

        categories.put("4bf58dd8d48988d1d3941735", of()
                .placeType("Restaurant")
                .interiorTags(getRandomInteriorTags())
                .featureTags(getRandomFeatureTags())
                .cuisineTag("Vegetarian")
                .cuisineTags(getRandomCuisineTags())
                .create());

        categories.put("4bf58dd8d48988d1df931735", of()
                .placeType("BBQ Joint")
                .interiorTags(getRandomInteriorTags())
                .featureTags(getRandomFeatureTags())
                .cuisineTags(getRandomCuisineTags())
                .create());

        categories.put("4bf58dd8d48988d16c941735", of()
                .placeType("Burger Joint")
                .interiorTags(getRandomInteriorTags())
                .featureTags(getRandomFeatureTags())
                .cuisineTags(getRandomCuisineTags())
                .create());

        categories.put("4bf58dd8d48988d11e941735", of()
                .placeType("Cocktail Bar")
                .interiorTags(getRandomInteriorTags())
                .featureTags(getRandomFeatureTags())
                .cuisineTags(getRandomCuisineTags())
                .create());

        categories.put("4bf58dd8d48988d1e0931735", of()
                .placeType("Coffee Shop")
                .interiorTags(getRandomInteriorTags())
                .featureTags(getRandomFeatureTags())
                .cuisineTags(getRandomCuisineTags())
                .create());

        categories.put("56aa371ce4b08b9a8d57358b", of()
                .placeType("Currywurst Joint")
                .interiorTags(getRandomInteriorTags())
                .featureTags(getRandomFeatureTags())
                .cuisineTags(getRandomCuisineTags())
                .create());

        categories.put("4bf58dd8d48988d143941735", of()
                .placeType("Breakfast Spot")
                .interiorTags(getRandomInteriorTags())
                .featureTags(getRandomFeatureTags())
                .cuisineTags(getRandomCuisineTags())
                .create());

        categories.put("4bf58dd8d48988d1a1941735", of()
                .placeType("College Cafeteria")
                .interiorTags(getRandomInteriorTags())
                .featureTags(getRandomFeatureTags())
                .cuisineTags(getRandomCuisineTags())
                .create());

        categories.put("4bf58dd8d48988d148941735", of()
                .placeType("Donut Shop")
                .interiorTags(getRandomInteriorTags())
                .featureTags(getRandomFeatureTags())
                .cuisineTags(getRandomCuisineTags())
                .create());

        categories.put("4edd64a0c7ddd24ca188df1a", of()
                .placeType("Fish & Chips Shop")
                .interiorTags(getRandomInteriorTags())
                .featureTags(getRandomFeatureTags())
                .cuisineTags(getRandomCuisineTags())
                .create());

        categories.put("4bf58dd8d48988d120951735", of()
                .placeType("Food Court")
                .interiorTags(getRandomInteriorTags())
                .featureTags(getRandomFeatureTags())
                .cuisineTags(getRandomCuisineTags())
                .create());

        categories.put("4bf58dd8d48988d1cb941735", of()
                .placeType("Food Truck")
                .interiorTags(getRandomInteriorTags())
                .featureTags(getRandomFeatureTags())
                .cuisineTags(getRandomCuisineTags())
                .create());

        categories.put("4d4ae6fc7a7b7dea34424761", of()
                .placeType("Fried Chicken Joint")
                .interiorTags(getRandomInteriorTags())
                .featureTags(getRandomFeatureTags())
                .cuisineTags(getRandomCuisineTags())
                .create());

        categories.put("4bf58dd8d48988d1d8941735", of()
                .placeType("Gay Bar")
                .interiorTags(getRandomInteriorTags())
                .featureTags(getRandomFeatureTags())
                .cuisineTags(getRandomCuisineTags())
                .create());

        categories.put("4bf58dd8d48988d119941735", of()
                .placeType("Hookag Bar")
                .interiorTags(getRandomInteriorTags())
                .featureTags(getRandomFeatureTags())
                .cuisineTags(getRandomCuisineTags())
                .create());

        categories.put("4bf58dd8d48988d1c9941735", of()
                .placeType("Ice Cream Shop")
                .interiorTags(getRandomInteriorTags())
                .featureTags(getRandomFeatureTags())
                .cuisineTags(getRandomCuisineTags())
                .create());

        categories.put("4bf58dd8d48988d1bd941735", of()
                .placeType("Salad Place")
                .interiorTags(getRandomInteriorTags())
                .featureTags(getRandomFeatureTags())
                .cuisineTags(getRandomCuisineTags())
                .create());

        categories.put("4bf58dd8d48988d1c7941735", of()
                .placeType("Snack Place")
                .interiorTags(getRandomInteriorTags())
                .featureTags(getRandomFeatureTags())
                .cuisineTags(getRandomCuisineTags())
                .create());

        categories.put("4bf58dd8d48988d1dc931735", of()
                .placeType("Tea Room")
                .interiorTags(getRandomInteriorTags())
                .featureTags(getRandomFeatureTags())
                .cuisineTags(getRandomCuisineTags())
                .create());

        return categories;
    }

    public static List<String> getRandomInteriorTags() {
        return randomTagsGenerator(TagsHelper.getInteriorNames(), 2);
    }

    public static List<String> getRandomFeatureTags() {
        return randomTagsGenerator(TagsHelper.getFeaturesNames(), 2);
    }

    public static List<String> getRandomMusicTags() {
        return randomTagsGenerator(TagsHelper.getMusicNames(), 2);
    }

    public static List<String> getRandomCuisineTags() {
        return randomTagsGenerator(TagsHelper.getCuisineNames(), 2);
    }

    public static List<String> randomTagsGenerator(List<String> tagNamesCollection, int tagsNumber) {
        Set<String> tags = new HashSet<>();

        while (tagsNumber >= 1) {
            int tagIndex = RandomUtils.nextInt(0, tagNamesCollection.size());
            String tagName = tagNamesCollection.get(tagIndex);
            if (!tags.contains(tagName)) {
                tags.add(tagName);
                tagsNumber--;
            }
        }

        return new ArrayList<>(tags);
    }
}

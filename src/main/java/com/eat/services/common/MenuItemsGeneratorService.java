package com.eat.services.common;

import com.eat.models.b2b.Menu;
import com.eat.models.b2b.MenuItem;
import com.eat.models.b2b.Place;
import com.eat.models.b2b.PlaceDetail;
import com.eat.models.b2b.enums.PlaceDetailType;
import com.eat.models.common.Tag;
import com.eat.services.b2b.MenuService;
import com.eat.services.b2b.PlaceDetailService;
import com.eat.services.b2b.PlaceService;
import com.eat.utils.MenuItemsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MenuItemsGeneratorService {

    @Autowired
    private PlaceService placeService;

    @Autowired
    private PlaceDetailService placeDetailService;

    @Autowired
    private MenuService menuService;

    private List<Place> places;

    @Autowired
    private TagService tagService;

    @PostConstruct
    private void afterInit() {
        places = placeService.findAll();
    }

    public void processMenuGeneration() {
        places.forEach(place -> {
            place.getMenus().forEach(menu -> menuService.delete(menu));
            Menu mainMenu = Menu.of()
                    .name("Main")
                    .place(place)
                    .create();

            Optional<Set<PlaceDetail>> placeTypesOptional = Optional
                    .ofNullable(placeDetailService.findAllPlaceDetailsInPlaceByType(place, PlaceDetailType.PLACE_TYPE));

            Optional<Set<PlaceDetail>> placeCuisinesOptional = Optional
                    .ofNullable(placeDetailService.findAllPlaceDetailsInPlaceByType(place, PlaceDetailType.CUISINE));

            PlaceDetail firstPlaceDetail;

            if (!placeTypesOptional.get().isEmpty()) {
                firstPlaceDetail = placeTypesOptional.get().iterator().next();
            } else {
                firstPlaceDetail = placeDetailService.findByName("Restaurant");
            }

            PlaceDetail firstPlaceCuisine = null;

            if (!placeCuisinesOptional.get().isEmpty()) {
                firstPlaceCuisine = placeCuisinesOptional.get().iterator().next();
            } else {
                firstPlaceCuisine = PlaceDetail.of()
                        .name("European")
                        .placeDetailType(PlaceDetailType.CUISINE)
                        .create();
            }
            List<MenuItem> menuItems = MenuItemsHelper.getMenuItemsByPlace(firstPlaceDetail, firstPlaceCuisine);

            for (MenuItem menuItem : menuItems) {
                Set<Tag> tags = new HashSet<>();
                try {
                    tags.add(tagService.findByNameInCache(menuItem.getName()));
                } catch (Exception e) {}
                menuItem.setTags(tags);
            }
            mainMenu.setMenuItems(menuItems);
            menuService.updateMenu(mainMenu, place.getId());
//            placeService.update(addFeatures(place, firstPlaceDetail, firstPlaceCuisine));
        });

    }

    public void processMenuGeneration(Place place) {

        Menu mainMenu = place.getMenus().iterator().next();

        Optional<Set<PlaceDetail>> placeTypesOptional = Optional
                .ofNullable(placeDetailService.findAllPlaceDetailsInPlaceByType(place, PlaceDetailType.PLACE_TYPE));

        Optional<Set<PlaceDetail>> placeCuisinesOptional = Optional
                .ofNullable(placeDetailService.findAllPlaceDetailsInPlaceByType(place, PlaceDetailType.CUISINE));

        PlaceDetail firstPlaceDetail = placeTypesOptional.get().iterator().next();

        PlaceDetail firstPlaceCuisine = null;

        if (!placeCuisinesOptional.get().isEmpty()) {
            firstPlaceCuisine = placeCuisinesOptional.get().iterator().next();
        } else {
            firstPlaceCuisine = PlaceDetail.of()
                    .name("European")
                    .placeDetailType(PlaceDetailType.CUISINE)
                    .create();
        }
        List<MenuItem> menuItems = MenuItemsHelper.getMenuItemsByPlace(firstPlaceDetail, firstPlaceCuisine);

        for (MenuItem menuItem : menuItems) {
            Set<Tag> tags = new HashSet<>();
            try {
                tags.add(tagService.findByNameInCache(menuItem.getName()));
            } catch (Exception e) {}
            menuItem.setTags(tags);
        }
        mainMenu.setMenuItems(menuItems);
        menuService.updateMenu(mainMenu, place.getId());
        placeService.update(addFeatures(place, firstPlaceDetail, firstPlaceCuisine));
    }

    public void deleteAllMenuTags() {
        List<Long> idsToRemove = tagService.findAll().stream()
                .filter(Tag::isMenuTag)
                .map(Tag::getId)
                .collect(Collectors.toList());

        tagService.deleteByIdIn(idsToRemove);
    }

    private Place addFeatures(Place place, PlaceDetail placeType, PlaceDetail cuisine) {
        switch (placeType.getName()) {
            case "Restaurant" :
                switch (cuisine.getName()) {
                    case "Vegan/ vegetarian" :
                        addFeature(place, "Good for Vegans");
                        addFeature(place, "Good for Vegetarians");
                        break;
                    case "Vegetarian" :
                        addFeature(place, "Good for Vegetarians");
                        break;
                    case "Thai" :
                    case "Chinese" :
                        addFeature(place, "Good for Low-calories");
                        break;
                    case "French" :
                        addFeature(place, "Good for Vegetarians");
                        break;
                    case "Spanish" :
                    case "Mediterranean" :
                        addFeature(place, "Good for Low-calories");
                        break;
                }
                break;
            case "Salad Bar" :
                addFeature(place, "Good for Low-calories");
                addFeature(place, "Good for Vegetarians");
                break;
            case "Bakery" :
            case "Breakfast Spot" :
                addFeature(place, "Good for Vegetarians");
                break;
            case "Cocktail Bar" :
                addFeature(place, "Good for Vegetarians");
                break;
            case "Sushi Restaurant" :
                addFeature(place, "Good for Vegetarians");
                addFeature(place, "Good for Vegans");
                addFeature(place, "Good for Low-calories");
                break;
            case "Ice Cream Shop" :
                addFeature(place, "Good for Vegetarians");
                break;
            case "Café" :
            case "Coffee Shop" :
                addFeature(place, "Good for Vegetarians");
                break;
        }
        return place;
    }

    private Place addFeature(Place place, String featureName) {
        place.getPlaceDetails().add(placeDetailService.findByName(featureName));
        return place;
    }

}

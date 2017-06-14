package com.eat.logic.rankers;

import com.eat.logic.rankers.wrappers.Rankable;
import com.eat.models.b2b.Menu;
import com.eat.models.b2b.MenuItem;
import com.eat.models.b2b.Place;
import com.eat.models.common.Tag;
import com.eat.models.recommender.RankingRate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TagsBasedPlaceRanker extends AbstractRankingCounter<Place> {

    public TagsBasedPlaceRanker(List<RankingRate> rankingRates) {
        super(rankingRates);
    }

    @Override
    public void countRank(Rankable<Place> rankable) {

       /* Set<Tag> userTasteTags = rankable.getUser().getUserPreferences().getTasteTags();
        Place place = rankable.getRankableObject();

        Set<Tag> placeTags = place.getTags();

        for (Tag placeTag : placeTags) {
            for (Tag userTasteTag : userTasteTags) {
                if (placeTag.equals(userTasteTag)) {
                    rankable.increaseRank(getTagRankingRate(userTasteTag));
                }
            }
        }

        Set<Tag> placeMenusItemsTags = getPlaceMenusItemsTags(place);
        for (Tag currentTag : placeMenusItemsTags) {
            for (Tag userTasteTag : userTasteTags) {
                if (currentTag.equals(userTasteTag)) {
                    rankable.increaseRank(BigDecimal.ONE);
                }
            }
        }

        proceed(rankable);*/
    }

    private Set<Tag> getPlaceMenusItemsTags(Place place) {

        Set<Tag> placeTags = new HashSet<>();

        Set<Menu> menus = place.getMenus();
        for (Menu menu : menus) {
            List<MenuItem> menuItems = menu.getMenuItems();
            for (MenuItem menuItem : menuItems) {
                Set<Tag> menuItemTags = menuItem.getTags();
                for (Tag menuItemTag : menuItemTags) {
                    placeTags.add(menuItemTag);
                }
            }
        }

        return placeTags;

       /* Set<Stream<Set<Tag>>> collect = place.getMenus().parallelStream()
                .map(menu -> menu.getMenuItems()
                        .parallelStream()
                        .map(menuItem -> menuItem.getTags()
                                .parallelStream()
                                .collect(Collectors.toSet())))
                .collect(Collectors.toSet());*/

    }
}
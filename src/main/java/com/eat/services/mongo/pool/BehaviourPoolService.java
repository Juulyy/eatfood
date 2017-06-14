package com.eat.services.mongo.pool;

import com.eat.models.b2b.MenuItem;
import com.eat.models.b2b.Place;
import com.eat.models.b2b.PlaceDetail;
import com.eat.models.b2c.AppUser;
import com.eat.models.common.Tag;
import com.eat.models.mongo.pool.BehaviourPool;
import com.eat.models.mongo.pool.PoolContextType;
import com.eat.models.mongo.pool.TagContext;
import com.eat.repositories.mongo.pool.BehaviourPoolRepository;
import com.eat.services.b2b.MenuItemService;
import com.eat.services.b2b.PlaceDetailService;
import com.eat.services.b2b.PlaceService;
import com.eat.services.b2c.AppUserService;
import com.eat.services.common.TagService;
import com.eat.utils.math.NumberRounder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.eat.models.mongo.pool.PoolContextRate.*;

@Slf4j
@Service
public class BehaviourPoolService {

    @Autowired
    private BehaviourPoolRepository repository;

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private TagContextService contextService;

    @Autowired
    private PlaceService placeService;

    @Autowired
    private MenuItemService menuItemService;

    @Autowired
    private PlaceDetailService placeDetailService;

    @Autowired
    private TagService tagService;

    @PostConstruct
    private void initialize() {
        try {

            List<BehaviourPool> pools = new ArrayList<>();

            if (repository.count() != 0) {
                destroyCollection();
            }

            List<AppUser> appUsers = appUserService.findAll();
            appUsers.forEach(appUser -> pools.add(createInitialPool(appUser)));
            save(pools);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public BehaviourPool createRefAndPrefPool(AppUser appUser) {
        return BehaviourPool.of()
                .sqlUserId(appUser.getId())
                .pool(contextService.tasteAndPrefTagContextFilling(appUser))
                .create();
    }

    public BehaviourPool createInitialPool(AppUser appUser) {
        return BehaviourPool.of()
                .sqlUserId(appUser.getId())
                .pool(contextService.initialTagContextFilling(appUser))
                .create();
    }

    @PreDestroy
    private void destroyCollection() {
        log.info("Behaviour_pool_stats collection cleared!");
        repository.deleteAll();
    }

    //    TODO tbd visited by plan impl
    /*public void planVisitedPlace...() {

    }*/

    public Set<Tag> findTopUserTags(Long userId) {
        return tagService.findAllByNames(findTopUserTagNames(userId));
    }

    public Set<ImmutablePair<Tag, Double>> findTopUserTagsWithRates(Long userId) {
        return tagService.findAllByNamesWithRates(findTopUserTagNamesWithRates(userId));
    }

    private List<TagContext> getSortedListByType(List<TagContext> list) {
        Collections.sort(list);
        return list;
    }

    private List<TagContext> getSortedListByType(Set<TagContext> set, PoolContextType type) {
        return set.stream()
                .filter(tagContext -> tagContext.getType().equals(type))
                .collect(Collectors.toList());
    }

    private List<String> findMostPopular(List<TagContext> list) {
        return list.stream()
                .filter(tagContext -> tagContext.getWeight() >= getCustomAverageWeight(list))
                .map(TagContext::getTagName)
                .collect(Collectors.toList());
    }

    private List<ImmutablePair<String, Double>> findMostPopularWithRates(List<TagContext> list) {
        return list.stream()
                .filter(tagContext -> tagContext.getWeight() >= getCustomAverageWeight(list))
                .map(tagContext -> new ImmutablePair<>(tagContext.getTagName(), tagContext.getWeight()))
                .collect(Collectors.toList());
    }

    private Set<ImmutablePair<String, Double>> findTopUserTagNamesWithRates(Long userId) {
        Set<ImmutablePair<String, Double>> topTagNames = new HashSet();

        BehaviourPool bp = findAllByUserSqlId(userId);
        Set<TagContext> pool = bp.getPool();

        List<TagContext> sortedMenuItems = getSortedListByType(pool, PoolContextType.MENU_ITEM);
        topTagNames.addAll(findMostPopularWithRates(sortedMenuItems));

        List<TagContext> sortedMenuItemGroups = getSortedListByType(pool, PoolContextType.MENU_ITEM_GROUP);
        topTagNames.addAll(findMostPopularWithRates(sortedMenuItemGroups));

        List<TagContext> sortedMenuItemCategories = getSortedListByType(pool, PoolContextType.MENU_ITEM_CATEGORY);
        topTagNames.addAll(findMostPopularWithRates(sortedMenuItemCategories));

        List<TagContext> sortedFeatures = getSortedListByType(pool, PoolContextType.FEATURE);
        topTagNames.addAll(findMostPopularWithRates(sortedFeatures));

        List<TagContext> sortedAtmospheres = getSortedListByType(pool, PoolContextType.ATMOSPHERE);
        topTagNames.addAll(findMostPopularWithRates(sortedAtmospheres));

        List<TagContext> sortedCuisines = getSortedListByType(pool, PoolContextType.CUISINE);
        topTagNames.addAll(findMostPopularWithRates(sortedCuisines));

        List<TagContext> sortedInteriors = getSortedListByType(pool, PoolContextType.INTERIOR);
        topTagNames.addAll(findMostPopularWithRates(sortedInteriors));

        List<TagContext> sortedMusic = getSortedListByType(pool, PoolContextType.MUSIC);
        topTagNames.addAll(findMostPopularWithRates(sortedMusic));

        List<TagContext> sortedPlaceTypes = getSortedListByType(pool, PoolContextType.PLACE_TYPE);
        topTagNames.addAll(findMostPopularWithRates(sortedPlaceTypes));

        /*List<TagContext> sortedCustomTags = getSortedListByType(pool, PoolContextType.CUSTOM);
        topTagNames.addAll(findMostPopularWithRates(sortedCustomTags));*/

        return topTagNames;
    }

    private Set<String> findTopUserTagNames(Long userId) {
        Set<String> topTagNames = new HashSet();

        BehaviourPool bp = findAllByUserSqlId(userId);
        Set<TagContext> pool = bp.getPool();

        List<TagContext> sortedMenuItems = getSortedListByType(pool, PoolContextType.MENU_ITEM);
        topTagNames.addAll(findMostPopular(sortedMenuItems));

        List<TagContext> sortedMenuItemGroups = getSortedListByType(pool, PoolContextType.MENU_ITEM_GROUP);
        topTagNames.addAll(findMostPopular(sortedMenuItemGroups));

        List<TagContext> sortedMenuItemCategories = getSortedListByType(pool, PoolContextType.MENU_ITEM_CATEGORY);
        topTagNames.addAll(findMostPopular(sortedMenuItemCategories));

        List<TagContext> sortedFeatures = getSortedListByType(pool, PoolContextType.FEATURE);
        topTagNames.addAll(findMostPopular(sortedFeatures));

        List<TagContext> sortedAtmospheres = getSortedListByType(pool, PoolContextType.ATMOSPHERE);
        topTagNames.addAll(findMostPopular(sortedAtmospheres));

        List<TagContext> sortedCuisines = getSortedListByType(pool, PoolContextType.CUISINE);
        topTagNames.addAll(findMostPopular(sortedCuisines));

        List<TagContext> sortedInteriors = getSortedListByType(pool, PoolContextType.INTERIOR);
        topTagNames.addAll(findMostPopular(sortedInteriors));

        List<TagContext> sortedMusic = getSortedListByType(pool, PoolContextType.MUSIC);
        topTagNames.addAll(findMostPopular(sortedMusic));

        List<TagContext> sortedPlaceTypes = getSortedListByType(pool, PoolContextType.PLACE_TYPE);
        topTagNames.addAll(findMostPopular(sortedPlaceTypes));

        /*List<TagContext> sortedCustomTags = getSortedListByType(pool, PoolContextType.CUSTOM);
        topTagNames.addAll(findMostPopular(sortedCustomTags));*/

        return topTagNames;
    }

    private double getCustomAverageWeight(List<TagContext> list) {
        double weightSum = list.stream()
                .filter(Objects::nonNull)
                .map(TagContext::getWeight)
                .mapToDouble(weight -> weight)
                .sum();
        double average = weightSum / list.size();
        average = NumberRounder.roundToThousands(average);
        return average;
    }

    /*private Double getAverageWeight(List<TagContext> list) {
        return list.stream()
                .filter(Objects::nonNull)
                .map(TagContext::getWeight)
                .mapToDouble(weight -> weight).average().getAsDouble();
    }*/


    public void processAutocheckVisitedPlaceBehaviour(Long userId, Long placeId) {
        BehaviourPool bp = findAllByUserSqlId(userId);
        Place place = placeService.findById(placeId);
        if (bp != null && place != null) {
            place.getTags().stream()
                    .filter(Tag::isVisitedBehaviourTags)
                    .forEach(tagConsumer(bp, VISITED_PLACE_AUTOCHECK.getWeight()));

            place.getTags().stream()
                    .filter(Tag::isConcreteMenuCategoryTag)
                    .forEach(tagConsumer(bp, VISITED_PLACE_AUTOCHECK_MENU_CATEGORY.getWeight()));

            place.getTags().stream()
                    .filter(Tag::isConcreteMenuGroupTag)
                    .forEach(tagConsumer(bp, VISITED_PLACE_AUTOCHECK_MENU_CATEGORY.getWeight() / 2));

            update(bp);
        }
    }

    public void processAutocheckVisitedPlaceBehaviourDemoDecrement(Long userId, Long placeId) {
        BehaviourPool bp = findAllByUserSqlId(userId);
        Place place = placeService.findById(placeId);
        if (bp != null && place != null) {
            place.getTags().stream()
                    .filter(Tag::isVisitedBehaviourTags)
                    .forEach(tagConsumer(bp, - VISITED_PLACE_AUTOCHECK.getWeight()));

            place.getTags().stream()
                    .filter(Tag::isConcreteMenuCategoryTag)
                    .forEach(tagConsumer(bp, - VISITED_PLACE_AUTOCHECK_MENU_CATEGORY.getWeight()));

            place.getTags().stream()
                    .filter(Tag::isConcreteMenuGroupTag)
                    .forEach(tagConsumer(bp, - VISITED_PLACE_AUTOCHECK_MENU_CATEGORY.getWeight() / 2));

            update(bp);
        }
    }

    private Consumer<Tag> tagConsumer(BehaviourPool bp, Double rate) {
        return tag -> {
            Optional<TagContext> context = bp.getPool()
                    .stream()
                    .filter(tagContext -> tagContext.getTagName().equals(tag.getName()))
                    .findAny();
            if (context.isPresent()) {
                context.get().setWeight(context.get().getWeight() + rate);
            } else {
                bp.getPool().add(TagContext.of()
                        .tagName(tag.getName())
                        .weight(rate)
                        .type(contextService.getContextType(tag))
                        .create());
            }
        };
    }

    public void processPlaceRecBehaviour(Long userId, Long placeId) {
        BehaviourPool bp = findAllByUserSqlId(userId);
        Place place = placeService.findById(placeId);
        if (bp != null && place != null) {
            place.getTags().stream()
                    .filter(Tag::isRecPlaceBehaviourTags)
                    .forEach(tagConsumer(bp, REC_PLACE.getWeight()));

            place.getTags().stream()
                    .filter(Tag::isConcreteMenuCategoryTag)
                    .forEach(tagConsumer(bp, REC_PLACE_MENU_ITEM.getWeight()));

            place.getTags().stream()
                    .filter(Tag::isConcreteMenuGroupTag)
                    .forEach(tagConsumer(bp, REC_PLACE_MENU_ITEM.getWeight() / 2));

            update(bp);
        }
    }

    public void resetBehaviour(Long userId) {
        AppUser appUser = appUserService.findById(userId);
        BehaviourPool bp = findAllByUserSqlId(userId);
        if (bp != null && appUser != null) {
            bp.getPool().clear();
            bp.getPool().addAll(contextService.tasteAndPrefTagContextFilling(appUser));
            update(bp);
        }
    }

    public void processPlaceRecBehaviourDemoDecrement(Long userId, Long placeId) {
        BehaviourPool bp = findAllByUserSqlId(userId);
        Place place = placeService.findById(placeId);
        if (bp != null && place != null) {
            place.getTags().stream()
                    .filter(Tag::isRecPlaceBehaviourTags)
                    .forEach(tagConsumer(bp, - REC_PLACE.getWeight()));

            place.getTags().stream()
                    .filter(Tag::isConcreteMenuCategoryTag)
                    .forEach(tagConsumer(bp, - REC_PLACE_MENU_ITEM.getWeight()));

            place.getTags().stream()
                    .filter(Tag::isConcreteMenuGroupTag)
                    .forEach(tagConsumer(bp, - REC_PLACE_MENU_ITEM.getWeight() / 2));

            update(bp);
        }
    }

    public void processMenuItemRecBehaviour(Long userId, Long menuItemId) {
        BehaviourPool bp = findAllByUserSqlId(userId);
        MenuItem menuItem = menuItemService.findById(menuItemId);
        if (bp != null && menuItem != null) {
            /*menuItem.getTags().stream()
                    .filter(Tag::isConcreteMenuItemTag)
                    .forEach(tagConsumer(bp, REC_MENU_ITEM.getWeight()));*/

            menuItem.getTags().stream()
                    .filter(Tag::isConcreteMenuCategoryTag)
                    .forEach(tagConsumer(bp, REC_MENU_ITEM.getWeight()));

            menuItem.getTags().stream()
                    .filter(Tag::isConcreteMenuGroupTag)
                    .forEach(tagConsumer(bp, REC_MENU_ITEM.getWeight() / 2));

            update(bp);
        }
    }

    public void processMenuItemRecBehaviourDemoDecrement(Long userId, Long menuItemId) {
        BehaviourPool bp = findAllByUserSqlId(userId);
        MenuItem menuItem = menuItemService.findById(menuItemId);
        if (bp != null && menuItem != null) {
            /*menuItem.getTags().stream()
                    .filter(Tag::isConcreteMenuItemTag)
                    .forEach(tagConsumer(bp, REC_MENU_ITEM.getWeight()));*/

            menuItem.getTags().stream()
                    .filter(Tag::isConcreteMenuCategoryTag)
                    .forEach(tagConsumer(bp, - REC_MENU_ITEM.getWeight()));

            menuItem.getTags().stream()
                    .filter(Tag::isConcreteMenuGroupTag)
                    .forEach(tagConsumer(bp, - REC_MENU_ITEM.getWeight() / 2));

            update(bp);
        }
    }

    public void processFeatureRecBehaviour(Long userId, Long featureId) {
        BehaviourPool bp = findAllByUserSqlId(userId);
        PlaceDetail feature = placeDetailService.findById(featureId);
        if (bp != null && feature != null) {
            Optional<TagContext> context = bp.getPool().stream()
                    .filter(tagContext -> tagContext.getTagName().equals(feature.getName()))
                    .findAny();
            if (context.isPresent()) {
                context.get().setWeight(context.get().getWeight() + REC_FEATURE.getWeight());
            } else {
                bp.getPool().add(TagContext.of()
                        .tagName(feature.getName())
                        .weight(REC_FEATURE.getWeight())
                        .type(PoolContextType.FEATURE)
                        .create());
            }
        }
    }

    public void processPlaceFollowBehaviour(Long userId, Long placeId) {
        BehaviourPool bp = findAllByUserSqlId(userId);
        Place place = placeService.findById(placeId);
        if (bp != null && place != null) {
            place.getTags().stream()
                    .filter(Tag::isVisitedBehaviourTags)
                    .forEach(tagConsumer(bp, FOLLOWED_PLACE.getWeight()));

            place.getTags().stream()
                    .filter(Tag::isConcreteMenuCategoryTag)
                    .forEach(tagConsumer(bp, FOLLOWED_PLACE_MENU_ITEM.getWeight()));

            place.getTags().stream()
                    .filter(Tag::isConcreteMenuGroupTag)
                    .forEach(tagConsumer(bp, FOLLOWED_PLACE_MENU_ITEM.getWeight() / 2));

            update(bp);
        }
    }

//    TODO tbd not interesting impl
    /*public void notInterestedIn...() {

    }*/

    public BehaviourPool findById(String id) {
        return repository.findOne(id);
    }

    public BehaviourPool save(BehaviourPool pool) {
        return repository.save(pool);
    }

    public void save(List<BehaviourPool> pools) {
        repository.save(pools);
    }

    public BehaviourPool update(BehaviourPool pool) {
        return repository.save(pool);
    }

    public void delete(BehaviourPool pool) {
        repository.delete(pool);
    }

    public void delete(String id) {
        repository.delete(id);
    }

    public List<BehaviourPool> findAll() {
        return repository.findAll();
    }

    public BehaviourPool findAllByUserSqlId(Long userId) {
        return repository.findBySqlUserId(userId);
    }

}
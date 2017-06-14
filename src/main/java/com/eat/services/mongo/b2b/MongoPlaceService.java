package com.eat.services.mongo.b2b;

import com.clearspring.analytics.util.Lists;
import com.eat.models.b2b.Place;
import com.eat.models.common.Tag;
import com.eat.models.mongo.MongoPlace;
import com.eat.models.recommender.SuggestionCategory;
import com.eat.repositories.mongo.b2b.MongoPlaceRepository;
import com.eat.services.b2b.PlaceService;
import com.eat.utils.converters.MongoPlaceConverter;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Slf4j
@Service
public class MongoPlaceService {

    @Autowired
    private MongoPlaceRepository mongoPlaceRepository;

    @Autowired
    private MongoOperations mongoOperations;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private PlaceService placeService;

    @Autowired
    private MongoPlaceConverter mongoPlaceConverter;

    /**
     * Notice:
     * This method must invoke only before updateSuggestionCategories in SuggestionCategoryPool
     */
    @PostConstruct
    private void fillCollection() {
        try {
            mongoPlaceRepository.deleteAll();

            List<Place> places = placeService.findAll();

            List<MongoPlace> mongoPlaces = places.stream()
                    .map(mongoPlaceConverter::toMongoPlace)
                    .collect(Collectors.toList());

            save(mongoPlaces);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public List<MongoPlace> findAllBySqlEntityIdIn(List<Long> ids) {
        return mongoPlaceRepository.findAllBySqlEntityIdIn(ids);
    }

    public MongoPlace findById(String id) {
        return mongoPlaceRepository.findOne(id);
    }

    public MongoPlace findBySqlEntityId(Long id) {
        return mongoPlaceRepository.findBySqlEntityId(id);
    }

    public MongoPlace save(MongoPlace mongoPlace) {
        return mongoPlaceRepository.save(mongoPlace);
    }

    public List<MongoPlace> save(List<MongoPlace> mongoPlaces) {
        return mongoPlaceRepository.save(mongoPlaces);
    }

    public void update(MongoPlace mongoPlace) {
        Gson gson = new Gson();

        Query query = query(where("place_id").is(mongoPlace.getId()));
        Update update = Update.fromDBObject(BasicDBObject.parse(gson.toJson(mongoPlace)));

        mongoTemplate.updateFirst(query, update, MongoPlace.class);
    }

    public Double getDistance(double lat1, double lat2, double lon1, double lon2) {

        final int R = 6371;

        Double latDistance = Math.toRadians(lat2 - lat1);
        Double lonDistance = Math.toRadians(lon2 - lon1);
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

//        double height = el1 - el2;
        double height = 0;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }

    /*public List<MongoPlace> findAllMapPinsByLocation(Circle circle) {
        Query query = new Query();
        query.fields().include("location");
        query.addCriteria(Criteria.where("location").withinSphere(circle));
        return mongoTemplate.find(query, MongoPlace.class, "ranked_places");
    }*/

    public void update(List<MongoPlace> mongoPlaces) {

        mongoPlaces.forEach(mongoPlace -> update(mongoPlace));
    }

    public void delete(MongoPlace mongoPlace) {
        mongoPlaceRepository.delete(mongoPlace);
    }

    public void delete(String id) {
        mongoPlaceRepository.delete(id);
    }

    public List<MongoPlace> findAll() {
        return mongoPlaceRepository.findAll();
    }

    public List<MongoPlace> findByLocationWithin(Circle circle) {
        return mongoPlaceRepository.findByLocationWithin(circle);
    }

    public List<MongoPlace> findByLocationWithin(Circle circle, List<Long> ids) {
        return mongoPlaceRepository.findByLocationWithinAndSqlEntityIdIn(circle, ids);
    }

    public List<MongoPlace> findByLocationNearAndSqlEntityIdIn(Point point, Distance distance, List<Long> ids) {
        return mongoPlaceRepository.findByLocationNearAndSqlEntityIdIn(point, distance, ids);
    }

    public List<MongoPlace> findAllByContainingText(String text) {
        return mongoPlaceRepository.findAllByOrderByScoreDesc(TextCriteria.forDefaultLanguage().matchingAny(text));
    }

    public List<MongoPlace> findByAllFieldsContainsTextWithDefaultWeight(String... strings) {
        Query query = TextQuery.queryText(TextCriteria.forDefaultLanguage().matchingAny(strings)).sortByScore();
        return mongoOperations.find(query, MongoPlace.class);
    }

    public List<MongoPlace> getPlacesByCategoryOptions(SuggestionCategory category) {

        /*Optional<DayTimePeriod> dateTimePeriod = category.getDateTimePeriodFor();
        if (!dateTimePeriod.isPresent()) {
            return Lists.newArrayList();
        }*/

        List<AggregationOperation> operations = getAggregationOperationsBySuggestionCategory(category);

        List<MongoPlace> places;
        places = getAggregationResult(operations);

        return places;
    }

    public Optional<MongoPlace> getPlaceByCategoryOptionsAndSqlId(SuggestionCategory category, Long placeId) {

        /*Optional<DayTimePeriod> dateTimePeriod = category.getDateTimePeriodFor(dateTime);
        if (!dateTimePeriod.isPresent()) {
            return Optional.empty();
        }*/

        List<AggregationOperation> operations = getAggregationOperationsBySuggestionCategory(category);
        operations.add(match(where("sql_entity_id").is(placeId)));

        List<MongoPlace> places = getAggregationResult(operations);
        if (places.size() > 0) {
            return Optional.of(places.get(0));
        } else {
            return Optional.empty();
        }
    }

    public List<MongoPlace> getPlacesByCategoryOptionsAndSqlIds(SuggestionCategory category, List<Long> placeIds) {

        /*Optional<DayTimePeriod> dateTimePeriod = category.getDateTimePeriodFor(dateTime);
        if (!dateTimePeriod.isPresent()) {
            return Lists.newArrayList();
        }*/

        List<AggregationOperation> operations = getAggregationOperationsBySuggestionCategory(category);
        operations.add(match(where("sql_entity_id").in(placeIds)));

        List<MongoPlace> places;
        places = getAggregationResult(operations);

        return places;
    }

    private List<AggregationOperation> getAggregationOperationsBySuggestionCategory(SuggestionCategory category) {

        List<AggregationOperation> operations = Lists.newArrayList();

        if (category.isUseTagFilter() && !category.getTags().isEmpty()) {
            operations.add(match(TextCriteria.forDefaultLanguage()
                    .matchingAny(category.getTagNamesAsString())));
        }

//        Set<Pair<LocalTime, LocalTime>> timePeriods = category.getDateTimePeriodsWithoutDays();

//        TODO open times checking for category shown times, add check for day
/*        Criteria workingTimesCriteria = where("schedule.openFrom");
        int i = 0;
        for (Pair<LocalTime, LocalTime> timePeriod : timePeriods) {
            if (i > 0) {
                workingTimesCriteria.orOperator(where("schedule.openFrom")
                        .gte(timePeriod.getKey().getHour() + timePeriod.getKey().getMinute() / 100)
                        .and("schedule.openTo")
                        .lte(timePeriod.getValue().getHour() + timePeriod.getValue().getMinute() / 100));
            } else {
                workingTimesCriteria.gte(timePeriod.getKey().getHour() + timePeriod.getKey().getMinute() / 100)
                        .and("schedule.openTo").lte(
                        timePeriod.getValue().getHour() + timePeriod.getValue().getMinute() / 100);
            }
            i++;
        }*/

//        operations.add(match(workingTimesCriteria));

        if (category.getMinCuratorRecommendations() != null && category.getMinCuratorRecommendations() > 0) {
            operations.add(match(where("curator_recommendations").gte(category.getMinCuratorRecommendations())));
        }

        return operations;
    }

    private List<MongoPlace> getAggregationResult(List<AggregationOperation> operations) {

        List<MongoPlace> places;
        if (!operations.isEmpty()) {
            Aggregation aggregation = newAggregation(operations);
            AggregationResults groupResults = mongoTemplate.aggregate(aggregation, MongoPlace.class, MongoPlace.class);
            places = Lists.newArrayList(groupResults.getMappedResults());
        } else {
            places = Lists.newArrayList();
        }
        return places;
    }

    public List<MongoPlace> findByTags(List<Tag> tags) {
        List<MatchOperation> operations = new ArrayList<>();
        List<String> strings = tags.stream()
                .map(tag -> tag.getName().toLowerCase())
                .collect(Collectors.toList());
        strings.forEach(phrase -> operations.add(match(TextCriteria.forDefaultLanguage().matchingPhrase(phrase))));
        Aggregation aggregation = newAggregation(operations);
        AggregationResults groupResults = mongoTemplate.aggregate(aggregation, MongoPlace.class, MongoPlace.class);
        return groupResults.getMappedResults();
    }

}
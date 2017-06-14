package com.eat.services.elasticsearch.recommender;

import com.eat.configs.databases.ElasticsearchConfiguration;
import com.eat.models.b2b.Place;
import com.eat.models.b2c.AppUser;
import com.eat.models.common.Tag;
import com.eat.models.elasticsearch.ESPlace;
import com.eat.models.elasticsearch.ESUser;
import com.eat.models.elasticsearch.recommender.ESUserFavouritePlace;
import com.eat.services.b2b.PlaceService;
import com.eat.services.b2c.AppUserService;
import com.eat.services.elasticsearch.client.ESClient;
import com.eat.utils.math.NumberRounder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.termsQuery;

@Component
@NoArgsConstructor
@Scope("singleton")
public class ESRecommenderService implements NumberRounder {

    @Autowired
    private ESClient esClient;

    @Autowired
    private AppUserService userService;

    @Autowired
    private PlaceService placeService;

    private ObjectMapper mapper = new ObjectMapper();

    public void indexNewUserFavouritePlace(ESUserFavouritePlace place) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        RestTemplate restTemplate = new RestTemplate();
        StringBuilder urlBuilder = new StringBuilder();
        HttpEntity<String> entity = new HttpEntity<String>(mapper.writeValueAsString(place));
        urlBuilder.append("http://" + ElasticsearchConfiguration.SERVER_IP + ":9200/");
        urlBuilder.append(ElasticsearchConfiguration.INDEX_NAME + "/");
        urlBuilder.append("_taste/event");
        restTemplate.postForEntity(urlBuilder.toString(), entity, String.class);
    }

    public void recomputeSimilarPlaces() {
        RestTemplate restTemplate = new RestTemplate();
        StringBuilder urlBuilder = new StringBuilder();
        HttpEntity<String> entity = new HttpEntity<String>(readFile("elasticsearch.files/ItemRecommender.json",
                StandardCharsets.UTF_8));
        urlBuilder.append("http://" + ElasticsearchConfiguration.SERVER_IP + ":9200/");
        urlBuilder.append("_taste/action/recommended_items_from_item");
        restTemplate.postForObject(urlBuilder.toString(), entity, String.class);
    }

    public void recomputeSimilarUsers() {
        RestTemplate restTemplate = new RestTemplate();
        StringBuilder urlBuilder = new StringBuilder();
        HttpEntity<String> entity = new HttpEntity<String>(readFile("elasticsearch.files/SimilarUsers.json",
                StandardCharsets.UTF_8));
        urlBuilder.append("http://" + ElasticsearchConfiguration.SERVER_IP + ":9200/");
        urlBuilder.append("_taste/action/similar_users");
        restTemplate.postForObject(urlBuilder.toString(), entity, String.class);
    }

    public void recomputeRecomendationsFromUsers() {
        RestTemplate restTemplate = new RestTemplate();
        StringBuilder urlBuilder = new StringBuilder();
        HttpEntity<String> entity = new HttpEntity<String>(readFile("elasticsearch.files/RecommendItemsFromUsers.json",
                StandardCharsets.UTF_8));
        urlBuilder.append("http://" + ElasticsearchConfiguration.SERVER_IP + ":9200/");
        urlBuilder.append("_taste/action/recommended_items_from_user");
        restTemplate.postForObject(urlBuilder.toString(), entity, String.class);
    }

    public Map<AppUser, Double> getSimilarUsers(long userId) {
        Map<AppUser, Double> result = new HashMap();
        JSONObject temp = null;
        JSONArray tempArray = null;
        SearchHits hits = esClient.getClient().prepareSearch(ElasticsearchConfiguration.INDEX_NAME)
                .setTypes("user_similarity")
                .setQuery(QueryBuilders.matchQuery("user_id", userId))
                .execute().actionGet().getHits();

        SearchHit hit = hits.getAt(0);

        try {
            temp = new JSONObject(hit.getSourceAsString());
            tempArray = temp.getJSONArray("users");
            for (int i = 0; i < tempArray.length(); i++) {
                JSONObject obj = tempArray.getJSONObject(i);
                AppUser tempPlace = userService.findById(obj.getLong("user_id"));
                result.put(tempPlace, round(obj.getDouble("value")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return userSort(result);
    }

    public Map<Place, Double> getSimilarPlaces(long placeId) {
        Map<Place, Double> result = new HashMap();
        JSONObject temp = null;
        JSONArray tempArray = null;
        SearchHits hits = esClient.getClient().prepareSearch(ElasticsearchConfiguration.INDEX_NAME)
                .setTypes("item_similarity")
                .setQuery(QueryBuilders.matchQuery("item_id", placeId))
                .execute().actionGet().getHits();

        SearchHit hit = hits.getAt(0);

        try {
            temp = new JSONObject(hit.getSourceAsString());
            tempArray = temp.getJSONArray("items");
            for (int i = 0; i < tempArray.length(); i++) {
                JSONObject obj = tempArray.getJSONObject(i);
                Place tempPlace = placeService.findById(obj.getLong("item_id"));
                result.put(tempPlace, obj.getDouble("value"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    public Map<Place, Double> getRecommendedPlaces(long userId) {
        Map<Place, Double> result = new HashMap();
        JSONObject temp = null;
        JSONArray tempArray = null;
        SearchHits hits = esClient.getClient().prepareSearch(ElasticsearchConfiguration.INDEX_NAME)
                .setTypes("recommendation")
                .setQuery(QueryBuilders.matchQuery("user_id", userId))
                .execute().actionGet().getHits();
        SearchHit hit = hits.getAt(0);

        try {
            temp = new JSONObject(hit.getSourceAsString());
            tempArray = temp.getJSONArray("items");
            for (int i = 0; i < tempArray.length(); i++) {
                JSONObject obj = tempArray.getJSONObject(i);
                Place tempPlace = placeService.findById(obj.getLong("item_id"));
                result.put(tempPlace, round(obj.getDouble("value")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return placeSort(result);
    }

    public Map<Place, Double> getSimpleMatchRecommendations(long userId) {
        Map<Place, Double> result = new HashMap();
        AppUser user = userService.findById(userId);
        List<String> tags = new ArrayList<>();
        for (Tag tag : user.getUserPreferences().getTasteTags())
            tags.add(tag.getName());

        SearchHits hits = esClient.getClient().prepareSearch(ElasticsearchConfiguration.INDEX_NAME)
                .setTypes(ElasticsearchConfiguration.PLACES_TYPE_NAME)
                .setQuery(QueryBuilders.disMaxQuery()
                        .add(termsQuery("tags", tags))
                        .boost(1.2f)
                        .tieBreaker(0.7f))
                .setSize(150)
                .execute().actionGet().getHits();

        for (SearchHit hit : hits) {
            ESPlace place1;
            try {
                place1 = mapper.readValue(hit.getSourceAsString(), ESPlace.class);
                result.put(placeService.findById(place1.getId()), round((double) hit.getScore()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return placeSort(result);
    }

    public Map<Place, Double> getSimpleMatchPlaces(long placeId) {
        Map<Place, Double> result = new HashMap();
        int count = 0;
        Place place = placeService.findById(placeId);
        List<String> tags = new ArrayList<>();
        for (Tag tag : place.getTags())
            tags.add(tag.getName());

        SearchHits hits = esClient.getClient().prepareSearch(ElasticsearchConfiguration.INDEX_NAME)
                .setTypes(ElasticsearchConfiguration.PLACES_TYPE_NAME)
                .setQuery(QueryBuilders.disMaxQuery()
                        .add(termsQuery("tags", tags))
                        .boost(1.2f)
                        .tieBreaker(0.7f))
                .execute().actionGet().getHits();


        for (SearchHit hit : hits) {
            count++;
            ESPlace place1;
            try {
                place1 = mapper.readValue(hit.getSourceAsString(), ESPlace.class);
                result.put(placeService.findById(place1.getId()), (double) hit.getScore());
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (count == 10)
                break;
        }

        return result;
    }

    public Map<AppUser, Double> getSimpleMatchUsers(long userId) {
        Map<AppUser, Double> result = new HashMap();
        int count = 0;
        AppUser user = userService.findById(userId);
        List<String> tags = new ArrayList<>();
        for (Tag tag : user.getUserPreferences().getTasteTags())
            tags.add(tag.getName());

        SearchHits hits = esClient.getClient().prepareSearch(ElasticsearchConfiguration.INDEX_NAME)
                .setTypes(ElasticsearchConfiguration.USERS_TYPE_NAME)
                .setQuery(QueryBuilders.disMaxQuery()
                        .add(termsQuery("tags", tags))
                        .boost(1.2f)
                        .tieBreaker(0.7f))
                .execute().actionGet().getHits();


        for (SearchHit hit : hits) {
            count++;
            ESUser user1;
            try {
                user1 = mapper.readValue(hit.getSourceAsString(), ESUser.class);
                result.put(userService.findById(user1.getId()), (double) hit.getScore());
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (count == 10)
                break;
        }

        return result;
    }

    public Map<Place, ImmutablePair<Double, Double>> filterRecommendedPlaces(Map<Place, Double> simpleMap, Map<Place, Double> reMap) {
        HashMap<Place, ImmutablePair<Double, Double>> result = new HashMap<>();
        reMap.forEach((place, ratio) -> {
            if (simpleMap.containsKey(place)) {
                result.put(place, new ImmutablePair<>(ratio, simpleMap.get(place)));
            } else {
                result.put(place, new ImmutablePair<>(ratio, 0d));
            }
        });
        return recommendedPlacesSort(result);
    }

    private Map<Place, ImmutablePair<Double, Double>> recommendedPlacesSort(Map<Place, ImmutablePair<Double, Double>> map) {
        return map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
    }

    private Map<AppUser, Double> userSort(Map<AppUser, Double> hashMap) {
        return hashMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
    }

    private Map<Place, Double> placeSort(Map<Place, Double> hashMap) {
        return hashMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (d1, d2) -> d2, LinkedHashMap::new));
    }

    public void recomputeAll() {
        recomputeRecomendationsFromUsers();
        recomputeSimilarPlaces();
        recomputeSimilarUsers();
    }

    private String readFile(String path, Charset encoding) {
        String result = "";
        try {
            JSONObject object = new JSONObject(Resources.toString(Resources.getResource(path), encoding));
            result = object.toString();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
}

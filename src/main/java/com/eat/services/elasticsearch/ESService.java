package com.eat.services.elasticsearch;

import com.eat.configs.databases.ElasticsearchConfiguration;
import com.eat.models.elasticsearch.ESAbstractObject;
import com.eat.models.elasticsearch.ESPlace;
import com.eat.models.elasticsearch.ESUser;
import com.eat.services.elasticsearch.client.ESClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

@Component
@NoArgsConstructor
@Scope("singleton")
public class ESService {

    @Autowired
    private ESClient esClient;

    private ObjectMapper mapper = new ObjectMapper();

    /**
     * This method is used to find merchant IDs by {@link String } query
     * @param tag single-word String query
     * @return list of places
     */
    public <T extends ESAbstractObject> List<?> searchObjectByTag(String tag, Class<T> clazz) {

        List<ESAbstractObject> result = new ArrayList<>();

        SearchHits hits = esClient.getClient().prepareSearch(ElasticsearchConfiguration.INDEX_NAME)
                .setTypes(getTypeName(clazz))
                .setQuery(QueryBuilders.matchQuery("tags", tag).fuzziness("AUTO"))
                .execute().actionGet().getHits();

        for (SearchHit hit : hits) {
            ESAbstractObject object;
            try {
                object = mapper.readValue(hit.getSourceAsString(), ESPlace.class);
                result.add(clazz.cast(object));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    /**
     * This method is used to find users IDs by {@link String } query
     * @param tags list of String query
     * @return list of users
     */
    public <T extends ESAbstractObject> List<?> searchByTags(List<String> tags, Class<T> clazz) {

        List<ESAbstractObject> result = new ArrayList<>();

        SearchHits hits = esClient.getClient().prepareSearch(ElasticsearchConfiguration.INDEX_NAME)
                .setTypes(getTypeName(clazz))
                .setQuery(QueryBuilders.matchQuery("tags", tags).fuzziness("AUTO"))
                .execute().actionGet().getHits();

        for (SearchHit hit : hits) {
            ESAbstractObject object;
            try {
                object = mapper.readValue(hit.getSourceAsString(), ESAbstractObject.class);
                result.add(clazz.cast(object));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    /**
     * This method is used to find place by {@link String } userId
     * @param id String primary key from database
     * @return object of ESUser
     */
    public < T extends ESAbstractObject> T searchById(String id, Class<T> clazz) {

        SearchHit hit = esClient.getClient().prepareSearch(ElasticsearchConfiguration.INDEX_NAME)
                .setTypes(getTypeName(clazz))
                .setQuery(QueryBuilders.matchQuery(getIdName(clazz), id))
                .execute().actionGet().getHits().getAt(0);

        ESAbstractObject result = null;
        try {
            result = mapper.readValue(hit.getSourceAsString(), clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return clazz.cast(result);
    }

    /**
     * This method is used to delete place by {@link String } placeId
     * @param object ESAbstractObject item
     */
    public void deleteObject(ESAbstractObject object, Class clazz) {
        SearchHit hit = esClient.getClient().prepareSearch(ElasticsearchConfiguration.INDEX_NAME)
                .setTypes(getTypeName(clazz))
                .setQuery(QueryBuilders.matchQuery(getIdName(clazz), object.getId()))
                .execute().actionGet().getHits().getAt(0);

        esClient.getClient().prepareDelete(ElasticsearchConfiguration.INDEX_NAME, getTypeName(clazz), hit.getId()).execute();
    }

    /**
     * This method is used to index new objects
     * @param object ESAbstractObject item
     * @throws IOException
     */
    public void indexNewObject(ESAbstractObject object, Class clazz) throws IOException {
        IndexResponse response = esClient.getClient().prepareIndex(ElasticsearchConfiguration.INDEX_NAME, getTypeName(clazz))
                .setSource(jsonBuilder()
                        .startObject()
                        .field(getIdName(clazz), object.getId())
                        .field("tags", object.getTags())
                        .endObject())
                .get();
    }

    /**
     * This method is used to update info of a single object
     * @param object ESAbstractObject item
     * @throws IOException
     */
    public void updateObjectInfo(ESAbstractObject object, Class clazz) throws IOException {

        SearchHit hit = esClient.getClient().prepareSearch(ElasticsearchConfiguration.INDEX_NAME)
                .setTypes(getTypeName(clazz))
                .setQuery(QueryBuilders.matchQuery(getIdName(clazz), object.getId()))
                .execute().actionGet().getHits().getAt(0);

        esClient.getClient().prepareIndex(ElasticsearchConfiguration.INDEX_NAME, getTypeName(clazz), hit.getId())
                .setSource(jsonBuilder()
                        .startObject()
                        .field("tags", object.getTags())
                        .field(getIdName(clazz), object.getId())
                        .endObject())
                .get();
    }

    private String getTypeName(Class clazz) {
        String TYPE_NAME = "";
        if (clazz == ESPlace.class)
                TYPE_NAME = ElasticsearchConfiguration.PLACES_TYPE_NAME;

        if (clazz == ESUser.class)
                TYPE_NAME = ElasticsearchConfiguration.USERS_TYPE_NAME;
        return TYPE_NAME;
    }

    private String getIdName(Class clazz) {
        String ID_NAME = "";
        if (clazz == ESPlace.class)
            ID_NAME = "place_id";

        if (clazz == ESUser.class)
            ID_NAME = "user_id";
        return ID_NAME;
    }
}

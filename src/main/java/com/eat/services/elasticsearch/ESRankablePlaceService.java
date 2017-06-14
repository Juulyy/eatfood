package com.eat.services.elasticsearch;

import com.eat.models.elasticsearch.ESRankablePlace;
import com.eat.repositories.elasticsearch.PlaceESRepository;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.lucene.search.function.CombineFunction;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.Collection;

import static org.elasticsearch.index.query.QueryBuilders.multiMatchQuery;

@Slf4j
@Service
public class ESRankablePlaceService {

    @Autowired
    private PlaceESRepository placeESRepository;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    public Collection<ESRankablePlace> getPlacesNameMatching(String name) {
        return placeESRepository.findPlaceByNameNear(name);
    }

    public ESRankablePlace save(ESRankablePlace place) {
        log.info("Try to save place...");
        return placeESRepository.save(place);
    }

    public Collection<ESRankablePlace> getPlacesWithRatings(){


        String queryText = "\"query\": {\n" +
                "    \"function_score\": {\n" +
                "        \"query\": {\n" +
                "            \"multi_match\": {\n" +
                "                \"query\": \"Lambda Expressions\",\n" +
                "                \"fields\": [\"placeDetailTags^0.8\", \"menuItemsTags^0.6\"]\n" +
                "            }\n" +
                "        },\n" +
                "        \"functions\": [\n" +
                "          #geolocation  " +
                "        ]\n" +
                "    }\n" +
                "}";

        String geolocation = "{\n" +
                "          \"gauss\": {\n" +
                "            \"coordinates\": {\n" +
                "              \"origin\": {\n" +
                "                \"lat\": 48.8582,\n" +
                "                \"lon\": 2.2945\n" +
                "              },\n" +
                "              \"offset\": \"2km\",\n" +
                "              \"scale\": \"4km\"\n" +
                "            }\n" +
                "          }";
        queryText = queryText.replaceAll("#geolocation", geolocation);

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(multiMatchQuery("tutorial")
                        .field("placeDetailTags")
                        .field("menuItemsTags")
                        .type(MultiMatchQueryBuilder.Type.BEST_FIELDS))
                .build();

        Page<ESRankablePlace> places =  elasticsearchTemplate.queryForPage(searchQuery, ESRankablePlace.class);

        MultiMatchQueryBuilder multiMatchQuery = multiMatchQuery(queryText,
                "placeDetailTags^0.8", "menuItemsTags^0.6")
                .type(MultiMatchQueryBuilder.Type.BEST_FIELDS);

        FunctionScoreQueryBuilder functionScoreQuery = QueryBuilders.functionScoreQuery(multiMatchQuery);
        functionScoreQuery.scoreMode("multiply");
        functionScoreQuery.boostMode(CombineFunction.MULT);
        functionScoreQuery.add(ScoreFunctionBuilders.gaussDecayFunction("location","5km").setOffset("2km"));

        return null;
    }
}
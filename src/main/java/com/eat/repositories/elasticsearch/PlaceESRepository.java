package com.eat.repositories.elasticsearch;

import com.eat.models.elasticsearch.ESRankablePlace;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public interface PlaceESRepository extends ElasticsearchRepository<ESRankablePlace, String>{

    @Query("{\"query\": {\"fuzzy\": {\"place_name\": \"?0\"}}}")
    Collection<ESRankablePlace> findPlaceByNameNear(@Param("name") String name);

    /*@RestResource(path = "/api/find-by-tag")
    Page<ESRankablePlace> findPlaceByTags(@Param("tag") String tag);

    @RestResource(path = "/api/find-by-tag-contains")
    Page<ESRankablePlace> findPlaceByTagsContains(@Param("tag") String tag);

    @RestResource(path = "/api/find-by-name-near")
    @Query("{\"query\": {\"fuzzy\": {\"place_name\": \"?0\"}}}")
    Page<ESRankablePlace> findPlaceByNameNear(@Param("name") String name);*/

}

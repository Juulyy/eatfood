package com.eat.models.elasticsearch;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "of", buildMethodName = "create")
@Document(indexName = "rank_place", type = "rank_place", shards = 1, replicas = 0)
public class ESRankablePlace {

    @JsonProperty(value = "place_id")
    @Id
    private String id;

    @JsonProperty(value = "place_name")
    @Field(type = FieldType.String)
    private String name;

    @Field(type = FieldType.Object)
    private GeoPoint location;

    @Singular
    @JsonProperty(value = "place_tags")
//    @Field(type = FieldType.Nested)
    @Field
    private Set<String> tags;

    @Singular
    @JsonProperty(value = "place_detail_tags")
//    @Field(type = FieldType.Nested)
    @Field
    private Set<String> placeDetailTags;

    @Singular
    @JsonProperty(value = "menu_item_tags")
//    @Field(type = FieldType.Nested)
    @Field
    private Set<String> menuItemsTags;

    @Singular
    @JsonProperty(value = "atmospheres")
//    @Field(type = FieldType.Nested)
    @Field
    private Set<String> atmospheres;

    @Singular
    @JsonProperty(value = "musics")
//    @Field(type = FieldType.Nested)
    @Field
    private Set<String> musics;

    @Singular
    @JsonProperty(value = "interiors")
//    @Field(type = FieldType.Nested)
    @Field
    private Set<String> interiors;

    @Singular
    @JsonProperty(value = "features")
//    @Field(type = FieldType.Nested)
    @Field
    private Set<String> features;

    @Singular
    @JsonProperty(value = "cuisines")
//    @Field(type = FieldType.Auto)
    @Field
    private Set<String> cuisines;

    @Singular
    @JsonProperty(value = "place_types")
//    @Field(type = FieldType.Nested)
    @Field
    private Set<String> placeTypes;

    @Singular
    @JsonProperty(value = "additional_options")
    @Field
//    @Field(type = FieldType.Nested)
    private Set<String> additionalOptions;

}

package com.eat.models.mongo;

import com.eat.models.common.Tag;
import com.eat.models.common.enums.TagType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.TextScore;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder(builderMethodName = "of", buildMethodName = "create")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"sqlEntityId", "name", "location"})
@ToString(of = {"name", "location", "placeDetailTags"})
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "ranked_places")
public class MongoPlace implements Serializable {

    private static final long serialVersionUID = 9001465648994860784L;

    @JsonProperty(value = "place_id")
    @Id
    private String id;

    @NotNull
    @JsonProperty(value = "sql_entity_id")
    @Indexed(name = "sql_entity_id", unique = true)
    private Long sqlEntityId;

    @NotNull
    @JsonProperty(value = "place_name")
//    @TextIndexed(weight = 3)
    @Field(value = "name")
    private String name;

    @NotNull
    @JsonProperty(value = "location")
    @GeoSpatialIndexed(name = "location")
    private double[] location;

    @JsonProperty(value = "place_tags")
    @TextIndexed(weight = 2)
    @Field(value = "place_tags")
    private Set<String> tags;

    @JsonProperty(value = "place_detail_tags")
    @Field(value = "place_detail_tags")
    private Set<String> placeDetailTags;

    @JsonProperty(value = "menu_item_tags")
    @Field(value = "menu_item_tags")
    private Set<String> menuItemsTags;

    @JsonProperty(value = "atmospheres")
    @Field(value = "atmospheres")
    private Set<String> atmospheres;

    @JsonProperty(value = "musics")
    @Field(value = "musics")
    private Set<String> musics;

    @JsonProperty(value = "interiors")
    @Field(value = "interiors")
    private Set<String> interiors;

    @JsonProperty(value = "features")
    @Field(value = "features")
    private Set<String> features;

    @JsonProperty(value = "cuisines")
    @Field(value = "cuisines")
    private Set<String> cuisines;

    @JsonProperty(value = "place_types")
    @Field(value = "place_types")
    private Set<String> placeTypes;

    @JsonProperty(value = "allergies")
    @Field(value = "allergies")
    private Set<String> allergies;

    @JsonProperty(value = "diets")
    @Field(value = "diets")
    private Set<String> diets;

    @JsonProperty(value = "additional_options")
    @Field(value = "additional_options")
    private Set<String> additionalOptions;

    @JsonProperty(value = "schedule")
    @Field(value = "schedule")
    private List<MongoDayOpenTime> schedule;

    @JsonProperty(value = "curators_recommendations")
    @Field(value = "curator_recommendations")
    private Integer curatorRecommendations;

    @TextScore
    private Float score;

    public boolean containsTag(Tag tag) {

        if (tag.getType() == TagType.CUISINE) {
            return getCuisines().contains(tag.getName().toLowerCase());
        } else if (tag.getType() == TagType.MENU_ITEM
                || tag.getType() == TagType.MENU_ITEM_GROUP
                || tag.getType() == TagType.MENU_ITEM_CATEGORY) {

            return getMenuItemsTags().contains(tag.getName().toLowerCase());
        } else if (tag.getType() == TagType.ATMOSPHERE) {

            return getAtmospheres().contains(tag.getName().toLowerCase());
        } else if (tag.getType() == TagType.FEATURE) {

            return getFeatures().contains(tag.getName().toLowerCase());
        } else if (tag.getType() == TagType.MUSIC) {

            return getMusics().contains(tag.getName().toLowerCase());
        } else if (tag.getType() == TagType.CUISINE) {

            return getCuisines().contains(tag.getName().toLowerCase());
        } else if (tag.getType() == TagType.PLACE_TYPE) {

            return getPlaceTypes().contains(tag.getName().toLowerCase());
        } else if (tag.getType() == TagType.ALLERGY) {

            return getAllergies().contains(tag.getName().toLowerCase());
        } else if (tag.getType() == TagType.DIET) {

            return getDiets().contains(tag.getName().toLowerCase());
        } else {
            return false;
        }

    }

}
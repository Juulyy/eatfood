package com.eat.dto.recommender;

import com.eat.dto.b2b.PlaceMobileDto;
import com.eat.models.common.Image;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.NonNull;

import java.util.List;

@Builder(builderMethodName = "of", buildMethodName = "create")
public class SuggestionCategoryMobileDto {

    @NonNull
    @JsonProperty(value = "id")
    public final Long id;

//    @NonNull
    @JsonProperty(value = "image")
    public final Image image;

    @NonNull
    @JsonProperty(value = "name")
    public final String name;

    @NonNull
    @JsonProperty(value = "places")
    public List<PlaceMobileDto> places;

}
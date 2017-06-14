package com.eat.dto.demo;

import com.eat.dto.recommender.SuggestionCategoryMobileDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder(builderMethodName = "of", buildMethodName = "create")
public class DemoSuggestionCategoryDto {

    @JsonProperty(value = "currentCategories")
    public final List<SuggestionCategoryMobileDto> currentCategories;

    @JsonProperty(value = "futureCategories")
    public final List<SuggestionCategoryMobileDto> futureCategories;

}
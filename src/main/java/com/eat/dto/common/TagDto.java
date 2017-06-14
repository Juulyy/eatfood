package com.eat.dto.common;

import com.eat.models.common.enums.TagType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder(builderMethodName = "of", buildMethodName = "create")
public class TagDto {

    @NonNull
    @JsonProperty(value = "id")
    private final Long id;

    @NonNull
    @JsonProperty(value = "name")
    private final String name;

    @NonNull
    @JsonProperty(value = "type")
    private final TagType type;

}
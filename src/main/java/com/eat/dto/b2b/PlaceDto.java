package com.eat.dto.b2b;

import com.eat.models.b2b.enums.BusinessPlan;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonInclude.*;

@Value
@Builder(builderMethodName = "of", buildMethodName = "create")
public class PlaceDto {

    @NonNull
    @JsonProperty(value = "id")
    private final Long id;

    @NonNull
    @JsonProperty(value = "isActive")
    private final Boolean isActive;

    @NonNull
    @JsonProperty(value = "name")
    private final String name;

    @NonNull
    @JsonProperty(value = "businessPlan")
    private final BusinessPlan businessPlan;

    @NonNull
    @JsonProperty(value = "b2bAdminLoginNames")
    private final Set<String> b2bAdminLoginNames;

    @NonNull
    @JsonProperty(value = "creationDate")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private final LocalDateTime creationDate;

    @NonNull
    @JsonProperty(value = "usersRecCount")
    private final Integer usersRecCount;

    @NonNull
    @JsonProperty(value = "curatorsRecCount")
    private final Integer curatorsRecCount;

    @NonNull
    @JsonProperty(value = "visitsCount")
    private final Integer visitsCount;

    @JsonInclude(value = Include.NON_NULL)
    @JsonProperty(value = "rank")
    private final Double rank;

}
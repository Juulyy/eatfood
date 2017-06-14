package com.eat.dto.demo;

import com.eat.models.common.Tag;
import com.eat.models.mongo.enums.WeatherIcon;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalTime;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "of", buildMethodName = "create")
@JsonIgnoreProperties(ignoreUnknown = true)
public class DemoAppParamDto {

    @NotNull
    @JsonProperty(value = "time")
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    public LocalTime time;

    @NotNull
    @JsonProperty("temp")
    public Double temp;

    @NotNull
    @JsonProperty("icon")
    public WeatherIcon icon;

    @JsonProperty("longtitude")
    public Double longtitude;

    @JsonProperty("latitude")
    public Double latitude;

    @NotNull
    @JsonProperty("tags")
    public Set<Tag> tags;

}
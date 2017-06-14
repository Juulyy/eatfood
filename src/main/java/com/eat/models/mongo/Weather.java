package com.eat.models.mongo;

import com.eat.models.mongo.enums.WeatherIcon;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import lombok.*;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder(builderMethodName = "of", buildMethodName = "create")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "id")
@ToString(exclude = "id")
@Document(collection = "weather_reports")
@ApiModel(value = "weather data")
public class Weather implements Serializable {

    private static final long serialVersionUID = 1422089768928236149L;

    @JsonIgnore
    @Id
    private String id;

    @JsonIgnore
    @Field(value = "summary")
    private String summary;

    @JsonIgnore
    @Field(value = "icon")
    private WeatherIcon icon;

    @JsonIgnore
    @Field(value = "precipIntensity")
    private Double precipIntensity;

    @Field(value = "precipType")
    private String precipType;

    @Field(value = "temp")
    private Double temp;

    @Field(value = "apparentTemp")
    private Double apparentTemp;

    @Field(value = "dewPoint")
    private Double dewPoint;

    @Field(value = "windSpeed")
    private Double windSpeed;

    @Field(value = "pressure")
    private Double pressure;

    @JsonIgnore
    @Field(value = "cloudCover")
    private Double cloudCover;

    @JsonIgnore
    @Field(value = "timeZone")
    private String timeZone;

    @JsonIgnore
    @GeoSpatialIndexed(name = "location")
    private double[] location;

    @JsonIgnore
    @Field(value = "systemDateTime")
    private LocalDateTime systemDateTime;

}
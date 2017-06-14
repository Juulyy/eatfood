package com.eat.models.recommender;

import com.eat.models.mongo.enums.WeatherIcon;
import com.eat.utils.converters.WeatherIconSetConverter;
import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;
import java.util.Set;

@ApiModel("Weather options for suggestion categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "of", buildMethodName = "create")
@Embeddable
public class WeatherOption {

    @Column(name = "FROM_TEMPERATURE")
    private Double fromTemperature;

    @Column(name = "TO_TEMPERATURE")
    private Double toTemperature;

    @Column(name = "USE_WEATHER_FILTER")
    private boolean useTempFilter;

    @Singular
    @Basic
    @Convert(converter = WeatherIconSetConverter.class)
    @Column(name = "WEATHER_CONDITIONS")
    private Set<WeatherIcon> icons;

    @Column(name = "USE_WEATHER_ICONS_FILTER")
    private boolean useWeatherIconsFilter;

}

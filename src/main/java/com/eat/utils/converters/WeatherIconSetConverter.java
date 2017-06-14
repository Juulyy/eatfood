package com.eat.utils.converters;

import com.eat.models.mongo.enums.WeatherIcon;
import org.apache.directory.api.util.Strings;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Converter(autoApply = true)
public class WeatherIconSetConverter implements AttributeConverter<Set<WeatherIcon>, String> {

    @Override
    public String convertToDatabaseColumn(Set<WeatherIcon> attribute) {
        if (Optional.ofNullable(attribute).isPresent()) {
            return attribute.stream().map(WeatherIcon::getIcon).collect(Collectors.joining(", "));
        }
        return null;
    }

    @Override
    public Set<WeatherIcon> convertToEntityAttribute(String dbData) {
        if (Strings.isEmpty(dbData)) {
            return null;
        }
        Set<String> names = new HashSet<>(Arrays.asList(dbData.split(", ")));
        Set<WeatherIcon> result = new HashSet<>();
        names.forEach(s -> result.add(WeatherIcon.valueOf(s.toUpperCase().replace("-", "_"))));
        return result;
    }

}
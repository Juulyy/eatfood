package com.eat.utils.converters;

import com.eat.models.b2c.AppUserSettings;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Converter(autoApply = true)
public class TransitTypeListConverter implements AttributeConverter<List<AppUserSettings.TransitType>, String> {

    @Override
    public String convertToDatabaseColumn(List<AppUserSettings.TransitType> attribute) {
        if (attribute == null) {
            return null;
        }
        List<String> names = new ArrayList<>();
        attribute.forEach(transitType -> names.add(transitType.name()));
        return String.join(", ", names);
    }

    @Override
    public List<AppUserSettings.TransitType> convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        List<String> names = new ArrayList<>(Arrays.asList(dbData.split(", ")));
        List<AppUserSettings.TransitType> result = new ArrayList<>();
        names.forEach(s -> result.add(AppUserSettings.TransitType.valueOf(s)));
        return result;
    }

}
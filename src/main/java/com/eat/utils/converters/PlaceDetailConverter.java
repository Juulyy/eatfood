package com.eat.utils.converters;

import com.eat.models.b2b.PlaceDetail;
import com.eat.models.b2b.enums.PlaceDetailType;
import com.eat.models.common.Tag;
import com.eat.models.common.enums.TagType;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PlaceDetailConverter {

    public Tag toTag(PlaceDetail placeDetail) {
        return Tag.of()
                .name(placeDetail.getName())
                .type(toTagType(placeDetail.getPlaceDetailType()))
                .create();
    }

    public TagType toTagType(PlaceDetailType detailType) {
        switch (detailType) {
            case CUISINE:
                return TagType.CUISINE;
            case PLACE_TYPE:
                return TagType.PLACE_TYPE;
            case OPTION:
                return TagType.OPTION;
            case ATMOSPHERE:
                return TagType.ATMOSPHERE;
            case INTERIOR:
                return TagType.INTERIOR;
            case FEATURE:
                return TagType.FEATURE;
            case MUSIC:
                return TagType.MUSIC;
            case CUSTOM:
                return TagType.CUSTOM;
            default:
                return null;
        }
    }

}
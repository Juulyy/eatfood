package com.eat.dto.b2b;

import com.eat.models.b2b.PlaceDetail;
import com.eat.models.b2b.enums.PriceLevel;
import com.eat.models.common.Image;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.NonNull;

@Builder(builderMethodName = "of", buildMethodName = "create")
public final class PlaceMobileDto {

    @NonNull
    @JsonProperty(value = "id")
    public final Long id;

    @JsonProperty(value = "image")
    public final Image image;

    @NonNull
    @JsonProperty(value = "name")
    public final String name;

    @NonNull
    @JsonProperty(value = "placeType")
    public final PlaceDetail placeType;

    @NonNull
    @JsonProperty(value = "priceLevel")
    public final PriceLevel priceLevel;

    @NonNull
    @JsonProperty(value = "longtitude")
    public final Double longtitude;

    @NonNull
    @JsonProperty(value = "latitude")
    public final Double latitude;

}
package com.eat.models.mongo;

import com.eat.models.b2b.Contact;
import com.eat.models.b2b.PlaceDetail;
import com.eat.models.b2b.Schedule;
import com.eat.models.b2b.enums.BusinessPlan;
import com.eat.models.b2b.enums.PriceLevel;
import com.eat.models.common.Image;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import java.util.Set;

@ApiModel("Request from b2b user for place page info changing")
@Builder(builderMethodName = "of", buildMethodName = "create")
@Data
@Value
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlacePageChangeRequest extends AbstractPlaceChangeRequest {

    private static final long serialVersionUID = -7883589855676416284L;

    @NotNull
    @JsonProperty(value = "name")
    @Field(value = "name")
    private String name;

    @NotNull
    @JsonProperty(value = "longtitude")
    @Field(value = "longtitude")
    private Double longtitude;

    @NotNull
    @JsonProperty(value = "latitude")
    @Field(value = "latitude")
    private Double latitude;

    @NotNull
    @JsonProperty(value = "contacts")
    @Field(value = "contacts")
    private Set<Contact> contacts;

    @NotNull
    @JsonProperty(value = "schedule")
    @Field(value = "schedule")
    private Schedule schedule;

    @NotNull
    @JsonProperty(value = "placeDetails")
    @Field(value = "placeDetails")
    private Set<PlaceDetail> placeDetails;

    @NotNull
    @JsonProperty(value = "images")
    @Field(value = "images")
    private Set<Image> images;

    @NotNull
    @JsonProperty(value = "priceLevel")
    @Field(value = "priceLevel")
    private PriceLevel priceLevel;

    @NotNull
    @JsonProperty(value = "businessPlan")
    @Field(value = "businessPlan")
    private BusinessPlan businessPlan;

}
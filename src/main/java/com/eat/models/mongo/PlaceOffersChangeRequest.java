package com.eat.models.mongo;

import com.eat.models.b2b.Offer;
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

@ApiModel("Request from b2b user for place offers content changing")
@Builder(builderMethodName = "of", buildMethodName = "create")
@Data
@Value
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaceOffersChangeRequest extends AbstractPlaceChangeRequest {

    private static final long serialVersionUID = -2659457414457055076L;

    @NotNull
    @JsonProperty(value = "offers")
    @Field(value = "offers")
    private Set<Offer> offers;

}
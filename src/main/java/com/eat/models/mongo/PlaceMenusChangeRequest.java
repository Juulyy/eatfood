package com.eat.models.mongo;

import com.eat.models.b2b.Menu;
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

@ApiModel("Request from b2b user for place menus content changing")
@Builder(builderMethodName = "of", buildMethodName = "create")
@Data
@Value
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaceMenusChangeRequest extends AbstractPlaceChangeRequest {

    private static final long serialVersionUID = 2728105439548357762L;

    @NotNull
    @JsonProperty(value = "offers")
    @Field(value = "offers")
    private Set<Menu> menus;

}
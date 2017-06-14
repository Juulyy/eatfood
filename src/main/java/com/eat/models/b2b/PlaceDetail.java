package com.eat.models.b2b;

import com.eat.models.b2b.enums.PlaceDetailType;
import com.eat.models.common.Image;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Builder(builderMethodName = "of", buildMethodName = "create")
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "id")
@EqualsAndHashCode(exclude = "id")
@Entity
@Table(name = "PLACE_DETAIL")
@ApiModel("Details of places such as modern for interior design, rock for music, warm for atmosphere etc.")
public class PlaceDetail implements Serializable {

    private static final long serialVersionUID = -3799609321659107150L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DETAIL_ID")
    @ApiModelProperty(value = "ID in the Database", position = 1)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", nullable = false)
    @ApiModelProperty(value = "type of place detail", allowableValues = "music, interior design, atmosphere, features", position = 2)
    private PlaceDetailType placeDetailType;

    @Column(name = "NAME", nullable = false, unique = true)
    @ApiModelProperty(value = "detail's type names", allowableValues = "modern, country, crowded, radio, electronic", position = 3)
    private String name;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "IMAGE", referencedColumnName = "IMAGE_ID")
    private Image image;

    @JsonIgnore
    public boolean isFittedPlaceType(PlaceDetailType type) {
        return getPlaceDetailType() == type;
    }

    @JsonIgnore
    public boolean isCuisine() {
        switch (getPlaceDetailType()) {
            case CUISINE:
                return true;
            default:
                return false;
        }
    }

    @JsonIgnore
    public boolean isPlaceType() {
        switch (getPlaceDetailType()) {
            case PLACE_TYPE:
                return true;
            default:
                return false;
        }
    }

    @JsonIgnore
    public boolean isAtmosphere() {
        switch (getPlaceDetailType()) {
            case ATMOSPHERE:
                return true;
            default:
                return false;
        }
    }

    @JsonIgnore
    public boolean isInterior() {
        switch (getPlaceDetailType()) {
            case INTERIOR:
                return true;
            default:
                return false;
        }
    }

    @JsonIgnore
    public boolean isFeature() {
        switch (getPlaceDetailType()) {
            case FEATURE:
                return true;
            default:
                return false;
        }
    }

    @JsonIgnore
    public boolean isMusic() {
        switch (getPlaceDetailType()) {
            case MUSIC:
                return true;
            default:
                return false;
        }
    }

    @JsonIgnore
    public boolean isCustom() {
        switch (getPlaceDetailType()) {
            case CUSTOM:
                return true;
            default:
                return false;
        }
    }

    @JsonIgnore
    public boolean isOption() {
        switch (getPlaceDetailType()) {
            case OPTION:
                return true;
            default:
                return false;
        }
    }

}
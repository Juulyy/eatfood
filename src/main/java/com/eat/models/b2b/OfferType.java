package com.eat.models.b2b;

import com.eat.models.b2b.localization.LocalizedOfferAction;
import com.eat.models.b2b.localization.LocalizedOfferType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "of", buildMethodName = "create")
@ToString(exclude = "id")
@EqualsAndHashCode(exclude = "id")
@Entity
@Table(name = "OFFER_TYPE")
@ApiModel(value = "is the type of the offer type, for instance Discount, One Free, 50 % sale of for everything")
public class OfferType implements Serializable {

    private static final long serialVersionUID = 8864306033392965310L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OFFER_TYPE_ID")
    @ApiModelProperty(value = "the ID in the DB", allowableValues = "Long", position = 1)
    private Long id;

    @Column(name = "PHOTO_URL")
    @ApiModelProperty(value = "the URL of the photo where is going to be the advertisement of the offer", allowableValues = "String", position = 2)
    private String photoUrl;

    @Column(name = "NAME", nullable = false)
    @ApiModelProperty(value = "the of teh offer type", allowableValues = "String", position = 3)
    private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "offerType", fetch = FetchType.EAGER)
    private Set<LocalizedOfferType> localized;

}

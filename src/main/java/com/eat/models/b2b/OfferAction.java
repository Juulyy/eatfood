package com.eat.models.b2b;

import com.eat.models.b2b.localization.LocalizedOfferAction;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@ApiModel("is intended for determining the user intention for example Buy smth, Visit some place, Make a plan, etc.")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "of", buildMethodName = "create")
@ToString(exclude = "id")
@EqualsAndHashCode(exclude = "id")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "OFFER_ACTION")
public class OfferAction implements Serializable {

    private static final long serialVersionUID = -2720917544705392727L;

    @ApiModelProperty(value = "the ID in the DB", allowableValues = "Long", position = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OFFER_ACTION_ID")
    private Long id;

    @ApiModelProperty(value = "name of the user action intention" ,allowableValues = "String", position = 2)
    @Column(name = "NAME", nullable = false)
    private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "offerAction", fetch = FetchType.EAGER)
    private Set<LocalizedOfferAction> localized;

}

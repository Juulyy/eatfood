/*
package com.eat.models.b2b;

import com.eat.models.b2b.localization.LocalizedMealType;
import com.eat.models.b2b.localization.LocalizedOfferType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@ApiModel(value = "is the subcategory of menu item for instance salads, soup, pizza etc")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "of", buildMethodName = "create")
@EqualsAndHashCode(exclude = "id")
@ToString(exclude = "id")
@Entity
@Table(name = "MEAL_TYPE")
public class MealType implements Serializable {

    private static final long serialVersionUID = 5366971265561236029L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEAL_TYPE_ID")
    @ApiModelProperty(value = "is the ID in the DataBase", allowableValues = "Long", required = true, position = 1)
    private Long id;

    @Column(name = "NAME", nullable = false)
    @ApiModelProperty(value = "is the name of the type of meal", allowableValues = "String", required = true, position = 2)
    private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mealType", fetch = FetchType.EAGER)
    private Set<LocalizedMealType> localized;
}
*/

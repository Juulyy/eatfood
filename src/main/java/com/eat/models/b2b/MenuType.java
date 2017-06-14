package com.eat.models.b2b;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "of", buildMethodName = "create")
@ToString(exclude = "id")
@EqualsAndHashCode(exclude = "id")
@Entity
@Table(name = "MENU_TYPE")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@ApiModel(value = "is for e.g. main dishes, snacks, beverages, cold appetizer etc.")
public class MenuType implements Serializable {

    private static final long serialVersionUID = 6827175449882251107L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MENU_TYPE_ID")
    @ApiModelProperty(value = "is the ID in the DataBase", allowableValues = "Long", required = true, position = 1)
    private Long id;

    @Column(name = "NAME", nullable = false)
    @ApiModelProperty(value = "is the name of the menu type", allowableValues = "String", required = true, position = 2)
    private String name;

}
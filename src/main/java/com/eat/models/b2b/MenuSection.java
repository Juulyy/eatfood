package com.eat.models.b2b;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@EqualsAndHashCode(exclude = {"id"})
@ToString(exclude = {"id"})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@ApiModel(value = "menu sections")
@Entity
@Table(name = "MENU_SECTION")
public class MenuSection implements Serializable {

    private static final long serialVersionUID = 1502873430085496149L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MENU_SECTION_ID")
    @ApiModelProperty(value = "is the ID in the DataBase", allowableValues = "Long", required = true, position = 1)
    private Long id;

    @Column(name = "NAME", nullable = false)
    @ApiModelProperty(value = "is the title of the menu", allowableValues = "String", required = true, position = 2)
    private String name;

    @JsonBackReference(value = "place_menu_section")
    @ManyToOne
    @JoinColumn(name = "MENU_FK", referencedColumnName = "MENU_ID")
    private Menu menu;

}
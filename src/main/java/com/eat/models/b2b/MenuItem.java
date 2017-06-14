package com.eat.models.b2b;

import com.eat.models.common.Tag;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@ApiModel(value = "is the menu item (dish) each menu containing")
@Getter
@Setter
@Builder(builderMethodName = "of", buildMethodName = "create")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"id", "tags"})
@ToString(exclude = {"id", "tags"})
@Entity
@Table(name = "MENU_ITEM")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MenuItem implements Serializable {

    private static final long serialVersionUID = 2772786191148947378L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MENU_ITEM_ID")
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    /*@ApiModelProperty(value = "is type of meal or category of meal such as soup, salad etc", required = true)
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "MEAL_TYPE_FK", referencedColumnName = "MEAL_TYPE_ID")
    private MealType mealType;*/

//    TODO resolve problem
    @JsonBackReference(value = "place_menu_item")
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "MENU_FK")
    private Menu menu;

    @ApiModelProperty("is list of menu sections which contain current menu item")
    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany
    @JoinTable(name = "MENU_ITEM_CATEGORY", joinColumns = @JoinColumn(name = "MENU_ITEM_FK"),
            inverseJoinColumns = @JoinColumn(name = "MENU_SECTION_FK"))
    private Set<MenuSection> menuSection;

    @ApiModelProperty(value = "is the price of the dish", allowableValues = "Double", required = true)
    @Column(name = "PRICE")
    private Double price;

    @Column(name = "DESCRIPTION")
    @ApiModelProperty(value = "is the detailed comments about the menu position", allowableValues = "String")
    private String description;

    @Basic
    @Column(name = "IS_SPICY")
    private Boolean isSpicy;

    @ApiModelProperty(value = "is the list of menu item tags", allowableValues = "String")
    @Singular
    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany
    @JoinTable(name = "MENU_ITEM_TAGS", joinColumns = @JoinColumn(name = "MENU_ITEM_FK"),
            inverseJoinColumns = @JoinColumn(name = "TAG_FK"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"MENU_ITEM_FK", "TAG_FK"}, name = "menu_item_tag_uk"))
    private Set<Tag> tags;

}
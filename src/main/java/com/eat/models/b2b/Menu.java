package com.eat.models.b2b;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "of", buildMethodName = "create")
@EqualsAndHashCode(exclude = {"id", "menuItems", "place"})
@ToString(exclude = {"id", "menuItems", "place"})
@Entity
@Table(name = "MENU")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"}, ignoreUnknown = true)
@ApiModel(value = "is the list of dishes in concrete place")
public class Menu implements Serializable {

    private static final long serialVersionUID = 4402723433385459149L;

    @JsonProperty(value = "id", access = JsonProperty.Access.AUTO)
    @ApiModelProperty(value = "is the ID in the DataBase", allowableValues = "Long", required = true, position = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MENU_ID")
    private Long id;

//    TODO resolve problem
    @JsonProperty(value = "place", access = JsonProperty.Access.AUTO, required = true)
    @JsonBackReference(value = "place_menu")
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "PLACE_FK")
    private Place place;

    @JsonProperty(value = "name", access = JsonProperty.Access.READ_WRITE)
    @ApiModelProperty(value = "is the title of the menu", allowableValues = "String", required = true, position = 2)
    @Column(name = "NAME", nullable = false)
    private String name;

    @JsonProperty(value = "description", access = JsonProperty.Access.READ_WRITE)
    @ApiModelProperty(value = "is the more detailed information about Menu",
            allowableValues = "String", required = true, position = 3)
    @Column(name = "DESCRIPTION")
    private String description;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "MENU_TYPE_FK", referencedColumnName = "MENU_TYPE_ID")
    @JsonProperty(value = "menuType", access = JsonProperty.Access.READ_WRITE)
    private MenuType menuType;

    @JsonManagedReference(value = "place_menu_section")
    @NotFound(action = NotFoundAction.IGNORE)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "menu")
    @JsonProperty(value = "menuSection", access = JsonProperty.Access.READ_WRITE)
    private List<MenuSection> menuSection;

    @JsonManagedReference(value = "place_menu_item")
    @NotFound(action = NotFoundAction.IGNORE)
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "menu")
    @JsonProperty(value = "menuItems", access = JsonProperty.Access.READ_WRITE)
    private List<MenuItem> menuItems;

}
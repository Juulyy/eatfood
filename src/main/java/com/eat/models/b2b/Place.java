package com.eat.models.b2b;

import com.eat.models.b2b.enums.BusinessPlan;
import com.eat.models.b2b.enums.PriceLevel;
import com.eat.models.common.Image;
import com.eat.models.common.Tag;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder(builderMethodName = "of", buildMethodName = "create")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id", "name", "latitude", "longtitude", "menus", "schedule"})
@ToString(exclude = {"id", "offers", "menus", "schedule"})
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "PLACE")
@ApiModel(value = "where users are having intention to enjoy or otherwise in what they are not interested in")
public class Place implements Serializable {

    private static final long serialVersionUID = -2535002812204579812L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PLACE_ID")
    @ApiModelProperty(value = "the ID in the DB", position = 1)
    private Long id;

    @Column(name = "ACTIVE")
    @JsonProperty("isActive")
    @ApiModelProperty(value = "the effectiveness of the place", position = 2)
    private Boolean isActive;

    @Column(name = "NAME")
    @JsonProperty("name")
    @ApiModelProperty(value = "name of the place", position = 3)
    private String name;

    @Column(name = "LONGTITUDE")
    @JsonProperty("longtitude")
    @ApiModelProperty(value = "the longtitude where the place is situated in", position = 4)
    private Double longtitude;

    @Column(name = "LATITUDE")
    @JsonProperty("latitude")
    @ApiModelProperty(value = "the latitude where the place is situated in", position = 5)
    private Double latitude;

    @JsonManagedReference(value = "network")
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PLACE_NETWORK_FK", referencedColumnName = "PlACE_NETWORK_ID")
    private PlaceNetwork placeNetwork;

    @Singular
    @JsonManagedReference(value = "business_user")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "place")
    private List<BusinessUser> businessUsers;

    /**
     * Can edited only by Admin
     */
    @Column(name = "IS_LUXURY")
    @ApiModelProperty(value = "the signs of the luxury", allowableValues = "Boolean", required = true, position = 6)
    private boolean isLuxury;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinTable(name = "PLACE_CONTACTS", joinColumns = @JoinColumn(name = "PLACE_FK"),
            inverseJoinColumns = @JoinColumn(name = "CONTACTS_FK"))
    private Set<Contact> contacts;

    @JsonManagedReference(value = "place_menu")
    @NotFound(action = NotFoundAction.IGNORE)
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "place")
    private Set<Menu> menus;

    @JsonManagedReference(value = "place_schedule")
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "place")
    @JoinColumn(name = "SCHEDULE_FK", referencedColumnName = "SCHEDULE_ID")
    private Schedule schedule;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "PLACE_OPTIONS", joinColumns = @JoinColumn(name = "PLACE_FK"),
            inverseJoinColumns = @JoinColumn(name = "DETAIL_FK"))
    private Set<PlaceDetail> placeDetails;

    @JsonManagedReference(value = "place_offer")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "place")
    private Set<Offer> offers;

    @Fetch(value = FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "PLACE_TAGS", joinColumns = @JoinColumn(name = "PLACE_FK"),
            inverseJoinColumns = @JoinColumn(name = "TAG_FK"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"PLACE_FK", "TAG_FK"}, name = "place_tag_uk"))
    private Set<Tag> tags;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "PLACE_IMAGES", joinColumns = @JoinColumn(name = "PLACE_FK"),
            inverseJoinColumns = @JoinColumn(name = "IMAGE_FK"))
    private Set<Image> images;

    @Enumerated(EnumType.STRING)
    @Column(name = "PRICE_LEVEL")
    private PriceLevel priceLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "BUSINESS_PLAN")
    private BusinessPlan businessPlan;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "CREATION_DATE")
    private LocalDateTime creationDate;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "FREE_REG_DATE")
    private LocalDateTime freeRegDate;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "PAID_REG_DATE")
    private LocalDateTime paidRegDate;

    public Place dropAdditionalInfo() {

//        setBusinessUsers(null);
//        setMenus(null);
//        setTags(null);
//        setOffers(null);

        return this;
    }
}
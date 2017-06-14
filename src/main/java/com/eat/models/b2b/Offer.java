package com.eat.models.b2b;

import com.eat.models.common.Tag;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "of", buildMethodName = "create")
@ToString(exclude = {"id", "place"})
@EqualsAndHashCode(exclude = {"id", "place"})
@Entity
@Table(name = "OFFER")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "is the discount offer by which the user can visit the specified location at the specified time")
public class Offer implements Serializable {

    private static final long serialVersionUID = -2789973349187531835L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OFFER_ID")
    @ApiModelProperty(value = "the ID in the DB", allowableValues = "Long", position = 1)
    private Long id;

    @Column(name = "NAME")
    @ApiModelProperty(value = "the name of the offer", allowableValues = "String", position = 2)
    private String name;

    @JsonBackReference(value = "place_offer")
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PLACE_FK")
    private Place place;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "OFFER_MENU_ITEMS", joinColumns = @JoinColumn(name = "OFFER_FK"),
            inverseJoinColumns = @JoinColumn(name = "MENU_ITEM_FK"))
    private List<MenuItem> menuItems;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "EXPIRATION_DATE", nullable = false)
    private LocalDateTime expirationDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "OFFER_ACTION_FK", referencedColumnName = "OFFER_ACTION_ID")
    private OfferAction action;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "OFFER_TYPE_FK", referencedColumnName = "OFFER_TYPE_ID")
    private OfferType offerType;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    @ApiModelProperty(value = "the status of the offer: DRAFT, PENDING, CURRENT, PAST", position = 3)
    private OfferStatus status;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "OFFER_TAGS", joinColumns = @JoinColumn(name = "OFFER_FK"),
            inverseJoinColumns = @JoinColumn(name = "TAG_FK"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"OFFER_FK", "TAG_FK"}, name = "offer_tag_uk"))
    private List<Tag> tags;

    public enum OfferStatus {

        DRAFT("DRAFT"), PENDING("PENDING"), CURRENT("CURRENT"), PAST("PAST");

        @Getter
        private String status;

        OfferStatus(String status) {
            this.status = status;
        }

    }

}
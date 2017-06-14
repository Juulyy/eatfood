package com.eat.models.b2c;

import com.eat.models.b2b.MenuItem;
import com.eat.models.b2b.OfferType;
import com.eat.models.b2b.Place;
import com.eat.models.b2b.PlaceDetail;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@Builder(builderMethodName = "of", buildMethodName = "create")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "id")
@ToString(exclude = "id")
@Entity
@Table(name = "APP_USER_NOT_INTERESTING")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "user's collection of places, cuisines, offers, menu items doesn't like")
public class NotInteresting implements Serializable {

    private static final long serialVersionUID = -4574568093223439151L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NOT_INTERESTING_ID", updatable = false)
    private Long id;

    @JsonBackReference(value = "user_not_interesting")
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_PREFERENCE_FK", referencedColumnName = "USER_PREFERENCE_ID")
    private AppUserPreference appUserPreference;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "NOT_INTERESTING_PLACES", joinColumns = @JoinColumn(name = "NOT_INTERESTING_FK"),
            inverseJoinColumns = @JoinColumn(name = "PLACE_FK"))
    private Set<Place> places;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "NOT_INTERESTING_CUISINES", joinColumns = @JoinColumn(name = "NOT_INTERESTING_FK"),
            inverseJoinColumns = @JoinColumn(name = "CUISINE_FK"))
    private Set<PlaceDetail> cuisines;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "NOT_INTERESTING_OFFER_TYPES", joinColumns = @JoinColumn(name = "NOT_INTERESTING_FK"),
            inverseJoinColumns = @JoinColumn(name = "OFFER_TYPE_FK"))
    private Set<OfferType> offerTypes;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "NOT_INTERESTING_MENU_ITEMS", joinColumns = @JoinColumn(name = "NOT_INTERESTING_FK"),
            inverseJoinColumns = @JoinColumn(name = "MENU_ITEM_FK"))
    private Set<MenuItem> menuItems;

//    TODO add list of categoryCards

}

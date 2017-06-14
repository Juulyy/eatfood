package com.eat.models.b2c;

import com.eat.models.b2b.MenuItem;
import com.eat.models.b2b.Offer;
import com.eat.models.b2b.Place;
import com.eat.models.b2b.PlaceDetail;
import com.eat.models.b2c.enums.AppUserCollectionType;
import com.eat.models.b2c.enums.PrivacyType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@EqualsAndHashCode(exclude = {"id"})
@ToString(exclude = {"id"})
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "is intended to hold in the settings of the user the values for recommendation " +
        "e.g. Recommend Places, Recommend Offers etc.")
@Entity
@Table(name = "APP_USER_COLLECTION")
public class AppUserCollection implements Serializable {

    private static final long serialVersionUID = 32700889007686450L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COLLECTION_ID", updatable = false)
    private Long id;

    @JsonBackReference(value = "user_collection")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_PREFERENCE_FK", referencedColumnName = "USER_PREFERENCE_ID")
    private AppUserPreference appUserPreference;

    @Column(name = "COLLECTION_NAME", nullable = false)
    private String collectionName;

    @Enumerated(EnumType.STRING)
    @Column(name = "COLLECTION_PRIVACY")
    private PrivacyType collectionPrivacy;

    @Enumerated(EnumType.STRING)
    @Column(name = "COLLECTION_TYPE")
    private AppUserCollectionType collectionType;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "APP_USER_COLLECTIONS_OFFERS", joinColumns = @JoinColumn(name = "COLLECTION_FK"),
            inverseJoinColumns = @JoinColumn(name = "OFFER_FK"))
    private Set<Offer> offers;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "APP_USER_COLLECTIONS_PLACES", joinColumns = @JoinColumn(name = "COLLECTION_FK"),
            inverseJoinColumns = @JoinColumn(name = "PLACE_FK"))
    private Set<Place> places;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "APP_USER_COLLECTIONS_FEATURES", joinColumns = @JoinColumn(name = "COLLECTION_FK"),
            inverseJoinColumns = @JoinColumn(name = "FEATURE_FK"))
    private Set<PlaceDetail> features;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "APP_USER_COLLECTIONS_MENU_ITEMS", joinColumns = @JoinColumn(name = "COLLECTION_FK"),
            inverseJoinColumns = @JoinColumn(name = "MENU_ITEM_FK"))
    private Set<MenuItem> menuItems;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "APP_USER_FOLLOWING_COLLECTIONS", joinColumns = @JoinColumn(name = "COLLECTION_FK"),
            inverseJoinColumns = @JoinColumn(name = "FOLLOW_COLLECTION_FK"))
    private Set<AppUserCollection> followingCollections;

    @JsonIgnore
    public boolean isRecPlacesCollection() {
        switch (getCollectionType()) {
            case RECOMMENDED_PLACES:
                return true;
            default:
                return false;
        }
    }

    @JsonIgnore
    public boolean isFollowedPlacesCollection() {
        switch (getCollectionType()) {
            case FOLLOWED_PLACES:
                return true;
            default:
                return false;
        }
    }

    @JsonIgnore
    public boolean isRecMenuItemsCollection() {
        switch (getCollectionType()) {
            case RECOMMENDED_MENU_ITEMS:
                return true;
            default:
                return false;
        }
    }

    @JsonIgnore
    public boolean isRecFeaturesCollection() {
        switch (getCollectionType()) {
            case RECOMMENDED_FEATURES:
                return true;
            default:
                return false;
        }
    }

}
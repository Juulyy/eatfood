package com.eat.models.common;

import com.eat.models.common.enums.TagType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "of", buildMethodName = "create")
@EqualsAndHashCode(exclude = {"id", "parent", "childTags"})
@ToString(exclude = {"id", "parent", "childTags"})
@Entity
@Table(name = "TAGS", uniqueConstraints = @UniqueConstraint(columnNames = {"NAME", "TYPE"}))
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIgnoreType
@ApiModel(value = "is intended to mark as a keyword, the identifier for the categorization, description, " +
        "search data and set the internal structure.")
public class Tag implements Serializable {

    private static final long serialVersionUID = -2784316216033465193L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TAG_ID")
    @ApiModelProperty(value = "is the ID in the DataBase", allowableValues = "Long", required = true, position = 1)
    private Long id;

    @Column(name = "NAME")
    @ApiModelProperty(value = "is the name of the tag, e.g. cool interior, great coffee", allowableValues = "String",
            required = true, position = 2)
    private String name;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "TYPE")
    private TagType type;

    /**
     * all types exclude Group and Custom type
     */
//    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ManyToOne
    @JoinColumn(name = "PARENT")
    private Tag parent;

    /**
     * only Category Type have this field
     */
//    @JsonInclude(JsonInclude.Include.NON_NULL)
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Tag> childTags;

    @JsonIgnore
    public boolean isAtmosphereTag() {
        switch (getType()) {
            case ATMOSPHERE:
                return true;
            default:
                return false;
        }
    }

    @JsonIgnore
    public boolean isInteriorTag() {
        switch (getType()) {
            case INTERIOR:
                return true;
            default:
                return false;
        }
    }

    @JsonIgnore
    public boolean isMusicTag() {
        switch (getType()) {
            case MUSIC:
                return true;
            default:
                return false;
        }
    }

    @JsonIgnore
    public boolean isFeatureTag() {
        switch (getType()) {
            case FEATURE:
                return true;
            default:
                return false;
        }
    }

    @JsonIgnore
    public boolean isCuisineTag() {
        switch (getType()) {
            case CUISINE:
                return true;
            default:
                return false;
        }
    }

    @JsonIgnore
    public boolean isPlaceDetailTag() {
        switch (getType()) {
            case ATMOSPHERE:
                return true;
            case INTERIOR:
                return true;
            case MUSIC:
                return true;
            case FEATURE:
                return true;
            case CUISINE:
                return true;
            case PLACE_TYPE:
                return true;
            default:
                return false;
        }
    }

    @JsonIgnore
    public boolean isMenuItemTag() {
        switch (getType()) {
            case MENU_ITEM:
                return true;
            default:
                return false;
        }
    }

    @JsonIgnore
    public boolean isMenuItemCategoryTag() {
        switch (getType()) {
            case MENU_ITEM_CATEGORY:
                return true;
            default:
                return false;
        }

    }

    @JsonIgnore
    public boolean isMenuItemGroupTag() {
        switch (getType()) {
            case MENU_ITEM_GROUP:
                return true;
            default:
                return false;
        }

    }

    @JsonIgnore
    public boolean isPlaceTypeTag() {
        switch (getType()) {
            case PLACE_TYPE:
                return true;
            default:
                return false;
        }
    }

    @JsonIgnore
    public boolean isConcreteMenuItemTag() {
        switch (getType()) {
            case MENU_ITEM:
                return true;
            default:
                return false;
        }
    }

    @JsonIgnore
    public boolean isConcreteMenuCategoryTag() {
        switch (getType()) {
            case MENU_ITEM_CATEGORY:
                return true;
            default:
                return false;
        }
    }

    @JsonIgnore
    public boolean isConcreteMenuGroupTag() {
        switch (getType()) {
            case MENU_ITEM_GROUP:
                return true;
            default:
                return false;
        }
    }

    @JsonIgnore
    public boolean isVisitedBehaviourTags() {
        switch (getType()) {
            case CUISINE:
            case FEATURE:
            case ATMOSPHERE:
            case MUSIC:
            case INTERIOR:
            case PLACE_TYPE:
                return true;
            default:
                return false;
        }
    }

    @JsonIgnore
    public boolean isRecPlaceBehaviourTags() {
        switch (getType()) {
            case CUISINE:
            case FEATURE:
            case ATMOSPHERE:
            case MUSIC:
            case INTERIOR:
            case PLACE_TYPE:
                return true;
            default:
                return false;
        }
    }

    @JsonIgnore
    public boolean isMenuTag() {
        switch (getType()) {
            case MENU_ITEM_GROUP:
            case MENU_ITEM_CATEGORY:
            case MENU_ITEM:
                return true;
            default:
                return false;
        }
    }

    @JsonIgnore
    public boolean isAllergyTag() {
        switch (getType()) {
            case ALLERGY:
                return true;
            default:
                return false;
        }
    }

    @JsonIgnore
    public boolean isDietTag() {
        switch (getType()) {
            case DIET:
                return true;
            default:
                return false;
        }
    }

    public Set<Tag> getParentsTags(Tag tag, Set<Tag> tags) {

        if (tag.getParent() == null) {
            return tags;
        }
        tags.add(tag);

        return getParentsTags(tag.getParent(), tags);
    }

}
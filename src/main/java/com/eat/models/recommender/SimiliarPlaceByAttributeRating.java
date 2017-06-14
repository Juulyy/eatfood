package com.eat.models.recommender;

import com.eat.models.b2b.Place;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder(builderMethodName = "of", buildMethodName = "create")
@IdClass(value = SimiliarPlaceByAttributeRatingId.class)
@Entity
@Table(name = "SIMILIAR_PLACES")
public class SimiliarPlaceByAttributeRating implements Serializable{

    @Id
    @ManyToOne
    private Place place;

    @Id
    @ManyToOne
    private Place similiarTo;

    @Column
    private Integer similiarityRatio;
}

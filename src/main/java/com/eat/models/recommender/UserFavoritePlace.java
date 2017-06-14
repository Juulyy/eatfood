package com.eat.models.recommender;

import com.eat.models.b2b.Place;
import com.eat.models.b2c.AppUser;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = {"user", "place"})
@Builder(builderMethodName = "of", buildMethodName = "create")
@IdClass(value = UserFavoritePlaceId.class)
@Entity
@Table(name = "USER_FAVOURITE_PLACE")
public class UserFavoritePlace implements Serializable{

    @Id
    @ManyToOne
    private AppUser user;

    @Id
    @ManyToOne
    private Place place;

    @Column
    private Float preferenceRatio;
}

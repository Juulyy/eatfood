package com.eat.models.recommender;

import lombok.*;

import javax.persistence.Id;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class UserFavoritePlaceId implements Serializable{

    private Long user;

    private Long place;

}

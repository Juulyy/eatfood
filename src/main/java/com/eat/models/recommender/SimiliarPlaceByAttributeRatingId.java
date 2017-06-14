package com.eat.models.recommender;

import lombok.*;

import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class SimiliarPlaceByAttributeRatingId implements Serializable{

    private Long place;

    private Long similiarTo;

}

package com.eat.repositories.sql.recommender;

import com.eat.models.recommender.SimiliarPlaceByAttributeRating;
import com.eat.models.recommender.SimiliarPlaceByAttributeRatingId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SimiliarPlaceByAttributeRatingRepository extends JpaRepository<SimiliarPlaceByAttributeRating, SimiliarPlaceByAttributeRatingId> {
}

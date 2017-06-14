package com.eat.repositories.sql.recommender;

import com.eat.models.recommender.SuggestionCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SuggestionCategoryRepository extends JpaRepository<SuggestionCategory, Long> {

}

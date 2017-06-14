package com.eat.aspects.pool;

import com.eat.logic.b2c.SuggestionCategoryPool;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SuggestionCategoryPoolAspect {

    @Autowired
    private SuggestionCategoryPool suggestionCategoryPool;

    @After(value = "execution(* com.eat.services.recommender.SuggestionCategoryService.save(..)) ||" +
            "execution(* com.eat.services.recommender.SuggestionCategoryService.update(..)) ||" +
            "execution(* com.eat.services.recommender.SuggestionCategoryService.delete(..)) ||" +
            "execution(* com.eat.services.b2c.AppUserPreferenceService.updateForDemo(..)))")
    public void afterCategoriesChanges() {
        suggestionCategoryPool.updateSuggestionCategories();
    }

}
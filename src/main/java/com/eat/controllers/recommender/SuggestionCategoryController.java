package com.eat.controllers.recommender;

import com.eat.logic.manager.InheritanceTagRemover;
import com.eat.models.recommender.SuggestionCategory;
import com.eat.services.recommender.SuggestionCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/suggestion-category")
public class SuggestionCategoryController {

    @Autowired
    private SuggestionCategoryService categoryService;

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<SuggestionCategory> getRankingRates() {
        List<SuggestionCategory> categories = categoryService.findAll();
        categories.stream()
                .map(SuggestionCategory::getTags)
                .flatMap(Collection::stream)
                .forEachOrdered(InheritanceTagRemover::removeTagInheritance);
        return categories;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public SuggestionCategory getRankingRate(@PathVariable Long id) {
        SuggestionCategory category = categoryService.findById(id);
        category.getTags().forEach(InheritanceTagRemover::removeTagInheritance);
        return category;
    }

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public SuggestionCategory addRankingRate(@RequestBody SuggestionCategory category) {
        return categoryService.save(category);
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public SuggestionCategory updateRankingRate(@PathVariable Long id, @RequestBody SuggestionCategory category) {
        category.setId(id);
        return categoryService.update(category);
    }

    @DeleteMapping(value = "/{id}")
    public HttpStatus deleteRankingRate(@PathVariable Long id) {
        categoryService.delete(id);
        return HttpStatus.OK;
    }

}
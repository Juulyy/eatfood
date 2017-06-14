package com.eat.services.recommender;

import com.eat.models.recommender.SuggestionCategory;
import com.eat.repositories.sql.recommender.SuggestionCategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class SuggestionCategoryService {

    @Autowired
    private SuggestionCategoryRepository repository;

    public List<SuggestionCategory> findAll(){
        return repository.findAll();
    }

    public SuggestionCategory findById(Long id){
        return repository.findOne(id);
    }

    public SuggestionCategory save(SuggestionCategory category){
        return repository.save(category);
    }

    public List<SuggestionCategory> saveAll(List<SuggestionCategory> category){
        return repository.save(category);
    }

    public void delete(Long id){
        repository.delete(id);
    }

    public SuggestionCategory update(SuggestionCategory contact){
        return repository.save(contact);
    }

}

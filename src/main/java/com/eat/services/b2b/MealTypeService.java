/*
package com.eat.services.b2b;

import com.eat.models.b2b.MealType;
import com.eat.repositories.sql.b2b.MealTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class MealTypeService {

    @Autowired
    private MealTypeRepository repository;

    public MealType findById(Long id) {
        return repository.findOne(id);
    }

    public MealType save(MealType mealType) {
        return repository.save(mealType);
    }

    public void saveAll(List<MealType> mealTypes) {
        repository.save(mealTypes);
    }

    public void update(MealType mealType) {
        repository.save(mealType);
    }

    public void delete(MealType mealType) {
        repository.delete(mealType);
    }

    public void delete(Long id) {
        repository.delete(id);
    }

    public List<MealType> findAll() {
        return repository.findAll();
    }

}*/
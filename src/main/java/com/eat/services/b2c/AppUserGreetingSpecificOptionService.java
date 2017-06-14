package com.eat.services.b2c;

import com.eat.models.b2c.AppUserGreetingContextSpecific;
import com.eat.repositories.sql.b2c.AppUserGreetingSpecificOptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppUserGreetingSpecificOptionService {

    @Autowired
    private AppUserGreetingSpecificOptionRepository repository;

    public List<AppUserGreetingContextSpecific> findAll() {
        return repository.findAll();
    }

    public AppUserGreetingContextSpecific findById(Long id) {
        return repository.findOne(id);
    }

    public AppUserGreetingContextSpecific add(AppUserGreetingContextSpecific greeting) {
        return repository.save(greeting);
    }

    public void delete(Long id) {
        repository.delete(id);
    }

    public AppUserGreetingContextSpecific update(AppUserGreetingContextSpecific greeting) {
        return repository.save(greeting);
    }

}
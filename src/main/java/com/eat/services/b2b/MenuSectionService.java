package com.eat.services.b2b;

import com.eat.models.b2b.MenuSection;
import com.eat.repositories.sql.b2b.MenuSectionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class MenuSectionService {

    @Autowired
    private MenuSectionRepository repository;

    public MenuSection getById(Long id) {
        return repository.findOne(id);
    }

    public MenuSection save(MenuSection section) {
        return repository.save(section);
    }

    public void saveAll(List<MenuSection> categories) {
        repository.save(categories);
    }

    public void update(MenuSection section) {
        repository.save(section);
    }

    public void delete(MenuSection section) {
        repository.delete(section);
    }

    public void delete(Long id) {
        repository.delete(id);
    }

    public List<MenuSection> getAll() {
        return repository.findAll();
    }

}
package com.eat.services.b2b;

import com.eat.models.b2b.MenuType;
import com.eat.repositories.sql.b2b.MenuTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class MenuTypeService {

    @Autowired
    private MenuTypeRepository menuTypeRepository;

    public List<MenuType> findAll() {
        return new ArrayList<>(menuTypeRepository.findAll());
    }

    public MenuType getById(String id) {
        return menuTypeRepository.findOne(Long.valueOf(id));
    }

    public MenuType add(MenuType menuType) {
        return menuTypeRepository.save(menuType);
    }

    public void deleteById(Long id) {
        menuTypeRepository.delete(id);
    }

    public MenuType update(MenuType menuType) {
        return menuTypeRepository.save(menuType);
    }

    public List<MenuType> findMenuTypeByIds(List<Long> ids) {
        return menuTypeRepository.findByIdIn(ids);
    }

    public List<MenuType> getMenuTypeByName(String name) {
        return menuTypeRepository.findByNameIgnoreCase(name);
    }

    public List<MenuType> getMenuTypeByNameContaining(String name) {
        return menuTypeRepository.findByNameContainingIgnoreCase(name);
    }

}
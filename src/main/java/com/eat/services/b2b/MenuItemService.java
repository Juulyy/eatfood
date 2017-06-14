package com.eat.services.b2b;

import com.eat.controllers.exceptions.DataNotFoundException;
import com.eat.models.b2b.Menu;
import com.eat.models.b2b.MenuItem;
import com.eat.repositories.sql.b2b.MenuItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class MenuItemService {

    @Autowired
    private MenuItemRepository repository;

    @Autowired
    private MenuService menuService;

    public MenuItem addMenuItem(Long menuId, MenuItem menuItem) {
        Menu menu = menuService.findById(menuId);
        if (menuItem != null && menu != null) {
            menuItem.setMenu(menu);
            repository.save(menuItem);
            return menuItem;
        }
        throw new DataNotFoundException();
    }

    public MenuItem updateMenuItem(Long itemId) {
        MenuItem menuItem = repository.findOne(itemId);
        if (menuItem != null) {
            repository.save(menuItem);
            return menuItem;
        }
        throw new DataNotFoundException();
    }

    public List<MenuItem> findAll() {
        return repository.findAll();
    }

    public MenuItem findById(Long id) {
        return repository.findOne(id);
    }

    public MenuItem save(MenuItem menuItem) {
        return repository.save(menuItem);
    }

    public void saveAll(List<MenuItem> menuItems) {
        repository.save(menuItems);
    }

    public MenuItem update(MenuItem menuItem) {
        return repository.save(menuItem);
    }

    public void delete(Long id) {
        repository.delete(id);
    }

    public void delete(MenuItem menuItem) {
        repository.delete(menuItem);
    }

}
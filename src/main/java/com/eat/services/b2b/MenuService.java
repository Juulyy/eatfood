package com.eat.services.b2b;

import com.eat.controllers.exceptions.DataNotFoundException;
import com.eat.models.b2b.Menu;
import com.eat.models.b2b.Place;
import com.eat.repositories.sql.b2b.MenuRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private PlaceService placeService;

    public Menu addMenu(Long placeId, Menu menu) {
        Place place = placeService.findById(placeId);
        if (menu != null && place != null) {
            menu.setPlace(place);
            return save(menu);
        }
        throw new DataNotFoundException();
    }

    public Menu updateMenu(Menu menu, Long placeId) {
        Place place = placeService.findById(placeId);
        if (menu != null && place != null) {
            menu.setPlace(place);
            menu.getMenuItems().forEach(menuItem -> menuItem.setMenu(menu));
            return update(menu);
        }
        throw new DataNotFoundException();
    }

    public List<Menu> findAll() {
        return new ArrayList<>(menuRepository.findAll());
    }

    public Menu findById(Long id) {
        return menuRepository.findOne(id);
    }

    public Menu save(Menu menu) {
        return menuRepository.save(menu);
    }

    public void saveAll(List<Menu> menus) {
        menuRepository.save(menus);
    }

    public void delete(Long id) {
        menuRepository.delete(id);
    }

    public void delete(Menu menu) {
        menuRepository.delete(menu);
    }

    public Menu update(Menu menu) {
        return menuRepository.save(menu);
    }

    public List<Menu> findMenuByIds(List<Long> ids) {
        return menuRepository.findByIdIn(ids);
    }

    public List<Menu> findMenuByName(String name) {
        return menuRepository.findByNameIgnoreCase(name);
    }

    public List<Menu> findMenuByNameContaining(String name) {
        return menuRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Menu> findMenuByPlace(Long placeId) {
        Place place = placeService.findById(placeId);
        if (place != null) {
            return menuRepository.findByPlace(place);
        }
        throw new DataNotFoundException();
    }

}
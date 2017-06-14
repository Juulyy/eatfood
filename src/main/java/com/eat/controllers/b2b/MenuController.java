package com.eat.controllers.b2b;

import com.eat.factories.EntityFactory;
import com.eat.logic.manager.InheritanceTagRemover;
import com.eat.models.b2b.Menu;
import com.eat.models.b2b.MenuItem;
import com.eat.services.b2b.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(value = "/api/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @Autowired
    private EntityFactory entityFactory;

    /**
     * /findAll
     *
     * @return http://localhost:8090/api/menu/5/menus
     */
    /*@PreAuthorize("hasRole('ROLE_ADMIN')")
    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.BUSINESS_MANAGER, AuthoritiesConstants.BUSINESS_USER})*/
    @GetMapping(value = "/find-all-by-place/{id}", produces = "application/json", consumes = "application/json")
    public List<Menu> findAllByPlace(@PathVariable("id") Long placeId) {
        List<Menu> menus = menuService.findMenuByPlace(placeId);
        menus.stream()
                .map(Menu::getMenuItems)
                .flatMap(Collection::stream)
                .map(MenuItem::getTags)
                .flatMap(Collection::stream)
                .forEach(InheritanceTagRemover::removeTagInheritance);
        return menus;
    }


    /**
     * /findAll
     *
     * @return http://localhost:8090/api/menu/find-all/
     */
    /*@PreAuthorize("hasRole('ROLE_ADMIN')")
    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.BUSINESS_MANAGER, AuthoritiesConstants.BUSINESS_USER})*/
    @GetMapping(value = "/find-all", produces = "application/json", consumes = "application/json")
    public List<Menu> findAll() {
        return menuService.findAll();
    }

    /**
     * /{id} as PathVariable
     *
     * @param id
     * @return http://localhost:8090/api/menu/2
     */
    @GetMapping(value = "/{id}", produces = "application/json", consumes = "application/json")
    public Menu findById(@PathVariable("id") Long id) {
        return menuService.findById(id);
    }

    /**
     * POST /add @RequestBody Menu menu
     *
     * @param placeId
     * @param menuRaw
     * @return http://localhost:8090/api/menu/add
     */
//    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.BUSINESS_MANAGER, AuthoritiesConstants.BUSINESS_USER})
    @PostMapping(value = "/add", produces = "application/json", consumes = "application/json")
    public Menu addMenu(@RequestParam(value = "placeId") Long placeId, @RequestBody String menuRaw) {
        Menu instance = entityFactory.createInstance(menuRaw, Menu.class);
        return menuService.addMenu(placeId, instance);
    }

    /**
     * PATCH(needeable to update all fields) @RequestBody Menu menu
     *
     * @param menuRaw
     * @return http://localhost:8090/api/menu/5
     */
//    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.BUSINESS_MANAGER, AuthoritiesConstants.BUSINESS_USER})
    @PatchMapping(value = "/update", produces = "application/json", consumes = "application/json")
    public Menu updateMenu(@RequestBody String menuRaw, @RequestParam(value = "placeId") Long placeId) {
        Menu instance = entityFactory.createInstance(menuRaw, Menu.class);
        return menuService.updateMenu(instance, placeId);
    }

    /**
     * DELETE
     * http://localhost:8090/api/menu/2
     *
     * @param id
     */
//    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.BUSINESS_MANAGER, AuthoritiesConstants.BUSINESS_USER})
    @DeleteMapping(value = "/{id}", produces = "application/json", consumes = "application/json")
    public void deleteMenu(@PathVariable("id") Long id) {
        menuService.delete(id);
    }

    /**
     * /findByName @RequestParam(value = "name")
     *
     * @param name
     * @return http://localhost:8090/api/menu/find-by-name?name=email
     */
    @GetMapping(value = "/find-by-name", produces = "application/json", consumes = "application/json")
    public List<Menu> getMenuByName(@RequestParam(value = "name") String name) {
        return new ArrayList<>(menuService.findMenuByName(name));
    }

    /**
     * /findByNameContaining
     *
     * @param name
     * @return http://localhost:8090/api/menu/find-by-name-containing?name=Delive
     */
    @GetMapping(value = "/find-by-name-containing", produces = "application/json", consumes = "application/json")
    public List<Menu> getMenuByNameContaining(@RequestParam(value = "name") String name) {
        return new ArrayList<>(menuService.findMenuByNameContaining(name));
    }

    /**
     * /findByIds @RequestParam(value = "ids")
     *
     * @param ids
     * @return http://localhost:8090/api/menu/find-by-ids?ids=1&ids=2
     */
    @GetMapping(value = "/find-by-ids", produces = "application/json", consumes = "application/json")
    public List<Menu> getMenusByIds(@RequestParam(value = "ids") List<Long> ids) {
        return new ArrayList<>(menuService.findMenuByIds(ids));
    }

}
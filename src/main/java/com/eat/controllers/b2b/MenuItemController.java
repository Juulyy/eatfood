package com.eat.controllers.b2b;

import com.eat.factories.EntityFactory;
import com.eat.models.b2b.MenuItem;
import com.eat.services.b2b.MenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/menu/item")
public class MenuItemController {

    @Autowired
    private MenuItemService menuItemService;

    @Autowired
    private EntityFactory entityFactory;

    /**
     * GET secured method to get all menuItems
     *
     * @return http://localhost:8090/api/menu/item/details/find-all
     */
//    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.BUSINESS_MANAGER, AuthoritiesConstants.BUSINESS_USER})
    @GetMapping(value = "/find-all", produces = "application/json")
    public List<MenuItem> findAll() {
        return menuItemService.findAll();
    }

    /**
     * GET secured method to get menuItem by id
     *
     * @return http://localhost:8090/api/menu/item/2
     */
//    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.BUSINESS_MANAGER, AuthoritiesConstants.BUSINESS_USER})
    @GetMapping(value = "/{id}", produces = "application/json")
    public MenuItem findById(@PathVariable(value = "id") Long id) {
        return menuItemService.findById(id);
    }

    /**
     * POST secured method to add new menuItem for Place
     *
     * @param menuId - Menu ID
     * @param menuItemRaw   - MenuItem
     * @return http://localhost:8090/api/menu/item/details/add-for-place
     */
//    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.BUSINESS_MANAGER, AuthoritiesConstants.BUSINESS_USER})
    @PostMapping(value = "/add", produces = "application/json", consumes = "application/json")
    public MenuItem addMenuItem(@RequestParam("menuId") Long menuId, @RequestBody String menuItemRaw) {
        return menuItemService.addMenuItem(menuId, entityFactory.createInstance(menuItemRaw, MenuItem.class));
    }

    /**
     * DELETE secured method to delete menuItem for Place
     *
     * @param menuItemId - MenuItem ID
     * @return http://localhost:8090/api/menu/item/details/delete-from-place/2
     */
//    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.BUSINESS_MANAGER, AuthoritiesConstants.BUSINESS_USER})
    @DeleteMapping(value = "/{id}", produces = "application/json")
    public void deleteMenuItem(@PathVariable("id") Long menuItemId) {
        menuItemService.delete(menuItemId);
    }

    /**
     * PATCH secured method to update all menuItem's fields
     *
     * @param itemId - MenuItem ID
     * @return http://localhost:8090/api/menu/item/details/update-for-place/2
     */
//    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.BUSINESS_MANAGER, AuthoritiesConstants.BUSINESS_USER})
    @PatchMapping(value = "{id}", produces = "application/json")
    public MenuItem updateMenuItemFromPlaceMenu(@PathVariable("id") Long itemId) {
        return menuItemService.updateMenuItem(itemId);
    }

}

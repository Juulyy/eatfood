package com.eat.controllers.b2b;

import com.eat.models.b2b.MenuType;
import com.eat.services.b2b.MenuTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(value = "/api/menu-type")
public class MenuTypeController {

    @Autowired
    MenuTypeService menuTypeService;

    /**
     * ///////////////////////////////////////////CRUD OPERATION////////////////////////////
     */

    /**
     * /findAll
     * @return http://localhost:8090/api/menu/types/find-all/
     */
    @GetMapping(value = "/find-all", produces = "application/json")
    public Collection<MenuType> findAll(){
        return menuTypeService.findAll();
    }

    /**
     * /{id} as PathVariable
     * @param id
     * @return http://localhost:8090/api/menu/types/2
     */
    @GetMapping(value = "/{id}", produces = "application/json")
    public MenuType getMenuType(@PathVariable("id") String id){
        return menuTypeService.getById(id);
    }

    /**
     * POST /add @RequestBody MenuType menuType
     * @param menuType
     * @return http://localhost:8090/api/menu/types/add
     */
    @PostMapping(value = "/add", produces = "application/json")
    public MenuType menuType(@RequestBody MenuType menuType){
        return menuTypeService.add(menuType);
    }

    /**
     * PATCH(needeable to update all fields) @RequestBody MenuType menuType
     * @param menuType
     * @return http://localhost:8090/api/menu/types/5
     */
    @PatchMapping(value = "/{id}", produces = "application/json")
    public MenuType updateMenuType(@RequestBody MenuType menuType){
        return menuTypeService.update(menuType);
    }

    /** DELETE
     * http://localhost:8090/api/menu/types/2
     * @param id
     */
    @DeleteMapping(value = "/{id}")
    public void deleteMenuType(@PathVariable Long id){
        menuTypeService.deleteById(id);
    }

    /**
     * ////////////////////////////ADDITIONAL SEARCHING OPERATION/////////////////////////////
     */

    /**
     * /findByName @RequestParam(value = "name")
     * @param name
     * @return http://localhost:8090/api/menu/types/find-by-name?name=email
     */
    @GetMapping(value = "/find-by-name", produces = "application/json")
    public List<MenuType> getMenuByName(@RequestParam(value = "name") String name){
        return new ArrayList<>(menuTypeService.getMenuTypeByName(name));
    }

    /**
     * /findByNameContaining
     * @param name
     * @return http://localhost:8090/api/menu/types/find-by-name-containing?name=Delive
     */
    @GetMapping(value = "/find-by-name-containing", produces = "application/json")
    public List<MenuType> getMenuTypeByNameContaining(@RequestParam(value = "name") String name){
        return new ArrayList<>(menuTypeService.getMenuTypeByNameContaining(name));
    }

    /**
     * /findByIds @RequestParam(value = "ids")
     * @param ids
     * @return http://localhost:8090/api/menu/types/find-by-ids?ids=1&ids=2
     */
    @GetMapping(value = "/find-by-ids", produces = "application/json")
    public List<MenuType> getMenuTypesByIds(@RequestParam(value = "ids") List<Long> ids){
        return new ArrayList<>(menuTypeService.findMenuTypeByIds(ids));
    }
}

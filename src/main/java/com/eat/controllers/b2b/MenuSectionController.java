package com.eat.controllers.b2b;

import com.eat.models.b2b.MenuSection;
import com.eat.services.b2b.MenuSectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/menu/section")
public class MenuSectionController {

    @Autowired
    private MenuSectionService service;

    @GetMapping(value = "/{id}", produces = "application/json")
    public MenuSection get(@PathVariable("id") Long id) {
        return service.getById(id);
    }

    @PostMapping(value = "/", produces = "application/json")
    public MenuSection add(@RequestBody MenuSection section) {
        return service.save(section);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @PatchMapping(value = "/{id}")
    public MenuSection update(@RequestBody MenuSection section) {
        return service.save(section);
    }

    @GetMapping(value = "/find-all")
    public Collection<MenuSection> getAll() {
        return service.getAll();
    }

}

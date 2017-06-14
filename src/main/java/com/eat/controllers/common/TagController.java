package com.eat.controllers.common;

import com.eat.logic.manager.InheritanceTagRemover;
import com.eat.models.common.Tag;
import com.eat.models.common.enums.TagType;
import com.eat.services.common.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/tags")
public class TagController {

    @Autowired
    private TagService tagService;

    /**
     * /findAllByType
     *
     * @param menuItemName
     * @return http://localhost:8090/api/tags/find-by-word
     */
    @GetMapping(value = "/find-by-word", produces = "application/json", consumes = "application/json")
    public Set<Tag> findTagsByWordIgnoreCase(@RequestParam(value = "menuItemName") String menuItemName) {
        return tagService.findTagsByString(menuItemName)
                .stream()
                .peek(InheritanceTagRemover.removeInheritanceConsumer())
                .collect(Collectors.toSet());
    }

    /**
     * /findAllByType
     *
     * @param type
     * @return http://localhost:8090/api/tags/find-all/{group/category/item}/
     */
    @GetMapping(value = "/find-all/{type}", produces = "application/json")
    public List<Tag> findByType(@PathVariable("type") TagType type) {
        return tagService.findAllByType(type);
    }

    /**
     * /findAll
     *
     * @return http://localhost:8090/api/tags/find-all/
     */
    @GetMapping(value = "/find-all", produces = "application/json")
    public List<Tag> findAll() {
        return tagService.findAll();
    }

    /**
     * /findAll without tag inheritance
     *
     * @return http://localhost:8090/api/tags/find-all-v2/
     */
    @GetMapping(value = "/find-all-v2", produces = "application/json")
    public List<Tag> findAllWithoutInheritance() {
        List<Tag> tags = tagService.findAll();
        tags.forEach(InheritanceTagRemover::removeTagInheritance);
        return tags;
    }

    /**
     * /{id} as PathVariable
     *
     * @param id
     * @return http://localhost:8090/api/tags/2
     */
    @GetMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public Tag findById(@PathVariable("id") Long id) {
        return tagService.findById(id);
    }

    /**
     * POST /add @RequestBody ContactType contactType
     *
     * @param tag
     * @return http://localhost:8090/api/restaurantItemTags/add
     */
    @PostMapping(value = "/add", consumes = "application/json", produces = "application/json")
    public Tag addTag(@RequestBody Tag tag) {
        return tagService.save(tag);
    }

    /**
     * PATCH(needeable to update all fields) @RequestBody ContactType restaurantItemTags
     *
     * @param tag
     * @return http://localhost:8090/api/restaurantItemTags/5
     */
    @PatchMapping(value = "/{id}", produces = "application/json")
    public Tag updateTag(@RequestBody Tag tag) {
        return tagService.update(tag);
    }

    /**
     * DELETE
     * http://localhost:8090/api/tags/2
     *
     * @param id
     */
    @DeleteMapping(value = "/{id}")
    public void deleteTag(@PathVariable Long id) {
        tagService.delete(id);
    }

}
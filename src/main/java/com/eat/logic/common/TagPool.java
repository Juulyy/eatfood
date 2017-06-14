package com.eat.logic.common;

import com.eat.logic.manager.InheritanceTagRemover;
import com.eat.models.common.Tag;
import com.eat.services.common.TagService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
public class TagPool {

    @Autowired
    private TagService tagService;

    @Getter
    private List<Tag> persistedTags = new ArrayList<>();

    @Getter
    private HashMap<Long, Tag> notInheritanceTags = new HashMap<>();

    @PostConstruct
    private void initialize() {
        updateTagsPool();
    }

    public void updateTagsPool() {
        this.persistedTags = new ArrayList<>();
        this.notInheritanceTags = new HashMap<>();
        try {
            this.persistedTags.addAll(tagService.findAll());
            tagService.findAll().forEach(tag -> notInheritanceTags.put(tag.getId(), Tag.of()
                    .id(tag.getId())
                    .name(tag.getName())
                    .parent(tag.getParent())
                    .childTags(tag.getChildTags())
                    .create()));
            this.getNotInheritanceTags().forEach((key, value) -> InheritanceTagRemover.removeTagInheritance(value));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
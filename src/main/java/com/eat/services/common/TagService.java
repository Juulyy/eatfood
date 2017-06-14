package com.eat.services.common;

import com.eat.dto.common.TagDto;
import com.eat.logic.common.TagPool;
import com.eat.models.common.Tag;
import com.eat.models.common.enums.TagType;
import com.eat.repositories.sql.common.TagRepository;
import com.eat.utils.converters.dto.TagDtoConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TagService {

    @Autowired
    private TagRepository repository;

    @Autowired
    private TagDtoConverter dtoConverter;

    @Autowired
    private TagPool tagPool;

    public Set<Tag> findAllByNames(Set<String> tagNames) {
        Set<Tag> tags = new HashSet<>();
        tagNames.forEach(tagName -> tagPool.getPersistedTags().stream()
                .filter(tag -> tag.getName().equals(tagName))
                .findAny()
                .ifPresent(tags::add));
        return tags;
    }

    public Set<ImmutablePair<Tag, Double>> findAllByNamesWithRates(Set<ImmutablePair<String, Double>> set) {
        Set<ImmutablePair<Tag, Double>> tags = new HashSet<>();

        set.forEach(pair -> tagPool.getPersistedTags().stream()
                .filter(tag -> tag.getName().equals(pair.left))
                .findAny()
                .ifPresent(tag -> tags.add(new ImmutablePair<>(tag, pair.right))));

        return tags;
    }

    public Set<Tag> findTagsByString(String fullMenuItemName) {
        return parseToTag(Arrays.asList(fullMenuItemName));
    }

    public Set<TagDto> findTagsDtoByString(String fullMenuItemName) {
        return findTagsByString(fullMenuItemName).stream()
                .map(tag -> dtoConverter.toDto(tag))
                .collect(Collectors.toSet());
    }

    private Set<Tag> parseToTag(List<String> strings) {
        List<String> buffer = new ArrayList<>(strings);
        Set<Tag> tags = new HashSet<>();

        tagPool.getPersistedTags().stream()
                .filter(Tag::isConcreteMenuCategoryTag)
                .forEach(tag -> strings.forEach(name -> {
                    if (StringUtils.containsIgnoreCase(tag.getName(), name)) {
                        tags.add(tagPool.getNotInheritanceTags().get(tag.getId()));
                        if (tag.getParent() != null) {
                            tags.add(tagPool.getNotInheritanceTags().get(tag.getParent().getId()));
                        }
                        buffer.remove(name);
                    }
                }));

        tagPool.getPersistedTags().stream()
                .filter(Tag::isConcreteMenuItemTag)
                .forEach(tag -> buffer.forEach(name -> {
                    if (StringUtils.containsIgnoreCase(tag.getName(), name)) {
                        tags.add(tagPool.getNotInheritanceTags().get(tag.getId()));
                        if (tag.getParent() != null) {
                            tags.add(tagPool.getNotInheritanceTags().get(tag.getParent().getId()));

                            if (tag.getParent().getParent() != null) {
                                tags.add(tagPool.getNotInheritanceTags().get(tag.getParent().getParent().getId()));
                            }
                        }
                    }
                }));

        return tags;
    }

    public Tag findByNameInCache(String name) {
        return tagPool.getPersistedTags().stream()
                .filter(tag -> tag.getName().equals(name))
                .findFirst()
                .orElseGet(null);
    }

    public Tag findByNameInCacheIgnoreCase(String name) {
        return tagPool.getPersistedTags().stream()
                .filter(tag -> StringUtils.equalsIgnoreCase(tag.getName(), name))
                .findFirst()
                .orElseGet(null);
    }

    public List<Tag> findByName(String name) {
        return repository.findByName(name);
    }

    public Set<Tag> findDictinctByName(String name) {
        return repository.findDistinctByName(name);
    }

    public Set<Tag> findDictinctByNames(Set<Tag> tags) {
        return repository.findDistinctByNames(tags.stream()
                .map(Tag::getName)
                .collect(Collectors.toList()));
    }

    public Tag findByNameAndType(String name, TagType type) {
        return repository.findByNameAndType(name, type);
    }

    public List<Tag> findByParent(Tag parent) {
        return repository.findByParent(parent);
    }

    public List<Tag> findAllByType(TagType type) {
        return repository.findByType(type);
    }

    public Tag findById(Long id) {
        return repository.findOne(id);
    }

    public Tag save(Tag tag) {
        return repository.save(tag);
    }

    public Tag update(Tag tag) {
        Tag persistedTag = findById(tag.getId());
        if (tag.getParent() != null) {
            persistedTag.setParent(tag.getParent());
            if (tag.getParent().getParent() != null) {
                persistedTag.setParent(tag.getParent().getParent());
            }
        }
        persistedTag.setName(tag.getName());
        persistedTag.setType(tag.getType());
        return repository.save(persistedTag);
    }

    public void delete(Tag tag) {
        repository.delete(tag);
    }

    public void delete(Long id) {
        repository.delete(id);
    }

    public List<Tag> findAll() {
        return repository.findAll();
    }

    public Set<Tag> getAll() {
        return new HashSet<>(repository.findAll());
    }

    public List<Tag> saveAll(List<Tag> tags) {
        return repository.save(tags);
    }

    public Set<Tag> findByNameIn(Set<String> names) {
        return repository.findByNameIn(names);
    }

    @Transactional
    public void deleteByIdIn(List<Long> ids) {
        repository.deleteByIdIn(ids);
    }

}
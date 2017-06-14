package com.eat.utils.converters.dto;

import com.eat.dto.common.TagDto;
import com.eat.models.common.Tag;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class TagDtoConverter {

    public TagDto toDto(Tag tag) {
        return TagDto.of()
                .id(tag.getId())
                .name(tag.getName())
                .type(tag.getType())
                .create();
    }

    public Tag toTag(TagDto tagDto) {
        return Tag.of()
                .name(tagDto.getName())
                .type(tagDto.getType())
                .create();
    }

}
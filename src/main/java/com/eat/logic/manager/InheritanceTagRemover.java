package com.eat.logic.manager;

import com.eat.models.common.Tag;

import java.util.function.Consumer;

public interface InheritanceTagRemover {

    static Consumer<Tag> removeInheritanceConsumer() {
        return tag -> {
            tag.setParent(null);
            tag.setChildTags(null);
        };
    }

    static Tag removeTagInheritance(Tag tag) {
        tag.setParent(null);
        tag.setChildTags(null);
        return tag;
    }

}
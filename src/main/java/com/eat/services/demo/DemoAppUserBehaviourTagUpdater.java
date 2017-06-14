package com.eat.services.demo;

import com.eat.models.common.Tag;
import com.eat.models.mongo.pool.BehaviourPool;
import com.eat.models.mongo.pool.PoolContextRate;
import com.eat.models.mongo.pool.TagContext;
import com.eat.services.mongo.pool.BehaviourPoolService;
import com.eat.services.mongo.pool.TagContextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class DemoAppUserBehaviourTagUpdater {

    @Autowired
    private BehaviourPoolService poolService;

    @Autowired
    private TagContextService contextService;

    public void addTagsIntoBehaviourPool(Long userId, List<Tag> tags) {
        BehaviourPool behaviourPool = poolService.findAllByUserSqlId(userId);

        tags.forEach(tag -> behaviourPool.getPool().add(TagContext.of()
                .type(contextService.getContextType(tag))
                .tagName(tag.getName())
                .weight(PoolContextRate.REG_PREF.getWeight())
                .create()));

        poolService.update(behaviourPool);
    }

    public void addTagIntoBehaviourPool(Long userId, Tag tag) {
        BehaviourPool behaviourPool = poolService.findAllByUserSqlId(userId);

        behaviourPool.getPool().add(TagContext.of()
                .type(contextService.getContextType(tag))
                .tagName(tag.getName())
                .weight(PoolContextRate.REG_PREF.getWeight())
                .create());

        poolService.update(behaviourPool);
    }

    public void removeTagIntoBehaviourPool(Long userId, Tag tag) {
        BehaviourPool behaviourPool = poolService.findAllByUserSqlId(userId);

        Optional<TagContext> context = behaviourPool.getPool().stream()
                .filter(tagContext -> tagContext.getTagName().equals(tag.getName()))
                .findFirst();

        if (context.isPresent()) {
            behaviourPool.getPool().remove(context.get());
            poolService.update(behaviourPool);
        }
    }

    public void removeTagsIntoBehaviourPool(Long userId, List<Tag> tags) {
        BehaviourPool behaviourPool = poolService.findAllByUserSqlId(userId);

        tags.forEach(tag -> {
            Optional<TagContext> context = behaviourPool.getPool().stream()
                    .filter(tagContext -> tagContext.getTagName().equals(tag.getName()))
                    .findFirst();

            context.ifPresent(tagContext -> behaviourPool.getPool().remove(tagContext));
        });

        poolService.update(behaviourPool);
    }

}
package com.eat.aspects.pool;

import com.eat.logic.common.TagPool;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TagUpdatePoolAspect {

    @Autowired
    private TagPool tagPool;

    @After(value = "execution(* com.eat.services.common.TagService.save(..)) ||" +
            "execution(* com.eat.services.common.TagService.delete(..)) || " +
            "execution(* com.eat.services.common.TagService.update(..))")
    public void afterTagChanges() {
        tagPool.updateTagsPool();
    }

}
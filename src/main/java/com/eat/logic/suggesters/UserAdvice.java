package com.eat.logic.suggesters;


import com.eat.logic.rankers.wrappers.Rankable;
import com.eat.models.b2c.AppUser;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.Set;

@Value
@Builder(builderMethodName = "of", buildMethodName = "create")
public class UserAdvice<T> implements Advice<T> {

    private AppUser user;

    @Singular
    private Set<Rankable<T>> advices;

    @Override
    public Set<Rankable<T>> getAdvices() {
        return advices;
    }
}

package com.eat.aspects.pool;

import com.eat.logic.b2c.AppUserGreetingPool;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AppUserGreetingPoolAspect {

    @Autowired
    private AppUserGreetingPool greetingPool;

    @After(value = "execution(* com.eat.services.b2c.AppUserGreetingService.save(..)) ||" +
            "execution(* com.eat.services.b2c.AppUserGreetingService.update(..)) ||" +
            "execution(* com.eat.services.b2c.AppUserGreetingService.delete(..))")
    public void afterGreetingsUpdate() {
        greetingPool.updatePoll();
    }

}
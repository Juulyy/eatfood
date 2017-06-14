package com.eat.aspects.pool;

import com.eat.logic.b2c.SuggestionCategoryPool;
import com.eat.models.common.enums.CrudMethod;
import com.eat.models.demo.DemoAppParam;
import com.eat.models.mongo.pool.BehaviourPool;
import com.eat.services.mongo.pool.BehaviourPoolService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class BehaviourPoolAspect {

    @Autowired
    private BehaviourPoolService poolService;

    @Autowired
    private SuggestionCategoryPool categoryPool;

    @After(value = "execution(* com.eat.services.b2c.VisitedPlacesService.add(..))")
    public void visitedPlaceAfter(JoinPoint joinPoint) {
        Long userId = (Long) joinPoint.getArgs()[0];
        Long placeId = (Long) joinPoint.getArgs()[1];
        poolService.processAutocheckVisitedPlaceBehaviour(userId, placeId);
        categoryPool.clearCachedUserResults(userId);
    }

    @After(value = "execution(* com.eat.services.b2c.AppUserCollectionService.recommendFeature(..))")
    public void recFeatureAfter(JoinPoint joinPoint) {
        Long userId = (Long) joinPoint.getArgs()[0];
        Long featureId = (Long) joinPoint.getArgs()[1];
        poolService.processFeatureRecBehaviour(userId, featureId);
        categoryPool.clearCachedUserResults(userId);
    }

    @After(value = "execution(* com.eat.services.b2c.AppUserCollectionService.recommendMenuItem(..))")
    public void recMenuItemAfter(JoinPoint joinPoint) {
        Long userId = (Long) joinPoint.getArgs()[0];
        Long menuItemId = (Long) joinPoint.getArgs()[1];
        poolService.processMenuItemRecBehaviour(userId, menuItemId);
        categoryPool.clearCachedUserResults(userId);
    }

    @After(value = "execution(* com.eat.services.b2c.AppUserCollectionService.recommendPlace(..))")
    public void recPlaceAfter(JoinPoint joinPoint) {
        Long userId = (Long) joinPoint.getArgs()[0];
        Long placeId = (Long) joinPoint.getArgs()[1];
        poolService.processPlaceRecBehaviour(userId, placeId);
        categoryPool.clearCachedUserResults(userId);
    }

    @After(value = "execution(* com.eat.services.b2c.AppUserCollectionService.followingPlacesManagement(..))")
    public void followPlaceAfter(JoinPoint joinPoint) {
        Long placeId = (Long) joinPoint.getArgs()[0];
        Long userId = (Long) joinPoint.getArgs()[1];
        CrudMethod method = (CrudMethod) joinPoint.getArgs()[2];
        if (method.equals(CrudMethod.PUT)) {
            poolService.processPlaceFollowBehaviour(userId, placeId);
            categoryPool.clearCachedUserResults(userId);
        }
    }


    @AfterReturning(value = "execution(* com.eat.services.demo.DemoAppParamService.save(..))")
    public void saveDemoAppUserAfter(JoinPoint joinPoint) {
        DemoAppParam demoAppParam = (DemoAppParam) joinPoint.getArgs()[0];
        BehaviourPool pool = poolService.createRefAndPrefPool(demoAppParam.getAppUser());
        poolService.save(pool);
    }

}
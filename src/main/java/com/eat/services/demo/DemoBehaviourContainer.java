package com.eat.services.demo;

import com.eat.services.mongo.pool.BehaviourPoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class DemoBehaviourContainer {

    @Autowired
    private BehaviourPoolService bpService;

    private final Set<Long> coffeeShopIdsStream = Stream.of(43L, 54L).collect(Collectors.toSet());
    private final Long cinnamonaRoll = 3297L;
    private final Long cherryPieId = 4116L;

    private final Set<Long> barIdsStream = Stream.of(39L, 41L, 46L, 47L, 50L, 65L, 72L).collect(Collectors.toSet());
    private final Set<Long> veganIds = Stream.of(15L, 25L, 29L, 34L, 48L).collect(Collectors.toSet());

    private final Set<Long> sushiIdsStream = Stream.of(40L, 49L,  51L, 59L, 77L).collect(Collectors.toSet());

    public void processFirstBehaviour(Long userId) {
        coffeeShopIdsStream.forEach(coffeeShopId ->
                bpService.processPlaceRecBehaviour(userId, coffeeShopId));

        bpService.processMenuItemRecBehaviour(userId, cinnamonaRoll);
        bpService.processMenuItemRecBehaviour(userId, cherryPieId);
    }

    public void processSecondBehaviour(Long userId) {
        barIdsStream.forEach(barId ->
                bpService.processPlaceFollowBehaviour(userId, barId));

        veganIds.forEach(veganId ->
                bpService.processPlaceFollowBehaviour(userId, veganId));
    }

    public void processThirdBehaviour(Long userId) {
        sushiIdsStream.forEach(sushiId ->
                bpService.processAutocheckVisitedPlaceBehaviour(userId, sushiId));
    }

    public void processResetDemoBehaviour(Long userId) {
        bpService.resetBehaviour(userId);
    }

    public void processMixedBehaviour(Long userId) {
        processFirstBehaviour(userId);
        processSecondBehaviour(userId);
        processThirdBehaviour(userId);
    }

    /*private List<Long> shuffleAndTrimList(Stream<Long> stream, int size) {
        List<Long> list = stream.collect(Collectors.toList());
        Collections.shuffle(list);
        return list.subList(0, size - 1);
    }*/

}
package com.eat.repositories.mongo.pool;

import com.eat.models.mongo.pool.BehaviourPool;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BehaviourPoolRepository extends MongoRepository<BehaviourPool, String> {

    BehaviourPool findBySqlUserId(Long userId);

}
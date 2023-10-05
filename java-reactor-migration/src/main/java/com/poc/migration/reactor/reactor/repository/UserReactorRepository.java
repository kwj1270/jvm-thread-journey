package com.poc.migration.reactor.reactor.repository;

import com.poc.migration.reactor.common.repository.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.Map;

public class UserReactorRepository {

    private static final Logger logger = LoggerFactory.getLogger(UserReactorRepository.class);
    private final Map<String, UserEntity> userMap;

    public UserReactorRepository() {
        var user = new UserEntity("1234", "taewoo", 32, "image#1000");

        userMap = Map.of("1234", user);
    }

    public Mono<UserEntity> findById(String userId) {
        return Mono.create(sink -> {
            logger.info("UserRepository.findById: {}", userId);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            UserEntity user = userMap.get(userId);
            if (user == null) {
                sink.success();
            } else {
                sink.success(user);
            }
        });
    }
}

package com.poc.migration.reactor.future.repository;

import com.poc.migration.reactor.common.repository.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class UserFutureRepository {
    private static final Logger logger = LoggerFactory.getLogger(UserFutureRepository.class);
    private final Map<String, UserEntity> userMap;

    public UserFutureRepository() {
        var user = new UserEntity("1234", "taewoo", 32, "image#1000");

        userMap = Map.of("1234", user);
    }

    public CompletableFuture<Optional<UserEntity>> findById(String userId) {
        return CompletableFuture.supplyAsync(() -> {
            logger.info("UserRepository.findById: {}", userId);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            var user = userMap.get(userId);
            return Optional.ofNullable(user);
        });
    }
}

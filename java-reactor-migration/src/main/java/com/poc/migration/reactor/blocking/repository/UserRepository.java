package com.poc.migration.reactor.blocking.repository;

import com.poc.migration.reactor.common.repository.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;

public class UserRepository {

    private static final Logger logger = LoggerFactory.getLogger(UserRepository.class);
    private final Map<String, UserEntity> userMap;

    public UserRepository() {
        var user = new UserEntity("1234", "taewoo", 32, "image#1000");

        userMap = Map.of("1234", user);
    }

    public Optional<UserEntity> findById(String userId) {
        logger.info("UserRepository.findById: {}", userId);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        var user = userMap.get(userId);
        return Optional.ofNullable(user);
    }
}

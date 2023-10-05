package com.poc.migration.reactor.future.repository.after;

import com.poc.migration.reactor.common.repository.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;

public class UserFutureAfterRepository {
    private static final Logger logger = LoggerFactory.getLogger(UserFutureAfterRepository.class);
    private final Map<String, UserEntity> userMap;

    public UserFutureAfterRepository() {
        var user = new UserEntity("1234", "taewoo", 32, "image#1000");

        userMap = Map.of("1234", user);
    }

    public Mono<UserEntity> findById(String userId) {
        return Mono.create(userEntityMonoSink -> {
            logger.info("UserRepository.findById: {}", userId);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            final UserEntity userEntity = userMap.get(userId);
            if(Objects.isNull(userEntity)) {
                userEntityMonoSink.success();
            }
            userEntityMonoSink.success(userEntity);
        });
    }
}

package com.poc.migration.reactor.reactor.repository;

import com.poc.migration.reactor.common.repository.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Optional;

public class FollowReactorRepository {

    private static final Logger logger = LoggerFactory.getLogger(FollowReactorRepository.class);

    private Map<String, Long> userFollowCountMap;

    public FollowReactorRepository() {
        userFollowCountMap = Map.of("1234", 1000L);
    }

    public Mono<Long> countByUserId(String userId) {
        return Mono.create(sink -> {
            logger.info("FollowRepository.countByUserId: {}", userId);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            sink.success(userFollowCountMap.getOrDefault(userId, 0L));
        });
    }

    public Mono<Long> countWithContext() {
        return Mono.deferContextual(context -> {
            Optional<UserEntity> userOptional = context.getOrEmpty("user");
            if (userOptional.isEmpty()) throw new RuntimeException("user not found");

            return Mono.just(userOptional.get().id());
        }).flatMap(this::countByUserId);
    }
}

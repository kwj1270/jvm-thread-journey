package com.poc.migration.reactor.future.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class FollowFutureRepository {

    private static final Logger logger = LoggerFactory.getLogger(FollowFutureRepository.class);

    private Map<String, Long> userFollowCountMap;

    public FollowFutureRepository() {
        userFollowCountMap = Map.of("1234", 1000L);
    }

    public CompletableFuture<Long> countByUserId(String userId) {
        return CompletableFuture.supplyAsync(() -> {
            logger.info("FollowRepository.countByUserId: {}", userId);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return userFollowCountMap.getOrDefault(userId, 0L);
        });
    }
}

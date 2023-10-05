package com.poc.migration.reactor.blocking.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class FollowRepository {

    private static final Logger logger = LoggerFactory.getLogger(FollowRepository.class);

    private Map<String, Long> userFollowCountMap;

    public FollowRepository() {
        userFollowCountMap = Map.of("1234", 1000L);
    }

    public Long countByUserId(String userId) {
        logger.info("FollowRepository.countByUserId: {}", userId);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return userFollowCountMap.getOrDefault(userId, 0L);
    }
}

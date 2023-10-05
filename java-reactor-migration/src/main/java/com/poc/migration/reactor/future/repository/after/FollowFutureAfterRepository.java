package com.poc.migration.reactor.future.repository.after;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.Map;

public class FollowFutureAfterRepository {

    private static final Logger logger = LoggerFactory.getLogger(FollowFutureAfterRepository.class);

    private Map<String, Long> userFollowCountMap;

    public FollowFutureAfterRepository() {
        userFollowCountMap = Map.of("1234", 1000L);
    }

    public Mono<Long> countByUserId(String userId) {
        return Mono.create(monoSink -> {
            logger.info("FollowRepository.countByUserId: {}", userId);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            monoSink.success(userFollowCountMap.getOrDefault(userId, 0L));
        });
    }
}

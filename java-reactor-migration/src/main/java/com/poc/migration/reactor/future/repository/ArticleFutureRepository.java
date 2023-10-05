package com.poc.migration.reactor.future.repository;

import com.poc.migration.reactor.common.repository.ArticleEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class ArticleFutureRepository {

    private static final Logger logger = LoggerFactory.getLogger(ArticleFutureRepository.class);

    private static List<ArticleEntity> articleEntities;

    public ArticleFutureRepository() {
        articleEntities = List.of(
                new ArticleEntity("1", "소식1", "내용1", "1234"),
                new ArticleEntity("2", "소식2", "내용2", "1234"),
                new ArticleEntity("3", "소식3", "내용3", "10000")
        );
    }

    public CompletableFuture<List<ArticleEntity>> findAllByUserId(String userId) {
        return CompletableFuture.supplyAsync(() -> {
            logger.info("ArticleRepository.findAllByUserId: {}", userId);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return articleEntities.stream()
                    .filter(articleEntity -> articleEntity.userId().equals(userId))
                    .collect(Collectors.toList());
        });
    }
}

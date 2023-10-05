package com.grizz.wooman.reactor.blocking.repository;

import com.poc.migration.reactor.common.repository.ArticleEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class ArticleRepository {

    private static final Logger logger = LoggerFactory.getLogger(ArticleRepository.class);


    private static List<ArticleEntity> articleEntities;

    public ArticleRepository() {
        articleEntities = List.of(
                new ArticleEntity("1", "소식1", "내용1", "1234"),
                new ArticleEntity("2", "소식2", "내용2", "1234"),
                new ArticleEntity("3", "소식3", "내용3", "10000")
        );
    }

    public List<ArticleEntity> findAllByUserId(String userId) {
        logger.info("ArticleRepository.findAllByUserId: {}", userId);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return articleEntities.stream()
                .filter(articleEntity -> articleEntity.userId().equals(userId))
                .collect(Collectors.toList());
    }
}

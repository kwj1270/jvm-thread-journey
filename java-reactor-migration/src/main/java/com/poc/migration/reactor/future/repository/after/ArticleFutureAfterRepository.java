package com.poc.migration.reactor.future.repository.after;

import com.poc.migration.reactor.common.repository.ArticleEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.util.List;

public class ArticleFutureAfterRepository {

    private static final Logger logger = LoggerFactory.getLogger(ArticleFutureAfterRepository.class);

    private static List<ArticleEntity> articleEntities;

    public ArticleFutureAfterRepository() {
        articleEntities = List.of(
                new ArticleEntity("1", "소식1", "내용1", "1234"),
                new ArticleEntity("2", "소식2", "내용2", "1234"),
                new ArticleEntity("3", "소식3", "내용3", "10000"),
                new ArticleEntity("4", "소식4", "내용4", "1234"),
                new ArticleEntity("5", "소식5", "내용5", "1234"),
                new ArticleEntity("6", "소식6", "내용6", "1234"),
                new ArticleEntity("7", "소식7", "내용7", "1234"),
                new ArticleEntity("8", "소식8", "내용8", "1234"),
                new ArticleEntity("9", "소식9", "내용9", "1234"),
                new ArticleEntity("10", "소식10", "내용10", "1234"),
                new ArticleEntity("11", "소식11", "내용11", "1234")
        );
    }

    public Flux<ArticleEntity> findAllByUserId(String userId) {
        return Flux.create(articleEntityFluxSink -> {
            logger.info("ArticleRepository.findAllByUserId: {}", userId);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            articleEntities.stream()
                    .filter(articleEntity -> articleEntity.userId().equals(userId))
                    .forEach(articleEntityFluxSink::next);
            articleEntityFluxSink.complete();
        });
    }
}

package com.poc.migration.reactor.future;

import com.poc.migration.reactor.common.Article;
import com.poc.migration.reactor.common.Image;
import com.poc.migration.reactor.common.User;
import com.poc.migration.reactor.common.repository.UserEntity;
import com.poc.migration.reactor.future.repository.ArticleFutureRepository;
import com.poc.migration.reactor.future.repository.FollowFutureRepository;
import com.poc.migration.reactor.future.repository.ImageFutureRepository;
import com.poc.migration.reactor.future.repository.UserFutureRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class UserFutureService {

    private static final Logger logger = LoggerFactory.getLogger(UserFutureService.class);

    private final UserFutureRepository userRepository;
    private final ArticleFutureRepository articleRepository;
    private final ImageFutureRepository imageRepository;
    private final FollowFutureRepository followRepository;

    public UserFutureService(final UserFutureRepository userRepository, final ArticleFutureRepository articleRepository, final ImageFutureRepository imageRepository, final FollowFutureRepository followRepository) {
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
        this.imageRepository = imageRepository;
        this.followRepository = followRepository;
    }

    public CompletableFuture<Optional<User>> getUserById(String id) {
        return userRepository.findById(id)
                .thenComposeAsync(this::User);
    }

    private CompletableFuture<Optional<User>> User(Optional<UserEntity> userEntityOptional) {
        if (userEntityOptional.isEmpty()) {
            return CompletableFuture.completedFuture(Optional.empty());
        }

        var userEntity = userEntityOptional.get();

        var imageFuture = imageRepository.findById(userEntity.profileImageId())
                .thenApplyAsync(imageEntityOptional ->
                        imageEntityOptional.map(imageEntity ->
                                new Image(imageEntity.id(), imageEntity.name(), imageEntity.url())
                        )
                );


        var articlesFuture = articleRepository.findAllByUserId(userEntity.id())
                .thenApplyAsync(articleEntities ->
                        articleEntities.stream()
                                .map(articleEntity ->
                                        new Article(articleEntity.id(), articleEntity.title(), articleEntity.content())
                                )
                                .collect(Collectors.toList())
                );

        var followCountFuture = followRepository.countByUserId(userEntity.id());

        return CompletableFuture.allOf(imageFuture, articlesFuture, followCountFuture)
                .thenAcceptAsync(v -> {
                    logger.info("Three futures are completed");
                })
                .thenRunAsync(() -> {
                    logger.info("Three futures are also completed");
                })
                .thenApplyAsync(v -> {
                    try {
                        var image = imageFuture.get();
                        var articles = articlesFuture.get();
                        var followCount = followCountFuture.get();

                        return Optional.of(
                                new User(
                                        userEntity.id(),
                                        userEntity.name(),
                                        userEntity.age(),
                                        image,
                                        articles,
                                        followCount
                                )
                        );
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}

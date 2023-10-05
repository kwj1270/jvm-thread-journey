package com.poc.migration.reactor.reactor;

import com.poc.migration.reactor.common.Article;
import com.poc.migration.reactor.common.EmptyImage;
import com.poc.migration.reactor.common.Image;
import com.poc.migration.reactor.common.User;
import com.poc.migration.reactor.common.repository.UserEntity;
import com.poc.migration.reactor.reactor.repository.ArticleReactorRepository;
import com.poc.migration.reactor.reactor.repository.FollowReactorRepository;
import com.poc.migration.reactor.reactor.repository.ImageReactorRepository;
import com.poc.migration.reactor.reactor.repository.UserReactorRepository;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.List;
import java.util.Optional;

public class UserReactorService {
    private final UserReactorRepository userRepository;
    private final ArticleReactorRepository articleRepository;
    private final ImageReactorRepository imageRepository;
    private final FollowReactorRepository followRepository;

    public UserReactorService(final UserReactorRepository userRepository, final ArticleReactorRepository articleRepository, final ImageReactorRepository imageRepository, final FollowReactorRepository followRepository) {
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
        this.imageRepository = imageRepository;
        this.followRepository = followRepository;
    }

    public Mono<User> getUserById(String id) {
        return userRepository.findById(id)
                .flatMap(this::User);
    }

    private Mono<User> User(UserEntity userEntity) {
        Context context = Context.of("user", userEntity);

        var imageMono = imageRepository.findWithContext()
                .map(imageEntity ->
                        new Image(imageEntity.id(), imageEntity.name(), imageEntity.url())
                ).onErrorReturn(new EmptyImage())
                .contextWrite(context);

        var articlesMono = articleRepository.findAllWithContext()
                .skip(5)
                .take(2)
                .map(articleEntity ->
                        new Article(articleEntity.id(), articleEntity.title(), articleEntity.content())
                ).collectList()
                .contextWrite(context);

        var followCountMono = followRepository.countWithContext()
                .contextWrite(context);

        return Mono.zip(imageMono, articlesMono, followCountMono)
                .map(resultTuple -> {
                    Image image = resultTuple.getT1();
                    List<Article> articles = resultTuple.getT2();
                    Long followCount = resultTuple.getT3();

                    Optional<Image> imageOptional = Optional.empty();
                    if (!(image instanceof EmptyImage)) {
                        imageOptional = Optional.of(image);
                    }

                    return new User(
                            userEntity.id(),
                            userEntity.name(),
                            userEntity.age(),
                            imageOptional,
                            articles,
                            followCount
                    );
                });
    }
}

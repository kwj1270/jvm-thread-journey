
package com.poc.migration.reactor.future;

import com.poc.migration.reactor.common.Article;
import com.poc.migration.reactor.common.EmptyImage;
import com.poc.migration.reactor.common.Image;
import com.poc.migration.reactor.common.User;
import com.poc.migration.reactor.common.repository.UserEntity;
import com.poc.migration.reactor.future.repository.after.ArticleFutureAfterRepository;
import com.poc.migration.reactor.future.repository.after.FollowFutureAfterRepository;
import com.poc.migration.reactor.future.repository.after.ImageFutureAfterRepository;
import com.poc.migration.reactor.future.repository.after.UserFutureAfterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

public class UserFutureToFluxService {

    private static final Logger logger = LoggerFactory.getLogger(UserFutureToFluxService.class);

    private final UserFutureAfterRepository userRepository;
    private final ArticleFutureAfterRepository articleRepository;
    private final ImageFutureAfterRepository imageRepository;
    private final FollowFutureAfterRepository followRepository;

    public UserFutureToFluxService(final UserFutureAfterRepository userRepository, final ArticleFutureAfterRepository articleRepository, final ImageFutureAfterRepository imageRepository, final FollowFutureAfterRepository followRepository) {
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
        this.articleRepository = articleRepository;
        this.followRepository = followRepository;
    }

    public Mono<User> getUserById(String id) {
        return userRepository.findById(id)
                .flatMap(this::getUser);
    }

    private Mono<User> getUser(UserEntity userEntity) {
        var imageMono = imageRepository.findById(userEntity.profileImageId())
                .map(imageEntity -> new Image(imageEntity.id(), imageEntity.name(), imageEntity.url()))
                .onErrorReturn(new EmptyImage());

        var articlesMono = articleRepository.findAllByUserId(userEntity.id())
                .skip(5)
                .take(2, true)
                .map(articleEntity -> new Article(articleEntity.id(), articleEntity.title(), articleEntity.content()))
                .collectList();

        var followCountMono = followRepository.countByUserId(userEntity.id());
        return Flux.mergeSequential(imageMono, articlesMono, followCountMono)
                .collectList()
                .map(it -> {
                    final Optional<Image> image = getImageOptional((Image) it.get(0));
                    final List<Article> articles = (List<Article>) it.get(1);
                    final Long followCount = (Long) it.get(2);
                    return new User(
                            userEntity.id(),
                            userEntity.name(),
                            userEntity.age(),
                            image,
                            articles,
                            followCount
                    );
                });
    }

    private static Optional<Image> getImageOptional(final Image image) {
        if (image instanceof EmptyImage) {
            return Optional.empty();
        }
        return Optional.of(image);
    }
}

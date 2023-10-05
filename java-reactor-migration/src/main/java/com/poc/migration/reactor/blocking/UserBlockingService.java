package com.poc.migration.reactor.blocking;

import com.grizz.wooman.reactor.blocking.repository.ArticleRepository;
import com.poc.migration.reactor.blocking.repository.FollowRepository;
import com.poc.migration.reactor.blocking.repository.ImageRepository;
import com.poc.migration.reactor.blocking.repository.UserRepository;
import com.poc.migration.reactor.common.Article;
import com.poc.migration.reactor.common.Image;
import com.poc.migration.reactor.common.User;

import java.util.Optional;
import java.util.stream.Collectors;

public class UserBlockingService {
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final ImageRepository imageRepository;
    private final FollowRepository followRepository;

    public UserBlockingService(final UserRepository userRepository,
                               final ArticleRepository articleRepository,
                               final ImageRepository imageRepository,
                               final FollowRepository followRepository) {
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
        this.imageRepository = imageRepository;
        this.followRepository = followRepository;
    }

    public Optional<User> getUserById(String id) {
        return userRepository.findById(id)
                .map(user -> {
                    var image = imageRepository.findById(user.profileImageId())
                            .map(imageEntity -> {
                                return new Image(imageEntity.id(), imageEntity.name(), imageEntity.url());
                            });

                    var articles = articleRepository.findAllByUserId(user.id())
                            .stream().map(articleEntity ->
                                    new Article(articleEntity.id(), articleEntity.title(), articleEntity.content()))
                            .collect(Collectors.toList());

                    var followCount = followRepository.countByUserId(user.id());

                    return new User(
                            user.id(),
                            user.name(),
                            user.age(),
                            image,
                            articles,
                            followCount
                    );
                });
    }
}

package com.grizz.wooman.reactor.reactor;

import com.poc.migration.reactor.common.User;
import com.poc.migration.reactor.reactor.UserReactorService;
import com.poc.migration.reactor.reactor.repository.ArticleReactorRepository;
import com.poc.migration.reactor.reactor.repository.FollowReactorRepository;
import com.poc.migration.reactor.reactor.repository.ImageReactorRepository;
import com.poc.migration.reactor.reactor.repository.UserReactorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceReactorTest {
    UserReactorService userService;
    UserReactorRepository userRepository;
    ArticleReactorRepository articleRepository;
    ImageReactorRepository imageRepository;
    FollowReactorRepository followRepository;

    @BeforeEach
    void setUp() {
        userRepository = new UserReactorRepository();
        articleRepository = new ArticleReactorRepository();
        imageRepository = new ImageReactorRepository();
        followRepository = new FollowReactorRepository();

        userService = new UserReactorService(
                userRepository, articleRepository, imageRepository, followRepository
        );
    }

    @Test
    void getUserEmptyIfInvalidUserIdIsGiven() throws ExecutionException, InterruptedException {
        // given
        String userId = "invalid_user_id";

        // when
        Optional<User> user = userService.getUserById(userId).blockOptional();

        // then
        assertTrue(user.isEmpty());
    }

    @Test
    void testGetUser() throws ExecutionException, InterruptedException {
        // given
        String userId = "1234";

        // when
        Optional<User> optionalUser = userService.getUserById(userId).blockOptional();

        // then
        assertFalse(optionalUser.isEmpty());
        var user = optionalUser.get();
        assertEquals(user.name(), "taewoo");
        assertEquals(user.age(), 32);

        assertFalse(user.profileImage().isEmpty());
        var image = user.profileImage().get();
        assertEquals(image.getId(), "image#1000");
        assertEquals(image.getName(), "profileImage");
        assertEquals(image.getUrl(), "https://dailyone.com/images/1000");

        assertEquals(2, user.articleList().size());

        assertEquals(1000, user.followCount());
    }
}

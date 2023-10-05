package com.grizz.wooman.reactor.future;

import com.poc.migration.reactor.common.User;
import com.poc.migration.reactor.future.UserFutureService;
import com.poc.migration.reactor.future.repository.ArticleFutureRepository;
import com.poc.migration.reactor.future.repository.FollowFutureRepository;
import com.poc.migration.reactor.future.repository.ImageFutureRepository;
import com.poc.migration.reactor.future.repository.UserFutureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceFutureTest {
    UserFutureService userFutureService;
    UserFutureRepository userRepository;
    ArticleFutureRepository articleRepository;
    ImageFutureRepository imageRepository;
    FollowFutureRepository followRepository;

    @BeforeEach
    void setUp() {
        userRepository = new UserFutureRepository();
        articleRepository = new ArticleFutureRepository();
        imageRepository = new ImageFutureRepository();
        followRepository = new FollowFutureRepository();

        userFutureService = new UserFutureService(
                userRepository, articleRepository, imageRepository, followRepository
        );
    }

    @Test
    void getUserEmptyIfInvalidUserIdIsGiven() throws ExecutionException, InterruptedException {
        // given
        String userId = "invalid_user_id";

        // when
        Optional<User> user = userFutureService.getUserById(userId).get();

        // then
        assertTrue(user.isEmpty());
    }

    @Test
    void testGetUser() throws ExecutionException, InterruptedException {
        // given
        String userId = "1234";

        // when
        Optional<User> optionalUser = userFutureService.getUserById(userId).get();

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

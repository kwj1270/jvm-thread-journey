package com.grizz.wooman.reactor.blocking;

import com.grizz.wooman.reactor.blocking.repository.ArticleRepository;
import com.poc.migration.reactor.blocking.UserBlockingService;
import com.poc.migration.reactor.blocking.repository.FollowRepository;
import com.poc.migration.reactor.blocking.repository.ImageRepository;
import com.poc.migration.reactor.blocking.repository.UserRepository;
import com.poc.migration.reactor.common.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceBlockingTest {
    UserBlockingService userBlockingService;
    UserRepository userRepository;
    ArticleRepository articleRepository;
    ImageRepository imageRepository;
    FollowRepository followRepository;

    @BeforeEach
    void setUp() {
        userRepository = new UserRepository();
        articleRepository = new ArticleRepository();
        imageRepository = new ImageRepository();
        followRepository = new FollowRepository();

        userBlockingService = new UserBlockingService(
                userRepository, articleRepository, imageRepository, followRepository
        );
    }

    @Test
    void getUserEmptyIfInvalidUserIdIsGiven() {
        // given
        String userId = "invalid_user_id";

        // when
        Optional<User> user = userBlockingService.getUserById(userId);

        // then
        assertTrue(user.isEmpty());
    }

    @Test
    void testGetUser() {
        // given
        String userId = "1234";

        // when
        Optional<User> optionalUser = userBlockingService.getUserById(userId);

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

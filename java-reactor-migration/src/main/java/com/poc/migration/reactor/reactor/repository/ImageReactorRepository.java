package com.poc.migration.reactor.reactor.repository;

import com.poc.migration.reactor.common.repository.ImageEntity;
import com.poc.migration.reactor.common.repository.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Optional;

public class ImageReactorRepository {

    private static final Logger logger = LoggerFactory.getLogger(ImageReactorRepository.class);
    private final Map<String, ImageEntity> imageMap;

    public ImageReactorRepository() {
        imageMap = Map.of(
                "image#1000", new ImageEntity("image#1000", "profileImage", "https://dailyone.com/images/1000")
        );
    }

    public Mono<ImageEntity> findById(String id) {
        return Mono.create(sink -> {
            logger.info("ImageRepository.findById: {}", id);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            var image = imageMap.get(id);
            if (image == null) {
                sink.error(new RuntimeException("image not found"));
            } else {
                sink.success(image);
            }
        });
    }

    public Mono<ImageEntity> findWithContext() {
        return Mono.deferContextual(context -> {
            Optional<UserEntity> userOptional = context.getOrEmpty("user");
            if (userOptional.isEmpty()) throw new RuntimeException("user not found");

            return Mono.just(userOptional.get().profileImageId());
        }).flatMap(this::findById);
    }
}

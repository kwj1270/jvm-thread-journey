package com.poc.migration.reactor.future.repository;

import com.poc.migration.reactor.common.repository.ImageEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class ImageFutureRepository {
    private static final Logger logger = LoggerFactory.getLogger(ImageFutureRepository.class);

    private final Map<String, ImageEntity> imageMap;

    public ImageFutureRepository() {
        imageMap = Map.of(
                "image#1000", new ImageEntity("image#1000", "profileImage", "https://dailyone.com/images/1000")
        );
    }

    public CompletableFuture<Optional<ImageEntity>> findById(String id) {
        return CompletableFuture.supplyAsync(() -> {
            logger.info("ImageRepository.findById: {}", id);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return Optional.ofNullable(imageMap.get(id));
        });
    }
}

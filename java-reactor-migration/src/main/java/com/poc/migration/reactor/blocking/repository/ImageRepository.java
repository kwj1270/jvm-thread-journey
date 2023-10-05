package com.poc.migration.reactor.blocking.repository;

import com.poc.migration.reactor.common.repository.ImageEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;

public class ImageRepository {

    private static final Logger logger = LoggerFactory.getLogger(ImageRepository.class);

    private final Map<String, ImageEntity> imageMap;

    public ImageRepository() {
        imageMap = Map.of(
                "image#1000", new ImageEntity("image#1000", "profileImage", "https://dailyone.com/images/1000")
        );
    }

    public Optional<ImageEntity> findById(String id) {
        logger.info("ImageRepository.findById: {}", id);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(imageMap.get(id));
    }
}

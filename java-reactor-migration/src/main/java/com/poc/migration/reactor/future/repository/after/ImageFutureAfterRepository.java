package com.poc.migration.reactor.future.repository.after;

import com.poc.migration.reactor.common.repository.ImageEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;

public class ImageFutureAfterRepository {
    private static final Logger logger = LoggerFactory.getLogger(ImageFutureAfterRepository.class);

    private final Map<String, ImageEntity> imageMap;

    public ImageFutureAfterRepository() {
        imageMap = Map.of(
                "image#1000", new ImageEntity("image#1000", "profileImage", "https://dailyone.com/images/1000")
        );
    }

    public Mono<ImageEntity> findById(String id) {
        return Mono.create(imageEntityMonoSink -> {
            logger.info("ImageRepository.findById: {}", id);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            final ImageEntity imageEntity = imageMap.get(id);
            if (Objects.isNull(imageEntity)) {
                imageEntityMonoSink.error(new RuntimeException("Image Not Found"));
            }
            imageEntityMonoSink.success(imageEntity);
        });
    }
}

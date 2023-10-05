package com.poc.migration.reactor.common;

import java.util.List;
import java.util.Optional;

public record User(String id, String name, int age, Optional<Image> profileImage, List<Article> articleList,
                   Long followCount) {
}


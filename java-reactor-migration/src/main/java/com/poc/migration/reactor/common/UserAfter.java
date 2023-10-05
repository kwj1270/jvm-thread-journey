package com.poc.migration.reactor.common;

import java.util.List;
import java.util.Optional;

public record UserAfter(String id, String name, int age, Image profileImage, List<Article> articleList,
                        Long followCount) {
}


package com.poc.migration.reactor.common;

public class Article {
    private final String id;
    private final String title;
    private final String content;

    public Article(final String id, final String title, final String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}

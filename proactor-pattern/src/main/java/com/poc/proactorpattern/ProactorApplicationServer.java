package com.poc.proactorpattern;

import com.poc.proactorpattern.server.Proactor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

public class ProactorApplicationServer {

    private static final Logger logger = LoggerFactory.getLogger(ProactorApplicationServer.class);


    public static void main(String[] args) {
        logger.info(">>> start main");
        CompletableFuture.runAsync(() -> new Proactor(8080).run());
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        logger.info("<<< start end");
    }
}

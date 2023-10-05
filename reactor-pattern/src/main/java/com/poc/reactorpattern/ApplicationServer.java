package com.poc.reactorpattern;

import com.poc.reactorpattern.server.EventLoop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ApplicationServer {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationServer.class);


    public static void main(String[] args) {
        logger.info(">>> start main");
        final List<EventLoop> eventLoops = List.of(new EventLoop(8080));
        eventLoops.forEach(EventLoop::run);
        logger.info("<<< start end");
    }
}

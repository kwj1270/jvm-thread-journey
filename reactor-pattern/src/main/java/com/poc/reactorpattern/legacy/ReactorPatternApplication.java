package com.poc.reactorpattern.legacy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReactorPatternApplication {

    private static final Logger logger = LoggerFactory.getLogger(ReactorPatternApplication.class);


    public static void main(String[] args) {
        logger.info(">>> start main");
        final Reactor reactor = new Reactor(8080);
        reactor.run();
        logger.info("<<< start end");
    }
}

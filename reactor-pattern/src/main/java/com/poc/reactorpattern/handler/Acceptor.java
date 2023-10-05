package com.poc.reactorpattern.handler;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Acceptor implements EventHandler{

    private final Selector selector;
    private final ServerSocketChannel serverSocketChannel;


    public Acceptor(final Selector selector, final ServerSocketChannel serverSocketChannel) {
        this.selector = selector;
        this.serverSocketChannel = serverSocketChannel;
    }

    @Override
    public void handle() {
        try {
            final SocketChannel clientSocketChannel = serverSocketChannel.accept();
            new HttpEventHandler(selector, clientSocketChannel);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

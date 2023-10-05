
package com.poc.reactorpattern.legacy;

import com.poc.reactorpattern.handler.Acceptor;
import com.poc.reactorpattern.handler.EventHandler;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Reactor implements Runnable {

    private static final ExecutorService SINGLE_THREAD_POOL = Executors.newSingleThreadExecutor();

    private final EventHandler acceptor;
    private final Selector selector;
    private final ServerSocketChannel serverSocketChannel;


    public Reactor(final int port) {
        try {
            this.serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress("localhost", port));
            serverSocketChannel.configureBlocking(false);
            this.selector = Selector.open();
            this.acceptor = new Acceptor(selector, serverSocketChannel);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT).attach(acceptor);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void run() {
        SINGLE_THREAD_POOL.submit(() -> {
            while (true) {
                selector.select();
                final Iterator<SelectionKey> selectionKeys = selector.selectedKeys().iterator();
                while (selectionKeys.hasNext()) {
                    final SelectionKey selectionKey = selectionKeys.next();
                    selectionKeys.remove();
                    dispatch(selectionKey);
                }
            }
        });
    }

    private void dispatch(final SelectionKey selectionKey) {
        final EventHandler eventHandler = (EventHandler) selectionKey.attachment();
        if (selectionKey.isReadable() || selectionKey.isAcceptable()) {
            eventHandler.handle();
        }
    }
}

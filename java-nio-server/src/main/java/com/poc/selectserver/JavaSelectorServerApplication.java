
package com.poc.selectserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class JavaSelectorServerApplication {

    private static final AtomicInteger atomicInteger = new AtomicInteger();
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(50);
    private static final Logger logger = LoggerFactory.getLogger(JavaSelectorServerApplication.class);

    public static void main(String[] args) {

        // Create Server Socket
        try (final ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
             final Selector selector = Selector.open()) {
            // Bind HostName And Port
            serverSocketChannel.bind(new InetSocketAddress("localhost", 8080));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
                selector.select();
                final Iterator<SelectionKey> selectedKeys = selector.selectedKeys().iterator();

                while (selectedKeys.hasNext()) {
                    final SelectionKey key = selectedKeys.next();
                    selectedKeys.remove();
                    if(key.isAcceptable()) {
                        final SocketChannel clientSocket = ((ServerSocketChannel) key.channel()).accept();
                        clientSocket.configureBlocking(false);
                        clientSocket.register(selector, SelectionKey.OP_READ);
                    } else if (key.isReadable()) {
                        final SocketChannel clientSocket = (SocketChannel) key.channel();
                        final String requestBody = handleRequest(clientSocket);
                        logger.info("request: {}", requestBody.trim());
                        handleResponse(clientSocket, requestBody);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String handleRequest(final SocketChannel clientSocket) {
        try {
            final ByteBuffer requestByteBuffer = ByteBuffer.allocateDirect(1024);
            while (clientSocket.read(requestByteBuffer) == 0) {
                logger.trace("Reading...");
            }
            requestByteBuffer.flip();
            return StandardCharsets.UTF_8.decode(requestByteBuffer).toString().trim();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    private static void handleResponse(final SocketChannel clientSocket, final String requestBody) throws InterruptedException, IOException {
        Thread.sleep(10);
        final String content = "Received : " + requestBody;
        final ByteBuffer responseByteBuffer = ByteBuffer.wrap(content.getBytes(StandardCharsets.UTF_8));
        clientSocket.write(responseByteBuffer);
        clientSocket.close();
        final int taskIdentifiedId = atomicInteger.getAndIncrement();
        logger.info("Run handleRequest Task Number = {}", taskIdentifiedId);
    }
}

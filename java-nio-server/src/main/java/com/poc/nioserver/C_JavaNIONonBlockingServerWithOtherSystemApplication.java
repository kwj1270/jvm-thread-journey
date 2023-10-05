
package com.poc.nioserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class C_JavaNIONonBlockingServerWithOtherSystemApplication {

    private static final Logger logger = LoggerFactory.getLogger(C_JavaNIONonBlockingServerWithOtherSystemApplication.class);

    public static void main(String[] args) throws InterruptedException {

        // Create Server Socket
        try (final ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            // Bind HostName And Port
            serverSocketChannel.bind(new InetSocketAddress("localhost", 8080));
            serverSocketChannel.configureBlocking(false);

            // 하나의 와일문, 그리고 응답이 길어진다면 어떻게 될까? -> 하나의 스레드만 겁나 쓰는중
            while (true) {
                // no block and return null
                final SocketChannel clientSocket = serverSocketChannel.accept();
                if (clientSocket == null) {
                    Thread.sleep(100);
                    logger.trace("Wait Accept...");
                    continue;
                }

                final ByteBuffer requestByteBuffer = ByteBuffer.allocateDirect(1024);
                while (clientSocket.read(requestByteBuffer) == 0) {
                    logger.trace("Reading...");
                }
                requestByteBuffer.flip();
                final String requestBody = StandardCharsets.UTF_8.decode(requestByteBuffer).toString();
                logger.info("request: {}", requestBody.trim());

                // this is other System Call
                Thread.sleep(10);

                final ByteBuffer responseByteBuffer = ByteBuffer.wrap("This is Server".getBytes(StandardCharsets.UTF_8));
                clientSocket.write(responseByteBuffer);
                clientSocket.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}

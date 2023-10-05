package com.example.ioclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class JavaIOClientApplication {

    private static final Logger logger = LoggerFactory.getLogger(JavaIOClientApplication.class);

    public static void main(String[] args) {
        try (final Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("localhost", 8080));

            final OutputStream outputStream = socket.getOutputStream();
            outputStream.write("This is Socket Client".getBytes(StandardCharsets.US_ASCII));
            outputStream.flush();

            final InputStream inputStream = socket.getInputStream();
            final byte[] bytes = new byte[1024];
            inputStream.read(bytes);
            logger.info("response: {}", new String(bytes).trim());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

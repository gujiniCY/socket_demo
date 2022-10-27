package com.example.socket_demo.socket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Administrator
 */
@Component
@Slf4j
public class TcpSocket implements Runnable {
    public Integer port;
    private ServerSocket server;
    private ExecutorService threadPool;

    public TcpSocket() {
        try {
            port = 8081;
            threadPool = Executors.newCachedThreadPool();
            server = new ServerSocket(port);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket socket = server.accept();
                if (socket != null) {
                    SocketReceive socketReceive = new SocketReceive(socket);
                    threadPool.submit(socketReceive);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

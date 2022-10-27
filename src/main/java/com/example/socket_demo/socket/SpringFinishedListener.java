package com.example.socket_demo.socket;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SpringFinishedListener implements InitializingBean {
    @Autowired
    private TcpSocket tcpsocket;

    @Override
    public void afterPropertiesSet() {
        Thread serverThread = new Thread(tcpsocket);
        serverThread.start();
    }
}

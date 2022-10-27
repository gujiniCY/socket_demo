package com.example.socket_demo.socket;


import lombok.extern.slf4j.Slf4j;

import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
public class AllClientsMap {

    /**
     * 所有已连接设备
     */
    private static final ConcurrentMap<String, Socket> ALLCLIENTS = new ConcurrentHashMap<>();

    /**
     * 返回设备列表
     *
     * @return
     */
    public static ConcurrentMap<String, Socket> getAllClients() {
        return ALLCLIENTS;
    }

    /**
     * 通过key获取客户端
     *
     * @return
     */
    public static Socket getSocketByKey(String key) {
        return ALLCLIENTS.get(key);
    }

    /**
     * 添加设备到列表
     *
     * @param key
     * @param socket
     */
    public static void put(String key, Socket socket) {
        ALLCLIENTS.put(key, socket);
        log.info("设备Key:{}========ip:{}已加入列表", key, socket.getInetAddress().getHostAddress());
    }

    /**
     * 移除设备
     *
     * @param key
     */
    public static void remove(String key) {
        ALLCLIENTS.remove(key);
        log.info("已移除设备Key:{}", key);
    }

    /**
     * 返回已连接设备数量
     *
     * @return
     */
    public static int size() {
        log.info("当前设备数：{}", ALLCLIENTS.size());
        return ALLCLIENTS.size();
    }

    /**
     * 打印信息
     *
     * @return
     */
    public static void print() {
        log.info("当前设备列表信息:长度：{}", ALLCLIENTS.size());
        ALLCLIENTS.forEach((key, socket) -> {
            log.info("设备Key:{}========ip:{}", key, socket.getInetAddress().getHostAddress());
        });
    }

    /**
     * 是否包含
     *
     * @param key
     * @return
     */
    public static boolean contains(String key) {
        return ALLCLIENTS.containsKey(key);
    }
}

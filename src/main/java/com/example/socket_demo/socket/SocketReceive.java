package com.example.socket_demo.socket;

import lombok.extern.slf4j.Slf4j;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

@Slf4j
public class SocketReceive implements Runnable {
    private Socket socket;

    public SocketReceive() {
    }

    public SocketReceive(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        while (true) {
            if (null == socket) {
                log.info("socket为空");
                return;
            }
            boolean isClosed = socket.isClosed();
            if (isClosed) {
                log.info("socket检测到关闭了");
                return;
            }
            String hostAddress = socket.getInetAddress().getHostAddress();
            try {
                //建立客户端信息输入流
                DataInputStream in = new DataInputStream(socket.getInputStream());
                //定义字节数组读取数据
                byte[] bytes = new byte[1024];
                int len = in.read(bytes);
                if (len == -1) {
                    return;
                }
                //定义一个新数组copy，解决读取出来的数据字节不够全是0的问题
                byte[] bytes1 = new byte[len];
                System.arraycopy(bytes, 0, bytes1, 0, len);
                log.info("客户端传的byte字节数组：" + printBytesByStringBuilder(bytes1));
                String s = new String(bytes1);
                log.info("客户端传的byte字节数组转换成字符串打印:" + s);
                //转换hex数据
                String data = byteArrayToHex(bytes1);
                log.info("接收的16进制数据:" + data);
                //如果服务端没有保存该socket
                if(!AllClientsMap.contains(hostAddress)){
                    AllClientsMap.put(hostAddress, socket);
                    AllClientsMap.print();
                }
                log.debug("客户端" + hostAddress + "发送数据：{}", data);
                //执行业务
                System.out.println("执行业务");

                //从map中获取客户端发送消息
                response(AllClientsMap.getSocketByKey(hostAddress), data);

            } catch (IOException e) {
                if (AllClientsMap.contains(hostAddress)) {
                    AllClientsMap.remove(hostAddress);
                    AllClientsMap.print();
                }
                try {
                    socket.close();
                    log.error("{}断开连接", hostAddress);
                    return;
                } catch (IOException ioException) {
                    log.error(ioException.getMessage());
                }
            }
        }
    }


    /**
     * 根据字节数组，输出对应的格式化字符串
     *
     * @param bytes 字节数组
     * @return 字节数组字符串
     */
    public static String printBytesByStringBuilder(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte aByte : bytes) {
            stringBuilder.append(byte2String(aByte));
        }
        return stringBuilder.toString();
    }

    public static String byte2String(byte b) {
        return String.format("%02x ", b);
    }

    /**
     * 向socket发送消息
     *
     * @param socket 对应socket
     * @param msg    消息
     */
    public static void response(Socket socket, String msg) {
        log.debug("向设备IP：{}发送消息：{}", socket.getInetAddress().getHostAddress(), msg);
        OutputStream outputStream = null;
        try {
            outputStream = socket.getOutputStream();
            outputStream.write(hexStringToByteArray(msg));
        } catch (IOException e) {
            try {
                socket.close();
            } catch (IOException ioException) {
                log.error(ioException.getMessage());
            }
            log.error(e.getMessage());
        }
    }

    /**
     * 字节数组转字符串
     *
     * @param bytes
     * @return
     */
    public static String byteArrayToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (int index = 0, len = bytes.length; index <= len - 1; index += 1) {
            int char1 = ((bytes[index] >> 4) & 0xF);
            char chara1 = Character.forDigit(char1, 16);
            int char2 = ((bytes[index]) & 0xF);
            char chara2 = Character.forDigit(char2, 16);
            result.append(chara1);
            result.append(chara2);
        }
        return result.toString();
    }


    /**
     * 16进制表示的字符串转换为字节数组
     *
     * @param hexString 16进制表示的字符串
     * @return byte[] 字节数组
     */
    public static byte[] hexStringToByteArray(String hexString) {
        hexString = hexString.replaceAll(" ", "");
        int len = hexString.length();
        byte[] bytes = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            // 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个字节
            bytes[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character
                    .digit(hexString.charAt(i + 1), 16));
        }
        return bytes;
    }
}

package com.zjx.youchat.biochat;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ChatServer {
    public static void main(String[] args) {
        // 创建服务端
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(1132);
        } catch (IOException e) {
            // 服务端创建失败
            e.printStackTrace();
            return;
        }

        // 创建容器管理所有客户端
        Map<String, Socket> map = new HashMap<String, Socket>();

        while (true) {
            try {
                // 每接收到一个新客户端就创建一个新线程处理其消息
                Socket socket = serverSocket.accept();
                map.put(socket.getRemoteSocketAddress().toString(), socket);
                new Thread(() -> {
                    while (true) {
                        try {
                            InputStream inputStream = socket.getInputStream();
                            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                            String str = bufferedReader.readLine();
                            System.out.println(socket.getRemoteSocketAddress().toString() + ": " + str);

                            // 转发客户端消息
                            for(String addr : map.keySet()) {
                                Socket s = map.get(addr);
                                if(socket.getRemoteSocketAddress().toString().equals(addr)) {
                                    continue;
                                }
                                OutputStream outputStream = s.getOutputStream();
                                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
                                PrintWriter printWriter = new PrintWriter(outputStreamWriter);
                                printWriter.println(socket.getRemoteSocketAddress().toString() + ": " + str);
                                printWriter.flush();
                            }

                        } catch (Exception e) {
                            // 处理客户端消息失败
                            e.printStackTrace();
                        }
                    }
                }).start();
            } catch (Exception e) {
                // 接收新客户端失败
                e.printStackTrace();
            }
        }
    }
}

package com.zjx.youchat.biochat;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {
    public static void main(String[] args) {
        // 创建客户端
        Socket socket = null;
        try {
            socket = new Socket("localhost", 1132);
        } catch (IOException e) {
            // 创建客户端失败
            e.printStackTrace();
            return;
        }

        // 创建新线程接收其他客户端的消息
        Socket socket_copy = socket;
        new Thread(() -> {
            while(true) {
                try {
                    InputStream inputStream = socket_copy.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String str = bufferedReader.readLine();
                    System.out.println(str);
                } catch (Exception e) {
                    // 接收其他客户端消息失败
                    e.printStackTrace();
                }
            }
        }).start();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                OutputStream outputStream = socket.getOutputStream();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
                PrintWriter printWriter = new PrintWriter(outputStreamWriter);
                printWriter.println(scanner.nextLine());
                printWriter.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

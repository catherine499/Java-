package server;

import server.controller.RequestProcessor;
import server.ui.ServerInfoFrame;

import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//服务端主函数
public class ServerMain {
    public static void main(String[] args) {
    	//获取服务端端口号
        int port = Integer.parseInt(DataBuffer.configProp.getProperty("port"));
        //初始化服务端socket
        try {
            DataBuffer.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        new Thread(new Runnable() {//启动新线程监听客户端连接
            public void run() {
                try {
                    while (true) {
                        // 监听客户端的连接
                        Socket socket = DataBuffer.serverSocket.accept();
                        System.out.println("客户来了："
                                + socket.getInetAddress().getHostAddress()
                                + ":" + socket.getPort());

                        //对每个客户端启动一个线程  在线程中调用请求处理器来处理每个客户端的请求
                        new Thread(new RequestProcessor(socket)).start();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        //启动服务器监控窗体
        new ServerInfoFrame();
    }
}

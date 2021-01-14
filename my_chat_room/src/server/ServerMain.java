package server;

import server.controller.RequestProcessor;
import server.ui.ServerInfoFrame;

import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//�����������
public class ServerMain {
    public static void main(String[] args) {
    	//��ȡ����˶˿ں�
        int port = Integer.parseInt(DataBuffer.configProp.getProperty("port"));
        //��ʼ�������socket
        try {
            DataBuffer.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        new Thread(new Runnable() {//�������̼߳����ͻ�������
            public void run() {
                try {
                    while (true) {
                        // �����ͻ��˵�����
                        Socket socket = DataBuffer.serverSocket.accept();
                        System.out.println("�ͻ����ˣ�"
                                + socket.getInetAddress().getHostAddress()
                                + ":" + socket.getPort());

                        //��ÿ���ͻ�������һ���߳�  ���߳��е�����������������ÿ���ͻ��˵�����
                        new Thread(new RequestProcessor(socket)).start();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        //������������ش���
        new ServerInfoFrame();
    }
}

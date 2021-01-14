package client;

import client.model.entity.OnlineUserListModel;
import common.model.entity.User;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;
import java.util.Properties;


//�ͻ������ݻ���
public class DataBuffer {
	//��ǰ�ͻ��˵��û���Ϣ
    public static User currentUser;
    //�����û��б�
    public static List<User> onlineUsers;
    //��ǰ�ͻ������ӵ���������socket
    public static Socket clientSeocket;
    //��ǰ�ͻ������ӵ��������������
    public static ObjectOutputStream oos;
    //��ǰ�ͻ������ӵ��������������� 
    public static ObjectInputStream ois;
    //���������ò������Լ�
    public static Properties configProp;
    // ��ǰ�ͻ��˵���Ļ�ߴ�
    public static Dimension screenSize;
    //���ͻ��˵�IP��ַ 
    public static String ip ;
    //���������ļ��Ķ˿� 
    public static final int RECEIVE_FILE_PORT = 6667;
    // �����û�JList��Model 
    public static OnlineUserListModel onlineUserListModel;

    static{
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        //���ط����������ļ�
        configProp = new Properties();
        try {
        	//��ȡ����IP��ַ
            ip = InetAddress.getLocalHost().getHostAddress();
            //���������ж�ȡ�����б�����Ԫ�ضԣ�
            configProp.load(Thread.currentThread()
                    .getContextClassLoader()
                    .getResourceAsStream("serverconfig.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private DataBuffer(){}
}

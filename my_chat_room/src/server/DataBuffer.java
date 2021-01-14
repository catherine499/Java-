package server;

import common.model.entity.User;
import server.model.entity.OnlineUserTableModel;
import server.model.entity.RegistedUserTableModel;
import server.model.service.UserService;

import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentSkipListMap;

//��������ݻ���
public class DataBuffer {
    // ��������socket
    public static ServerSocket serverSocket;
    //�����û���IO Map
    public static Map<Long, OnlineClientIOCache> onlineUserIOCacheMap;
    //�����û�Map
    public static Map<Long, User> onlineUsersMap;
    //���������ò������Լ�
    public static Properties configProp;
    // ��ע���û����Model
    public static RegistedUserTableModel registedUserTableModel;
    // ��ǰ�����û����Model
    public static OnlineUserTableModel onlineUserTableModel;
    // ��ǰ����������ϵͳ����Ļ�ߴ�
    public static Dimension screenSize;
    // ��ǰ�û����ݿ�
    public static UserService userService;

    static{
        // ��ʼ��
    	//ConcurrentSkipListMap: �̰߳�ȫ������Ĺ�ϣ�� �����ڸ߲����ĳ���
        onlineUserIOCacheMap = new ConcurrentSkipListMap<Long,OnlineClientIOCache>(); //�����û���IO����map
        onlineUsersMap = new ConcurrentSkipListMap<Long, User>(); //�����û�map
        configProp = new Properties(); //���������ļ�
        registedUserTableModel = new RegistedUserTableModel(); //��ʼ��������ע���û�
        onlineUserTableModel = new OnlineUserTableModel(); //��ȡ�����û�
        screenSize = Toolkit.getDefaultToolkit().getScreenSize(); //��ȡ��Ļ�ߴ�
        userService = new UserService(); //����������

        // ���ط����������ļ�
        try {
            configProp.load(Thread.currentThread()
                    .getContextClassLoader()
                    .getResourceAsStream("serverconfig.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

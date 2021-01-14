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

//服务端数据缓存
public class DataBuffer {
    // 服务器端socket
    public static ServerSocket serverSocket;
    //在线用户的IO Map
    public static Map<Long, OnlineClientIOCache> onlineUserIOCacheMap;
    //在线用户Map
    public static Map<Long, User> onlineUsersMap;
    //服务器配置参数属性集
    public static Properties configProp;
    // 已注册用户表的Model
    public static RegistedUserTableModel registedUserTableModel;
    // 当前在线用户表的Model
    public static OnlineUserTableModel onlineUserTableModel;
    // 当前服务器所在系统的屏幕尺寸
    public static Dimension screenSize;
    // 当前用户数据库
    public static UserService userService;

    static{
        // 初始化
    	//ConcurrentSkipListMap: 线程安全的有序的哈希表 适用于高并发的场景
        onlineUserIOCacheMap = new ConcurrentSkipListMap<Long,OnlineClientIOCache>(); //在线用户的IO缓存map
        onlineUsersMap = new ConcurrentSkipListMap<Long, User>(); //在线用户map
        configProp = new Properties(); //创建配置文件
        registedUserTableModel = new RegistedUserTableModel(); //初始化几个已注册用户
        onlineUserTableModel = new OnlineUserTableModel(); //获取在线用户
        screenSize = Toolkit.getDefaultToolkit().getScreenSize(); //获取屏幕尺寸
        userService = new UserService(); //创建服务器

        // 加载服务器配置文件
        try {
            configProp.load(Thread.currentThread()
                    .getContextClassLoader()
                    .getResourceAsStream("serverconfig.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

package client;
import client.ui.LoginFrame;

import javax.swing.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


//客户端入口
public class ClientMain {
	 public static void main(String[] args) {
		    //连接到服务器
	        connection(); 

	        //启动登录窗体
	        new LoginFrame();	   
	     }

	    //连接到服务器 
	    public static void connection() {
	    	//得到服务器IP地址和端口号
	        String ip = DataBuffer.configProp.getProperty("ip");
	        int port = Integer.parseInt(DataBuffer.configProp.getProperty("port"));
	        try {
	        	//创建服务端socket
	            DataBuffer.clientSeocket = new Socket(ip, port);
	            DataBuffer.oos = new ObjectOutputStream(DataBuffer.clientSeocket.getOutputStream());
	            DataBuffer.ois = new ObjectInputStream(DataBuffer.clientSeocket.getInputStream());

	        } catch (Exception e) {
	        	//连接失败  弹出提示窗口
	            JOptionPane.showMessageDialog(new JFrame(),
	                    "连接服务器失败,请检查!","服务器连接失败！", JOptionPane.ERROR_MESSAGE);
	            System.exit(0);
	        }
	    }
}

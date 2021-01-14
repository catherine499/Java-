package client;
import client.ui.LoginFrame;

import javax.swing.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


//�ͻ������
public class ClientMain {
	 public static void main(String[] args) {
		    //���ӵ�������
	        connection(); 

	        //������¼����
	        new LoginFrame();	   
	     }

	    //���ӵ������� 
	    public static void connection() {
	    	//�õ�������IP��ַ�Ͷ˿ں�
	        String ip = DataBuffer.configProp.getProperty("ip");
	        int port = Integer.parseInt(DataBuffer.configProp.getProperty("port"));
	        try {
	        	//���������socket
	            DataBuffer.clientSeocket = new Socket(ip, port);
	            DataBuffer.oos = new ObjectOutputStream(DataBuffer.clientSeocket.getOutputStream());
	            DataBuffer.ois = new ObjectInputStream(DataBuffer.clientSeocket.getInputStream());

	        } catch (Exception e) {
	        	//����ʧ��  ������ʾ����
	            JOptionPane.showMessageDialog(new JFrame(),
	                    "���ӷ�����ʧ��,����!","����������ʧ�ܣ�", JOptionPane.ERROR_MESSAGE);
	            System.exit(0);
	        }
	    }
}

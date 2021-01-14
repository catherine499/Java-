package client;

import client.ui.ChatFrame;
import client.util.ClientUtil;
import common.model.entity.*;
import common.util.IOUtil;
import common.util.SocketUtil;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

//�ͻ����߳�  �������������͹�������Ϣ
public class ClientThread extends Thread {
	//��ǰ����
    private JFrame currentFrame;  

    public ClientThread(JFrame frame){
        currentFrame = frame;
    }

    public void run() {
        try {
        	//�ͻ������������������״̬
            while (DataBuffer.clientSeocket.isConnected()) {
            	//�ӿͻ��˵���������ȡ��������Ӧ��Ϣ
                Response response = (Response) DataBuffer.ois.readObject();
                ResponseType type = response.getType();

                System.out.println("��ȡ����Ӧ���ݣ�" + type);
                if (type == ResponseType.LOGIN) {//�û���¼
                    User newUser = (User)response.getData("loginUser");  //��ӵ������û��б�
                    DataBuffer.onlineUserListModel.addElement(newUser);
                  //�ڷ����������´�ӡ�����û���Ϣ
                    ChatFrame.onlineCountLbl.setText(
                            "�����û��б�("+ DataBuffer.onlineUserListModel.getSize() +")");
                    ClientUtil.appendTxt2MsgListArea("��ϵͳ��Ϣ���û�"+newUser.getNickname() + "�����ˣ�\n"); //�ڿͻ���ҳ����ʾ�û�������Ϣ
                }else if(type == ResponseType.LOGOUT){ //�û��˳�
                    User newUser = (User)response.getData("logoutUser"); //�������û��б�ɾ��
                    DataBuffer.onlineUserListModel.removeElement(newUser);
                  //�ڷ����������´�ӡ�����û���Ϣ
                    ChatFrame.onlineCountLbl.setText(
                            "�����û��б�("+ DataBuffer.onlineUserListModel.getSize() +")");
                    ClientUtil.appendTxt2MsgListArea("��ϵͳ��Ϣ���û�"+newUser.getNickname() + "�����ˣ�\n"); //�ڿͻ������������ʾ�û�����

                }else if(type == ResponseType.CHAT){ //����
                    Message msg = (Message)response.getData("txtMsg");
                    ClientUtil.appendTxt2MsgListArea(msg.getMessage());
                }else if(type == ResponseType.TOSENDFILE){ //׼�������ļ�
                    toSendFile(response);
                }else if(type == ResponseType.AGREERECEIVEFILE){ //�Է�ͬ������ļ�
                    sendFile(response);
                }else if(type == ResponseType.REFUSERECEIVEFILE){ //�Է��ܾ������ļ�
                    ClientUtil.appendTxt2MsgListArea("���ļ���Ϣ���Է��ܾ����գ��ļ�����ʧ�ܣ�\n");
                }else if(type == ResponseType.RECEIVEFILE){ //��ʼ�����ļ�
                    receiveFile(response);
                }else if(type == ResponseType.BOARD){  //���������͹㲥��Ϣ
                    Message msg = (Message)response.getData("txtMsg");
                    ClientUtil.appendTxt2MsgListArea(msg.getMessage());
                }else if(type == ResponseType.REMOVE){ //�������޳��û�
                    ChatFrame.remove();
                }
            }
        } catch (IOException e) {
            //e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

  //�����ļ�
  private void sendFile(Response response) {
  	//�����������ļ�����
      final FileInfo sendFile = (FileInfo)response.getData("sendFile");
      //������������ֽ���
      BufferedInputStream bis = null;
      BufferedOutputStream bos = null;
      Socket socket = null;
      try {
      	//�׽�������
          socket = new Socket(sendFile.getDestIp(),sendFile.getDestPort());
          //�ļ�����
          bis = new BufferedInputStream(new FileInputStream(sendFile.getSrcName()));
          //�ļ�д��
          bos = new BufferedOutputStream(socket.getOutputStream());
          //д�뻺����
          byte[] buffer = new byte[1024];
          int n = -1;
          while ((n = bis.read(buffer)) != -1){
              bos.write(buffer, 0, n);
          }
          bos.flush();
          synchronized (this) {
          	//��ʾ��Ϣ
              ClientUtil.appendTxt2MsgListArea("���ļ���Ϣ���ļ��������!\n");
          }
      } catch (IOException e) {
          e.printStackTrace();
      }finally{
          IOUtil.close(bis,bos);
          SocketUtil.close(socket);
      }
  }

  //�����ļ�
  private void receiveFile(Response response) {
  	//�����������ļ�����
      final FileInfo sendFile = (FileInfo)response.getData("sendFile");
      //������������ֽ���
      BufferedInputStream bis = null;
      BufferedOutputStream bos = null;
      ServerSocket serverSocket = null;
      Socket socket = null;
      try {
          serverSocket = new ServerSocket(sendFile.getDestPort());
          //����
          socket = serverSocket.accept(); 
          //�����
          bis = new BufferedInputStream(socket.getInputStream());
          //����д��
          bos = new BufferedOutputStream(new FileOutputStream(sendFile.getDestName()));

          byte[] buffer = new byte[1024];
          int n = -1;
          while ((n = bis.read(buffer)) != -1){
              bos.write(buffer, 0, n);
          }
          bos.flush();
          synchronized (this) {
          	//��ʾ��Ϣ
              ClientUtil.appendTxt2MsgListArea("���ļ���Ϣ���ļ��������!�����["
                      + sendFile.getDestName()+"]\n");
          }

      } catch (IOException e) {
          e.printStackTrace();
      }finally{
          IOUtil.close(bis,bos);
          SocketUtil.close(socket);
          SocketUtil.close(serverSocket);
      }
  }

  // ׼�������ļ�
  private void toSendFile(Response response) {
      FileInfo sendFile = (FileInfo)response.getData("sendFile");
      //��ȡ�������ǳ�
      String fromName = sendFile.getFromUser().getNickname()
              + "(" + sendFile.getFromUser().getId() + ")";
      //��ȡ�ļ�����
      String fileName = sendFile.getSrcName()
              .substring(sendFile.getSrcName().lastIndexOf(File.separator)+1);
      //������ʾ����  ����û���ѡ��
      int select = JOptionPane.showConfirmDialog(this.currentFrame,
              fromName + " ���������ļ� [" + fileName+ "]!\nͬ�������?",
              "�����ļ�", JOptionPane.YES_NO_OPTION);
      try {
          Request request = new Request();
          request.setAttribute("sendFile", sendFile);
          //�û�ͬ������ļ�
          if (select == JOptionPane.YES_OPTION) {
          	//ѡ������ļ��Ĵ�ŵ�ַ
              JFileChooser jfc = new JFileChooser();
              jfc.setSelectedFile(new File(fileName));
              //��ַѡ����
              int result = jfc.showSaveDialog(this.currentFrame);
              //��ַû������
              if (result == JFileChooser.APPROVE_OPTION){
                  //����Ŀ�ĵ��ļ���
                  sendFile.setDestName(jfc.getSelectedFile().getCanonicalPath());
                  //����Ŀ��ص�IP�ͽ����ļ��Ķ˿�
                  sendFile.setDestIp(DataBuffer.ip);
                  sendFile.setDestPort(DataBuffer.RECEIVE_FILE_PORT);

                  request.setAction("agreeReceiveFile");
                  ClientUtil.appendTxt2MsgListArea("���ļ���Ϣ������ͬ��������� "
                          + fromName +" ���ļ������ڽ����ļ� ...\n");
              } else {//��ַѡ�������δѡ���ַ
                  request.setAction("refuseReceiveFile");
                  ClientUtil.appendTxt2MsgListArea("���ļ���Ϣ�����Ѿܾ��������� "
                          + fromName +" ���ļ�!\n");
              }
          } else {//�ܾ������ļ�
              request.setAction("refuseReceiveFile");
              ClientUtil.appendTxt2MsgListArea("���ļ���Ϣ�����Ѿܾ��������� "
                      + fromName +" ���ļ�!\n");
          }

          ClientUtil.sendTextRequest2(request);
      } catch (IOException e) {
          e.printStackTrace();
      }
  }
}


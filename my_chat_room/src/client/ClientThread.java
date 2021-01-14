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

//客户端线程  监听服务器发送过来的信息
public class ClientThread extends Thread {
	//当前窗体
    private JFrame currentFrame;  

    public ClientThread(JFrame frame){
        currentFrame = frame;
    }

    public void run() {
        try {
        	//客户端与服务器处于连接状态
            while (DataBuffer.clientSeocket.isConnected()) {
            	//从客户端得输入流读取服务器响应信息
                Response response = (Response) DataBuffer.ois.readObject();
                ResponseType type = response.getType();

                System.out.println("获取了响应内容：" + type);
                if (type == ResponseType.LOGIN) {//用户登录
                    User newUser = (User)response.getData("loginUser");  //添加到在线用户列表
                    DataBuffer.onlineUserListModel.addElement(newUser);
                  //在服务器端重新打印在线用户信息
                    ChatFrame.onlineCountLbl.setText(
                            "在线用户列表("+ DataBuffer.onlineUserListModel.getSize() +")");
                    ClientUtil.appendTxt2MsgListArea("【系统消息】用户"+newUser.getNickname() + "上线了！\n"); //在客户端页面提示用户上线信息
                }else if(type == ResponseType.LOGOUT){ //用户退出
                    User newUser = (User)response.getData("logoutUser"); //从在线用户列表删除
                    DataBuffer.onlineUserListModel.removeElement(newUser);
                  //在服务器端重新打印在线用户信息
                    ChatFrame.onlineCountLbl.setText(
                            "在线用户列表("+ DataBuffer.onlineUserListModel.getSize() +")");
                    ClientUtil.appendTxt2MsgListArea("【系统消息】用户"+newUser.getNickname() + "下线了！\n"); //在客户端聊天界面提示用户下线

                }else if(type == ResponseType.CHAT){ //聊天
                    Message msg = (Message)response.getData("txtMsg");
                    ClientUtil.appendTxt2MsgListArea(msg.getMessage());
                }else if(type == ResponseType.TOSENDFILE){ //准备发送文件
                    toSendFile(response);
                }else if(type == ResponseType.AGREERECEIVEFILE){ //对方同意接收文件
                    sendFile(response);
                }else if(type == ResponseType.REFUSERECEIVEFILE){ //对方拒绝接收文件
                    ClientUtil.appendTxt2MsgListArea("【文件消息】对方拒绝接收，文件发送失败！\n");
                }else if(type == ResponseType.RECEIVEFILE){ //开始接收文件
                    receiveFile(response);
                }else if(type == ResponseType.BOARD){  //服务器发送广播消息
                    Message msg = (Message)response.getData("txtMsg");
                    ClientUtil.appendTxt2MsgListArea(msg.getMessage());
                }else if(type == ResponseType.REMOVE){ //服务器剔除用户
                    ChatFrame.remove();
                }
            }
        } catch (IOException e) {
            //e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

  //发送文件
  private void sendFile(Response response) {
  	//创建待发送文件对象
      final FileInfo sendFile = (FileInfo)response.getData("sendFile");
      //输入输出缓冲字节流
      BufferedInputStream bis = null;
      BufferedOutputStream bos = null;
      Socket socket = null;
      try {
      	//套接字连接
          socket = new Socket(sendFile.getDestIp(),sendFile.getDestPort());
          //文件读入
          bis = new BufferedInputStream(new FileInputStream(sendFile.getSrcName()));
          //文件写出
          bos = new BufferedOutputStream(socket.getOutputStream());
          //写入缓冲区
          byte[] buffer = new byte[1024];
          int n = -1;
          while ((n = bis.read(buffer)) != -1){
              bos.write(buffer, 0, n);
          }
          bos.flush();
          synchronized (this) {
          	//提示消息
              ClientUtil.appendTxt2MsgListArea("【文件消息】文件发送完毕!\n");
          }
      } catch (IOException e) {
          e.printStackTrace();
      }finally{
          IOUtil.close(bis,bos);
          SocketUtil.close(socket);
      }
  }

  //接收文件
  private void receiveFile(Response response) {
  	//创建待发送文件对象
      final FileInfo sendFile = (FileInfo)response.getData("sendFile");
      //输入输出缓冲字节流
      BufferedInputStream bis = null;
      BufferedOutputStream bos = null;
      ServerSocket serverSocket = null;
      Socket socket = null;
      try {
          serverSocket = new ServerSocket(sendFile.getDestPort());
          //接收
          socket = serverSocket.accept(); 
          //缓冲读
          bis = new BufferedInputStream(socket.getInputStream());
          //缓冲写出
          bos = new BufferedOutputStream(new FileOutputStream(sendFile.getDestName()));

          byte[] buffer = new byte[1024];
          int n = -1;
          while ((n = bis.read(buffer)) != -1){
              bos.write(buffer, 0, n);
          }
          bos.flush();
          synchronized (this) {
          	//提示信息
              ClientUtil.appendTxt2MsgListArea("【文件消息】文件接收完毕!存放在["
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

  // 准备发送文件
  private void toSendFile(Response response) {
      FileInfo sendFile = (FileInfo)response.getData("sendFile");
      //获取发送者昵称
      String fromName = sendFile.getFromUser().getNickname()
              + "(" + sendFile.getFromUser().getId() + ")";
      //获取文件名称
      String fileName = sendFile.getSrcName()
              .substring(sendFile.getSrcName().lastIndexOf(File.separator)+1);
      //弹出提示窗口  获得用户的选择
      int select = JOptionPane.showConfirmDialog(this.currentFrame,
              fromName + " 向您发送文件 [" + fileName+ "]!\n同意接收吗?",
              "接收文件", JOptionPane.YES_NO_OPTION);
      try {
          Request request = new Request();
          request.setAttribute("sendFile", sendFile);
          //用户同意接受文件
          if (select == JOptionPane.YES_OPTION) {
          	//选择接收文件的存放地址
              JFileChooser jfc = new JFileChooser();
              jfc.setSelectedFile(new File(fileName));
              //地址选择结果
              int result = jfc.showSaveDialog(this.currentFrame);
              //地址没有问题
              if (result == JFileChooser.APPROVE_OPTION){
                  //设置目的地文件名
                  sendFile.setDestName(jfc.getSelectedFile().getCanonicalPath());
                  //设置目标地的IP和接收文件的端口
                  sendFile.setDestIp(DataBuffer.ip);
                  sendFile.setDestPort(DataBuffer.RECEIVE_FILE_PORT);

                  request.setAction("agreeReceiveFile");
                  ClientUtil.appendTxt2MsgListArea("【文件消息】您已同意接收来自 "
                          + fromName +" 的文件，正在接收文件 ...\n");
              } else {//地址选择有误或未选择地址
                  request.setAction("refuseReceiveFile");
                  ClientUtil.appendTxt2MsgListArea("【文件消息】您已拒绝接收来自 "
                          + fromName +" 的文件!\n");
              }
          } else {//拒绝接受文件
              request.setAction("refuseReceiveFile");
              ClientUtil.appendTxt2MsgListArea("【文件消息】您已拒绝接收来自 "
                      + fromName +" 的文件!\n");
          }

          ClientUtil.sendTextRequest2(request);
      } catch (IOException e) {
          e.printStackTrace();
      }
  }
}


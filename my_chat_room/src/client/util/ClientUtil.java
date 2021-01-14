package client.util;

import java.io.IOException;

import client.DataBuffer;
import client.ui.ChatFrame;
import common.model.entity.Request;
import common.model.entity.Response;

//�ͻ��˷������󵽷������Ĺ���
public class ClientUtil {
	//�����������,����������Ӧ
    public static Response sendTextRequest(Request request) throws IOException {
        Response response = null;
        try {
            // ��������
            DataBuffer.oos.writeObject(request);
            DataBuffer.oos.flush();
            System.out.println("�ͻ��˷������������:" + request.getAction());

            if(!"exit".equals(request.getAction())){
                // ��ȡ��Ӧ
                response = (Response) DataBuffer.ois.readObject();
                System.out.println("�ͻ��˻�ȡ������Ӧ����:" + response.getStatus());
            }else{
                System.out.println("�ͻ��˶Ͽ�����");
            }
        } catch (IOException e) {
            throw e;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return response;
    }

    //�����������,������������Ӧ
    public static void sendTextRequest2(Request request) throws IOException {
        try {
            DataBuffer.oos.writeObject(request); // ��������
            DataBuffer.oos.flush();
            System.out.println("�ͻ��˷������������:" + request.getAction());
        } catch (IOException e) {
            throw e;
        }
    }

    //��ָ���ı���ӵ���Ϣ�б��ı�����
    public static void appendTxt2MsgListArea(String txt) {
        ChatFrame.msgListArea.append(txt);
        //�ѹ�궨λ���ı�������һ��
        ChatFrame.msgListArea.setCaretPosition(ChatFrame.msgListArea.getDocument().getLength());
    }
}

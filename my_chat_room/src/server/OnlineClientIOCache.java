package server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


//�����û���IO��������
public class OnlineClientIOCache {
    //���ͬһ��Socket�л�ȡ������ȫ�ַ�Χ�����ֻ��װһ�Σ��������
    private ObjectInputStream ois; //����������
    private ObjectOutputStream oos; //���������

    public OnlineClientIOCache(ObjectInputStream ois, ObjectOutputStream oos){
        this.ois = ois;
        this.oos = oos;
    }

    public ObjectOutputStream getOos(){
        return oos;
    }

    public ObjectInputStream getOis() {
        return ois;
    }

}

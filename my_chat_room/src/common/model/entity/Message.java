package common.model.entity;

import java.io.Serializable;
import java.util.Date;



//��Ϣ��
public class Message implements Serializable {
    private static final long serialVersionUID = 1820192075144114657L;
    //��Ϣ������
    private User toUser;
    //��Ϣ������
    private User fromUser;
    //��Ϣ����
    private String message;
    //��Ϣ����ʱ��
    private Date sendTime;


    public User getToUser() {
        return toUser;
    }
    public void setToUser(User toUser) {
        this.toUser = toUser;
    }
    public User getFromUser() {
        return fromUser;
    }
    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public Date getSendTime() {
        return sendTime;
    }
    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }
}

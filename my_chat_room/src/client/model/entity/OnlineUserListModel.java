package client.model.entity;


import java.util.List;
import javax.swing.AbstractListModel;
import common.model.entity.User;

//�����û��б���
public class OnlineUserListModel extends AbstractListModel {
	private static final long serialVersionUID = -3903760573171074301L;
    private List<User> onlineUsers;

    public OnlineUserListModel(List<User> onlineUsers) {
        this.onlineUsers = onlineUsers;
    }
    //����µ�¼�û�
    public void addElement(Object object) {
        if (onlineUsers.contains(object)) {
            return;
        }
        int index = onlineUsers.size();
        onlineUsers.add((User)object);
        fireIntervalAdded(this, index, index);
    }
    //ɾ���˳��û�
    public boolean removeElement(Object object) {
        int index = onlineUsers.indexOf(object);
        if (index >= 0) {
            fireIntervalRemoved(this, index, index);
        }
        return onlineUsers.remove(object);
    }
    //��ȡ��������
    public int getSize() {
        return onlineUsers.size();
    }

    public Object getElementAt(int i) {
        return onlineUsers.get(i);
    }

    public List<User> getOnlineUsers() {
        return onlineUsers;
    }
	
}

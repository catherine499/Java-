package client.model.entity;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import common.model.entity.User;

//��Ⱦ��  �����촰��չʾ�����û�
public class MyCellRenderer extends JLabel implements ListCellRenderer  {
	 private static final long serialVersionUID = 3460394416991636990L;

	    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
	        //��ȡ�û�
	    	User user = (User)value;
	    	//��ȡ�û��ǳƺ��˺�
	        String name = user.getNickname() + "(" + user.getId() + ")";
	        //��ӡ�û��ǳƺ��˺�
	        setText(name);
	        //��ȡ�û�ͷ��ͼƬ
	        setIcon(user.getHeadIcon());
	        //�û��Ƿ�ѡ��  �ı䱳����ɫ
	        if (isSelected) {
	            setBackground(list.getSelectionBackground());
	            setForeground(list.getSelectionForeground());
	        } else {
	            setBackground(list.getBackground());
	            setForeground(list.getForeground());
	        }
	        //����Ϊ�ɵ��
	        setEnabled(list.isEnabled());
	        //��������
	        setFont(list.getFont());
	        //ȷ��������ɫ���ᵲסͷ�������
	        setOpaque(true);
	        return this;
	    }
}

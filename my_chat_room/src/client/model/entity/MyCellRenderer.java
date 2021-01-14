package client.model.entity;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import common.model.entity.User;

//渲染器  在聊天窗口展示在线用户
public class MyCellRenderer extends JLabel implements ListCellRenderer  {
	 private static final long serialVersionUID = 3460394416991636990L;

	    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
	        //获取用户
	    	User user = (User)value;
	    	//获取用户昵称和账号
	        String name = user.getNickname() + "(" + user.getId() + ")";
	        //打印用户昵称和账号
	        setText(name);
	        //获取用户头像图片
	        setIcon(user.getHeadIcon());
	        //用户是否被选中  改变背景颜色
	        if (isSelected) {
	            setBackground(list.getSelectionBackground());
	            setForeground(list.getSelectionForeground());
	        } else {
	            setBackground(list.getBackground());
	            setForeground(list.getForeground());
	        }
	        //设置为可点击
	        setEnabled(list.isEnabled());
	        //设置字体
	        setFont(list.getFont());
	        //确保背景颜色不会挡住头像和文字
	        setOpaque(true);
	        return this;
	    }
}

package client.ui;

import client.ClientThread;
import client.DataBuffer;
import client.model.entity.MyCellRenderer;
import client.model.entity.OnlineUserListModel;
import client.util.ClientUtil;
import common.model.entity.FileInfo;
import common.model.entity.Message;
import common.model.entity.Request;
import common.model.entity.User;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

//聊天窗口
public class ChatFrame extends JFrame{
    private static final long serialVersionUID = -2310785591507878535L;
    //聊天对方的信息Label
    private JLabel otherInfoLbl;
    //当前用户信息Lbl
    private JLabel currentUserLbl;
    //聊天信息列表区域
    public static JTextArea msgListArea;
    //要发送的信息区域
    public static JTextArea sendArea;
    //在线用户列表 
    public static JList onlineList;
    // 在线用户数统计Lbl 
    public static JLabel onlineCountLbl;
    // 准备发送的文件
    public static FileInfo sendFile;
    // 私聊复选框
    public JCheckBox rybqBtn;

    public ChatFrame(){
        this.init();
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }
    //初始化
    public void init(){
        this.setTitle("MY CHART ROOM");
        this.setSize(550, 500);
        this.setResizable(false);

        //设置默认窗体在屏幕中央
        int x = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        int y = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        this.setLocation((x - this.getWidth()) / 2, (y-this.getHeight())/ 2);

        //左边用户面板
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BorderLayout());
        //右边主面板
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        
        
        // 创建一个分隔窗格
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                userPanel, mainPanel);
        splitPane.setDividerLocation(125);
        splitPane.setDividerSize(10);
        splitPane.setOneTouchExpandable(true);
        this.add(splitPane, BorderLayout.CENTER);

        
        //在线用户列表展示
        JPanel onlineListPane = new JPanel();
        onlineListPane.setLayout(new BorderLayout());
        onlineCountLbl = new JLabel("在线用户");
        onlineListPane.add(onlineCountLbl, BorderLayout.NORTH);

        //当前用户面板
        JPanel currentUserPane = new JPanel();
        currentUserPane.setLayout(new BorderLayout());
        Border border = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        currentUserPane.setBorder(BorderFactory.createTitledBorder(border,
                "当前用户", TitledBorder.LEFT,TitledBorder.TOP));
        this.add(currentUserPane, BorderLayout.NORTH);

        // 右边用户列表创建一个分隔窗格
        JSplitPane splitPane3 = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
        		currentUserPane, onlineListPane);
        splitPane3.setDividerLocation(60);
        splitPane3.setDividerSize(1);
        userPanel.add(splitPane3, BorderLayout.CENTER);

        //获取在线用户并缓存
        DataBuffer.onlineUserListModel = new OnlineUserListModel(DataBuffer.onlineUsers);
        //在线用户列表
        onlineList = new JList(DataBuffer.onlineUserListModel);
        onlineList.setCellRenderer(new MyCellRenderer());
        //设置为单选模式
        onlineList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        onlineListPane.add(new JScrollPane(onlineList,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));

        //当前用户信息Label
        currentUserLbl = new JLabel();
        currentUserPane.add(currentUserLbl);
        
        
        //右上方信息显示面板
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BorderLayout());
        //右下方发送消息面板
        JPanel sendPanel = new JPanel();
        sendPanel.setLayout(new BorderLayout());

        // 创建一个分隔窗格
        JSplitPane splitPane2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                infoPanel, sendPanel);
        splitPane2.setDividerLocation(300);
        splitPane2.setDividerSize(1);
        mainPanel.add(splitPane2, BorderLayout.CENTER);

        otherInfoLbl = new JLabel("当前状态：群聊中...");
        infoPanel.add(otherInfoLbl, BorderLayout.NORTH);

        msgListArea = new JTextArea();
        msgListArea.setLineWrap(true);
        //给信息窗口添加滚动条
        infoPanel.add(new JScrollPane(msgListArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));

        JPanel tempPanel = new JPanel();
        tempPanel.setLayout(new BorderLayout());
        sendPanel.add(tempPanel, BorderLayout.NORTH);

        // 聊天按钮面板
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        tempPanel.add(btnPanel, BorderLayout.CENTER);

        //字体按钮
        JButton fontBtn = new JButton(new ImageIcon("images/font.png"));
        fontBtn.setMargin(new Insets(0,0,0,0));
        fontBtn.setToolTipText("设置字体和格式");
        btnPanel.add(fontBtn);

        //表情按钮
        JButton faceBtn = new JButton(new ImageIcon("images/sendFace.png"));
        faceBtn.setMargin(new Insets(0,0,0,0));
        faceBtn.setToolTipText("选择表情");
        btnPanel.add(faceBtn);

        //发送文件按钮
        JButton sendFileBtn = new JButton(new ImageIcon("images/sendPic.png"));
        sendFileBtn.setMargin(new Insets(0,0,0,0));
        sendFileBtn.setToolTipText("向对方发送文件");
        btnPanel.add(sendFileBtn);

        //私聊按钮
        rybqBtn = new JCheckBox("私聊");
        tempPanel.add(rybqBtn, BorderLayout.EAST);

        //要发送的信息的区域
        sendArea = new JTextArea();
        sendArea.setLineWrap(true);
        sendPanel.add(new JScrollPane(sendArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));

        // 聊天按钮面板
        JPanel btn2Panel = new JPanel();
        btn2Panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        this.add(btn2Panel, BorderLayout.SOUTH);
        JButton closeBtn = new JButton("关闭");
        closeBtn.setToolTipText("退出程序");
        btn2Panel.add(closeBtn);
        JButton submitBtn = new JButton("发送");
        submitBtn.setToolTipText("按Enter键发送消息");
        btn2Panel.add(submitBtn);
        sendPanel.add(btn2Panel, BorderLayout.SOUTH);



        ///////////////////////////注册事件监听器////////////////////////////
        //关闭窗口
        this.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e) {
                logout();
            }
        });

        //关闭按钮的事件
        closeBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                logout();
            }
        });

        //选择某个用户私聊
        rybqBtn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if(rybqBtn.isSelected()){
                    User selectedUser = (User)onlineList.getSelectedValue();
                    if(null == selectedUser){
                        otherInfoLbl.setText("当前状态：私聊(从在线用户列表中选择某个用户进行私聊)...");
                    }else if(DataBuffer.currentUser.getId() == selectedUser.getId()){
                        otherInfoLbl.setText("警告：不允许和自己私聊！！！");
                    }else{
                        otherInfoLbl.setText("当前状态：与 "+ selectedUser.getNickname()
                                +"(" + selectedUser.getId() + ") 私聊中...");
                    }
                }else{
                    otherInfoLbl.setText("当前状态：群聊...");
                }
            }
        });

        //选择某个用户
        onlineList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                User selectedUser = (User)onlineList.getSelectedValue();
                if(rybqBtn.isSelected()){
                    if(DataBuffer.currentUser.getId() == selectedUser.getId()){
                        otherInfoLbl.setText("警告：不允许和自己私聊！！！");
                    }else{
                        otherInfoLbl.setText("当前状态：与 "+ selectedUser.getNickname()
                                +"(" + selectedUser.getId() + ") 私聊中...");
                    }
                }
            }
        });

        //发送文本消息
        //回车发送
        sendArea.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    sendTxtMsg();
                }
            }
        });
        //点击按钮发送
        submitBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                sendTxtMsg();
            }
        });


        //发送文件
        sendFileBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                sendFile();
            }
        });

        this.loadData();  //加载初始数据
    }

    
    //加载数据
    public void loadData(){
        //加载当前用户数据
        if(null != DataBuffer.currentUser){
        	//头像
            currentUserLbl.setIcon(
                    new ImageIcon("images/" + DataBuffer.currentUser.getHead() + ".png"));
            //用户昵称和账号
            currentUserLbl.setText(DataBuffer.currentUser.getNickname()
                    + "(" + DataBuffer.currentUser.getId() + ")");
        }
        //设置在线用户列表
        onlineCountLbl.setText("在线用户列表("+ DataBuffer.onlineUserListModel.getSize() +")");
        //启动监听服务器消息的线程
        new ClientThread(this).start();
    }

    //发送文本消息
    public void sendTxtMsg(){
        String content = sendArea.getText();
        if ("".equals(content)) { //无内容
            JOptionPane.showMessageDialog(ChatFrame.this, "不能发送空消息!",
                    "不能发送", JOptionPane.ERROR_MESSAGE);
        } else { //发送
            User selectedUser = (User)onlineList.getSelectedValue();

            //如果设置了ToUser 表示私聊，否则群聊
            Message msg = new Message();
            if(rybqBtn.isSelected()){  //私聊
                if(null == selectedUser){//私聊对象为空
                    JOptionPane.showMessageDialog(ChatFrame.this, "没有选择私聊对象!",
                            "不能发送", JOptionPane.ERROR_MESSAGE);
                    return;
                }else if (DataBuffer.currentUser.getId() == selectedUser.getId()){//私聊对象为自己
                    JOptionPane.showMessageDialog(ChatFrame.this, "不能给自己发送消息!",
                            "不能发送", JOptionPane.ERROR_MESSAGE);
                    return;
                }else{
                    msg.setToUser(selectedUser);
                }
            }
            //获取系统时间
            msg.setFromUser(DataBuffer.currentUser);
            msg.setSendTime(new Date());
            DateFormat df = new SimpleDateFormat("HH:mm:ss");
            //存储消息的相关信息
            StringBuffer sb = new StringBuffer();
            sb.append(" ").append(df.format(msg.getSendTime())).append(" ")
                    .append(msg.getFromUser().getNickname())
                    .append("(").append(msg.getFromUser().getId()).append(") ");
            if(!this.rybqBtn.isSelected()){ //群聊
                sb.append("对大家说");
            }
            sb.append("\n  ").append(content).append("\n");
            msg.setMessage(sb.toString());
            //创建请求对象  存储消息信息
            Request request = new Request();
            request.setAction("chat");
            request.setAttribute("msg", msg);
            try {
            	//发送请求
                ClientUtil.sendTextRequest2(request);
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            //JTextArea 中发送消息后，清空内容并回到首行
            InputMap inputMap = sendArea.getInputMap();
            ActionMap actionMap = sendArea.getActionMap();
            Object transferTextActionKey = "TRANSFER_TEXT";
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0),transferTextActionKey);
            actionMap.put(transferTextActionKey,new AbstractAction() {
                private static final long serialVersionUID = 7041841945830590229L;
                public void actionPerformed(ActionEvent e) {
                    sendArea.setText("");
                    sendArea.requestFocus();
                }
            });
            sendArea.setText("");
            ClientUtil.appendTxt2MsgListArea(msg.getMessage());
        }
    }

    //发送文件
    private void sendFile() {
        User selectedUser = (User)onlineList.getSelectedValue();
        if(null != selectedUser){ //选择了发送文件的对象
            if(DataBuffer.currentUser.getId() == selectedUser.getId()){
                JOptionPane.showMessageDialog(ChatFrame.this, "不能给自己发送文件!",
                        "不能发送", JOptionPane.ERROR_MESSAGE);
            }else{ 
            	//选择要发送的文件
                JFileChooser jfc = new JFileChooser();
                if (jfc.showOpenDialog(ChatFrame.this) == JFileChooser.APPROVE_OPTION) {
                    File file = jfc.getSelectedFile();
                    //创建要发送的文件对象 存储文件内容及相关信息
                    sendFile = new FileInfo();
                    //设置文件发送者
                    sendFile.setFromUser(DataBuffer.currentUser);
                    //设置文件接收者
                    sendFile.setToUser(selectedUser);
                    try {
                    	//待发送文件的源地址及文件名
                        sendFile.setSrcName(file.getCanonicalPath());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    //设置发送时间
                    sendFile.setSendTime(new Date());
                    //创建请求对象
                    Request request = new Request();
                    request.setAction("toSendFile");
                    request.setAttribute("file", sendFile);
                    try {
                    	//发送请求
                        ClientUtil.sendTextRequest2(request);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //打印提示信息
                    ClientUtil.appendTxt2MsgListArea("【文件消息】向 "
                            + selectedUser.getNickname() + "("
                            + selectedUser.getId() + ") 发送文件 ["
                            + file.getName() + "]，等待对方接收...\n");
                }
            }
        }else{
            JOptionPane.showMessageDialog(ChatFrame.this, "警告：只能给指定用户发送文件！！！",
                    "不能发送", JOptionPane.ERROR_MESSAGE);
        }
    }

    //关闭客户端
    private void logout() {
    	//弹出提示窗口
        int select = JOptionPane.showConfirmDialog(ChatFrame.this,
                "确定要退出吗？\n\n退出程序将会中断与服务器的连接!", "退出聊天室",
                JOptionPane.YES_NO_OPTION);
        //选择退出
        if (select == JOptionPane.YES_OPTION) {
        	//创建请求对象
            Request req = new Request();
            req.setAction("exit");
            req.setAttribute("user", DataBuffer.currentUser);
            try {
            	//发送请求
                ClientUtil.sendTextRequest(req);
            } catch (IOException ex) {
                ex.printStackTrace();
            }finally{
                System.exit(0);
            }
        }else{
        	// 未选择退出 不响应
            this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        }
    }

    //服务器踢除用户
    public static void remove() {
        int select = JOptionPane.showConfirmDialog(sendArea,
                "你已被踢出聊天室！\n\n", "系统通知",
                JOptionPane.YES_NO_OPTION);
        //创建请求对象   等同于用户退出
        Request req = new Request();
        req.setAction("exit");
        req.setAttribute("user", DataBuffer.currentUser);
        try {
            ClientUtil.sendTextRequest(req);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            System.exit(0);
        }

    }
}

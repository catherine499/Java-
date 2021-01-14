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

//���촰��
public class ChatFrame extends JFrame{
    private static final long serialVersionUID = -2310785591507878535L;
    //����Է�����ϢLabel
    private JLabel otherInfoLbl;
    //��ǰ�û���ϢLbl
    private JLabel currentUserLbl;
    //������Ϣ�б�����
    public static JTextArea msgListArea;
    //Ҫ���͵���Ϣ����
    public static JTextArea sendArea;
    //�����û��б� 
    public static JList onlineList;
    // �����û���ͳ��Lbl 
    public static JLabel onlineCountLbl;
    // ׼�����͵��ļ�
    public static FileInfo sendFile;
    // ˽�ĸ�ѡ��
    public JCheckBox rybqBtn;

    public ChatFrame(){
        this.init();
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }
    //��ʼ��
    public void init(){
        this.setTitle("MY CHART ROOM");
        this.setSize(550, 500);
        this.setResizable(false);

        //����Ĭ�ϴ�������Ļ����
        int x = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        int y = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        this.setLocation((x - this.getWidth()) / 2, (y-this.getHeight())/ 2);

        //����û����
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BorderLayout());
        //�ұ������
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        
        
        // ����һ���ָ�����
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                userPanel, mainPanel);
        splitPane.setDividerLocation(125);
        splitPane.setDividerSize(10);
        splitPane.setOneTouchExpandable(true);
        this.add(splitPane, BorderLayout.CENTER);

        
        //�����û��б�չʾ
        JPanel onlineListPane = new JPanel();
        onlineListPane.setLayout(new BorderLayout());
        onlineCountLbl = new JLabel("�����û�");
        onlineListPane.add(onlineCountLbl, BorderLayout.NORTH);

        //��ǰ�û����
        JPanel currentUserPane = new JPanel();
        currentUserPane.setLayout(new BorderLayout());
        Border border = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        currentUserPane.setBorder(BorderFactory.createTitledBorder(border,
                "��ǰ�û�", TitledBorder.LEFT,TitledBorder.TOP));
        this.add(currentUserPane, BorderLayout.NORTH);

        // �ұ��û��б���һ���ָ�����
        JSplitPane splitPane3 = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
        		currentUserPane, onlineListPane);
        splitPane3.setDividerLocation(60);
        splitPane3.setDividerSize(1);
        userPanel.add(splitPane3, BorderLayout.CENTER);

        //��ȡ�����û�������
        DataBuffer.onlineUserListModel = new OnlineUserListModel(DataBuffer.onlineUsers);
        //�����û��б�
        onlineList = new JList(DataBuffer.onlineUserListModel);
        onlineList.setCellRenderer(new MyCellRenderer());
        //����Ϊ��ѡģʽ
        onlineList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        onlineListPane.add(new JScrollPane(onlineList,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));

        //��ǰ�û���ϢLabel
        currentUserLbl = new JLabel();
        currentUserPane.add(currentUserLbl);
        
        
        //���Ϸ���Ϣ��ʾ���
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BorderLayout());
        //���·�������Ϣ���
        JPanel sendPanel = new JPanel();
        sendPanel.setLayout(new BorderLayout());

        // ����һ���ָ�����
        JSplitPane splitPane2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                infoPanel, sendPanel);
        splitPane2.setDividerLocation(300);
        splitPane2.setDividerSize(1);
        mainPanel.add(splitPane2, BorderLayout.CENTER);

        otherInfoLbl = new JLabel("��ǰ״̬��Ⱥ����...");
        infoPanel.add(otherInfoLbl, BorderLayout.NORTH);

        msgListArea = new JTextArea();
        msgListArea.setLineWrap(true);
        //����Ϣ������ӹ�����
        infoPanel.add(new JScrollPane(msgListArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));

        JPanel tempPanel = new JPanel();
        tempPanel.setLayout(new BorderLayout());
        sendPanel.add(tempPanel, BorderLayout.NORTH);

        // ���찴ť���
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        tempPanel.add(btnPanel, BorderLayout.CENTER);

        //���尴ť
        JButton fontBtn = new JButton(new ImageIcon("images/font.png"));
        fontBtn.setMargin(new Insets(0,0,0,0));
        fontBtn.setToolTipText("��������͸�ʽ");
        btnPanel.add(fontBtn);

        //���鰴ť
        JButton faceBtn = new JButton(new ImageIcon("images/sendFace.png"));
        faceBtn.setMargin(new Insets(0,0,0,0));
        faceBtn.setToolTipText("ѡ�����");
        btnPanel.add(faceBtn);

        //�����ļ���ť
        JButton sendFileBtn = new JButton(new ImageIcon("images/sendPic.png"));
        sendFileBtn.setMargin(new Insets(0,0,0,0));
        sendFileBtn.setToolTipText("��Է������ļ�");
        btnPanel.add(sendFileBtn);

        //˽�İ�ť
        rybqBtn = new JCheckBox("˽��");
        tempPanel.add(rybqBtn, BorderLayout.EAST);

        //Ҫ���͵���Ϣ������
        sendArea = new JTextArea();
        sendArea.setLineWrap(true);
        sendPanel.add(new JScrollPane(sendArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));

        // ���찴ť���
        JPanel btn2Panel = new JPanel();
        btn2Panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        this.add(btn2Panel, BorderLayout.SOUTH);
        JButton closeBtn = new JButton("�ر�");
        closeBtn.setToolTipText("�˳�����");
        btn2Panel.add(closeBtn);
        JButton submitBtn = new JButton("����");
        submitBtn.setToolTipText("��Enter��������Ϣ");
        btn2Panel.add(submitBtn);
        sendPanel.add(btn2Panel, BorderLayout.SOUTH);



        ///////////////////////////ע���¼�������////////////////////////////
        //�رմ���
        this.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e) {
                logout();
            }
        });

        //�رհ�ť���¼�
        closeBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                logout();
            }
        });

        //ѡ��ĳ���û�˽��
        rybqBtn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if(rybqBtn.isSelected()){
                    User selectedUser = (User)onlineList.getSelectedValue();
                    if(null == selectedUser){
                        otherInfoLbl.setText("��ǰ״̬��˽��(�������û��б���ѡ��ĳ���û�����˽��)...");
                    }else if(DataBuffer.currentUser.getId() == selectedUser.getId()){
                        otherInfoLbl.setText("���棺��������Լ�˽�ģ�����");
                    }else{
                        otherInfoLbl.setText("��ǰ״̬���� "+ selectedUser.getNickname()
                                +"(" + selectedUser.getId() + ") ˽����...");
                    }
                }else{
                    otherInfoLbl.setText("��ǰ״̬��Ⱥ��...");
                }
            }
        });

        //ѡ��ĳ���û�
        onlineList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                User selectedUser = (User)onlineList.getSelectedValue();
                if(rybqBtn.isSelected()){
                    if(DataBuffer.currentUser.getId() == selectedUser.getId()){
                        otherInfoLbl.setText("���棺��������Լ�˽�ģ�����");
                    }else{
                        otherInfoLbl.setText("��ǰ״̬���� "+ selectedUser.getNickname()
                                +"(" + selectedUser.getId() + ") ˽����...");
                    }
                }
            }
        });

        //�����ı���Ϣ
        //�س�����
        sendArea.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    sendTxtMsg();
                }
            }
        });
        //�����ť����
        submitBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                sendTxtMsg();
            }
        });


        //�����ļ�
        sendFileBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                sendFile();
            }
        });

        this.loadData();  //���س�ʼ����
    }

    
    //��������
    public void loadData(){
        //���ص�ǰ�û�����
        if(null != DataBuffer.currentUser){
        	//ͷ��
            currentUserLbl.setIcon(
                    new ImageIcon("images/" + DataBuffer.currentUser.getHead() + ".png"));
            //�û��ǳƺ��˺�
            currentUserLbl.setText(DataBuffer.currentUser.getNickname()
                    + "(" + DataBuffer.currentUser.getId() + ")");
        }
        //���������û��б�
        onlineCountLbl.setText("�����û��б�("+ DataBuffer.onlineUserListModel.getSize() +")");
        //����������������Ϣ���߳�
        new ClientThread(this).start();
    }

    //�����ı���Ϣ
    public void sendTxtMsg(){
        String content = sendArea.getText();
        if ("".equals(content)) { //������
            JOptionPane.showMessageDialog(ChatFrame.this, "���ܷ��Ϳ���Ϣ!",
                    "���ܷ���", JOptionPane.ERROR_MESSAGE);
        } else { //����
            User selectedUser = (User)onlineList.getSelectedValue();

            //���������ToUser ��ʾ˽�ģ�����Ⱥ��
            Message msg = new Message();
            if(rybqBtn.isSelected()){  //˽��
                if(null == selectedUser){//˽�Ķ���Ϊ��
                    JOptionPane.showMessageDialog(ChatFrame.this, "û��ѡ��˽�Ķ���!",
                            "���ܷ���", JOptionPane.ERROR_MESSAGE);
                    return;
                }else if (DataBuffer.currentUser.getId() == selectedUser.getId()){//˽�Ķ���Ϊ�Լ�
                    JOptionPane.showMessageDialog(ChatFrame.this, "���ܸ��Լ�������Ϣ!",
                            "���ܷ���", JOptionPane.ERROR_MESSAGE);
                    return;
                }else{
                    msg.setToUser(selectedUser);
                }
            }
            //��ȡϵͳʱ��
            msg.setFromUser(DataBuffer.currentUser);
            msg.setSendTime(new Date());
            DateFormat df = new SimpleDateFormat("HH:mm:ss");
            //�洢��Ϣ�������Ϣ
            StringBuffer sb = new StringBuffer();
            sb.append(" ").append(df.format(msg.getSendTime())).append(" ")
                    .append(msg.getFromUser().getNickname())
                    .append("(").append(msg.getFromUser().getId()).append(") ");
            if(!this.rybqBtn.isSelected()){ //Ⱥ��
                sb.append("�Դ��˵");
            }
            sb.append("\n  ").append(content).append("\n");
            msg.setMessage(sb.toString());
            //�����������  �洢��Ϣ��Ϣ
            Request request = new Request();
            request.setAction("chat");
            request.setAttribute("msg", msg);
            try {
            	//��������
                ClientUtil.sendTextRequest2(request);
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            //JTextArea �з�����Ϣ��������ݲ��ص�����
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

    //�����ļ�
    private void sendFile() {
        User selectedUser = (User)onlineList.getSelectedValue();
        if(null != selectedUser){ //ѡ���˷����ļ��Ķ���
            if(DataBuffer.currentUser.getId() == selectedUser.getId()){
                JOptionPane.showMessageDialog(ChatFrame.this, "���ܸ��Լ������ļ�!",
                        "���ܷ���", JOptionPane.ERROR_MESSAGE);
            }else{ 
            	//ѡ��Ҫ���͵��ļ�
                JFileChooser jfc = new JFileChooser();
                if (jfc.showOpenDialog(ChatFrame.this) == JFileChooser.APPROVE_OPTION) {
                    File file = jfc.getSelectedFile();
                    //����Ҫ���͵��ļ����� �洢�ļ����ݼ������Ϣ
                    sendFile = new FileInfo();
                    //�����ļ�������
                    sendFile.setFromUser(DataBuffer.currentUser);
                    //�����ļ�������
                    sendFile.setToUser(selectedUser);
                    try {
                    	//�������ļ���Դ��ַ���ļ���
                        sendFile.setSrcName(file.getCanonicalPath());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    //���÷���ʱ��
                    sendFile.setSendTime(new Date());
                    //�����������
                    Request request = new Request();
                    request.setAction("toSendFile");
                    request.setAttribute("file", sendFile);
                    try {
                    	//��������
                        ClientUtil.sendTextRequest2(request);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //��ӡ��ʾ��Ϣ
                    ClientUtil.appendTxt2MsgListArea("���ļ���Ϣ���� "
                            + selectedUser.getNickname() + "("
                            + selectedUser.getId() + ") �����ļ� ["
                            + file.getName() + "]���ȴ��Է�����...\n");
                }
            }
        }else{
            JOptionPane.showMessageDialog(ChatFrame.this, "���棺ֻ�ܸ�ָ���û������ļ�������",
                    "���ܷ���", JOptionPane.ERROR_MESSAGE);
        }
    }

    //�رտͻ���
    private void logout() {
    	//������ʾ����
        int select = JOptionPane.showConfirmDialog(ChatFrame.this,
                "ȷ��Ҫ�˳���\n\n�˳����򽫻��ж��������������!", "�˳�������",
                JOptionPane.YES_NO_OPTION);
        //ѡ���˳�
        if (select == JOptionPane.YES_OPTION) {
        	//�����������
            Request req = new Request();
            req.setAction("exit");
            req.setAttribute("user", DataBuffer.currentUser);
            try {
            	//��������
                ClientUtil.sendTextRequest(req);
            } catch (IOException ex) {
                ex.printStackTrace();
            }finally{
                System.exit(0);
            }
        }else{
        	// δѡ���˳� ����Ӧ
            this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        }
    }

    //�������߳��û�
    public static void remove() {
        int select = JOptionPane.showConfirmDialog(sendArea,
                "���ѱ��߳������ң�\n\n", "ϵͳ֪ͨ",
                JOptionPane.YES_NO_OPTION);
        //�����������   ��ͬ���û��˳�
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

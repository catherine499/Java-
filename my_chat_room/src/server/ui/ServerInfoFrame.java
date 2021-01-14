package server.ui;

import common.model.entity.User;
import server.DataBuffer;
import server.controller.RequestProcessor;
import server.model.service.UserService;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

//����������
public class ServerInfoFrame extends JFrame {
    private static final long serialVersionUID = 6274443611957724780L;
    //������Ϣ���봰��
    private JTextField jta_msg;
    //�����û���
    private JTable onlineUserTable ;
    //��ע���û���
    private JTable registedUserTable ;

    public ServerInfoFrame() {
        init();
        loadData();
        setVisible(true);
    }

    public void init() {  //��ʼ������
        this.setTitle("����������");//���÷�������������
        this.setBounds((DataBuffer.screenSize.width - 700)/2,
                (DataBuffer.screenSize.height - 475)/2, 700, 475);
        this.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        Border border = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        panel.setBorder(BorderFactory.createTitledBorder(border,
                "������������", TitledBorder.LEFT,TitledBorder.TOP));
        this.add(panel, BorderLayout.NORTH);

      
        int port = Integer.parseInt(DataBuffer.configProp.getProperty("port")); //��ȡ����˶˿ں�
        JLabel label = new JLabel("�������˿�: \n"+port);
        panel.add(label);
        JButton exitBtn = new JButton("�رշ�����");//�رչرշ�������ť
        panel.add(exitBtn);

        JLabel la_msg = new JLabel("Ҫ���͵���Ϣ");
        panel.add(la_msg);
        // ������Ҫ������Ϣ�������
        jta_msg = new JTextField(30);
        // ����һ���������� �󣺷��͹㲥��Ϣ
        ActionListener sendCaseMsgAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    sendAllMsg();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        };

        // �������Ӻ��¼������������س�ʱ����
        jta_msg.addActionListener(sendCaseMsgAction);
        JButton bu_send = new JButton("Send");
        // ����ť���Ϸ��͹㲥��Ϣ�ļ�����
        bu_send.addActionListener(sendCaseMsgAction);
        panel.add(jta_msg);
        panel.add(bu_send);

        //ʹ�÷����������е�TableModel
        onlineUserTable = new JTable(DataBuffer.onlineUserTableModel);
        registedUserTable = new JTable(DataBuffer.registedUserTableModel);

        // ȡ�ñ���ϵĵ����˵�����,�ӵ������
        JPopupMenu pop = getTablePop();
        onlineUserTable.setComponentPopupMenu(pop);

        //ѡ�
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("�����û��б�", new JScrollPane(onlineUserTable));
        tabbedPane.addTab("��ע���û��б�", new JScrollPane(registedUserTable));
        tabbedPane.setTabComponentAt(0, new JLabel("�����û��б�"));
        this.add(tabbedPane, BorderLayout.CENTER);

        final JLabel stateBar = new JLabel("", SwingConstants.RIGHT);
        stateBar.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        //�ö�ʱ��������ʾ��ǰʱ��
        new java.util.Timer().scheduleAtFixedRate(
                new TimerTask(){
                    DateFormat df = new SimpleDateFormat("yyyy��MM��dd�� HH:mm:ss");
                    public void run() {
                        stateBar.setText("��ǰʱ�䣺" + df.format(new Date()) + "  ");
                    }
                }, 0, 1000);
        this.add(stateBar, BorderLayout.SOUTH); //��״̬����ӵ�������ϱ�

        //�رմ���
        this.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e) {
                logout();
            }
        });

        /* ��ӹرշ�������ť�¼������� */
        exitBtn.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent event) {
                logout();
            }
        });
    }

     //��������ϵĵ����˵����� ʵ�ַ��� ���˹���
    private JPopupMenu getTablePop() {
        JPopupMenu pop = new JPopupMenu(); //�����˵�����
        JMenuItem mi_send = new JMenuItem("����");
        // �˵������
        mi_send.setActionCommand("send"); //�趨�˵�����ؼ���
        JMenuItem mi_del = new JMenuItem("�ߵ�"); //�˵������
        mi_del.setActionCommand("del"); //�趨�˵�����ؼ���
        // �����˵��ϵ��¼�����������
        ActionListener al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String s = e.getActionCommand();
                // �ĸ��˵������� ���s�������趨��ActionCommand
                popMenuAction(s);
            }
        };
        mi_send.addActionListener(al);
        mi_del.addActionListener(al);// ���˵����ϼ�����
        pop.add(mi_send);
        pop.add(mi_del);
        return pop;
    }

    // �������˵��ϵ��¼�
    private void popMenuAction(String command) {
        // �õ��ڱ����ѡ�е���
        final int selectIndex = onlineUserTable.getSelectedRow();
        String usr_id = (String)onlineUserTable.getValueAt(selectIndex,0);
        System.out.println(usr_id);
        if (selectIndex == -1) {
            JOptionPane.showMessageDialog(this, "��ѡ��һ���û�");
            return;
        }

        if (command.equals("del")) {
            // ���߳����Ƴ������̶߳���
            try {
                RequestProcessor.remove(DataBuffer.onlineUsersMap.get(Long.valueOf(usr_id)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (command.equals("send")) {
            final JDialog jd = new JDialog(this, true);// ���ͶԻ���
            jd.setLayout(new FlowLayout());
            jd.setSize(200, 100);
            final JTextField jtd_m = new JTextField(20);
            JButton jb = new JButton("����");
            jd.add(jtd_m);
            jd.add(jb);
            // ���Ͱ�ť���¼�ʵ��
            jb.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.out.println("������������һ����Ϣ...");
                    String msg = jtd_m.getText();
                    try {
                        RequestProcessor.chat_sys(msg,DataBuffer.onlineUsersMap.get(Long.valueOf(usr_id)));
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    jtd_m.setText("");// ��������
                    jd.dispose();
                }
            });
            jd.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "δ֪�˵�:" + command);
        }
        // ˢ�±��
        SwingUtilities.updateComponentTreeUI(onlineUserTable);
    }

    // ���·��ͷ�������Ϣ�İ�ť�������������û�������Ϣ
    private void sendAllMsg() throws IOException {
        RequestProcessor.board(jta_msg.getText());
        jta_msg.setText(""); //��������
    }

    //��������ע����û���Ϣ���ص�RegistedUserTableModel�� 
    private void loadData(){
        List<User> users = new UserService().loadAllUser();
        for (User user : users) {
            DataBuffer.registedUserTableModel.add(new String[]{
                    String.valueOf(user.getId()),
                    user.getPassword(),
                    user.getNickname(),
                    String.valueOf(user.getSex())
            });
        }
    }

    //�رշ�����
    private void logout() {
    	//������ʾ����
        int select = JOptionPane.showConfirmDialog(ServerInfoFrame.this,
                "ȷ���ر���\n\n�رշ��������ж������пͻ��˵�����!",
                "�رշ�����",
                JOptionPane.YES_NO_OPTION);
        //����û�������ǹرշ�������ťʱ����ʾ�Ƿ�ȷ�Ϲرա�
        if (select == JOptionPane.YES_OPTION) {
            System.exit(0);//�˳�ϵͳ
        }else{
            //����Ĭ�ϵĴ��ڹر��¼�����
            setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        }
    }
}

package client.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import client.util.ClientUtil;
import common.model.entity.Request;
import common.model.entity.Response;
import common.model.entity.ResponseStatus;
import common.model.entity.User;
import server.DataBuffer;


//ע�����
public class RegisterFrame extends JFrame {
    private static final long serialVersionUID = -768631070458723803L;
    private JPasswordField pwdFld;
    private JPasswordField pwd2Fld;
    private JTextField nickname;
    private JComboBox head;
    private JRadioButton sex0;
    private JRadioButton sex1;
    private JButton ok;
    private JButton reset;
    private JButton cancel;

    public RegisterFrame(){
        this.init();
        setVisible(true);
    }

    public void init(){
        this.setTitle("ע�����˺�");//���ñ���
        setBounds((DataBuffer.screenSize.width - 387)/2,
                (DataBuffer.screenSize.height - 267)/2,
                387, 267);
        getContentPane().setLayout(null);
        setResizable(false);

        JLabel label = new JLabel("�ǳ�:"); //label��ʾ
        label.setBounds(24, 36, 59, 17);
        getContentPane().add(label);

        nickname = new JTextField(); //�ǳ�
        nickname.setBounds(90, 34, 110, 22);
        getContentPane().add(nickname);

        JLabel label5 = new JLabel("����: *");
        label5.setBounds(24, 72, 50, 17);
        getContentPane().add(label5);

        JLabel label3 = new JLabel("ȷ������: *");
        label3.setBounds(24, 107, 65, 17);
        getContentPane().add(label3);

        pwdFld = new JPasswordField(); //�����
        pwdFld.setBounds(90, 70, 110, 22);
        getContentPane().add(pwdFld);

        pwd2Fld = new JPasswordField(); //ȷ�������
        pwd2Fld.setBounds(90, 105, 110, 22);
        getContentPane().add(pwd2Fld);

        JLabel label4 = new JLabel("�Ա�:");
        label4.setBounds(230, 36, 31, 17);
        getContentPane().add(label4);

        sex1 = new JRadioButton("Ů",true); //�Ա�ѡ��
        sex1.setBounds (268, 31,44, 25);
        getContentPane().add(sex1);
        sex0 = new JRadioButton("��");
        sex0.setBounds(310, 31, 44, 25);
        getContentPane().add(sex0);
        ButtonGroup buttonGroup = new ButtonGroup(); //��ѡ��ť��
        buttonGroup.add(sex0);
        buttonGroup.add(sex1);

        JLabel label6 = new JLabel("ͷ��:");
        label6.setBounds(230, 72, 31, 17);
        getContentPane().add(label6);

        head = new JComboBox(); //�����б�ͼ��
        head.setBounds(278, 70, 65, 45);
        head.setMaximumRowCount(5);
        for (int i = 0; i < 13; i++) {
            head.addItem(new ImageIcon("images/" + i + ".png"));
            //ͨ��ѭ�����ͼƬ ע��ͼƬ����Ҫȡ��1,2,3,4,5,��
        }
        head.setSelectedIndex(0);
        getContentPane().add(head);

        //��ť
        ok = new JButton("ȷ��");
        ok.setBounds(27, 176, 60, 28);
        getContentPane().add(ok);

        reset = new JButton("����");
        reset.setBounds(123, 176, 60, 28);
        getContentPane().add(reset);

        cancel = new JButton("ȡ��");
        cancel.setBounds(268, 176, 60, 28);
        getContentPane().add(cancel);

        //////////////////////ע���¼�������////////////////////////
        //ȡ����ť�����¼�����
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent event) {
                RegisterFrame.this.dispose();
            }
        });
        //�رմ���
        this.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e) {
                RegisterFrame.this.dispose();
            }
        });

        // ���ð�ť�����¼�����
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                nickname.setText("");
                pwdFld.setText("");
                pwd2Fld.setText("");
                nickname.requestFocusInWindow(); //�û�����ý���
            }
        });

        //ȷ�ϰ�ť�����¼�����
        ok.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                if (pwdFld.getPassword().length==0 || pwd2Fld.getPassword().length==0) {
                    JOptionPane.showMessageDialog(RegisterFrame.this, "�� �� * �� Ϊ��������!");
                    //�ж��û����������Ƿ�Ϊ��
                } else if (!new String(pwdFld.getPassword()).equals(new String(pwd2Fld.getPassword()))) {
                    JOptionPane.showMessageDialog(RegisterFrame.this, "�����������벻һ��!");
                    pwdFld.setText("");
                    pwd2Fld.setText("");
                    pwdFld.requestFocusInWindow();
                    //�ж����������Ƿ�һ��
                } else {
                    User user = new User(new String(pwdFld.getPassword()),
                            nickname.getText(),
                            sex0.isSelected() ? 'm' : 'f',
                            head.getSelectedIndex());
                    try {
                        RegisterFrame.this.regist(user);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (ClassNotFoundException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }
    //ע��
    private void regist(User user) throws IOException, ClassNotFoundException{
        Request request = new Request();
        request.setAction("userRegist");
        request.setAttribute("user", user);

        //��ȡ��Ӧ
        Response response = ClientUtil.sendTextRequest(request);

        ResponseStatus status = response.getStatus();
        switch(status){
            case OK:
                User user2 = (User)response.getData("user");
                JOptionPane.showMessageDialog(RegisterFrame.this,
                        "ע��ɹ��������˺�Ϊ :"+ user2.getId() + ",���μ�!!!",
                        "ע��ɹ�",JOptionPane.INFORMATION_MESSAGE);
                this.setVisible(false);
                break;
            default:
                JOptionPane.showMessageDialog(RegisterFrame.this,
                        "ע��ʧ�ܣ����Ժ����ԣ�����","�������ڲ�����",JOptionPane.ERROR_MESSAGE);
        }
    }
}

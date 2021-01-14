package client.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import client.DataBuffer;
import client.util.ClientUtil;
import common.model.entity.Request;
import common.model.entity.Response;
import common.model.entity.ResponseStatus;
import common.model.entity.User;

//��¼����
public class LoginFrame extends JFrame {
    private static final long serialVersionUID = -3426717670093483287L;

    private JTextField idTxt;
    private JPasswordField pwdFld;

    public LoginFrame(){
        this.init();
        setVisible(true);
    }

    public void init(){
        this.setTitle("��¼");
        this.setSize(430, 330);
        //����Ĭ�ϴ�������Ļ����
        int x = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        int y = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        this.setLocation((x - this.getWidth()) / 2, (y-this.getHeight())/ 2);
        this.setResizable(false);

        //��Logo���õ�JFrame�ı���
        Icon icon = new ImageIcon("images/logo.png");
        JLabel label = new JLabel(icon);
        label.setPreferredSize(new Dimension(430,150));
        this.add(label, BorderLayout.NORTH);

        //��¼��Ϣ���
        JPanel mainPanel = new JPanel();
        Border border = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        mainPanel.setBorder(BorderFactory.createTitledBorder(border, "�����¼��Ϣ", TitledBorder.CENTER,TitledBorder.TOP));
        this.add(mainPanel, BorderLayout.CENTER);
        mainPanel.setLayout(null);

        JLabel nameLbl = new JLabel("�˺�:");
        nameLbl.setBounds(110, 30, 70, 22);
        mainPanel.add(nameLbl);
        idTxt = new JTextField();
        idTxt.setBounds(150, 30, 150, 22);
        idTxt.requestFocusInWindow();//�û�����ý���
        mainPanel.add(idTxt);

        JLabel pwdLbl = new JLabel("����:");
        pwdLbl.setBounds(110, 60, 40, 22);
        mainPanel.add(pwdLbl);
        pwdFld = new JPasswordField();
        pwdFld.setBounds(150, 60, 150, 22);
        mainPanel.add(pwdFld);

        //��ť��������JFrame���ϱ�
        JPanel btnPanel = new JPanel();
        this.add(btnPanel, BorderLayout.SOUTH);
        btnPanel.setLayout(new BorderLayout());
        btnPanel.setBorder(new EmptyBorder(2, 8, 4, 8));

        JButton registeBtn = new JButton("ע��");
        btnPanel.add(registeBtn, BorderLayout.WEST);
        JButton submitBtn = new JButton("��¼");
        btnPanel.add(submitBtn, BorderLayout.EAST);

        //�رմ���
        this.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e) {
                Request req = new Request();
                req.setAction("exit");
                try {
                    ClientUtil.sendTextRequest(req);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }finally{
                    System.exit(0);
                }
            }
        });

        //ע��
        registeBtn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                new RegisterFrame();  //��ע�ᴰ��
            }
        });

        //��¼
        submitBtn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
    }

    //��¼
    @SuppressWarnings("unchecked")
    private void login() {
        if (idTxt.getText().length() == 0
                || pwdFld.getPassword().length == 0) {
            JOptionPane.showMessageDialog(LoginFrame.this,
                    "�������˺ź����룡",
                    "��������",JOptionPane.ERROR_MESSAGE);
            idTxt.requestFocusInWindow();
            return;
        }

        if(!idTxt.getText().matches("^\\d*$")){
            JOptionPane.showMessageDialog(LoginFrame.this,
                    "�˺ű��������֣�",
                    "��������",JOptionPane.ERROR_MESSAGE);
            idTxt.requestFocusInWindow();
            return;
        }
        //��������
        Request req = new Request();
        req.setAction("userLogin");
        req.setAttribute("id", idTxt.getText());
        req.setAttribute("password", new String(pwdFld.getPassword()));

        //��ȡ��Ӧ
        Response response = null;
        try {
            response = ClientUtil.sendTextRequest(req);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        if(response.getStatus() == ResponseStatus.OK){
            //��ȡ��ǰ�û�
            User user2 = (User)response.getData("user");
            if(user2!= null){ //��¼�ɹ�
                DataBuffer.currentUser = user2;
                //��ȡ��ǰ�����û��б�
                DataBuffer.onlineUsers = (List<User>)response.getData("onlineUsers");

                LoginFrame.this.dispose();
                new ChatFrame();  //�����촰��
            }else{ //��¼ʧ��
                String str = (String)response.getData("msg");
                JOptionPane.showMessageDialog(LoginFrame.this,
                        str,
                        "��¼ʧ��",JOptionPane.ERROR_MESSAGE);
            }
        }else{
            JOptionPane.showMessageDialog(LoginFrame.this,
                    "�������ڲ��������Ժ����ԣ�����","��¼ʧ��",JOptionPane.ERROR_MESSAGE);
        }
    }
}

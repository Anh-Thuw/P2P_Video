package Client.tmpl;

import java.awt.Color;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;

import Client.services.CheckInfor;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.net.Socket;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Login extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField tf_tdn;
	private JPasswordField jf_pass;
	private JButton btn_login;
	private JButton btn_register;
	private JLabel lb_title;
	private JLabel lb_tdn;
	private JLabel lb_pass;
	private JLabel lb_text;
	private Socket socket ; 
	static Register_1 signup;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Login() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 450);
		setResizable(false);
		setTitle("Meet Team - Login");

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screenSize.width - getWidth()) / 2;
		int y = (screenSize.height - getHeight()) / 2;
		setLocation(x, y);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		lb_title = new JLabel("MEET TEAM-LOGIN");
		lb_title.setFont(new Font("Tahoma", Font.BOLD, 24));
		lb_title.setBounds(171, 21, 258, 50);
		contentPane.add(lb_title);

		lb_tdn = new JLabel("Tên đăng nhập :");
		lb_tdn.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lb_tdn.setBounds(50, 100, 150, 30);
		contentPane.add(lb_tdn);

		tf_tdn = new JTextField();
		tf_tdn.setFont(new Font("Tahoma", Font.PLAIN, 16));
		tf_tdn.setBounds(210, 100, 300, 30);
		contentPane.add(tf_tdn);

		lb_pass = new JLabel("Mật khẩu:");
		lb_pass.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lb_pass.setBounds(50, 160, 150, 30);
		contentPane.add(lb_pass);

		jf_pass = new JPasswordField();
		jf_pass.setFont(new Font("Tahoma", Font.PLAIN, 16));
		jf_pass.setBounds(210, 160, 300, 30);
		contentPane.add(jf_pass);

		btn_login = new JButton("Đăng nhập");
		btn_login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					doLogin();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		btn_login.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btn_login.setBackground(new Color(70, 130, 180));
		btn_login.setForeground(Color.WHITE);
		btn_login.setBounds(210, 220, 150, 40);
		contentPane.add(btn_login);

		lb_text = new JLabel("Bạn đã có tài khoản chưa?");
		lb_text.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lb_text.setBounds(220, 280, 200, 30);
		contentPane.add(lb_text);

		btn_register = new JButton("Đăng kí");
		btn_register.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					doSign();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		btn_register.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btn_register.setBackground(new Color(34, 139, 34));
		btn_register.setForeground(Color.WHITE);
		btn_register.setBounds(210, 320, 150, 40);
		contentPane.add(btn_register);
	}
	public void doLogin() throws Exception {
		
		socket = new Socket("localhost", 1234);
		CheckInfor checkInfor = new CheckInfor(socket, tf_tdn.getText(), jf_pass.getText(),null ,  this);
		checkInfor.doLogin();
		checkInfor.start();
		
		tf_tdn.setText("");
		jf_pass.setText("");

	}
	public void doSign() throws Exception {
		socket = new Socket("localhost", 1234);
		signup = new Register_1(socket);
		setVisible(false);
		signup.setVisible(true);
	}

	public String getPass(char[] pass) {
		String p = "";
		for (char x:pass) {
			p+= x;
		}
		return p;
	}
}

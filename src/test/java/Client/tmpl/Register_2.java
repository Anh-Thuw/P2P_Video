package Client.tmpl;

import Client.services.CheckInfor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import javax.swing.JCheckBox;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.Socket;
import java.awt.event.ActionEvent;

public class Register_2 extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField tf_username;
    private JButton btn_register;
    private JButton btn_clear;
    private JButton btn_back;
    private JPasswordField tf_pass1;
    private JPasswordField tf_pass2;
    private Socket socket;
    private String email;
    private CheckInfor checkInfor;
    private JCheckBox showPass;

    public Register_2(Socket socket, String email) {
		try {
			this.socket = socket;
			this.checkInfor = new CheckInfor(socket, null, null, null, this);
		    this.email = email;
	        Frame_Register_2();
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    public void Frame_Register_2() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 500, 400);
        setResizable(false);
        setTitle("Meet Team - Register");

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;
        setLocation(x, y);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setBackground(new Color(245, 245, 245));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lbl_title = new JLabel("MEET TEAM - REGISTER");
        lbl_title.setFont(new Font("Tahoma", Font.BOLD, 22));
        lbl_title.setForeground(new Color(70, 130, 180));
        lbl_title.setBounds(100, 20, 300, 30);
        contentPane.add(lbl_title);

        JLabel lbl_username = new JLabel("Username:");
        lbl_username.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lbl_username.setBounds(50, 70, 150, 25);
        contentPane.add(lbl_username);

        tf_username = new JTextField();
        tf_username.setFont(new Font("Tahoma", Font.PLAIN, 16));
        tf_username.setBounds(200, 70, 230, 30);
        contentPane.add(tf_username);

        // New Password label and field
        JLabel lbl_newPassword = new JLabel("New Password:");
        lbl_newPassword.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lbl_newPassword.setBounds(50, 130, 150, 25);
        contentPane.add(lbl_newPassword);

        tf_pass1 = new JPasswordField();
        tf_pass1.setFont(new Font("Tahoma", Font.PLAIN, 16));
        tf_pass1.setBounds(200, 130, 230, 30);
        contentPane.add(tf_pass1);

        // Confirm Password label and field
        JLabel lbl_confirmPassword = new JLabel("Confirm Password:");
        lbl_confirmPassword.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lbl_confirmPassword.setBounds(50, 190, 150, 25);
        contentPane.add(lbl_confirmPassword);

        tf_pass2 = new JPasswordField();
        tf_pass2.setFont(new Font("Tahoma", Font.PLAIN, 16));
        tf_pass2.setBounds(200, 190, 230, 30);
        contentPane.add(tf_pass2);

        // Show password checkbox for Confirm Password
        showPass = new JCheckBox("Show Password");
        showPass.setFont(new Font("Tahoma", Font.PLAIN, 15));
        showPass.setBounds(200, 226, 150, 20);
        showPass.setBackground(new Color(245, 245, 245));
        showPass.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(showPass.isSelected()) {
					tf_pass1.setEchoChar((char)0);
					tf_pass2.setEchoChar((char)0);
					//checkNewPass.setEchoChar((char)0);
				} else {
					tf_pass1.setEchoChar('*');
					tf_pass2.setEchoChar('*');
					//checkNewPass.setEchoChar('*');
				}
			}
		});
        contentPane.add(showPass);

        // Register button
        btn_register = new JButton("REGISTER");
        btn_register.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		if (checkSignUp()) {
					try {
						doSignup();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				} 
        	}
        });
        btn_register.setFont(new Font("Tahoma", Font.BOLD, 16));
        btn_register.setBackground(new Color(34, 139, 34));
        btn_register.setForeground(Color.WHITE);
        btn_register.setBounds(342, 269, 134, 40); 
        contentPane.add(btn_register);

        btn_clear = new JButton("CLEAR");
        btn_clear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tf_username.setText("");
                tf_pass1.setText("");
                tf_pass2.setText("");
            }
        });
        btn_clear.setFont(new Font("Tahoma", Font.BOLD, 16));
        btn_clear.setBackground(new Color(30, 144, 255));
        btn_clear.setForeground(Color.WHITE);
        btn_clear.setBounds(178, 269, 134, 40); 
        contentPane.add(btn_clear);

        // Back button
        btn_back = new JButton("BACK");
        btn_back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Back clicked");
            }
        });
        btn_back.setFont(new Font("Tahoma", Font.BOLD, 16));
        btn_back.setBackground(new Color(220, 20, 60));
        btn_back.setForeground(Color.WHITE);
        btn_back.setBounds(10, 269, 134, 40); 
        contentPane.add(btn_back);
    }

    public boolean checkSignUp() {
		if (tf_username.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Please enter all text!");
			return false;
		}
		return rootPaneCheckingEnabled;
		

	}
	
	public void doSignup() throws Exception {
		checkInfor.doSignup(email ,tf_username.getText() ,tf_pass1.getText() , tf_pass2.getText() );
	}
	
	public String getPass(char[] pass) {
		String p = "";
		for (char x:pass) {
			p+= x;
		}
		return p;
	}
}

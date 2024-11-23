package Client.tmpl;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.net.Socket;
import java.net.URL;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.swing.JPasswordField;



public class SignUp extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private Color panel;
	public JButton btnNewButton_1_1 = new JButton("BACK");
	public JButton btnSignUp = new JButton("SIGN UP");
	public JPasswordField passwordField,passwordField_1;
	private BufferedWriter bufferedWriter;
	private BufferedReader bufferedReader;
	private Socket socket ;
	//private ConnectDatabase conDB = new ConnectDatabase();

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					SignUp frame = new SignUp();
//					frame.setVisible(true);
//					frame.setResizable(false);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the frame.
	 */
	public SignUp() {
			
			setForeground(new Color(128, 0, 0));
			this.setTitle("SIGN UP");
		//	URL MainIcon = SignUp.class.getResource("/com/images/top_signup.png");
		//	Image img = Toolkit.getDefaultToolkit().createImage(MainIcon);
		//	this.setIconImage(img);
			
			setFont(new Font("Tahoma", Font.BOLD, 25));
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setBounds(100, 100, 1000, 500);
			setSize(1000,500);
			setResizable(false);
			getContentPane().setLayout(null);
			
			contentPane = new JPanel();
			contentPane.setBackground(new Color(244, 250, 255));
			contentPane.setForeground(new Color(128, 0, 0));
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	
			setContentPane(contentPane);
			contentPane.setLayout(null);
			
//			JLabel lblNewLabel_1 = new JLabel(new ImageIcon( new ImageIcon(
//					Toolkit.getDefaultToolkit().getImage(
//							SignUp.class.getResource("/com/images/icon_human.jpg")
//							)
//					).getImage().getScaledInstance(253, 253, Image.SCALE_DEFAULT)));
//			lblNewLabel_1.setBounds(10, 112, 253, 209);
//			contentPane.add(lblNewLabel_1);
			
			JLabel lblNewLabel_7 = new JLabel("* START CREATING AN ACCOUNT");
			lblNewLabel_7.setForeground(new Color(118, 54, 224));
			lblNewLabel_7.setFont(new Font("Tahoma", Font.BOLD, 25));
			lblNewLabel_7.setBounds(349, 50, 532, 47);
			contentPane.add(lblNewLabel_7);
			
			JLabel lblNewLabel_8 = new JLabel("Enter user name:");
			lblNewLabel_8.setFont(new Font("Tahoma", Font.PLAIN, 20));
			lblNewLabel_8.setBounds(273, 130, 181, 47);
			contentPane.add(lblNewLabel_8);
			
			JLabel lblNewLabel_9 = new JLabel("Enter the password:");
			lblNewLabel_9.setFont(new Font("Tahoma", Font.PLAIN, 20));
			lblNewLabel_9.setBounds(273, 196, 202, 47);
			contentPane.add(lblNewLabel_9);
			
			JLabel lblNewLabel_10 = new JLabel("Re-enter the password:");
			lblNewLabel_10.setFont(new Font("Tahoma", Font.PLAIN, 20));
			lblNewLabel_10.setBounds(273, 254, 215, 47);
			contentPane.add(lblNewLabel_10);
			
			textField = new JTextField();
			textField.setFont(new Font("Tahoma", Font.PLAIN, 20));
			textField.setColumns(10);
			textField.setBounds(516, 134, 460, 40);
			contentPane.add(textField);
			
			passwordField = new JPasswordField();
			passwordField.setFont(new Font("Tahoma", Font.PLAIN, 20));
			passwordField.setBounds(516, 200, 460, 40);
			contentPane.add(passwordField);
			
			passwordField_1 = new JPasswordField();
			passwordField_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
			passwordField_1.setBounds(516, 265, 460, 40);
			contentPane.add(passwordField_1);
			
			JButton btnClean = new JButton("CLEAN");
			btnClean.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					textField.setText("");
					passwordField.setText("");
					passwordField_1.setText("");
					
					
					
					
				}
			});
			btnClean.setForeground(new Color(128, 0, 0));
			btnClean.setFont(new Font("Tahoma", Font.BOLD, 20));
			btnClean.setBackground(new Color(255, 227, 227));
			btnClean.setBounds(527, 377, 168, 47);
			contentPane.add(btnClean);
			
			
			btnSignUp.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					if (checkSignUp()) {
						try {
							doSignup();
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					} 
					
					
					
				}
			});
			btnSignUp.setForeground(new Color(0, 0, 96));
			btnSignUp.setFont(new Font("Tahoma", Font.BOLD, 20));
			btnSignUp.setBackground(new Color(140, 198, 255));
			btnSignUp.setBounds(764, 377, 168, 47);
			contentPane.add(btnSignUp);
			
			//nhấn vào nút Back ở trang SignUp thì trở về trang Login_Signup
			btnNewButton_1_1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
	                    if ( JOptionPane.showConfirmDialog(null, "Any information will not be saved. Do you still want to get out?") == 0) {
						
						setVisible(false);
					//	ls.setVisible(true);
						
					}
					
				}
			});
			btnNewButton_1_1.setForeground(new Color(128, 0, 0));
			btnNewButton_1_1.setFont(new Font("Tahoma", Font.BOLD, 20));
			btnNewButton_1_1.setBackground(new Color(255, 255, 221));
			btnNewButton_1_1.setBounds(295, 377, 157, 47);
			contentPane.add(btnNewButton_1_1);
			
			
	//		checkNewPass = new JPasswordField();
	//		checkNewPass.setFont(new Font("Tahoma", Font.PLAIN, 20));
	//		contentPane.add(checkNewPass);
			
			JCheckBox chckbxNewCheckBox_1 = new JCheckBox("Show password");
			chckbxNewCheckBox_1.setBackground(new Color(244, 250, 255));
			chckbxNewCheckBox_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
			chckbxNewCheckBox_1.setBounds(516, 312, 179, 23);
			chckbxNewCheckBox_1.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if(chckbxNewCheckBox_1.isSelected()) {
						passwordField.setEchoChar((char)0);
						passwordField_1.setEchoChar((char)0);
						//checkNewPass.setEchoChar((char)0);
					} else {
						passwordField.setEchoChar('*');
						passwordField_1.setEchoChar('*');
						//checkNewPass.setEchoChar('*');
					}
				}
			});
			contentPane.add(chckbxNewCheckBox_1);
			
	
		
			
			
		}
		
		
		
		public boolean checkSignUp() {
			if (textField.getText().isEmpty()) {
				JOptionPane.showMessageDialog(null, "Please enter all text!");
				return false;
			}
			return rootPaneCheckingEnabled;
			
	
		}
		
		public void doSignup() throws Exception {
//			Socket socket = new Socket("localhost", 1234);
//			CheckInfor checkInfor = new CheckInfor(socket, textField.getText(), passwordField.getText(), passwordField_1.getText(), this);
//			checkInfor.start();
//			checkInfor.doSignup();
		}
		
		public String getPass(char[] pass) {
			String p = "";
			for (char x:pass) {
				p+= x;
			}
			return p;
		}
}
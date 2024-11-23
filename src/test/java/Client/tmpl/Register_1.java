package Client.tmpl;

import Client.services.CheckInfor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.net.Socket;
import java.awt.event.ActionEvent;
import javax.swing.Timer;

public class Register_1 extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField tf_email;
	private JTextField tf_otp;
	private JButton btn_otp;
	private JButton btn_checkOTP;
	private Socket socket;
	private JLabel lb_time;
	private int timeRemaining = 60;
	private Timer timer;
	private CheckInfor checkInfor ;
	
	public Register_1(Socket socket) {
		try {
			this.socket = socket;
			this.checkInfor = new CheckInfor(socket, null, null, null, this);
			checkInfor.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Frame_Register_1();
	
	}

	public void Frame_Register_1() {
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
		lbl_title.setBounds(120, 30, 300, 30);
		contentPane.add(lbl_title);

		JLabel lb_email = new JLabel("Email:");
		lb_email.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lb_email.setBounds(50, 120, 100, 25);
		contentPane.add(lb_email);

		tf_email = new JTextField();
		tf_email.setFont(new Font("Tahoma", Font.PLAIN, 16));
		tf_email.setBounds(150, 120, 280, 30);
		contentPane.add(tf_email);

		JLabel lb_otp = new JLabel("Code OTP:");
		lb_otp.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lb_otp.setBounds(50, 170, 100, 25);
		contentPane.add(lb_otp);

		tf_otp = new JTextField();
		tf_otp.setFont(new Font("Tahoma", Font.PLAIN, 16));
		tf_otp.setBounds(150, 170, 280, 30);
		contentPane.add(tf_otp);

		btn_otp = new JButton("Get OTP");
		btn_otp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					doEmail();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		btn_otp.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btn_otp.setBackground(new Color(34, 139, 34));
		btn_otp.setForeground(Color.WHITE);
		btn_otp.setBounds(93, 239, 120, 40);
		contentPane.add(btn_otp);

		btn_checkOTP = new JButton("SEND");
		btn_checkOTP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					doCheckOTP();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		btn_checkOTP.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btn_checkOTP.setBackground(new Color(34, 139, 34));
		btn_checkOTP.setForeground(Color.WHITE);
		btn_checkOTP.setBounds(310, 239, 120, 40);
		contentPane.add(btn_checkOTP);

		JLabel lb_timeOtp = new JLabel("Time Remaining:");
		lb_timeOtp.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lb_timeOtp.setBounds(301, 333, 145, 20);
		contentPane.add(lb_timeOtp);

		lb_time = new JLabel();
		lb_time.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lb_time.setBounds(431, 333, 45, 20);
		contentPane.add(lb_time);
		lb_time.setVisible(false);

		timer = new Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (timeRemaining > 0) {
					timeRemaining--;
					lb_time.setText(timeRemaining + "s");
				} else {
					timer.stop();
				}
			}
		});
	}

	public void doEmail() throws Exception {
		//checkInfor = new CheckInfor(socket, null, null, tf_email.getText(), this);
		checkInfor.doEmail(tf_email.getText());
	//	checkInfor.start();
	}

	public void doCheckOTP() throws Exception {
		String otp = tf_otp.getText().trim();
		checkInfor.doCheckOTP(otp);
	}
	public void startCountdown() {
		timeRemaining = 60;
	    lb_time.setText(timeRemaining + "s");
	    lb_time.setVisible(true); 
	    if (!timer.isRunning()) {
	        timer.start();
	    }
	}
}



//checkInfor = new CheckInfor(socket, null, null, tf_email.getText(), this);
//
//checkInfor.doCheckOTP(otp);
//checkInfor.start();

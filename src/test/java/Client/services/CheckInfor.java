package Client.services;

import Client.tmpl.Login;
import Client.tmpl.Register_1;
import Client.tmpl.Register_2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.StringTokenizer;

import javax.swing.JFrame;
import javax.swing.JOptionPane;



public class CheckInfor extends Thread {
	private 	Socket 				socket;
	private 	DataOutputStream	dos ;
	private 	DataInputStream 	dis;
	
	private 	String 				username;
	private 	String 				password;
	private 	String 				email;
	private 	JFrame 				jFrame;

	static Login log;
	static Register_1 reg1;
	static Register_2 reg2;

	
	
	public CheckInfor(Socket socket, String username, String password, String email , JFrame jFrame) throws Exception {
		this.socket 		= 	socket;
		this.username 		= 	username;
		this.password 		= 	password;
		this.email 			= 	email;
		this.jFrame 		= 	jFrame;		
		this.dos 			= 	new DataOutputStream(socket.getOutputStream());
		this.dis 			= 	new DataInputStream(socket.getInputStream());

	}
	
	private static final String sepa = "<?>";
	public void doSendData(String cmd, String...cont) {
		try {
			synchronized(dos) {
				String msg = cmd;
				for (String s: cont) msg = msg + sepa + s ;
				dos.writeUTF(msg);
			}

		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		try {
			int n = 1;
			while (!socket.isClosed() && n == 1) {
				String 			msg 	= 	dis.readUTF();
				StringTokenizer cont 	= 	new StringTokenizer(msg, sepa);
				String 			cmd 	= 	cont.nextToken();
				
				switch (cmd) {
				case "Call_Login_OK": {
					doLoginOK(cont);
					n = 0;
					break;
				}
				case "Call_Login_KO": {
					doLoginKO (cont);
					break;
				}
				case "Call_Signup_OK": {
					doSignupOK (cont);
					break;
				}
				case "Call_Signup_KO": {
					doSignupKO (cont);
					break;
				}
				
				case "Call_Email_OTP_OK": {
					System.out.println("kkkkk");

					doEmailOK(cont);
					break;
				}
				case "Call_Email_OTP_KO": {
					doEmailKO(cont);
					break;
				}
				
				case "Call_Check_OTP_OK": {
					doCheckOtpOK(cont);
					break;
				}
				case "Call_Check_OTP_KO": {
					doCheckOtpKO(cont);
					break;
				}
				default: {
					break;
				}

				}
				System.out.println("msg: "+msg+", cmd: "+cmd);
			}

		} catch (Exception e) {
			close(socket, dis, dos);
		}
	}
	public void doEmail(String email) throws Exception {
		this.email = email;
		doSendData ("Call_Email_OTP", email);
	}
	
	public void doEmailOK(StringTokenizer cont) throws Exception {
		System.out.println("gui mail thanh cong ");
		((Register_1) jFrame).startCountdown(); 
	}
	
	public void doEmailKO(StringTokenizer cont) throws Exception {
		JOptionPane.showMessageDialog(null, "Email has been registered!");
		
	}
	public void doCheckOTP(String otp) throws Exception {
	    doSendData("Call_Check_OTP", otp);
	}
	public void doCheckOtpOK(StringTokenizer cont) throws Exception {
		reg2 = new Register_2(socket , email);
		reg2.setVisible(true);
		jFrame.setVisible(false);
		System.out.println(email);

	}
	
	public void doCheckOtpKO(StringTokenizer cont) throws Exception {
		JOptionPane.showMessageDialog(null, "OTP code is wrong", "Notification", JOptionPane.WARNING_MESSAGE);
	}
	public void doCheckOtp60(StringTokenizer cont) throws Exception {
		JOptionPane.showMessageDialog(null, "Time out, please get the otp code again!", "Notification", JOptionPane.WARNING_MESSAGE);
	}
	
	public void doLogin() throws Exception {
		doSendData ("Call_Login", username, password);
	}
	
	public void doLoginOK(StringTokenizer cont) throws Exception {
		String uname 	= cont.nextToken();
		Client client = new Client(socket, username);
		client.start();			
		jFrame.setVisible(false);
	}

	
	public void doLoginKO(StringTokenizer cont) throws Exception {
		JOptionPane.showMessageDialog(null, "Impossible login!");
	}
	
	
	public void doSignup(String email , String username , String password , String r_password) throws Exception {
		if(password.equals(r_password)) {
			doSendData ("Call_Signup", email,username, password);
		}else {
			JOptionPane.showMessageDialog(null, "Wrong password!");
		}
	}
	
	public void doSignupOK(StringTokenizer cont) throws Exception {
		log = new Login();
		log.setVisible(true);
		jFrame.setVisible(false);
		
//		doSendData("Call_Sent", "Call_Update_Client_List");
		close(socket, dis, dos);
	}
	
	public void doSignupKO(StringTokenizer cont) throws Exception {
		JOptionPane.showMessageDialog(null, "The account name exists!");
		
		close(socket, dis, dos);
	}
	
	public void close(Socket socket, DataInputStream dataInputStream, DataOutputStream dataOutputStream) {
		try {
			if (dataInputStream != null) {
				dataInputStream.close();
			}

			if (dataOutputStream != null) {
				dataOutputStream.close();
			}

			if (socket != null) {
				socket.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
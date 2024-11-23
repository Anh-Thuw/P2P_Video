package clientserver.services;

import clientserver.db.ConnectDB;
import clientserver.model.Account;
import clientserver.model.Email;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Hashtable;
import java.util.Random;
import java.util.StringTokenizer;



public class ServerCheckInfo extends Thread {

	private 	Socket 						socket;
	private 	Hashtable<String, Account> 	userlist;
	private 	Connection 					connection;
	private 	String 						username;
	private 	String 						password;
	private 	String 						txtemail;
	private 	DataOutputStream 			dos;
	private 	DataInputStream 			dis;
	private 	Email 						email ;
	private 	int 						otp ; 
	private		long 						otpTime;


	public ServerCheckInfo(Socket socket) throws Exception {
		this.socket 	= 	socket;
		this.userlist 	= 	getAll();
		this.dos 		= 	new DataOutputStream(socket.getOutputStream());
		this.dis 		= 	new DataInputStream(socket.getInputStream());

	}

	private static final String sepa = "<?>";
	public void doSendData(String cmd, String... cont) {
		try {
			synchronized (dos) {
				String msg = cmd;
				for (String s : cont)
					msg = msg + sepa + s;
				dos.writeUTF(msg);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			int n = 1;
			while (!socket.isClosed() && n == 1) {
				String 				msg 	= 	dis.readUTF();
				StringTokenizer 	cont 	= 	new StringTokenizer(msg, sepa);
				String 				cmd 	= 	cont.nextToken();
				
				switch (cmd) {
				case "Call_Login":
					doCheckLogin(cont);
					n = 0;
					break;
				case "Call_Signup": {
					doCheckSignup(cont);
					break;
				}
				case "Call_Email_OTP": {
					doCheckEmail(cont);
					break;
				}
				case "Call_Check_OTP": {
					System.out.println("toi roi ");

					doCheckOTP(cont);
					break;
				}
				
				
				case "Call_Sent": {

					String cmd1 = cont.nextToken();
					ServerThreadSent sent = new ServerThreadSent(cmd1, socket, username);
					sent.start();
					
					break ;
	
				}
				
				default: {
					break;
				}

				}

			}

		} catch (Exception e) {
			close();
		}
	}
	public void doCheckEmail(StringTokenizer cont) {//cont_0= unama
		txtemail 		= cont.nextToken();
		otp = 1; 
//		int										min = 100000 ; 
//		int										max = 999999; 
//		Random random = new Random() ; 
//		otp = random.nextInt(max-min)+min ; 
//		try {
//			email.sendEmail(txtemail,otp);
//			otpTime = System.currentTimeMillis();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		doSendData("Call_Email_OTP_OK");
		System.out.println("otp ra ");
	}
	public void doCheckOTP(StringTokenizer cont) {
		 String receivedOtp = cont.nextToken();
//		 long currentTime = System.currentTimeMillis();
//	     long timeElapsed = currentTime - otpTime;
			System.out.println(String.valueOf(otp));
			System.out.println(receivedOtp);

	     if (String.valueOf(otp).equals(receivedOtp) ) {
		        doSendData("Call_Check_OTP_OK");
		 }
//	     else if( timeElapsed > 60000) {
//		        doSendData("Call_Check_OTP_60");
//		 }
//	     else {
//		        doSendData("Call_Check_OTP_KO");
//		 }
	}

	public void doCheckLogin(StringTokenizer cont) {
		try {
			username 		= cont.nextToken();
			password 		= cont.nextToken();
			Account account = userlist.get(username);
			
			if (account==null) {
				doSendData ("Call_Login_KO", username);
				System.out.println("login failed");
				
			} else if (account.getUsename().equals(username) && account.getPassword().equals(password)) {
				doSendData ("Call_Login_OK", username);
				System.out.println("login success");
				
//				ServerThreadSent	sent	= new ServerThreadSent("Call_Sent_Data", socket, username);
//				sent.start();
//				
				ServerThreadReceive receive = new ServerThreadReceive(socket, username);
				receive.start();
				
				
			} else {
				doSendData ("Call_Login_KO", username);
				System.out.println("login failed");
			} 

		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}

	public void doCheckSignup(StringTokenizer cont) {
		try {
			String email 	= cont.nextToken();
			String username = cont.nextToken();
			String password = cont.nextToken();

			Account account = userlist.get(username);
			if (account != null) {
				doSendData("Call_Signup_KO", username);
				System.out.println("Call_Signup Failed");
			} else {
				account 				= new Account(email , username, password);

				String sql = "insert into users (username, email, password) values (?, ?, ?)";
				PreparedStatement 	ps 	= connection.prepareStatement(sql);
				ps.setString(1, account.getUsename())	;
				ps.setString(2, account.getEmail())		;
				ps.setString(3, account.getPassword())	;
				ps.executeUpdate();

				userlist.put(username, account);

				doSendData("Call_Signup_OK", username);
				System.out.println("Call_Signup Success");

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("end signup");
	}

	public Hashtable<String, Account> getAll() {
		Hashtable<String, Account> tab = new Hashtable<String, Account>();

		connection = ConnectDB.ConnectionDB();

		String sql = "select * from users";
		
		try {
			PreparedStatement 	preparedStatement 	= (PreparedStatement) connection.prepareStatement(sql);
			ResultSet 			rs 					= preparedStatement.executeQuery();
			while (rs.next()) {
				Account u = new Account(rs);

				tab.put(u.getUsename(), u);

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tab;
	}

	public void close() {
		try {
			if (socket.equals(null)) {
				socket.close();
			}
			
			if (dos.equals(null)) {
				dos.close();
			}
			
			if (dis.equals(null)) {
				dis.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
	
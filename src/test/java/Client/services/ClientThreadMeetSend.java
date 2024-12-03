package Client.services;

import java.io.DataOutputStream;
import java.net.Socket;
import javax.swing.*;
import java.lang.String;

public class ClientThreadMeetSend extends Thread{

	private 	Socket 				socket;
	private 	DataOutputStream	dos ;
	private 	JFrame 				jFrame;
	
    private		String 				username ;
	private		int 				port ;
	private 	String 				ip ;
	private 	String 				cmd;



	public ClientThreadMeetSend(String cmd,Socket socket, String username , int port , String ip , JFrame jFrame) throws Exception {
		this.cmd 				=	cmd ;
		this.socket				= 	socket ;
		this.username			= 	username ;
		this.port 				= 	port ;
		this.ip 				= 	ip ;
		this.jFrame 			= 	jFrame;
		this.dos  				= 	new DataOutputStream(null);
	}

	private static final String sepa = "<?>";
	public void doSendData(String cmd, String...cont) {
		try {
			if(socket != null){
				dos = new DataOutputStream(socket.getOutputStream());
				if(dos!=null){
					synchronized(dos) {
						String msg = cmd;
						for (String s: cont) msg = msg + sepa + s ;
						dos.writeUTF(msg);
					}
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void run() {
		switch (cmd) {
			case "create":
				doCreate();
				break;

			case "join":
				doJoin();
				break;
			default:
				break;
		}

	}
	public void doCreate(){
		doSendData ("Call_Meet<?>Call_Meet_Create<?>" + port + "<?>"+ip+ "<?>" + username);
	}
	private void doJoin() {
		doSendData ("Call_Meet<?>Call_Meet_Join<?>" + port + "<?>" + username);
	}
}
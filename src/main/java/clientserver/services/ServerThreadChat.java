package clientserver.services;

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class ServerThreadChat extends Thread {

	private 	ArrayList<ServerThreadReceive>	clientList;
	private		DataOutputStream 				dataOutputStream;
	private 	String 							username;
	private		StringTokenizer 				cont;	

	public ServerThreadChat(StringTokenizer cmd, Socket socket, String username) {
		try {		
			this.dataOutputStream 	= 	new DataOutputStream(socket.getOutputStream());
			this.username 			= 	username;
			this.cont				= 	cmd;
			this.clientList			=	ServerThreadReceive.clientList;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void doMassage(String massage, String receiver) {
		try {
			for (ServerThreadReceive client : clientList) { 
				if (client.getUsername().equals(receiver)) {
					client.getDataOutputStream().writeUTF("Call_Chat<?>Call_Sent_To_Receiver<?>" + username + "<?>" + massage);

				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static final String sepa = "<?>";
	
	public void doSendData(String cmd,String... cont) {
		try {
			synchronized (dataOutputStream) {
				String msg = cmd;
				for (String s : cont) msg = msg + sepa + s;
				dataOutputStream.writeUTF(msg);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	@Override
	public void run() {
		
		try {			
			String cmd = cont.nextToken();

			switch (cmd) {
			case "Call_Sent_Msg":
				String 	receiver 		= cont.nextToken();
				String 	massageClient 	= cont.nextToken();
				doMassage(massageClient, receiver);

				break;

			default:
				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
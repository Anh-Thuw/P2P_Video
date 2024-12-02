package Client.services;

import javax.swing.*;
import java.io.DataInputStream;
import java.net.Socket;
import java.util.StringTokenizer;


public class ClientThreadReceive extends Thread{
	private		Socket					socket;
	private		DataInputStream			dataInputStream;
	private		String 					username;
	private 	JFrame 					jFrame;

	
	public ClientThreadReceive(Socket socket, String username , JFrame jFrame) throws Exception {
		this.jFrame 		= 	jFrame;		
		this.socket 			= 	socket;
		this.dataInputStream	=	new DataInputStream(socket.getInputStream());
		this.username			=	username;
	}
	
	@Override
	public void run() {
		while(!socket.isClosed()) {
			try {
				String 				msg 	= 	dataInputStream.readUTF();
				StringTokenizer 	cont 	= 	new StringTokenizer(msg, "<?>");
				String 				cmd 	= 	cont.nextToken();
				
				switch (cmd) {
				case "Call_Chat": {
					ClientThreadChatReceive chatReceive = new ClientThreadChatReceive(cont, socket);
					chatReceive.start();
					break;
				}
				case "Call_Meet": {
					ClientThreadMeetReceive meetReceive = new ClientThreadMeetReceive(cont, socket, username, jFrame);
					meetReceive.start();
					break;
				}
				default: {
					break;
				}
				
				}
			} catch (Exception e) {
				close();
			}
			
		}

	}
	
	public void close() {
		try {
			if (socket.equals(null)) {
				socket.close();
			}
			
			if (dataInputStream.equals(null)) {
				dataInputStream.close();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
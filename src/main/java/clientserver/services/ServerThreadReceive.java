package clientserver.services;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class ServerThreadReceive extends Thread{
	public 		static		ArrayList<ServerThreadReceive> 	clientList			= 	new ArrayList<>();
	
	private					Socket							socket;
	private					String							username;
	private					DataInputStream					dataInputStream;
	private					DataOutputStream				dataOutputStream;
	
	public ServerThreadReceive(Socket socket, String username) throws Exception {
		this.socket 			= 	socket;
		this.username 			= 	username;
		this.dataInputStream 	= 	new DataInputStream(socket.getInputStream());
		this.dataOutputStream	=	new	DataOutputStream(socket.getOutputStream());
		clientList.add(this);
	}
	
	public String getUsername() {
		return username;
	}

	public DataInputStream getDataInputStream() {
		return dataInputStream;
	}
	
	public DataOutputStream getDataOutputStream() {
		return dataOutputStream;
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
					ServerThreadChat serverThreadChat = new ServerThreadChat(cont, socket, username);
					serverThreadChat.start();
					break;
				}
				case "Call_Meet": {
					ServerThreadMeet serverThreadMeet = new ServerThreadMeet(cont, socket );
					serverThreadMeet.start();
					break;
				}
				
				
//				case "Call_File": {
//					ServerThreadFileReceive fileReceive = new ServerThreadFileReceive(cont, socket);
//					fileReceive.start();
//					fileReceive.join();
//					break ;
//	
//				}
				
				
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
			
			if (dataOutputStream.equals(null)) {
				dataOutputStream.close();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
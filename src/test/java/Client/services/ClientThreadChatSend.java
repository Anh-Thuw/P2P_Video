package Client.services;

import java.io.DataOutputStream;
import java.net.Socket;


public class ClientThreadChatSend extends Thread{

    private 	DataOutputStream	dataOutputStream;
    private 	String 			    sender;
    private		String			    receiver;
    private		String 				message;

	public ClientThreadChatSend(Socket socket, String sender, String receiver, String message) throws Exception {		
		this.sender   		  	= 	sender;
		this.receiver		  	= 	receiver;
		this.dataOutputStream  	= 	new DataOutputStream(socket.getOutputStream());
        this.message			= 	message;

	}
	
	@Override
	public void run() {
		try {
			dataOutputStream.writeUTF("Call_Chat<?>Call_Sent_Msg<?>" + receiver + "<?>" + sender + ": " + message);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
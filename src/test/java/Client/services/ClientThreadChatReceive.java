package Client.services;

import Client.model.Receiver;

import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;


public class ClientThreadChatReceive extends Thread {
    private		ArrayList<Receiver> 	receiverList 		= Receiver.list;
    private 	StringTokenizer			cont;	

    public ClientThreadChatReceive(StringTokenizer cmd, Socket socket) {             
            this.cont				= 	cmd;

    }

    @Override
    public void run() {  
        try {      
    		String cmd = cont.nextToken();

    		switch (cmd) {
			case "Call_Sent_To_Receiver":
				String sender	= cont.nextToken();
				String massage 	= cont.nextToken();

				for (Receiver re : receiverList) {
					if (re.getReceiver().equals(sender)) {
						System.out.println(massage);
		                if (massage != null && massage.length() > 0) {              	
		                	re.getChatArea().append("\n" + massage);
		                }
					}
                }
				break;
				

			default:
				break;
			}
        	
                         
        } catch (Exception e) {
        	e.printStackTrace();
        }
        
    }

}
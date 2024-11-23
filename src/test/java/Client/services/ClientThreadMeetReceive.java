package Client.services;

import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.JFrame;

import Client.model.ListClient;
import Client.tmpl.RoomMeet;

public class ClientThreadMeetReceive extends Thread {
    private		ArrayList<ListClient> 	listClients 		= ListClient.list;
    private 	StringTokenizer			cont;	
	private 	RoomMeet 				roomMeet;
	private 	Socket 					socket;
    private		String 					username ;
	private 	JFrame 					jFrame;

    
    public ClientThreadMeetReceive(StringTokenizer cmd, Socket socket , String username , JFrame jFrame) {      
    		this.cont				= 	cmd;
    		this.socket				= 	socket;
    		this.username			= 	username ;
    		this.jFrame 			= 	jFrame;		

    }

	@Override
    public void run() {  
        try {      
    		String cmd = cont.nextToken();

    		switch (cmd) {
			case "Call_Meet_Create_OK":
				doCreateMeetOK();
				break;
			case "Call_Meet_ListClient":
			//	doListClientMeet(cont);
				break;
			default:
				break;
			}
        	
                         
        } catch (Exception e) {
        	e.printStackTrace();
        }
        
    }
	public void doCreateMeetOK() throws Exception {
		String username = cont.nextToken();
		int    port 	= Integer.parseInt(cont.nextToken());
		roomMeet = new RoomMeet(username ,port);
		jFrame.setVisible(false);
		roomMeet.setVisible(true);
	}
	
}
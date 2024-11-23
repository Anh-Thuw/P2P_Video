
package Client.tmpl;
import Client.services.ClientThreadMeetSend;

import javax.swing.*;
import java.awt.*;
import java.net.Socket;
import java.util.Timer;

public class RoomMeet extends JFrame {
    private Socket socket ;
	private String username ;
	private int port;

    private Timer time  ;

  public RoomMeet (String username , int port) {
	try {
	//	this.socket 		= socket ;
		this.username 		= username ;
		this.port 		    = port ;
		Frame_RoomMeet();

	} catch (Exception e) {
		e.printStackTrace();
	}
  }
    public void Frame_RoomMeet() throws Exception {

        setTitle("Room Meeting");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1465, 780);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(Color.WHITE);

        RoomMain mainRoom = new RoomMain( port,  username);
        mainRoom.setBackground(Color.LIGHT_GRAY);
        tabbedPane.addTab("Main Room", mainRoom);

        RoomManager roomManager = new RoomManager();
        roomManager.setBackground(Color.WHITE);
        tabbedPane.addTab("Room Manager", roomManager);

        getContentPane().add(tabbedPane);

        setVisible(true);
    }
}

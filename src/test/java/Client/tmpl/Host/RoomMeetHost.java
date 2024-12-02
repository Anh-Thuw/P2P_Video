
package Client.tmpl.Host;

import javax.swing.*;
import java.awt.*;
import java.net.Socket;
import java.util.Timer;

public class RoomMeetHost extends JFrame {
    private Socket      socket ;
	private String      username ;
	private int         port;

    private Timer       time  ;

  public RoomMeetHost(Socket socket , String username , int port ) {
	try {
		this.socket 		= socket ;
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

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height); // Đặt kích thước JFrame bằng kích thước màn hình

        setExtendedState(JFrame.MAXIMIZED_BOTH); // Thiết lập toàn màn hình
        setUndecorated(true); // Tùy chọn: Loại bỏ thanh tiêu đề (nếu muốn toàn màn hình tuyệt đối)

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(Color.WHITE);

        RoomMainHost mainRoom = new RoomMainHost(port, username);
        mainRoom.setBackground(Color.LIGHT_GRAY);
        tabbedPane.addTab("Main Room", mainRoom);

        RoomManagerHost roomManager = new RoomManagerHost();
        roomManager.setBackground(Color.WHITE);
        tabbedPane.addTab("Room Manager", roomManager);

        getContentPane().add(tabbedPane);

        setVisible(true);
    }

}

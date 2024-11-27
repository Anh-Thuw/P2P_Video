package Client.tmpl;

import Client.services.ClientThreadMeetSend;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
public class Home extends JFrame {

    private static final long serialVersionUID = 1L;
    private Socket socket ; 
	private String username ; 
    private JPanel contentPane;
    private JTextField tf_codemeet;
    private JButton btn_meet;
    private JButton btn_join;
    private JButton btn_logout;

    public Home(Socket socket, String username) {
  		try {
  			this.socket = socket ; 
  			this.username = username ; 
  			Frame_Home();
  	        
  		} catch (Exception e) {
  			e.printStackTrace();
  		}
    }
//    public Home() {
//        Frame_Home();
//    }
    public void Frame_Home() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 773, 438);
        setResizable(false);
        setTitle("Meet Team - Home");

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;
        setLocation(x, y);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setBackground(new Color(245, 245, 245));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitle = new JLabel("Meet Team - Start or Join a Meeting");
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblTitle.setForeground(new Color(70, 130, 180));
        lblTitle.setBounds(200, 50, 400, 30);
        contentPane.add(lblTitle);

        btn_meet = new JButton("Start an Instant Meeting");
        btn_meet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                Create_Host_Video_Room ui1 = new Create_Host_Video_Room(socket , username);
                ui1.setVisible(true);
                dispose();
            }
        });
        btn_meet.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btn_meet.setBackground(new Color(34, 139, 34));
        btn_meet.setForeground(Color.WHITE);
        btn_meet.setBounds(100, 250, 220, 42);
        btn_meet.setIcon(new ImageIcon("start_icon.png")); // add icon if available
        contentPane.add(btn_meet);


        tf_codemeet = new JTextField();
    //    tf_codemeet.setText("Enter Port:");
        tf_codemeet.setFont(new Font("Tahoma", Font.PLAIN, 14));
        tf_codemeet.setForeground(new Color(169, 169, 169));
        tf_codemeet.setBounds(350, 260, 250, 30);
        contentPane.add(tf_codemeet);
        tf_codemeet.setColumns(10);

        btn_join = new JButton("Join");
        btn_join.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btn_join.setBackground(new Color(70, 130, 180));
        btn_join.setForeground(Color.WHITE);
        btn_join.setBounds(620, 260, 80, 30);
        btn_join.setIcon(new ImageIcon("join_icon.png")); // add icon if available
        contentPane.add(btn_join);
        btn_join.addActionListener(e -> {
            String portText = tf_codemeet.getText();
            if (portText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a port number.", "Input Error", JOptionPane.WARNING_MESSAGE);
            } else {
                try {
                    int port = Integer.parseInt(portText);
                    String ip = socket.getInetAddress().getHostAddress();

                    ClientThreadMeetSend clientThreadMeetSend = new ClientThreadMeetSend("join",socket , username , port , ip, this );
                    clientThreadMeetSend.start();

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid port number. Please enter a valid integer.", "Input Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        // Adding the Logout button
        btn_logout = new JButton("Logout");
        btn_logout.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btn_logout.setBackground(new Color(255, 69, 0)); // Red color for logout
        btn_logout.setForeground(Color.WHITE);
        btn_logout.setBounds(644, 358, 105, 42); // Positioning below the start meeting button
        btn_logout.setIcon(new ImageIcon("logout_icon.png")); // Add icon if available
        contentPane.add(btn_logout);

        JLabel lblFooter = new JLabel("Powered by Meet Team");
        lblFooter.setFont(new Font("Tahoma", Font.ITALIC, 12));
        lblFooter.setForeground(new Color(105, 105, 105));
        lblFooter.setBounds(320, 380, 200, 20);
        contentPane.add(lblFooter);
    }
}

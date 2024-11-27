package Client.tmpl;

import Client.services.ClientThreadMeetSend;
import javax.swing.*;
import java.awt.*;
import java.net.Socket;
import java.net.InetAddress;

public class Create_Host_Video_Room extends JFrame {

    private Socket          socket;
    private String          username;
    private JPanel          contentPane;
    private JLabel          lblTitle, labelEnterPort, lblFooter;
    private JTextField      textFieldEnterPort;
    private JButton         buttonJoin, buttonBack;

    public Create_Host_Video_Room(Socket socket, String username) {
        try {
            this.socket = socket;
            this.username = username;
            Frame_Create_Host_Video_Room();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Frame_Create_Host_Video_Room() {
        EventQueue.invokeLater(() -> {
            setTitle("Meet Team - Create Room");
            setSize(773, 438);
            setResizable(false);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int x = (screenSize.width - getWidth()) / 2;
            int y = (screenSize.height - getHeight()) / 2;
            setLocation(x, y);

            contentPane = new JPanel();
            contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            contentPane.setBackground(new Color(245, 245, 245));
            setContentPane(contentPane);
            contentPane.setLayout(null);

            lblTitle = new JLabel("Create a Meeting Room");
            lblTitle.setFont(new Font("Tahoma", Font.BOLD, 20));
            lblTitle.setForeground(new Color(70, 130, 180));
            lblTitle.setBounds(250, 50, 300, 30);
            contentPane.add(lblTitle);

            labelEnterPort = new JLabel("Enter Port:");
            labelEnterPort.setFont(new Font("Tahoma", Font.PLAIN, 16));
            labelEnterPort.setBounds(200, 150, 100, 30);
            contentPane.add(labelEnterPort);

            textFieldEnterPort = new JTextField();
            textFieldEnterPort.setFont(new Font("Tahoma", Font.PLAIN, 14));
            textFieldEnterPort.setBounds(300, 150, 250, 30);
            contentPane.add(textFieldEnterPort);

            buttonJoin = new JButton("Join");
            buttonJoin.setFont(new Font("Tahoma", Font.PLAIN, 16));
            buttonJoin.setBackground(new Color(70, 130, 180));
            buttonJoin.setForeground(Color.WHITE);
            buttonJoin.setBounds(450, 250, 150, 40);
            contentPane.add(buttonJoin);

            buttonBack = new JButton("Back");
            buttonBack.setFont(new Font("Tahoma", Font.PLAIN, 16));
            buttonBack.setBackground(new Color(255, 69, 0)); // Red for the "Back" button
            buttonBack.setForeground(Color.WHITE);
            buttonBack.setBounds(173, 250, 150, 40);
            contentPane.add(buttonBack);

            lblFooter = new JLabel("Powered by Meet Team");
            lblFooter.setFont(new Font("Tahoma", Font.ITALIC, 12));
            lblFooter.setForeground(new Color(105, 105, 105));
            lblFooter.setBounds(320, 380, 200, 20);
            contentPane.add(lblFooter);

            buttonJoin.addActionListener(e -> {
                String portText = textFieldEnterPort.getText();
                if (portText.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter a port number.", "Input Error", JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        int port = Integer.parseInt(portText);
                        String ip = InetAddress.getLocalHost().getHostAddress();

                        ClientThreadMeetSend clientThreadMeetSend = new ClientThreadMeetSend("create", socket, username, port, ip, this);
                        clientThreadMeetSend.start();

                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Invalid port number. Please enter a valid integer.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });

            buttonBack.addActionListener(e -> {
                setVisible(false);
                dispose();
                // Optional: Return to the previous screen or perform another action
                JOptionPane.showMessageDialog(this, "Going back to the previous screen.", "Back", JOptionPane.INFORMATION_MESSAGE);
            });
        });
    }
}

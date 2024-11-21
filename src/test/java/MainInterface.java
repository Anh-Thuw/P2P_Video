
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainInterface extends JFrame {

    public MainInterface() {

        setTitle("Simple Swing App");
        setSize(700, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton buttonCreateVideoRoom = new JButton("Create room video");
        JButton buttonJoin = new JButton("Join");

        JLabel labelEnterIP = new JLabel("Enter IP");
        JTextField textFieldEnterIP = new JTextField(10);

        JLabel labelEnterPort = new JLabel("Enter Port");
        JTextField textFieldEnterPort = new JTextField(10);

        JLabel labelEnterName = new JLabel("Enter Name");
        JTextField textFieldEnterName = new JTextField(10);

        JPanel createRoomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        createRoomPanel.add(buttonCreateVideoRoom);

        JPanel joinPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 14));
        joinPanel.add(labelEnterIP);
        joinPanel.add(textFieldEnterIP);
        joinPanel.add(labelEnterPort);
        joinPanel.add(textFieldEnterPort);
        joinPanel.add(labelEnterName);
        joinPanel.add(textFieldEnterName);
        joinPanel.add(buttonJoin);

        // Create the main panel with a GridLayout for organizing the subpanels
        JPanel mainPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        mainPanel.add(createRoomPanel);
        mainPanel.add(joinPanel);

        add(mainPanel, BorderLayout.CENTER);
        
        setLocationRelativeTo(null);
        try {
          //  Socket sk = new Socket("localhost", 1106);

            buttonCreateVideoRoom.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    Create_Host_Video_Room ui1 = new Create_Host_Video_Room();
                    ui1.setVisible(true);
                    dispose();
                }
            });

            buttonJoin.addActionListener(e -> {
                try {

                    String IP = String.format(textFieldEnterIP.getText());
                    int port = Integer.parseInt(textFieldEnterPort.getText());
                    String enterName = String.format(textFieldEnterName.getText());

                    System.out.println("Main interface  89: " + IP + " " + port + " " + enterName);

                    ClientInterface ri = new ClientInterface(IP, port, enterName);

                    setVisible(false);
                    dispose();

                } catch (Exception ex) {
                    Logger.getLogger(MainInterface.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(this, "Không thể kết nối đến máy chủ.", "Lỗi Kết Nối", JOptionPane.ERROR_MESSAGE);
                }

            });

        } catch (Exception e) {
            Logger.getLogger(MainInterface.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(this, "Không thể kết nối đến máy chủ.", "Lỗi Kết Nối", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        MainInterface main = new MainInterface();
        main.setVisible(true);
    }
}

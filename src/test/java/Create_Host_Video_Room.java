import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Create_Host_Video_Room extends JFrame {

    public Create_Host_Video_Room() {
        java.awt.EventQueue.invokeLater(() -> {
            setTitle("Create Room");
            setSize(300, 200);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(5, 1, 10, 10));

            JButton buttonJoin = new JButton("Join");
            buttonJoin.setPreferredSize(new Dimension(80, 30));

            JLabel labelEnterName = new JLabel("Enter Name:");
            JTextField textFieldEnterName = new JTextField();
            textFieldEnterName.setPreferredSize(new Dimension(180, 30));

            JLabel labelEnterPort = new JLabel("Enter Port:");
            JTextField textFieldEnterPort = new JTextField();
            textFieldEnterPort.setPreferredSize(new Dimension(180, 30));

            panel.add(labelEnterPort);
            panel.add(textFieldEnterPort);
            panel.add(labelEnterName);
            panel.add(textFieldEnterName);
            panel.add(buttonJoin);

            add(panel);
            setLocationRelativeTo(null);

            buttonJoin.addActionListener(e -> {
                String name = textFieldEnterName.getText();
                int port = Integer.parseInt(textFieldEnterPort.getText());

                SwingUtilities.invokeLater(() -> {
                    try {
                        HostInterface ri = new HostInterface(port, name);
                        setVisible(false);
                        dispose();
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(Create_Host_Video_Room.class.getName()).log(Level.SEVERE, null, ex);
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(panel, "Không thể kết nối đến máy chủ.", "Lỗi Kết Nối", JOptionPane.ERROR_MESSAGE);
                    }
                });
            });
        });
    }
}

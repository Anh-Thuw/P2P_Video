package Client.tmpl;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class test extends JFrame {
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    test frame = new test();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public test() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 450);
        setResizable(false);
        setTitle("Meet Team - Login");

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;
        setLocation(x, y);

        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JButton btnEndCall = new JButton("dhaffa");
        btnEndCall.setIcon(new ImageIcon("src/main/java/com/mycompany/baitaplonmonhoc/image/endphone.png"));
        btnEndCall.setBounds(420, 661, 70, 50);
        btnEndCall.setBackground(new Color(255, 102, 102));
        btnEndCall.setFocusPainted(false);
        add(btnEndCall);
      // \src\test\java\Client\image ImageIcon iconOn = new ImageIcon("src/main/java/com/mycompany/baitaplonmonhoc/img/" + iconOnPath);

    }
}

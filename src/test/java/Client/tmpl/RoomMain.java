package Client.tmpl;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;


public class RoomMain extends JPanel {
    private JButton              btnEndCall, btnMute, btnToggleVideo , btnChatToggle;
    private JLabel               lblTime, lblRoomCode;
    private JPanel               chatPanel;
    private JTextArea            chatArea;
    private JTextField           chatInput;
    private Timer                timer;
    private Webcam               webcam;
    private boolean              isCameraOn = true;
    private boolean              isMicOn = true;
    private static   JLabel      video = new JLabel();
    //    private static JLabel videoOut = new JLabel();
    private ImageIcon            icOut;
    private BufferedImage        br;
    private ObjectInputStream    in;
    private ObjectOutputStream   out;
    private Socket               clientSocket;
    private ServerSocket         serverSocket;
    private WebcamPanel          camPanel;
    private final List<ObjectOutputStream> clients = new ArrayList<>();
    private String               username ;
    private int port ;
    public RoomMain (int port , String username) {
        try {
            this.username 		= username ;
            this.port 		    = port ;
            Frame_RoomMain();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void Frame_RoomMain() throws UnknownHostException {

        setBackground(new Color(204, 255, 255));
        setLayout(null);
        setPreferredSize(new Dimension(1465, 780));

//        JPanel containerPanelLeftAndRight = new JPanel(new GridLayout(1, 2));
//        JPanel panelCenter = new JPanel(new BorderLayout());

        // Video panel placeholder
        JPanel webcamPanel = new JPanel(new BorderLayout());
        webcamPanel.setBounds(20, 20, 1421, 631);
        webcamPanel.setBackground(new Color(229, 255, 255));

        camPanel = webcamPanel();
        webcamPanel.add(camPanel, BorderLayout.CENTER);
        add(webcamPanel);


        // Button to toggle chat panel visibility
        btnChatToggle = new JButton("Chat");
        btnChatToggle = createButton("chat.png", null);
        btnChatToggle.setBackground(Color.PINK);
        btnChatToggle.setBounds(1356, 661, 70, 50);
        btnChatToggle.addActionListener(e -> toggleChatPanel());
        add(btnChatToggle);

        // Bottom buttons with icons
        btnEndCall = new JButton();
        btnEndCall = createButton("IconExit.png", null);
        btnEndCall.setBounds(420, 661, 70, 50);
        btnEndCall.setBackground(new Color(255, 102, 102));
        btnEndCall.setFocusPainted(false);
        add(btnEndCall);
        btnEndCall.addActionListener(e -> exitVideoRoom());

        btnMute = new JButton();
        btnMute = createButton("IconOnMic.png", "IconOffMic.png");
        btnMute.setBounds(567, 661, 70, 50);
        btnMute.setBackground(new Color(102, 204, 255));
        btnMute.setFocusPainted(false);
        btnMute.addActionListener(e -> toggleMute(btnMute));
        add(btnMute);

        btnToggleVideo = new JButton();
        btnToggleVideo = createButton("IconOnVideo.png", "IconOffVideo.png");
        btnToggleVideo.setBounds(711, 661, 70, 50);
        btnToggleVideo.setBackground(new Color(153, 255, 204));
        btnToggleVideo.setFocusPainted(false);
        btnToggleVideo.addActionListener(e -> toggleVideo(btnToggleVideo));
        add(btnToggleVideo);

        lblTime = new JLabel();
        lblTime.setBounds(20, 651, 150, 30);
        lblTime.setFont(new Font("Tahoma", Font.BOLD, 14));
        add(lblTime);

        InetAddress ip = InetAddress.getLocalHost();
        String portString = String.valueOf(port);

        lblRoomCode = new JLabel("IP: " + ip.getHostAddress() + "- Port: " + portString);
        lblRoomCode.setBounds(20, 681, 150, 30);
        lblRoomCode.setFont(new Font("Tahoma", Font.BOLD, 14));
        add(lblRoomCode);

        JScrollPane chatScrollPane = new JScrollPane();
        chatScrollPane.setBounds(1128, 20, 313, 631);
        add(chatScrollPane);

        chatPanel = new JPanel();
        chatScrollPane.setViewportView(chatPanel);
        chatPanel.setBackground(new Color(240, 255, 255));
        chatPanel.setLayout(null);

        chatArea = new JTextArea();
        chatArea.setBounds(0, 0, 293, 605);
        chatArea.setEditable(false);
        chatArea.setFont(new Font("Tahoma", Font.PLAIN, 16));
        chatArea.setBackground(new Color(240, 255, 255));
        chatPanel.add(chatArea);

        chatInput = new JTextField();
        chatInput.setBounds(0, 605, 233, 26);
        chatInput.setFont(new Font("Tahoma", Font.PLAIN, 16));
        chatPanel.add(chatInput);

        JButton btnSend = new JButton("Send");
        btnSend.setBounds(240, 605, 71, 26);
        btnSend.setBackground(new Color(153, 204, 255));
        btnSend.setForeground(Color.WHITE);
        btnSend.setFocusPainted(false);
        btnSend.addActionListener(e -> sendMessageToAll());
        chatPanel.add(btnSend);

        chatPanel.setVisible(false);

        timer = new Timer(1000, e -> updateTime());
        timer.start();

        new Thread(() -> {
            try {
                System.out.println(port);
                serverSocket = new ServerSocket(port);
                System.out.println("Server socket initialized");
                clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket);

                // Thread for receiving data
                new Thread(() -> {
                    try {
                        in = new ObjectInputStream(clientSocket.getInputStream());

                        while (true) {
                            ImageIcon ic = (ImageIcon) in.readObject();
                            video.setIcon(ic);
                            System.out.println("inFromClient");
                        }
                    } catch (IOException | ClassNotFoundException ex) {
                        Logger.getLogger(RoomMain.class.getName()).log(Level.SEVERE, null, ex);
                        video.setIcon(null);
                    }
                }).start();

                new Thread(() -> {
                    try {
                        out = new ObjectOutputStream(clientSocket.getOutputStream());

                        new Thread(() -> {
                            webcam.open();
                            isCameraOn = true;
                            isMicOn = true;
                        }).start();

                        while (true) {

                            br = webcam.getImage();
                            icOut = new ImageIcon(br);
                            out.writeObject(icOut);
                            out.flush();
                            System.out.println("outToClient");

                            new Thread(() -> handleClient(clientSocket, out)).start();

                        }
                    } catch (IOException ex) {
                        Logger.getLogger(RoomMain.class.getName()).log(Level.SEVERE, null, ex);
                        video.setIcon(null);
                    }
                }).start();


            } catch (IOException ex) {
                Logger.getLogger(RoomMain.class.getName()).log(Level.SEVERE, null, ex);
                video.setIcon(null);
            }
        }).start();
    }
    private void sendMessageToAll() {
        String message = chatInput.getText();
        if (!message.trim().isEmpty()) {
            chatArea.append( username+ ":"+ message + "\n");
            broadcastMessage("You: " + message);
            chatInput.setText("");
        }
    }

    private void handleClient(Socket socketClient, ObjectOutputStream out) {
        try (ObjectInputStream clientIn = new ObjectInputStream(socketClient.getInputStream())) {
            while (true) {
                String message = (String) clientIn.readObject();
                broadcastMessage("Client: " + message);
            }
        } catch (IOException | ClassNotFoundException e) {
            Logger.getLogger(RoomMain.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            clients.remove(out);
        }
    }
    private void broadcastMessage(String message) {
        for (ObjectOutputStream client : clients) {
            try {
                client.writeObject(message);
                client.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void toggleChatPanel() {
        if (chatPanel.isVisible()) {
            // Ẩn chat panel và trả lại kích thước video panel
            chatPanel.setVisible(false);
            // Đẩy video panel về lại vị trí ban đầu
            JPanel webcamPanel = (JPanel) getComponent(0);  // Lấy video panel đầu tiên
            webcamPanel.setBounds(20, 20, 1421, 631);
            revalidate();
            repaint();
        } else {
            // Hiển thị chat panel và di chuyển video panel sang trái
            chatPanel.setVisible(true);
            JPanel webcamPanel = (JPanel) getComponent(0);  // Lấy video panel đầu tiên
            webcamPanel.setBounds(20, 20, 1050, 631); // Điều chỉnh kích thước video panel
            revalidate();
            repaint();
        }
    }
    private void toggleVideo(JButton buttonOnOffVideo){
        if (isCameraOn) {
            updateButtonIcon(buttonOnOffVideo, "IconOffVideo.png");
            stopWebcam();
            isCameraOn = !isCameraOn;
        } else {
            updateButtonIcon(buttonOnOffVideo, "IconOnVideo.png");
            startWebcam();
            isCameraOn = !isCameraOn;
        }
    }
    private void startWebcam() {
        if (webcam != null && !webcam.isOpen()) {
            webcam.open();
            camPanel.start();
//            sendData = true;
        }
    }

    private void stopWebcam(){
        if (webcam != null && webcam.isOpen()) {
            webcam.close();
            camPanel.stop();
//            sendData = false;
        }
    }

    private void updateButtonIcon(JButton button, String iconOnPath) {
        ImageIcon iconOn = new ImageIcon("src/main/java/com/mycompany/baitaplonmonhoc/img/" + iconOnPath);
        Image scaledImage = iconOn.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        button.setIcon(new ImageIcon(scaledImage));
    }
    private void toggleMute(JButton buttonOnOffMic) {

        if (isMicOn) {
            isMicOn = !isMicOn;
            updateButtonIcon(buttonOnOffMic, "IconOffMic.png");
        } else {
            isMicOn = !isMicOn;
            updateButtonIcon(buttonOnOffMic, "IconOnMic.png");
        }

    }

    private void exitVideoRoom() {
        try {
//        sendData = false; // Stop receiving data

            if (clientSocket != null) {
                clientSocket.close();
                clientSocket = null;
            }

            if (serverSocket != null) {
                serverSocket.close();
                serverSocket = null;
            }

            if (in != null) {
                in.close();
                in = null;
            }

            if (out != null) {
                out.close();
                out.flush();
                out = null;
            }

            if (webcam != null) {
                webcam.close();
                webcam = null;
            }

            video.setIcon(null);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Home main = new Home(null , username);
            main.setVisible(true);
            setVisible(false);
        }
    }

    private WebcamPanel webcamPanel() {

        webcam = Webcam.getDefault();
        webcam.setViewSize(new Dimension(640, 480));

        camPanel = new WebcamPanel(webcam);

        return camPanel;
    }


    private void updateTime() {
        lblTime.setText(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
    }
    private JButton createButton(String iconOnPath, String iconOffPath) {
        ImageIcon iconOn = new ImageIcon("src/main/java/com/mycompany/baitaplonmonhoc/img/" + iconOnPath);
        Image scaledImage = iconOn.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        return new JButton(new ImageIcon(scaledImage));
    }
}

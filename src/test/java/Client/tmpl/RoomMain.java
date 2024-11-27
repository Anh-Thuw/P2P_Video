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
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;


public class RoomMain extends JPanel {
    private static final int WEBCAM_WIDTH = 640, WEBCAM_HEIGHT = 480;

    // UI Components
    private JButton              btnSend , btnEndCall, btnMute, btnToggleVideo , btnChatToggle;
    private JLabel               lblTime, lblRoomCode;
    private JPanel               chatPanel , webcamPanel ;
    private JTextArea            chatArea;
    private JScrollPane          chatScrollPane;

    private JTextField           chatInput;
    private Timer                timer;
    private Webcam               webcam;
    // Networking
    private ObjectInputStream    in;
    private ObjectOutputStream   out;
    private ServerSocket         serverSocket;
    private Socket               clientSocket;
    private List<Socket>         connectedClients = new ArrayList<>();
    private WebcamPanel          camPanel;
    private final List<ObjectOutputStream> clients = new CopyOnWriteArrayList<>();

    // State Variables
    private String               username ;
    private int                  port ;
    private boolean              isCameraOn = true;
    private boolean              isMicOn = true;


    public RoomMain (int port , String username) {
        try {
            this.username 		= username ;
            this.port 		    = port ;
            Frame_RoomMain();
            setupNetworking();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void Frame_RoomMain() throws UnknownHostException {

        // Lấy kích thước màn hình
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        setBackground(new Color(204, 255, 255));
        setLayout(null);
        setPreferredSize(screenSize);

        // Webcam Panel
        webcamPanel = new JPanel(new BorderLayout());
        webcamPanel.setBounds(20, 20, screenWidth - 340, (int) (screenHeight * 0.8));
        webcamPanel.setBackground(new Color(229, 255, 255));
        camPanel = initWebcamPanel();
        webcamPanel.add(camPanel, BorderLayout.CENTER);
        add(webcamPanel);

        // Nút bật/tắt chat
        btnChatToggle = createButton("chat.png", null);
        btnChatToggle.setBackground(Color.PINK);
        btnChatToggle.setBounds(screenWidth - 100, (int) (screenHeight * 0.85), 70, 50);
        btnChatToggle.addActionListener(e -> toggleChatPanel());
        add(btnChatToggle);

        // Nút kết thúc cuộc gọi
        btnEndCall = createButton("IconExit.png", null);
        btnEndCall.setBounds((int) (screenWidth * 0.3), (int) (screenHeight * 0.85), 70, 50);
        btnEndCall.setBackground(new Color(255, 102, 102));
        btnEndCall.addActionListener(e -> exitVideoRoom());
        add(btnEndCall);

        // Nút tắt tiếng
        btnMute = createButton("IconOnMic.png", "IconOffMic.png");
        btnMute.setBounds((int) (screenWidth * 0.4), (int) (screenHeight * 0.85), 70, 50);
        btnMute.setBackground(new Color(102, 204, 255));
        btnMute.addActionListener(e -> toggleMute(btnMute));
        add(btnMute);

        // Nút bật/tắt video
        btnToggleVideo = createButton("IconOnVideo.png", "IconOffVideo.png");
        btnToggleVideo.setBounds((int) (screenWidth * 0.5), (int) (screenHeight * 0.85), 70, 50);
        btnToggleVideo.setBackground(new Color(153, 255, 204));
        btnToggleVideo.addActionListener(e -> toggleVideo(btnToggleVideo));
        add(btnToggleVideo);

        // Thời gian hiển thị
        lblTime = new JLabel();
        lblTime.setBounds(20, (int) (screenHeight * 0.85), 150, 30);
        lblTime.setFont(new Font("Tahoma", Font.BOLD, 14));
        add(lblTime);

        // Mã phòng
        lblRoomCode = new JLabel("IP: " + InetAddress.getLocalHost().getHostAddress() + " - Port: " + port);
        lblRoomCode.setBounds(20, (int) (screenHeight * 0.88), 300, 30);
        lblRoomCode.setFont(new Font("Tahoma", Font.BOLD, 14));
        add(lblRoomCode);

        // Chat panel setup
        chatPanel = new JPanel(null);
        chatScrollPane = new JScrollPane(chatPanel);
        chatScrollPane.setBounds(screenWidth - 320, 20, 300, (int) (screenHeight * 0.8));
        add(chatScrollPane);

        chatArea = new JTextArea();
        chatArea.setBounds(0, 0, 280, (int) (screenHeight * 0.75));
        chatArea.setEditable(false);
        chatPanel.add(chatArea);

        chatInput = new JTextField();
        chatInput.setBounds(0, (int) (screenHeight * 0.75), 230, 26);
        chatPanel.add(chatInput);

        btnSend = new JButton("Send");
        btnSend.setBounds(240, (int) (screenHeight * 0.75), 60, 26);
        btnSend.setBackground(new Color(153, 204, 255));
        btnSend.setForeground(Color.WHITE);
        btnSend.addActionListener(e -> sendMessageToAll());
        chatPanel.add(btnSend);

        chatPanel.setVisible(false);

        timer = new Timer(1000, e -> updateTime());
        timer.start();
    }

    private void setupNetworking() {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(port);
                while (true) {
                    Socket newClientSocket = serverSocket.accept();
                    connectedClients.add(newClientSocket);
                    out = new ObjectOutputStream(newClientSocket.getOutputStream());
                    clients.add(out);
                    //video
                    new Thread(() -> receiveVideo(newClientSocket)).start();
                    new Thread(() -> sendVideoToClient(newClientSocket)).start();
                    // chat
                    new Thread(() -> handleClientMessages(newClientSocket)).start();


                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void handleClientMessages(Socket clientSocket) {
        try {
            in = new ObjectInputStream(clientSocket.getInputStream());
            while (true) {
                String message = (String) in.readObject();
                broadcastMessage(message);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void sendMessageToAll() {
        String message = chatInput.getText().trim();
        if (!message.isEmpty()) {
            try {
                String formattedMessage = username + ": " + message;

                // Send to server
                out.writeObject(formattedMessage);
                out.flush();

                // Display in local chat area
                chatArea.append("Me: " + message + "\n");
                chatInput.setText("");

                // Broadcast to other clients
                broadcastMessage(formattedMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void broadcastMessage(String message) {
        for (ObjectOutputStream clientOut : clients) {
            try {
                clientOut.writeObject(message);
                clientOut.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Append message locally
        chatArea.append(message + "\n");
    }
    // Nhận video từ các client khác
    private void receiveVideo(Socket clientSocket) {
        try {
            in = new ObjectInputStream(clientSocket.getInputStream());
            while (true) {
                ImageIcon receivedImage = (ImageIcon) in.readObject();
                SwingUtilities.invokeLater(() -> {
                    JLabel videoLabel = new JLabel(receivedImage);
                    updateVideoDisplay(videoLabel);
                });
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void sendVideoToClient(Socket clientSocket) {
        try (ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())) {
            if (!webcam.isOpen()) webcam.open();
            while (isCameraOn) {
                BufferedImage frame = webcam.getImage();
                ImageIcon imageIcon = new ImageIcon(frame);
                out.writeObject(imageIcon);
                out.flush();
                Thread.sleep(50); // Reduce frequency to ~20 FPS
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    // Cập nhật hiển thị video nhận được
    private void updateVideoDisplay(JLabel videoLabel) {
        webcamPanel.removeAll();
        webcamPanel.add(videoLabel);
        webcamPanel.revalidate();
        webcamPanel.repaint();
    }
    private WebcamPanel initWebcamPanel() {
        webcam = Webcam.getDefault();
        webcam.setViewSize(new Dimension(WEBCAM_WIDTH, WEBCAM_HEIGHT));
        return new WebcamPanel(webcam);
    }

    private void toggleChatPanel() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        if (chatPanel.isVisible()) {
            chatPanel.setVisible(false);
            webcamPanel.setBounds(20, 20, screenWidth - 40, (int) (screenHeight * 0.8));
        } else {
            chatPanel.setVisible(true);
            webcamPanel.setBounds(20, 20, screenWidth - 360, (int) (screenHeight * 0.8));
        }
        revalidate();
        repaint();
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
            for (ObjectOutputStream client : clients) {
                client.close();
            }
            clients.clear();
            connectedClients.clear();

         //   video.setIcon(null);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Home main = new Home(null , username);
            main.setVisible(true);
            setVisible(false);
        }
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

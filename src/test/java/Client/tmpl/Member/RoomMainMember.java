package Client.tmpl.Member;

import Client.tmpl.Home;
import Client.tmpl.Host.RoomMainHost;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


public class RoomMainMember extends JPanel {
    private static final int WEBCAM_WIDTH = 640, WEBCAM_HEIGHT = 480;

    // UI Components
    private JButton              btnSend , btnEndCall, btnMute, btnToggleVideo , btnChatToggle;
    private JLabel               lblTime, lblRoomCode , videoLabel;
    private JPanel               chatPanel , webcamPanel ;
    private JTextArea            chatArea;
    private JScrollPane          chatScrollPane;

    private JTextField           chatInput;
    private Timer                timer;
    private Webcam               webcam;
    // Networking
    private DatagramSocket       socket;
    static  MulticastSocket      multicastSocket = null;


    private WebcamPanel          camPanel;
    private String               username , ipHost;
    private int                  port ;
    private boolean              isCameraOn = true;
    private boolean              isMicOn = true;
    private  BufferedImage       frame ;
    // Networking

  //  private InetAddress ipHost;
    public RoomMainMember(int port , String username , String ipHost) {
        try {
            this.username 		= username ;
            this.port 		    = port ;
            this.ipHost         = ipHost ;
            setupClient();
            Frame_RoomMain();
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
//        webcamPanel = new JPanel(new BorderLayout());
//        webcamPanel.setBounds(20, 20, screenWidth - 340, (int) (screenHeight * 0.8));
//        webcamPanel.setBackground(new Color(229, 255, 255));
//        camPanel = initWebcamPanel();
//        webcamPanel.add(camPanel, BorderLayout.CENTER);
//        add(webcamPanel);

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
        lblRoomCode = new JLabel("IP: " + ipHost + " - Port: " + port);
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
        btnSend.addActionListener(e -> sendChat());

        chatPanel.add(btnSend);

        chatPanel.setVisible(false);

        timer = new Timer(1000, e -> updateTime());
        timer.start();
    }
    private void setupClient() {
        try {
            socket = new DatagramSocket(port);

            multicastSocket = RoomMainHost.multicastSocket;
            multicastSocket.joinGroup(InetAddress.getByName(ipHost));;

//            new Thread(() -> sendVideo()).start();
//            new Thread(() -> receiveVideo()).start();
            new Thread(() -> receiveChat()).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void receiveVideo() {
        try {
            while (true) {
                byte[] buffer = new byte[65535];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                // Tái tạo hình ảnh từ gói tin
                ByteArrayInputStream bais = new ByteArrayInputStream(packet.getData(), 0, packet.getLength());
                BufferedImage receivedImage = ImageIO.read(bais);

                SwingUtilities.invokeLater(() -> updateVideoDisplay(receivedImage));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void sendVideo() {
        try {
            if (webcam == null) {
                JOptionPane.showMessageDialog(this, "Webcam chưa được khởi tạo");
                return;
            }
            webcam.open();
            isCameraOn = true;
            while (isCameraOn) {
                frame = webcam.getImage();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(frame, "jpg", baos);
                byte[] data = baos.toByteArray();

                // Chia nhỏ gói tin
                int packetSize = 64000;
                for (int i = 0; i < data.length; i += packetSize) {
                    int length = Math.min(packetSize, data.length - i);
                    DatagramPacket packet = new DatagramPacket(data, i, length, InetAddress.getByName(ipHost), port);
                    socket.send(packet);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Cập nhật hiển thị video nhận được
    private void updateVideoDisplay(BufferedImage image) {
        videoLabel = new JLabel(new ImageIcon(image));
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
    private void receiveChat() {
        try {
            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String message = new String(packet.getData(), 0, packet.getLength());
                chatArea.append(message + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendChat() {
        try {
            String message = username + ": " + chatInput.getText().trim();
            if (!message.isEmpty()) {
                byte[] buffer = message.getBytes();
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(ipHost), port);
                socket.send(packet);

                chatArea.append(message + "\n");
                chatInput.setText("");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            String exitMessage = username + " đã rời phòng.";
            byte[] buffer = exitMessage.getBytes();
            DatagramPacket exitPacket = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(ipHost), port);
            socket.send(exitPacket);

            if (multicastSocket != null) {
                multicastSocket.leaveGroup(InetAddress.getByName(ipHost));
                multicastSocket.close();
            }
            if (socket != null) {
                socket.close();
            }
            if (webcam != null && webcam.isOpen()) {
                webcam.close();
            }
            SwingUtilities.getWindowAncestor(this).dispose();
        } catch (IOException e) {
            e.printStackTrace();
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

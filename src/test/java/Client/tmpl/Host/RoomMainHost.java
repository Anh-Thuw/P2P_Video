package Client.tmpl.Host;

import Client.tmpl.Home;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class RoomMainHost extends JPanel {
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
    private WebcamPanel          camPanel;
    private String               username;
    private int                  port ;
    private boolean              isCameraOn = false;
    private boolean              isMicOn = true;
    private  BufferedImage       frame ;
    public static final byte[]   BUFFER = new byte[4096];
    private DatagramPacket       sendPacket, receivePacket;
    static  MulticastSocket     multicastSocket = null;
    private InetAddress         groupAddress ;
    public RoomMainHost(int port , String username) {
        try {
            this.username 		= username ;
            this.port 		    = port ;
            setupHost();
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
        btnSend.addActionListener(e -> sendChat());
        chatPanel.add(btnSend);

        chatPanel.setVisible(false);

        timer = new Timer(1000, e -> updateTime());
        timer.start();
    }

    private void setupHost() {
        new Thread(() -> {
            try {
                //địa chỉ của nhóm họp
                groupAddress = InetAddress.getLocalHost();

                multicastSocket = new MulticastSocket(port);
                multicastSocket.joinGroup(groupAddress);

//                new Thread(() -> {
//                    try {
//                        while (true) {
//                            if (isCameraOn) {
//                                sendVideo(groupAddress);
//                            }
//                        }
//                    } catch (IOException | InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }).start();
                new Thread(() -> receiveData()).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
    private void sendVideo(InetAddress groupAddress) throws IOException, InterruptedException {
        frame = webcam.getImage();
        while (frame != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(frame, "jpg", baos);
            byte[] frameData = baos.toByteArray();

            sendPacket = new DatagramPacket(frameData, frameData.length, groupAddress, port);
            multicastSocket.send(sendPacket); // Gửi gói video
            Thread.sleep(50);
        }
    }
    private void receiveData() {
        try {
            byte[] buffer = new byte[BUFFER.length];
            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
                multicastSocket.receive(receivePacket);

                String senderData = new String(receivePacket.getData(), 0, receivePacket.getLength());
                if (senderData.startsWith("CHAT:")) {
                    chatArea.append(senderData.substring(5) + "\n");
                } else {
                    ByteArrayInputStream bais = new ByteArrayInputStream(receivePacket.getData());
                    BufferedImage receivedFrame = ImageIO.read(bais);
                    updateVideoDisplay(new JLabel(new ImageIcon(receivedFrame)));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void sendChat() {
        try {
            String message = "CHAT:" + username + ": " + chatInput.getText().trim();
            byte[] messageData = message.getBytes();

            sendPacket = new DatagramPacket(messageData, messageData.length, groupAddress, port);
            multicastSocket.send(sendPacket);
            chatArea.append(username + ": " + chatInput.getText().trim() + "\n");
            chatInput.setText("");
        } catch (IOException e) {
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
        if (webcam != null) {
            webcam.setViewSize(new Dimension(WEBCAM_WIDTH, WEBCAM_HEIGHT));
            return new WebcamPanel(webcam);
        } else {
            JOptionPane.showMessageDialog(null, "Webcam not detected!");
            return new WebcamPanel(Webcam.getDefault()); // Hoặc tạo một webcam panel mặc định.
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
            String exitMessage = username + " đã rời khỏi phòng.";
            byte[] exitBuffer = exitMessage.getBytes();
            sendPacket = new DatagramPacket(exitBuffer, exitBuffer.length, groupAddress, port);
            multicastSocket.send(sendPacket);

            if (multicastSocket != null) {
                multicastSocket.leaveGroup(groupAddress);
                multicastSocket.close();
            }
            if (webcam != null && webcam.isOpen()) {
                webcam.close();
            }
            if (timer != null) {
                timer.stop();
            }
            SwingUtilities.getWindowAncestor(this).dispose();
            System.out.println("Đã rời khỏi phòng họp và đóng tài nguyên.");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi thoát khỏi phòng họp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
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

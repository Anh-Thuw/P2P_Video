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
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;


public class RoomMainHost extends JPanel {
    private static final int WEBCAM_WIDTH = 640, WEBCAM_HEIGHT = 480;

    // UI Components

    private JPanel otherWebcamPanel = new JPanel(new BorderLayout());
    private JButton              btnSend , btnEndCall, btnMute, btnToggleVideo , btnChatToggle;
    private JLabel               lblTime, lblRoomCode;
    private JPanel               chatPanel , webcamPanel ;
    private JTextArea            chatArea;
    private JScrollPane          chatScrollPane;

    private JTextField           chatInput;
    private Timer                timer;
    private Webcam               webcam;
    // Networking
    private ObjectInputStream    objectInputStream;
    private ObjectOutputStream   objectOutputStream;
    private DataOutputStream dataOutputStream ;
    private DataInputStream dataInputStream ;
    private ServerSocket         serverSocket  ;
    private Socket               clientSocket;
    private WebcamPanel          camPanel;
    private String               username;
    private int                  port ;
    private boolean              isCameraOn = true;
    private boolean              isMicOn = true;
    private  BufferedImage       frame ;
    private List<Socket> clientSockets = Collections.synchronizedList(new ArrayList<>());
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
        btnSend.addActionListener(e -> sendChat());
        chatPanel.add(btnSend);

        chatPanel.setVisible(false);

        timer = new Timer(1000, e -> updateTime());
        timer.start();
    }

    private void setupHost() {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(port);
                clientSocket = serverSocket.accept();

                clientSockets.add(clientSocket);

                dataOutputStream    = new DataOutputStream(clientSocket.getOutputStream());
                dataInputStream     = new DataInputStream(clientSocket.getInputStream());

//                objectInputStream   = new ObjectInputStream(clientSocket.getInputStream());
//                objectOutputStream  = new ObjectOutputStream(clientSocket.getOutputStream());

                //video
//                new Thread(() -> sendVideo(dataOutputStream)).start();
//                new Thread(() -> receiveVideo(dataInputStream)).start();
                sendVideo(dataOutputStream);
                receiveVideo(dataInputStream);
                // chat
                receiveChat(dataInputStream);
//                new Thread(() -> receiveChat(clientSocket)).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
    // Nhận video từ các client khác
//    private void receiveVideo(Socket clientSocket) {
//        try {
//            System.out.println("zoo");
//            ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
//            while (true) {
//                ImageIcon receivedImage = (ImageIcon) objectInputStream.readObject();
//                SwingUtilities.invokeLater(() -> {
//                    JLabel videoLabel = new JLabel(receivedImage);
//                    updateVideoDisplay(videoLabel);
//                });
//                try {
//                    Thread.sleep(50);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//    }

    private void receiveVideo(DataInputStream inputStream){
        new Thread(() -> {
            try {
                while (true) {
                    // Đọc kích thước của ảnh
                    int length = inputStream.readInt();
                    byte[] imageBytes = new byte[length];

                    // Đọc nội dung ảnh
                    inputStream.readFully(imageBytes);

                    // Chuyển đổi byte thành ảnh
                    BufferedImage image = convertBytesToImage(imageBytes);

                    // Hiển thị ảnh trên JLabel
//                    ImageIcon icon = new ImageIcon(image);
//                    JLabel videoLabel = new JLabel(icon);
//                    videoLabel.setIcon(icon);
//                    updateVideoDisplay(videoLabel);
                    // Hiển thị ảnh trên JLabel
                    ImageIcon icon = new ImageIcon(image);
                    JLabel videoLabel = new JLabel(icon);

                    // Thêm video vào webcam panel
                    updateVideoDisplay(videoLabel);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    //    private void sendVideo(Socket clientSocket) {
//        try (ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())) {
//            while (isCameraOn) {
//                frame = webcam.getImage();
//                ImageIcon imageIcon = new ImageIcon(frame);
//                out.writeObject(imageIcon);
//                out.flush();
//                Thread.sleep(50);
//            }
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
    private void sendVideo(DataOutputStream outputStream) {

        new Thread(() -> {
            try {
                System.out.println("cam send: "+isCameraOn);

                while (isCameraOn) {
                    if(!webcam.isOpen()){
                        webcam = Webcam.getDefault();
                        System.out.println("co zo");
                        webcam.open();
                    }

                    try {
                        // Đợi 1 giây hoặc lâu hơn để webcam ổn định
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    BufferedImage image = webcam.getImage();
                    byte[] imageBytes = convertImageToBytes(image);

                    // Gửi kích thước của ảnh trước
                    outputStream.writeInt(imageBytes.length);
                    outputStream.flush();

                    // Gửi nội dung ảnh
                    outputStream.write(imageBytes);
                    outputStream.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
    private byte[] convertImageToBytes(BufferedImage image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos);
        return baos.toByteArray();
    }

    private BufferedImage convertBytesToImage(byte[] bytes) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        return ImageIO.read(bais);
    }


    private void receiveChat(DataInputStream dataInputStream) {
        new Thread(() -> {
            try {
                while (true) {
                    String message;
                    if (dataInputStream.available() > 0) {
                        // Đọc tin nhắn
                        message = dataInputStream.readUTF();

                        for (Socket socket : clientSockets) {
                            if (socket != clientSocket) {
                                new DataOutputStream(socket.getOutputStream()).writeUTF(message);
                            }
                        }
                        chatArea.append(message + "\n");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void sendChat() {

        try {
            String inputText = chatInput.getText().trim();
            if (inputText.isEmpty()) return;

            String message = username + ": " + inputText;
            for (Socket socket : clientSockets) {
                new DataOutputStream(socket.getOutputStream()).writeUTF(message);
            }
            chatArea.append(message + "\n");
            chatInput.setText("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // Cập nhật hiển thị video nhận được
//    private void updateVideoDisplay(JLabel videoLabel) {
//        webcamPanel.removeAll();
//        webcamPanel.add(videoLabel);
//        webcamPanel.revalidate();
//        webcamPanel.repaint();
//    }
    private void updateVideoDisplay(JLabel videoLabel) {
        // Nếu webcam của người gửi chưa được thêm, thêm vào một panel mới
        if (otherWebcamPanel.getComponentCount() == 0) {
            otherWebcamPanel.add(videoLabel, BorderLayout.CENTER);
            webcamPanel.add(otherWebcamPanel, BorderLayout.EAST);
        } else {
            // Cập nhật lại video của người gửi
            otherWebcamPanel.removeAll();
            otherWebcamPanel.add(videoLabel, BorderLayout.CENTER);
        }

        // Cập nhật lại giao diện
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
            System.out.println("cam dong: "+isCameraOn);
        } else {
            updateButtonIcon(buttonOnOffVideo, "IconOnVideo.png");
            startWebcam();
            isCameraOn = !isCameraOn;
            System.out.println("cam bat: "+isCameraOn);
        }
    }
    private void startWebcam() {
        webcam = Webcam.getDefault();
        if (webcam != null && !webcam.isOpen()) {
            webcam.open();
            camPanel.start();
//            sendData = true;
            sendVideo(dataOutputStream);
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

            if (dataInputStream != null) {
                dataInputStream.close();
                dataInputStream = null;
            }

            if (dataOutputStream != null) {
                dataOutputStream.close();
                dataOutputStream.flush();
                dataOutputStream = null;
            }
//            if (objectInputStream != null) {
//                objectInputStream.close();
//                objectInputStream = null;
//            }
//
//            if (objectOutputStream != null) {
//                objectOutputStream.close();
//                objectOutputStream.flush();
//                objectOutputStream = null;
//            }

            if (webcam != null) {
                webcam.close();
                webcam = null;
            }
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

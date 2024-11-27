
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HostInterface extends JFrame {

    private Webcam webcam;
    private boolean isCameraOn = true;
    private boolean isMicOn = true;
    private static JLabel video = new JLabel();
//    private static JLabel videoOut = new JLabel();
    private ImageIcon icOut;
    private BufferedImage br;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Socket clientSocket;
    private ServerSocket serverSocket;
    private WebcamPanel camPanel;
//    private boolean sendData = true;
    
    public HostInterface(int port, String name) throws ClassNotFoundException {
        SwingUtilities.invokeLater(() -> {

            try {
                setSize(1200, 700);
                setTitle("Host Video Room");
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                JPanel containerPanelLeftAndRight = new JPanel(new GridLayout(1, 2));

                JPanel panelCenter = new JPanel(new BorderLayout());
                JButton buttonOnOffMic = createButton("IconOnMic.png", "IconOffMic.png");
                JButton buttonOnOffVideo = createButton("IconOnVideo.png", "IconOffVideo.png");
                JButton buttonExitVideoRoom = createButton("IconExit.png", null);
                JPanel buttonPanel = new JPanel();

                buttonOnOffMic.addActionListener(e -> toggleMic(buttonOnOffMic));
                buttonOnOffVideo.addActionListener(e -> toggleVideo(buttonOnOffVideo));
                buttonExitVideoRoom.addActionListener(e -> exitVideoRoom());

                InetAddress ip = InetAddress.getLocalHost();
                String portString = String.valueOf(port);

                JLabel ipRoom = new JLabel("IP: " + ip.getHostAddress());
                JLabel portRoom = new JLabel("Port: " + portString);

                buttonPanel.add(buttonOnOffMic);
                buttonPanel.add(buttonOnOffVideo);
                buttonPanel.add(buttonExitVideoRoom);
                buttonPanel.add(ipRoom);
                buttonPanel.add(portRoom);

                JPanel webcamPanel = new JPanel();
                webcamPanel.setLayout(new BorderLayout());
                camPanel = webcamPanel();
                
                webcamPanel.add(camPanel, BorderLayout.CENTER);

                panelCenter.add(buttonPanel, BorderLayout.SOUTH);
                panelCenter.add(video, BorderLayout.EAST);
                panelCenter.add(webcamPanel, BorderLayout.CENTER);

                containerPanelLeftAndRight.add(panelCenter);

                getContentPane().add(containerPanelLeftAndRight);
                setVisible(true);
            } catch (Exception ex) {
                Logger.getLogger(HostInterface.class.getName()).log(Level.SEVERE, null, ex);
                video.setIcon(null);
            } 
        });
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
                        Logger.getLogger(HostInterface.class.getName()).log(Level.SEVERE, null, ex);
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
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(HostInterface.class.getName()).log(Level.SEVERE, null, ex);
                        video.setIcon(null);
                    }
                }).start();
                
            } catch (IOException ex) {
                Logger.getLogger(HostInterface.class.getName()).log(Level.SEVERE, null, ex);
                video.setIcon(null);
            }
        }).start();
    }

    private WebcamPanel webcamPanel() {
        
        webcam = Webcam.getDefault();
        webcam.setViewSize(new Dimension(640, 480));

        camPanel = new WebcamPanel(webcam);        

        return camPanel;
    }

    private void toggleMic(JButton buttonOnOffMic) {

        if (isMicOn) {
            isMicOn = !isMicOn;
            updateButtonIcon(buttonOnOffMic, "IconOffMic.png");
        } else {
            isMicOn = !isMicOn;
            updateButtonIcon(buttonOnOffMic, "IconOnMic.png");
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
        e.printStackTrace(); // Handle or log the exception as needed
    } finally {
        MainInterface main = new MainInterface();
        main.setVisible(true);
        setVisible(false);
        dispose();
    }
}

    private JButton createButton(String iconOnPath, String iconOffPath) {
        ImageIcon iconOn = new ImageIcon("src/main/java/com/mycompany/baitaplonmonhoc/img/" + iconOnPath);
        Image scaledImage = iconOn.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        return new JButton(new ImageIcon(scaledImage));
    }
}

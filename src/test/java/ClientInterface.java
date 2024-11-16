
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientInterface extends JFrame {

    private Webcam webcam;
    private boolean isCameraOn = true;
    private boolean isMicOn = true;
    private static JLabel video = new JLabel();
    private static JLabel videoIn = new JLabel();
    private ImageIcon icOut;
    private BufferedImage br;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Socket socket;
    private WebcamPanel camPanel;
//    private boolean sendData = true;

    public ClientInterface(String IP_Server, int port, String name) throws ClassNotFoundException {

        SwingUtilities.invokeLater(() -> {

            setSize(1200, 700);
            setTitle("Client Video Room");
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

            buttonPanel.add(buttonOnOffMic);
            buttonPanel.add(buttonOnOffVideo);
            buttonPanel.add(buttonExitVideoRoom);

            JPanel webcamPanel = new JPanel();
            webcamPanel.setLayout(new BorderLayout());
            camPanel = webcamPanel();
  
            webcamPanel.add(camPanel, BorderLayout.CENTER);

            panelCenter.add(buttonPanel, BorderLayout.SOUTH);
            panelCenter.add(webcamPanel, BorderLayout.CENTER);
            panelCenter.add(videoIn, BorderLayout.EAST);

            containerPanelLeftAndRight.add(panelCenter);

            getContentPane().add(containerPanelLeftAndRight);
            setVisible(true);
        });

        new Thread(() -> {
            try {
                socket = new Socket(IP_Server, port);

                new Thread(() -> {
                    try {
                        out = new ObjectOutputStream(socket.getOutputStream());

                        
                            webcam.open();
                            isCameraOn = true;
                            isMicOn = true;
                       

                        while (true) {
                            br = webcam.getImage();
                            icOut = new ImageIcon(br);
                            out.writeObject(icOut);
                            out.flush();
                            System.out.println("outToHost");
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(HostInterface.class.getName()).log(Level.SEVERE, null, ex);
                        MainInterface main = new MainInterface();
                        main.setVisible(true);
                        setVisible(false);
                        dispose();
                    }
                }).start();

                new Thread(() -> {
                    try {
                        in = new ObjectInputStream(socket.getInputStream());

                        while (true) {
                            ImageIcon ic = (ImageIcon) in.readObject();
                            video.setIcon(ic);
                            System.out.println("inFromHost");
                        }
                    } catch (IOException | ClassNotFoundException ex) {
                        Logger.getLogger(HostInterface.class.getName()).log(Level.SEVERE, null, ex);
                        video.setIcon(null);
                    }
                }).start(); 
                
            } catch (IOException ex) {
                Logger.getLogger(HostInterface.class.getName()).log(Level.SEVERE, null, ex);
                MainInterface main = new MainInterface();
                main.setVisible(true);
                setVisible(false);
                dispose();
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
       

        if (socket != null) {
            socket.close();
            socket = null;
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
 
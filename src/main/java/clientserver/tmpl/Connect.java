package clientserver.tmpl;

import clientserver.services.Server;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.border.LineBorder;


public class Connect extends JFrame {

	private JPanel contentPane;
	private Socket socket;
	public Server server;
	private JLabel lblNewLabel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Connect frame = new Connect();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Connect() {
		setForeground(new Color(128, 0, 0));
		this.setTitle("Connection");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 280);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(232, 232, 232));
		contentPane.setBorder(new LineBorder(new Color(123, 123, 123)));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		lblNewLabel = new JLabel("Connection!");
		lblNewLabel.setForeground(new Color(183, 0, 0));
		lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 54));
		lblNewLabel.setBounds(85, 38, 317, 77);
		contentPane.add(lblNewLabel);
		JButton btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        updateStatusLabel("Connected!");

		        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
		            @Override
		            protected Void doInBackground() throws Exception {
		                server = new Server(1234);
		                server.startServer();
		                return null;
		                
		            }
		        };

		        worker.execute();
		    }
		});
		btnConnect.setForeground(new Color(128, 0, 0));
		btnConnect.setBackground(new Color(255, 185, 185));
		btnConnect.setFont(new Font("Tahoma", Font.PLAIN, 23));
		btnConnect.setBounds(53, 126, 155, 59);
		contentPane.add(btnConnect);
		
		JButton btnDisconnect = new JButton("Disconnect");
		btnDisconnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateStatusLabel("Connection!");
				//lblNewLabel.setText("Connection!");
				
					server.closeServer();
					System.out.println("Server closed!");
					
				
				
			}
		});
		btnDisconnect.setBackground(new Color(213, 213, 213));
		btnDisconnect.setFont(new Font("Tahoma", Font.PLAIN, 23));
		btnDisconnect.setBounds(233, 126, 155, 59);
		contentPane.add(btnDisconnect);
		
		
	}
	private void updateStatusLabel(String status) {
		
	    lblNewLabel.setText(status);
	
		
	}
	
	
}
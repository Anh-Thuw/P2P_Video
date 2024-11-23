package Client.tmpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class RoomManager extends JPanel {
    private DefaultListModel<String> memberListModel;

    public RoomManager() {
        setBackground(Color.LIGHT_GRAY);
        setLayout(new BorderLayout());

        JLabel lblTitle = new JLabel("Room Manager", SwingConstants.CENTER);
        lblTitle.setBackground(Color.WHITE);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        add(lblTitle, BorderLayout.NORTH);

        memberListModel = new DefaultListModel<>();
        JList<String> memberList = new JList<>(memberListModel);
        memberList.setFont(new Font("Arial", Font.PLAIN, 18));
        memberList.setBackground(Color.WHITE);
        memberList.setBorder(BorderFactory.createEtchedBorder());

        // Custom cell renderer for spacing and formatting
        memberList.setCellRenderer(new MemberListCellRenderer());

        JScrollPane scrollPane = new JScrollPane(memberList);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        scrollPane.setBorder(BorderFactory.createTitledBorder("Members List"));
        add(scrollPane, BorderLayout.CENTER);

        // Sample members to display
        addSampleMembers();

        // Panel for member controls
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout()); // Using FlowLayout to keep buttons in one line

        JButton btnRemove = createButton("Remove");
        btnRemove.setPreferredSize(new Dimension(200, 60)); // Increased height and width
        controlPanel.add(btnRemove);

        JButton btnMute = createButton("Mute Mic");
        btnMute.setPreferredSize(new Dimension(200, 60)); // Increased height and width
        controlPanel.add(btnMute);

        JButton btnDisableCamera = createButton("Disable Camera");
        btnDisableCamera.setPreferredSize(new Dimension(200, 60)); // Increased height and width
        controlPanel.add(btnDisableCamera);
        
        add(controlPanel, BorderLayout.SOUTH);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(174, 238, 255)); // Light turquoise color
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEtchedBorder());
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (text.equals("Remove")) {
                    removeSelectedMember();
                } else if (text.equals("Mute Mic")) {
                    muteSelectedMember();
                } else if (text.equals("Disable Camera")) {
                    disableCameraForSelectedMember();
                }
            }
        });
        return button;
    }

    private void addSampleMembers() {
        String[] sampleMembers = {
            "John Doe - " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
            "Jane Smith - " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
            "Alice Johnson - " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
            "Bob Brown - " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
            "Emily Davis - " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
            "Michael Wilson - " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
            "Sarah Johnson - " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
            "David Lee - " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
        };
        for (String member : sampleMembers) {
            memberListModel.addElement(member);
        }
    }

    private void removeSelectedMember() {
        int selectedIndex = memberListModel.getSize() - 1; // Assume last member selected for example
        if (selectedIndex != -1) {
            memberListModel.remove(selectedIndex);
        }
    }

    private void muteSelectedMember() {
        // Logic to mute the selected member
        JOptionPane.showMessageDialog(this, "Muted the selected member.");
    }

    private void disableCameraForSelectedMember() {
        // Logic to disable camera for the selected member
        JOptionPane.showMessageDialog(this, "Camera disabled for the selected member.");
    }

    // Custom cell renderer to format list items
    private static class MemberListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                      boolean isSelected, boolean cellHeight) {
            String[] parts = value.toString().split(" - ");
            String name = parts[0]; // Member name
            String time = parts.length > 1 ? parts[1] : ""; // Time
            
            // Create a panel for custom layout
            JPanel panel = new JPanel(new BorderLayout());
            JLabel nameLabel = new JLabel(name);
            JLabel timeLabel = new JLabel(time);
            
            nameLabel.setFont(new Font("Arial", Font.PLAIN, 18));
            timeLabel.setFont(new Font("Arial", Font.PLAIN, 18));
            timeLabel.setHorizontalAlignment(SwingConstants.RIGHT); // Align time to the right
            
            panel.add(nameLabel, BorderLayout.WEST); // Add name on the left
            panel.add(timeLabel, BorderLayout.EAST); // Add time on the right
            
            // Add spacing to the panel
            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            // Change background color when selected
            if (isSelected) {
                panel.setBackground(new Color(200, 255, 255)); // Light color when selected
            } else {
                panel.setBackground(Color.WHITE); // Default color
            }
            
            return panel;
        }
    }
}

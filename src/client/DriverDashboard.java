package client;

import javax.swing.*;
import java.awt.*;
import shared.Message;
import shared.RideRequest;
import shared.User;

public class DriverDashboard extends JPanel {
    private ServerConnection connection;
    private User currentUser;
    private DefaultListModel<String> requestListModel;
    private JList<String> requestList;
    private RideRequest currentPendingRequest;
    private JLabel statusLabel;

    public DriverDashboard(JFrame parent, ServerConnection connection) {
        this.connection = connection;
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        statusLabel = new JLabel("Driver Dashboard - Waiting for requests...");
        headerPanel.add(statusLabel);
        add(headerPanel, BorderLayout.NORTH);

        requestListModel = new DefaultListModel<>();
        requestList = new JList<>(requestListModel);
        add(new JScrollPane(requestList), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        JButton acceptBtn = new JButton("Accept Selected Ride");
        JButton updateLocBtn = new JButton("Simulate Movement");
        bottomPanel.add(acceptBtn);
        bottomPanel.add(updateLocBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        acceptBtn.addActionListener(e -> {
            if (currentPendingRequest != null) {
                connection.sendMessage(new Message("ACCEPT_RIDE", currentPendingRequest));
                statusLabel.setText("Ride Accepted! En route to pickup.");
                requestListModel.clear();
            }
        });

        // Simulate GPS location update loop for real-time feature
        updateLocBtn.addActionListener(e -> {
            new Thread(() -> {
                double lat = 10.0;
                double lng = 20.0;
                for (int i = 0; i < 10; i++) {
                    lat += 0.5;
                    lng += 0.5;
                    connection.sendMessage(new Message("LOCATION_UPDATE", new double[]{lat, lng}));
                    try { Thread.sleep(2000); } catch (Exception ignored) {}
                }
            }).start();
        });
    }

    public void setUser(User user) {
        this.currentUser = user;
    }

    public void handleMessage(Message msg) {
        if (msg.type.equals("RIDE_REQUEST")) {
            currentPendingRequest = (RideRequest) msg.payload;
            requestListModel.addElement("New Request from Rider " + currentPendingRequest.riderId + 
                " [Start: " + currentPendingRequest.startLat + ", " + currentPendingRequest.startLng + "]");
        }
    }
}

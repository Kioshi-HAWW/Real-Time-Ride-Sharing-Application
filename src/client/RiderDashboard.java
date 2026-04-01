package client;

import javax.swing.*;
import java.awt.*;
import shared.Message;
import shared.RideRequest;
import shared.User;

public class RiderDashboard extends JPanel {
    private ServerConnection connection;
    private User currentUser;
    private JLabel statusLabel;
    private JLabel locationLabel;

    public RiderDashboard(JFrame parent, ServerConnection connection) {
        this.connection = connection;
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        statusLabel = new JLabel("Welcome Rider! Request a ride.");
        headerPanel.add(statusLabel);
        add(headerPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(3, 1));
        
        JButton requestBtn = new JButton("Request Ride");
        centerPanel.add(requestBtn);
        
        locationLabel = new JLabel("Driver Location: N/A", SwingConstants.CENTER);
        centerPanel.add(locationLabel);

        add(centerPanel, BorderLayout.CENTER);

        requestBtn.addActionListener(e -> {
            if (currentUser != null) {
                statusLabel.setText("Requesting ride...");
                // Mock coordinates
                RideRequest req = new RideRequest(currentUser.id, 10.0, 20.0, 15.0, 25.0);
                connection.sendMessage(new Message("REQUEST_RIDE", req));
            }
        });
    }

    public void setUser(User user) {
        this.currentUser = user;
        statusLabel.setText("Welcome " + user.username + "! Request a ride.");
    }

    public void handleMessage(Message msg) {
        if (msg.type.equals("RIDE_ACCEPTED")) {
            int driverId = (int) msg.payload;
            statusLabel.setText("Ride Accepted by Driver " + driverId + "! Waiting for arrival...");
        } else if (msg.type.equals("LOCATION_UPDATE")) {
            double[] loc = (double[]) msg.payload;
            locationLabel.setText("Driver Location: Lat " + loc[0] + ", Lng " + loc[1]);
        }
    }
}

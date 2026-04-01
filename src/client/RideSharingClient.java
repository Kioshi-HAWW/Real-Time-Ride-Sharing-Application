package client;

import javax.swing.*;
import java.awt.*;
import shared.Message;
import shared.User;

public class RideSharingClient extends JFrame {
    private ServerConnection connection;
    private JPanel mainPanel;
    private CardLayout cardLayout;

    public RideSharingClient() {
        setTitle("Real-Time Ride Sharing App");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        connection = new ServerConnection();
        if (!connection.connect()) {
            JOptionPane.showMessageDialog(this, "Could not connect to the server at 127.0.0.1:8080");
            System.exit(1);
        }

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // UI components
        LoginPanel loginPanel = new LoginPanel(this, connection);
        RiderDashboard riderDashboard = new RiderDashboard(this, connection);
        DriverDashboard driverDashboard = new DriverDashboard(this, connection);

        mainPanel.add(loginPanel, "LOGIN");
        mainPanel.add(riderDashboard, "RIDER_DASHBOARD");
        mainPanel.add(driverDashboard, "DRIVER_DASHBOARD");

        add(mainPanel);

        // Core message router for the client
        connection.setOnMessageReceived(msg -> {
            SwingUtilities.invokeLater(() -> {
                if (msg.type.equals("LOGIN_SUCCESS")) {
                    User user = (User) msg.payload;
                    if ("RIDER".equals(user.role)) {
                        riderDashboard.setUser(user);
                        cardLayout.show(mainPanel, "RIDER_DASHBOARD");
                    } else {
                        driverDashboard.setUser(user);
                        cardLayout.show(mainPanel, "DRIVER_DASHBOARD");
                    }
                } else if (msg.type.equals("LOGIN_FAILED")) {
                    JOptionPane.showMessageDialog(this, (String) msg.payload, "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    // Route other messages to active dashboard
                    riderDashboard.handleMessage(msg);
                    driverDashboard.handleMessage(msg);
                }
            });
        });

        cardLayout.show(mainPanel, "LOGIN");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new RideSharingClient().setVisible(true);
        });
    }
}

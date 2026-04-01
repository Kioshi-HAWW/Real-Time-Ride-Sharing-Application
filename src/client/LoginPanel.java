package client;

import javax.swing.*;
import java.awt.*;
import shared.Message;

public class LoginPanel extends JPanel {
    public LoginPanel(JFrame parent, ServerConnection connection) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Ride Sharing Authentication");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        add(new JLabel("Username:"), gbc);
        JTextField userField = new JTextField(15);
        gbc.gridx = 1; add(userField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Password:"), gbc);
        JPasswordField passField = new JPasswordField(15);
        gbc.gridx = 1; add(passField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Role:"), gbc);
        String[] roles = {"RIDER", "DRIVER"};
        JComboBox<String> roleBox = new JComboBox<>(roles);
        gbc.gridx = 1; add(roleBox, gbc);

        JPanel btnPanel = new JPanel();
        JButton loginBtn = new JButton("Login");
        JButton registerBtn = new JButton("Register");
        btnPanel.add(loginBtn);
        btnPanel.add(registerBtn);
        
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        add(btnPanel, gbc);

        loginBtn.addActionListener(e -> {
            String u = userField.getText();
            String p = new String(passField.getPassword());
            connection.sendMessage(new Message("LOGIN", new String[]{u, p}));
        });

        registerBtn.addActionListener(e -> {
            String u = userField.getText();
            String p = new String(passField.getPassword());
            String r = (String) roleBox.getSelectedItem();
            connection.sendMessage(new Message("REGISTER", new String[]{u, p, r}));
        });
    }
}

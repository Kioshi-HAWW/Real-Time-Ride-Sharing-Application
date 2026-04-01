package server;

import shared.Message;
import shared.User;
import shared.RideRequest;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler extends Thread {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private User loggedInUser;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public User getUser() {
        return loggedInUser;
    }

    @Override
    public void run() {
        try {
            // Note: create output stream before input stream to prevent blocking
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            while (true) {
                Message msg = (Message) in.readObject();
                handleMessage(msg);
            }
        } catch (Exception e) {
            System.out.println("Client disconnected.");
        } finally {
            if (loggedInUser != null) {
                RideManager.removeClient(loggedInUser.id);
            }
            try { socket.close(); } catch (Exception ignored) {}
        }
    }

    private void handleMessage(Message msg) {
        try {
            switch (msg.type) {
                case "LOGIN":
                    String[] creds = (String[]) msg.payload;
                    loggedInUser = DatabaseManager.loginUser(creds[0], creds[1]);
                    if (loggedInUser != null) {
                        RideManager.addClient(loggedInUser, this);
                        sendMessage(new Message("LOGIN_SUCCESS", loggedInUser));
                    } else {
                        sendMessage(new Message("LOGIN_FAILED", "Invalid credentials"));
                    }
                    break;
                case "REGISTER":
                    String[] regData = (String[]) msg.payload; // [username, password, role]
                    loggedInUser = DatabaseManager.registerUser(regData[0], regData[1], regData[2]);
                    if (loggedInUser != null) {
                        RideManager.addClient(loggedInUser, this);
                        sendMessage(new Message("LOGIN_SUCCESS", loggedInUser));
                    } else {
                        sendMessage(new Message("LOGIN_FAILED", "Registration failed. Username might exist."));
                    }
                    break;
                case "REQUEST_RIDE":
                    RideRequest req = (RideRequest) msg.payload;
                    RideManager.requestRide(req);
                    break;
                case "ACCEPT_RIDE":
                    RideRequest accReq = (RideRequest) msg.payload;
                    RideManager.acceptRide(loggedInUser.id, accReq);
                    break;
                case "LOCATION_UPDATE":
                    double[] loc = (double[]) msg.payload; // [lat, lng]
                    RideManager.updateDriverLocation(loggedInUser.id, loc[0], loc[1]);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void sendMessage(Message msg) {
        try {
            out.writeObject(msg);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

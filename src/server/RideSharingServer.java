package server;

import java.net.ServerSocket;
import java.net.Socket;

public class RideSharingServer {
    private static final int PORT = 8080;

    public static void main(String[] args) {
        System.out.println("Starting the Server...");
        DatabaseManager.initializeDatabase(); // ensure DB is ready

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is listening on port " + PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected: " + socket.getInetAddress());
                new ClientHandler(socket).start(); // Start thread for real-time events
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package client;

import shared.Message;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.function.Consumer;

public class ServerConnection {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int PORT = 8080;
    
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Consumer<Message> onMessageReceived;

    public boolean connect() {
        try {
            socket = new Socket(SERVER_ADDRESS, PORT);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            
            // Start listening for server messages in a separate thread
            new Thread(() -> {
                try {
                    while (true) {
                        Message msg = (Message) in.readObject();
                        if (onMessageReceived != null) {
                            onMessageReceived.accept(msg);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Connection to server lost.");
                }
            }).start();
            
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void setOnMessageReceived(Consumer<Message> callback) {
        this.onMessageReceived = callback;
    }

    public void sendMessage(Message msg) {
        try {
            if (out != null) {
                out.writeObject(msg);
                out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

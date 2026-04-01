package shared;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public int id;
    public String username;
    public String role; // "RIDER" or "DRIVER"
    public double latitude;
    public double longitude;
    public boolean isAvailable; // For drivers

    public User(int id, String username, String role) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.isAvailable = role.equals("DRIVER");
    }
}

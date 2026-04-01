package shared;

import java.io.Serializable;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // Command types e.g., "LOGIN", "RIDE_REQUEST", "MATCH_FOUND", "LOCATION_UPDATE"
    public String type; 
    public Object payload;
    
    public Message(String type, Object payload) {
        this.type = type;
        this.payload = payload;
    }
}

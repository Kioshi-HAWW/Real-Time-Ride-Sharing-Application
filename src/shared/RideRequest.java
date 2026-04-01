package shared;

import java.io.Serializable;

public class RideRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public int id;
    public int riderId;
    public int driverId;
    public double startLat;
    public double startLng;
    public double endLat;
    public double endLng;
    public String status; // "PENDING", "ACCEPTED", "COMPLETED", "CANCELLED"

    public RideRequest(int riderId, double startLat, double startLng, double endLat, double endLng) {
        this.riderId = riderId;
        this.startLat = startLat;
        this.startLng = startLng;
        this.endLat = endLat;
        this.endLng = endLng;
        this.status = "PENDING";
    }
}

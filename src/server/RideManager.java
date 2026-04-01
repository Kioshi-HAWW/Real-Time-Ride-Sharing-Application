package server;

import shared.Message;
import shared.RideRequest;
import shared.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// Manages real-time state independent of long-term database storage
public class RideManager {
    // Maps userId to their active handler for real-time routing
    public static final Map<Integer, ClientHandler> activeClients = new ConcurrentHashMap<>();
    
    // Maps driverId to their current location mapping
    public static final Map<Integer, double[]> driverLocations = new ConcurrentHashMap<>();
    
    // Maps Rider ID to active ride request
    public static final Map<Integer, RideRequest> activeRides = new ConcurrentHashMap<>();

    public static synchronized void addClient(User user, ClientHandler handler) {
        activeClients.put(user.id, handler);
        if ("DRIVER".equals(user.role)) {
            // Initial location can be set later
            driverLocations.put(user.id, new double[]{user.latitude, user.longitude});
        }
    }

    public static synchronized void removeClient(int userId) {
        ClientHandler handler = activeClients.remove(userId);
        if (handler != null && handler.getUser() != null) {
            if ("DRIVER".equals(handler.getUser().role)) {
                driverLocations.remove(userId);
            }
        }
    }

    public static void updateDriverLocation(int driverId, double lat, double lng) {
        driverLocations.put(driverId, new double[]{lat, lng});
        
        // Find if this driver is in an active ride, and relay location to rider
        for (RideRequest req : activeRides.values()) {
            if (req.driverId == driverId && req.status.equals("ACCEPTED")) {
                ClientHandler riderHandler = activeClients.get(req.riderId);
                if (riderHandler != null) {
                    riderHandler.sendMessage(new Message("LOCATION_UPDATE", new double[]{lat, lng}));
                }
            }
        }
    }

    public static void requestRide(RideRequest request) {
        activeRides.put(request.riderId, request);
        
        // Simple matching logic: broadcast to all available drivers
        // In a real app, we'd calculate distance based on driverLocations
        for (ClientHandler handler : activeClients.values()) {
            if (handler.getUser() != null && "DRIVER".equals(handler.getUser().role) && handler.getUser().isAvailable) {
                handler.sendMessage(new Message("RIDE_REQUEST", request));
            }
        }
    }

    public static void acceptRide(int driverId, RideRequest request) {
        if (activeRides.containsKey(request.riderId)) {
            RideRequest active = activeRides.get(request.riderId);
            if (active.status.equals("PENDING")) {
                active.status = "ACCEPTED";
                active.driverId = driverId;
                
                // Notify Rider
                ClientHandler riderHandler = activeClients.get(active.riderId);
                if (riderHandler != null) {
                    riderHandler.sendMessage(new Message("RIDE_ACCEPTED", driverId));
                }
                
                // Mark driver as unavailable
                ClientHandler driverHandler = activeClients.get(driverId);
                if (driverHandler != null) {
                    driverHandler.getUser().isAvailable = false;
                }
            }
        }
    }
}

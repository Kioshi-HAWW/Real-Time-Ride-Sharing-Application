# Report 2: Objectives and Outcomes

## Project Objectives
- **Real-Time Connectivity:** Establish a seamless link between riders and drivers using low-latency network communication.
- **Data Persistence:** Implement a robust storage system to manage user profiles, ride history, and active sessions.
- **Intuitive Interface:** Create a user-friendly desktop application that simplifies the ride-sharing process.
- **Concurrent Processing:** Handle multiple simultaneous client connections without performance degradation.

## Project Outcomes
- **Functional Prototype:** Successfully built a full-stack Java application with a dedicated server and multiple client instances.
- **Reliable Data Management:** Integration with SQLite ensures all user and ride data is persisted correctly.
- **Scalable Architecture:** The multithreaded server design allows for multiple riders and drivers to interact in the same environment.
- **Live Updates:** Riders can see driver availability and ride status updates in real-time.

## Advanced Java Concepts Used
1. **Multithreaded Socket Programming:** Used to handle concurrent client connections (`ClientHandler` and `RideSharingServer`).
2. **JDBC & SQLite:** Employed for persistent data storage and retrieval in `DatabaseManager`.
3. **Java Swing GUI:** Developed comprehensive dashboards for Riders and Drivers with dynamic updates.
4. **Serialization (Object Input/Output Streams):** Leveraged to transmit complex data objects (`Message`, `RideRequest`, `User`) over the network.
5. **Collection Framework:** Utilized for managing active rides and client connections in memory.

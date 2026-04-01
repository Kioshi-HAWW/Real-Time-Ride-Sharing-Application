# Real-Time Ride Sharing Application
**Advanced Java Programming Course Project**

This project is a real-time ride-sharing application built entirely in Java, demonstrating core concepts of the Advanced Java Programming course.

## Features
- **Client-Server Architecture:** Centralized Server handling real-time requests from multiple Java Swing Clients.
- **Multithreaded Real-Time Sockets:** Concurrently routes location updates, active rides, and requests via `Socket` and `Thread`.
- **Java Swing GUI:** Desktop application interface for Riders and Drivers.
- **JDBC & SQLite:** Stores users and trip details without needing a separate database process running.
- **Serialization:** Shared Models (`Message`, `RideRequest`, `User`) exchanged over standard I/O ObjectStreams.

## How to run the project
1. Double click `compile.bat` to compile all source codes into `/bin`.
2. Double click `run_server.bat` to launch the SQLite Socket Server.
3. Double click `run_client.bat` multiple times to launch Riders and Drivers.
4. **Login/Register:**
   - Register a Rider account and a Driver account.
   - For drivers, click `Simulate Movement` to simulate GPS, and accept ride requests.
"# Advance-Java-Challenging-grp-26" 

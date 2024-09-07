# World Cup Game Updates: STOMP Client-Server Application

This project is a client-server application built using the STOMP (Streaming Text Oriented Messaging Protocol) that delivers real-time updates for World Cup matches. Game events are formatted in JSON and provided to the clients. The client side is implemented in C++, while the server is built in Java, offering support for two server architectures: Thread-Per-Client (TPC) and Reactor.

### Key Features

- **Real-Time Notifications**: The server sends live game updates to all connected clients in real time.
- **JSON Integration**: Game events are parsed from JSON files by the server and relayed to the clients.
- **STOMP Protocol**: The client-server communication follows the STOMP protocol, enabling clients to subscribe to specific game events and receive updates.
- **Thread-Per-Client (TPC) Server**: A TCP server handles individual client connections, with each client running in its own thread.
- **Reactor Server**: The project also includes a Reactor server that processes client requests using a single-threaded event loop.

### Supported STOMP Frames
- **Server Frames**: CONNECTED, MESSAGE, RECEIPT, ERROR
- **Client Frames**: CONNECT, SEND, SUBSCRIBE, UNSUBSCRIBE, DISCONNECT

For further details, refer to the `instruction.pdf` file.

### Getting Started

To set up and run the World Cup Game Updates STOMP Client-Server application, follow these steps:

1. **Clone the repository**:
   ```bash
   git clone https://github.com/your-repo-url.git
   ```

2. **Compile the server**:
   ```bash
   javac Server.java
   ```

3. **Run the server**:
   ```bash
   java Server <server-type>
   ```
   - Replace `<server-type>` with either `TCP` or `Reactor` to choose the server architecture.

4. **Compile the client**:
   ```bash
   g++ Client.cpp -o client
   ```

5. **Run the client**:
   ```bash
   ./client <server-address> <server-port>
   ```
   - Replace `<server-address>` and `<server-port>` with the correct values for the server you're connecting to.

For additional details, please refer to the `SPL231__Assignment_3_instructions.pdf` file.

### Usage

Once both the server and client are operational, you can interact with the server by using the following client commands:

- **login `<username>` `<password>`**: Authenticates the user with the server.
- **subscribe `<game-name>`**: Subscribes to updates for the specified game.
- **report `<events-file>`**: Sends game events from the specified JSON file to the server.
- **summary `<game-name>` `<user>` `<file>`**: Outputs a summary of the game updates for the specified user into a file.
- **logout**: Logs out and disconnects the client from the server.

Refer to the `SPL231__Assignment_3_instructions.pdf` file for further usage details.

### Acknowledgments

The implementation of the STOMP protocol and supported actions is based on the guidelines provided in the project documentation.

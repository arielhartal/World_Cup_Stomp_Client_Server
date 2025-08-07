# World Cup STOMP Client-Server

A multi-threaded client-server application implementing the STOMP (Simple Text Oriented Messaging Protocol) for real-time World Cup game updates and news feeds.

## 📋 Project Overview

This project implements a complete client-server system that allows multiple clients to:
- Connect to a server using STOMP protocol
- Subscribe to World Cup game channels
- Receive real-time game updates and events
- Send and receive messages in a pub-sub architecture

The system consists of:
- **Java Server**: Multi-threaded server supporting both Thread-Per-Client and Reactor patterns
- **C++ Client**: Interactive client application for connecting to the server and managing subscriptions

## 🏗️ Architecture

### Server (Java)
- **Location**: `server/`
- **Framework**: Maven-based Java project
- **Protocol**: STOMP over TCP
- **Concurrency**: Supports both Thread-Per-Client (TPC) and Reactor patterns
- **Features**:
  - User authentication and session management
  - Channel-based message distribution
  - Real-time game event broadcasting
  - Thread-safe connections management

### Client (C++)
- **Location**: `client/`
- **Build System**: Makefile with GCC
- **Dependencies**: Boost libraries (boost_system, pthread)
- **Features**:
  - Interactive command-line interface
  - STOMP frame handling
  - Real-time event processing
  - JSON parsing for game events

## 🚀 Getting Started

### Prerequisites

**For Server:**
- Java 8 or higher
- Maven 3.x

**For Client:**
- GCC compiler with C++11 support
- Boost libraries (`libboost-system-dev`, `libboost-all-dev`)
- pthread library

### Building the Project

#### Server Build
```bash
cd server/server
mvn compile
mvn exec:java -Dexec.mainClass="bgu.spl.net.impl.stomp.StompServer" -Dexec.args="<port> <server-type>"
```

**Server Types:**
- `tpc` - Thread Per Client
- `reactor` - Reactor pattern

#### Client Build
```bash
cd client
make clean
make
```

### Running the Application

#### Start the Server
```bash
# Thread-Per-Client server on port 7777
java -cp target/classes bgu.spl.net.impl.stomp.StompServer 7777 tpc

# Reactor server on port 7777
java -cp target/classes bgu.spl.net.impl.stomp.StompServer 7777 reactor
```

#### Start the Client
```bash
cd client/bin
./StompWCIClient
```

#### Client Commands
```
login <host:port> <username> <password>
join <game-channel>
exit <game-channel>
report <filename>
summary <game> <user> <filename>
logout
```

## 📁 Project Structure

```
.
├── client/
│   ├── bin/                 # Compiled executables and object files
│   ├── data/               # Sample JSON event files
│   │   ├── events1.json    # Sample World Cup events
│   │   └── events1_partial.json
│   ├── include/            # Header files
│   │   ├── ConnectionHandler.h
│   │   ├── StompProtocol.h
│   │   ├── event.h
│   │   ├── game.h
│   │   ├── user.h
│   │   ├── clientFrame.h
│   │   └── json.hpp        # JSON parsing library
│   ├── src/                # Source files
│   │   ├── StompClient.cpp      # Main client application
│   │   ├── ConnectionHandler.cpp # TCP connection management
│   │   ├── StompProtocol.cpp    # STOMP protocol implementation
│   │   ├── event.cpp           # Event handling
│   │   ├── game.cpp           # Game state management
│   │   ├── user.cpp           # User management
│   │   └── clientFrame.cpp    # STOMP frame handling
│   └── makefile            # Build configuration
└── server/
    └── src/main/java/bgu/spl/net/
        ├── api/            # Protocol interfaces
        │   ├── MessagingProtocol.java
        │   ├── MessageEncoderDecoder.java
        │   ├── StompProtocol.java
        │   └── StompMessageEncoderDecoder.java
        ├── impl/           # Protocol implementations
        │   ├── stomp/      # STOMP-specific implementations
        │   ├── echo/       # Echo server implementation
        │   ├── newsfeed/   # News feed functionality
        │   └── rci/        # Remote Command Invocation
        └── srv/            # Server infrastructure
            ├── Server.java
            ├── BaseServer.java
            ├── Connections.java
            ├── ConnectionHandler.java
            └── Reactor.java
```

## 🎮 Game Events

The system supports various World Cup events:
- **kickoff**: Game start
- **goal!!!!**: Goal scored
- **foul**: Foul committed
- **yellow card**: Yellow card issued
- **red card**: Red card issued
- **substitution**: Player substitution
- **halftime**: End of first half
- **fulltime**: End of game

Example event format (JSON):
```json
{
    "team a": "Germany",
    "team b": "Japan",
    "events": [
        {
            "event name": "goal!!!!",
            "time": 1980,
            "general game updates": {},
            "team a updates": {
                "goals": "1",
                "possession": "90%"
            },
            "team b updates": {
                "possession": "10%"
            },
            "description": "GOOOAAALLL!!! Germany lead!!!"
        }
    ]
}
```

## 🔧 STOMP Protocol Implementation

The project implements key STOMP frames:
- **CONNECT**: Client authentication
- **SUBSCRIBE**: Channel subscription
- **UNSUBSCRIBE**: Channel unsubscription
- **SEND**: Message sending
- **DISCONNECT**: Client disconnection
- **MESSAGE**: Server-to-client messages
- **RECEIPT**: Acknowledgment frames
- **ERROR**: Error handling

## 🚨 Known Issues

⚠️ **Server Issue**: There's a known problem with the main server related to string comparison. The issue appears to be using `==` instead of `.equals()` for string comparison in Java. This needs to be fixed in the server implementation.

## 🔍 Debugging Tips

1. **Connection Issues**: Verify the server is running and the port is correct
2. **Build Issues**: Ensure all dependencies (Boost, pthread) are installed
3. **Protocol Issues**: Check STOMP frame formatting and encoding
4. **String Comparison**: Look for `==` vs `.equals()` issues in Java code

## 📚 Technologies Used

- **Languages**: Java 8, C++11
- **Protocols**: STOMP, TCP
- **Libraries**: 
  - Boost (C++)
  - pthread (C++)
  - Maven (Java)
- **Patterns**: 
  - Thread-Per-Client
  - Reactor Pattern
  - Publisher-Subscriber

## 🤝 Contributing

This is an academic project (SPL Assignment 3). For educational purposes and learning about:
- Network programming
- Multi-threading
- Protocol implementation
- Client-server architecture

## 📄 License

Academic project for Ben-Gurion University - Systems Programming Laboratory course.

---
**Course**: SPL (Systems Programming Laboratory)  
**Assignment**: Assignment 3  
**Topic**: Multi-threaded Client-Server with STOMP Protocol

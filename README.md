# ICESI University - SwArch Homework 1

## Collaborators:
- Juan Pablo Acevedo
- Jesus Garces
- Martin Cifuentes

This repository contains the source code for a homework assignment in the System Engineering course at ICESI University. The project is implemented using Java programming language, ICE middleware, and Gradle build system.

## Assignment Description:
The goal of this assignment is to implement a client-server interaction using ICE middleware. The server is designed to handle various types of messages from the client and provide appropriate responses. The server extracts relevant information like the username and hostname of the client to enhance the output.

## Project Structure:
The project is divided into two main parts: the client and the server.

### Client:
The client code (`Client.java`) is responsible for sending messages to the server. It interacts with the user, constructs messages with the client's username and hostname, and sends them to the server for processing. The client receives responses from the server and displays them in the console.

### Server:
The server code (`Server.java`) initializes an ICE communicator, sets up an object adapter, and implements the core logic for handling different types of messages. It processes messages based on specified patterns and returns relevant information to the client. The server also incorporates utility methods for prime factorization, listing logical interfaces, listing open ports, and executing commands.

## How to Run:
1. Clone the repository to your local machine.
2. Navigate to the project directory using the command line.
3. To run the server, execute the following command: ./gradlew server
4. To run the client, execute the following command: ./gradlew client
5. Follow the prompts in the client's console to interact with the server. You can enter various types of messages and observe the server's responses.

## Note:
This project is intended for educational purposes as part of the System Engineering course at ICESI University. Feel free to explore, modify, and learn from the code.

If you have any questions or need further assistance, please contact the collaborators: Juan Pablo Acevedo, Martin cifuentes and Jesus Garces.

## Importand
Change the client and server .config file

**Languages Used:**
- Java
- ICE Middleware
- Gradle Build System

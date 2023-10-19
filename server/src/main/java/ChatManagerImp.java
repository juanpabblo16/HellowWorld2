import java.util.ArrayList;
import java.util.List;
import com.zeroc.Ice.Current;
import Demo.CallbackPrx;

public class ChatManagerImp implements Demo.ChatManager {

    // Lists to store messages and client managers
    private List<String> messages;
    private List<ClientManager> clientManager;

    // Variables to track statistics
    private int totalRequests = 0;
    private int successfulResponses = 0;
    private int unprocessedRequests = 0;
    private int missingResponses = 0;

    // Constructor initializes the lists
    ChatManagerImp() {
        messages = new ArrayList<>();
        clientManager = new ArrayList<>();
    }

    @Override
    public void subscribe(CallbackPrx callback, String hostname, Current current) {
        System.out.println("Subscribe");
        
        // Check if the client is already subscribed
        if (!checkIfSubscribed(hostname)) {
            clientManager.add(new ClientManager(callback, hostname));
        }
    }

    // Check if a client is already subscribed
    public boolean checkIfSubscribed(String hostname) {
        for (ClientManager client : clientManager) {
            if (client.getId().equals(hostname)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String[] getState(Current current) {
        System.out.println("GetState");
        // Get the current state of messages and return them
        String[] state = new String[messages.size()];

        for (int i = 0; i < state.length; i++) {
            state[i] = messages.get(i);
        }
        return state;
    }

    @Override
    public void sendMessage(String msg, String hostname, Current current) {
        totalRequests++;

        new Thread(() -> {
            CallbackPrx callbackPrx = getCallbackPrx(hostname);
            System.out.println("New message: " + msg);

            // Process the incoming message
            comand(msg, callbackPrx);

        }).start();
    }

    // Process incoming messages
    public void comand(String msg, CallbackPrx callbackPrx) {
        String username = System.getProperty("user.name");
        String hostnamee = "localhost";

        if (msg.contains("BC")) {
            // Broadcast the message to all clients
            broadCast(msg.split("BC ")[1]);
            successfulResponses++;
            showStatistics();
        } else if (msg.contains("list clients")) {
            // List connected clients
            getClients(callbackPrx);
            successfulResponses++;
            showStatistics();
        } else if (msg.contains("to ") && msg.contains(":")) {
            // Send a message to a specific client
            String hostname = msg.split("to ")[1].split(":")[0];
            String message = msg.split("to ")[1].split(":")[1];
            if (!sendTo(hostname, message.trim())) {
                callbackPrx.printMsg("Client " + hostname + " not found.");
            } else {
                callbackPrx.printMsg("Message sent to " + hostname);
            }
            sendTo(hostname, message.trim());
            successfulResponses++;
            showStatistics();
        } else if (msg.contains("listifs")) {
            // List logical interfaces on the server
            String result = listInterfaces(username, hostnamee);
            callbackPrx.printMsg(result);
            successfulResponses++;
            showStatistics();
        } else if (msg.contains("listports")) {
            // List open ports on a specified IP address
            String ipAddress = msg.substring("listports".length()).trim();
            String result = listOpenPorts(username, hostnamee, ipAddress);
            successfulResponses++;
            callbackPrx.printMsg(result);
            showStatistics();
        } else if (msg.startsWith("!")) {
            // Execute a system command
            String command = msg.substring(1).trim();
            String result = executeCommand(username, hostnamee, command);
            callbackPrx.printMsg(result);
            successfulResponses++;
            showStatistics();
        } else if (msg.contains("QA")) {
            // Handle QA (Quality Assurance) requests
            successfulResponses++;
            showStatistics();
        } else {
            try {
                int number = Integer.parseInt(msg);
                if (number > 0) {
                    // Calculate and print prime factors
                    String result = printFactors(username, hostnamee, number);
                    callbackPrx.printMsg(result);
                    successfulResponses++;
                    showStatistics();
                } else {
                    callbackPrx.printMsg("Please enter a positive integer.");
                    unprocessedRequests++;
                }
            } catch (NumberFormatException e) {
                callbackPrx.printMsg("Invalid input. Please enter a correct input");
                unprocessedRequests++;
            }
        }
    }

    // List logical network interfaces on the server
    private static String listInterfaces(String username, String hostname) {
        StringBuilder interfaces = new StringBuilder("List of logical interfaces for " + username + "@" + hostname + ": ");

        try {
            java.util.Enumeration<java.net.NetworkInterface> networkInterfaces = java.net.NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                java.net.NetworkInterface networkInterface = networkInterfaces.nextElement();
                interfaces.append(networkInterface.getName()).append(", ");
            }
        } catch (java.net.SocketException e) {
            interfaces.append("Error obtaining interfaces: ").append(e.getMessage());
            e.printStackTrace();
        }

        String result = interfaces.toString();
        System.out.println(result); // Print on the server's console
        return result;
    }

    // List open ports for a specified IP address
    private static String listOpenPorts(String username, String hostname, String ipAddress) {
        StringBuilder openPorts = new StringBuilder("Open ports for " + ipAddress + " on " + username + "@" + hostname + ": ");

        try {
            java.net.InetAddress inetAddress = java.net.InetAddress.getByName(ipAddress);
            for (int port = 1; port <= 65535; port++) {
                try (java.net.Socket socket = new java.net.Socket()) {
                    socket.connect(new java.net.InetSocketAddress(inetAddress, port), 1000); // Connection attempt with a 1-second timeout
                    openPorts.append(port).append(", ");
                } catch (java.io.IOException ex) {
                    // Port is not open
                }
            }
        } catch (java.net.UnknownHostException e) {
            openPorts.append("Invalid IP address.");
        }

        String result = openPorts.toString();
        System.out.println(result); // Print on the server's console
        return result;
    }

    // Execute a system command and return the result
    private static String executeCommand(String username, String hostname, String command) {
        StringBuilder result = new StringBuilder("Result of executing the command on " + username + "@" + hostname + ": ");

        try {
            Process process = Runtime.getRuntime().exec(command);
            java.io.InputStream inputStream = process.getInputStream();
            java.util.Scanner scanner = new java.util.Scanner(inputStream).useDelimiter("\\A");
            if (scanner.hasNext()) {
                result.append(scanner.next());
            }
        } catch (java.io.IOException e) {
            result.append("Error executing the command.");
        }

        String output = result.toString();
        System.out.println(output); // Print on the server's console
        return output;
    }

    // Calculate and return prime factors of a number
    private static String printFactors(String username, String hostname, int number) {
        // Placeholder implementation to calculate prime factors
        StringBuilder factors = new StringBuilder("Prime factors of " + number + " on " + username + "@" + hostname + ": ");
        for (int i = 2; i <= number; i++) {
            while (number % i == 0) {
                factors.append(i).append(" ");
                number /= i;
            }
        }
        if (number > 1) {
            factors.append(number);
        }
        return factors.toString();
    }

    // Broadcast a message to all connected clients
    public void broadCast(String msg) {
        for (ClientManager client : clientManager) {
            client.getCallbackPrx().printMsg(msg);
        }
    }

    // Get a list of connected client IDs
    public void getClients(CallbackPrx callbackPrx) {
        String clientList = "";
        for (ClientManager client : clientManager) {
            clientList += client.getId() + " ";
        }
        callbackPrx.printMsg(clientList);
    }

    // Send a message to a specific client
    public boolean sendTo(String hostname, String msg) {
        boolean found = false;
        for (ClientManager client : clientManager) {
            if (client.getId().equals(hostname) && !found) {
                client.getCallbackPrx().printMsg(msg);
                found = true;
            }
        }
        if (!found) {
            missingResponses++;
        }
        return found;
    }

    // Method to show statistics
    public void showStatistics() {
        System.out.println("Success Rate: " + ((double) successfulResponses / totalRequests) * 100 + "%");
        System.out.println("Unprocessed Rate: " + ((double) unprocessedRequests / totalRequests) * 100 + "%");
        System.out.println("Missing Rate: " + ((double) missingResponses / totalRequests) * 100 + "%");
    }

    // Get the callback proxy for a given hostname
    public CallbackPrx getCallbackPrx(String hostname) {
        for (ClientManager client : clientManager) {
            if (client.getId().equals(hostname)) {
                System.out.println("Callback found for " + hostname + "");
                return client.getCallbackPrx();
            }
        }
        System.out.println("Callback not found for " + hostname + "");
        return null;
    }
}

import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        int requestsSent = 0;
        int requestsProcessed = 0;

        while (!exit) {
            System.out.print("Enter a message (type 'exit' to quit): ");
            String message = scanner.nextLine();

            if (message.equalsIgnoreCase("exit")) {
                exit = true;
            } else {
                requestsSent++;
                long startTime = System.currentTimeMillis();

                String result = processMessage(message);
                long endTime = System.currentTimeMillis();
                long responseTime = endTime - startTime;

                
                System.out.println("Server response: " + result);
                System.out.println("Response time client: " + responseTime + " ms");

                if (!result.equals("Unprocessed")) {
                    requestsProcessed++;
                }

                double unprocessRate = (double) (requestsSent - requestsProcessed) / requestsSent * 100;
                System.out.println("Unprocess rate: " + unprocessRate + "%");
                double scr = requestsProcessed / requestsSent;
                System.out.println("Succes rate: "+scr+"%");
                double missingRate  = requestsSent / requestsProcessed;
                System.out.println("Mising rate: "+missingRate+"%");
            }
        }
    }

    private static String processMessage(String message) {
        String username = System.getProperty("user.name");
        String hostname = "localhost"; // Placeholder for hostname retrieval
        
        if (message.toLowerCase().startsWith("listifs")) {
            return listInterfaces(username, hostname);
        } else if (message.toLowerCase().startsWith("listports")) {
            String ipAddress = message.substring("listports".length()).trim();
            return listOpenPorts(username, hostname, ipAddress);
        } else if (message.startsWith("!")) {
            String command = message.substring(1).trim();
            return executeCommand(username, hostname, command);
        } else {
            try {
                int number = Integer.parseInt(message);
                if (number > 0) {
                    return printFactors(username, hostname, number);
                } else {
                    return "Please enter a positive integer.";
                }
            } catch (NumberFormatException e) {
                return "Invalid input. Please enter a positive integer, 'listifs', 'listports', '!', or 'exit'.";
            }
        }
    }
    
    private static String listInterfaces(String username, String hostname) {
        return "List of logical interfaces for " + username + "@" + hostname;
    }
    
    private static String listOpenPorts(String username, String hostname, String ipAddress) {
        return "Open ports for " + ipAddress + " on " + username + "@" + hostname;
    }
    
    private static String executeCommand(String username, String hostname, String command) {
        // Placeholder implementation for executing the command
        return "Command executed on " + username + "@" + hostname + ": " + command;
    }
    
    private static String printFactors(String username, String hostname, int number) {
        // Placeholder implementation to calculate prime factors
        StringBuilder factors = new StringBuilder("Prime factors of " + number + " on " + username + "@" + hostname + ": ");
        for (int i = 2; i <= number; i++) {
            while (number % i == 0) {
                factors.append(i).append(" ");
                number /= i;
            }
        }
        return factors.toString();
    }
}

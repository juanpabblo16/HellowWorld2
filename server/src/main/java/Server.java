import java.io.*;

public class Server {
    public static void main(String[] args) {
        try (com.zeroc.Ice.Communicator communicator = com.zeroc.Ice.Util.initialize(args, "config.server")) {
            // Create an object adapter to handle incoming requests.
            com.zeroc.Ice.ObjectAdapter adapter = communicator.createObjectAdapter("Service");

            // Create an instance of ChatManagerImp to handle client interactions.
            ChatManagerImp chatManagerImp = new ChatManagerImp();

            // Add the ChatManagerImp object to the object adapter with a specific identity.
            adapter.add(chatManagerImp, com.zeroc.Ice.Util.stringToIdentity("ChatManager"));

            // Activate the object adapter to start handling requests.
            adapter.activate();

            // Wait for the communicator to be shut down (e.g., by user action).
            communicator.waitForShutdown();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

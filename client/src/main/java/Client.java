import java.net.Inet4Address;
import java.util.Scanner;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.ObjectPrx;
import com.zeroc.Ice.Util;

import Demo.CallbackPrx;
import Demo.ChatManagerPrx;

public class Client {
    public static void main(String[] args) {
        try (com.zeroc.Ice.Communicator communicator = com.zeroc.Ice.Util.initialize(args, "config.client")) {
            // Get a reference to the ChatManagerPrx object from the server.
            Demo.ChatManagerPrx chatManagerPrx = Demo.ChatManagerPrx
                    .checkedCast(communicator.propertyToProxy("ChatManager.Proxy"));

            try {
                // Create an instance of CallBackImp and set up an object adapter for it.
                CallBackImp callbackImp = new CallBackImp(chatManagerPrx);
                ObjectAdapter adapter = communicator.createObjectAdapter("Callback");
                ObjectPrx objectPrx = adapter.add(callbackImp, Util.stringToIdentity("Callback"));
                adapter.activate();

                // Get the client's hostname.
                String hostname = Inet4Address.getLocalHost().getHostName();

                // Get a reference to the CallbackPrx proxy.
                CallbackPrx prx = CallbackPrx.uncheckedCast(objectPrx);

                // Subscribe to the server's service.
                chatManagerPrx.subscribe(prx, hostname);

                // Wait for user input.
                System.out.println("Please enter a number or keyword");
                Scanner sc = new Scanner(System.in);
                String x = sc.nextLine();

                while (!x.equals("exit")) {
                    long startTime = System.currentTimeMillis(); // Record the start time

                    // Send the message to the server.
                    chatManagerPrx.sendMessage(x, hostname);

                    long endTime = System.currentTimeMillis(); // Record the end time
                    long responseTime = endTime - startTime; // Calculate the response time

                    // Print the server's response time.
                    System.out.println("Server Response Time: " + responseTime + " ms");

                    x = sc.nextLine();
                    System.out.println("_____________________________________________________________________");
                }

                sc.close();
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}

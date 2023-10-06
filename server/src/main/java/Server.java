import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Object;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Util;

import java.io.*;
import java.util.List;
import java.util.concurrent.*;

public class Server {
    private static int requestsReceived = 0;

    public static void main(String[] args) {
        List<String> extraArgs = new java.util.ArrayList<String>();

        try (Communicator communicator = Util.initialize(args, "config.server", extraArgs)) {
            if (!extraArgs.isEmpty()) {
                System.err.println("Too many arguments");
                for (String v : extraArgs) {
                    System.out.println(v);
                }
            }

            // Get the object adapter by name (must match the name in the config)
            ObjectAdapter adapter = communicator.createObjectAdapter("Printer");

            // Create a servant (object) and add it to the adapter
            Object object = new PrinterI();
            adapter.add((Object) object, Util.stringToIdentity("SimplePrinter"));

            // Activate the adapter to start listening for incoming connections
            adapter.activate();

            System.out.println("Server is running...");

            communicator.waitForShutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void handleClientRequest(com.zeroc.Ice.Communicator ic) {
        try {
            // Handle the client request here
            Demo.PrinterPrx printer = Demo.PrinterPrx.checkedCast(ic.stringToProxy("SimplePrinter:tcp -p 9099"));
            if (printer != null) {
                String message = "Response from Server to Client " + Thread.currentThread().getId();
                printer.printString(message);
            }
            ic.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

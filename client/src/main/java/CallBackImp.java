import java.util.Arrays; // Import the Arrays class for array manipulation
import com.zeroc.Ice.Current; // Import Ice's Current class for current request information
import Demo.ChatManagerPrx; // Import the ChatManagerPrx from the Demo package

public class CallBackImp implements Demo.Callback {
    private ChatManagerPrx server; // Reference to the ChatManagerPrx for communication with the server

    CallBackImp(ChatManagerPrx m) {
        server = m; // Initialize the ChatManagerPrx reference
    }

    @Override
    public void printMsg(String result, Current current) {
        System.out.println(result); // Print the received message to the console
    }
}

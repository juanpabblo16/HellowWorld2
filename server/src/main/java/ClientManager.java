import Demo.Callback;
import Demo.CallbackPrx;

public class ClientManager {
    private CallbackPrx callbackPrx; // Reference to the CallbackPrx proxy
    private String id; // Unique identifier for the client

    public ClientManager(CallbackPrx callbackPrx, String id) {
        this.callbackPrx = callbackPrx; // Initialize the callback proxy
        this.id = id; // Set the unique identifier for the client
    }

    public CallbackPrx getCallbackPrx() {
        return callbackPrx; // Get the CallbackPrx proxy associated with this client
    }

    public void setCallbackPrx(CallbackPrx callbackPrx) {
        this.callbackPrx = callbackPrx; // Set the CallbackPrx proxy for this client
    }

    public String getId() {
        return id; // Get the unique identifier for this client
    }

    public void setId(String id) {
        this.id = id; // Set the unique identifier for this client
    }
}

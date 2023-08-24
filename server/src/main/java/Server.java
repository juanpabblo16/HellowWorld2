import java.io.*;


public class Server
{
    private static int requestsReceived = 0;
    public static void main(String[] args)
    {
        java.util.List<String> extraArgs = new java.util.ArrayList<String>();

        try(com.zeroc.Ice.Communicator communicator = com.zeroc.Ice.Util.initialize(args,"config.server",extraArgs))
        {
            if(!extraArgs.isEmpty())
            {
                System.err.println("too many arguments");
                for(String v:extraArgs){
                    System.out.println(v);
                }
            }
            com.zeroc.Ice.ObjectAdapter adapter = communicator.createObjectAdapter("Printer");
            com.zeroc.Ice.Object object = new PrinterI();
            adapter.add(object, com.zeroc.Ice.Util.stringToIdentity("SimplePrinter"));
            adapter.activate();
            communicator.waitForShutdown();
        }
    }

    public static void f(String m)
    {
        String str = null, output = "";
        long startTime = System.currentTimeMillis();
        InputStream s;
        BufferedReader r;

        try {
            Process p = Runtime.getRuntime().exec(m);

            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream())); 
            while ((str = br.readLine()) != null) 
            output += str + System.getProperty("line.separator"); 
            br.close(); 
        }
        catch(Exception ex) {
        }

        long endTime = System.currentTimeMillis();
        long responseTime = endTime - startTime;

        System.out.println("Response time server: " + responseTime + " ms");

        if (responseTime > 1000) { // Adjust this threshold as needed
            System.out.println("Unprocessed");
        } else {
            requestsReceived++;
            System.out.println("Requests received: " + requestsReceived);
        }
    }

}
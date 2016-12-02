import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.SocketException;


public class ChatClient {
	public static void main(String[] args) throws Exception {
        try (Socket socket = new Socket("cs380.codebank.xyz", 38002)) {
            OutputStream os = socket.getOutputStream();
            PrintStream out = new PrintStream(os);
            
            InputStreamReader userStreamReader = new InputStreamReader(System.in);
            BufferedReader userIn = new BufferedReader(userStreamReader);
            String input = "";

            Thread t = new Thread(new ThreadedListener(socket));
            t.start();
            
            String username;
            System.out.print("Enter Username > ");
            username = userIn.readLine();
            out.println(username);
            out.flush();
            
            System.out.println("Hello " + username + ", enter your messages below.");
            
            while(true) {
            	//System.out.print(username + " > ");
            	input = userIn.readLine();
            	
            	if (input.equals("exit")) break;
            	out.println(input);
                out.flush();
            }
            
            out.close();
            socket.close();
        }
	    catch (SocketException e)
	    {
	    	System.err.println("Error: Problem connecting to server");
	    }
	}
}

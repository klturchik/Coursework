
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public final class EchoClient {

    public static void main(String[] args) throws Exception {
        try (Socket socket = new Socket("localhost", 22222)) {
            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            System.out.println(br.readLine());
            
            OutputStream os = socket.getOutputStream();
            PrintStream out = new PrintStream(os);
            
            InputStreamReader userStreamReader = new InputStreamReader(System.in);
            BufferedReader userIn = new BufferedReader(userStreamReader);
            String input = "";
            
            while(true) {
            	System.out.print("Client > ");
            	input = userIn.readLine();
            	
            	if (input.equals("exit")) break;
            	out.println(input);
                out.flush();
                System.out.println("Server > " + br.readLine());
            }
            
            out.close();
            br.close();
            socket.close();
		}
	}
}
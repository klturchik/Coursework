
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;


public class ThreadedEchoHandler implements Runnable {
	Socket socket;
	OutputStream os;
	
	public ThreadedEchoHandler(Socket socket){
		this.socket = socket;
	}

	public void run() {
		try
		{  
			try
			{
				OutputStream os = socket.getOutputStream();
			    PrintStream out = new PrintStream(os);
			    
			    InputStream is = socket.getInputStream();
			    InputStreamReader isr = new InputStreamReader(is, "UTF-8");
			    BufferedReader in = new BufferedReader(isr);                  
			                   
			    String inputLine = "";
			    while ((inputLine = in.readLine()) != null) {
			    	//System.out.println("echoing: " + inputLine);
			    	out.println(inputLine);
			    } 
			}
		    finally
		    {
		    	System.err.println("Closing connection with client");
		    	socket.close();
		    }
		}
		catch (Exception e)
		{  
	        System.err.println(e);
		} 
	}  
}
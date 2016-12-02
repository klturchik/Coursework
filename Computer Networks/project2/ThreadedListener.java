import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;


public class ThreadedListener implements Runnable {
	Socket socket;
	
	public ThreadedListener (Socket socket){
		this.socket = socket;
	}

	public void run() {
		try
		{  
			try
			{		    
			    InputStream is = socket.getInputStream();
			    InputStreamReader isr = new InputStreamReader(is, "UTF-8");
			    BufferedReader in = new BufferedReader(isr);                  
			                   
			    String inputLine = "";
			    while ((inputLine = in.readLine()) != null) {
			    	System.out.println(inputLine);
			    } 
			}
		    finally
		    {
		    	System.err.println("Closing connection with server");
		    	socket.close();
		    }
		}
	    catch (SocketException e)
	    {
	    }
		catch (Exception e)
		{  
	        System.err.println(e);
		} 
	}  
}

import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public final class EchoServer {

    public static void main(String[] args) throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(22222)) {
            while (true) {
            	Socket socket = null;
                try {
                	socket = serverSocket.accept();
                			
                    String address = socket.getInetAddress().getHostAddress();
                    System.out.printf("Client connected: %s%n", address);
                    OutputStream os = socket.getOutputStream();
                    PrintStream out = new PrintStream(os);
                    out.printf("Hi %s, thanks for connecting!%n", address);
                }        
                catch (SocketException e) 
                {
                	System.err.println("lllll");
                }
                
                Thread t = new Thread(new ThreadedEchoHandler(socket));
                t.start();
            }       
        }
    }
}
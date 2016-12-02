import java.io.*;
import java.net.*;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public final class Exercise2Client {

	 public static void main(String[] args) throws Exception {
	        try (Socket socket = new Socket("cs380.codebank.xyz", 38101)) {
	            InputStream is = socket.getInputStream();
	            BufferedInputStream bis= new BufferedInputStream(is);
	            DataInputStream din = new DataInputStream(bis);
	            
	            OutputStream os = socket.getOutputStream();
	            //PrintStream out = new PrintStream(os);
	            
	            System.out.println("Connected to server.");
	            
	            byte bytes[] = new byte[100];
	            int byte1;
	            int byte2;
	            int num;
	            
	            System.out.println("Received bytes:");
	            
	            for(int x = 1; x <= 100; x++){
	            	byte1 = din.read();
	            	byte2 = din.read();
	            	
	            	//combine two bytes received to form a new byte
	            	num = byte1*0x10 + byte2;
	            	bytes[x-1] = (byte) num;
	            	
	            	System.out.printf(String.format("%02X",num) + " ");
	            	if ((x%10)==0 && x!=0)
	            		System.out.println();
	            }
	            
	            //generate CRC32 error code for the byte array
	            Checksum checksum = new CRC32();
	            checksum.update(bytes, 0, bytes.length);
	            long tmp = checksum.getValue();
	            
	            System.out.println("Generated CRC32: " + String.format("%08X",tmp));
	            
	            //send CRC code to server as a sequence of four bytes
	            byte[] crcCode = new byte[] {
	            		(byte)((tmp >> 24) & 0xff),
	            		(byte)((tmp >> 16) & 0xff),
	            		(byte)((tmp >> 8) & 0xff),
	            		(byte)((tmp) & 0xff),
	            };
	            
	            byte response; //response from server (1 = correct, 0 = wrong)
	            
	            os.write(crcCode);            
	            response = (byte) din.read();
	            
	            if (response == 1) 
	            	System.out.println("Response good.");
	            else
	            	System.out.println("Response incorrect");
	            
	            
	            os.close();
	            din.close();
	            socket.close();
	            
	            System.out.println("Disconnected from server.");
	            
            } catch (IOException e){
            	System.out.println("Error: Unable to connect to server.");
            	System.out.println(e);
            }
	}
}

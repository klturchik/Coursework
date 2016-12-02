import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;


public class Ipv4Client {

	public static void main(String[] args) throws Exception {
	        try (Socket socket = new Socket("cs380.codebank.xyz", 38003)) {
			    InputStream is = socket.getInputStream();
			    InputStreamReader isr = new InputStreamReader(is, "UTF-8");
			    BufferedReader in = new BufferedReader(isr);   
	            
	            OutputStream os = socket.getOutputStream();
	            DataOutputStream dout = new DataOutputStream(os);
	            
	            System.out.println("Connected to server.");
	            
	            //INITIALIZE HEADER FIELDS
	            //Ipv4 = 4
	            byte version = 4;
	            
		   		//Length of header in 32-bit words
	            //5 * 32 bits = 20 bytes
		   		byte hLen = 5;

		   		//Do not implement
			    byte tos = 0x00;
			     
			    //Length of the header and data in bytes
			    //2 bytes of data in first packet
			    short length = 22;
	            
			    //Do not implement
			    short id = 0x0000;
			     
			    //Flags and offset
			    //Assume no fragmentation (010)
			    short frag = 0x4000;  
			     
			    //Assume TTL of 50
			    byte ttl = 50;
			     
			    //Assume TCP (6)
			    byte protocol = 6;
			     
			    //Checksum = 0 before calculation
			    short checksum = 0;
			    
			    //Source Address
			    InetAddress myIp =  InetAddress.getLocalHost();       
			    byte[] sourceAddr = myIp.getAddress();
			    
			    //Destination Address
				InetAddress hostIp = socket.getInetAddress();	 
				byte[] destAddr = hostIp.getAddress();
			     
				
	            //SEND 12 PACKETS
				byte packet[];
				int sum;
				
				for(int i=0; i<12; i++){
					//Create a new packet
					packet = new byte[length];
					
					packet[0] = (byte) ((version << 4) + hLen);
				    packet[1] = tos;
				    
				    packet[2] = (byte)((length >> 8) & 0xFF);
				    packet[3] = (byte)(length & 0xFF);
				    
				    packet[4] = (byte)((id >> 8) & 0xFF);
				    packet[5] = (byte)(id & 0xFF);
				    
				    packet[6] = (byte)((frag >> 8) & 0xFF);
				    packet[7] = (byte)(frag & 0xFF);
				    
				    packet[8] = ttl;

				    packet[9] = protocol;
				     
				    packet[10] = (byte)((checksum >> 8) & 0xFF);
				    packet[11] = (byte)(checksum & 0xFF);
				    
				    packet[12] = sourceAddr[0];
				    packet[13] = sourceAddr[1];
				    packet[14] = sourceAddr[2];
				    packet[15] = sourceAddr[3];
				     
				    packet[16] = destAddr[0];
				    packet[17] = destAddr[1];
				    packet[18] = destAddr[2];
				    packet[19] = destAddr[3];
				     
				    //Fill data with zeros
				    for(int j=20; j<packet.length; j++){
				    	packet[j] = 0;
				    }

		            //Checksum Algorithm
					sum = 0;
				    for(int j=0; j<hLen*4; j+=2){
				    	sum += (((packet[j]) << 8) & 0xFF00) | ((packet[j+1]) & 0xFF);

				    	//1's complement carry bit correction
				    	if((sum & 0xFFFF0000) > 0){
				    		sum &= 0xFFFF;
				    		sum += 0x0001;
				    	}
				    }
				    
				    checksum = (short) (~sum & 0xFFFF);
				    
				    packet[10] = (byte)((checksum >> 8) & 0xFF);
				    packet[11] = (byte)(checksum & 0xFF);
				    
		            //Display packet's header fields in hex and data length
				    System.out.print("Packet " + (i+1) + ": ");
				    for(int j = 0; j<20; j++){
						System.out.print(Integer.toHexString(packet[j] & 0xff) + " ");
			     	}
				    System.out.println();
				    System.out.println("data length: " + String.format("%.0f", Math.pow(2, i+1)));
				    
		            //Write to server & receive response
				    dout.write(packet);	
			         
				    String response = in.readLine();
				    System.out.println(response);
				    System.out.println();
				    
				    if(response.equals("good")){
				    	//Data size is doubled for each packet sent
				    	length = (short) (20 + Math.pow(2, i+2));
				    	checksum = 0;
				    } 
				    else break;
				}
				
				System.out.println("Closing connection with server");
				in.close();
	            dout.close();
	            socket.close();
	            
         } catch (IOException e){
        	 System.out.println("Error: Unable to connect to server.");
        	 System.out.println(e);
         }
	        
	}
}

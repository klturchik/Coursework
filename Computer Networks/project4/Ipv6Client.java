import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;


public class Ipv6Client {

	public static void main(String[] args) throws Exception {
		try (Socket socket = new Socket("cs380.codebank.xyz", 38004)) {
			InputStream is = socket.getInputStream();
            BufferedInputStream bis= new BufferedInputStream(is);
            DataInputStream din = new DataInputStream(bis); 
            
			OutputStream os = socket.getOutputStream();
			DataOutputStream dout = new DataOutputStream(os);
	            
			System.out.println("Connected to server.");
	            
			//INITIALIZE HEADER FIELDS
			//Ipv6 = 6
			byte version = 6;  //0-4
	            
			//Traffic Class
			//Do not implement
			byte traffic = 0x00;   //5-12

			//Flow Label
			//Do not implement
			int flow = 0x00;	//13-32
			     
			//Payload Length
			//length of data field in bytes
			short length = 2;
			
			//Next Header
			//UDP = 17
			byte next = 17;
	            
			//Hop Limit
			byte hop = 20;
			     
			//Source Address
			InetAddress myIp =  InetAddress.getLocalHost();       
			byte[] sourceAddr = myIp.getAddress();
			    
			//Destination Address
			InetAddress hostIp = socket.getInetAddress();	 
			byte[] destAddr = hostIp.getAddress();
			
			//SEND 12 PACKETS
			byte packet[];
				
			for(int i=0; i<12; i++){
				//Create a new packet
				packet = new byte[40+length];
					
				packet[0] = (byte) ((version << 4) + ((traffic >> 4) & 0xF));
				packet[1] = (byte) ((traffic & 0xF) + (flow >> 16) & 0xFFFF);
				    
				packet[2] = (byte)((flow >> 8) & 0xFFFF);
				packet[3] = (byte)(flow & 0xFFFF);
				    
				packet[4] = (byte)((length >> 8) & 0xFF);
				packet[5] = (byte)(length & 0xFFFF);
				    
				packet[6] = next;
				packet[7] = hop;
				  
				for(int j=8; j<18; j++){
					packet[j] = 0x00;
				}

				packet[18] = (byte) 0xFF;
				packet[19] = (byte) 0xFF;

				packet[20] = sourceAddr[0];
				packet[21] = sourceAddr[1];
				packet[22] = sourceAddr[2];
				packet[23] = sourceAddr[3];
				
				for(int j=24; j<34; j++){
					packet[j] = 0x00;
				}
				
				packet[34] = (byte) 0xFF;
				packet[35] = (byte) 0xFF;
				
				packet[36] = destAddr[0];
				packet[37] = destAddr[1];
				packet[38] = destAddr[2];
				packet[39] = destAddr[3];

				for(int j=40; j<packet.length; j++){
					packet[j] = 0;
				}
				    
				//Display packet's header fields in hex and data length
				System.out.print("Packet " + (i+1) + ": ");
				for(int j = 0; j<40; j++){
					System.out.printf(String.format("%02X", packet[j] & 0xFF) + " ");
				}
				System.out.println();
				System.out.println("data length: " + length + " bytes");
				
				//Write to server & receive response
				dout.write(packet);
				
				int response = (din.read() << 24);
				response += (din.read() << 16);
				response += (din.read() << 8);
				response += din.read();
			
				System.out.print("response: ");
				System.out.printf(String.format("%02X", response ) + "\n\n");
				
				if(response == 0xCAFEBABE){
					length *= 2;
				}
				else break;
			}

			System.out.println("Closing connection with server");
			din.close();
			dout.close();
			socket.close();
	            
		} catch (IOException e){
			System.out.println("Error: Unable to connect to server.");
			System.out.println(e);
		}        
	}
}

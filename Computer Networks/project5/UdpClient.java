import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Random;


public class UdpClient {
    
	public static void main(String[] args) throws Exception {
	        try (Socket socket = new Socket("cs380.codebank.xyz", 38005)) {
	        	InputStream is = socket.getInputStream();
	            BufferedInputStream bis= new BufferedInputStream(is);
	            DataInputStream din = new DataInputStream(bis); 
	            
				OutputStream os = socket.getOutputStream();
				DataOutputStream dout = new DataOutputStream(os);
	            
	            System.out.println("Connected to server.");
	            
	            //*****************************
	            //INITIALIZE IPV4 HEADER FIELDS
	            //*****************************
	            //Ipv4 = 4
	            byte version = 4;
	            
		   		//Length of header in 32-bit words
	            //5 * 32 bits = 20 bytes
		   		byte hLen = 5;

		   		//Do not implement
			    byte tos = 0x00;
			     
			    //Length of the header and data in bytes
			    //4 bytes of data in handshake
			    short length = 24;
	            
			    //Do not implement
			    short id = 0x0000;
			     
			    //Flags and offset
			    //Assume no fragmentation (010)
			    short frag = 0x4000;  
			     
			    //Assume TTL of 50
			    byte ttl = 50;
			     
			    //TCP(6) UDP(17)
			    byte protocol = 17;
			     
			    //Checksum = 0 before calculation
			    short ipv4Checksum = 0;
			    
			    //Source Address
			    InetAddress myIp =  InetAddress.getLocalHost();       
			    byte[] sourceAddr = myIp.getAddress();
			    
			    //Destination Address
				InetAddress hostIp = socket.getInetAddress();	 
				byte[] destAddr = hostIp.getAddress();
			     
				byte[] packet = InitializeIpv4(version, hLen, tos, length, id, 
						frag, ttl, protocol, ipv4Checksum, sourceAddr, destAddr);
			    
			    ipv4Checksum = Ipv4Checksum(packet);
			    packet[10] = (byte)((ipv4Checksum >> 8) & 0xFF);
			    packet[11] = (byte)(ipv4Checksum & 0xFF);
			    
			    //Handshake Data
			    packet[20] = (byte) 0xDE;
			    packet[21] = (byte) 0xAD;
			    packet[22] = (byte) 0xBE;
			    packet[23] = (byte) 0xEF;
			    
			    
	            //Display packet's header fields in hex and data length
			    System.out.println("HANDSHAKE");
			    System.out.print("Ipv4 Header: ");
			    for(int j = 0; j<20; j++){
			    	System.out.printf(String.format("%02X", packet[j] & 0xFF) + " ");
		     	}
			    System.out.println();
			    
			    
	            //Write to server & receive response
			    dout.write(packet);	
		         
				int response = (din.read() << 24);
				response += (din.read() << 16);
				response += (din.read() << 8);
				response += din.read();
			
				System.out.print("Response: ");
				System.out.printf(String.format("%02X", response) + "\n\n");
			    
				int port = 0;
			    if(response == 0xCAFEBABE){
			    	port = ((din.read() << 8) | din.read());
			    } 

			    
			    //*****************************
	            //INITIALIZE UDP HEADER FIELDS	
			    //*****************************
			    Random rn = new Random();	
			    
			    
			    short srcPort = (short) (rn.nextInt(500) + 1);
			    short dstPort = (short) port;
			    
			    short udpLength = 10; //UDP(8) + 2 bytes initially
			    short udpChecksum = 0;	
			    
			    long startTime;
			    long endTime;
			    long rtt = 0;
			    //Send 12 Packets
			    for(int i=0; i<12; i++){
				    length = (short) (20 + udpLength); //Ipv4(20) + UDP(8) + data
				    ipv4Checksum = 0;
				    udpChecksum = 0;
				    
					packet = InitializeIpv4(version, hLen, tos, length, id, 
							frag, ttl, protocol, ipv4Checksum, sourceAddr, destAddr);	
				    
				    ipv4Checksum = Ipv4Checksum(packet);
				    packet[10] = (byte)((ipv4Checksum >> 8) & 0xFF);
				    packet[11] = (byte)(ipv4Checksum & 0xFF);
				    
		            //Display packet's header fields in hex and data length
				    System.out.println("PACKET" + (i+1) + ":");
				    System.out.print("Ipv4 Header: ");
				    for(int j = 0; j<20; j++){
				    	System.out.printf(String.format("%02X", packet[j] & 0xFF) + " ");
			     	}
				    System.out.println();
				    
				    
					//Randomize Data
				    for(int j=28; j<packet.length; j++){
				    	packet[j] = (byte) (rn.nextInt(256));
				    } 
				    
				    
					packet[20] = (byte)((srcPort >> 8) & 0xFF);
					packet[21] = (byte)(srcPort & 0xFF);
					packet[22] = (byte)((dstPort >> 8) & 0xFF);
					packet[23] = (byte)(dstPort & 0xFF);
					
					packet[24] = (byte)((udpLength >> 8) & 0xFF);
					packet[25] = (byte)(udpLength & 0xFF);
					
				    udpChecksum = UdpChecksum(sourceAddr, destAddr, protocol, udpLength, packet);
					packet[26] = (byte)((udpChecksum >> 8) & 0xFF);
					packet[27] = (byte)(udpChecksum & 0xFF);
				    
					//Display packet's header fields in hex and data length
					System.out.print("UDP Header: ");
					for(int j = 20; j<28; j++){
						System.out.printf(String.format("%02X", packet[j] & 0xFF) + " ");
					}
					System.out.println();
					System.out.println("Data Length: " + (udpLength-8) + " bytes");
					
					
		            //Write to server & receive response	
					startTime =  System.currentTimeMillis();
				    dout.write(packet);	
				    
					response = (din.read() << 24);
					response += (din.read() << 16);
					response += (din.read() << 8);
					response += din.read();
					
					endTime = System.currentTimeMillis();
					rtt += (endTime - startTime); 
	
					System.out.print("Response: ");
					System.out.printf(String.format("%02X", response) + "\n");
					System.out.println("RTT: " + (endTime - startTime) + "ms\n");
				    
				    if(response == 0xCAFEBABE){
				    	//Data size is doubled for each packet sent
				    	udpLength = (short) (8 + Math.pow(2, i+2));
				    }
				    
				    if(i == 11){
				    	System.out.println("Average RTT: " + (rtt/12) + "ms");
				    }
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
	
	public static byte[] InitializeIpv4(byte version, byte hLen, byte tos, 
			short length, short id, short frag, byte ttl, byte protocol, 
			short checksum, byte[] sourceAddr, byte[] destAddr) {			
        //SEND 12 PACKETS

		
		//Create a new packet
		byte packet[] = new byte[length];
		
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
	    
	    return packet;
    }
	
	public static short Ipv4Checksum(byte[] packet){
        //Checksum Algorithm
		int sum = 0;
	    for(int i=0; i<5*4; i+=2){
	    	sum += (((packet[i]) << 8) & 0xFF00) | ((packet[i+1]) & 0xFF);

	    	//1's complement carry bit correction
	    	while((sum & 0xFFFF0000) > 0){
	    		sum = (sum & 0xFFFF) + (sum >> 16);
	    	}
	    }
	    
	    short ipv4Checksum = (short) (~sum & 0xFFFF);
		return ipv4Checksum;
	}
	
	public static short UdpChecksum(byte[] sourceAddr, byte[] destAddr, byte protocol, short udpLength, byte[] packet){
        //Checksum Algorithm
		int sum = 0;
		
		sum += ((sourceAddr[0] << 8) & 0xFF00) | ((sourceAddr[1]) & 0xFF); 	
		sum += ((sourceAddr[2] << 8) & 0xFF00) | ((sourceAddr[3]) & 0xFF);
   	
		sum += ((destAddr[0] << 8) & 0xFF00) | ((destAddr[1]) & 0xFF);  	
		sum += ((destAddr[2] << 8) & 0xFF00) | ((destAddr[3]) & 0xFF);
				
	    sum += 0x00;
	    sum += protocol;    
    	
	    sum += udpLength;

    	for(int i=20; i<packet.length; i+=2){
    		sum += (((packet[i]) << 8) & 0xFF00) | ((packet[i+1]) & 0xFF);
    	}
    	
    	//1's complement carry bit correction
    	while((sum & 0xFFFF0000) > 0){
    		sum = (sum & 0xFFFF) + (sum >> 16);
    	}
    	
	    short udpChecksum = (short) (~sum & 0xFFFF);
		return udpChecksum;
	}
}
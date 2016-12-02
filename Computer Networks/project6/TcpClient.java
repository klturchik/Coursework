import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Random;


public class TcpClient {
    
	public static void main(String[] args) throws Exception {
		do{
	        try (Socket socket = new Socket("cs380.codebank.xyz", 38006)) {
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
			    short length = 40; //Ipv4(20) + TCP(20)
	            
			    //Do not implement
			    short id = 0x0000;
			     
			    //Flags and offset
			    //Assume no fragmentation (010)
			    short frag = 0x4000;  
			     
			    //Assume TTL of 50
			    byte ttl = 50;
			     
			    //TCP(6) UDP(17)
			    byte protocol = 6;
			     
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
			    
			    
			    //*****************************
	            //INITIALIZE TCP HEADER FIELDS	
			    //*****************************
			    Random rn = new Random();	

			    //Randomize
			    short srcPort = (short) (rn.nextInt(1024));
			    short dstPort = (short) (rn.nextInt(1024));
			    
			    //Randomize
			    int seqNum = (rn.nextInt(2147483647));
			    int ackNum = 0;
			    
		   		//Length of header in 32-bit words
			    byte offset = 5;
			    
			    //URG ACK PSH RST SYN FIN
			    byte tcpFlags = 0b00000010; //SYN
			    
			    //Do not implement
			    short window = 0;
			    
			    //Checksum = 0 before calculation			    
			    short tcpChecksum = 0;
			    
			    //Do not implement
			    short urgPtr = 0;
			    
			    //Used only in checksum, includes data
			    short tcpLength = 20; 
			       
			    InitializeTcp(srcPort, dstPort, seqNum, ackNum, offset, tcpFlags, window, 
						tcpChecksum, urgPtr, packet);	
			    
			    tcpChecksum = TcpChecksum(sourceAddr, destAddr, protocol, tcpLength, packet);
				packet[36] = (byte)((tcpChecksum >> 8) & 0xFF);
				packet[37] = (byte)(tcpChecksum & 0xFF);
			    
			    
			    //*****************************
	            //HANDSHAKE
			    //*****************************
				//[1] Send SYN packet
	            //Display packet's header fields in hex and data length
			    System.out.println("SYN");
			    System.out.print("Ipv4 Header: ");
			    for(int j = 0; j<20; j++){
			    	System.out.printf(String.format("%02X", packet[j] & 0xFF) + " ");
		     	}
			    System.out.println();
			    System.out.print("TCP Header: ");
			    for(int j = 20; j<40; j++){
			    	System.out.printf(String.format("%02X", packet[j] & 0xFF) + " ");
		     	}
			    System.out.println();		    

			    
	            //[2] Write to server & receive response
			    dout.write(packet);	
		         
				int response = (din.read() << 24);
				response += (din.read() << 16);
				response += (din.read() << 8);
				response += din.read();
			
				System.out.print("Response: ");
				System.out.printf(String.format("%02X", response) + "\n\n");
			    
			    if(response != 0xCAFEBABE){
			    	break;
			    } 

	    
				//[3] Read TCP Header
				byte[] serverPacket = ReadTcp(din);
				
				//Are SYN and ACK flags set?
			    if(serverPacket[13] != 0b00010010){
			    	break;
			    } 
				
				int serverSeq = ((serverPacket[4] & 0xFF) << 24);
				serverSeq += ((serverPacket[5] & 0xFF) << 16);
				serverSeq += ((serverPacket[6] & 0xFF) << 8);
				serverSeq += serverPacket[7] & 0xFF;
				

				//[4] Send ACK Packet
			    seqNum = seqNum + 1;
			    tcpFlags = 0b00010000; //ACK
			    ackNum = serverSeq + 1;
			    tcpChecksum = 0;
			    
			    InitializeTcp(srcPort, dstPort, seqNum, ackNum, offset, tcpFlags, window, 
						tcpChecksum, urgPtr, packet);	
			    
			    tcpChecksum = TcpChecksum(sourceAddr, destAddr, protocol, tcpLength, packet);
				packet[36] = (byte)((tcpChecksum >> 8) & 0xFF);
				packet[37] = (byte)(tcpChecksum & 0xFF);
			    
			    
	            //Display packet's header fields in hex and data length
			    System.out.println("ACK");
			    System.out.print("Ipv4 Header: ");
			    for(int j = 0; j<20; j++){
			    	System.out.printf(String.format("%02X", packet[j] & 0xFF) + " ");
		     	}
			    System.out.println();
			    System.out.print("TCP Header: ");
			    for(int j = 20; j<40; j++){
			    	System.out.printf(String.format("%02X", packet[j] & 0xFF) + " ");
		     	}
			    System.out.println();			    
			    
			    
			    //[5] Write to server & receive response
			    dout.write(packet);	
		         
				response = (din.read() << 24);
				response += (din.read() << 16);
				response += (din.read() << 8);
				response += din.read();
			
				System.out.print("Response: ");
				System.out.printf(String.format("%02X", response) + "\n\n");
				
			    if(response != 0xCAFEBABE){
			    	break;
			    } 
			    
			    
			    //*****************************
	            //SEND DATA
			    //*****************************			
			    tcpFlags = 0b00000000;
				tcpLength = 22;
				
				//[6] Send 12 Packets
			    for(int i=0; i<12; i++){
				    length = (short) (20 + tcpLength); //Ipv4(20) + TCP(20) + data
				    ipv4Checksum = 0;
				    tcpChecksum = 0;
				    seqNum = (int) (seqNum + Math.pow(2, i));
				    			    
					packet = InitializeIpv4(version, hLen, tos, length, id, 
							frag, ttl, protocol, ipv4Checksum, sourceAddr, destAddr);
				    
				    ipv4Checksum = Ipv4Checksum(packet);
				    packet[10] = (byte)((ipv4Checksum >> 8) & 0xFF);
				    packet[11] = (byte)(ipv4Checksum & 0xFF);
	
				    InitializeTcp(srcPort, dstPort, seqNum, ackNum, offset, tcpFlags, window, 
							tcpChecksum, urgPtr, packet);	
				    
					//Randomize Data
				    for(int j=40; j<packet.length; j++){
				    	packet[j] = (byte) (rn.nextInt(256));
				    } 
				    
				    tcpChecksum = TcpChecksum(sourceAddr, destAddr, protocol, tcpLength, packet);
					packet[36] = (byte)((tcpChecksum >> 8) & 0xFF);
					packet[37] = (byte)(tcpChecksum & 0xFF);
			    
			
		            //Display packet's header fields in hex and data length
				    System.out.println("Packet " + (i+1));
				    System.out.print("Ipv4 Header: ");
				    for(int j = 0; j<20; j++){
				    	System.out.printf(String.format("%02X", packet[j] & 0xFF) + " ");
			     	}
				    System.out.println();
				    System.out.print("TCP Header: ");
				    for(int j = 20; j<40; j++){
				    	System.out.printf(String.format("%02X", packet[j] & 0xFF) + " ");
			     	}
				    System.out.println();
				    
		            //Write to server & receive response	
				    dout.write(packet);	
				    
					response = (din.read() << 24);
					response += (din.read() << 16);
					response += (din.read() << 8);
					response += din.read();
					
					System.out.print("Response: ");
					System.out.printf(String.format("%02X", response) + "\n\n");
				    if(response == 0xCAFEBABE){
				    	//Data size is doubled for each packet sent
				    	tcpLength = (short) (20 + Math.pow(2, i+2));
				    } else {
				    	break;
				    }
			    }
			    
			    
			    //*****************************
	            //TEARDOWN
			    //*****************************		
			    //[7] Send FIN packet
			    seqNum = seqNum + 1;
			    tcpFlags = 0b00000001; //FIN
				tcpLength = 20;
			    tcpChecksum = 0;
			    
			    length = (short) (20 + tcpLength); //Ipv4(20) + TCP(20) + data
			    ipv4Checksum = 0;
			      
				packet = InitializeIpv4(version, hLen, tos, length, id, 
						frag, ttl, protocol, ipv4Checksum, sourceAddr, destAddr);
			    
			    ipv4Checksum = Ipv4Checksum(packet);
			    packet[10] = (byte)((ipv4Checksum >> 8) & 0xFF);
			    packet[11] = (byte)(ipv4Checksum & 0xFF);

			    InitializeTcp(srcPort, dstPort, seqNum, ackNum, offset, tcpFlags, window, 
						tcpChecksum, urgPtr, packet);	
			    
			    tcpChecksum = TcpChecksum(sourceAddr, destAddr, protocol, tcpLength, packet);
				packet[36] = (byte)((tcpChecksum >> 8) & 0xFF);
				packet[37] = (byte)(tcpChecksum & 0xFF);
			    
				
				//Display packet's header fields in hex and data length
			    System.out.println("FIN");
			    System.out.print("Ipv4 Header: ");
			    for(int j = 0; j<20; j++){
			    	System.out.printf(String.format("%02X", packet[j] & 0xFF) + " ");
		     	}
			    System.out.println();
			    System.out.print("TCP Header: ");
			    for(int j = 20; j<40; j++){
			    	System.out.printf(String.format("%02X", packet[j] & 0xFF) + " ");
		     	}
			    System.out.println();
			    
	            //[8] Write to server & receive response	
			    dout.write(packet);	
			    
				response = (din.read() << 24);
				response += (din.read() << 16);
				response += (din.read() << 8);
				response += din.read();
				
				System.out.print("Response: ");
				System.out.printf(String.format("%02X", response) + "\n\n");
				
				
				//[9] Read TCP Header
				serverPacket = ReadTcp(din);
				
				//Is ACK flag set?
			    if(serverPacket[13] != 0b00010000){
			    	break;
			    } 
				
				//[10] Read TCP Header
				serverPacket = ReadTcp(din);
				
				//Is FIN flag set?
			    if(serverPacket[13] != 0b00000001){
			    	break;
			    } 
				
				
			    //[11] Send ACK packet
			    seqNum = seqNum + 1;
			    tcpFlags = 0b00010000; //ACK
			    tcpChecksum = 0;
			    ipv4Checksum = 0;
			    	    
				packet = InitializeIpv4(version, hLen, tos, length, id, 
						frag, ttl, protocol, ipv4Checksum, sourceAddr, destAddr);
			    
			    ipv4Checksum = Ipv4Checksum(packet);
			    packet[10] = (byte)((ipv4Checksum >> 8) & 0xFF);
			    packet[11] = (byte)(ipv4Checksum & 0xFF);

			    InitializeTcp(srcPort, dstPort, seqNum, ackNum, offset, tcpFlags, window, 
						tcpChecksum, urgPtr, packet);	
			    
			    tcpChecksum = TcpChecksum(sourceAddr, destAddr, protocol, tcpLength, packet);
				packet[36] = (byte)((tcpChecksum >> 8) & 0xFF);
				packet[37] = (byte)(tcpChecksum & 0xFF);
			    
				
				//Display packet's header fields in hex and data length
			    System.out.println("ACK");
			    System.out.print("Ipv4 Header: ");
			    for(int j = 0; j<20; j++){
			    	System.out.printf(String.format("%02X", packet[j] & 0xFF) + " ");
		     	}
			    System.out.println();
			    System.out.print("TCP Header: ");
			    for(int j = 20; j<40; j++){
			    	System.out.printf(String.format("%02X", packet[j] & 0xFF) + " ");
		     	}
			    System.out.println();
			    
	            //[12] Write to server & receive response	
			    dout.write(packet);	
			    
				response = (din.read() << 24);
				response += (din.read() << 16);
				response += (din.read() << 8);
				response += din.read();
				
				System.out.print("Response: ");
				System.out.printf(String.format("%02X", response) + "\n\n");

			    if(response != 0xCAFEBABE){
			    	break;
			    } 				
				
				din.close();
		        dout.close();
		        socket.close();
		        
	         } catch (Exception e){
	        	 System.out.println(e);
	         }
		}while(false);
		
		System.out.println("Closing connection with server");
    }	

	
	public static byte[] InitializeIpv4(byte version, byte hLen, byte tos, 
			short length, short id, short frag, byte ttl, byte protocol, 
			short checksum, byte[] sourceAddr, byte[] destAddr) {			
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
		
	public static void InitializeTcp(short srcPort, short dstPort, int seqNum, 
			int ackNum, byte offset, byte tcpFlags, short window, short tcpChecksum, 
			short urgPtr, byte[] packet) {	
		
		packet[20] = (byte)((srcPort >> 8) & 0xFF);
		packet[21] = (byte)(srcPort & 0xFF);
		packet[22] = (byte)((dstPort >> 8) & 0xFF);
		packet[23] = (byte)(dstPort & 0xFF);
		
		packet[24] = (byte)((seqNum >> 24) & 0xFF);
		packet[25] = (byte)((seqNum >> 16) & 0xFF);
		packet[26] = (byte)((seqNum >> 8) & 0xFF);
		packet[27] = (byte)(seqNum & 0xFF);
		
		packet[28] = (byte)((ackNum >> 24) & 0xFF);
		packet[29] = (byte)((ackNum >> 16) & 0xFF);
		packet[30] = (byte)((ackNum >> 8) & 0xFF);
		packet[31] = (byte)(ackNum & 0xFF);
		
		packet[32] = (byte)((offset << 4) & 0xFF);
		packet[33] = tcpFlags;
		
		packet[34] = (byte)((window >> 4) & 0xFF);
		packet[35] = (byte)(window & 0xFF);
		
		packet[36] = (byte)((tcpChecksum >> 8) & 0xFF);
		packet[37] = (byte)(tcpChecksum & 0xFF);
		
		packet[38] = (byte)((urgPtr >> 8) & 0xFF);
		packet[39] = (byte)(urgPtr & 0xFF);
	}
	
	public static short TcpChecksum(byte[] sourceAddr, byte[] destAddr, byte protocol, short tcpLength, byte[] packet){
        //Checksum Algorithm
		int sum = 0;
		
		sum += ((sourceAddr[0] << 8) & 0xFF00) | ((sourceAddr[1]) & 0xFF); 	
		sum += ((sourceAddr[2] << 8) & 0xFF00) | ((sourceAddr[3]) & 0xFF);
   	
		sum += ((destAddr[0] << 8) & 0xFF00) | ((destAddr[1]) & 0xFF);  	
		sum += ((destAddr[2] << 8) & 0xFF00) | ((destAddr[3]) & 0xFF);
				
	    sum += 0x00;
	    sum += protocol;    
    	
	    sum += tcpLength;

    	for(int i=20; i<packet.length; i+=2){
    		sum += (((packet[i]) << 8) & 0xFF00) | ((packet[i+1]) & 0xFF);
    	}
    	
    	//1's complement carry bit correction
    	while((sum & 0xFFFF0000) > 0){
    		sum = (sum & 0xFFFF) + (sum >> 16);
    	}
    	
	    short tcpChecksum = (short) (~sum & 0xFFFF);
		return tcpChecksum;
	}
	
	public static byte[] ReadTcp(DataInputStream din) throws IOException{
		byte serverPacket[] = new byte[20];

    	for(int i=0; i<serverPacket.length; i++){
    		serverPacket[i] = (byte) din.read();
    	}

		return serverPacket;
	}
}
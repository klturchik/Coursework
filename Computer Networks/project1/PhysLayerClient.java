import java.io.*;
import java.net.*;

public final class PhysLayerClient {

	 public static void main(String[] args) throws Exception {
	        try (Socket socket = new Socket("cs380.codebank.xyz", 38001)) {
	            InputStream is = socket.getInputStream();
	            BufferedInputStream bis= new BufferedInputStream(is);
	            DataInputStream din = new DataInputStream(bis);
	            
	            OutputStream os = socket.getOutputStream();
	            DataOutputStream dout = new DataOutputStream(os);
	            
	            System.out.println("Connected to server.");
	            
	            
	            int base = 0;  
	            int signal;
	            
	            //64-bit preamble
	            //each signal read represents one bit
	            for(int x = 0; x < 64; x++){
	            	signal = din.read();
	            	base += signal;
	            }
	            
	            //baseline is the average of the preamble signal 
	            base = base / 64;
            	System.out.println("Baseline established from preamble: " + base);
	            
            	
	            byte[] sequence = new byte[32];	//the decoded sequence
	            
	            String fiveB = "";		//5-Bit code sequence
	            int fourB = 0x0;		//4-Bit data
	            int num = 0;			//number represented by 8 Bits/1 Byte
	            
	            boolean prev = false;	//previous state of signal
	            
	            System.out.println("Received 32 bytes: ");
	            	         
	            //decode 32 bytes
	            for(int x = 0; x < 32; x++){
	            	num = 0;

	            	//1 byte = 2 sets of 4-Bit Data
	            	for(int y = 0; y < 2; y++){
		            	fiveB = "";	
		            	fourB = 0x0;
		            	
		            	//convert each 5 bit sequence to 4-Bit Data
		            	for(int z = 0; z < 5; z++){
		            		signal = din.read();
			            	
		            		/*if the signal changes from high to low
		            		or vice versa add 1 to the sequence*/
		            		//else add 0 to the sequence
			            	if(prev == true) //was high
			            		if(signal < base)
			            			fiveB += 1;
			            		else
			            			fiveB += 0;
			            	if(prev == false) //was low	
			            		if(signal >= base)
			            			fiveB += 1;
			            		else
			            			fiveB += 0;

			            	//track state of the signal
			            	if(signal >= base)
			            		prev = true; //now high
			            	else
			            		prev = false; //now low
		            	}

		            	//5-Bit to 4-Bit conversion
		            	switch(fiveB){
		            		case "11110": fourB = 0x0; 
		            			break;
		            		case "01001": fourB = 0x1; 
	            				break;
		            		case "10100": fourB = 0x2; 
	            				break;
		            		case "10101": fourB = 0x3; 
	            				break;
		            		case "01010": fourB = 0x4; 
	            				break;
		            		case "01011": fourB = 0x5; 
	            				break;
		            		case "01110": fourB = 0x6; 
	            				break;
		            		case "01111": fourB = 0x7; 
	            				break;
		            		case "10010": fourB = 0x8; 
	            				break;
		            		case "10011": fourB = 0x9; 
	            				break;
		            		case "10110": fourB = 0xA; 
	            				break;
		            		case "10111": fourB = 0xB; 
	            				break;
		            		case "11010": fourB = 0xC; 
	            				break;
		            		case "11011": fourB = 0xD; 
	            				break;
		            		case "11100": fourB = 0xE; 
	            				break;
		            		case "11101": fourB = 0xF; 
	            				break;
		            	}
	            		
		            	//shift first 4 digits
            			if(y == 0) 
            				fourB *= 0x10;
            			
		            	num += fourB;
	            	}
	            	
	            	//add 8-bit number to decoded sequence
	            	sequence[x] = (byte) num; 
	            	System.out.printf(String.format("%02X",num) + " ");
	            }

	            
	            byte response; //response from server (1 = correct, 0 = wrong)
	            
	            dout.write(sequence);	            
	            response = (byte) din.read();
	            
	            System.out.println();
	            if (response == 1) 
	            	System.out.println("Response good.");
	            else
	            	System.out.println("Response incorrect");
	            
	            dout.close();
	            din.close();
	            socket.close();
	            
	            System.out.println("Disconnected from server.");
	            
            } catch (IOException e){
            	System.out.println("Error: Unable to connect to server.");
            	System.out.println(e);
            }
	        
	}
}

import java.io.*;
import java.net.*;
import java.util.*;

public class TicTacToeClient{
    static ObjectOutputStream outputStream;
    static ObjectInputStream inputStream;
    
    static Scanner sc = new Scanner(System.in);
	private static String username;
    
    public static void main(String[] args){
        System.out.println("Connecting to server...");
        Socket socket;
        try{
            socket = new Socket("cs380.codebank.xyz", 38007);
            System.out.println("Connected\n");

            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            
            //Connect Message
            System.out.print("Enter Username > ");
            username = sc.nextLine();

            ConnectMessage connect = new ConnectMessage(username);
            outputStream.writeObject(connect);

            //Command Message
            System.out.println("Starting a new game...");
            outputStream.writeObject(new CommandMessage(CommandMessage.Command.NEW_GAME));

            while(true){
            	//Board Message
                System.out.println("Attempting to read an incoming object");
                Message gameBoard = (Message)inputStream.readObject();

                if(gameBoard instanceof BoardMessage){
                	switch (((BoardMessage)gameBoard).getStatus()){
                		case IN_PROGRESS:
                            printBoard(((BoardMessage)gameBoard).getBoard());
                            makeMove(); 
							break;
                		case PLAYER1_VICTORY:
                            printBoard(((BoardMessage)gameBoard).getBoard());
                            System.out.println(username + " Wins! =D");
                            System.out.println("Disconnected from server...");
                            System.exit(-1);
							break;
                		case PLAYER2_VICTORY:
                            printBoard(((BoardMessage)gameBoard).getBoard());
                            System.out.println("Server Wins! :C");
                            System.out.println("Disconnected from server...");
                            System.exit(-1);
							break;
                		case  STALEMATE:
                            printBoard(((BoardMessage)gameBoard).getBoard());
                            System.out.println("Stalemate!");
                            System.out.println("Disconnected from server...");
                            System.exit(-1);
							break;
						default:
							break;
                	}
                }
                else if(gameBoard instanceof ErrorMessage){
                    System.out.println("Error: " + ((ErrorMessage)gameBoard).getError());
                    System.exit(-1);
                }
                else{
                    System.out.println("Something went terribly wrong...");
                    System.exit(-1);
                }
            }
        }
        catch(Exception e){
            System.out.println("Exception: " + e.toString());
        }
    }


    static byte x,y;
    public static void makeMove(){
        System.out.println(username + "'s Turn");
        System.out.println("Make a move");
        System.out.print("Column: ");
        x = (byte) (sc.nextByte() - 1);
        System.out.print("Row: ");
        y = (byte) (sc.nextByte() - 1);
        
        try{
        	//Move Message
            outputStream.writeObject(new MoveMessage(y,x));
        }
        catch(Exception e){
            System.out.println("Exception: " + e.toString());
            System.exit(-1);
        }
    }

    public static void printBoard(byte[][] board){
    	System.out.println("\t   1  2  3");
        for(int i = 0; i < board.length; i++){
            System.out.print("\t" + (i+1));
            for(int j = 0; j < board[i].length; j++){
                switch(board[i][j]){
                    case 0:
                        System.out.print("  .");
                        break;
                    case 1:
                        System.out.print("  X");
                        break;
                    case 2:
                        System.out.print("  O");
                        break;
                    default:
                        System.out.print("-");
                }
            }
            System.out.println();
        }
        System.out.println();
    }
}
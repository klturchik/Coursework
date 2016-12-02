//
//	Name: Turchik, Kyle
//	Project: 3
//	Due: 6/5/2015
//	Course: cs-241-02-s15
//

import java.util.*;
import java.io.*;

public class Project3 {
	public static void main (String[] args) throws FileNotFoundException{		
		DiGraph myGraph = new DiGraph();
		
		// Read in data from city file
		File file = new File("city.dat");
		Scanner inFile = new Scanner(file);
		int vertex = 0;
		while(inFile.hasNext()){
			String label = inFile.nextLine();
			myGraph.setData(vertex, label);
			vertex++;
		}
		inFile.close();

		// Read in data from road file
		file = new File("road.dat");
		inFile = new Scanner(file);
		while(inFile.hasNext()){
			int source = inFile.nextInt()-1;
			int target = inFile.nextInt()-1;
			int distance = inFile.nextInt();
			myGraph.addEdge(source, target, distance);
		}
		inFile.close();
		
		// Get choice
		Scanner kb = new Scanner(System.in);
		while(true){
			//Prompt
			System.out.print("Command? ");
			char command = kb.next().charAt(0);
			kb.nextLine();
			
			// Query the city information by entering the city code
			if(command ==  'Q'){
				System.out.print("City code: ");
				String cityCode = kb.nextLine();
				
				System.out.println(myGraph.getLabel(cityCode));
			}
			// Find the minimum distance between two cities
			else if (command == 'D'){
				System.out.print("City codes: ");
				String inputLine = kb.nextLine();
				String[] codeArray = inputLine.split(" +");

				try{
				System.out.println(myGraph.dijkstra(codeArray[0], codeArray[1]));
				} catch (ArrayIndexOutOfBoundsException e) {
					System.out.println("ERROR: Invalid Input");
				}
			}
			// Insert a road by entering two city codes and distance
			else if (command == 'I'){
				System.out.print("City codes: ");
				String inputLine = kb.nextLine();
				String[] codeArray = inputLine.split(" +");
				
				try {
					System.out.println(myGraph.addEdge(codeArray[0], codeArray[1], Integer.parseInt(codeArray[2])));
				} catch (NumberFormatException e) {
					System.out.println("ERROR: " + codeArray[2] + " is not a valid distance");
				} catch (ArrayIndexOutOfBoundsException e) {
					System.out.println("ERROR: Invalid Input");
				}
				
			}
			// Remove an existing road by entering two city codes
			else if (command == 'R'){
				System.out.print("City codes: ");
				String inputLine = kb.nextLine();
				String[] codeArray = inputLine.split(" +");
				
				try {
				System.out.println(myGraph.removeEdge(codeArray[0], codeArray[1]));
				} catch (ArrayIndexOutOfBoundsException e) {
					System.out.println("ERROR: Invalid Input");
				}
			}
			// Display the list of possible commands
			else if (command == 'H'){
				System.out.println("   Q   Query the city information by entering the city code.");
				System.out.println("   D   Find the minimum distance between two cities.");
				System.out.println("   I   Insert a road by entering two city codes and distance."); 
				System.out.println("   R   Remove an existing road by entering two city codes.");
				System.out.println("   H   Display this message.");
				System.out.println("   E   Exit.");
			}
			// Exit
			else if (command == 'E'){
				System.out.println("Thank you for using dijkstra maps!");
				break;
			}
			// Display error message for invalid command
			else{
				System.out.println("ERROR: Invalid Command (Enter 'H' to see list of commands)");
			}
		}
		kb.close();
	}
}
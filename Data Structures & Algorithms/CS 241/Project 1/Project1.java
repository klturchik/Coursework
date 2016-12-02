//
//	Name: Turchik, Kyle
//	Project: 1
//	Due: 4/28/2015
//	Course: cs-241-02-s15
//

import java.util.Scanner;

public class Project1 {

	public static void main (String[] args){
		BST myTree = new BST();
		
		//Prompt
		System.out.println("Please enter the initial sequence of values:");
		
		//Get data
		Scanner kb = new Scanner(System.in);
		String inputLine;
		inputLine = kb.nextLine();
	
		String[] values = inputLine.split(" ");
		
		//Build the tree
		for(int i = 0; i < values.length; i++){
			myTree.add(Integer.parseInt(values[i]));
		}
		
		//Print 3 traversals
		System.out.print("Pre-order: ");
		myTree.preorderPrint();
		System.out.println();
		
		System.out.print("In-order: ");
		myTree.inorderPrint();
		System.out.println();
		
		System.out.print("Post-order: ");
		myTree.postorderPrint();
		System.out.println();
		
		//Get additional commands
		boolean quit = false;

		do {
			System.out.print("Command? ");
			inputLine = kb.nextLine();
			String[] commandLine = inputLine.split(" ");
		
			switch (commandLine[0]) {
			case "I":	//Insert a value
				if(!(myTree.exists(Integer.parseInt(commandLine[1])))){
					myTree.add(Integer.parseInt(commandLine[1]));
					myTree.inorderPrint();
					System.out.println();
				} else {
					System.out.println(commandLine[1] + " already exists, ignore.");
				}
				break;
			case "D":	//Delete a value
				if(myTree.exists(Integer.parseInt(commandLine[1]))){
					myTree.remove(Integer.parseInt(commandLine[1]));
					myTree.inorderPrint();
					System.out.println();
				} else {
					System.out.println(commandLine[1] + " doesn't exist!");
				}
				break;
			case "P":	//Find predecessor
				int predecessor = myTree.findPredecessor(Integer.parseInt(commandLine[1]));
				if(predecessor != -1)
					System.out.println(predecessor);
				else
					System.out.println(commandLine[1] + " has no predecessor!");
				break;
			case "S":	//Find successor
				int successor = myTree.findSuccessor(Integer.parseInt(commandLine[1]));
				if(successor != -1)
					System.out.println(successor);
				else
					System.out.println(commandLine[1] + " has no successor!");
				break;			
			case "E":	//Exit the program
				quit = true;
				break;
			case "H":	//Display help message
				System.out.println("I	Insert a value");
				System.out.println("D	Delete a value");
				System.out.println("P	Find predecessor");
				System.out.println("S	Find successor");
				System.out.println("E	Exit the program");
				System.out.println("H	Display this message");
				break;
			default:
				System.out.println("Invalid command!");
			}
		} while (quit != true);
		
		System.out.print("Thank you for using!");
		
		kb.close();
	}
}
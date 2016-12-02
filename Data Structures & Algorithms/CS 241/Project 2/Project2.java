//
//	Name: Turchik, Kyle
//	Project: 2
//	Due: 5/14/2015
//	Course: cs-241-02-s15
//

import java.util.*;

public class Project2 {
	public static void main (String[] args){
		Scanner kb = new Scanner(System.in);
		
		while(true){
			//Prompt
			System.out.println("===================================================");
			System.out.println();
			System.out.println("Please select how to test the program:");
			System.out.println("(1) 20 sets of 100 randomly generated integers"); 
			System.out.println("(2) Fixed integer values 1-100");
			System.out.println("(Other) Quit");
			System.out.print("Enter Choice: ");
			
			//Get choice
			int choice;
			choice = kb.nextInt();
			
			System.out.println();
			
			//TEST WITH RANDOMLY GENERATED INTEGERS 
			if(choice ==  1){
				int averageSwapsSeries = 0;
				int averageSwapsOptimal = 0;
			
				//Build trees from 20 different sets of integers
				for(int i = 0; i < 20; i++){
					Heap seriesTree = new Heap();
					Heap optimalTree = new Heap();
					
					//Create a list of valid numbers in range 1-100 and shuffle it, this prevents duplicates
					List<Integer> random = new ArrayList<>();
					for(int x = 1; x <= 100; x++){
						random.add(x);  // list contains 1-100
					}
					Collections.shuffle(random);	// list is now in random order

					
					//For each set of integers, build two trees using both series of insertions and the optimal method
					for(int j = 0; j < 100; j++){
						int num = (int)random.get(j);
						seriesTree.insert(num);
						optimalTree.insertArbitrary(num);
					}
					
					optimalTree.optimalSort();
					
					//Track the total number of swaps needed to construct the different trees
					averageSwapsSeries += seriesTree.getSwapCount();
					averageSwapsOptimal += optimalTree.getSwapCount();
				}
				
				//Print the average number of swaps needed to construct each tree over 20 sets
				System.out.println("Average swaps for series of insertions: " + averageSwapsSeries/20);
				System.out.println("Average swaps for optimal method: " + averageSwapsOptimal/20);
			
				System.out.println();
				System.out.println();
			}
			
			//TEST WITH FIXED INTEGER VALUES 1-100
			else if (choice == 2){
				Heap seriesTree = new Heap();
				Heap optimalTree = new Heap();
					
				//Build two trees using both series of insertions and the optimal method
				for(int i = 0; i < 100; i++){						
					seriesTree.insert(i+1);
					optimalTree.insertArbitrary(i+1);
				}
					
				optimalTree.optimalSort();
				
				//PRINT SERIES OF INSERTIONS TREE
				System.out.print("Heap built using series of insertions: ");
				seriesTree.print();
				//Print total number of swaps needed to construct the tree
				System.out.println("Number of swaps: " + seriesTree.getSwapCount());
				//Remove the root node from the tree 10 times and print the new tree
				seriesTree.remove(10);	
				System.out.print("Heap after 10 removals: ");
				seriesTree.print();
				
				System.out.println();
				
				//PRINT OPTIMAL TREE
				System.out.print("Heap built using optimal method: ");
				optimalTree.print();
				//Print total number of swaps needed to construct the tree
				System.out.println("Number of swaps: " + optimalTree.getSwapCount());
				//Remove the root node from the tree 10 times and print the new tree
				optimalTree.remove(10);
				System.out.print("Heap after 10 removals: ");
				optimalTree.print();
				
				System.out.println();
				System.out.println();
			}
			else{
				//Done
				System.out.print("Thank you for using!");
				break;
			}
		}
		kb.close();
	}
}
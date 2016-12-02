//
//	Name: Turchik, Kyle
//	Homework: 2
//	Due: 3/3/2015
//	Course: cs-240-02-w15
//
//	Description:
//	Implement the class Table from page 592 of the textbook.  Add 
//	code and methods to track the number of collisions, to return a
//	String containing statistics about the Table, and to put words 
//	from a file into the Table.  Print out statistics for the Table 
//	with capacities 1000, 2000, 4000, and 8000.
//

import java.io.IOException;
import java.util.StringTokenizer;
import java.io.*;
	
//HOMEWORK PART 1
public class Table<K, E> { 
	private int manyItems; 
	private Object[ ] keys; 
	private static Object[ ] data; 
	private boolean[ ] hasBeenUsed;
	
	//PART 2
	private int collisionCounter;
	
	public Table(int capacity) {
		if (capacity <= 0) 
			throw new IllegalArgumentException("Capacity is negative."); 
		keys = new Object[capacity]; 
		data = new Object[capacity]; 
		hasBeenUsed = new boolean[capacity];
		
		//PART 2
		collisionCounter = 0;
	}


	public boolean containsKey(K key) {
		return (findIndex(key) != -1);
	}
	
	private int findIndex(K key) {
	// Postcondition: If the specified key is found in the table, then the return 
	// value is the index of the specified key. Otherwise, the return value is -1. 
		int count = 0; 
		int i = hash(key); 
		
		while ((count < data.length) && (hasBeenUsed[i])) 
		{ 
			if (key.equals(keys[i])) 
				return i; 
			count++; 
			i = nextIndex(i); 
		}
		
		return -1; 
	} 
	
	@SuppressWarnings("unchecked") // See the warnings discussion on page 265 
	public E get(K key) {
		int index = findIndex(key); 
	
		if (index == -1) 
			return null; 
		else 
			return (E) data[index]; 
	} 
	
	private int hash(Object key) {
	// The return value is a valid index of the table’s arrays. The index is 
	// calculated as the remainder when the absolute value of the key’s 
	// hash code is divided by the size of the table’s arrays. 
		return Math.abs(key.hashCode( )) % data.length;
	}
	
	private int nextIndex(int i) {
	//The return value is normally i+1. But if i+1 is data.length, then the return value is zero 
	// instead. 
		if (i+1 == data.length) 
			return 0; 
		else 
			return i+1; 
	} 
	
	private E put(K key, E element) {
		int index = findIndex(key);
		E answer;
		
		if (index != -1)
		{	//The key is already in the table.
			answer = (E) data[index];
			data[index] = element;
			return answer;
		}
		else if (manyItems < data.length)
		{	//The key is not yet in this Table
			index = hash(key);
			if (keys[index] != null){
				//PART 2
				//Only count the initial collision
				//Not every transversal needed to find an open index
				collisionCounter++;
				
				while (keys[index] != null)
					index = nextIndex(index);
			}
			keys[index] = key;
			data[index] = element;
			hasBeenUsed[index] = true;
			manyItems++;
			return null;
		}
		else
		{	//The table is full
			throw new IllegalStateException("Table is full.");
		}
	}
	
	@SuppressWarnings("unchecked")
	private E remove(K key)  {
		int index = findIndex(key);
		E answer = null;
		
		if (index != -1)
		{
			answer = (E) data[index];
			keys[index] = null;
			data[index] = null;
			manyItems--;
		}
		
		return answer;
	}
	
	//PART 3
	public String toString() {
		//Print out collision rate as a percentage to the hundredth place
		double collisionRate = Math.round(((double)collisionCounter/(double)manyItems)*10000);
		String stats = ("Table-size: " + Integer.toString(data.length) + "\n" + //
				"Number of Keys Inserted: " + Integer.toString(manyItems) + "\n" + //
				"Number of Collisions: " + Integer.toString(collisionCounter)) + "\n" + //
				"Collision Rate: %" + Double.toString(collisionRate/100);
		
		return stats;
	}
	
	//PART 4
	public static void main(String args[]) throws IOException {
		FileReader fr = new FileReader("declare.txt");
		BufferedReader br = new BufferedReader(fr);
		String line, word;
		StringTokenizer st;
		
	//PART 5
		Table<String,String> declareTable1000 = new Table<>(1000);
		while ((line = br.readLine()) != null) {
			st = new StringTokenizer(line);
			while (st.hasMoreTokens()) {
				word = st.nextToken().toLowerCase();
				declareTable1000.put(word, word);
			}
		}
		br.close();
		System.out.println(declareTable1000.toString());
		System.out.println("");

		Table<String,String> declareTable2000 = new Table<>(2000);
		fr = new FileReader("declare.txt");
		br = new BufferedReader(fr);
		while ((line = br.readLine()) != null) {
			st = new StringTokenizer(line);
			while (st.hasMoreTokens()) {
				word = st.nextToken().toLowerCase();
				declareTable2000.put(word, word);
			}
		}
		br.close();
		System.out.println(declareTable2000.toString());
		System.out.println("");
		
		Table<String,String> declareTable4000 = new Table<>(4000);
		fr = new FileReader("declare.txt");
		br = new BufferedReader(fr);
		while ((line = br.readLine()) != null) {
			st = new StringTokenizer(line);
			while (st.hasMoreTokens()) {
				word = st.nextToken().toLowerCase();
				declareTable4000.put(word, word);
			}
		}
		br.close();
		System.out.println(declareTable4000.toString());
		System.out.println("");
		
		Table<String,String> declareTable8000 = new Table<>(8000);
		fr = new FileReader("declare.txt");
		br = new BufferedReader(fr);
		while ((line = br.readLine()) != null) {
			st = new StringTokenizer(line);
			while (st.hasMoreTokens()) {
				word = st.nextToken().toLowerCase();
				declareTable8000.put(word, word);
			}
		}
		br.close();
		System.out.println(declareTable8000.toString());
		System.out.println("");
	}
}
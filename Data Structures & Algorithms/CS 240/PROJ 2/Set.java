//
//	Name: Turchik, Kyle
//	Project: 2
//	Due: 2/10/2015
//	Course: cs-240-02-w15
//
//	Description:
//	Write a program that implements a Set ADT using a singly linked
//	list.  Include several methods to perform operations on the Set
//	such as returning data on the Set, adding or removing elements  
//	from the set, and comparing two different sets.  Also, provide 
//	a main class with various test cases to verify the methods all
//	work as intended.
//

public class Set {
	private Node head; // reference to the head node.
	private int size; // number of nodes in the list

	// LinkedList constructor
	public Set() {
		head = new Node(null); // dummy head
		size = 0;
	}

	/**
	 * Set contain
	 * <p>
	 * 
	 * @param data	The object to search for within the Set
	 * @return 		true if the given object is contained within, otherwise false
	 */
	public boolean contain(Object data) {
		Node node = head;
		
		// traverse the list until the given object is found, or to the end
		while (node != null) {
			if (data == node.getData()) {
				return true;
			}
			node = node.getNext();
		}
		return false;
	}

	/**
	 * Set remove
	 * <p>
	 * 
	 * @param data	The object to be removed from the Set
	 * @return 		true if the node with the given object is removed, otherwise false
	 */
	public boolean remove(Object data) {
		// the Set must contain the given object or else there is nothing to remove
		if (contain(data) == true) {
			Node node = head;
			Node test = node;

			// traverse the list until the given object is found, or to the end
			while (node != null) {
				test = node.getNext();
				// if found, remove node from link chain and set data to null
				if (test.getData() == data) {
					test.setData(null);
					node.setNext(node.getNext().getNext());
					size--; // decrement the number of elements variable
					return true;
				}
				node = node.getNext();
			}
		}
		return false;
	}

	/**
	 * Set addElement
	 * <p>
	 * 
	 * @param data	The object to be added to the Set
	 * @return 		false if the given element already exists, otherwise true
	 */
	public boolean addElement(Object data) {
		// the Set must not contain the given object since duplicates cannot be added
		if (contain(data) == false) {
			Node temp = new Node(data);
			Node node = head;

			// traverse the list to the end
			while (node.getNext() != null) {
				node = node.getNext();
			}
			// add data to end of list, replacing null node with temp node
			node.setNext(temp);
			size++; // increment the number of elements variable

			return true;
		}
		return false;
	}

	/**
	 * Set size
	 * <p>
	 * 
	 * @return 		The number of distinct objects in this list
	 */
	public int size() {
		/*
		 * There should be no duplicates because the addElement method prevents
		 * the adding of duplicates to the list
		 */
		return size;
	}

	/**
	 * Set get
	 * <p>
	 * 
	 * @param index		The position in the list to get data from
	 * @return 			The element at the given position in the list
	 */
	public Object get(int index) {
		// index cannot be less than 0
		if (index <= 0)
			return null;

		Node node = head;
		// traverse the list to the given position
		for (int i = 0; i < index; i++) {
			if (node.getNext() == null)
				return null;

			node = node.getNext();
		}
		return node.getData();
	}

	/**
	 * Set subsetOf
	 * <p>
	 * 
	 * @param B		A second Set 'B' to compare with the first Set 'A'
	 * @return 		true if Set B contains every element of Set A, otherwise false
	 */
	public boolean subsetOf(Set B) {
		Node node = head;
		Object current;

		// traverse the list to the end
		while (node != null) {
			current = node.getData();
			// check to see if Set B also contains each element contained in Set A
			if (B.contain(current) == false) {
				return false;
			}
			node = node.getNext();
		}

		return true;
	}

	/**
	 * Set isEqual
	 * <p>
	 * 
	 * @param B		A second Set 'B' to compare with the first Set 'A'
	 * @return 		true if Sets A and B both contain the same elements
	 */
	public boolean isEqual(Set B) {
		// iff both sets are subsets of each other, then they are equal
		if (subsetOf(B) == true && B.subsetOf(this) == true)
			return true;
		else
			return false;
	}

	/**
	 * Set union
	 * <p>
	 * 
	 * @param B		A second Set 'B' to compare with the first Set 'A'
	 * @return 		A new Set containing all elements from Sets A and B
	 */
	public Set union(Set B) {
		Set union = new Set();

		// traverse Set A to the end, adding each element from A to the new Set along the way
		for (int index = 0; index <= size; index++) {
			if (get(index) != null) {
				union.addElement(get(index));
			}
		}
		// traverse Set B to the end, adding each element from B to the new Set along the way
		for (int index = 0; index <= B.size; index++) {
			if (B.get(index) != null) {
				union.addElement(B.get(index));
			}
		}

		return union;
	}

	/**
	 * Set intersection
	 * <p>
	 * 
	 * @param B		A second Set 'B' to compare with the first Set 'A'
	 * @return 		A new Set containing only elements common to Sets A and B
	 */
	public Set intersection(Set B) {
		Set intersection = new Set();

		// traverse Set A to the end
		for (int index = 0; index <= size; index++) {
			if (get(index) != null) {
				// if Set B also contains an element from Set A, add that element to the new Set
				if (B.contain(get(index)) == true)
					intersection.addElement(get(index));
			}
		}

		return intersection;
	}

	/**
	 * Set complement
	 * <p>
	 * 
	 * @param B		A second Set 'B' to compare with the first Set 'A'
	 * @return 		A new Set containing only elements found in A, but not B
	 */
	public Set complement(Set B) {
		Set complement = new Set();

		// traverse Set A to the end
		for (int index = 0; index <= size; index++) {
			if (get(index) != null) {
				// if Set B does not contain an element from Set A, add that element to the new Set
				if (B.contain(get(index)) == false)
					complement.addElement(get(index));
			}
		}

		return complement;
	}

	/**
	 * Set toString
	 * <p>
	 * 
	 * @return 	A message displaying all the data found in the Set list
	 */
	public String toString() {
		String result = "{";
		Node node = head;

		node = node.getNext();

		while (node != null) {
			result += node.getData();
			node = node.getNext();
			if (node != null)
				result += ", ";
		}

		return result + "}";
	}

	
	private class Node {
		Node next;
		Object data;

		// Node constructor
		public Node(Object dataValue) {
			next = null;
			data = dataValue;
		}

		/**
		 * Node getData
		 * <p>
		 * 
		 * @return 	The data object stored at the current node, otherwise null
		 */
		public Object getData() {
			return data;
		}

		/**
		 * Node getNext
		 * <p>
		 * 
		 * @return 	A reference to the next node in the list after the current node
		 */
		public Node getNext() {
			return next;
		}

		/**
		 * Node setData
		 * <p>
		 * 
		 * @param dataValue		A data object, to be stored in the current node
		 * @return
		 */
		public void setData(Object dataValue) {
			data = dataValue;
		}

		/**
		 * Node setData
		 * <p>
		 * 
		 * @param nextValue		A reference to another node in the list, to be stored in the current node
		 * @return
		 */
		public void setNext(Node nextValue) {
			next = nextValue;
		}
	}

	public static void main(String[] args) {
		Set A;
		Set B;
		Set D;

		// GIVEN TEST CASE
		A = new Set();
		B = new Set();
		D = new Set();

		A.addElement(1);
		A.addElement(3);
		A.addElement(5);
		A.addElement(7);

		B.addElement(3);
		B.addElement(4);
		B.addElement(5);

		// GIVEN TEST CASE
		System.out.println("-------- GIVEN TEST CASE --------");
		System.out.println("--------- Basic Methods ---------");
		System.out.println("A.toString(): " + A.toString());
		System.out.println("B.toString(): " + B.toString());
		System.out.println("D.toString(): " + D.toString());
		System.out.println("");
		System.out.println("A.size(): " + A.size());
		System.out.println("B.size(): " + B.size());
		System.out.println("D.size(): " + D.size());
		System.out.println("");
		System.out.println("A.contain(3): " + A.contain(3));
		System.out.println("A.contain(4): " + A.contain(4));
		System.out.println("B.contain(3): " + B.contain(3));
		System.out.println("B.contain(4): " + B.contain(4));
		System.out.println("D.contain(3): " + D.contain(3));
		System.out.println("D.contain(4): " + D.contain(4));
		System.out.println("");
		System.out.println("------ Comparison Methods -------");
		System.out.println("if C=A.union(B): then C=" + A.union(B).toString());
		System.out.println("if C=A.intersection(B).toString(): then C=" + A.intersection(B).toString());
		System.out.println("if C=A.complement(B).toString(): then C=" + A.complement(B).toString());
		System.out.println("");
		System.out.println("D.subsetOf(A): " + D.subsetOf(A));
		System.out.println("D.subsetOf(B): " + D.subsetOf(B));
		System.out.println("D.subsetOf(D): " + D.subsetOf(D));
		System.out.println("D.subsetOf(C): " + D.subsetOf(A.complement(B)));
		System.out.println("");
		System.out.println("A.subsetOf(B): " + A.subsetOf(B));
		System.out.println("(A.intersection(B)).subsetOf(A): " + (A.intersection(B)).subsetOf(A));
		System.out.println("");
		System.out.println("A.isEqual(B): " + A.isEqual(B));
		System.out.println("B.isEqual(D): " + B.isEqual(D));
		System.out.println("");
		System.out.println("------- Changing the Data -------");
		System.out.println("A.addElement(4): " + A.addElement(4));
		System.out.println("A.addElement(6): " + A.addElement(6));
		System.out.println("A.remove(1): " + A.remove(1));
		System.out.println("B.remove(4): " + B.remove(4));
		System.out.println("");
		System.out.println("A.toString(): " + A.toString());
		System.out.println("B.toString(): " + B.toString());
		System.out.println("");
		System.out.println("A.addElement(3): " + A.addElement(3));
		System.out.println("B.remove(2): " + B.remove(2));
		System.out.println("");
		System.out.println("A.union(B).toString():" + A.union(B).toString());
		System.out.println("A.intersection(B).toString():" + A.intersection(B).toString());
		System.out.println("A.complement(B).toString():" + A.complement(B).toString());
		System.out.println("");
		System.out.println("");

		// TEST CASE 1
		A = new Set();
		B = new Set();

		A.addElement(1);
		A.addElement(2);
		A.addElement(3);

		B.addElement(2);
		B.addElement(1);
		B.addElement(3);

		System.out.println("----- ADDITIONAL TEST CASES -----");
		System.out.println("---------- TEST CASE 1 ----------");
		System.out.println("A.toString(): " + A.toString());
		System.out.println("A.size(): " + A.size());
		System.out.println("B.toString(): " + B.toString());
		System.out.println("B.size(): " + B.size());
		System.out.println("");
		System.out.println("A.subsetOf(B): " + A.subsetOf(B));
		System.out.println("B.subsetOf(A): " + B.subsetOf(A));
		System.out.println("A.isEqual(B): " + A.isEqual(B));
		System.out.println("");
		System.out.println("A.union(B).toString():" + A.union(B).toString());
		System.out.println("A.intersection(B).toString():" + A.intersection(B).toString());
		System.out.println("A.complement(B).toString():" + A.complement(B).toString());
		System.out.println("");

		// TEST CASE 2
		A = new Set();
		B = new Set();

		A.addElement(1);

		B.addElement(1);
		B.addElement(2);

		System.out.println("---------- TEST CASE 2 ----------");
		System.out.println("A.toString(): " + A.toString());
		System.out.println("A.size(): " + A.size());
		System.out.println("B.toString(): " + B.toString());
		System.out.println("B.size(): " + B.size());
		System.out.println("");
		System.out.println("A.subsetOf(B): " + A.subsetOf(B));
		System.out.println("B.subsetOf(A): " + B.subsetOf(A));
		System.out.println("A.isEqual(B): " + A.isEqual(B));
		System.out.println("");
		System.out.println("A.union(B).toString():" + A.union(B).toString());
		System.out.println("A.intersection(B).toString():" + A.intersection(B).toString());
		System.out.println("A.complement(B).toString():" + A.complement(B).toString());
		System.out.println("");

		// TEST CASE 3
		A = new Set();
		B = new Set();

		A.addElement(1);
		A.addElement(2);
		A.addElement(3);

		B.addElement(2);
		B.addElement(3);
		B.addElement(4);
		B.addElement(5);

		System.out.println("---------- TEST CASE 3 ----------");
		System.out.println("A.toString(): " + A.toString());
		System.out.println("A.size(): " + A.size());
		System.out.println("B.toString(): " + B.toString());
		System.out.println("B.size(): " + B.size());
		System.out.println("");
		System.out.println("A.subsetOf(B): " + A.subsetOf(B));
		System.out.println("B.subsetOf(A): " + B.subsetOf(A));
		System.out.println("A.isEqual(B): " + A.isEqual(B));
		System.out.println("");
		System.out.println("A.union(B).toString():" + A.union(B).toString());
		System.out.println("A.intersection(B).toString():" + A.intersection(B).toString());
		System.out.println("A.complement(B).toString():" + A.complement(B).toString());
		System.out.println("");

		// TEST CASE 4
		A = new Set();
		B = new Set();

		A.addElement(1);

		B.addElement(2);
		B.addElement(3);

		System.out.println("---------- TEST CASE 4 ----------");
		System.out.println("A.toString(): " + A.toString());
		System.out.println("A.size(): " + A.size());
		System.out.println("B.toString(): " + B.toString());
		System.out.println("B.size(): " + B.size());
		System.out.println("");
		System.out.println("A.subsetOf(B): " + A.subsetOf(B));
		System.out.println("B.subsetOf(A): " + B.subsetOf(A));
		System.out.println("A.isEqual(B): " + A.isEqual(B));
		System.out.println("");
		System.out.println("A.union(B).toString():" + A.union(B).toString());
		System.out.println("A.intersection(B).toString():" + A.intersection(B).toString());
		System.out.println("A.complement(B).toString():" + A.complement(B).toString());
		System.out.println("");

		// TEST CASE 5
		A = new Set();
		B = new Set();

		B.addElement(1);
		B.addElement(2);
		B.addElement(3);

		System.out.println("---------- TEST CASE 5 ----------");
		System.out.println("A.toString(): " + A.toString());
		System.out.println("A.size(): " + A.size());
		System.out.println("B.toString(): " + B.toString());
		System.out.println("B.size(): " + B.size());
		System.out.println("");
		System.out.println("A.subsetOf(B): " + A.subsetOf(B));
		System.out.println("B.subsetOf(A): " + B.subsetOf(A));
		System.out.println("A.isEqual(B): " + A.isEqual(B));
		System.out.println("");
		System.out.println("A.union(B).toString():" + A.union(B).toString());
		System.out.println("A.intersection(B).toString():" + A.intersection(B).toString());
		System.out.println("A.complement(B).toString():" + A.complement(B).toString());
		System.out.println("");
	}
}
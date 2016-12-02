//
//	Name: Turchik, Kyle
//	Homework: Extra Credit
//	Due: 2/19/2015
//	Course: cs-240-02-w15
//
//	Description:
//	Write complete a generic class for the CircularlyLinkedList ADT 
//	from question #8 of the midterm.
//

public class CircularlyLinkedList<T> {
    Node<T> cursor;

    //This method adds a node after the cursor
    public void add (T data) {
        Node<T> node = new Node<>(data, null);
        
        //first node might be empty
        if (cursor == null) {
    		System.out.println("adding element: " + data);
    		
        	cursor = node;
        	cursor.link = cursor;
        }
        else {
    		System.out.println("adding element: " + data);
    		
        	node.link = cursor.link;
        	cursor.link = node;
        }
    }

    //This method removes a node after the cursor
    public void remove () {
        if (cursor != null) {
        	if (cursor.link == cursor) {
        		System.out.println("removing element: " + cursor.data);
        		
        		cursor.link = null;
        		cursor = null;
        	}
        	else {
        		Node<T> temp = cursor.link;
        		
        		System.out.println("removing element: " + temp.data);
        		
        		cursor.link = temp.link;
        		temp.link = null;
        		temp = null;
        	}
        }
    }
    
    //This method advances the cursor to the next node
    public void advance () {
            cursor = cursor.link;
    }
    
    //This method returns the cursor data member
    public T get () {
        return cursor.data;
    }
    
    //This method returns the contents of the list in String form
    //starting at the cursor
    public String toString() {
        if (cursor == null) {
        	return "()";
        }
        String s = "( " + cursor.data;
        Node<T> temp = cursor;
        for (advance(); temp != cursor; advance()) 
          s += ", " + cursor.data;
        return s + " )";
      }
    
    //This is the Node class
    private class Node<E> {
        E data;
        Node<E> link;

        Node(E data, Node<E> link) {
            this.data = data;
            this.link = link;
        }
    }

    //This is the main method, it declares two circularly linked lists
    //of types int and String
    public static void main(String[] args) {
    	CircularlyLinkedList<Integer> iCLL = new CircularlyLinkedList<>();
    	CircularlyLinkedList<String> sCLL = new CircularlyLinkedList<>();  	
    	
    	System.out.println("---STRING LIST---");
    	System.out.println("Cursor Position: 0");
    	
    	for (String s : new String[]{"this", "cList", "a", "is"}){
    			sCLL.add(s);
    	}
    
    	System.out.println("Printing List:");
    	System.out.println(sCLL);
    	//Removing Element
    	sCLL.remove();
    	System.out.println("Printing List:");
    	System.out.println(sCLL);
    	System.out.println("Cursor Position: 1");
    	sCLL.advance();
    	System.out.println("cursor = \"" + sCLL.get() +"\"");
    	System.out.println("Cursor Position: 2");
    	sCLL.advance();
    	System.out.println("cursor = \"" + sCLL.get() +"\"");
    	//Adding Elements
    	sCLL.add("world");
    	sCLL.add("hello");
    	System.out.println("Cursor Position: 0");
    	sCLL.advance();
    	System.out.println("Printing List:");
    	System.out.println(sCLL);
	    
    	System.out.println("");
    	System.out.println("");
	    
    	
    	System.out.println("---INT LIST---");
    	System.out.println("Cursor Position: 0");
    	
    	for (int n : new int[]{ 0, 3, 2, 1 }){
    			iCLL.add(n);
    	}
    
    	System.out.println("Printing List:");
    	System.out.println(iCLL);
    	System.out.println("Cursor Position: 1");
    	iCLL.advance();
    	System.out.println("cursor = \"" + iCLL.get() +"\"");
    	//Removing Element
    	iCLL.remove();
    	System.out.println("Printing List:");
    	System.out.println(iCLL);
    	//Adding Elements
    	iCLL.add(5);
    	iCLL.add(4);
    	System.out.println("Printing List:");
    	System.out.println(iCLL);
    }
}
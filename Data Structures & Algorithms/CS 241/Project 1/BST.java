//
//	Name: Turchik, Kyle
//	Project: 1
//	Due: 4/28/2015
//	Course: cs-241-02-s15
//

/**
 * This is the class for the Binary Search Tree, which consists of several BTNode objects.
 * The tree can be distinguished by it's root node.  This class is capable of adding and
 * removing nodes, locating a specific node in the tree, and printing the traversal of the tree.
 */
public class BST
{
	private BTNode root;
	
	
	/**
	 * This is the class for the BTNode.  Each node is used to store some data, a link to its left
	 * and right children, and a link to it's parent.
	 */
	private class BTNode
	{
		public int data;
		private BTNode left;
		private BTNode right;
		private BTNode parent;
		
		
		/* Constructor */
		public BTNode (int data, BTNode left, BTNode right, BTNode parent)
		{
			this.data = data;
			this.left = left;
			this.right = right;
			this.parent = parent;
		}
		
		/* Accessors */
		public int getData()
		{
			return data;
		}
		
		public BTNode getLeft()
		{
			return left;
		}
		
		public BTNode getRight()
		{
			return right;
		}
		
		public BTNode getParent()
		{
			return parent;
		}
		
		/* Mutators */
		public void setData(int newData)
		{
			data = newData;
		}
		
		public void setLeft(BTNode newLeft)
		{
			left = newLeft;
		}
		
		public void setRight(BTNode newRight)
		{
			right = newRight;
		}
		
		public void setParent(BTNode newParent)
		{
			parent = newParent;
		}
		
		/**
		 * searches the tree or subtree that has this node as it's root for the leftmost node
		 * @return 	the value stored in the leftmost node
		 */
		public int getLeftmostData( )
		{
			if (left == null)
				return data;
			else
				return left.getLeftmostData( );
		}
		   
		/**
		 * searches the tree or subtree that has this node as its root for the rightmost node
		 * @return 	the value stored in the rightmost node
		 */
		public int getRightmostData( )
		{
			if (right == null)
				return data;
			else
				return right.getRightmostData( );
		}
		
		/**
		 * removes the leftmost node from the tree that has this node as its root
		 * @return 	a reference to the root of the new tree
		 */
		public BTNode removeLeftmost()
		{
		  if (left == null) {
		    return right;
		  } else {
		    left = left.removeLeftmost();
		    return this;
		  }
		}
		
		/**
		 * removes the rightmost node from the tree that has this node as its root
		 * @return 	a reference to the root of the new tree
		 */
		public BTNode removeRightmost()
		{
		  if (right == null) {
		    return left;
		  } else {
		    right = right.removeRightmost();
		    return this;
		  }
		}
		
		/**
		 * uses In-order traversal to print the data from each node in the tree that 
		 * has this node as it's root
		 */
		public void inorderPrint()
		{
			if (left != null)
				left.inorderPrint( );
			System.out.print(data + " ");
			if (right != null)
				right.inorderPrint( );
		} 
		   
		/**
		 * uses Pre-order traversal to print the data from each node in the tree that 
		 * has this node as it's root
		 */
		public void preorderPrint()
		{
			System.out.print(data + " ");
			if (left != null)
				left.preorderPrint();
			if (right != null)
				right.preorderPrint();
		}
		
		/**
		 * uses Post-order traversal to print the data from each node in the tree that 
		 * has this node as it's root
		 */
		public void postorderPrint()
		{
			if (left != null)
				left.postorderPrint();
			if (right != null)
				right.postorderPrint();
			System.out.print(data + " ");
		}
	}
	
    /**
     * searches the binary tree to determine if a specified value exists in any node
     * @param target	the value to search for
     * @return			true if the value is found, otherwise return false
     */
	public boolean exists(int target)
	{
		BTNode cursor = root;
		if(cursor == null) //The tree is empty
			return false;
		
       	while(cursor.getData() != target){
    		if (target < cursor.getData()){
    			if(cursor.getLeft() == null)
    				return false;
    			cursor = cursor.getLeft();
    		} else {
    			if(cursor.getRight() == null)
    				return false;
    			cursor = cursor.getRight();
    		}
    	}
       	return true;
	} 
	
    /**
     * searches the binary tree to locate the node that contains a specified value
     * @param target	the value to search for
     * @return			a reference to the node that contains the target
     */
	public BTNode find(int target)
	{
		BTNode cursor = root;

       	while(cursor.getData() != target){
    		if (target < cursor.getData()){
    			cursor = cursor.getLeft();
    		} else {
    			cursor = cursor.getRight();
    		}
    	}
       	return cursor;
	} 
	   
	/* These methods simply call methods from the BTNode class to print the traversals 
	 * of the tree, given this tree's root. */
	public void inorderPrint()
	{
		root.inorderPrint();
	} 
	   
	public void preorderPrint()
	{
		root.preorderPrint();
	}
	
	public void postorderPrint()
	{
		root.postorderPrint();
	}
	
	
    /**
     * adds a node containing the specified value to the tree
     * @param element	the value to be added
     */
	public void add(int element){ 	//This method makes it easier for main to call the add method
		add(element, root); 		//from the BST class.  It only has to specify the value it
	}								//wants to add.  Then this method calls the recursive method.
	
    /**
     * recursive method used to navigate the tree and properly add the node
     */
	public void add(int element, BTNode cursor)
	{	 	
		if (cursor == null) {	// The tree is empty, create the initial root
			root = new BTNode(element, null, null, null);
		} else {
			if (cursor.getData() > element){
				if (cursor.getLeft() == null) {	// Add the new node
					cursor.setLeft(new BTNode(element, null, null, cursor));
				} else {	// Move farther down the tree
					cursor = cursor.getLeft();
					add(element, cursor);
				}
			} else if (cursor.getData() < element){
				if (cursor.getRight() == null) {// Add the new node
					cursor.setRight(new BTNode(element, null, null, cursor));
				} else {	// Move farther down the tree
					cursor = cursor.getRight();
					add(element, cursor);
				}
			}
	  }  
	}
	
    /**
     * removes the node containing the specified value to the tree
     * @param element	the value to be added
     */
	public void remove(int element){ 	//This method makes it easier for main to call the remove method
		remove(element, root, null);	//from the BST class.  It only has to specify the value it
	}									//wants to add.  Then this method calls the recursive method.
	
    /**
     * recursive method used to navigate the tree and perform the removal process
     */
	public void remove(int element, BTNode cursor, BTNode parent)
	{
		// case #1, target does not exist 
		// This method will never be called by main if the target does not exist
		if(cursor.getData() == element){
			if (cursor.getLeft() == null) {
				if (cursor == root) { // case #2, target found at root with no left child
					root = root.getRight();
					root.setParent(null); // Make sure new node doesn't carry over its obsolete parent
				}
			 	if (cursor == parent.getLeft()) { // case #3, target found with no left child
			 		parent.setLeft(cursor.getRight()); // cursor is the left child
			 			if(parent.getLeft() != null)
			 				parent.getLeft().setParent(parent);
				} else {
					parent.setRight(cursor.getRight()); // cursor is the right child
		 				if(parent.getRight() != null)
		 					parent.getRight().setParent(parent);
				}
			} else { // case #4, there's a left child
			  	cursor.setData(cursor.getLeft().getRightmostData());
			 	cursor.setLeft(cursor.getLeft().removeRightmost());
	 				if(cursor.getLeft() != null)
	 					cursor.getLeft().setParent(cursor);
			}
		} else if (element < cursor.getData()) { // target is less than the cursor data
			parent = cursor;
			cursor = cursor.getLeft();
			remove(element, cursor, parent); // Move farther down the tree
		} else { // target is greater than the cursor data
			parent = cursor;
			cursor = cursor.getRight();
			remove(element, cursor, parent); // Move farther down the tree
		}
	}
	
    /**
     * locates the predecessor node of the node containing the specified value
     * @param target	the value to search for
     * @return			the value of the predecessor node
     */
	public int findPredecessor(int target)
    {
	    if (root.getLeftmostData() == target) // Leftmost node has no predecessor
	    	return -1;
		
		BTNode cursor = find(target);
		BTNode y = null;
		BTNode x = null;
        
		// All nodes on the left of the target node are smaller, the next smallest will 
		// be the rightmost node on the left child's subtree
	    if (cursor.getLeft() != null){
	        return cursor.getLeft().getRightmostData();
	    } else { // Traverse up the tree until you find the first node on the left
		    y = cursor.getParent();
		    x = cursor;
		    
		    while (y != null && x == y.getLeft())
		    {
		        x = y;
		        y = y.getParent();
		    }
	    }
        
        return y.getData();
    }
	
    /**
     * locates the successor node of the node containing the specified value
     * @param target	the value to search for
     * @return			the value of the successor node, or -1 if there is none
     */
	public int findSuccessor(int target)
    {
	    if (root.getRightmostData() == target) // Rightmost node has no successor
	    	return -1;
	    
		BTNode cursor = find(target);
		BTNode y = null; 
		BTNode x = null;
        
		// All nodes on the right of the target node are larger, the next largest will 
		// be the leftmost node on the right child's subtree
	    if (cursor.getRight() != null){ 
	    	return cursor.getRight().getLeftmostData();
	    } else { // Traverse up the tree until you find the first node on the right
	    	y = cursor.getParent();
		    x = cursor;
		    
		    while (y != null && x == y.getRight())
		    {
		    	x = y;
		        y = y.getParent();
		    }
	    }

        return y.getData();
    }
}
	
	

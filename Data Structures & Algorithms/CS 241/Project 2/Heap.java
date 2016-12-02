//
//	Name: Turchik, Kyle
//	Project: 2
//	Due: 5/14/2015
//	Course: cs-241-02-s15
//

public class Heap {
	private static final int DEFAULT_CAPACITY = 100;
	protected int[] array;
	protected int size;
	protected int swapCount;
	
	/**
	 * Constructor
	 */
	public Heap (){
		array = new int[DEFAULT_CAPACITY];
		size = 0;
	}
	
	/**
	 * @return 	the number of swaps needed to construct the heap
	 */
	public int getSwapCount (){
		return swapCount; 
	}
	
	
	/**
	 * inserts values into the array in sequence, before using the 
	 * optimalSort method to create a valid heap
	 * @param value		the value to insert into the array
	 */
	public void insertArbitrary(int value) {
		int index = size;
		array[index] = value;
		
		if(size < DEFAULT_CAPACITY)
			size++;
	}
	
	
	/**
	 * uses the optimal method to construct a valid heap using values already
	 * inserted into the array by insertArbitrary()
	 */
	public void optimalSort() {
		int index;
		int swapsInPass;

		while(true){
			index = size - 1;
			swapsInPass = 0;
			
			while (hasParent(index)) {
				if(array[parentIndex(index)] < array[index]) {
		            // parent/child are out of order, swap parent with largest child
					if(array[index] >= array[siblingIndex(index)]){
						swap(index, parentIndex(index));
						swapsInPass += 1;
					}
					else if(array[index] < array[siblingIndex(index)]){
						swap(siblingIndex(index), parentIndex(index));
						swapsInPass += 1;
					}
				}
				index--;
			}
			// when the entire heap is traversed from last leaf to the root without any 
			// swaps, it is valid and finished sorting
			if(swapsInPass == 0){
				break;
			}
		}
	}
	
	
	/**
	 * inserts values into the array in sequence, treating the array as if it were a valid 
	 * heap. If inserting a value causes the heap to become invalid, the value is moved up 
	 * until the heap becomes valid again
	 * @param value		the value to insert into the heap
	 */
	public void insert(int value) {	
		int index = size;
		array[index] = value;
		
		heapifyUp();
		
		if(size < DEFAULT_CAPACITY)
			size++;
	}
	
	
	/**
	 * removes the first value in the array (the root of the heap) a given number of times,  
	 * the root is swapped with the value at the end of the array and this new value is
	 * moved down until the heap becomes valid again
	 * @param numRemovals		the number of times to remove the root from the heap
	 */
	public void remove(int numRemovals) {
		int index;
		
		for(int i = 0; i < numRemovals; i++){
			index = size - 1;
			
	    	array[0] = array[index];
	    	//array[index] = null;
	    	size--;
	    	
	    	heapifyDown();
		}
    }

	
	/**
	 * moves the node at the end of the array (the last leaf of the heap) up by
	 * swapping it with other values until the heap becomes valid
	 */
    protected void heapifyUp() {
        int index = size;
        
        while (hasParent(index)
                && (array[parentIndex(index)] < array[index])) {
            // parent/child are out of order, swap them
            swap(index, parentIndex(index));
            index = parentIndex(index);
        }        
    }
    
	
	/**
	 * moves the first node in the array (the root of the heap) down by
	 * swapping it with other values until the heap becomes valid
	 */
	protected void heapifyDown() {
        int index = 0;
        
        // if the node is not a leaf
        while (hasLeftChild(index)) {
            // find which child is largest
            int largestChild = leftIndex(index);
            
            if (hasRightChild(index)
                && array[leftIndex(index)] < array[rightIndex(index)]) {
            	largestChild = rightIndex(index);
            } 
            
            if (array[index] < array[largestChild]) {
            	// parent/child are out of order swap them
                swap(index, largestChild);
            } else {
                break;
            }
            
            index = largestChild;
        }        
    }
	

	/**
	 * @return 	true if the specified node is not the root node
	 */
    protected boolean hasParent(int i) {
        return i > 0;
    }
    
	/**
	 * @return 	the index of the specified node's left child
	 */
    protected int leftIndex(int i) {
        return i * 2 + 1;
    }
    
	/**
	 * @return 	the index of the specified node's right child
	 */
    protected int rightIndex(int i) {
        return i * 2 + 2;
    }
    
	/**
	 * @return 	true if the specified node has a left child
	 */
    protected boolean hasLeftChild(int i) {
        return leftIndex(i) < size;
    }
    
	/**
	 * @return 	true if the specified node has a right child
	 */
    protected boolean hasRightChild(int i) {
        return rightIndex(i) < size;
    }
    
	/**
	 * @return 		the index of the specified node's parent
	 */
    protected int parentIndex(int i) {
        return (i - 1) / 2;
    }
    
	/**
	 * @return 	the index of the specified node's sibling, or the index of the specified 
	 * 			node itself if there is no sibling
	 */
    protected int siblingIndex(int i) {
    	if(i > 0){
    		//if the index of the specified node is even, it is the right child 
	    	if((i % 2) == 0)
	    		return i - 1;
    		//if the index of the specified node is odd, it is the left child
	    	else if((i % 2) == 1)
	    		//the left child may not have a sibling if it is the last node in the heap
	    		if((i + 1) < size)
	    			return i + 1;
    	}
    	return i;
    }
    
    
	/**
	 * swaps the positions of two keys in the tree
	 * @param index1	the first node to swap
	 * @param index2	the second node to swap
	 */
    protected void swap(int index1, int index2) {
        int tmp = array[index1];
        array[index1] = array[index2];
        array[index2] = tmp;        
        
        swapCount++;
    }
    
    
	/**
	 * prints the first 10 numbers in the in-order traversal of the heap
	 */
    protected void print() {
    	for(int i = 0; i < size && i < 10; i++){
    		System.out.print(array[i] + ", ");
    	}
    	System.out.println("...");
    }
}

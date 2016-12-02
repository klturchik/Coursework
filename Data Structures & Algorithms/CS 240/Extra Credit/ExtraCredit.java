//
//	Name: Turchik, Kyle
//	Homework: Extra Credit
//	Due: 3/17/2015
//	Course: cs-240-02-w15
//
//	Description:
//	Implement a class with a method that can evaluate a postfix
//	expression using a variable/value list.  This class also 
//	includes a main class utilizing the methods in project #3 to
//	convert an infix expression to a postfix expression and input 
//	the expression along with a variable/value list into the 
//	postfix evaluation method.
//

public class ExtraCredit {
		
	public static int evalPostfix(String[] postfix, String[] variables, String[] values) {
		int r = 0; //The value that will be returned
		Stack a = new Stack(postfix.length);
		
		//First replace any variables with given values from the variable/value list
	    for (int i = 0, j = 0; i < postfix.length; i++) {
	        switch(postfix[i]) {
	        	//Default, if the current symbol is not an operator
	        	default:
	        		//Check if the current symbol from the postfix expression matches a variable
	        		//from the variable list
		        	if(postfix[i].equals(variables[j])) {
			        	try {
		        		postfix[i] = values[j];
			        	//EXCEPTION: Value not found 
			        	//If the value for the variable is not listed in the value list
			        	} catch (RuntimeException e) {
			        		System.err.println("Error: Value Not Found");
			        		System.err.println("For the variable: \"" + variables[j] + "\"");
			        		throw new RuntimeException();
			        	}
		        		//Do not increment values place if there are no more variables left
		        		if (j < variables.length - 1)
		        			j++;
		        	}
		        	//If the current symbol does not match a variable it may be a constant, so
		        	//attempt to parse the int
		        	try {
		        		a.push(Integer.parseInt(postfix[i]));
		        	//EXCEPTION: Variable not found 
		        	//If the variable is not listed in the variable list
		        	} catch (RuntimeException e) {
		        		System.err.println("Error: Variable Not Found");
		        		System.err.println(e.getMessage());
		        		throw new RuntimeException();
		        	}
		        	break;
		        //One of these cases, if the current symbol IS an operator
		        case "+":
		        case "-":
		        case "*":
		        case "/":
			        int y = a.pop();
			        int x = a.pop();
			        //EXCEPTION: Malformed Expression
			        //If the value cannot be popped, SEE: Stack class
			        switch(postfix[i]) {
				        default: r = 0;
				        case "+": r = x + y;
				        	break;
				        case "-": r = x - y;
				        	break;
				        case "*": r = x * y;
				        	break;
				        case "/": r = x / y;
				        	break;
			        }
			        a.push(r);
			        break;
	        }
	    }
    	//EXCEPTION: Malformed Expression 
	    //If there is a value left at the end of the expression, but no operator
    	if(!postfix[postfix.length - 1].equals("+") && !postfix[postfix.length - 1].equals("+") && //
    			!postfix[postfix.length - 1].equals("*") && !postfix[postfix.length - 1].equals("/")) {
    		System.err.println("Error: Malformed Expression"); 
    		System.err.println("Expression contains an operand without an operator");
    		throw new RuntimeException();
    	}
	    r = a.pop();
	    return r;
	}
	
	
	private static class Stack {
		private int[] stack;    // int stack
	    int top;        // top of the stack, -1 empty stack
	
	    public Stack (int capacity) {
	        stack = new int[capacity];
	        top = -1;
	    }
	
	    public void push (int key) {
	        if (top + 1 == stack.length) {
	            throw new RuntimeException("Stack full");
	        }
	        stack[++top] = key;
	    }
	
	    public int pop () {
        	//EXCEPTION: Malformed Expression 
	    	//If there are less operands in the expression than operators, 
	    	//there must always be two operands in the stack before an operator
	        if (top == -1) {
        		System.err.println("Error: Malformed Expression");
        		System.err.println("Expression is missing an operand");
	            throw new RuntimeException();
	        }
	        return stack[top--];
	    }
	
	    public String toString () {
	        String s = "BOTTOM[ ";
	        for (int i = 0; i <= top; i++) {
	            s += stack[i] + " ";
	        }
	        return s + "]TOP";
	    }
    }
	
	
	public static void main(String[] args) {
		//Testing the evalPostfix method by itself
		String[] postfix = { "value1", "value2", "+", "3", "*" };
		String[] variables = { "value1", "value2" };
		String[] values = { "1", "2" };
		
		System.out.print("Postfix:  ");
		for(int i=0; i <  postfix.length; i++){
			System.out.print(postfix[i] + " ");
    	}
		System.out.println("");
		System.out.print("Values:   ");
		for(int i=0; i <  values.length; i++){
			System.out.print(values[i] + " ");
    	}
		System.out.println("");
		
		System.out.println("Evaluation = " + evalPostfix(postfix, variables, values));	
		System.out.println("");
		
		//Utilizing methods from Project #3
		String infixExpression = "a+b*c";
		String[] postfix2;
		String[] variables2 = { "a", "b", "c" };
		String[] values2 = { "1", "2", "3" };
		
		System.out.println("Infix:    " + infixExpression);
		String[] infixExpressionArray = new String[infixExpression.length()];
		for(int i=0; i < infixExpressionArray.length; i++){
			String temp = "" + infixExpression.charAt(i);
			infixExpressionArray[i] = temp;
    	}
		postfix2 = InfixExpression.convertToPostfix(infixExpressionArray);
		System.out.print("Postfix:  ");
		for(int i=0; i <  postfix2.length; i++){
			System.out.print(postfix2[i] + " ");
    	}
		System.out.println("");
		System.out.print("Values:   ");
		for(int i=0; i <  values2.length; i++){
			System.out.print(values2[i] + " ");
    	}
		System.out.println("");
		
		System.out.println("Evaluation = " + evalPostfix(postfix2, variables2, values2));	
		System.out.println("");
		
		//Utilizing methods from Project #3	
		infixExpression = "(a+b)*(c-d)";
		String[] postfix3;
		String[] variables3 = { "a", "b", "c", "d" };
		String[] values3 = { "1", "2", "3", "4" };
		
		System.out.println("Infix:    " + infixExpression);
		infixExpressionArray = new String[infixExpression.length()];
		for(int i=0; i < infixExpressionArray.length; i++){
			String temp = "" + infixExpression.charAt(i);
			infixExpressionArray[i] = temp;
    	}
		postfix3 = InfixExpression.convertToPostfix(infixExpressionArray);
		System.out.print("Postfix:  ");
		for(int i=0; i <  postfix3.length; i++){
			System.out.print(postfix3[i] + " ");
    	}
		System.out.println("");
		System.out.print("Values:   ");
		for(int i=0; i <  values3.length; i++){
			System.out.print(values3[i] + " ");
    	}
		System.out.println("");
		
		System.out.println("Evaluation = " + evalPostfix(postfix3, variables3, values3));	
		System.out.println("");
    }
}
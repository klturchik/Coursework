//
//	Name: Turchik, Kyle
//	Project: 3
//	Due: 3/12/2015
//	Course: cs-240-02-w15
//
//	Description:
//	Write a program that implements two methods that can convert
//	an infix expression to its equivalent postfix and prefix 
//	expressions.  The methods will utilize the Stack ADT to sort
//	the characters in the expression.
//

public class InfixExpression {
	
	/**
	 * InfixExpression convertToPostfix
	 * <p>
	 * @param String[]	The original infix expression converted into an
	 * 					array of Strings. 
	 * @return 			The postfix equivalent of the expression converted 
	 * 					into an array of Strings.				 
	 */
    public static String[] convertToPostfix (String[] infixExpression) {
        	String postfixString = ""; //The String that will be returned
        	Stack s = new Stack(infixExpression.length);
        	
	        String op;
	        //Traverse the infix expression from left to right
	        for (int index = 0; index < infixExpression.length; index++) {
	            String symbol = infixExpression[index];
	            
	            switch (symbol) {
	            //Variables (such as a, b, and c) are directly copied to the output.
	            default:
	                postfixString += symbol;

		            break;
		        //Left parenthesis are always pushed onto a stack	            
	            case "(" :
	               s.push("(");

	               break;
	            //When a right parenthesis is encountered, the symbol on the top of the stack is popped off the
	            //stack and copied to the output. This process repeats until the top of the stack is a left
	            //parenthesis. When that occurs, both parentheses are discarded
	            case ")":
	                //If a right parenthesis is scanned when the stack is empty,
	                //the parenthesis of the original expression were unbalanced.
		        	if (s.empty()) {
		        		System.out.println("Error: Unbalanced Parenthesis");
		        		s.pop();
		        	}
	                while (!(s.peek() == "(") && !(s.empty())) {
		                op = s.pop();
	                    postfixString += op;
	                }
	                s.pop();

		            break;
		        //If the symbol being scanned has a higher precedence than the symbol on the top of the stack,
		        //the symbol being scanned is pushed onto the stack.
		        //   
	            //If the symbol being scanned has a lower or the same precedence than the symbol at the top of
	            //the stack, the symbol at the top of the stack is popped off to the output. The symbol being
	            //scanned will be compared with the new top element on the stack, and the process
	            //continues.
			        case "*": 
			        case "/":
			        	if (s.empty()) {
			        		s.push(symbol);
			        	} else {
			        		//While same or lower precedence
			        		while (!s.empty() && (s.peek().equals("*") || s.peek().equals("/"))) {
				        		op = s.pop();
			                	postfixString += op;
			                }
			        		//Higher precedence
			                s.push(symbol);
			            }

			            break;
			        //'+' and '-' will always have a lower or the same preference as the top of the stack
		            case "+":
		            case "-":
		                if (s.empty()) {
		                    s.push(symbol);
		                } else {
		                    while (!s.empty() && !(s.peek().equals("(") || s.peek().equals(")"))) {
		                        op = s.pop();
		                        postfixString += op;
		                    }
		                    s.push(symbol);
		                }

			            break;
		            }
	            }
	        
            //When the input is empty, the stack is popped to the output until the stack is empty. Then the
        	//algorithm terminates.
	        while (!s.empty()) {
	            //If the input is empty but the stack still has a left parenthesis,
	            //the parenthesis of the original expression were unbalanced.
	            if (s.peek().equals("(")) {
	        		System.out.println("Error: Unbalanced Parenthesis");
	        		s.pop();
	            }
	            
                op = s.pop();
                postfixString += op;
	        }

			String[] postfixStringArray = new String[postfixString.length()];
			for(int i=0; i < postfixStringArray.length; i++){
				String temp = "";
				temp += postfixString.charAt(i);
				postfixStringArray[i] = temp;
	    	}
	        return postfixStringArray;
	    }

		/**
		 * InfixExpression convertToPostfix
		 * <p>
		 * @param String[]	The original infix expression converted into an
		 * 					array of Strings. 
		 * @return 			The prefix equivalent of the expression converted 
		 * 					into an array of Strings.				 
		 */
		public static String[] convertToPrefix (String[] infixExpression){
	        String prefixString = ""; //The String that will be returned
        	Stack opand = new Stack(infixExpression.length);
        	Stack optor = new Stack(infixExpression.length);
	        
        	String op;
        	String RightOperand;
        	String LeftOperand;
            String newExp = "";
	        //Traverse the infix expression from left to right
	        for (int index = 0; index < infixExpression.length; index++) {
	            String symbol = infixExpression[index];

	            switch (symbol) {
	            //Variables (operands) are pushed onto the operand stack.
	            default:
	                opand.push(symbol);
	                
		            break;
		        //Left parentheses are always pushed onto the operators stack	            
	            case "(" :
	               optor.push("(");
	               
	               break;
	            //When a right parenthesis is encountered, the operator in the operator stack is popped off and
	            //saved to op, the expression on the top of the operand stack is popped off and saved to
	            //RightOperand, another expression on the top of the operand stack is popped off and saved to
	            //LeftOperand. A new expression is formed by “op LeftOperand RightOperand” and is pushed
	            //back to the operand stack. This process repeats until the top of the operator stack is a left 
	            //parenthesis. When that occurs, both parentheses are discarded. 
	            case ")":

	                while (!(optor.peek() == "(") && !(optor.empty())) {
		                op = optor.pop();
		                RightOperand = opand.pop();
		                LeftOperand = opand.pop();
		                	                
		                newExp = op + LeftOperand + RightOperand;
		                opand.push(newExp);
	                }
	                optor.pop();;
	                
		            break;
		        //If the symbol being scanned has a higher precedence than the symbol on the top of the
			    //operator stack, the symbol being scanned is pushed onto the operator stack.
		        //
			    //If the symbol being scanned has a lower or the same precedence as the symbol on the top of
			    //the operator stack, the symbol on the top of the stack is popped off and saved to op. Pop
			    //two expressions from the operands stack and saved them to RightOperand and LeftOperator
			    //respectively. They form a new expression “op LeftOperand RightOperand” and are pushed to
			    //the operand stack. The symbol been scanned will be compared with the new top element on
			    //the operator stack, and the process continue.    
		        case "*": 
		        case "/":
		            if (optor.empty()) {
		            	optor.push(symbol);
		            } else {
	                    op = optor.peek();
		        		//While same or lower precedence
	                    
	                    while (!optor.empty() && (optor.peek().equals("*") || optor.peek().equals("/"))) {
	                    	optor.pop();
			                RightOperand = opand.pop();
			                LeftOperand = opand.pop();
			                
			                newExp = op + LeftOperand + RightOperand;
			                opand.push(newExp);
	                    }
		        		//Higher precedence
	                    optor.push(symbol);
		            }
		            
		            break;
			    //'+' and '-' will always have a lower or the same preference as the top of the stack
	            case "+":
	            case "-":
	                if (optor.empty()) {
	                	optor.push(symbol);
	                } else {
	                    while (!optor.empty() && !(optor.peek().equals("(") || optor.peek().equals(")"))) {
	                    	op = optor.pop();
			                RightOperand = opand.pop();
			                LeftOperand = opand.pop();
			                
			                newExp = op + LeftOperand + RightOperand;
			                opand.push(newExp);
	                    }
	                    optor.push(symbol);
	                }
	                
		            break;
	            }
	        }

	        while (!optor.empty() && !opand.empty()) {
		        //If the input is empty but the stack still has a left parenthesis,
		        //the parenthesis of the original expression were unbalanced.
		        if (optor.peek() == "(") {
		        	System.out.println("Error: Unbalanced Parenthesis");
		        	optor.pop();
		        }
	        	else {
                	op = optor.pop();
	                RightOperand = opand.pop();
	                LeftOperand = opand.pop();
	                
	                newExp = op + LeftOperand + RightOperand;
	                opand.push(newExp);
	        	}
                prefixString = newExp;
	        }

	        
			String[] prefixStringArray = new String[prefixString.length()];
			for(int i=0; i < prefixStringArray.length; i++){
				String temp = "";
				temp += prefixString.charAt(i);
				prefixStringArray[i] = temp;
	    	}
	        return prefixStringArray;
		}
		
		
		private static class Stack {
			String[] stack;    // char stack
		    int top;        // top of the stack, -1 empty stack
		
		    public Stack (int capacity) {
		        stack = new String[capacity];
		        top = -1;
		    }
		
		    public void push (String e) {
		        if (top + 1 == stack.length) {
		            throw new RuntimeException("Stack full");
		        }
		        stack[++top] = e;
		    }
		
		    public String pop () {
		        if (top == -1) {
		            throw new RuntimeException("Stack empty");
		        }
		        return stack[top--];
		    }
		
		    public boolean empty () {
		        return top == -1;
		    }
		    
		    public String peek () {
		        if (top == -1) {
		            throw new RuntimeException("Stack empty");
		        }
		        
		        return stack[top];
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
	    	System.out.println("CUSTOM TEST CASES:");
			//TEST CASE CUSTOM 1
			String infixExpression = "3+4*5/6";
			System.out.println("Infix:    " + infixExpression);
			
			String[] infixExpressionArray = new String[infixExpression.length()];
			for(int i=0; i < infixExpressionArray.length; i++){
				String temp = "" + infixExpression.charAt(i);
				infixExpressionArray[i] = temp;
	    	}

			String postfixExpression = "";
			String[] postfixExpressionArray = convertToPostfix(infixExpressionArray);
			for(int i=0; i <  postfixExpressionArray.length; i++){
				postfixExpression += postfixExpressionArray[i];
	    	}
			
			String prefixExpression = "";
			String[] prefixExpressionArray = convertToPrefix(infixExpressionArray);
			for(int i=0; i <  prefixExpressionArray.length; i++){
				prefixExpression += prefixExpressionArray[i];
	    	}		
			System.out.println("Postfix:  " + postfixExpression);
			System.out.println("Prefix:   " + prefixExpression);
			System.out.println();
			
			//TEST CASE CUSTOM 2
			infixExpression = "(a+b-c)*(e/f)-(g-h/i)";
			System.out.println("Infix:    " + infixExpression);
			
			infixExpressionArray = new String[infixExpression.length()];
			for(int i=0; i < infixExpressionArray.length; i++){
				String temp = "" + infixExpression.charAt(i);
				infixExpressionArray[i] = temp;
	    	}

			postfixExpression = "";
			postfixExpressionArray = convertToPostfix(infixExpressionArray);
			for(int i=0; i <  postfixExpressionArray.length; i++){
				postfixExpression += postfixExpressionArray[i];
	    	}
			
			prefixExpression = "";
			prefixExpressionArray = convertToPrefix(infixExpressionArray);
			for(int i=0; i <  prefixExpressionArray.length; i++){
				prefixExpression += prefixExpressionArray[i];
	    	}
			System.out.println("Postfix:  " + postfixExpression);
			System.out.println("Prefix:   " + prefixExpression);
			System.out.println();
			
	    	System.out.println("PROVIDED TEST CASES:");
	    	//TEST CASE 1
	    	infixExpression = "A*(B+C)/D";
			System.out.println("Infix:    " + infixExpression);
			
			infixExpressionArray = new String[infixExpression.length()];
			for(int i=0; i < infixExpressionArray.length; i++){
				String temp = "" + infixExpression.charAt(i);
				infixExpressionArray[i] = temp;
	    	}

			postfixExpression = "";
			postfixExpressionArray = convertToPostfix(infixExpressionArray);
			for(int i=0; i <  postfixExpressionArray.length; i++){
				postfixExpression += postfixExpressionArray[i];
	    	}
			
			prefixExpression = "";
			prefixExpressionArray = convertToPrefix(infixExpressionArray);
			for(int i=0; i <  prefixExpressionArray.length; i++){
				prefixExpression += prefixExpressionArray[i];
	    	}
			System.out.println("Postfix:  " + postfixExpression);
			System.out.println("Prefix:   " + prefixExpression);
			System.out.println();
			
	    	//TEST CASE 2
			infixExpression = "a+b+c";
			System.out.println("Infix:    " + infixExpression);
			
			infixExpressionArray = new String[infixExpression.length()];
			for(int i=0; i < infixExpressionArray.length; i++){
				String temp = "" + infixExpression.charAt(i);
				infixExpressionArray[i] = temp;
	    	}

			postfixExpression = "";
			postfixExpressionArray = convertToPostfix(infixExpressionArray);
			for(int i=0; i <  postfixExpressionArray.length; i++){
				postfixExpression += postfixExpressionArray[i];
	    	}
			
			prefixExpression = "";
			prefixExpressionArray = convertToPrefix(infixExpressionArray);
			for(int i=0; i <  prefixExpressionArray.length; i++){
				prefixExpression += prefixExpressionArray[i];
	    	}		
			System.out.println("Postfix:  " + postfixExpression);
			System.out.println("Prefix:   " + prefixExpression);
			System.out.println();
			
	    	//TEST CASE 3
			infixExpression = "a+b*c";
			System.out.println("Infix:    " + infixExpression);
			
			infixExpressionArray = new String[infixExpression.length()];
			for(int i=0; i < infixExpressionArray.length; i++){
				String temp = "" + infixExpression.charAt(i);
				infixExpressionArray[i] = temp;
	    	}

			postfixExpression = "";
			postfixExpressionArray = convertToPostfix(infixExpressionArray);
			for(int i=0; i <  postfixExpressionArray.length; i++){
				postfixExpression += postfixExpressionArray[i];
	    	}
			
			prefixExpression = "";
			prefixExpressionArray = convertToPrefix(infixExpressionArray);
			for(int i=0; i <  prefixExpressionArray.length; i++){
				prefixExpression += prefixExpressionArray[i];
	    	}
			System.out.println("Postfix:  " + postfixExpression);
			System.out.println("Prefix:   " + prefixExpression);
			System.out.println();
			
	    	//TEST CASE 4
			infixExpression = "(a+b)*(c-d)";
			System.out.println("Infix:    " + infixExpression);
			
			infixExpressionArray = new String[infixExpression.length()];
			for(int i=0; i < infixExpressionArray.length; i++){
				String temp = "" + infixExpression.charAt(i);
				infixExpressionArray[i] = temp;
	    	}

			postfixExpression = "";
			postfixExpressionArray = convertToPostfix(infixExpressionArray);
			for(int i=0; i <  postfixExpressionArray.length; i++){
				postfixExpression += postfixExpressionArray[i];
	    	}
			
			prefixExpression = "";
			prefixExpressionArray = convertToPrefix(infixExpressionArray);
			for(int i=0; i <  prefixExpressionArray.length; i++){
				prefixExpression += prefixExpressionArray[i];
	    	}
			System.out.println("Postfix:  " + postfixExpression);
			System.out.println("Prefix:   " + prefixExpression);
			System.out.println();
	    }
}

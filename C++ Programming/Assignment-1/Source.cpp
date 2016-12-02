#include <iostream>
#include <stack>
#include <string>
#include <sstream>
using namespace std;


bool isDouble(string element);

class PostfixExpression {
	string expression;
	double answer;

	public:
		// constructor
		PostfixExpression (string);

		// accessors
		string get_expression(){
			return expression;
		}

		double get_answer(){
			return answer;
		}

		// automatically called by the constructor to evaluate the answer to the expression
		double eval(){
			stack<double> calcStack;
			stringstream stream;

			stream << expression; // helps split the expression into multiple strings separated by spaces
			string element;

			// invalid input
			// if the postfix expression starts with an operator
			if(expression[0] == '*' || expression[0] == '/' || expression[0] == '+' || expression[0] == '-')
			{
				cout << "Error: Invalid Input" << endl << endl; // display error message
				stream.str(""); // clear the string
				return NULL; // set answer to NULL
			}

			while(stream >> element) {
				// check for numeric value
				if(isDouble(element))
				{
					double num;
					num = stod(element);
					calcStack.push(num);
				}

				// check for operator
				else if (element == "*" || element == "/" || element == "+" || element == "-")
				{
					string optor = element;
					double op1, op2, result;

					op2 = calcStack.top();
					calcStack.pop();

					// invalid input
					// if there are not two operands for the operator
					if(calcStack.empty())
					{
						cout << "Error: Invalid Input" << endl << endl; // display error message
						stream.str(""); // clear the string
						return NULL; // set answer to NULL
					}

					op1 = calcStack.top();
					calcStack.pop();

					if(optor == "-")
						result = op1 - op2;
					if(optor == "+")
						result = op1 + op2;
					if(optor == "*")
						result = op1 * op2;
					if(optor == "/")
						result = op1 / op2;

					calcStack.push(result);
				}

				// invalid input
				// if any parts of the expression are not a valid double or operator
				else
				{
					cout << "Error: Invalid Input" << endl << endl; // display error message
					stream.str(""); // clear the string
					return NULL; // set answer to NULL
				}
			}

			double answer = calcStack.top();
			calcStack.pop();

			// invalid input
			// if the stack is not empty after evaluation, e.g. the postfix expression did not end with an operator
			if(!calcStack.empty())
			{
				cout << "Error: Invalid Input" << endl << endl; // display error message
				stream.str(""); // clear the string
				return NULL; // set answer to NULL
			}

			return answer;
		}
};

// constructor for the PostfixExpression class 
PostfixExpression::PostfixExpression (string input) {
	expression = input;
	answer = eval(); // automatically evaluate the expression and store the answer in the class
}

bool isDouble(string element){
	int dcount = 0; // keeps count of decimal points

	// check to see if each character in the string is a number or decimal point
	for(unsigned int i = 0; i < element.length(); i++){
		if(isdigit(element[i])){
		}
		else if(element[i] == '.'){
			dcount++;
			// if there is more than one decimal point, return false
			if(dcount > 1){
				return false;
			}
		}
		// if any character is not a number or decimal point, return false
		else{
			return false;
		}
	}
	// if all tests are passed, return true
	return true;
}

int main() {
	// introductory message
	cout << "Please enter a postfix expression (e.g. \"4 5 * 6 + 2 * 1 - 6 / 4 2 + 3 * *\")" << endl;
	cout << "Enter \"q\" to quit" << endl << endl;

	while(true){
		string input;

		// display prompt
		cout << "Input Expression: ";

		// get input
		getline (cin, input);

			// quit the program if the user inputs "q"
			if(input[0] == 'q' || input[0] == 'Q'){
				return 0;
			}

		// create an object of the PostfixExpression class
		PostfixExpression postfix (input);

		// get the answer to the expression 
		// automatically evaluated by the PostfixExpression class
		double answer = postfix.get_answer();
		
		// print the answer only if a valid expression was entered
		// otherwise an error message is automatically displayed when trying to evaluate the expression
		if (answer != NULL){
			cout << "Output: " << answer << endl << endl;
		}
	}
}
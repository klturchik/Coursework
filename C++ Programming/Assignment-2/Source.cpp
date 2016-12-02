#include <iostream>
#include <stack>
#include <string>
#include <sstream>
#include <map>
using namespace std;


bool isDouble(string element);
bool isOperator(string element);

class PostfixCalculator {
	string expression;
	double answer;
	map<string, string> varValues;

	public:
		// mutators
		void set_expression(string input){
			expression = input;;
		}

		// accessors
		string get_expression(){
			return expression;
		}

		double get_answer(){
			return answer;
		}

		int eval(){
			stack<double> calcStack;
			/*stack<string> stringStack;*/
			string element;
			string varName;
			
			stringstream stream;
			stream << expression; // helps split the expression into multiple strings separated by spaces

			stream >> element;

			if(element == "OUT")
			{
				if(stream >> element) // get the name of the variable
				{
					varName = element;

					if(varValues.find(varName) != varValues.end()) // if the variable is found
					{ 
						cout << stod(varValues[varName]) << endl;
						stream.str(""); // clear the string
					}
					else
					{
						cout << "Error: Variable Not Found" << endl; // display error message
						stream.str(""); // clear the string
						return 0;
					}
				}
				else
				{
					cout << "Error: Invalid Input" << endl; // display error message
					stream.str(""); // clear the string
					return 0;
				}
			}

			else if(isalpha(element[0]) && expression.back() == '=') //expression must start with variable and end with =
			{
				varName = element;
				/*string previous = element;*/

				while(stream >> element) {
					// check for numeric value
					if(isDouble(element))
					{
						double num;
						num = stod(element);
						calcStack.push(num);
					}

					// find varaible value to use in expression
					else if(isalpha(element[0]))
					{
						if(varValues.find(element) != varValues.end()) // if the variable is found	
						{ 	
							double num;

							num = stod(varValues[element]);
							calcStack.push(num);
						}
						else
						{
							cout << "Error: Variable Not Found" << endl; // display error message
							stream.str(""); // clear the string
							return 0;
						}
					}

					// check for operator
					else if(isOperator(element))
					{
						string optor = element;
						double op1, op2, result;

						/*string str1, str2, strResult;*/

						// invalid input
						// if there are not two operands for the operator
						if(calcStack.empty())
						{
							cout << "Error: Invalid Input" << endl << endl; // display error message
							stream.str(""); // clear the string
							return 0; // set answer to NULL
						}

						op2 = calcStack.top();
						calcStack.pop();

						// invalid input
						// if there are not two operands for the operator
						if(calcStack.empty())
						{
							cout << "Error: Invalid Input" << endl << endl; // display error message
							stream.str(""); // clear the string
							return 0; // set answer to NULL
						}

						op1 = calcStack.top();
						calcStack.pop();

						if(optor == "-")
							result = op1 - op2;
						if(optor == "+")
						{
							result = op1 + op2;
							/*if(!calcStack.empty()){
								result = op1 + op2;
							}
							else{
								strResult = str1 + str2;
								stringStack.push(strResult);
							}*/
						}
						if(optor == "*")
							result = op1 * op2;
						if(optor == "/")
							result = op1 / op2;

						calcStack.push(result);
					}

					// assign value to variable
					else if(element == "=")
					{
						if(!calcStack.empty() /*&& stringStack.empty()*/){
							answer = calcStack.top();
							calcStack.pop();

							// invalid input
							// if the calcStack is not empty after evaluation
							if(!calcStack.empty())
							{
								cout << "Error: Invalid Input" << endl; // display error message
								stream.str(""); // clear the string
								return 0;
							}

							varValues[varName] = to_string(answer);
							return 0;
						}

						/*
						else if(calcStack.empty() && !stringStack.empty())
						{
							string stringArg;

							while(!stringStack.empty())
							{
								stringArg += stringStack.top();
								stringStack.pop();
							}

							varValues[varName] = stringArg;
							return 0;
						}

						else
						{
							cout << "Error: Invalid Input" << endl; // display error message
							stream.str(""); // clear the string
							return 0;
						}
					}

					// check for the # command
					else if(element == "#")
					{
						if(varValues.find(previous) != varValues.end()) // if the variable is found
						{
							stringStack.push(varValues[previous]);
						}
						else
						{
							cout << "Error: Invalid Input" << endl; // display error message
							stream.str(""); // clear the string
							return 0;
						}
					}

					// check for the @ command
					else if(element == "@")
					{
						stringStack.push(previous);
						calcStack.pop();
					}

					// check for string
					else if(element[0] == '\"')
					{
						element.erase(0,1);
						string stringArg;

						while(true){
							if(element.back() != '\"')
							{ 
								stringArg += element;
								stringArg += " ";

								if(stream >> element){
								}
								else{
									cout << "Error: Invalid Input" << endl; // display error message
									stream.str(""); // clear the string
									return 0;
								}
							}
							else if(element.back() == '\"')
							{
								element.erase(element.size() - 1);
								stringArg += element;
								break;
							}
						}
						stringStack.push(stringArg);
					}

					// invalid input
					// if any parts of the expression are not a valid double, operator, or variable
					else
					{
						cout << "Error: Invalid Input" << endl; // display error message
						stream.str(""); // clear the string
						return 0;
					}

					previous = element;
					*/
					}
				}
			}

			else
			{
				cout << "Error: Invalid Input" << endl; // display error message
				stream.str(""); // clear the string
				return 0;
			}
		}
};


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

bool isOperator(string element){
	if(element == "*" || element == "/" || element == "+" || element == "-"){
		return true;
	}
	else{
		return false;
	}
}

int main() {
	// introductory message
	cout << "Expression format: \"A 4 5 * 6 + 2 * 1 - 6 / 4 2 + 3 * * =\"" << endl;
	/*cout << "Use \"@\" operator to convert number to string" << endl;
	cout << "Use \"#\" operator to convert string to number" << endl;*/
	cout << "Use \"OUT\" to display the value of a variable" << endl;
	cout << "Enter \"q\" to quit" << endl << endl;
	
	// create an object of the PostfixExpression class
	PostfixCalculator calculator;

	while(true){
		string input;

		// display prompt
		cout << "Input Statement: ";

		// get input
		getline (cin, input);

			// quit the program if the user inputs "q"
			if(input[0] == 'q' || input[0] == 'Q'){
				return 0;
			}

		calculator.set_expression(input);
		calculator.eval();
	}
}
#include <iostream>
#include <string>
#include <stack>
#include <vector>
#include <sstream>
#include <fstream>
#include <map>
#include <windows.h>
using namespace std;


bool isDouble(string element);
bool isOperator(string element);

class StatementEvaluator {
	map<string, double> varList;

	public:
		int command(string input){
			ifstream in_file;
			ofstream out_file;

			stringstream stream;
			stream << input; // helps split the expression into multiple strings separated by spaces

			string element;
			stream >> element;

			// will disable other commands and allow user to start inputing expressions until SAVE is entered
			// *** this allows commands like RUN to be stored in the file without being executed automatically
			if(element == "CREATE")
			{
				if(stream >> element) // get filename
				{
					string filename = element;
					out_file.open(filename.c_str());

					vector<string> statements;
					string line;

					while(out_file.is_open())
					{
						getline(cin, line);

						if(line == "CREATE")
						{
							cout << "Error: A file is already open" << endl; // display error message
							stream.str(""); // clear the string
						}

						// save statements to the file opened by CREATE and close the file
						else if(line == "SAVE")
						{
							vector<string>::iterator pos;
							for (pos = statements.begin(); pos != statements.end(); pos++){
								out_file << *pos << endl;
							}
							out_file.close();
							return 0;	
						}

						else
						{
							statements.push_back(line); 
						}
					}
				}
				
				else // if filename not found
				{
					cout << "Error: You must enter a file name" << endl; // display error message
					stream.str(""); // clear the string
				}
			}

			// if there is no file to write to
			else if(element == "SAVE")
			{
				if(!out_file.is_open()) 
				{
					cout << "Error: No file open" << endl; // display error message
					stream.str(""); // clear the string
					return 0;
				}
			}

			// execute commands from a file
			else if(element == "RUN")
			{
				if(stream >> element) // get filename
				{
					string filename = element;
					in_file.open(filename.c_str());

					string expression;
					while(getline(in_file, expression))
					{
						eval(expression);
					}

					in_file.close();
					return 0;
				}

				else // if filename not found
				{
					cout << "Error: You must enter a file name" << endl; // display error message
					stream.str(""); // clear the string
					return 0;
				}
			}

			// display the names of all files available for execution;
			else if(element == "DIR")
			{
				WIN32_FIND_DATA search_data;

				memset(&search_data, 0, sizeof(WIN32_FIND_DATA));

				HANDLE handle = FindFirstFile("*.txt", &search_data);
  
				while(handle != INVALID_HANDLE_VALUE)
				{
					printf("%s\r\n", search_data.cFileName);
    
					if(FindNextFile(handle, &search_data) == FALSE)
					break;
				}

				handle = FindFirstFile("*.pst", &search_data);
  
				while(handle != INVALID_HANDLE_VALUE)
				{
					printf("%s\r\n", search_data.cFileName);
    
					if(FindNextFile(handle, &search_data) == FALSE)
					break;
				}

				return 0;
			}

			// display all variables stored in map along with their values
			else if(element == "VARLIST")
			{
				map<string, double>::iterator pos;
				for (pos = varList.begin(); pos != varList.end(); pos++){
					 cout << pos->first << " = " << pos->second << endl;
				}
				return 0;
			}

			// output the value of a single variable stored in map
			if(element == "OUT")
			{
				if(stream >> element) // get the name of the variable
				{
					string varName = element;

					if(varList.find(varName) != varList.end()) // if the variable is found
					{ 
						cout << varList[varName] << endl;
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

			else // if variable is not found
			{
				cout << "Error: Invalid Command" << endl; // display error message
				stream.str(""); // clear the string
				return 0;
			}
		}

	private:
		int eval(string expression){
			stack<double> calcStack;
			string element;
			string varName;
			double answer;
			
			stringstream stream;
			stream << expression; // helps split the expression into multiple strings separated by spaces

			stream >> element;

			// if the statement is actually a command
			if(element == "CREATE" || element == "SAVE" || element == "RUN" || element == "DIR" || 
				element == "VARLIST" || element == "OUT")
			{
				command(expression);
			}

			// evaluate the expression and store the variable in map
			else if(isalpha(element[0]) && expression.back() == '=') // assignment expression must start with variable and end with =
			{
				varName = element;

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
						if(varList.find(element) != varList.end()) // if the variable is found	
						{ 	
							double num;

							num = varList[element];
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
							result = op1 + op2;
						if(optor == "*")
							result = op1 * op2;
						if(optor == "/")
							result = op1 / op2;

						calcStack.push(result);
					}

					// assign value to variable
					else if(element == "=")
					{
						if(!calcStack.empty()){
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

							varList[varName] = answer;
							return 0;
						}
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
	cout << "Enter \"CREATE (file name)\" to open a file for writing" << endl;
	cout << "Enter \"SAVE\" to save statements to the file opened by CREATE" << endl;
	cout << "Enter \"RUN (file name)\" to execute commands from a file" << endl;
	cout << "Enter \"DIR\" to display names of all files available for execution" << endl;
	cout << "Enter \"VARLIST\" to display all variables along with their values" << endl;
	cout << "Enter \"OUT (variable name)\" to display the value of a single variable" << endl;
	cout << "Enter \"QUIT\" to quit" << endl << endl;
	
	// create an object of the PostfixExpression class
	StatementEvaluator stmtEval;

	while(true){
		string input;

		// get input
		getline(cin, input);

			// quit the program if the user inputs "q"
			if(input[0] == 'q' || input[0] == 'Q'){
				return 0;
			}

		stmtEval.command(input);
	}
}
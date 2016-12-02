import java.util.ArrayList;

/**
 * 	Created by klturchik on 10/17/16.
 */

/**
	This class extends the Question class and contains the answer for a Single
	Choice question format	
*/
public class QuestionSingleChoice extends Question {
    private ArrayList<Boolean> answer; 	//TODO make single answer int instead of table

    /**
    CONSTRUCTOR
    
    @param question - a String containing the question's prompt  
    @param options - an ArrayList of Strings containing all possible answers
    to the question	
    @param answers - an ArrayList of Boolean values containing which answer 
    to the question is correct and which are otherwise incorrect
    */
    public QuestionSingleChoice(String question, ArrayList<String> options, ArrayList<Boolean> answer) {
        super(question, options);
        this.answer = answer;
    }

    /**
    @return An ArrayList of Boolean values containing which answer to the 
    question is correct and which are otherwise incorrect
    */
    public ArrayList<Boolean> getAnswer() {
        return answer;
    }

    /**
    Used to edit the answer key to the question
    
    @param answers - an ArrayList of Boolean values containing which answer 
    to the question is correct and which are otherwise incorrect
    */
    public void setAnswer(ArrayList<Boolean> answer) {
        this.answer = answer;
    }
    
    /**
    Prints the question's answer key to the terminal
    */
    public void displayAnswer(){
        for(int i = 0; i < answer.size(); i++){
            System.out.println(i + 1 + ": " + answer.get(i));
        }
    }
}

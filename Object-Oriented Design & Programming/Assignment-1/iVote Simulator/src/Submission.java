import java.util.ArrayList;

/**
 * 	Created by klturchik on 10/17/16.
 */
//TODO Divide into two separate classes for Single and Multiple Choice 

/**
	This class creates a single submission of answers to a question in the form of
	an ArrayList with Boolean values for each answer(s) a Student thinks are correct 
	or incorrect.
*/
public class Submission{
    private ArrayList<Boolean> answer;

    /**
    CONSTRUCTOR
    
    @param An ArrayList with Boolean values for each answer(s) a Student thinks 
    are correct or incorrect.
    */
    public Submission(ArrayList<Boolean> answer) {
        this.answer = answer;
    }
    
    /**
    @return An ArrayList with Boolean values for each answer(s) a Student thinks 
    are correct or incorrect.
    */
    public ArrayList<Boolean> getAnswer() {
        return answer;
    }
    
    /**
    Generates or changes the student's answer(s) to the question
    
    @param submission - The student's answer(s) of answers
    */
    public ArrayList<Boolean> setAnswer() {
        return answer;
    }
}

import java.util.ArrayList;

/**
 * 	Created by klturchik on 10/17/16.
 */

/**
	This class extends the Question class and contains the answer for a Multiple
	Choice question format	
*/
public class QuestionMultChoice extends Question {
    private ArrayList<Boolean> answers;

    /**
    CONSTRUCTOR
    
    @param question - a String containing the question's prompt  
    @param options - an ArrayList of Strings containing all possible answers
    to the question	
    @param answers - an ArrayList of Boolean values containing which answers 
    to the question are correct and incorrect
    */
    public QuestionMultChoice(String question, ArrayList<String> options, ArrayList<Boolean> answers) {
        super(question, options);
        this.answers = answers;
    }

    /**
    @return An ArrayList of Boolean values containing which answers to the 
    question are correct and incorrect
    */
    public ArrayList<Boolean> getAnswer() {
        return answers;
    }

    /**
    Used to edit the answer key to the question
    
    @param answers - an ArrayList of Boolean values containing which answers 
    to the question are correct and incorrect
    */
    public void setAnswer(ArrayList<Boolean> answers) {
        this.answers = answers;
    }
    
    /**
    Prints the question's answer key to the terminal
    */
    public void displayAnswer(){
        for(int i = 0; i < answers.size(); i++){
            System.out.println(i + 1 + ": " + answers.get(i));
        }
    }
}

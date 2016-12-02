import java.util.ArrayList;

/**
 * 	Created by klturchik on 10/17/16.
 */

/**
	This abstract class contains methods shared by both Single and Multiple 
	Choice questions and simplifies the calling of methods from instances of
	both question types by allowing their methods to be called from one class.
	This class also contains the variables for the question's prompt and 
	possible answers, which are also shared by both question types. 		
*/
public abstract class Question {
    private String question;
    private ArrayList<String> options;

    /**
    CONSTRUCTOR
    
    @param question - a String containing the question's prompt  
    @param options - an ArrayList of Strings containing all possible answers
    to the question		
    */
    public Question (String question, ArrayList<String> options){
        this.question = question;
        this.options = options;
    }

    /**
    @return A String containing the question's prompt
    */
    public String getQuestion() {
        return question;
    }
    
    /**
    Used to edit the question's prompt
    
    @param question - a String containing the question's prompt 
    */
    public void setQuestion(String question) {
        this.question = question;
    }

    /**
    Prints the question's prompt to the terminal
    */
    public void displayQuestion(){
        System.out.println(question);
    }
    
    /**
    @return an ArrayList of Strings containing all possible answers
    to the question		
    */
    public ArrayList<String> getOptions() {
        return options;
    }
    
    /**
    Used to edit the question's potential answers
    
    @param options - an ArrayList of Strings containing all possible answers
    to the question		
    */
    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }
    
    /**
    Prints the question's potential answers to the terminal
    */
    public void displayOptions(){
        for(int i = 0; i < options.size(); i++){
            System.out.println(i + 1 + ": " + options.get(i));
        }
    }
    
    public abstract ArrayList<Boolean> getAnswer();
	public abstract void setAnswer(ArrayList<Boolean> answer);
    public abstract void displayAnswer();
}

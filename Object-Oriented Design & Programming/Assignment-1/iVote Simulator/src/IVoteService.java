import java.util.ArrayList;
import java.util.HashMap;

/**
 * 	Created by klturchik on 10/17/16.
 */

/**
	The iVote Service class tracks the configuration of a question and the 
	submissions of several students. It provides methods to retrieve information 
	about a question or students and their submissions. It can also calculate 
	statistics about submissions from students.
*/
public class IVoteService {
    private Question question;
    private ArrayList<Student> students;
    private int correct;
    private int incorrect;
    private HashMap<String, Integer> stats = new HashMap<String, Integer>();

    /**
    CONSTRUCTOR

    @param question - an instance of a Question consisting of a prompt, possible
    answers, and a correct answer(s)  	
    */
    public IVoteService(Question question) {
        this.question = question;
    }

    /**
   	This class runs most of the iVote Service's display methods, printing the
   	question's prompt, possible answers, and statistics on what answers
   	students have input. This makes it convenient to call all these methods
   	at once using only one method.
    */
    public void run(){
        displayQuestion();
        displayOptions(); 
        displayStats();  
    }
    
    /**
    Compares a submission from one of the students to the question's actual
    correct answer(s). If the answers match the correct counter is incremented
    by 1, otherwise the incorrect counter is incremented by 1.
    
    @param submission - a submission from one of the students
    */
    public void checkCorrectness(Submission submission){
    	if(submission.getAnswer().equals(question.getAnswer())){
    		correct++;
    	} else{
    		incorrect++;
    	};
    }
    
    /**
    Instantiates an ArrayList of instances of the Student class, allowing
    the iVote Service to begin reading submissions from these students
    
    @param students - an ArrayList unique Students
    */
	public void setStudents(ArrayList<Student> students){
		this.students = students;
    }
    
    /**
    Prints the question's prompt to the terminal
    */
    public void displayQuestion(){
    	question.displayQuestion();
    }

    /**
    Prints the question's potential answers to the terminal
    */
	public void displayOptions(){
        question.displayOptions();
    }
    
    /**
    Prints the question's answer key to the terminal
    */
    public void displayAnswer(){
    	question.displayAnswer();
	}
    
    /**
    Determines the amount of correct and incorrect answers, as well as how many 
    students submitted each answer, and prints this information to the terminal
	*/
    public void displayStats(){
		Submission submission;
	    correct = 0;
	    incorrect = 0;
	    stats = new HashMap<String, Integer>();
	    
		for (int i = 0; i < question.getOptions().size(); i++){
        	stats.put(question.getOptions().get(i), 0);
		}

    	if(question instanceof QuestionMultChoice){
        	for(int i = 0; i < students.size(); i++){
        		submission = students.get(i).getSubmission();
        		checkCorrectness(submission);
        		
        		for(int j = 0; j < submission.getAnswer().size(); j++){
        			if(submission.getAnswer().get(j) == true){
					    int count = stats.containsKey(question.getOptions().get(j)) 
					    		? stats.get(question.getOptions().get(j)) : 0;
					    stats.put(question.getOptions().get(j), count + 1);
        			}
        		}
        	}
        }
        
        if(question instanceof QuestionSingleChoice){
        	for(int i = 0; i < students.size(); i++){
        		submission = students.get(i).getSubmission(); 
        		checkCorrectness(submission);
        		
        		for(int j = 0; j < submission.getAnswer().size(); j++){
        			if(submission.getAnswer().get(j) == true){
					    int count = stats.containsKey(question.getOptions().get(j)) 
					    		? stats.get(question.getOptions().get(j)) : 0;
					    stats.put(question.getOptions().get(j), count + 1);
        			}
        		}
        	}   	
        }
    	
        System.out.println(correct + " correct answers.");     
        System.out.println(incorrect + " incorrect answers.");   
        System.out.println(stats);
    }
}
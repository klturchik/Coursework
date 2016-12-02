import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * 	Kyle Turchik
 * 	CS356-01
 * 	10/17/16
 */

/**
	This class includes a main method which configures several test questions to
	demonstrate the functionality of the iVote Service and other classes.		
*/
public class SimulationDriver {
	
    public static void main(String[] args){
    	//1.) Declare the necessary classes and variables
    	String prompt;
    	ArrayList<String> options;
    	ArrayList<Boolean> answer;
    	Question question;
        IVoteService iVoteService;
    	
        Random random = new Random();
    	ArrayList<Student> students;
    	
        //2.) Generate an ArrayList with a random number of students
        int classSize = random.nextInt(30) + 20;
    	students = new ArrayList<>();
        for (int i = 0; i < classSize; i++) {
            Student student = new Student(i);
            students.add(student);
        }
        
        //3.) Configure the prompt, options, and answer of a question and use them to
		//instantiate a new "Single Choice Question"
        System.out.println("//Single Choice Question");
    	prompt = "Which of these answers is correct? (Single Choice)";
        options = new ArrayList<>(
                Arrays.asList(new String[]{
                        "A",
                        "B",
                        "C",
                        "D",
                        "E",
                })
        );
        answer = new ArrayList<>(
                Arrays.asList(new Boolean[]{
                        false,
                        true,
                        false,
                        false,   
                        false, 
                })
        );
        question = new QuestionSingleChoice(prompt, options, answer);
        iVoteService = new IVoteService(question);
        
        //4.) Generate a random single choice answer for each student in the ArrayList
        for (int i = 0; i < classSize; i++) {
        	Submission submission = new Submission(randomSingleAnswer(answer.size()));
            students.get(i).setSubmission(submission);
        }
        iVoteService.setStudents(students);
        
        //5.) Run the iVote Service
        iVoteService.run();
        System.out.println("-------------------------------\n");

        
        //6.) Repeat steps 3-5 for a "Multiple Choice Question"
        System.out.println("//Multiple Choice Question");
        prompt = "Which of these answers is correct? (Multiple Choice)";
        options = new ArrayList<>(
                Arrays.asList(new String[]{
                        "A",
                        "B",
                        "C",
                })
        );
        answer = new ArrayList<>(
                Arrays.asList(new Boolean[]{
                        true,
                        true,
                        false
                })
        );
        question = new QuestionMultChoice(prompt, options, answer);
        iVoteService = new IVoteService(question);
        
        //Generate Random Answers
        for (int i = 0; i < classSize; i++) {
        	Submission submission = new Submission(randomMultipleAnswer(answer.size()));
            students.get(i).setSubmission(submission);
        }
        iVoteService.setStudents(students);
        
        //Run iVote
        iVoteService.run();
        System.out.println("-------------------------------\n");      
        
        
        //7.) Repeat step 3-5 again changing one thing at a time and running the iVote
		//Service after each change
        System.out.println("//Step-By-Step Scenario");
        //7a.) Change the question's prompt
        System.out.println("//Instructor changes the prompt");
    	prompt = "Which of these cities is in California? (Multiple Choice)";
        question.setQuestion(prompt);
        iVoteService.run();
        
 		//7b.) Change the question's options
        System.out.println("\n//Instructor changes the options");
        options = new ArrayList<>(
                Arrays.asList(new String[]{
                        "Los Angeles",
                        "Irvine",
                        "Berlin",
                        "Sacramento",
                        "London"
                })
        );
        question.setOptions(options);
        iVoteService.run();
        
 		//7c.) Change the questions's answer key
        System.out.println("\n//Instructor changes the answer key");
        answer = new ArrayList<>(
                Arrays.asList(new Boolean[]{
                        true,
                        true,
                        false,
                        true,
                        false,
                })
        );
        question.setAnswer(answer);
        iVoteService.displayAnswer();
        
		//7d.) Generate a new random multiple choice answer for each student in the ArrayList.
        System.out.println("\n//Students resubmit with random answer");
        for (int i = 0; i < classSize; i++) {
        	Submission submission = new Submission(randomMultipleAnswer(answer.size()));
            students.get(i).setSubmission(submission);
        }
        iVoteService.setStudents(students);
        iVoteService.run();
        
		//7e.) Change each student's answer to the correct answer.
        System.out.println("\n//Students resubmit with correct answer");
        for (int i = 0; i < classSize; i++) {
        	Submission submission = new Submission(answer);
            students.get(i).setSubmission(submission);
        }
        iVoteService.run();
    }
    
    /**
    This method generates a random single choice answer
    
   	@param size - how many options there are in this question
    @return an ArrayList of Booleans with only one member set to true at random
     */
	public static ArrayList<Boolean> randomSingleAnswer(int size){
		ArrayList<Boolean> answers = new ArrayList<>();
	    Random random = new Random();

	    int j = random.nextInt(size);
	    for(int i = 0; i < size; i++){
	        if (i == j){
	        	answers.add(true);
	        } else {
	        	answers.add(false);
	        }
	    }
	    return new ArrayList<Boolean>(answers);
	}
       
	/**
    This method generates a random multiple choice answer
    
   	@param size - how many options there are in this question
    @return an ArrayList of Booleans with each member set to true or false at random
     */
    public static ArrayList<Boolean> randomMultipleAnswer(int size){
    	ArrayList<Boolean> answers = new ArrayList<>();
        Random random = new Random();
        
        for(int i = 0; i < size; i++){
            answers.add(random.nextBoolean());
        }
        return new ArrayList<Boolean>(answers);
    }
}
        



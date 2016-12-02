
/**
 * 	Created by klturchik on 10/17/16.
 */

/**
	This class creates a Student object. Each student has a unique ID and can 
	have single submission. This class contains methods to retrieve each student's
	ID and to set/change or retrieve each student's submission.
*/
public class Student {
    private int id;
    private Submission submission;
    
    /**
    CONSTRUCTOR
    This constructor takes in a student's id so we can tell the different instances
    of Student apart
    
    @param id - The student's unique ID number
    */
    public Student(int id){
		this.id = id;
    }
    
    /*public Student(int id, Submission submission){
		this.id = id;
		setSubmission(submission);
    }*/

    /**
    @return int - The student's unique ID number
    */
    public int getId() {
		return id;
	}
    
    /**
    @return Submission - the student's answer(s) to the question
    */
    public Submission getSubmission(){
		return submission;
    }
    
    /**
    Generates or replaces the student's submission to the question 
    
    @param Submission - An instance of the Submission class containing the 
    student's answer(s)
    */
    public void setSubmission(Submission submission){
		this.submission = submission;
    }
}

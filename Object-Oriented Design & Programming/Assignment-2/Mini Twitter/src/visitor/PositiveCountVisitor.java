/**
 * 	Created by klturchik on 11/6/16.
 */
package visitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ui.Group;
import ui.User;

public class PositiveCountVisitor implements Visitor {
    
    public int count; 
    
    //Messages contianing these words count as positive messages
    private final List<String> positiveWords = Arrays.asList( 
			"good", 
			"great", 
			"excellent", 
			"fantastic",
			"amazing",
			"wonderful",
			"nice", 
			"awesome", 
			"happy",  
			"love",
			"sweet"
			);
    /**
	Counts the number of "positive" messages posted by this User and adds that
	count to the count of all total messages.
    */
    @Override
    public void visitUser(User e) {
        //System.out.println("visitUser: " + e.getName());
        countPositive(e.getMessages());
    } 

    
    /**
    ...
    */
    @Override
    public void visitGroup(Group e) {
        //System.out.println("visitGroup: " + e.getName());
    }
    
    /**
	Checks all messages by this user and determines if they contain a positive
	word, if so the count is incremented by one.
    */
	private void countPositive(ArrayList<String> messages){
        for (String message : messages){
            String temp = message.toLowerCase();   
            for (String word : temp.split(" ")){
                if (positiveWords.contains(word)){
                    count++;
                }
            }  
        }
    }
}

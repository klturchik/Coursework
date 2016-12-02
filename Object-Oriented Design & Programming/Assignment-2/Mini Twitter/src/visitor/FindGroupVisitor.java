/**
 * 	Created by klturchik on 11/6/16.
 */
package visitor;

import ui.Group;
import ui.User;

public class FindGroupVisitor implements Visitor {
    
    public Group result;
    private String id;
       
    /**
	CONSTRUCTOR
	@param targetID - the id of the Group to search for
    */
    public FindGroupVisitor(String targetID) {
        id = targetID.toLowerCase();
    }

    /**
    ...
    */
    @Override
    public void visitUser(User e) {
        //System.out.println("visitUser: " + e.getName());
    }

    
    /**
	Determines if this Group 'e' has id equal to the search id. 
	If so, set result of this visitor equal to the Group 'e'.
    */
    @Override
    public void visitGroup(Group e) {
        //System.out.println("visitGroup: " + e.getName());
        if (e.getName().toLowerCase().equals(id)){
            //System.out.println("found: " + e.getName());
            result = e;
        }
    }  
}

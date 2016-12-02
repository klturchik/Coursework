/**
 * 	Created by klturchik on 11/6/16.
 */
package visitor;

import ui.Group;
import ui.User;

public class FindUserVisitor implements Visitor {
    
    public User result;
    private String id;
    
    /**
	CONSTRUCTOR
	@param targetID - the id of the User to search for
    */
    public FindUserVisitor(String targetID) {
        id = targetID.toLowerCase();
    }

    /**
	Determines if this user 'e' has id equal to the search id. 
	If so, set result of this visitor equal to the User 'e'.
    */
    @Override
    public void visitUser(User e) {
        //System.out.println("visitUser: " + e.getName());
        if (e.getName().toLowerCase().equals(id)){
            //System.out.println("found: " + e.getName());
            result = e;
        }
    }

    /**
    ...
    */
    @Override
    public void visitGroup(Group e) {
        //System.out.println("visitGroup: " + e.getName());
    } 
}

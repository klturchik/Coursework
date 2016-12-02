/**
 * 	Created by klturchik on 11/6/16.
 */
package visitor;

import ui.Group;
import ui.User;

public class UserCountVisitor implements Visitor {
    
    public int count;

    /**
	Adds this user to the count of total users
    */
    @Override
    public void visitUser(User e) {
        //System.out.println("visitUser: " + e.getName());
        count++;
    }

    /**
    ...
    */
    @Override
    public void visitGroup(Group e) {
        //System.out.println("visitGroup: " + e.getName());
    }
    
}

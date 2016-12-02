/**
 * 	Created by klturchik on 11/6/16.
 */
package visitor;

import ui.Group;
import ui.User;

public class GroupCountVisitor implements Visitor {
    
    public int count;
    
    /**
    ...
    */
    @Override
    public void visitUser(User e) {
        //System.out.println("visitUser: " + e.getName());
    }

    /**
	Adds this group to the count of total groups
    */
    @Override
    public void visitGroup(Group e) {
        //System.out.println("visitGroup: " + e.getName());
        count++;
    }
    
}

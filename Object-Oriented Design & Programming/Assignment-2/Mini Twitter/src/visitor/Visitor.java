/**
 * 	Created by klturchik on 11/6/16.
 */
package visitor;

import ui.Group;
import ui.User;

public interface Visitor {
    
    /**
	Visits a User component.
    */
    public void visitUser(User e);
    
    /**
	Visits a Group component.
    */
    public void visitGroup(Group e);
}

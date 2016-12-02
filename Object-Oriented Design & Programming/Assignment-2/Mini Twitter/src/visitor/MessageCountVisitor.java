/**
 * 	Created by klturchik on 11/6/16.
 */
package visitor;

import ui.Group;
import ui.User;

public class MessageCountVisitor implements Visitor {
    
    public int count;

    /**
	Counts the number of messages posted by this User and adds that
	count to the count of all total messages.
    */
    @Override
    public void visitUser(User e) {
        //System.out.println("visitUser: " + e.getName());
        count+= e.getMessages().size();
    }

    /**
    ...
    */
    @Override
    public void visitGroup(Group e) {
        //System.out.println("visitGroup: " + e.getName());
    }
    
}

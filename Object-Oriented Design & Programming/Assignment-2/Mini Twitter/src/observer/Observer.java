/**
 * 	Created by klturchik on 11/6/16.
 */
package observer;

public interface Observer {
    
    /**
	Updates this observer with information about the Subject.
    */
    public void update(Subject s);
}
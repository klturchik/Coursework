/**
 * 	Created by klturchik on 11/6/16.
 */
package observer;

import java.util.ArrayList;

public abstract class Subject {
    
    private ArrayList<Observer> observers = new ArrayList<Observer>(); 	
    
    /**
	Attaches an observer to this instance of Subject.  The observer will 
	keep track of information about this Subject.
    */
    public void attach(Observer o) {
        observers.add(o);
    }
    
    /**
	Detaches an observer from this instance of Subject.  The observer will 
	stop tracking information about this Subject.
     */
    public void detach(Observer o) {
        observers.remove(o);
    }
    
    /**
   	Calls update on all of this Subject's observers to receive information 
   	from this Subject.
    */
    protected void notifyObservers() {
        for (Observer observer : observers){
            observer.update(this);
        }
    }
}

/**
 * 	Created by klturchik on 11/6/16.
 */ 
package ui;

import visitor.Visitor;

public interface Component {
    
    /**
	Adds a component as a child of this component.
    */
    public void add(Component c);
    
    /**
	@return The id of this component.
    */
    public String getName();
    
    /**
	@return A child of this component with an index matching the parameter.
    */
    public Component getChild(int i);
    
    /**
	@return The total number of children this component has.
    */
    public int getChildCount();
    
    /**
	@return The index of the parameter component in this component's children.
    */
    public int getIndexOfChild(Component c);
    
    /**
	Passes itself to the visitor and allows it to carry out its operation.
    */
    public void accept(Visitor v);
    
    /**
	Calls the method to set the userUI for this component to visible.
    */
    public void openUserView();
}

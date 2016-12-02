/**
 * 	Created by klturchik on 11/6/16.
 */
package ui;

import visitor.Visitor;

import java.util.ArrayList;

public class Group implements Component{
    
    private String id;
    private ArrayList<Component> childList;

    /**
	CONSTRUCTOR
	@param	id - a string containing this group's id
    */
    public Group(String id) {
        this.id = id;
        childList = new ArrayList<>();
    }
    
    /**
	@return Returns this Group's id as a String.  Needed to display id in JTree.
    */
    public String toString() {
        return id;
    }   

    /**
	Adds a component as a child of this component.
    */
    @Override
    public void add(Component c) {
        childList.add(c);
    }
    
    /**
	@return The id of this component.
    */
    @Override
    public String getName() {
        return id;
    }

    /**
	@return A child of this component with an index matching the parameter.
    */
    @Override
    public Component getChild(int i) {
        return childList.get(i);
    }

    /**
	@return The total number of children this component has.
    */
    @Override
    public int getChildCount() {
        return childList.size();
    }
    
    /**
	@return The index of the component parameter in this component's children.
    */
    @Override
    public int getIndexOfChild(Component c) {
        return childList.indexOf(c);
    }
    
    /**
	Passes itself to the visitor and allows it to carry out its operation.
    */
    @Override
    public void accept(Visitor v) {
        v.visitGroup(this);
        for (Component c : childList){
            c.accept(v);
        }
    }

    /**
	Calls the method to set the userUI for this component to visible.
    */
    @Override
    public void openUserView() {
    	// TODO Auto-generated method stub
    }
}

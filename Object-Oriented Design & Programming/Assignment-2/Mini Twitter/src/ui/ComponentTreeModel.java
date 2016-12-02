/**
 * 	Created by klturchik on 11/6/16.
 */
package ui;

import ui.Component;
import visitor.FindUserVisitor;
import visitor.FindGroupVisitor;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.*;

/**
Implementation of TreeModel where nodes can be instances of the Component class.
*/
public class ComponentTreeModel implements TreeModel {
    
    private Component root;							//root of the tree
    private List<TreeModelListener> listenerList;	//listeners
    
    /**
    CONSTRUCTOR
	@param	root - the root node of the tree model
    */
    public ComponentTreeModel(Component root) {
        this.root = root;
        listenerList = new ArrayList<>();
    }
    
    /**
	Adds the given user user component 'c' as a child of 'parent'.
    */
    public void addUserComponent(Component parent, Component c) {
        parent.add(c);
        Object[] o = {root};
        TreeModelEvent e = new TreeModelEvent(this, o);
        for(TreeModelListener l : listenerList){
            l.treeStructureChanged(e);
        }
    }
    
    /**
    @return The User with the given 'id' in component 'start' or descendants of start.
    */
    public Component getUserFromID(Component start, String id) {
        FindUserVisitor v = new FindUserVisitor(id);
        start.accept(v);
        return v.result;
    }
    
    /**
    @return The Group with the given 'id' in component 'start' or descendants of start.
    */
    public Component getGroupFromID(Component start, String id) {
        FindGroupVisitor v = new FindGroupVisitor(id);
        start.accept(v);
        return v.result;
    }

    /**
	Adds a listener for the TreeModelEvent posted after the tree changes.
    */
    @Override
    public void addTreeModelListener(TreeModelListener l) {
        listenerList.add(l);
    }
    
    /**
	@return The child of the 'parent' at 'index' in the parent's child array
    */
    @Override
    public Object getChild(Object parent, int index) {
        return ((Component) parent).getChild(index);
    }

    /**
    @return The number of children of 'parent'.
    */
    @Override
    public int getChildCount(Object parent) {
        return ((Component) parent).getChildCount();
    }
    
    /**
    @return The index of 'child' in 'parent'.
    */
    @Override
    public int getIndexOfChild(Object parent, Object child) {
        return ((Component) parent).getIndexOfChild((Component) child);
    }
    
    /**
    @return The root of the tree.
    */
    @Override
    public Object getRoot() {
        return root;
    }

    /**
    @return Whether the specified node is a leaf node.
    */
    @Override
    public boolean isLeaf(Object node) {
        return (((Component) node).getChildCount() == 0);
    }

    /**
	Removes a listener previously added with addTreeModelListener(
    */
    @Override
    public void removeTreeModelListener(TreeModelListener l) {
        listenerList.remove(l);
    }  
    
    /**
	Sets the user object of the TreeNode identified by path and posts a node changed.
    */
    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
    	// TODO Auto-generated method stub
    }
}

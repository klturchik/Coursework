/**
 * 	Created by klturchik on 11/6/16.
 */
package ui;

import observer.Observer;
import observer.Subject;
import visitor.Visitor;

import java.util.ArrayList;

public class User extends Subject implements Observer, Component{
    private UserUI userUI;	
    private ComponentTreeModel treeModel;	
    
    private String id;	
    private ArrayList<User> followerList;	
    private ArrayList<String> messageList;	
    private ArrayList<String> newsFeed;	
    
    /**
	CONSTRUCTOR
    @param	treeModel - the tree model this user belongs to
	@param	id - a string containing this user's id
    */
    public User(ComponentTreeModel treeModel, String id) {
        this.treeModel = treeModel;
        this.id = id;
        userUI = new UserUI(this);
        followerList = new ArrayList<User>();
        messageList = new ArrayList<String>();
        newsFeed = new ArrayList<String>();

        userUI.initialize();
        attach(this);
        followerList.add(this);
    }
    
    /**
	Updates this User with information about the Users it is following.  Messages posted by users
	who are being followed will be displayed in this user's feed.
    */
    public void update(Subject subject) {
        if (subject instanceof User) {
            postToFeed(((User) subject).getName() + ": " + ((User) subject).getRecentMessage());
        }
    }
    
    /**
	@return The message most recently posted by this User.
    */
    public String getRecentMessage() {
        return messageList.get(messageList.size() - 1);
    }
    
    /**
	@return Then ArrayList of messages posted by this User.
    */
    public ArrayList<String> getMessages() {
        return messageList;
    }
    
    /**
	Sets this user to follow the User given by the id parameter by adding the user to the
	ArrayList of users being followed and attaching itself as an observer of the user.  If
	successful it returns an empty String, if unsuccessful it returns an error message.
    */
    public String followUser(String id) {
        User user = (User) treeModel.getUserFromID((Component) treeModel.getRoot(), id);
        
        if (user != null && !followerList.contains(user)){
            user.attach(this);
            followerList.add(user);
            return "";
        }
        else if (user == null){
            return "Error: User not found";
        }
        else if (followerList.contains(user)){
            return "Error: Already following user";
        }
        else {
            return "Error: Unable to follow user";
        }
    }
    
    /**
	Posts the message contained in the message parameter. Notifies the observers
	of the message so that it can be displayed in their newsFeed.
    */
    public void postMessage(String message) {
        messageList.add(message);
        notifyObservers();
    }
    
    /**
	Adds the message contained in the message parameter to the users news feed
	allowing it to be displayed in the user's UI menu.
    */
    public void postToFeed(String message) {
        newsFeed.add(message);
        userUI.addMessageToFeed(message);
    }
    
    /**
	@return Returns this User's id as a String.  Needed to display id in JTree.
    */
    public String toString() {
        return id;
    }

    /**
	Adds a component as a child of this component.
    */
    @Override
    public void add(Component c) {
    	// TODO Auto-generated method stub
    }
    
    /**
	@return The id of this component.
    */
    @Override
    public String getName() {
        return id;
    }

    /**
	@return  A child of this component with an index matching the parameter.
    */
    @Override
    public Component getChild(int i) {
    	// TODO Auto-generated method stub
        return null;
    }
    
    /**
	@return The total number of children this component has.
    */
    @Override
    public int getChildCount() {
    	// TODO Auto-generated method stub
        return 0;
    }

    /**
	@return The index of the parameter component in this component's children.
    */
    @Override
    public int getIndexOfChild(Component c) {
    	// TODO Auto-generated method stub
        return -1;
    }

    /**
	Passes itself to the visitor and allows it to carry out its operation.
    */
    @Override
    public void accept(Visitor v) {
        v.visitUser(this);
    }   
    
    /**
	Calls the method to set the userUI for this component to visible.
    */
    @Override
    public void openUserView() {
        userUI.setVisible(true);
    }
}

/**
 * 	Created by klturchik on 11/6/16.
 */
package ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class UserUI implements ActionListener {
    
    private User user;
	private JFrame frame;
	
    private JButton btnFollowUser;
    private JButton btnPostMessage;
    
    private JScrollPane followingScrollPane;
    private DefaultListModel<String> followingListModel;
    private JList<String> followingList;
    
    private JScrollPane newsFeedScrollPane;
    private DefaultListModel<String> newsFeedListModel;
    private JList<String> newsFeedList;
    
    private JLabel terminal;
    private JLabel userID;
    private JTextField txtFollowUserID;
    private JTextField txtMessage;
    
    /**
    CONSTRUCTOR 
    @param	user - The user this UI menu belongs to
    */
    public UserUI(User user) {
    	frame = new JFrame();
        this.user = user;
    }

	/**
	Sets the UserUI for this user to visible so it can be interacted with
	*/
    public void setVisible(Boolean e) {  
    	frame.setVisible(e);
    }
    
    /**
	Creates the GUI frame and components for users.
    */
    public void initialize() {

		//CREATE FRAME
    	frame.setTitle("User Menu");
		frame.setBounds(100, 100, 400, 500);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		//TERMINAL
		terminal = new JLabel("Welcome!");
		terminal.setBounds(10, 10, 360, 35);
		terminal.setFont(new Font("SANS_SERIF", Font.PLAIN, 20));
		terminal.setHorizontalAlignment(SwingConstants.LEFT);
		frame.getContentPane().add(terminal);
		
		//TEXT BOX - USER ID
		userID = new JLabel(user.getName());
		userID.setBounds(10, 10, 360, 35);
		userID.setFont(new Font("SANS_SERIF", Font.PLAIN, 20));
		userID.setHorizontalAlignment(SwingConstants.RIGHT);
		frame.getContentPane().add(userID);
		
		//TEXT BOX - FOLLOW ID
		txtFollowUserID = new JTextField();
		txtFollowUserID.setFont(new Font("SANS_SERIF", Font.PLAIN, 20));
		txtFollowUserID.setBounds(10, 50, 185, 35);
		frame.getContentPane().add(txtFollowUserID);
		
		//BUTTON - FOLLOW USER
		btnFollowUser = new JButton("Follow User");
		btnFollowUser.setFont(new Font("SANS_SERIF", Font.PLAIN, 20));
		btnFollowUser.setBounds(210, 50, 160, 35);
		frame.getContentPane().add(btnFollowUser);
		btnFollowUser.addActionListener(this);
		
		//LIST - USERS FOLLOWED
        followingListModel = new DefaultListModel<String>();
        followingList = new JList<String>(followingListModel);
        
        followingScrollPane = new JScrollPane(followingList);
        followingScrollPane.setBounds(10, 100, 360, 140);
		frame.getContentPane().add(followingScrollPane);
		
		// TEXT BOX - MESSAGE
		txtMessage = new JTextField();
		txtMessage.setFont(new Font("SANS_SERIF", Font.PLAIN, 20));
		txtMessage.setBounds(10, 260, 185, 35);
		frame.getContentPane().add(txtMessage);
		
		//BUTTON - POST MESSAGE 
		btnPostMessage = new JButton("Post Tweet");
		btnPostMessage.setFont(new Font("SANS_SERIF", Font.PLAIN, 20));
		btnPostMessage.setBounds(210, 260, 160, 35);
		frame.getContentPane().add(btnPostMessage);
		btnPostMessage.addActionListener(this);
		
		//LIST - NEWS FEED  
        newsFeedListModel = new DefaultListModel<String>();
        newsFeedList = new JList<String>(newsFeedListModel);
        
        newsFeedScrollPane = new JScrollPane(newsFeedList);
        newsFeedScrollPane.setBounds(10, 310, 360, 140);
		frame.getContentPane().add(newsFeedScrollPane);
    }

    /**
	Whenever a button is pressed this method is called to run the method
	corresponding to that button.
    */
     @Override
     public void actionPerformed(ActionEvent event) {
    	 switch (event.getActionCommand()) {
			case "Post Tweet":
	        	 postMessage();
	        	 break;
			case "Follow User":
	             followUser();
	             break;
    	 }
     }
     
	/**
	Sets this user to follow another user with the name given by txtFollowUserID.  
    The followed user is displayed in followingList, and whenever the followed user
	posts a message it will appear in this user's feed.
    */
     public void followUser() {
         String followUserID = txtFollowUserID.getText();
         
         if (!followUserID.equals("")){
             if (user.followUser(followUserID).equals("")){
            	 followingListModel.add(0, followUserID);
             }
             else {
            	 terminal.setText(user.followUser(followUserID));
             }
             txtFollowUserID.setText("");
         }
     }
     
	/**
	Posts the message given by txtMessage. Followers of this user will be able to
	see this message in their feed.
    */
     public void postMessage() {
         if (!txtMessage.getText().equals("")){
             user.postMessage(txtMessage.getText());
             txtMessage.setText("");
         }
     }
     
	/**
	Displays a message to this user's feed
	*/
     public void addMessageToFeed(String message) {
         newsFeedListModel.add(0, message);
     }
 }

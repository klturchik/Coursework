/**
 * 	Created by klturchik on 11/6/16.
 */
package ui;

import visitor.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.text.DecimalFormat;

import javax.swing.*;


public class AdminUI implements ActionListener {

    private static AdminUI INSTANCE = null;
    
	private JFrame frame;
    private JButton btnAddUser;
    private JButton btnAddGroup;
    private JButton btnUserView;
    private JButton btnUserTotal;
    private JButton btnGroupTotal;
    private JButton btnMessageTotal;
    private JButton btnShowPositive;
    
    private Component root;
    private ComponentTreeModel treeModel;
    private JTree tree;
    
    private JLabel terminal;  
    private JTextField txtUserID;
    private JTextField txtGroupID;
   
    /**
    CONSTRUCTOR 	
    */
    private AdminUI() {

    }
    
    /**
    Initializes and returns a "singleton" instance of AdminUI.
    */
    public static AdminUI getInstance() {
        if (INSTANCE == null){
        	INSTANCE = new AdminUI();
        }
        return INSTANCE;
    }
    
    /**
	Creates the GUI frame and components for Admin users.
    */
    public void initialize() {
    	//CREATE FRAME
		frame = new JFrame();
    	frame.setTitle("Admin Menu");
		frame.setBounds(100, 100, 750, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		//BUTTON - ADD USER
		btnAddUser = new JButton("Add User");
		btnAddUser.setBounds(570, 20, 150, 50);
		frame.getContentPane().add(btnAddUser);
		btnAddUser.addActionListener(this);
		
		//BUTTON - ADD GROUP
		btnAddGroup = new JButton("Add Group");
		btnAddGroup.setBounds(570, 75, 150, 50);
		frame.getContentPane().add(btnAddGroup);
		btnAddGroup.addActionListener(this);
		
		//TEXT BOX  - USER ID
		txtUserID = new JTextField();
		txtUserID.setBounds(350, 20, 210, 50);
		txtUserID.setFont(new Font("SANS_SERIF", Font.PLAIN, 20));
		frame.getContentPane().add(txtUserID);
		
		//TEXT BOX  - GROUP ID
		txtGroupID = new JTextField();
		txtGroupID.setBounds(350, 75, 210, 50);
		txtGroupID.setFont(new Font("SANS_SERIF", Font.PLAIN, 20));
		frame.getContentPane().add(txtGroupID);

		//BUTTON - OPEN USER VIEW
		btnUserView = new JButton("Open User View");
		btnUserView.setBounds(350, 130, 370, 50);
		frame.getContentPane().add(btnUserView);
		btnUserView.addActionListener(this);
		
		//TERMINAL
		terminal = new JLabel("Welcome!");
		terminal.setBounds(350, 230, 370, 50);
		terminal.setFont(new Font("SANS_SERIF", Font.PLAIN, 20));
		terminal.setHorizontalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(terminal);
		
		//BUTTON - SHOW USER TOTAL
		btnUserTotal = new JButton("Show User Total");
		btnUserTotal.setBounds(350, 335, 180, 50);
		frame.getContentPane().add(btnUserTotal);
		btnUserTotal.addActionListener(this);
		
		//BUTTON - SHOW GROUP TOTAL
		btnGroupTotal = new JButton("Show Group Total");
		btnGroupTotal.setBounds(540, 335, 180, 50);
		frame.getContentPane().add(btnGroupTotal);
		btnGroupTotal.addActionListener(this);
		
		//BUTTON - SHOW MESSAGE TOTAL
		btnMessageTotal = new JButton("Show Message Total");
		btnMessageTotal.setBounds(350, 390, 180, 50);
		frame.getContentPane().add(btnMessageTotal);
		btnMessageTotal.addActionListener(this);
		
		//BUTTON - SHOW POSITIVE %
		btnShowPositive = new JButton("Show Positive %");
		btnShowPositive.setBounds(540, 390, 180, 50);
		frame.getContentPane().add(btnShowPositive);
		btnShowPositive.addActionListener(this);
		
		//TREE - USER GROUP
        root = new Group("Root");
        treeModel = new ComponentTreeModel(root);
		tree = new JTree(treeModel);
		tree.setBounds(0, 0, 330, 500);
		tree.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		frame.getContentPane().add(tree);
		
		frame.setVisible(true);
    }
    
    
    /**
    Whenever a button is pressed this method is called to run the method
    corresponding to that button.
    */
    public void actionPerformed(ActionEvent event) {
		switch (event.getActionCommand()) {
			case "Add User":
				addUser();
				break;
			case "Add Group":
				addGroup();
		        break;
			case "Open User View":
				openUserView();
		        break;
			case "Show User Total":
				showUserTotal();
		        break;
			case "Show Group Total":
				showGroupTotal();
		        break;
			case "Show Message Total":
				showMessageTotal();
		        break;
			case "Show Positive %":
	            showPositivePercentage();
		        break;
		}
    }
    
    /**
	Adds a new user with the name given in txtUserID.
    */
    private void addUser() {
        String id = txtUserID.getText();
        
        if (!id.equals("")){
            Component parent = getSelectedUserComponent();
            
            if (treeModel.getUserFromID(root, id) == null){
                treeModel.addUserComponent(parent, new User(treeModel, id));
                txtUserID.setText("");
            }
            else {
            	terminal.setText("Error: That username already exists.");
            }
            
        }
    }
    
    /**
	Adds a new group with the name given in txtGroupID.
    */
    private void addGroup() {
        String id = txtGroupID.getText();
        
        if (!id.equals("")){
            Component parent = getSelectedUserComponent();
            
            if (treeModel.getGroupFromID(root, id) == null){
                treeModel.addUserComponent(parent, new Group(id));
                txtGroupID.setText("");
            }
            else {
            	terminal.setText("Error: That group name already exists.");
            }            
        }
    }
    
    /**
	Opens the UserUI for the selected user in the tree. If a group is 
	selected, opens the UserUI for all children in the group.
    */
    private void openUserView() {
        Component c = getSelectedUserComponent();
        c.openUserView();
    }
    
    /**
	Counts the total number of users within the selected component of
	the tree using visitors.  Displays the total on the UI.
    */
    private void showUserTotal() {
        Component source = getSelectedUserComponent();
        UserCountVisitor userCountVis = new UserCountVisitor();
        source.accept(userCountVis);
        
        terminal.setText("Total Users: " + userCountVis.count);;
    }
    
    /**
	Counts the total number of groups within the selected component of 
	the tree using visitors.  Displays the total on the UI.
    */
    private void showGroupTotal() {
        Component source = getSelectedUserComponent();
        GroupCountVisitor groupCountVis = new GroupCountVisitor();
        source.accept(groupCountVis);
        
        terminal.setText("Total Groups: " + groupCountVis.count);
    }
    
    /**
	Counts the total number of messages within the selected component of 
	the tree using visitors.  Displays the total on the UI.
    */
    private void showMessageTotal() {
        Component source = getSelectedUserComponent();
        MessageCountVisitor messageCountVis = new MessageCountVisitor();
        source.accept(messageCountVis);
        
        terminal.setText("Total Messages: " + messageCountVis.count);
    }
    
    /**
	Counts the total number of messages and the total number of messages
	containing positive words within the selected component of the tree 
	using visitors.  Calculates and displays the percentage of positive 
	messages on the UI.
    */
    private void showPositivePercentage() {
        Component source = getSelectedUserComponent();
        PositiveCountVisitor posCountVis = new PositiveCountVisitor();
        MessageCountVisitor messageCountVis = new MessageCountVisitor();
        source.accept(posCountVis);
        source.accept(messageCountVis);
        
        double result = ((double)posCountVis.count / (double)messageCountVis.count) * 100.0;
        DecimalFormat df = new DecimalFormat("#.##");
        
        terminal.setText("Positive Messages: " + posCountVis.count + " / " 
        + messageCountVis.count + " " + df.format(result) + "%");
    }
    
    /**
	Returns the UserComponent currently selected in the JTree. If nothing 
	is selected, it returns the root.
    */
    private Component getSelectedUserComponent() {
        Component result = ((Component)tree.getLastSelectedPathComponent());
        if (result == null){
            result = root;
        }
        return result;
    }
}

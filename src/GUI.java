import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class GUI extends Thread implements ActionListener {
	//	-=GLOBAL VARIABLES=-
	/**
	 * This is creating the main frame
	 * It is a static field in order to allow the rest of the class to access/use it freely
	 * This allows for the buttons (representing Modules) to be added and removed freely
	 * Frame has a Grid layout of 1 Column and 3 Rows
	 * There will be three panels to fill each row:
	 * 		-logoPanel: The panel containing the Edge Hill logo
	 * 		-modulePanel: The panel containing the buttons for the modules
	 * 		-footerPanel: The panel containing the setting buttons at the bottom
	 */
	static JFrame frame = new JFrame("Student Management System");
	static JPanel cards = new JPanel();
	CardLayout cl = new CardLayout();
	
	//JPanel cards, logoPanel, modulePanel, footerPanel, homePanel;
	static JPanel logoPanel = new JPanel(); //Creating the panel in which the logo will be, filling column 1
	static JPanel modulePanel = new JPanel(); //Creating the panel in which the module buttons will be, filling column 2
	static JPanel footerPanel = new JPanel(); //Creating the panel in which the settings buttons will be, filling column 3
	//All of the above panels will be displayed on this panel below
	static JPanel homePanel = new JPanel();
	JPanel Jmodule = new JPanel();
	
	//HashMaps - used to store a list of all of the created objects
	static HashMap<String, Module> modules = new HashMap<String, Module>(); //Creating the HashMap that will store all of the Module objects with an identifiable key
	static HashMap<String, Student> students = new HashMap<String, Student>(); //Creating the HashMap that will store all of the Student objects with and identifiable key
	static HashMap<String, Staff> staff = new HashMap<String, Staff>(); //Creating the HashMap that will store all of the Staff objects. Each of these objects will also be stored in Tutor/Academics hashmaps
	static HashMap<String, Tutor> tutors = new HashMap<String, Tutor>(); //The hashmap that will store all of the tutors' objects
	static HashMap<String, Academic> academics = new HashMap<String, Academic>(); //The hashmap that will store all of the academics' objects
	
	/*
	 * Counting the number of modules, students and staff are currently in the system
	 * This is to aid the program in generating a unique identifier
	 * This is set to 1 automatically to avoid having a unique ID of 0000
	 */
	static int moduleCount = 1;
	static int studentCount = 1;
	static int staffCount = 1;
	
	String studentData[][];
	String studentColumns[] = {"ID","Name","Grade"};
	JTable studentRecords;
	
	Boolean manualStudentCreate = false; //Used in the createStudent() method - will determine if the button has been clicked or not. If it has been clicked (value would turn true) then the program will not load data from file
	
	static studentProfileBtnColumn first;
	
	/*
	 * The method used to build the main frame of the application
	 * This is the frame of the home screen which includes a grid layout of 3 Rows and 1 Column
	 * Uses the static frame object initialised at the top of the class
	 */
	public void buildFrame() throws IOException {
		//Setting up the frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Program will exit upon pressing the windows X button
		//Adjusting the appearance of the frame
		frame.setSize(750, 693);//750 width and 693 height (height is just enough to show EHU logo due to 3 row layout. I don't want the program to have a height of this already so I at least got it as short as possible)
		ImageIcon crest = new ImageIcon("./Crest.png");
		frame.setIconImage(crest.getImage());
		
		//Setting the layout of the frame
		
		cards.setLayout(cl);
		//cards.setSize(750, 693);
		
		homePanel.setLayout(new GridLayout(3,1)); //Putting the newly created layout onto the frame
		//homePanel.setSize(750, 650);
		showLogo();
		showModules();
		showSettings();
		
		cards.add(homePanel, "1");
		
		//frame.setLayout(null);
		frame.add(cards);
		frame.setVisible(true);//making the frame visible
		cards.setVisible(true);
		//cl.show(homePanel, "1");
	}
	
	/*
	 * The method used to display the logo on the frame
	 * This is the panel which will be in the 1st of the three columns of the frame's grid layout
	 */
	public void showLogo() throws IOException {
		JLabel label = new JLabel(); //This is the label that will contain the image logo
		
		/**
		 * To help with this block of code two resources were used:
		 * 
		 * 	1. https://www.javacodex.com/More-Examples/2/1
		 * This helped me retrieve the image using BufferedImage and allowed me to convert the
		 * BufferedImage into an ImageIcon which was then placed onto a JLabel.
		 * Lines 2/6 of the block
		 * 
		 * 	2. https://www.youtube.com/watch?v=1q7VzBiEchk&ab_channel=MauriceMuteti
		 * This helped me scale the image to my preferred size
		 * Lines 4/6 of the block
		 *
		 * I combined these two resources to produce the block of code.
		 * No code was copied verbatim, variable names were changed, a couple lines were altered & values were changed
		 */
		BufferedImage bufferedImg = ImageIO.read(new File("./logo.png")); //Retrieving the image using BufferedImage
		ImageIcon iconImg = new ImageIcon(bufferedImg); //Converting the retrieved BufferedImage into an imageIcon
		Image img = iconImg.getImage(); //Converting the image icon into a full scale image
		Image imageScale = img.getScaledInstance(391, 221, Image.SCALE_SMOOTH); //Changing the size of the image & ensuring it scaled smoothly
		iconImg = new ImageIcon(imageScale); //Setting the newly scaled image to an image icon
		label.setIcon(iconImg); //Adding the image icon to the label
		
		logoPanel.add(label); //Adding the image to the panel
		homePanel.add(logoPanel); //Finally adding the panel to the main grid layout panel
	}
	
	/*
	 * The method used to display the module buttons on the frame
	 * This is the panel which will be in the 2nd of the three columns of the frame's grid layout
	 * Uses the static modulePanel object initialised at the top of the class
	 */
	public void showModules() {
		FlowLayout moduleLayout = new FlowLayout(); //Creating a flow layout - allows buttons to position themselves nicely
		moduleLayout.setHgap(25); //Setting the horizontal gap between the buttons to 25 pixels
		moduleLayout.setVgap(30); //Setting the vertical gap between the buttons and the logo to 30 pixels
		modulePanel.setLayout(moduleLayout); //Applying the newly created layout to the panel
		homePanel.add(modulePanel); //Adding the panel to the main frame
	}

	/*
	 * BOTTOM PANEL OF HOME PAGE - SETTINGS BUTTONS (Add Module, Student List, Staff List)
	 * The method used to display the settings buttons on the frame
	 * This method includes the process of inputing information upon creating a new module
	 * The panel which will be in the 3rd of the three columns of the frame's grid layout
	 */
	public void showSettings() {
		//Creating the buttons
		JButton addModule = new JButton("Add Module");
		JButton seeStaff = new JButton("Staff List");
		JButton seeStudents = new JButton("Students List");
		
		//Dividing the section into two panels; top and bottom; to allow for easier layout of buttons
		JPanel footerTop = new JPanel(); //Will contain the Add Module button
		JPanel footerBottom = new JPanel(); //Will contain the Student and Staff list buttons
		
		//Setting the size of the buttons
		addModule.setPreferredSize(new Dimension(152,50));
		seeStaff.setPreferredSize(new Dimension(125,25));
		seeStudents.setPreferredSize(new Dimension(125,25));
		
		//Creating the layout. GridBagLayout is used in order to have the two small buttons directly under the larger button
		GridBagLayout footerGrid = new GridBagLayout();
		GridBagConstraints footerConstraints = new GridBagConstraints();
		FlowLayout footerFlow = new FlowLayout();
		
		//Adding the layout to all of the panels
		footerTop.setLayout(footerFlow);
		footerBottom.setLayout(footerFlow);
		footerPanel.setLayout(footerGrid);
		
		//Adding the buttons to the new panels
		footerTop.add(addModule);
		footerBottom.add(seeStaff);
		footerBottom.add(seeStudents);
		
		//Setting the constraints for the top panel
		footerConstraints.gridx = 0;
		footerConstraints.gridy = 0;
		footerConstraints.insets = new Insets(100,0,0,0);
		footerPanel.add(footerTop, footerConstraints);
		
		//Setting the constraints for the bottom panel
		footerConstraints.gridx = 0;
		footerConstraints.gridy = 1;
		footerConstraints.insets = new Insets(0,0,0,0);
		footerPanel.add(footerBottom, footerConstraints);
		
		//ACTIONS FOR EACH BUTTON
		addModule.addActionListener(new ActionListener() { //The event that occurs upon clicking Add Module
			/*
			 * The events that occur upon clicking the "Add Module" button within the settings section of the home page
			 * This ActionListener method will:
			 * 		1. Build and load a new window, which includes a "submit" button, requesting two inputs from the user; Module Name & Module Leader
			 * 		2. Upon clicking the submit button, a further action listener method is deployed which will:
			 * 				1. Retrieve the inputed Module name and Leader
			 * 				2. Create the module using the inputed information 
			 * 				3. Hide and dispose of the new window
			 */
			public void actionPerformed(ActionEvent e) {
				JFrame prompt = new JFrame("Add a new module"); //Creating the new frame window for input
				prompt.setSize(300, 150); //Setting the size, width and height in pixels
				prompt.setLayout(null); //Null layout - for such a small window it is simpler to just position the components manually
				
				//Setting up the text fields where the user will input their data
				JTextField n = new JTextField(); //Module Name text field
				JTextField l = new JTextField(); //Module Leader text field
				//Setting the position and size of the text fields - xPosition, yPosition, width, height
				n.setBounds(110, 10, 160, 20);
				l.setBounds(110, 40, 160, 20);
				
				//Setting up the labels that indicate which text field is for what input
				JLabel nameLabel = new JLabel("Module Name:");
				JLabel leaderLabel = new JLabel("Module Leader:");
				//Setting the position and size of the labels - xPosition, yPosition, width, height
				nameLabel.setBounds(15, 10, 100, 20);
				leaderLabel.setBounds(15, 40, 100, 20);
				
				//Creating the submit button
				JButton submit = new JButton("Submit");
				submit.setBounds(90, 75, 100, 20); //Setting the position and size of the button - xPosition, yPosition, width, height
				submit.addActionListener(new ActionListener() { //Event that will take place upon clicking
					public void actionPerformed(ActionEvent e) {
						String name = n.getText(); //Retrieving inputed module name
						String leader = l.getText(); //Retrieving inputed module leader
						createModule(name, leader); //Calling the createModule() method with the passed data
						prompt.setVisible(false); //"Hiding" the input window
						prompt.dispose(); //Essentially deleting the window so it isn't taking up resources
					}
				});
				//Adding all created components to the window
				prompt.add(n);
				prompt.add(l);
				prompt.add(nameLabel);
				prompt.add(leaderLabel);
				prompt.add(submit);
				//Setting the window to be visible
				prompt.setVisible(true); 
			}
			
		});
		seeStudents.addActionListener(new ActionListener() { //The event that occurs upon clicking Students List button
			/*
			 * The events that occur upon clicking the "Students List" button within the settings section of the home page
			 * This ActionListener method creates a new pop out window that will show a full, scroll-able list of every student that is on the system
			 */
			public void actionPerformed(ActionEvent e) {
				first = new studentProfileBtnColumn();
				
			}
			
		});
		seeStaff.addActionListener(new ActionListener() { //The event that occurs upon clicking Staff list button
			public void actionPerformed(ActionEvent e) {
				//DEFINING THE COMPONENTS NEEDED
				String[] staffColumns = {"ID","Name","Office Location","Position"}; //Setting the column headers
				JFrame staffList = new JFrame("Full List of Staff"); //Creating the new frame
				staffList.setSize(500, 500); //Setting the size of the frame, width and height in pixels
				JPanel top = new JPanel(); //Top panel in the grid - contains the search bar. This is in a panel to make easier placement
				
				//CREATING THE LAYOUT FOR THE FRAME. GridBagLayout is used to allow for the search facility to sit nicely on the top of the JTable
				GridBagLayout staffListLayout = new GridBagLayout();
				staffList.setLayout(staffListLayout);
				//CREATING THE CONSTRAINTS AND SETTING A FEW PARAMETERS ALREADY
				GridBagConstraints staffGBC = new GridBagConstraints();
				staffGBC.fill = GridBagConstraints.BOTH; //Will fill out the x & y axis
				staffGBC.weightx = 1; //Will distribute components over the x axis evenly
				staffGBC.weighty = 1;//Will distribute components over the y axis evenly
				
				//SEARCH BAR
				JLabel search = new JLabel("Search bar will be here");
				top.add(search);
				staffGBC.gridx = 0;
				staffGBC.gridy = 0;
				staffList.add(top, staffGBC);
				
				//TABLE
				JTable staffListTbl = new JTable(getList("Staff"), staffColumns);
				JScrollPane scrollStaff = new JScrollPane(staffListTbl);
				staffGBC.gridx = 0;
				staffGBC.gridy = 1;
				staffGBC.gridheight = 1;
				staffList.add(scrollStaff, staffGBC);
				
				//ADD + DELETE STAFF MEMBERS
				JPanel settings = new JPanel(new FlowLayout());
				JButton addStaff = new JButton("+");
				JButton removeStaff = new JButton("-");
				settings.add(addStaff);
				settings.add(removeStaff);
				staffGBC.gridx = 0;
				staffGBC.gridy = 2;
				staffGBC.gridheight = 1;
				staffGBC.weighty = 0;
				staffList.add(settings, staffGBC);
				
				addStaff.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						JFrame prompt = new JFrame("Add a new staff member"); //Creating the new frame window for input
						prompt.setSize(300, 230); //Setting the size, width and height in pixels
						prompt.setLayout(null); //Null layout - for such a small window it is simpler to just position the components manually
						
						//Setting up the text fields where the user will input their data
						JTextField n = new JTextField(); 
						JTextField a = new JTextField();
						JTextField em = new JTextField();
						JTextField o = new JTextField();
						String[] options = {"Tutor", "Academic"};
						JComboBox combo = new JComboBox(options);
						//Setting the position and size of the text fields - xPosition, yPosition, width, height
						n.setBounds(110, 10, 160, 20);
						a.setBounds(110, 40, 160, 20);
						em.setBounds(110, 70, 160, 20);
						o.setBounds(110, 100, 160, 20);
						combo.setBounds(110, 130, 160, 20);
						
						//Setting up the labels that indicate which text field is for what input
						JLabel name = new JLabel("Name:");
						JLabel age = new JLabel("Age:");
						JLabel email = new JLabel("Email:");
						JLabel office = new JLabel("Office Location:");
						JLabel position = new JLabel("Position: ");
						//Setting the position and size of the labels - xPosition, yPosition, width, height
						name.setBounds(15, 10, 100, 20);
						age.setBounds(15, 40, 100, 20);
						email.setBounds(15, 70, 100, 20);
						office.setBounds(15, 100, 100, 20);
						position.setBounds(15, 130, 100, 20);
						
						//Creating the submit button
						JButton submit = new JButton("Submit");
						submit.setBounds(90, 160, 100, 20); //Setting the position and size of the button - xPosition, yPosition, width, height
						submit.addActionListener(new ActionListener() { //Event that will take place upon clicking
							public void actionPerformed(ActionEvent e) {
								String name = n.getText();
								int age = Integer.parseInt(a.getText());
								String email = em.getText();
								String office = o.getText();
								String position = String.valueOf(combo.getSelectedItem());
								addStaff(name, age, email, office, position); //Calling the addStaff() method with the passed data
								DefaultTableModel updatedModel = new DefaultTableModel(getList("Staff"), staffColumns);
								staffListTbl.setModel(updatedModel);
								prompt.setVisible(false); //"Hiding" the input window
								prompt.dispose(); //Essentially deleting the window so it isn't taking up resources
								staffList.repaint();
							}
						});
						//Adding all created components to the window
						prompt.add(n);
						prompt.add(name);
						prompt.add(a);
						prompt.add(age);
						prompt.add(em);
						prompt.add(email);
						prompt.add(o);
						prompt.add(office);
						prompt.add(submit);
						prompt.add(combo);
						prompt.add(position);
						//Setting the window to be visible
						
						prompt.setVisible(true); 
					}
					
				});
				removeStaff.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						Frame prompt = new JFrame("Remove a staff member from the system"); //Creating the new frame window for input
						prompt.setSize(300, 150); //Setting the size, width and height in pixels
						prompt.setLayout(null); //Null layout - for such a small window it is simpler to just position the components manually
						
						//Setting up the text fields where the user will input their data
						JTextField id = new JTextField(); 
						id.setBounds(110, 10, 160, 20);
						
						//Setting up the label that indicate which text field is for what input
						JLabel staffID = new JLabel("ID:");
						staffID.setBounds(15, 10, 100, 20);
						
						//Creating the submit button
						JButton submit = new JButton("Submit");
						submit.setBounds(90, 50, 100, 20); //Setting the position and size of the button - xPosition, yPosition, width, height
						submit.addActionListener(new ActionListener() { //Event that will take place upon clicking
							public void actionPerformed(ActionEvent e) {
								String staffID = id.getText();
								//All IDs are unique, no matter what HashMap it is in so we can run these lines to remove the same ID from both HashMaps
								tutors.remove(staffID);
								academics.remove(staffID);
								DefaultTableModel updatedModel = new DefaultTableModel(getList("Staff"), staffColumns);
								staffListTbl.setModel(updatedModel);
								prompt.setVisible(false); //"Hiding" the input window
								prompt.dispose(); //Essentially deleting the window so it isn't taking up resources
								staffList.repaint();
							}
						});
						//Adding all created components to the window
						prompt.add(id);
						prompt.add(staffID);
						prompt.add(submit);
						//Setting the window to be visible
						
						prompt.setVisible(true); 
					}
					
				});
				
				staffList.pack(); 
				staffList.setVisible(true); //Making the frame visible after setup
			}
			
		});
		
		homePanel.add(footerPanel);
	}
	
//	-=CREATING MODULE METHODS=-
	/**
	 * The method used to create a new module
	 * @param n (NAME) - String input for the name of the module
	 * @param l (LEADER) - String input for the name of the module leader
	 */
	public void createModule(String n, String l) {
		
		Module newModule = new Module(n, l); //Creating the new module, passing the name and leader through
		moduleCount += 1; //Incrementing the count of the modules by 1. This allows for the unique identifiers to remain unique
		modules.put(newModule.getModuleID(), newModule);
		addButton(newModule.getModuleID(), n);
		cards.add(Jmodule, newModule.getModuleID());
		Jmodule.setName("panel"+newModule.getModuleName());
		frame.add(cards);
	}
	
	public void addStaff(String name, int age, String email, String office, String position) {
		if (position == "Tutor") {
			staffCount += 1;
			Tutor tutorNew = new Tutor(name, age, email, office);
			tutors.put(tutorNew.getStaffId(), tutorNew);
		}
		else if (position == "Academic") {
			staffCount += 1;
			Academic newAcademic = new Academic(name, age, email, office);
			academics.put(newAcademic.getStaffId(), newAcademic);
		}
		
	}
	
	public static void addStudent(String name, int age, String email, String course) {
		studentCount += 1;
		Student newStudent = new Student(name, age, email, course);
		students.put(newStudent.getId(), newStudent);
	}
	
	/**
	 * The method that is used to create the button for the module created.
	 * It is only called by the createModule() method. It is it's own method to allow for easier passing of the moduleID
	 * @param id - This is retrieved automatically by the getModuleID() method within the module class.
	 * @param text - This is the text that will be displayed 
	 */
	public void addButton(String id, String text){
		JButton newButton = new JButton(); //Creating the button
		
		//Editing the appearance of the button to match the colour scheme
		newButton.setText(text); //Setting the display text on the button to be the passed value
		newButton.setBackground(new Color(110, 8, 110)); //Purple background
		newButton.setForeground(Color.WHITE); //White font
		newButton.setFocusPainted(false); //No ugly hover outline
		newButton.setPreferredSize(new Dimension(150,50));
		
		//Adding the button to the frame
		modulePanel.add(newButton); //Adding the button to the frame
		newButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buildModulePane(id);
			}
			
		});
		newButton.setName(id); //Finally, setting the name of the button. This is done last since the program would otherwise not be able to edit it since it's lack of ability to predict the name of the button
		frame.revalidate(); //In a way "refreshing" the frame - allows the newly created button to be displayed
	}
	
	/**
	 * This is the method that will build the panel that will be displaying the module's "page" *WARNING:
	 * 																							 THIS METHOD IS MESSY, REPETITIVE & LONG.
	 * 																				  		 	 REASONS ARE TO BE EXPLAINED*
	 * 
	 * This method takes advantage of GridBagLayout's GridBagConstraints method. To further understand the code below, refer to Figure 2 of the report
	 * The window was first designed and then split into the desired amount of rows and columns
	 * Code is often repeated for consistency and ease of avoiding holes and gaps within the code
	 * Each component is spaced out within their own blocks of code, accompanied with comments identifying the component
	 * 
	 * UNDERSTANDING THE COMMENTS:
	 * 		Blocks will be categorised in the rows of the design (refer to Figure 2 of the report)
	 * 		The design rows do not symbolise the GridBagLayout's rows (ModuleName is row 1, ModuleID is row 2, Student Title row 3 etc) 
	 * 		Each row in the design will be separated into different blocks
	 * 		Each component in a row will be separated by a comment
	 * 
	 * COMPONENT TEMPLATE:
	 * 		Each component has (mostly) the same lines of code to keep consistency
	 * 		The template is as follows:
	 * 			gridx: The x position of the component
	 * 			gridy: The y position of the component
	 * 			gridwidth: For how many columns should the component span over
	 * 			weightx: How much horizontal space of the column should the component fill up
	 * 			weighty: How much vertical space of the column should the component fill up
	 * 			insets: Setting whitespace on the sides. Parameters(Top, Left, Right, Bottom)
	 * 
	 * 
	 * @param id - ID of the module, this is passed through automatically upon clicking the module's button via its ActionListener method
	 */
	public void buildModulePane(String id) {
		//Setting up
		JPanel modulePanel = new JPanel(new GridBagLayout()); //The panel in which will be displayed. Set the layout to GridBagLayout
		Module currentModule = modules.get(id); //Retrieving the module object from the hashmap "modules"
		currentModule.addStudent("George Macdonald");
		currentModule.addStudent("This is a test Student");
		GridBagConstraints gbc = new GridBagConstraints(); //Creating a new grid-bag-constraints
		gbc.fill = GridBagConstraints.BOTH; //Ensuring the components fill both horizontal and veritcal spaces
		gbc.weightx = 1; //The weight set to the most extreme value to fully distribute horizontal space - THIS WILL ME AMMENDED BY CERTAIN COMPONENTS
		
		//Additional panels were created to make positioning of certain components easier
		//E.g., a JLabel will automatically be centred with a panel and will cause issues if not in one
		JPanel studentsPanel = new JPanel();
		JPanel classesPanel = new JPanel();
		
//		-=ROW 1=- 
//		Return Button, Displayed Module name, Module Leader Title, Module Moderators Title
		JLabel moduleName = new JLabel(); //Name of the module to be retrieved and displayed
		JLabel moduleLeader = new JLabel(); //Module Leader Title
		JLabel moduleMods = new JLabel(); //Module Moderators Title
		JButton back = new JButton("Return"); //Return Button
		//Return Button
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		gbc.insets = new Insets(10, 10, 0, 0);
		modulePanel.add(back, gbc); //Adding the button to the panel
		back.addActionListener(returnMethod);
		//Displayed Module Name
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridwidth = 3;
		gbc.weighty = 0;
		gbc.weightx = 1;
		gbc.insets = new Insets(10, 35, 0, 0);
		moduleName.setText(currentModule.getModuleName());
		modulePanel.add(moduleName, gbc);
		//Module Leader Title
		gbc.gridx = 4;
		gbc.gridy = 0;
		gbc.gridwidth = 3;
		gbc.weighty = 0;
		gbc.insets = new Insets(10, 0, 0, 0);
		moduleLeader.setText("Module Leader");
		modulePanel.add(moduleLeader, gbc);
		//Module Moderators Title
		gbc.gridx = 7;
		gbc.gridy = 0;
		gbc.gridwidth = 3;
		gbc.weighty = 0;
		moduleMods.setText("Moderators");
		modulePanel.add(moduleMods, gbc);
		
//		-=ROW 2=-
//		Module's ID, Displayed Module Leader, Displayed Module Moderators
		JLabel moduleID = new JLabel(); 
		JLabel leader = new JLabel();
		JLabel mods = new JLabel();
		//ModuleID
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 4;
		gbc.weighty = 0;
		gbc.insets = new Insets(0, 117, 0, 0);
		moduleID.setText(currentModule.getModuleID());
		modulePanel.add(moduleID, gbc);
		//Module Leader's name
		gbc.gridx = 4;
		gbc.gridy = 1;
		gbc.gridwidth = 3;
		gbc.weighty = 0;
		gbc.insets = new Insets(0, 0, 0, 0);
		leader.setText(currentModule.getModuleLeader());
		modulePanel.add(leader, gbc);
		//Module Moderators' name(s)
		gbc.gridx = 8;
		gbc.gridy = 1;
		gbc.gridwidth = 3;
		gbc.weighty = 0;
		mods.setText(currentModule.getModerators());
		modulePanel.add(mods, gbc);
		
//		-=ROW 3=-
//		Student Title
		JLabel students = new JLabel();
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.gridwidth = 10;
		gbc.weighty = 0;
		gbc.insets = new Insets(25, 0, 0, 0);
		students.setText("Students");
		studentsPanel.add(students);
		modulePanel.add(studentsPanel, gbc);
		
//		-=ROW 4=-
//		List of all students
		DefaultTableModel tableModel = new DefaultTableModel(getStudentData(currentModule), studentColumns);
		JTable studentRecords = new JTable(tableModel);
		JScrollPane studentsPane = new JScrollPane(studentRecords);
		studentsPane.setSize(100, 250);
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.gridwidth = 10;
		gbc.weighty = 9;
		gbc.insets = new Insets(0, 10, 0, 10);
		modulePanel.add(studentsPane, gbc);
		
//		-=ROW 5=-
//		Class title
		JLabel classes = new JLabel();
		gbc.gridx = 0;
		gbc.gridy = 13;
		gbc.gridwidth = 10;
		gbc.weighty = 0;
		gbc.insets = new Insets(30, 0, 0, 0);
		classes.setText("Classes");
		classesPanel.add(classes);
		modulePanel.add(classesPanel, gbc);
		
//		-=ROW 6=-
//		Class Buttons
		JPanel classButtons = new JPanel();
		JButton exampleClass = new JButton("Example Class Button");
		gbc.gridx = 0;
		gbc.gridy = 14;
		gbc.gridwidth = 10;
		gbc.weighty = 2;
		gbc.insets = new Insets(10, 0, 0, 0);
		classButtons.add(exampleClass);
		modulePanel.add(classButtons, gbc);
		
//		-=ROW 7=-
//		Add Student Button
		JButton addStudent = new JButton("Add Students");
		JButton update = new JButton("Update");
		JPanel addStudentPanel = new JPanel(new FlowLayout());
		gbc.gridx = 0;
		gbc.gridy = 17;
		gbc.gridwidth = 10;
		gbc.weighty = 0;
		gbc.insets = new Insets(0, 0, 0, 0);
		
		addStudentPanel.add(addStudent);
		addStudentPanel.add(update);
		modulePanel.add(addStudentPanel, gbc); 
		
		addStudent.addActionListener(new ActionListener() { //EVENT TO OCCUR UPON CLICKING Add Student 
			public void actionPerformed(ActionEvent e) {
				
				ButtonColumn bc = new ButtonColumn(currentModule.getModuleID());
				bc.setVisible(true);
			}
		});
		update.addActionListener(new ActionListener() { //THE EVENT TO OCCUR UPON CLICKING Update
			public void actionPerformed(ActionEvent e) {
				for (String s : ButtonEditor.getAddedStudents() ) { //Looping through the list of students to be added
					currentModule.addStudent(s); //Adding each student from the list
				}
				//Since the data has changed, making a new model with the new data and setting said model to the JTable will update the information
				DefaultTableModel updatedModel = new DefaultTableModel(getStudentData(currentModule), studentColumns);
				studentRecords.setModel(updatedModel);
				ButtonEditor.resetAddedStudents();
			}
			
		});
		
		cards.add(modulePanel, id);
		cl.show(cards, id);
		
		
	}
	
	/**
	 * CREATING THE STUDENT PROFILE FRAME
	 * Refer to design diagrams for row comment references
	 * @param currentStudent - The current student object. This will be passed through automatically
	 */
	public static void studentProfile(Student currentStudent) {
		//CREATING THE FRAME
		JFrame studentProfile = new JFrame();
		studentProfile.setSize(500, 350);
		studentProfile.setTitle(currentStudent.getName() + "'s Profile");
		
		//CREATING & SETTING THE LAYOUT
		GridBagLayout profileLayout = new GridBagLayout();
		GridBagConstraints profileConstraints = new GridBagConstraints();
		profileConstraints.fill = GridBagConstraints.BOTH;
		profileConstraints.weightx = 1;
		studentProfile.setLayout(profileLayout);
		
		//ROW 1: Name Panel
		JPanel pnlName = new JPanel();
		JLabel lblName = new JLabel();
		lblName.setText(currentStudent.getName());;
		profileConstraints.gridx = 0;
		profileConstraints.gridy = 0;
		profileConstraints.gridheight = 1;
		profileConstraints.weighty = 0;
		pnlName.add(lblName);
		studentProfile.add(pnlName, profileConstraints);
		
		//ROW 2: Course Panel
		JPanel pnlCourse = new JPanel();
		JLabel lblCourse = new JLabel();
		lblCourse.setText(currentStudent.getCourse());
		profileConstraints.gridx = 0;
		profileConstraints.gridy = 1;
		profileConstraints.gridheight = 1;
		profileConstraints.weighty = 0;
		profileConstraints.insets = new Insets(0, 0, 10, 0);
		pnlCourse.add(lblCourse);
		studentProfile.add(pnlCourse, profileConstraints);
		
		//ROW 3: Modules Title
		JPanel pnlModules = new JPanel();
		JLabel lblModules = new JLabel("Modules");
		profileConstraints.gridx = 0;
		profileConstraints.gridy = 2;
		profileConstraints.gridheight = 1;
		profileConstraints.weighty = 0;
		profileConstraints.insets = new Insets(0, 0, 0, 0);
		pnlModules.add(lblModules);
		studentProfile.add(pnlModules, profileConstraints);
		
		//ROW 4: Modules Table
		String[] moduleColumns = {"ID", "Name", "Class", "Grade"};
		JTable tblModules = new JTable(getStudentModules(currentStudent), moduleColumns);
		JScrollPane scrollModules = new JScrollPane(tblModules);
		
		profileConstraints.gridx = 0;
		profileConstraints.gridy = 3;
		profileConstraints.gridheight = 3;
		profileConstraints.weighty = 1;
		studentProfile.add(scrollModules, profileConstraints);
		
		//ROW 5: Average Grade
		JPanel pnlGrade = new JPanel();
		JLabel lblGrade = new JLabel();
		lblGrade.setText("Average Grade: ");
		profileConstraints.gridx = 0;
		profileConstraints.gridy = 6;
		profileConstraints.gridheight = 1;
		profileConstraints.weighty = 0;
		pnlGrade.add(lblGrade);
		studentProfile.add(pnlGrade, profileConstraints);
		
		//ROW 6: Delete Button
		JPanel pnlDelete = new JPanel();
		JButton btnDelete = new JButton("Delete Student");
		profileConstraints.gridx = 0;
		profileConstraints.gridy = 7;
		profileConstraints.gridheight = 1;
		pnlDelete.add(btnDelete);
		studentProfile.add(pnlDelete, profileConstraints);
		
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				students.remove(currentStudent.getId());
				studentProfile.dispose();
				first.disposeList();
				studentProfileBtnColumn bc = new studentProfileBtnColumn();
			}
			
		});
		
		studentProfile.setVisible(true);
	}
	
	/**
	 * The method called to import the students from a file
	 * The method reads the file line-by-line with each line being a new student record
	 * 
	 * File format:
	 * [StudentName],[Age],[Email],[Course]
	 * [StudentName],[Age],[Email],[Course]
	 * 
	 * File Location: 
	 * StudentManagementSystem/studentData.txt
	 * 
	 * @throws FileNotFoundException
	 */
	public void importStudents() throws FileNotFoundException {
	//	-=LOCAL VARIABLES=-
		String unsplitRecord; //The string variable to store the full, un-split line. E.g; unsplitRecord = "George Macdonald,20,gtmacdonald06@gmail.com";
		String[] splitRecord = null; //The string array variable to store the split record, with each element being a different attribute in the record. E.g; splitRecord[] = {"George Macdonald", "20", "gtmacdonald06@gmail.com"};
		String name = ""; //String variable to store the name - this is used to pass through the Student class constructor
		String ageS = ""; //The file reader reads in Strings, so the age is stored temporarily in this string variable - it will later be converted to integer to satisfy the student constructor
		int age = 0; //The integer variable that will store the converted age (from string) which will be passed through to the student constructor
		String email = ""; //The string variable that will store the student's email which will be passed through to the student constructor
		String course = ""; //The string variable that will store the student's course which will be passed through to the student constructor
		
	//	-=READING THE FILE=-	
		File studentDataFile = new File("./studentData.txt"); //Retrieving the file
		Scanner fileReader = new Scanner(studentDataFile); //Scanning the file
		
		while (fileReader.hasNextLine()) { //This loop will continue so long as the file has another line to read.
			unsplitRecord = new String(fileReader.nextLine()); //Stored the full, un-split line into this string variable
			splitRecord = unsplitRecord.split(","); //Splits the line via the commas, putting each item into the array
			
			/* This for loop goes through each column, with i representing each column.
			 * The switch-case identifies which column they are dealing with (column 1, 2, 3 or 4).
			 * if column 1: store the element from position 0 in splitRecord array into name
			 * if column 2: store the element from position 1 in splitRecord array into ageS
			 * if column 3: store the element from position 2 in splitRecord array into email
			 * if column 4: store the element from position 3 in splitRecord array into course
			 * 
			 * Includes the conversion from String to Integer between ageS & age using parseInt.
			 * This is surrounded by a try and catch with a numberFormatException as the code will not compile if this is not included.
			 */
			for (int i = 0; i < 4; i++) {
				switch(i) {
				case 0: //Column 1: Name
					name = splitRecord[i];
				case 1: //Column 2: ID
					ageS = splitRecord[i];
					try{
						age = Integer.parseInt(ageS);
					} catch(NumberFormatException ex){ // handle your exception
					    
					}
				case 2: //Column 3: email
					email = splitRecord[i];
				case 3: //Column 4: Course
					course = splitRecord[i];
				}
			}
			
	//		-=CHECKING FOR DUPLICATIONS=-
			boolean exist = false;
			Student newStudent = new Student(name, age, email, course);
			for (String check : students.keySet()) {
				if (check == newStudent.getId()) {
					exist = true;
				}
			}
			if (exist == false) {
				studentCount += 1;
				students.put(newStudent.getId(), newStudent);	
			}
			
		}
		fileReader.close();
	}

	/**
	 * The method called to import the staff members from a file
	 * The method reads the file line-by-line with each line being a new staff record
	 * 
	 * File format:
	 * [Staff Name],[Age],[Email],[Office Location],[Role Position]
	 * [Staff Name],[Age],[Email],[Office Location],[Role Position]
	 * 
	 * File Location: 
	 * StudentManagementSystem/staffData.txt
	 * 
	 * @throws FileNotFoundException
	 */
	public void importStaff() throws FileNotFoundException {
//		-=LOCAL VARIABLES=-
		String unsplitRecord; //The string variable to store the full, un-split line. E.g; unsplitRecord = "Robert Lyon,35,RobertLyon@edgehill.ac.uk,THG12,Academic";
		String[] splitRecord = null; //The string array variable to store the split record, with each element being a different attribute in the record. E.g; splitRecord[] = {"Robert Lyon", "35", "RobertLyon@edgehill.ac.uk", "THG12" ,"Academic"};
		
		//The variables retrieve and pass the information to the relevant class constructor (Academic or Tutor Class)
		String name = ""; //String variable to store the name
		String ageS = ""; //The file reader reads in Strings, so the age is stored temporarily in this string variable - it will later be converted to integer to satisfy the relevant class constructor
		int age = 0; //The integer variable that will store the converted age (from string)
		String email = ""; //The string variable that will store the staff member's email
		String office = ""; //The string that will store the staff member's office location
		String position = ""; //The string variable that will store the staff's role position (Academic or Tutor)
		
		
	//	-=READING THE FILE=-	
		File staffDataFile = new File("./staffData.txt"); //Retrieving the file
		Scanner fileReader = new Scanner(staffDataFile); //Scanning the file
		
		while (fileReader.hasNextLine()) { //This loop will continue so long as the file has another line to read.
			unsplitRecord = new String(fileReader.nextLine()); //Stored the full, un-split line into this string variable
			splitRecord = unsplitRecord.split(","); //Splits the line via the commas, putting each item into the array
			
			/* This for loop goes through each column, with i representing each column.
			 * The switch-case identifies which column they are dealing with (column 1, 2, 3, 4 or 5).
			 * if column 1: store the element from position 0 in splitRecord array into name
			 * if column 2: store the element from position 1 in splitRecord array into ageS
			 * if column 3: store the element from position 2 in splitRecord array into email
			 * if column 4: store the element from position 3 in splitRecord array into office
			 * if column 5: store the element from position 4 in splitRecord array into position
			 * 
			 * Includes the conversion from String to Integer between ageS & age using parseInt.
			 * This is surrounded by a try and catch with a numberFormatException as the code will not compile if this is not included.
			 */
			for (int i = 0; i < 5; i++) {
				switch(i) {
				case 0: //Column 1: Name
					name = splitRecord[i];
				case 1: //Column 2: ID
					ageS = splitRecord[i];
					try{
						age = Integer.parseInt(ageS);
					} catch(NumberFormatException ex){ // handle your exception
					    
					}
				case 2: //Column 3: email
					email = splitRecord[i];
				case 3: //Column 4: Office Location
					office = splitRecord[i];
				case 4: //Column 5: Role position
					position = splitRecord[i];
				}
			}
			
	//		-=CHECKING FOR DUPLICATIONS=-
			boolean exist = false;
			switch(position) {
			
			//If the position of the staff member is "Academic", follow this check
			case "Academic": 
				Academic newAcademic = new Academic(name, age, email, office); //Creating the new Academic
				//Loop through the academics HashMap to see if the new staff ID is already present
				for (String check : academics.keySet()) {
					if (check == newAcademic.getStaffId()) {
						exist = true; //If presence of the same ID is found, this boolean will turn to true on first occurrence and remain that way
					}
				}
				//If after the whole HashMap has looped through and no duplication of IDs is found then the object is added to the HashMap and the count is incremented
				if (exist == false) {
					staffCount += 1;
					academics.put(newAcademic.getStaffId(), newAcademic);
				}
				break;
				
			//If the position of the staff member is "Tutor", follow this check
			case "Tutor": 
				Tutor newTutor = new Tutor(name, age, email, office); //Creating the new Tutor
				//Loop through the tutors HashMap to see if the new staff ID is already present
				for (String check : tutors.keySet()) {
					if (check == newTutor.getStaffId()) {
						exist = true; //If presence of the same ID is found, this boolean will turn to true on first occurrence and remain that way
					}
				}
				//If after the whole HashMap has looped through and no duplication of IDs is found then the object is added to the HashMap and the count is incremented
				if (exist == false) {
					staffCount += 1;
					tutors.put(newTutor.getStaffId(), newTutor);
				}
			}
		}
		fileReader.close();
	}
	
	//NAME AGE EMAIL ID CLASS
	public void createStudent(String n, int a, String e, String c) {
		Student newStudent = new Student(n, a, e, c);
		studentCount += 1;
		students.put(newStudent.getId(), newStudent);
	}
	
	/*
	 * The method in which will be called to update the students lists
	 */
	public String[][] getStudentData(Module currentModule) {
		int row = 0;
		int rowCount = currentModule.getStudents().length;
		String[][] studentData = new String[rowCount][3];
		for (String m : currentModule.getStudents()) {
			for (String i : students.keySet()) {
				Student currentStudent = students.get(i);
				if (m == currentStudent.getId()) {
					for (int col = 0; col < 3 ; col++) {
						String name = currentStudent.getName();
						String id = currentStudent.getId();
						String grades = Arrays.toString(currentStudent.getGrades());
						switch(col) {
						case 0:
							studentData[row][col] = id;
							break;
						case 1:
							studentData[row][col] = name;
							break;
						case 2:
							studentData[row][col] = grades;
								}
							}
					row += 1;
						}
					}
					
			
		}
		return studentData;
	}

	public static String[][] getStudentModules(Student currentStudent) {
		int row = 0;
		int rowCount = currentStudent.getModules().length;
		String[][] studentModules = new String[moduleCount][4];
		for (String i : modules.keySet()) {			//{"Programming","databases"}
			Module currentModule = modules.get(i);  
			for (String s : currentModule.getStudents()) {
				if (s == currentStudent.getId()) {
					for (int col = 0; col < 4 ; col++) {
						String id = currentModule.getModuleID();
						String name = currentModule.getModuleName();
						String grades = Arrays.toString(currentStudent.getGrades());
						switch(col) {
						case 0:
							studentModules[row][col] = id;
							break;
						case 1:
							studentModules[row][col] = name;
							break;
						case 2:
							studentModules[row][col] = "null";
							break;
						case 3:
							studentModules[row][col] = grades;
							break;
						}
					}
					row += 1;
				}
			}
		}		
			
		
		return studentModules;
	}
	
	/**
	 * METHOD TO RETURN A LIST OF INDIVIDUALS (STUDENTS OR STAFF) USED FOR JTABLES
	 * @param type - The type of individual to return the list of (Input "Student" or "Staff")
	 * @return list - 2D String Array containing each individual
	 */
	public static String[][] getList(String type) {
		String[][] list = null; //The 2D array to be returned
		int row;
		
		switch(type) { //This determines which set of individuals to return the list of
		
		//If "Student" is inputed then the following code will return all of the Students in a list
		case "Student": 
			row = 0; //Resetting the row to 0
			studentCount = students.size(); //Obtaining the amount of students there are, which will be the amount of rows in the list
			String[][] studentList = new String[studentCount][4]; //Creating the new list. Temporary list storing all students
			
			//Looping through the whole students HashMap. i representing each student each loop around
			for (String i : students.keySet()) {
				//Looping through each column, setting them one at a time
				for (int col = 0; col < 4; col++) {
					Student currentStudent = students.get(i); //Retrieving the current student's object from the HashMap
					String name = currentStudent.getName(); //Retrieving the current student's name
					String id = currentStudent.getId(); //Retrieving the current student's ID
					String course = currentStudent.getCourse(); //Retrieving the current student's course
					
					//This switch will input each retrieved bit of information into each column
					switch(col) {
					case 0: //Column 1, input ID
						studentList[row][col] = id;
						break;
					case 1: //Column 2, input name
						studentList[row][col] = name;
						break;
					case 2: //Column 3, input course
						studentList[row][col] = course;
						break;
					case 3: //Column 4, profile button
						studentList[row][col] = "Profile";
						break;
						}
					}
				row += 1; //Going to the next row in the list.
			}
			list = studentList; //Once all students have been looped through and all data is input into the list. The list information is now put into this list array which will be returned by the method
			break;
		
		/* If "Staff" is inputed to the method then the following code will return the full list of Staff members on the system
		 * Creating the staff list is the same process as creating the students list, only double the work.
		 * Since there are two types of staff individual (Tutors and Academics), and both types are stored in separate HashMaps,
		 * both HashMaps needs to be looped through and added to the same array.
		 */
		case "Staff":
			row = 0;
			staffCount = academics.size() + tutors.size();
			String[][] staffList = new String[staffCount][4];
			
			//Looping through the Academics HashMap
			for (String i : academics.keySet()) {
				for (int col = 0; col < 4; col++) {
					Academic currentAcademic = academics.get(i);
					String name = currentAcademic.getName();
					String id = currentAcademic.getStaffId();
					String office = currentAcademic.getOfficeLocation();
					switch(col) {
					case 0: //Column 1: Staff ID
						staffList[row][col] = id;
						break;
					case 1: //Column 2: Staff Name
						staffList[row][col] = name;
						break;
					case 2: //Column 3: Staff's Office Location
						staffList[row][col] = office;
						break;
					case 3: //Column 4: Staff's role position (Academic for this loop)
						staffList[row][col] = "Academic";
						}
					}
				row += 1;
			}
			
			//Looping through the Tutors HashMap
			for (String x : tutors.keySet()) {
				for (int col = 0; col < 4; col++) {
					Tutor currentTutor = tutors.get(x);
					String name = currentTutor.getName();
					String id = currentTutor.getStaffId();
					String office = currentTutor.getOfficeLocation();
					switch(col) {
					case 0: //Column 1: Staff ID
						staffList[row][col] = id;
						break;
					case 1: //Column 2: Staff Name
						staffList[row][col] = name;
						break;
					case 2: //Column 3: Staff's Office Location
						staffList[row][col] = office;
						break;
					case 3: //Column 4: Staff's role position (Tutor for this loop)
						staffList[row][col] = "Tutor";
						}
					}
				row += 1;
			}
			list = staffList;
		}
		return list;
		
	}
	
	/**
	 * The method which will show the frame for adding a new student to a module
	 * @param m (MODULE) - The current module that the GUI is working with. This is automatically passed through
	 * @return studentList - The data in which to be displayed within the JTable
	 */
	public static String[][] getUnaddedStudents(String m) {
		int row = 0;
		studentCount = students.size();
		String[][] studentList = new String[studentCount][4];
		for (String i : students.keySet()) {
			for (int col = 0; col <= students.size(); col++) {
				Student currentStudent = students.get(i);
				Module currentModule = modules.get(m);
				String name = currentStudent.getName();
				String id = currentStudent.getId();
				String course = currentStudent.getCourse();
				JButton add = new JButton("+");
				add.setName(currentStudent.getId());
				for (String s : currentModule.getStudents() ) {
					if (currentStudent.getId() == s) {
						add.setEnabled(false);
					}
				}
				add.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						currentModule.addStudent(currentStudent.getId());
						add.setEnabled(false);
					}
				});
				switch(col) {
				case 0: //Column 1
					studentList[row][col] = id;
					break;
				case 1: //Column 2
					studentList[row][col] = name;
					break;
				case 2: //Column 3
					studentList[row][col] = course;
					break;
				case 3: 
					studentList[row][col] = "+";
					break;
					}
				}
			row += 1;
		}
		
		return studentList;
	}
	
	
	
	public GUI () throws IOException {
		importStudents();
		importStaff();
		buildFrame();
	}
	
	/*
	 * Return method for the back button to take the user back to the main screen
	 */
	ActionListener returnMethod = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			cl.show(cards, "1"); //Show the main menu upon clicking the return button
		}
	};
	
	public void actionPerformed(ActionEvent e) {
		
	}
}

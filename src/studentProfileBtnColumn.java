import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

/**
 * THIS METHOD WAS CREATED WITH THE HELP OF A REFERENCE
 * This method allows for JButtons to be placed into a JTable's cell through using CellRendering and CellEditing
 * The code was not copied verbatim, the youtube link (see below) was used to follow the code from the reference and better help me understand it
 * The code was edited to best suit my program's needs.
 * 
 * Reference:
 * camposha, July 25, 2021
 * https://camposha.info/java-examples/java-jtable-buttoncolumn-tutorial/
 * 
 * Youtube Tutorial: (This contains the same code from the reference but the creator of the video just explains it line-by-line which helped me better understand it and allowed me to edit it to suit my program's needs)
 * https://www.youtube.com/watch?v=3LiSHPqbuic&list=WL&index=89
 * @author Throw
 *
 */

public class studentProfileBtnColumn {
	public Object data[][];
	public JTable table;
	public String[] studentID = {""};
	JFrame studentsList = new JFrame("Full List of Students"); //Creating the new frame
	public studentProfileBtnColumn() {
		
		//DATA FOR OUR TABLE
		this.data = GUI.getList("Student");
		
		//COLUMN HEADERS
		String[] studentColumns = {"ID","Name","Course","Profile"};
		 
		//CREATE OUR TABLE AND SET HEADERS
		this.table = new JTable(data,studentColumns);
		
		//Retrieving and storing every student ID that is listed in the table
		//This is difficult to do through the use of the Student class since there is no direct way to pass this through
		//To overcome this, I have created this for loop which will loop through every row and store each item from column 1 (the student IDs) into a new array
		//This allows for the student's ID to get added to the list of students on the current module
		int row = 0;
		for (int i = 0; i < table.getRowCount(); i++) {
			studentID = Arrays.copyOf(studentID, studentID.length + 1);
			studentID[studentID.length - 1] = data[row][0].toString();
			row += 1;
		}
		
		//SCROLLPANE, SET SIZE, SET CLOSE OPERATION
		
		studentsList.setSize(500, 500); //Setting the size of the frame, width and height in pixels
		JPanel top = new JPanel(); //Top panel in the grid - contains the search bar. This is in a panel to make easier placement
		
		//CREATING THE LAYOUT FOR THE FRAME. GridBagLayout is used to allow for the search facility to sit nicely on the top of the JTable
		GridBagLayout studentListLayout = new GridBagLayout();
		studentsList.setLayout(studentListLayout);
		//CREATING THE CONSTRAINTS AND SETTING A FEW PARAMETERS ALREADY
		GridBagConstraints studentGBC = new GridBagConstraints();
		studentGBC.fill = GridBagConstraints.BOTH; //Will fill out the x & y axis
		studentGBC.weightx = 1; //Will distribute components over the x axis evenly
		studentGBC.weighty = 1;//Will distribute components over the y axis evenly
		
		//SEARCH BAR
		JLabel search = new JLabel("Search bar will be here");
		top.add(search);
		studentGBC.gridx = 0;
		studentGBC.gridy = 0;
		studentsList.add(top, studentGBC);
		
		//TABLE
		JScrollPane scrollStudents = new JScrollPane(table);
		studentGBC.gridx = 0;
		studentGBC.gridy = 1;
		studentsList.add(scrollStudents, studentGBC);
		
		//ADD+REMOVE
		JPanel settings = new JPanel(new FlowLayout());
		JButton addStudent = new JButton("+");
		JButton removeStudent = new JButton("-");
		settings.add(addStudent);
		settings.add(removeStudent);
		studentGBC.gridx = 0;
		studentGBC.gridy = 2;
		studentGBC.gridheight = 1;
		studentGBC.weighty = 0;
		studentsList.add(settings, studentGBC);
		
		addStudent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame prompt = new JFrame("Add a new student"); //Creating the new frame window for input
				prompt.setSize(300, 200); //Setting the size, width and height in pixels
				prompt.setLayout(null); //Null layout - for such a small window it is simpler to just position the components manually
				
				//Setting up the text fields where the user will input their data
				JTextField n = new JTextField(); 
				JTextField a = new JTextField();
				JTextField em = new JTextField();
				JTextField c = new JTextField();
				//Setting the position and size of the text fields - xPosition, yPosition, width, height
				n.setBounds(110, 10, 160, 20);
				a.setBounds(110, 40, 160, 20);
				em.setBounds(110, 70, 160, 20);
				c.setBounds(110, 100, 160, 20);
				
				//Setting up the labels that indicate which text field is for what input
				JLabel name = new JLabel("Name:");
				JLabel age = new JLabel("Age:");
				JLabel email = new JLabel("Email:");
				JLabel course = new JLabel("Course:");
				//Setting the position and size of the labels - xPosition, yPosition, width, height
				name.setBounds(15, 10, 100, 20);
				age.setBounds(15, 40, 100, 20);
				email.setBounds(15, 70, 100, 20);
				course.setBounds(15, 100, 100, 20);
				
				//Creating the submit button
				JButton submit = new JButton("Submit");
				submit.setBounds(90, 130, 100, 20); //Setting the position and size of the button - xPosition, yPosition, width, height
				submit.addActionListener(new ActionListener() { //Event that will take place upon clicking
					public void actionPerformed(ActionEvent e) {
						String name = n.getText();
						int age = Integer.parseInt(a.getText());
						String email = em.getText();
						String course = c.getText();
						GUI.addStudent(name, age, email, course); //Calling the addStaff() method with the passed data
						data = GUI.getList("Student");
						DefaultTableModel updatedModel = new DefaultTableModel(data, studentColumns);
						table.setModel(updatedModel);
						//SET CUSTOM RENDERER TO THE BUTTONS COLUMN
						table.getColumnModel().getColumn(3).setCellRenderer(new ProfileButtonRenderer(studentID));
								
						//SET CUSTOM EDITOR TO BUTTONS COLUMN
						table.getColumnModel().getColumn(3).setCellEditor(new ProfileButtonEditor(new JTextField(), studentID));
						prompt.setVisible(false); //"Hiding" the input window
						prompt.dispose(); //Essentially deleting the window so it isn't taking up resources
						table.repaint();
					}
				});
				//Adding all created components to the window
				prompt.add(n);
				prompt.add(name);
				prompt.add(a);
				prompt.add(age);
				prompt.add(em);
				prompt.add(email);
				prompt.add(c);
				prompt.add(course);
				prompt.add(submit);
				//Setting the window to be visible
				
				prompt.setVisible(true); 
			}
			
		});
		removeStudent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Frame prompt = new JFrame("Remove a student from the system"); //Creating the new frame window for input
				prompt.setSize(300, 120); //Setting the size, width and height in pixels
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
						GUI.students.remove(staffID);
						data = GUI.getList("Student");
						DefaultTableModel updatedModel = new DefaultTableModel(data, studentColumns);
						table.setModel(updatedModel);
						//SET CUSTOM RENDERER TO THE BUTTONS COLUMN
						table.getColumnModel().getColumn(3).setCellRenderer(new ProfileButtonRenderer(studentID));
								
						//SET CUSTOM EDITOR TO BUTTONS COLUMN
						table.getColumnModel().getColumn(3).setCellEditor(new ProfileButtonEditor(new JTextField(), studentID));
						prompt.setVisible(false); //"Hiding" the input window
						prompt.dispose(); //Essentially deleting the window so it isn't taking up resources
						table.repaint();
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
		
		//SET CUSTOM RENDERER TO THE BUTTONS COLUMN
		table.getColumnModel().getColumn(3).setCellRenderer(new ProfileButtonRenderer(studentID));
				
		//SET CUSTOM EDITOR TO BUTTONS COLUMN
		table.getColumnModel().getColumn(3).setCellEditor(new ProfileButtonEditor(new JTextField(), studentID));
		
		studentsList.pack();
		studentsList.setVisible(true); //Making the frame visible after setup  */
		
		
	}
	
	public void disposeList() {studentsList.dispose();}
	
}



//Button Renderer Class
class ProfileButtonRenderer extends JButton implements TableCellRenderer {

	//CONSTRUCTOR
	public ProfileButtonRenderer(String[] ids) {
		//Set the button properties
		setOpaque(true);
		
		
		
	}
	@Override
	public Component getTableCellRendererComponent(JTable table, Object obj, boolean selected, boolean focussed,
			int row, int col) {
		
		//Set the passed object as button text
		setText((obj==null) ? "":obj.toString());
		
		return this;
	}
	
}

//BUTTON EDITOR CLASS
class ProfileButtonEditor extends DefaultCellEditor {
	
	protected JButton btn; //The button
	private String lbl; //This will be the text on the button
	private Boolean clicked; //Determines when the button has been clicked
	protected int currentRow; //Indicates the row that the methods are dealing with
	public static String studentID; //Stores the Student's ID to display on the message dialog box
	private Student currentStudent = null;
	
	public ProfileButtonEditor(JTextField txt, String[] ids) {
		super(txt);
		
		btn = new JButton();
		
		//When button is clicked
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				fireEditingStopped();
			}
			
		});
		
	}
	
	//OVERRIDE A COUPLE OF METHODS
	public Component getTableCellEditorComponent(JTable table, Object obj, boolean selected, int row, int col) {
		
		//SET BUTTON TEXT, SET CLICKED TO TRUE, THEN RETURN THE BUTTON OBJECT
		lbl = (obj==null) ? "" : obj.toString();
		btn.setText(lbl);
		clicked  = true;
		currentRow = row + 1;
		studentID = table.getModel().getValueAt(currentRow - 1, 0).toString(); //Retrieves Student's ID
		return btn;
	}
	
	//IF THE BUTTON CELL VALUE CHANGES< IF CLICKED THAT IS
	public Object getCellEditorValue() {
		
		if(clicked) { //THE EVENT IN WHICH WILL TAKE PLACE WHEN CLICKING EACH + BUTTON
			Student currentStudent = GUI.students.get(studentID);
			GUI.studentProfile(currentStudent);
		}
		
		//SET IT TO FALSE NOW THAT IT IS CLICKED
		clicked = false;
		return new String(lbl);
	}
	
	
	public boolean stopCellEditing() {
		//SET CLICKED TO FALSE
		clicked = false;
		return super.stopCellEditing();
	}
	
	protected void fireEditingStopped() {
		super.fireEditingStopped();
	}
	
	public String getStudentID() {return studentID;}
	
}

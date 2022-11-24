import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
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

public class ButtonColumn extends JFrame {
	public Object data[][];
	public JTable table;
	public String[] studentID = {""};
	public ButtonColumn(String currentModule) {
		//FORM TITLE
		super("Add Students to this Module");
		
		//DATA FOR OUR TABLE
		this.data = GUI.getUnaddedStudents(currentModule);
		
		//COLUMN HEADERS
		String columnHeaders[] = {"ID","Name","Course", " "};
		 
		//CREATE OUR TABLE AND SET HEADERS
		this.table = new JTable(data,columnHeaders);
		
		//Retrieving and storing every student ID that is listed in the table
		//This is difficult to do through the use of the Student class since there is no direct way to pass this through
		//To overcome this, I have created this for loop which will loop through every row and store each item in column 1 (the student IDs) into a new array
		//This allows for the student's ID to get added to the list of students on the current module
		int row = 0;
		for (int i = 0; i < table.getRowCount(); i++) {
			studentID = Arrays.copyOf(studentID, studentID.length + 1);
			studentID[studentID.length - 1] = data[row][0].toString();
			row += 1;
		}
		
		//SET CUSTOM RENDERER TO THE BUTTONS COLUMN
		table.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer(studentID));
		
		//SET CUSTOM EDITOR TO BUTTONS COLUMN
		table.getColumnModel().getColumn(3).setCellEditor(new ButtonEditor(new JTextField(), studentID));
		
		//SCROLLPANE, SET SIZE, SET CLOSE OPERATION
		JScrollPane pane = new JScrollPane(table);
		JButton add = new JButton();
		add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
			
		});
		getContentPane().add(pane);
		setSize(450,300);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		
	}
	
}



//Button Renderer Class
class ButtonRenderer extends JButton implements TableCellRenderer {

	//CONSTRUCTOR
	public ButtonRenderer(String[] ids) {
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
class ButtonEditor extends DefaultCellEditor {
	
	protected JButton btn; //The button
	private String lbl; //This will be the text on the button
	private Boolean clicked; //Determines when the button has been clicked
	protected int currentRow; //Indicates the row that the methods are dealing with
	protected String studentID; //Stores the Student's ID to display on the message dialog box
	protected String studentName; //Stores the Student's name to display on the message dialog box
	protected static String[] addedStudents = {}; //List of students to be added once update button is clicked
	
	public ButtonEditor(JTextField txt, String[] ids) {
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
		studentName = table.getModel().getValueAt(currentRow - 1, 1).toString(); //Retrieves Student's name
		return btn;
	}
	
	//IF THE BUTTON CELL VALUE CHANGES< IF CLICKED THAT IS
	public Object getCellEditorValue() {
		
		if(clicked) { //THE EVENT IN WHICH WILL TAKE PLACE WHEN CLICKING EACH + BUTTON
			JOptionPane.showMessageDialog(btn, studentName + " added. " + " ID: " + studentID);
			addedStudents = Arrays.copyOf(addedStudents, addedStudents.length + 1);
			addedStudents[addedStudents.length - 1] = studentID;
			
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
	
	static public String[] getAddedStudents() {return addedStudents;} //Used to retrieve the array of students to be added
	static public void resetAddedStudents() {Arrays.fill(addedStudents, null);} //Empties the array. If this is done through {addedStudents = null;} then it is no longer usable by other methods
	
}
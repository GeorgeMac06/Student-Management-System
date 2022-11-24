import java.util.Arrays;

/**
 * MODULE CLASS
 * This class will be the base for every Module inputed into the system
 * @author George Macdonald
 */
public class Module {

//	-=VARIABLE DECLARATIONS=-
	private String moduleName; /* String variable to store the name of the module */
	private String moduleLeader; /* String variable to store the name of the module leader */
	private String moduleID; /* The unique identifier of the module. This will be automatically given in the constructor ADD THIS TO THE DESIGN */
	private String[] moderators = {"No Moderators Set"}; //THIS NEEDS TO BE ADDED TO THE DESIGN
	private String[] students = new String[0];
	

	
//	-=GETTERS=-
	public String getModuleName() {return this.moduleName;} /* Will return the name of the module */
	public String getModuleLeader() {return this.moduleLeader;} /* Will return the name of the module leader */
	public String getModuleID() {return this.moduleID;} /* Will return the module's unique identifier ADD THIS TO THE DESIGN */
	public String getModerators() {
		String mods = Arrays.toString(moderators);
		return mods;
	}
	public String[] getStudents() {return this.students;}
	
	
//	-=SETTERS=- (includes add/remove/clear moderator/students arrays)
	public void setModuleLeader(String l) {this.moduleLeader = l;} /* Sets the name of the module leader to the inputed name */
	public void setModuleName(String n) {this.moduleName = n;} //THIS NEEDS TO BE ADDED TO DESIGN DIAGRAMS
	
	//CONFIGURING MODERATORS
	public void addModerator(String m) {
		this.moderators = Arrays.copyOf(moderators, moderators.length + 1);
		moderators[moderators.length - 1] = m;
	}
	public void removeModerator(String m) {
		int index = 0;
		int i = 0;
		while (i < moderators.length) {
			if (moderators[i] == m) {index = i;} else {i += 1;}
		}
		for (int x = index; i < moderators.length - 1; x++) {
		    moderators[x] = moderators[x + 1];
		}
	}
	public void clearModerators() {moderators = null;}
	
	//CONFIGURING STUDENTS
	public void addStudent(String s) {
		Boolean flag = false; //This ensures a student can't be on the list twice
		for (String i : students) { //Loop through array to check for presence already
			if (i == s) { //If the loop finds a presence, the flag will be raised to true.
				flag = true; //The student won't be added if the flag is true.
			}
		}
		if (flag == false) { //If flag is false; the student is not already in the array
			this.students = Arrays.copyOf(students, students.length + 1);
			students[students.length - 1] = s;
		}
		
	}
	public void removeStudent(String s) {
		int index = 0;
		int i = 0;
		while (i < students.length) {
			if (moderators[i] == s) {index = i;} else {i += 1;}
		}
		for (int x = index; i < students.length - 1; x++) {
		    students[x] = students[x + 1];
		}
	}

//	-=CONSTRUCTOR=-
	/**
	 * Constructor to make a new object for the module
	 * @param n (NAME) - String input to set the name of the module
	 * @param l (LEADER) - String input to set the name of the module leader
	 */
	public Module(String n, String l) {
		this.moduleName = n;
		this.moduleLeader = l;
		
		//Giving the module a unique ID
		this.moduleID = "CS00"+ String.valueOf(GUI.moduleCount); //ADD THIS TO THE DESIGN
	}
}

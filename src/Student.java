import java.util.Arrays;

/**
 * STUDENT CLASS
 * This class will be the base for every student inputed into the system
 * Inherits Name, Age and Email from the PERSON class so that it does not have to be written again
 * @author George Macdonald
 */
public class Student extends Person{

//	-=VARIABLE DECLARATIONS=-
	private String studentId; /* Integer variable to store the student's ID - This is automatically generated within the constructor*/
	private String course;
	private String[] grades;
	private String[] classes;
	private String[] modules = {};
	
	
//	-=GETTERS=- Methods to retrieve information from the class
	public String getId() {return this.studentId;} //Returns the Student's ID in an Integer variable
	public String[] getGrades() {return this.grades;}
	public String[] getClasses() {return this.classes;}
	public String getCourse() {return this.course;}
	public String[] getModules() {return this.modules;}
	
	
//	-=SETTERS=- Methods to update information in the class variables. Includes adding/removing of classes, grades & modules
	public void addClass(String c) {
		this.classes = Arrays.copyOf(classes, classes.length + 1);
		classes[classes.length - 1] = c;
	}
	public void setCourse(String c) {this.course = c;}
	
	//CONFIGURING MODULES
	public void addModule(String m) {
		Boolean flag = false; //This ensures a module can't be on the list twice
		for (String i : modules) { //Loop through array to check for presence already
			if (i == m) { //If the loop finds a presence, the flag will be raised to true.
				flag = true; //The student won't be added if the flag is true.
			}
		}
		if (flag == false) { //If flag is false; the module is not already in the array
			this.modules = Arrays.copyOf(modules, modules.length + 1);
			modules[modules.length - 1] = m;
		}
		
	}
	public void removeModule(String m) {
		int index = 0;
		int i = 0;
		while (i < modules.length) {
			if (modules[i] == m) {index = i;} else {i += 1;}
		}
		for (int x = index; i < modules.length - 1; x++) {
		    modules[x] = modules[x + 1];
		}
	}

	
//	-=CONSTRUCTOR=-
	/**
	 * Constructor to construct the object for the Student
	 * @param n (NAME) - String input for the name of the student
	 * @param a (AGE) - Integer input for the age of the student
	 * @param e (EMAIL) - String input for the email of the student
	 * @param c (COURSE) - String input for the course of the student
	 */
	public Student(String n, int a, String e, String c) {
		super(n, a, e);
		this.studentId = "EHU"+ String.valueOf(GUI.studentCount);
		this.course = c;
	}
}

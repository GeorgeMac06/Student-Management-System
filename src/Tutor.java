/**
 * TUTOR CLASS
 * This class will be the base for every tutor inputed into the system
 * Inherits Staff ID & Office Location directly from the STAFF class
 * Inherits Name, Age and Email from the PERSON class via the STAFF class
 * @author George Macdonald
 */
public class Tutor extends Staff {
	
//	-=VARIABLE DECLARATIONS=-
	private String[] studentsSupervised = {"George Macdonald"}; /* String Array to store the list of students the tutor supervises */
	
//	-=GETTERS=- Methods to retrieve information from the class
	public String[] getStudentsSupervised() {return this.studentsSupervised;}
	
//	-=SETTERS=- Methods to update information in the class variables
	public void setStudentsSupervised(String[] students) {this.studentsSupervised = students;}
	
//	-=CONSTRUCTOR=-
	public Tutor(String n, int a, String e, String o) {
		super(n, a, e, o);
	}

}

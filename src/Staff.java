/**
 * STAFF CLASS
 * This class will be the base for every staff member inputed into the system
 * Inherits Name, Age and Email from the PERSON class so that it does not have to be written again
 * @author George Macdonald
 */
public class Staff extends Person{

	//	-=VARIABLE DECLARATIONS=-
	private String staffId; /* Integer variable to store the staff member's ID */
	private String officeLocation; /* String variable to store the staff member's office location */
	
//	-=GETTERS=- Methods to retrieve information from the class
	public String getStaffId() {return this.staffId;} //Returns the staff member's ID in a String variable
	public String getOfficeLocation() {return this.officeLocation;} //Returns the staff member's Office location in a String variable
	
//	-=SETTERS=- Methods to update information in the class variables
	public void setOfficeLocation(String o) {this.officeLocation = o;} //Sets the office location to the passed value
	
//	-=CONSTRUCTOR=-
	/**
	 * Constructor to construct the staff member
	 * @param n (NAME) - String input for the name of the staff member
	 * @param a (AGE) - Integer input for the age of the staff member
	 * @param e (EMAIL) - String input for the email of the staff member
	 * @param o (OFFICE LOCATION) - String input for the office location of the staff member
	 */
	public Staff(String n, int a, String e, String o) {
		super(n, a, e);
		this.staffId = "EHUS" + String.valueOf(GUI.staffCount); //The "S" stands for Staff
		this.officeLocation = o;
	}
}

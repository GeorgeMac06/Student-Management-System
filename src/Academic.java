import java.util.Arrays;

/**
 * ACADEMIC CLASS
 * This class will be the base for every academic inputed into the system
 * Inherits Staff ID & Office Location directly from the STAFF class
 * Inherits Name, Age and Email from the PERSON class via the STAFF class
 * @author George Macdonald
 */
public class Academic extends Staff {

//	-=VARIABLE DECLARATIONS=-
	private String[] subjects = {}; //String array to store the list of subjects the Academic teaches
	
//	-=GETTERS=-
	public String[] getSubject() {return this.subjects;} //Returns the string array containing the list of subjects taught by the academic
	
//	-=SETTERS=-
	/**
	 * Add a subject to the list of the Academic's teaching subjects
	 * @param s (SUBJECT) - String input for the subject to be added
	 */
	public void addSubject(String s) {
		this.subjects = Arrays.copyOf(subjects, subjects.length + 1);
		subjects[subjects.length - 1] = s;
	}
	/**
	 * Remove a subject from the list of the Academic's teaching subjects
	 * @param s (SUBJECT) - String input for for subject to be removed
	 */
	public void removeSubject(String s) {
		int index = 0;
		int i = 0;
		while (i < subjects.length) {
			if (subjects[i] == s) {index = i;} else {i += 1;}
		}
		for (int x = index; i < subjects.length - 1; x++) {
			subjects[x] = subjects[x + 1];
		}
	}
	
//	-=CONSTRUCTOR=-
	/**
	 * Constructor to construct the object for the staff
	 * @param n (NAME) - String input for the name of the staff
	 * @param a (AGE) - Integer input for the age of the staff
	 * @param e (EMAIL) - String input for the email of the staff
	 * @param o (OFFICE LOCATION) - String input for the office location of the staff
	 */
	public Academic(String n, int a, String e, String o) {
		super(n, a, e, o);
	}
}

/**
 * PERSON CLASS
 * This class will be the base for every individual inputed into the system
 * Allows for less code to be written since every individual will have a name, age and an email
 * @author George Macdonald
 */
public class Person {
	
//	-=VARIABLE DECLARATIONS=-
	private String name; /*String variable to store the person's name*/
	private int age; /*Integer variable to store the person's age*/
	private String email; /*String variable to store the person's email*/
	
//	-=GETTERS=- Methods to retrieve information from the class
	public String getName() {return this.name;} //Returns the person's name in a String variable
	public String getEmail() {return this.email;} //Returns the person's email in a String variable
	public int getAge() {return this.age;} //Returns the person's age in an Integer variable
	
//	-=SETTERS=- Methods to update information in the class variables
	public void setName(String n) {this.name = n;} //Sets the person's name to the passed value
	public void setEmail(String e) {this.email = e;} //Sets the person's email to the passed value
	
//	-=CONSTRUCTOR=-
	/**
	 * Constructor to construct a person object
	 * @param n (NAME)- String input for the name of the person
	 * @param a (AGE) - Integer input for the age of the person
	 * @param e (EMAIL) - String input for the email of the person
	 */
	public Person(String n, int a, String e) {
		this.name = n;
		this.age = a;
		this.email = e;
	}
}

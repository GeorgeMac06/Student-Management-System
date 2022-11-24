import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

public class StudentManagementSystem {

	public static void main(String[] args) throws IOException {
		//Creating the GUI
		GUI program = new GUI();
		
		//Creating some example modules
		program.createModule("Programming", "Robert Lyon");
		program.createModule("Databases", "Nonso Nnamoko");
		program.createModule("Employability", "Collette Gavan");

		/**REFERENCE - SAVING STAFF AND STUDENT RECORDS TO FILE ON CLOSE
		 * 
		 * Line 23 was taken from this reference to help me learn how to save to a file on program close
		 * The code between the run's body is my own.
		 * 
		 * Walter, M. December 28 2015 [online]
		 * https://stackoverflow.com/questions/34491793/how-to-get-java-to-save-data-when-you-exit-a-program
		 * Accessed at: 15/12/21
		 */
		Runtime.getRuntime().addShutdownHook(new Thread() {
			//This will create a print writer, overwrite both data files with the current contents of the HashMaps at time of close, in the correct format.
	        public void run(){
	        	
	        	//Creating the PrintWriter that will write to the file. Surrounded with try/catch
	        	PrintWriter studentWriter = null;
				try {
					studentWriter = new PrintWriter("./studentData.txt"); //Loading the file into the PrintWriter
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//Looping through the Student HashMap
				for (String id : GUI.students.keySet()) {
					Student currentStudent = GUI.students.get(id);
					//Printing each object from the hashmap into the correct format. This overwrites everything already in the file but will also re-write any information that is meant to stay
					studentWriter.print(currentStudent.getName() + "," + currentStudent.getAge() + "," + currentStudent.getEmail() + "," + currentStudent.getCourse() + "\n");
				}
				studentWriter.close();
				
				PrintWriter staffWriter = null;
				try {
					staffWriter = new PrintWriter("./staffData.txt"); //Loading the file into the PrintWriter
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//Looping through the Student HashMap
				for (String id : GUI.tutors.keySet()) {
					Tutor currentTutor = GUI.tutors.get(id);
					//Printing each object from the hashmap into the correct format. This overwrites everything already in the file but will also re-write any information that is meant to stay
					staffWriter.print(currentTutor.getName() + "," + currentTutor.getAge() + "," + currentTutor.getEmail() + "," + currentTutor.getOfficeLocation() + "," + "Tutor" + "\n");
				}
				for (String id : GUI.academics.keySet()) {
					Academic currentAcademic = GUI.academics.get(id);
					//Printing each object from the hashmap into the correct format. This overwrites everything already in the file but will also re-write any information that is meant to stay
					staffWriter.print(currentAcademic.getName() + "," + currentAcademic.getAge() + "," + currentAcademic.getEmail() + "," + currentAcademic.getOfficeLocation() + "," + "Academic" + "\n");
				}
				staffWriter.close();
	        }
	    });
		
			
	}

}

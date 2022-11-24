/**
 * CLASSES CLASS
 * This class will be the base for every class inputed into the system
 * Contains methods to do tasks such as calculate the class average and storing/retrieving the number of students
 * @author George Macdonald
 */
public class Classes {
	
//	-=VARIABLE DECLARATIONS=-
	private int classId;
	private int classCount;
	
//	-=GETTERS=-
	public int getClassId() {return this.classId;}
	public int getClassCount() {return this.classCount;}
	
//	-=SETTERS=-
	public void setClassCount(int c) {this.classCount = c;}
	
//	-=METHODS=- 
	/**
	 * This method calculates the class average from an array of scores
	 * Contains a for loop which will add all of the scores together
	 * After the for loop has finished, the scores are divided by the length of the array, which is then rounded to two decimal places.
	 * @param scores - an array containing double values representing each score inputed
	 * @return - Returns the final calculated class average, rounded to two decimal places
	 */
	public double classAverage(double[] scores) {
		double average = 0.0;
		double calc = 0.0;
		for (double score: scores) {
			calc += score;
		}
		average = calc / (scores.length);
		average = Math.round(average*100.0)/100.0;
		return average;
	}
	
//	-=CONSTRUCTOR=-
	/**
	 * Constructor used to make a new object
	 * @param i (ID) - Integer input for the ID of the class
	 * @param c (COUNT) - Integer input for the count of students in the class
	 */
	public Classes() {
	}
}

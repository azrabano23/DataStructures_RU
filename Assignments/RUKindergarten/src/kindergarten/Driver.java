package kindergarten;

/**
 * This class is designed to test each method in the Classroom file
 * interactively.
 * 
 */

public class Driver {
	public static void main(String[] args) {
		// Lists an array of all methods to test.
		String[] methods = { "enterClassroom", "createSeats", "seatStudents",
				"insertMusicalChairs", "moveStudentFromChairsToLine", "eliminateLosingStudents",
				"seatMusicalChairsWinner", "playMusicalChairs" };
		// Allows students to start over, test a new method, or quit
		String[] options = { "Test a new input file", "Test another method on the same file", "Quit" };
		// Start by testing a new input file
		int controlChoice = 1;
		// Driver initializes a new Classroom object
		Classroom studentClassroom = new Classroom();

		do {
			// Reads input file name
			StdOut.print("Enter a student info input file => ");
			String inputFile = StdIn.readLine();

			do {
				StdOut.println("\nWhat method would you like to test?");
				for (int i = 0; i < methods.length; i++) {
					StdOut.printf("%d. %s\n", i + 1, methods[i]);
				}
				StdOut.print("Enter a number => ");
				int choice = Integer.parseInt(StdIn.readLine());
				switch (choice) {
					case 1:
						// enterClassroom1
						// Starts over
						studentClassroom = testEnterClassroom(inputFile);
						studentClassroom.printClassroom();
						break;
					case 2:
						// createSeats
						StdOut.print("Enter a seating availability input file => ");
						String seating = StdIn.readLine();
						testCreateSeats(studentClassroom, seating);
						studentClassroom.printClassroom();
						break;
					case 3:
						// seatStudents
						testSeatStudents(studentClassroom);
						studentClassroom.printClassroom();
						break;
					case 4:
						// insertMusicalChairs
						testInsertMusicalChairs(studentClassroom);
						studentClassroom.printClassroom();
						break;
					case 5:
						// moveStudentFromMusicalChairsToLine
						StdOut.print("Enter the size of the circular linked list --> ");
						int size = Integer.parseInt(StdIn.readLine());
						testMoveStudentFromChairsToLine(studentClassroom, size);
						studentClassroom.printClassroom();
						break;
					case 6:
						// eliminateLosingStudents
						testEliminateLosingStudents(studentClassroom);
						studentClassroom.printClassroom();
						break;
					case 7:
						// seatMusicalChairsWinner
						testSeatMusicalChairsWinner(studentClassroom);
						studentClassroom.printClassroom();
						break;
					case 8:
						// playMusicalChairs -- calls all musical chairs methods in sequence
						StdRandom.setSeed(2025);
						testPlayMusicalChairs(studentClassroom);
						studentClassroom.printClassroom();
						break;
					default:
						StdOut.println("Not a valid option!");
				}
				StdIn.resync();
				// Prompts students to select an option to continue
				StdOut.println("What would you like to do now?");
				for (int i = 0; i < 3; i++) {
					StdOut.printf("%d. %s\n", i + 1, options[i]);
				}
				StdOut.print("Enter a number => ");
				controlChoice = Integer.parseInt(StdIn.readLine());
			} while (controlChoice == 2);
		} while (controlChoice == 1);
	}

	/**
	 * Caller method for enterClassroom method
	 * @param filename the info input file name
	 * @return the newly created classroom
	 */
	private static Classroom testEnterClassroom(String filename) {
		// Call student's enterClassroom method
		Classroom studentClassroom = new Classroom();
		studentClassroom.enterClassroom(filename);
		return studentClassroom;
	}

	/**
	 * Caller method for createSeats method
	 * @param studentClassroom the student classroom object
	 * @param filename the seating file name
	 */
	private static void testCreateSeats(Classroom studentClassroom, String filename) {
		studentClassroom.createSeats(filename);
	}

	/**
	 * Caller method for seatStudents method
	 * @param studentClassroom the student classroom object
	 */
	private static void testSeatStudents(Classroom studentClassroom) {
		studentClassroom.seatStudents();
	}

	/**
	 * Caller method for insertMusicalChairs method
	 * @param studentClassroom the student classroom object
	 */
	private static void testInsertMusicalChairs(Classroom studentClassroom) {
		studentClassroom.insertMusicalChairs();
	}

	/**
	 * Caller method for eliminateLosingStudents method
	 * @param studentClassroom the student classroom object
	 */
	private static void testEliminateLosingStudents(Classroom studentClassroom) {
		StdRandom.setSeed(2025);
		studentClassroom.eliminateLosingStudents();
	}

	/**
	 * Caller method for moveStudentFromChairsToLine method
	 * @param studentClassroom the student classroom object
	 * @param size the size of the linked list
	 */
	private static void testMoveStudentFromChairsToLine(Classroom studentClassroom, int size) {
		StdRandom.setSeed(2025);
		studentClassroom.moveStudentFromChairsToLine(size);
	}

	/**
	 * Caller method for seatMusicalChairsWinner method
	 * @param studentClassroom the student classroom object
	 */
	private static void testSeatMusicalChairsWinner(Classroom studentClassroom) {
		studentClassroom.seatMusicalChairsWinner();
	}

	/**
	 * Caller method for playMusicalChairs method
	 * @param studentClassroom the student classroom object
	 */
	private static void testPlayMusicalChairs(Classroom studentClassroom) {
		StdRandom.setSeed(2025);
		studentClassroom.playMusicalChairs();
	}
}

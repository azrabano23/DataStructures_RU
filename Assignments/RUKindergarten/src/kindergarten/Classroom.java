package kindergarten;

/**
 * You're a kindergarten teacher and you have many students to teach.
 * You'll supervise students when they line up, when they're in class,
 * and when they're playing musical chairs!
 * 
 * @author Ethan Chou
 * @author Kal Pandit
 * @author Maksims Kurjanovics Kravcenko
 */
public class Classroom {
    private SNode studentsInLine; // when students are in line - refers to the front of a singly linked list
    private SNode musicalChairs; // when students are in musical chairs: holds a reference to the LAST student in
                                 // the CLL
    private boolean[][] openSeats; // represents seat positions in the classroom
    private Student[][] studentsInSeats; // represents students in their corresponding seats

    /**
     * Constructor for classrooms. Do not edit.
     * 
     * @param inLine          passes in students in line
     * @param mChairs         passes in musical chairs
     * @param seats           passes in availability
     * @param studentsSitting passes in students sitting
     */
    public Classroom(SNode inLine, SNode mChairs, boolean[][] seats, Student[][] studentsSitting) {
        studentsInLine = inLine;
        musicalChairs = mChairs;
        openSeats = seats;
        studentsInSeats = studentsSitting;
    }

    /**
     * Default constructor starts an empty classroom. Do not edit.
     */
    public Classroom() {
        this(null, null, null, null);
    }

    /**
     * This method simulates students filing in line, before they enter the
     * classroom.
     * 
     * It does this by reading students from an input file and adding each to the
     * front
     * of the studentsInLine linked list.
     * 
     * 1. Open the file using StdIn.setFile(filename);
     * 
     * 2. For each line of the input file:
     * 1. read a student from the file
     * 2. create a Student object with the student's info
     * 3. insert the Student to the FRONT of studentsInLine
     * 
     * Students are stored in reverse alphabetical order in the input file.
     * This method will put students in A-Z order.
     * 
     * DO NOT implement a sorting method, PRACTICE add to front.
     * 
     * @param filename the student input file
     */
    public void enterClassroom(String filename) {
        StdIn.setFile(filename);
        
        int numStudents = StdIn.readInt();
        
        for (int i = 0; i < numStudents; i++) {
            String firstName = StdIn.readString();
            String lastName = StdIn.readString();
            int height = StdIn.readInt();
            
            Student newStudent = new Student(firstName, lastName, height);
            
            SNode newNode = new SNode(newStudent, null);
            
            newNode.setNext(studentsInLine);
            studentsInLine = newNode;
        }
    }
    /**
     * 
     * This method creates the open seats in the classroom.
     * 
     * 1. Open the file using StdIn.setFile(seatingChart);
     * 
     * 2. You will read the seating input file with the format:
     * An integer representing the number of rows in the classroom
     * An integer representing the number of columns in the classroom
     * Number of r lines, each containing c true or false values (true represents
     * that a
     * seat is present in that column)
     * 
     * 3. Initialize openSeats and studentsInSeats arrays with r rows and c columns
     * 
     * 4. Update studentsInSeats with the booleans read from the input file
     * 
     * This method does not seat students in the seats.
     * 
     * @param openSeatsFile the seating chart input file
     */
    public void createSeats(String openSeatsFile) {
        StdIn.setFile(openSeatsFile);
    
        int rows = StdIn.readInt();
        int cols = StdIn.readInt();
    
        openSeats = new boolean[rows][cols];
        studentsInSeats = new Student[rows][cols];
    
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                openSeats[i][j] = StdIn.readBoolean();
            }
        }
    }    

    /**
     * 
     * This method simulates students moving from the line to their respective
     * seats.
     * 
     * Students are removed one by one from the line and inserted into
     * studentsInSeats
     * according to the seating positions in openSeats.
     * 
     * studentsInLine will then be empty at the end of this method.
     * 
     * NOTE: If the students just played musical chairs, the winner of musical
     * chairs is seated separately
     * by seatMusicalChairsWinner().
     */
    public void seatStudents() {
        SNode currentStudentNode = studentsInLine;
        studentsInLine = null; // Students will all be seated, so clear the line
    
        for (int i = 0; i < openSeats.length; i++) {
            for (int j = 0; j < openSeats[i].length; j++) {
                if (openSeats[i][j] && studentsInSeats[i][j] == null && currentStudentNode != null) {
                    studentsInSeats[i][j] = currentStudentNode.getStudent();
                    currentStudentNode = currentStudentNode.getNext();
                }
            }
        }
    }
    

    /**
     * Traverses studentsInSeats row by row, column by column removing a student
     * from their seat and adding them to the END of the musical chairs CLL.
     * 
     * NOTE: musicalChairs refers to the LAST student in the CLL.
     */
    public void insertMusicalChairs() {
        musicalChairs = null;
    
        for (int i = 0; i < studentsInSeats.length; i++) {
            for (int j = 0; j < studentsInSeats[i].length; j++) {
                if (studentsInSeats[i][j] != null) {
                    SNode newNode = new SNode(studentsInSeats[i][j], null);
    
                    if (musicalChairs == null) {
                        musicalChairs = newNode;
                        musicalChairs.setNext(musicalChairs); 
                    } else {
                        newNode.setNext(musicalChairs.getNext()); 
                        musicalChairs.setNext(newNode); 
                        musicalChairs = newNode; 
                    }
    
                    studentsInSeats[i][j] = null;
                }
            }
        }
    }    

    /**
     * 
     * Removes a random student from the musicalChairs, if they can't find a seat.
     * Once eliminated students go back to the line.
     * 
     * @param size represents the number of students currently sitting in
     *             musicalChairs
     * 
     *             1. Use StdRandom.uniform(size) to pick the nth student (n=0 =
     *             student at front).
     *             2. Search for the selected student, and delete them from
     *             musicalChairs
     *             3. Call insertByName to insert the deleted student back to the
     *             line.
     * 
     *             The random value denotes the refers to the position of the
     *             student to be removed
     *             in the musicalChairs. 0 is the first student
     */
    public void moveStudentFromChairsToLine(int size) {
        if (musicalChairs == null || size == 0) {
            return; 
        }
    
        int n = StdRandom.uniform(size);
    
        SNode prev = musicalChairs;
        SNode current = musicalChairs.getNext(); 
    
        if (n == 0) {  
            if (musicalChairs == musicalChairs.getNext()) {  
                insertByName(musicalChairs.getStudent());
                musicalChairs = null;
            } else {
                while (prev.getNext() != musicalChairs) {
                    prev = prev.getNext();
                }
                insertByName(musicalChairs.getStudent());
                prev.setNext(musicalChairs.getNext());  
                musicalChairs = musicalChairs.getNext();
            }
            return;
        }
    
        for (int i = 0; i < n; i++) {
            prev = current;
            current = current.getNext();
        }
    
        insertByName(current.getStudent()); 
    
        prev.setNext(current.getNext());
    
        if (current == musicalChairs) {
            musicalChairs = prev;
        }
    }

    /**
     * Inserts a single student, eliminated from musical chairs, to the line
     * inserted
     * in ascending order by last name and then first name if students have the same
     * last name
     * 
     * USE compareNameTo on a student: < 0 = less than, > 0 greater than, = 0 equal
     * 
     * @param eliminatedStudent the student eliminated from chairs to insert
     */
    public void insertByName(Student eliminatedStudent) {
        SNode newNode = new SNode(eliminatedStudent, null);
    
        if (studentsInLine == null) {
            studentsInLine = newNode;
            return;
        }
            if (eliminatedStudent.compareNameTo(studentsInLine.getStudent()) < 0) {
            newNode.setNext(studentsInLine);
            studentsInLine = newNode;
            return;
        }
    
        SNode prev = studentsInLine;
        SNode current = studentsInLine.getNext();
    
        while (current != null && eliminatedStudent.compareNameTo(current.getStudent()) > 0) {
            prev = current;
            current = current.getNext();
        }
            
        newNode.setNext(current);
        prev.setNext(newNode);
    }
    
    /**
     * 
     * Removes ALL eliminated students from musical chairs and inserts those
     * students
     * back in studentsInLine in ascending name order (earliest to latest).
     * 
     * At the end of this method, all students will be in studentsInLine besides
     * the winner, who will only be in musicalChairs.
     * 
     * 1. Find the number of students currently in musicalChairs
     * 2. While there's more than 1 student in musicalChairs, call
     * moveRandomStudentFromChairsToLine()
     * --> pass the size from step (1) into the method call.
     */
    public void eliminateLosingStudents() {
        int size =0;

        SNode ptr3 = musicalChairs;

        if(ptr3!=null){
            do{
                size++;
                ptr3 = ptr3.getNext();

            }
            while(ptr3!=musicalChairs);
        



        }
        
        while(size>1){
            moveStudentFromChairsToLine(size);
            size--;
        }
    }

    /*
     * If musicalChairs (CLL) contains ONLY ONE student (the winner),
     * this method removes the winner from musicalChairs and inserts that student
     * into the first available seat in studentsInSeats. musicalChairs will then be
     * empty.
     * 
     * ASSUME eliminateLosingStudents is called before this method is.
     * 
     * NOTE: This method does nothing if there is more than one student in
     * musicalChairs
     * OR if musicalChairs is empty.
     */
   public void seatMusicalChairsWinner() {
        if (musicalChairs == null || musicalChairs.getNext() != musicalChairs) {
            return;
        }
       if(musicalChairs!=null) {
        Student W = musicalChairs.getStudent();
        musicalChairs = null;
        for(int r = 0; r<studentsInSeats.length;r++){
            for(int c =0;c<studentsInSeats[r].length;c++){ 
                if(openSeats[r][c]&&studentsInSeats[r][c]==null){
                    studentsInSeats[r][c]= W;
                    return;
                }
            }
        }
       }
    }
    /**
     * 
     * This method simulates the game of Musical Chairs!
     * 
     * This method calls previously-written methods to remove students from
     * musicalChairs until there is only one student (the winner), then seats the
     * winner
     * and then seat the students from the studentsInline.
     * 
     * DO NOT UPDATE THIS METHOD
     */
    public void playMusicalChairs() {
        eliminateLosingStudents();
        seatMusicalChairsWinner();
        seatStudents();
    }

    /**
     * Used by driver to display students in line
     * DO NOT edit.
     */
    public void printStudentsInLine() {

        // Print studentsInLine
        StdOut.println("Students in Line:");
        if (studentsInLine == null) {
            StdOut.println("EMPTY");
        }

        for (SNode ptr = studentsInLine; ptr != null; ptr = ptr.getNext()) {
            StdOut.print(ptr.getStudent().print());
            if (ptr.getNext() != null) {
                StdOut.print(" -> ");
            }
        }
        StdOut.println();
        StdOut.println();
    }

    /**
     * Prints the seated students; can use this method to debug.
     * DO NOT edit.
     */
    public void printSeatedStudents() {

        StdOut.println("Sitting Students:");

        if (studentsInSeats != null) {

            for (int i = 0; i < studentsInSeats.length; i++) {
                for (int j = 0; j < studentsInSeats[i].length; j++) {

                    String stringToPrint = "";
                    if (studentsInSeats[i][j] == null) {

                        if (openSeats[i][j] == false) {
                            stringToPrint = "X";
                        } else {
                            stringToPrint = "EMPTY";
                        }

                    } else {
                        stringToPrint = studentsInSeats[i][j].print();
                    }

                    StdOut.print(stringToPrint);

                    for (int o = 0; o < (10 - stringToPrint.length()); o++) {
                        StdOut.print(" ");
                    }
                }
                StdOut.println();
            }
        } else {
            StdOut.println("EMPTY");
        }
        StdOut.println();
    }

    /**
     * Prints the musical chairs; can use this method to debug.
     * DO NOT edit.
     */
    public void printMusicalChairs() {
        StdOut.println("Students in Musical Chairs:");

        if (musicalChairs == null) {
            StdOut.println("EMPTY");
            StdOut.println();
            return;
        }
        SNode ptr;
        for (ptr = musicalChairs.getNext(); ptr != musicalChairs; ptr = ptr.getNext()) {
            StdOut.print(ptr.getStudent().print() + " -> ");
        }
        if (ptr == musicalChairs) {
            StdOut.print(musicalChairs.getStudent().print() + " - POINTS TO FRONT");
        }
        StdOut.println();
    }

    /**
     * Prints the state of the classroom; can use this method to debug.
     * DO NOT edit.
     */
    public void printClassroom() {
        printStudentsInLine();
        printSeatedStudents();
        printMusicalChairs();
    }

    /**
     * Used to get and set objects.
     * DO NOT edit.
     */

    public SNode getStudentsInLine() {
        return studentsInLine;
    }

    public void setStudentsInLine(SNode l) {
        studentsInLine = l;
    }

    public SNode getMusicalChairs() {
        return musicalChairs;
    }

    public void setMusicalChairs(SNode m) {
        musicalChairs = m;
    }

    public boolean[][] getOpenSeats() {
        return openSeats;
    }

    public void setOpenSeats(boolean[][] a) {
        openSeats = a;
    }

    public Student[][] getStudentsInSeats() {
        return studentsInSeats;
    }

    public void setStudentsInSeats(Student[][] s) {
        studentsInSeats = s;
    }

}

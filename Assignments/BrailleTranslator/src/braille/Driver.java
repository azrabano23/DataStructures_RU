package braille;

import java.util.ArrayList;

/**
 * This is a Driver class which YOU implement.
 * You do not have to submit this file, but you can use it
 * to test your BrailleTranslator.java methods. 
 * 
 * We give you directions for the first task and how to
 * set up this driver. Build off the examples to make this driver work.
 * 
 * Alternatively, you can use JUnit tests to test your code (see BrailleTranslatorTest.java).
 */
public class Driver {

    public static void main(String[] args) {

        // Remove the next three lines once you've implemented the Driver


        BrailleTranslator test = new BrailleTranslator();
        Driver.printCommands();
        String line = StdIn.readLine();

        while (line != null && !line.equals("7")) {
            switch (line){
                case "1": 
                    System.out.println("Enter a filename: ");
                    String file = StdIn.readLine();

                    // Call createSymbolTree(file) on the "test" object with the String "file"  
                    test.createSymbolTree(file);
                    // Then, call printTree() on it.
                    test.printTree();
                    StdIn.resetFile();
                    break; // DON'T REMOVE
                case "2":
                    System.out.println("Enter an encoding (L = lowered, R = raised): ");
                    String encoding = StdIn.readLine();

                    // Call getSymbolNode with the encoding: test.getSymbolNode(encoding)
                    TreeNode res = test.getSymbolNode(encoding);

                    // If resulting node isn't null, print the character + encoding, otherwise say it's null
                    if (res != null) {
                        StdOut.println("Encoding: " + res.getSymbol().getEncoding() + ", " + res.getSymbol().getCharacter());
                    }
                    else {
                        StdOut.println("EMPTY");
                    }

                    break;
                case "3":
                    System.out.println("Enter a SINGLE character, in lowercase: ");
                    char character = StdIn.readLine().charAt(0);

                    // Use findBrailleEncoding to find the character encoding

                    String charRes = test.findBrailleEncoding(character);

                    // Print the encoding
                    StdOut.println(charRes);

                    break;
                case "4":
                    System.out.println("Enter an encoding (L = lowered, R = raised): ");
                    String oldEncoding = StdIn.readLine();

                    // Call encodingsStartWith with the encoding

                    ArrayList<Symbol> list = test.encodingsStartWith(oldEncoding);

                    // For each encoding, iterate through and print its symbol's character

                    for (Symbol s: list) {
                        StdOut.println(s.getCharacter() + " " + s.getEncoding());
                    }

                    break;
                case "5":
                    System.out.println("Enter a translate filename: ");
                    String translateFile = StdIn.readLine();

                    // Call translateBraille(file) on the "test" object with the String translateFile

                    String translated = test.translateBraille(translateFile);

                    // Print the string returned

                    StdOut.println(translated);

                    // DON'T remove this line
                    StdIn.resetFile();
                    break;
                case "6": 
                    System.out.println("Enter a SINGLE character, in lowercase: ");
                    char toDelete = StdIn.readLine().charAt(0);
                    // Call deleteSymbol on the character to delete

                    test.deleteSymbol(toDelete);

                    // Call printTree to print the tree
                    test.printTree();
                    break;
                case "7": 
                    // DO NOT MODIFY
                    System.out.println("Exiting...");
                    return;
            }
            System.out.println("\n");
            Driver.printCommands();
            line = StdIn.readLine();
        }
        System.out.println("Exiting...");
    }

    private static void printCommands() {
        System.out.println("1) createSymbolTree(String filename)");
        System.out.println("2) getSymbolNode(String characters)");
        System.out.println("3) findBrailleEncoding(char character)");
        System.out.println("4) encodingsStartWith(String startEncoding)");
        System.out.println("5) translateBraille(String inputFile)");
        System.out.println("6) deleteSymbol(char symbol)");
        System.out.println("7) Exit");
        System.out.println("Choose a command (1-7): ");
    }
}
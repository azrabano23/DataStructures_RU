package braille;

import java.util.ArrayList;

/**
 * Contains methods to translate Braille to English and English to Braille using
 * a BST.
 * Reads encodings, adds characters, and traverses tree to find encodings.
 * 
 * @author Seth Kelley
 * @author Kal Pandit
 */
public class BrailleTranslator {

    private TreeNode treeRoot;

    /**
     * Default constructor, sets symbols to an empty ArrayList
     */
    public BrailleTranslator() {
        treeRoot = null;
    }

    /**
     * Reads encodings from an input file as follows:
     * - One line has the number of characters
     * - n lines with character (as char) and encoding (as string) space-separated
     * USE StdIn.readChar() to read character and StdIn.readLine() after reading
     * encoding
     * 
     * @param inputFile the input file name
     */
    public void createSymbolTree(String inputFile) {

        /* PROVIDED, DO NOT EDIT */

        StdIn.setFile(inputFile);
        int numberOfChars = Integer.parseInt(StdIn.readLine());
        for (int i = 0; i < numberOfChars; i++) {
            Symbol s = readSingleEncoding();
            addCharacter(s);
        }
    }

    /**
     * Reads one line from an input file and returns its corresponding
     * Symbol object
     * 
     * ONE line has a character and its encoding (space separated)
     * 
     * @return the symbol object
     */
    public Symbol readSingleEncoding() {
        // WRITE YOUR CODE HERE
            char letter = StdIn.readChar();
            String encoding = StdIn.readString();
            StdIn.readLine();
            return new Symbol(letter, encoding);
        }
        

        //return null; Replace this line, it is provided so your code compiles


    /**
     * Adds a character into the BST rooted at treeRoot.
     * Traces encoding path (0 = left, 1 = right), starting with an empty root.
     * Last digit of encoding indicates position (left or right) of character within
     * parent.
     * 
     * @param newSymbol the new symbol object to add
     */
    public void addCharacter(Symbol newSymbol) {
        String encoding = newSymbol.getEncoding();
    
        if (treeRoot == null) {
            treeRoot = new TreeNode(new Symbol(""), null, null);
        }
    
        TreeNode current = treeRoot;
        String partialEncoding = "";

        for (int i = 0; i < encoding.length(); i++) {
            char direction = encoding.charAt(i);
            partialEncoding += direction;
    
            if (i == encoding.length() - 1) {
                TreeNode leaf = new TreeNode(newSymbol, null, null);
                if (direction == 'L') {
                    current.setLeft(leaf);
                } else {
                    current.setRight(leaf);
                }
            } else {
                if (direction == 'L') {
                    if (current.getLeft() == null) {
                        current.setLeft(new TreeNode(new Symbol(partialEncoding), null, null));
                    }
                    current = current.getLeft();
                } else if (direction == 'R') {
                    if (current.getRight() == null) {
                        current.setRight(new TreeNode(new Symbol(partialEncoding), null, null));
                    }
                    current = current.getRight();
                }
            }
        }
    }
    
    /**
     * Given a sequence of characters, traverse the tree based on the characters
     * to find the TreeNode it leads to
     * 
     * @param encoding Sequence of braille (Ls and Rs)
     * @return Returns the TreeNode of where the characters lead to, or null if there is no path
     */
    public TreeNode getSymbolNode(String encoding) {
        TreeNode current = treeRoot;
    
        for (int i = 0; i < encoding.length(); i++) {
            if (current == null) {
                return null; 
            }
    
            char direction = encoding.charAt(i);
            if (direction == 'L') {
                current = current.getLeft();
            } else if (direction == 'R') {
                current = current.getRight();
            } else {
                return null; 
            }
        }
    
        return current;
    }    

    /**
     * Given a character to look for in the tree will return the encoding of the
     * character
     * 
     * @param character The character that is to be looked for in the tree
     * @return Returns the String encoding of the character
     */
    public String findBrailleEncoding(char character) {
        return findBrailleEncodingHelper(treeRoot, character);
    }
    
    private String findBrailleEncodingHelper(TreeNode node, char character) {
        if (node == null) {
            return null;
        }
    
        Symbol symbol = node.getSymbol();
    
        if (symbol.hasCharacter() && symbol.getCharacter() == character) {
            return symbol.getEncoding();
        }
    
        String leftResult = findBrailleEncodingHelper(node.getLeft(), character);
        if (leftResult != null) {
            return leftResult;
        }
    
        return findBrailleEncodingHelper(node.getRight(), character);
    }
    

    /**
     * Given a prefix to a Braille encoding, return an ArrayList of all encodings that start with
     * that prefix
     * 
     * @param start the prefix to search for
     * @return all Symbol nodes which have encodings starting with the given prefix
     */
    public ArrayList<Symbol> encodingsStartWith(String start) {
        ArrayList<Symbol> result = new ArrayList<>();
    
        TreeNode startNode = getSymbolNode(start);
        if (startNode == null) {
            return result; 
        }
    
        collectEncodings(startNode, result);
    
        return result;
    }
    
    private void collectEncodings(TreeNode node, ArrayList<Symbol> result) {
        if (node == null) return;
    
        Symbol sym = node.getSymbol();
        if (sym.hasCharacter()) {
            result.add(sym); 
        }
    
        collectEncodings(node.getLeft(), result);
        collectEncodings(node.getRight(), result);
    }
    

    /**
     * Reads an input file and processes encodings six chars at a time.
     * Then, calls getSymbolNode on each six char chunk to get the
     * character.
     * 
     * Return the result of all translations, as a String.
     * @param input the input file
     * @return the translated output of the Braille input
     */
    public String translateBraille(String input) {
        StdIn.setFile(input);
        String encodedLine = StdIn.readLine();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < encodedLine.length(); i += 6) {
            String chunk = encodedLine.substring(i, i + 6);
            TreeNode node = getSymbolNode(chunk);
    
            if (node != null && node.getSymbol().hasCharacter()) {
                result.append(node.getSymbol().getCharacter());
            }
        }
        return result.toString();
    }    


    /**
     * Given a character, delete it from the tree and delete any encodings not
     * attached to a character (ie. no children).
     * 
     * @param symbol the symbol to delete
     */
    public void deleteSymbol(char symbol) {
        String encoding = findBrailleEncoding(symbol);
        if (encoding == null) return;
    
        TreeNode targetNode = getSymbolNode(encoding);
        String parentEncoding = encoding.substring(0, encoding.length() - 1);
        TreeNode parentNode = getSymbolNode(parentEncoding);
    
        if (parentNode != null) {
            char lastDirection = encoding.charAt(encoding.length() - 1);
            if (lastDirection == 'L') {
                parentNode.setLeft(null);
            } else if (lastDirection == 'R') {
                parentNode.setRight(null);
            }
        } else {
            treeRoot = null;
            return;
        }
    
        String currentEncoding = parentEncoding;
        while (currentEncoding.length() > 0) {
            TreeNode currentNode = getSymbolNode(currentEncoding);
            if (currentNode == null) break;
    
            boolean hasLeft = currentNode.getLeft() != null;
            boolean hasRight = currentNode.getRight() != null;
            boolean isLeaf = currentNode.getSymbol().hasCharacter();
    
            if (!hasLeft && !hasRight && !isLeaf) {
                String currParentEncoding = currentEncoding.substring(0, currentEncoding.length() - 1);
                TreeNode currParent = getSymbolNode(currParentEncoding);
    
                if (currParent != null) {
                    char dir = currentEncoding.charAt(currentEncoding.length() - 1);
                    if (dir == 'L') {
                        currParent.setLeft(null);
                    } else {
                        currParent.setRight(null);
                    }
                } else {
                    treeRoot = null;
                }
            } else {
                break; 
            }
            currentEncoding = currentEncoding.substring(0, currentEncoding.length() - 1);
        }
    }    

    public TreeNode getTreeRoot() {
        return this.treeRoot;
    }

    public void setTreeRoot(TreeNode treeRoot) {
        this.treeRoot = treeRoot;
    }

    public void printTree() {
        printTree(treeRoot, "", false, true);
    }

    private void printTree(TreeNode n, String indent, boolean isRight, boolean isRoot) {
        StdOut.print(indent);

        // Print out either a right connection or a left connection
        if (!isRoot)
            StdOut.print(isRight ? "|+R- " : "--L- ");

        // If we're at the root, we don't want a 1 or 0
        else
            StdOut.print("+--- ");

        if (n == null) {
            StdOut.println("null");
            return;
        }
        // If we have an associated character print it too
        if (n.getSymbol() != null && n.getSymbol().hasCharacter()) {
            StdOut.print(n.getSymbol().getCharacter() + " -> ");
            StdOut.print(n.getSymbol().getEncoding());
        }
        else if (n.getSymbol() != null) {
            StdOut.print(n.getSymbol().getEncoding() + " ");
            if (n.getSymbol().getEncoding().equals("")) {
                StdOut.print("\"\" ");
            }
        }
        StdOut.println();

        // If no more children we're done
        if (n.getSymbol() != null && n.getLeft() == null && n.getRight() == null)
            return;

        // Add to the indent based on whether we're branching left or right
        indent += isRight ? "|    " : "     ";

        printTree(n.getRight(), indent, true, false);
        printTree(n.getLeft(), indent, false, false);
    }

}

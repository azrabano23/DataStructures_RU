package braille;
/**
 * Stores a Braille symbol: character and encoding.
 * If this symbol doesn't correspond to a character, the character is Character.MIN_VALUE.
 * Encodings are stored for all nodes and are never null.
 * @author Kal Pandit
 */
public class Symbol {
    private char character; // lowercase character represented by the encoding. Only leaf nodes have a character.
    private String encoding; // Sequences of Ls and Rs (empty string at root)

    public Symbol() {
        character = Character.MIN_VALUE;
        encoding = null;
    }

    public Symbol(char character, String encoding) {
        this.character = character;
        this.encoding = encoding;
    }
    
    public Symbol(String encoding) {
        this.character = Character.MIN_VALUE;
        this.encoding = encoding;
    }
    public char getCharacter() {
        return this.character;
    }

    public void setCharacter(char character) {
        this.character = character;
    }

    public String getEncoding() {
        return this.encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    @Override
    public String toString() {
        return "'" + character + "' " + encoding;
    }

    public boolean hasCharacter() {
        return character != Character.MIN_VALUE;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof Symbol) {
            Symbol other = (Symbol) obj;
            return character == other.character && encoding.equals(other.encoding);
        }
        return false;
    }

}

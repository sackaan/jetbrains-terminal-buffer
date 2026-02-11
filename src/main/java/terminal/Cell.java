package terminal;

import java.util.EnumSet;

public class Cell {

    private char character;
    private CellAttributes attributes;

    public Cell() {
        this.character = ' ';
        this.attributes = new CellAttributes();
    }

    public Cell(char character, Color foreground, Color background, EnumSet<Style> styles) {
        this.character = character;
        this.attributes = new CellAttributes(foreground, background, styles);
    }

    public void clear() {
        this.character = ' ';
        this.attributes = new CellAttributes();
    }

    public Cell copy() {
        return new Cell(character, attributes.getForeground(), attributes.getBackground(), attributes.getStyles());
    }

    public char getCharacter() {
        return character;
    }

    public void setCharacter(char character) {
        this.character = character;
    }

    public CellAttributes getAttributes() {
        return attributes;
    }

}

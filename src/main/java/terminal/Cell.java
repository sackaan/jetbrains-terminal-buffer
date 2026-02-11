package terminal;

import java.util.EnumSet;

public class Cell {

    private char character;
    private CellAttributes attributes;

    public Cell() {
        this.character = ' ';
        this.attributes = new CellAttributes();
    }

    public Cell(char character, CellAttributes attributes) {
        this.character = character;
        this.attributes = attributes;
    }


    public void clear() {
        this.character = ' ';
        this.attributes = new CellAttributes();
    }

    public Cell copy() {
        return new Cell(character, attributes.copy());
    }

    public char getCharacter() {
        return character;
    }

    public void setCharacter(char character) {
        this.character = character;
    }

    public Color getForeground() {
        return attributes.getForeground();
    }

    public void setForeground(Color foreground) {
        attributes.setForeground(foreground);
    }

    public Color getBackground() {
        return attributes.getBackground();
    }

    public void setBackground(Color background) {
        attributes.setBackground(background);
    }

    public EnumSet<Style> getStyles() {
        return attributes.getStyles();
    }

    public void addStyle(Style style) {
        attributes.addStyle(style);
    }

    public void removeStyle(Style style) {
        attributes.removeStyle(style);
    }

    public boolean hasStyle(Style style) {
        return attributes.hasStyle(style);
    }


}

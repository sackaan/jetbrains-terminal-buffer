package terminal;

import java.util.EnumSet;

public class Cell {

    private char character;
    private Color foreground;
    private Color background;
    private EnumSet<Style> styles;

    public Cell() {
        this.character = ' ';
        this.foreground = Color.DEFAULT;
        this.background = Color.DEFAULT;
        this.styles = EnumSet.noneOf(Style.class);
    }

    public Cell(char character, Color foreground, Color background, EnumSet<Style> styles) {
        this.character = character;
        this.foreground = foreground;
        this.background = background;
        this.styles = EnumSet.copyOf(styles);
    }

    public void clear() {
        this.character = ' ';
        this.foreground = Color.DEFAULT;
        this.background = Color.DEFAULT;
        this.styles.clear();
    }

    public Cell copy() {
        return new Cell(character, foreground, background, styles);
    }

    public char getCharacter() {
        return character;
    }

    public void setCharacter(char character) {
        this.character = character;
    }

    public Color getForeground() {
        return foreground;
    }

    public void setForeground(Color foreground) {
        this.foreground = foreground;
    }

    public Color getBackground() {
        return background;
    }

    public void setBackground(Color background) {
        this.background = background;
    }

    public EnumSet<Style> getStyles() {
        return EnumSet.copyOf(styles);
    }

    public void addStyle(Style style) {
        styles.add(style);
    }

    public void removeStyle(Style style) {
        styles.remove(style);
    }

    public boolean hasStyle(Style style) {
        return styles.contains(style);
    }


}

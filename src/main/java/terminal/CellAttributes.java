package terminal;

import java.util.EnumSet;

public class CellAttributes {

    private Color foreground;
    private Color background;
    private final EnumSet<Style> styles;

    public CellAttributes() {
        this.foreground = Color.DEFAULT;
        this.background = Color.DEFAULT;
        this.styles = EnumSet.noneOf(Style.class);
    }

    public CellAttributes(Color foreground, Color background, EnumSet<Style> styles) {
        this.foreground = foreground;
        this.background = background;
        this.styles = styles.clone();
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

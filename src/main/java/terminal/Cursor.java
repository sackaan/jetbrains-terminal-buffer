package terminal;

public class Cursor {

    private int row;
    private int column;

    public Cursor() {
        this(0, 0);
    }

    public Cursor(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    void setPosition(int row, int column) {
        this.row = row;
        this.column = column;
    }
}


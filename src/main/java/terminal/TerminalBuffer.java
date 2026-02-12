package terminal;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class TerminalBuffer {

    private final int width;
    private final int height;
    private final int maxScrollback;

    private final List<Line> screen;
    private final Deque<Line> scrollback;

    private final Cursor cursor;
    private CellAttributes currentAttributes;

    public TerminalBuffer(int width, int height, int maxScrollback) {
        if (width < 0 || height < 0 || maxScrollback < 0) {
            throw new IllegalArgumentException("Width, height, and max scrollback must be non-negative");
        }

        this.width = width;
        this.height = height;
        this.maxScrollback = maxScrollback;

        this.screen = new ArrayList<>(height);
        for (int i = 0; i < height; i++) {
            screen.add(new Line(width));
        }

        this.scrollback = new ArrayDeque<>(maxScrollback);

        this.cursor = new Cursor();
        this.currentAttributes = new CellAttributes();
    }

    public void overwrite(String text) {
        for (char c : text.toCharArray()) {
            putChar(c);
        }
    }

    public void putChar(char c) {
        Line line = screen.get(cursor.getRow());

        Cell cell = new Cell(c, currentAttributes.copy());

        line.setCell(cursor.getColumn(), cell);

        int newColumn = cursor.getColumn() + 1;
        if (newColumn >= width) {
            if(cursor.getRow() == height - 1) {
                cursorSetPosition(cursor.getRow(), width - 1);
            } else {
                cursorNextLine();
            }
        } else {
            cursorMoveRight(1);
        }
    }

    public String printScreen() {
        StringBuilder sb = new StringBuilder();
        for (Line line : screen) {
            for (int i = 0; i < width; i++) {
                sb.append(line.getCell(i).getCharacter());
            }
            sb.append('\n');
        }
        System.out.println(sb);
        return sb.toString();
    }

    public void cursorMoveUp(int n) {
        int newRow = Math.max(0, cursor.getRow() - n);
        cursor.setPosition(newRow, cursor.getColumn());
    }

    public void cursorMoveDown(int n) {
        int newRow = Math.min(height - 1, cursor.getRow() + n);
        cursor.setPosition(newRow, cursor.getColumn());
    }

    public void cursorMoveLeft(int n) {
        int newColumn = Math.max(0, cursor.getColumn() - n);
        cursor.setPosition(cursor.getRow(), newColumn);
    }

    public void cursorMoveRight(int n) {
        int newColumn = Math.min(width - 1, cursor.getColumn() + n);
        cursor.setPosition(cursor.getRow(), newColumn);
    }

    public void cursorSetPosition(int row, int column) {
        int newRow = Math.max(0, Math.min(height - 1, row));
        int newColumn = Math.max(0, Math.min(width - 1, column));
        cursor.setPosition(newRow, newColumn);
    }

    public void cursorNextLine() {
        cursorSetPosition(cursor.getRow() + 1, 0);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getMaxScrollback() {
        return maxScrollback;
    }

    public Cursor getCursor() {
        return cursor;
    }

    public CellAttributes getCurrentAttributes() {
        return currentAttributes;
    }

    public List<Line> getScreen() {
        return screen;
    }

    public Deque<Line> getScrollback() {
        return scrollback;
    }
}

package terminal;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

public class TerminalBuffer {

    private final int width;
    private final int height;
    private final int maxScrollback;

    private final List<Line> screen;
    private final Deque<Line> scrollback;

    private boolean wrapPending = false;

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

    private void scrollUp() {
        Line removed = screen.remove(0);
        scrollback.addLast(removed);

        if (scrollback.size() > maxScrollback) {
            scrollback.removeFirst();
        }

        screen.add(new Line(width));
    }

    public void write(String text) {
        for (char c : text.toCharArray()) {
            putChar(c);
        }
    }

    public void insert(String text) {
        for (char c : text.toCharArray()) {
            insertChar(c);
        }
    }

    public void insertChar(char c) {
        Line line = screen.get(cursor.getRow());

        line.insert(cursor.getColumn(), String.valueOf(c), currentAttributes);

        int nextCol = cursor.getColumn() + 1;

        if (nextCol >= width) {
            if (cursor.getRow() < height - 1) {
                cursorNextLine();
            } else {
                cursorSetPosition(cursor.getRow(), width - 1);
            }
        } else {
            cursorSetPosition(cursor.getRow(), nextCol);
        }
    }

    public void putChar(char c) {

        if (wrapPending) {
            if (cursor.getRow() == height - 1) {
                scrollUp();
                cursorSetPosition(height - 1, 0);
            } else {
                cursorNextLine();
            }
            wrapPending = false;
        }

        Line line = screen.get(cursor.getRow());

        Cell cell = new Cell(c, currentAttributes.copy());

        line.setCell(cursor.getColumn(), cell);

        if (cursor.getColumn() == width - 1) {
            wrapPending = true;
        } else {
            cursorMoveRight(1);
        }

    }

    public String getScreenAsString() {
        StringBuilder sb = new StringBuilder();
        for (Line line : screen) {
            for (int i = 0; i < width; i++) {
                sb.append(line.getCell(i).getCharacter());
            }
            sb.append('\n');
        }
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
        wrapPending = false;
    }

    public void cursorNextLine() {
        if(cursor.getRow() == height - 1) {
            scrollUp();
            cursorSetPosition(height-1, 0);
        } else {
            cursorSetPosition(cursor.getRow() + 1, 0);
        }
    }

    public void fillLine(int row, char c){
        if(row < 0 || row >= height) {
            throw new IndexOutOfBoundsException("Row out of bounds");
        }

        Line line = screen.get(row);
        for(int i = 0; i < width; i++) {
            line.setCell(i, new Cell(c, currentAttributes.copy()));
        }
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
        return Collections.unmodifiableList(screen);
    }

    public List<Line> getScrollback() {
        return List.copyOf(scrollback);
    }

    public String getEntireBufferAsString() {
        StringBuilder sb = new StringBuilder();

        for (Line line : scrollback) {
            sb.append(line.toString()).append("\n");
        }

        for (Line line : screen) {
            sb.append(line.toString()).append("\n");
        }

        return sb.toString();
    }

    public void clearScreen() {
        for (Line line : screen) {
            line.clear();
        }
        cursorSetPosition(0, 0);
        wrapPending = false;
    }

    public void clearScreenAndScrollback() {
        clearScreen();
        scrollback.clear();
    }

}

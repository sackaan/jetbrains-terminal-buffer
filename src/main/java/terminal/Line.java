package terminal;

import java.util.ArrayList;
import java.util.List;

public class Line {

    private final int width;
    private final List<Cell> cells;

    public Line(int width) {
        if (width < 0) {
            throw new IllegalArgumentException("Width must be non-negative: " + width);
        }

        this.width = width;
        this.cells = new ArrayList<>(width);

        for(int i = 0; i < width; i++) {
            cells.add(new Cell());
        }
    }

    public int insert(int column, String text, Cell attributes) {
        checkBounds(column);
        int charsWritten = 0;

        // We either insert the whole text or as much as fits in the line
        int insertLength = Math.min(text.length(), width - column);

        if (insertLength <= 0) {
            return 0;
        }

        // Move existing cells to the right to make space for the new text
        for (int i = width - 1; i >= column + insertLength; i--) {
            cells.set(i, cells.get(i - insertLength));
        }

        // Write the new text
        for (int i = 0; i < insertLength; i++) {
            Cell cell = attributes.copy();
            cell.setCharacter(text.charAt(i));
            cells.set(column + i, cell);
            charsWritten++;
        }

        return charsWritten;
    }

    public int overwrite(int column, String text, Cell attributes){
        checkBounds(column);
        int charsWritten = 0;

        for(int i = 0; i < text.length() && column + i < width; i++) {
            Cell cell = attributes.copy();
            cell.setCharacter(text.charAt(i));
            cells.set(column + i, cell);
            charsWritten++;
        }

        return charsWritten;
    }

    public Cell getCell(int column){
        checkBounds(column);
        return cells.get(column);
    }

    public int getWidth() {
        return width;
    }

    public void setCell(int column, Cell cell){
        checkBounds(column);
        cells.set(column, cell.copy());
    }

    public void clear(){
        for(Cell cell : cells) {
            cell.clear();
        }
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(Cell c : cells) {
            sb.append(c.getCharacter());
        }

        return sb.toString();
    }

    private void checkBounds(int column) {
        if (column < 0 || column >= width) {
            throw new IndexOutOfBoundsException("Column out of bounds: " + column);
        }
    }
}

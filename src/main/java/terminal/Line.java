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

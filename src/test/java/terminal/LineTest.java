package terminal;

import org.junit.jupiter.api.Test;

import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.*;
public class LineTest {

    @Test
    void testLineConstructor() {
        Line line = new Line(42);

        assertEquals(42, line.getWidth());

        for (int i = 0; i < line.getWidth(); i++) {
            Cell cell = line.getCell(i);

            assertEquals(' ', cell.getCharacter());
            assertEquals(Color.DEFAULT, cell.getForeground());
            assertEquals(Color.DEFAULT, cell.getBackground());
            assertTrue(cell.getStyles().isEmpty());
        }
    }

    @Test
    void testLineConstructorIllegalWidth() {
        assertThrows(IllegalArgumentException.class, () -> new Line(-1));
    }

    @Test
    void testGetCell() {
        Line line = new Line(10);
        CellAttributes attributes = new CellAttributes(Color.RED, Color.BLUE, EnumSet.of(Style.BOLD));
        Cell cell = new Cell('X', attributes);
        line.setCell(5, cell);

        Cell retrievedCell = line.getCell(5);

        assertEquals('X', retrievedCell.getCharacter());
        assertEquals(Color.RED, retrievedCell.getForeground());
        assertEquals(Color.BLUE, retrievedCell.getBackground());
        assertTrue(retrievedCell.hasStyle(Style.BOLD));
    }

    @Test
    void testClear() {
        Line line = new Line(10);
        CellAttributes attributes = new CellAttributes(Color.RED, Color.BLUE, EnumSet.of(Style.BOLD));
        Cell cell = new Cell('X', attributes);
        line.setCell(5, cell);

        line.clear();

        for (int i = 0; i < line.getWidth(); i++) {
            Cell clearedCell = line.getCell(i);

            assertEquals(' ', clearedCell.getCharacter());
            assertEquals(Color.DEFAULT, clearedCell.getForeground());
            assertEquals(Color.DEFAULT, clearedCell.getBackground());
            assertTrue(clearedCell.getStyles().isEmpty());
        }
    }


    @Test
    void testToString() {
        Line line = new Line(12);
        CellAttributes attributes = new CellAttributes(Color.RED, Color.BLUE, EnumSet.of(Style.BOLD));
        line.setCell(0, new Cell('H', attributes));
        line.setCell(1, new Cell('E', attributes));
        line.setCell(2, new Cell('L', attributes));
        line.setCell(3, new Cell('L', attributes));
        line.setCell(4, new Cell('O', attributes));

        line.setCell(6, new Cell('W', attributes));
        line.setCell(7, new Cell('O', attributes));
        line.setCell(8, new Cell('R', attributes));
        line.setCell(9, new Cell('L', attributes));
        line.setCell(10, new Cell('D', attributes));

        assertEquals("HELLO WORLD ", line.toString());
    }

    @Test
    void testGetCellOutOfBounds() {
        Line line = new Line(10);
        assertThrows(IndexOutOfBoundsException.class, () -> line.getCell(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> line.getCell(10));
    }

    @Test
    void testSetCellOutOfBounds() {
        Line line = new Line(10);
        Cell cell = new Cell();
        assertThrows(IndexOutOfBoundsException.class, () -> line.setCell(-1, cell));
        assertThrows(IndexOutOfBoundsException.class, () -> line.setCell(10, cell));
    }

    @Test
    void testOverwrite() {
        Line line = new Line(10);
        CellAttributes attributes = new CellAttributes(Color.GREEN, Color.BLACK, EnumSet.of(Style.BOLD));
        int charsWritten = line.overwrite(2, "Hello", attributes);

        assertEquals(5, charsWritten);

        String expectedString = "  Hello   ";
        assertEquals(expectedString, line.toString());
    }

    @Test
    void testInsert(){
        Line line = new Line(10);
        CellAttributes attributes = new CellAttributes(Color.GREEN, Color.BLACK, EnumSet.of(Style.BOLD));
        line.overwrite(0, "ABCDE", attributes);
        int charsWritten = line.insert(2, "Hello", attributes);

        assertEquals(5, charsWritten);

        String finalExpectedString = "ABHelloCDE";
        assertEquals(finalExpectedString, line.toString());
    }

    @Test
    void testInsertOverflow(){
        Line line = new Line(10);
        CellAttributes attributes = new CellAttributes(Color.GREEN, Color.BLACK, EnumSet.of(Style.BOLD));
        line.overwrite(0, "ABCDEFGHIJ", attributes);

        int inserted = line.insert(8, "XYZ", attributes);

        assertEquals(2, inserted);
        assertEquals("ABCDEFGHXY", line.toString());
    }
}

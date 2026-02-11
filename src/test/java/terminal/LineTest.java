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
            assertEquals(' ', line.getCell(i).getCharacter());
            assertEquals(Color.DEFAULT, line.getCell(i).getForeground());
            assertEquals(Color.DEFAULT, line.getCell(i).getBackground());
            assertTrue(line.getCell(i).getStyles().isEmpty());
        }
    }

    @Test
    void testLineConstructorIllegalWidth() {
        assertThrows(IllegalArgumentException.class, () -> new Line(-1));
    }

    @Test
    void testGetCell() {
        Line line = new Line(10);
        Cell cell = new Cell('X', Color.RED, Color.BLUE, EnumSet.of(Style.BOLD));
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
        Cell cell = new Cell('X', Color.RED, Color.BLUE, EnumSet.of(Style.BOLD));
        line.setCell(5, cell);

        line.clear();

        for (int i = 0; i < line.getWidth(); i++) {
            assertEquals(' ', line.getCell(i).getCharacter());
            assertEquals(Color.DEFAULT, line.getCell(i).getForeground());
            assertEquals(Color.DEFAULT, line.getCell(i).getBackground());
            assertTrue(line.getCell(i).getStyles().isEmpty());
        }
    }


    @Test
    void testToString() {
        Line line = new Line(12);
        line.setCell(0, new Cell('H', Color.DEFAULT, Color.DEFAULT, EnumSet.noneOf(Style.class)));
        line.setCell(1, new Cell('E', Color.DEFAULT, Color.DEFAULT, EnumSet.noneOf(Style.class)));
        line.setCell(2, new Cell('L', Color.DEFAULT, Color.DEFAULT, EnumSet.noneOf(Style.class)));
        line.setCell(3, new Cell('L', Color.DEFAULT, Color.DEFAULT, EnumSet.noneOf(Style.class)));
        line.setCell(4, new Cell('O', Color.DEFAULT, Color.DEFAULT, EnumSet.noneOf(Style.class)));

        line.setCell(6, new Cell('W', Color.DEFAULT, Color.DEFAULT, EnumSet.noneOf(Style.class)));
        line.setCell(7, new Cell('O', Color.DEFAULT, Color.DEFAULT, EnumSet.noneOf(Style.class)));
        line.setCell(8, new Cell('R', Color.DEFAULT, Color.DEFAULT, EnumSet.noneOf(Style.class)));
        line.setCell(9, new Cell('L', Color.DEFAULT, Color.DEFAULT, EnumSet.noneOf(Style.class)));
        line.setCell(10, new Cell('D', Color.DEFAULT, Color.DEFAULT, EnumSet.noneOf(Style.class)));

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
}

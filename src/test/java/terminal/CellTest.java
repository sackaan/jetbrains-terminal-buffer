package terminal;

import org.junit.jupiter.api.Test;
import java.util.EnumSet;
import static org.junit.jupiter.api.Assertions.*;

public class CellTest {

    @Test
    void testCellConstructor() {
        Cell cell = new Cell();

        assertEquals(' ', cell.getCharacter());
        assertEquals(Color.DEFAULT, cell.getForeground());
        assertEquals(Color.DEFAULT, cell.getBackground());
        assertTrue(cell.getStyles().isEmpty());
    }

    @Test
    void testCellConstructorParameters() {
        EnumSet<Style> styles = EnumSet.of(Style.BOLD, Style.ITALIC);
        CellAttributes attributes = new CellAttributes(Color.RED, Color.BLUE, styles);
        Cell cell = new Cell('A', attributes);

        assertEquals('A', cell.getCharacter());
        assertEquals(Color.RED, cell.getForeground());
        assertEquals(Color.BLUE, cell.getBackground());
        assertTrue(cell.getStyles().contains(Style.BOLD));
        assertTrue(cell.getStyles().contains(Style.ITALIC));
    }

    @Test
    void testAddStyles() {
        Cell cell = new Cell();

        cell.addStyle(Style.BOLD);
        cell.addStyle(Style.ITALIC);

        assertTrue(cell.hasStyle(Style.BOLD));
        assertTrue(cell.hasStyle(Style.ITALIC));
    }

    @Test
    void testRemoveStyles() {
        Cell cell = new Cell();

        cell.addStyle(Style.BOLD);
        cell.addStyle(Style.ITALIC);
        cell.removeStyle(Style.BOLD);

        assertFalse(cell.hasStyle(Style.BOLD));
        assertTrue(cell.hasStyle(Style.ITALIC));
    }

    @Test
    void testSetCharacter() {
        Cell cell = new Cell();
        cell.setCharacter('X');
        assertEquals('X', cell.getCharacter());
    }

    @Test
    void testSetColors() {
        Cell cell = new Cell();

        cell.setForeground(Color.GREEN);
        cell.setBackground(Color.YELLOW);

        assertEquals(Color.GREEN, cell.getForeground());
        assertEquals(Color.YELLOW, cell.getBackground());
    }

    @Test
    void testClear() {
        CellAttributes attributes = new CellAttributes(Color.RED, Color.BLUE, EnumSet.of(Style.BOLD));
        Cell cell = new Cell('A', attributes);
        cell.clear();

        assertEquals(' ', cell.getCharacter());
        assertEquals(Color.DEFAULT, cell.getForeground());
        assertEquals(Color.DEFAULT, cell.getBackground());
        assertTrue(cell.getStyles().isEmpty());
    }

    @Test
    void testCopy() {
        CellAttributes attributes = new CellAttributes(Color.RED, Color.BLUE, EnumSet.of(Style.BOLD));
        Cell original = new Cell('A', attributes);
        Cell copy = original.copy();

        assertEquals(original.getCharacter(), copy.getCharacter());
        assertEquals(original.getForeground(), copy.getForeground());
        assertEquals(original.getBackground(), copy.getBackground());
        assertEquals(original.getStyles(), copy.getStyles());

        // Modify the copy
        copy.setCharacter('B');
        copy.setForeground(Color.GREEN);
        copy.setBackground(Color.YELLOW);
        copy.addStyle(Style.ITALIC);

        // Test original remains unchanged while copy has new value
        assertEquals('A', original.getCharacter());
        assertEquals('B', copy.getCharacter());

        assertEquals(Color.RED, original.getForeground());
        assertEquals(Color.BLUE, original.getBackground());
        assertEquals(Color.GREEN, copy.getForeground());
        assertEquals(Color.YELLOW, copy.getBackground());

        assertTrue(original.hasStyle(Style.BOLD));
        assertFalse(original.hasStyle(Style.ITALIC));
        assertTrue(copy.hasStyle(Style.BOLD));
        assertTrue(copy.hasStyle(Style.ITALIC));

    }
}
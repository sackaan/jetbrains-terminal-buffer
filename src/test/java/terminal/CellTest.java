package terminal;

import org.junit.jupiter.api.Test;
import java.util.EnumSet;
import static org.junit.jupiter.api.Assertions.*;

public class CellTest {

    @Test
    void testCellConstructor() {
        Cell cell = new Cell();
        CellAttributes attributes = cell.getAttributes();

        assertEquals(' ', cell.getCharacter());
        assertEquals(Color.DEFAULT, attributes.getForeground());
        assertEquals(Color.DEFAULT, attributes.getBackground());
        assertTrue(attributes.getStyles().isEmpty());
    }

    @Test
    void testCellConstructorParameters() {
        EnumSet<Style> styles = EnumSet.of(Style.BOLD, Style.ITALIC);
        Cell cell = new Cell('A', Color.RED, Color.BLUE, styles);
        CellAttributes attributes = cell.getAttributes();

        assertEquals('A', cell.getCharacter());
        assertEquals(Color.RED, attributes.getForeground());
        assertEquals(Color.BLUE, attributes.getBackground());
        assertTrue(attributes.getStyles().contains(Style.BOLD));
        assertTrue(attributes.getStyles().contains(Style.ITALIC));
    }

    @Test
    void testAddStyles() {
        Cell cell = new Cell();
        CellAttributes attributes = cell.getAttributes();

        attributes.addStyle(Style.BOLD);
        attributes.addStyle(Style.ITALIC);

        assertTrue(attributes.hasStyle(Style.BOLD));
        assertTrue(attributes.hasStyle(Style.ITALIC));
    }

    @Test
    void testRemoveStyles() {
        Cell cell = new Cell();
        CellAttributes attributes = cell.getAttributes();

        attributes.addStyle(Style.BOLD);
        attributes.addStyle(Style.ITALIC);
        attributes.removeStyle(Style.BOLD);

        assertFalse(attributes.hasStyle(Style.BOLD));
        assertTrue(attributes.hasStyle(Style.ITALIC));
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
        CellAttributes attributes = cell.getAttributes();

        attributes.setForeground(Color.GREEN);
        attributes.setBackground(Color.YELLOW);

        assertEquals(Color.GREEN, attributes.getForeground());
        assertEquals(Color.YELLOW, attributes.getBackground());
    }

    @Test
    void testClear() {
        Cell cell = new Cell('A', Color.RED, Color.BLUE, EnumSet.of(Style.BOLD));
        cell.clear();
        CellAttributes attributes = cell.getAttributes();

        assertEquals(' ', cell.getCharacter());
        assertEquals(Color.DEFAULT, attributes.getForeground());
        assertEquals(Color.DEFAULT, attributes.getBackground());
        assertTrue(attributes.getStyles().isEmpty());
    }

    @Test
    void testCopy() {
        Cell original = new Cell('A', Color.RED, Color.BLUE, EnumSet.of(Style.BOLD));
        Cell copy = original.copy();

        CellAttributes originalAttributes = original.getAttributes();
        CellAttributes copyAttributes = copy.getAttributes();

        assertEquals(original.getCharacter(), copy.getCharacter());
        assertEquals(originalAttributes.getForeground(), copyAttributes.getForeground());
        assertEquals(originalAttributes.getBackground(), copyAttributes.getBackground());
        assertEquals(originalAttributes.getStyles(), copyAttributes.getStyles());

        // Modify the copy
        copy.setCharacter('B');
        copyAttributes.setForeground(Color.GREEN);
        copyAttributes.setBackground(Color.YELLOW);
        copyAttributes.addStyle(Style.ITALIC);

        // Test original remains unchanged while copy has new value
        assertEquals('A', original.getCharacter());
        assertEquals('B', copy.getCharacter());

        assertEquals(Color.RED, originalAttributes.getForeground());
        assertEquals(Color.BLUE, originalAttributes.getBackground());
        assertEquals(Color.GREEN, copyAttributes.getForeground());
        assertEquals(Color.YELLOW, copyAttributes.getBackground());

        assertTrue(originalAttributes.hasStyle(Style.BOLD));
        assertFalse(originalAttributes.hasStyle(Style.ITALIC));
        assertTrue(copyAttributes.hasStyle(Style.BOLD));
        assertTrue(copyAttributes.hasStyle(Style.ITALIC));
    }

}
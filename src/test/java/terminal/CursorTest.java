package terminal;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CursorTest {

    @Test
    void testCursorConstructor() {
        Cursor cursor = new Cursor();
        assertEquals(0, cursor.getRow());
        assertEquals(0, cursor.getColumn());
    }

    @Test
    void testCursorConstructorParams() {
        Cursor cursor = new Cursor(5, 10);
        assertEquals(5, cursor.getRow());
        assertEquals(10, cursor.getColumn());
    }

    @Test
    void testSetPosition() {
        Cursor cursor = new Cursor();
        cursor.setPosition(3, 7);
        assertEquals(3, cursor.getRow());
        assertEquals(7, cursor.getColumn());
    }

}

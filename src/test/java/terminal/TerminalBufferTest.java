package terminal;

import org.junit.jupiter.api.Test;

import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.*;

public class TerminalBufferTest {

    @Test
    public void testTerminalConstructor() {
        TerminalBuffer termbuf = new TerminalBuffer(80, 24, 1000);

        assertEquals(80, termbuf.getWidth());
        assertEquals(24, termbuf.getHeight());
        assertEquals(1000, termbuf.getMaxScrollback());

        assertNotNull(termbuf.getScreen());
        assertNotNull(termbuf.getScrollback());
        assertEquals(EnumSet.noneOf(Style.class), termbuf.getCurrentAttributes().getStyles());
    }

    @Test
    public void testWriteWrapping() {
        TerminalBuffer termbuf = new TerminalBuffer(10, 7, 1000);
        String longText = "This is a long line that should wrap around the terminal buffer.";
        termbuf.overwrite(longText);

        String expected =
                "This is a \n" +
                "long line \n" +
                "that shoul\n" +
                "d wrap aro\n" +
                "und the te\n" +
                "rminal buf\n" +
                "fer.      \n";

        assertEquals(expected, termbuf.printScreen());
    }

    @Test
    public void testCursorClamp(){
        TerminalBuffer termbuf = new TerminalBuffer(80, 24, 1000);

        termbuf.cursorSetPosition(-5, -10);
        assertEquals(0, termbuf.getCursor().getRow());
        assertEquals(0, termbuf.getCursor().getColumn());

        termbuf.cursorSetPosition(100, 200);
        assertEquals(23, termbuf.getCursor().getRow());
        assertEquals(79, termbuf.getCursor().getColumn());
    }

    @Test
    public void testCursorUpDown() {
        TerminalBuffer termbuf = new TerminalBuffer(80, 24, 1000);

        termbuf.cursorSetPosition(5, 10);
        termbuf.cursorMoveUp(3);
        assertEquals(2, termbuf.getCursor().getRow());
        assertEquals(10, termbuf.getCursor().getColumn());

        termbuf.cursorMoveDown(5);
        assertEquals(7, termbuf.getCursor().getRow());
        assertEquals(10, termbuf.getCursor().getColumn());
    }

    @Test
    public void testPrintScreen(){
        TerminalBuffer termbuf = new TerminalBuffer(5, 3, 1000);
        CellAttributes attrs = new CellAttributes(Color.RED, Color.BLUE, EnumSet.of(Style.BOLD));
        termbuf.getScreen().get(0).overwrite(0, "Hello", attrs);
        termbuf.getScreen().get(1).overwrite(0, "World", attrs);
        termbuf.getScreen().get(2).overwrite(0, "!", attrs);

        String expected = "Hello\nWorld\n!    \n";
        assertEquals(expected, termbuf.printScreen());
    }
}

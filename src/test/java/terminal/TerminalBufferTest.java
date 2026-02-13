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
        termbuf.write(longText);

        String expected =
                "This is a \n" +
                "long line \n" +
                "that shoul\n" +
                "d wrap aro\n" +
                "und the te\n" +
                "rminal buf\n" +
                "fer.      \n";

        assertEquals(expected, termbuf.getScreenAsString());
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
    public void testGetScreenAsString(){
        TerminalBuffer termbuf = new TerminalBuffer(5, 3, 1000);
        CellAttributes attrs = new CellAttributes(Color.RED, Color.BLUE, EnumSet.of(Style.BOLD));
        termbuf.getScreen().get(0).overwrite(0, "Hello", attrs);
        termbuf.getScreen().get(1).overwrite(0, "World", attrs);
        termbuf.getScreen().get(2).overwrite(0, "!", attrs);

        String expected = "Hello\nWorld\n!    \n";
        assertEquals(expected, termbuf.getScreenAsString());
    }

    @Test
    public void testInsertText() {
        TerminalBuffer termbuf = new TerminalBuffer(10, 3, 1000);
        termbuf.write("Hello");
        termbuf.getCursor().setPosition(0, 3);
        termbuf.insert("World");

        String expected =
                "HelWorldlo\n" +
                "          \n" +
                "          \n";
        assertEquals(expected, termbuf.getScreenAsString());
    }

    @Test
    public void testWriteAtStart() {
        TerminalBuffer termbuf = new TerminalBuffer(10, 3, 1000);
        termbuf.write("InitialTxt");
        termbuf.cursorSetPosition(0, 0);
        termbuf.write("New");

        String expected =
                "NewtialTxt\n" +
                        "          \n" +
                        "          \n";
        assertEquals(expected, termbuf.getScreenAsString());
    }

    @Test
    public void testWriteAtEnd() {
        TerminalBuffer termbuf = new TerminalBuffer(10, 3, 1000);
        termbuf.write("Hello");
        termbuf.cursorSetPosition(0, 7);
        termbuf.write("End");

        String expected =
                "Hello  End\n" +
                        "          \n" +
                        "          \n";
        assertEquals(expected, termbuf.getScreenAsString());
    }

    @Test
    public void testInsertAtStart() {
        TerminalBuffer termbuf = new TerminalBuffer(10, 3, 1000);
        termbuf.write("World");
        termbuf.cursorSetPosition(0, 0);
        termbuf.insert("Hello");

        String expected =
                "HelloWorld\n" +
                        "          \n" +
                        "          \n";
        assertEquals(expected, termbuf.getScreenAsString());
    }

    @Test
    public void testInsertMidLine() {
        TerminalBuffer termbuf = new TerminalBuffer(15, 3, 1000);
        termbuf.write("HelloWorld");
        termbuf.cursorSetPosition(0, 5);
        termbuf.insert(" Big ");

        String expected =
                "Hello Big World\n" +
                        "               \n" +
                        "               \n";
        assertEquals(expected, termbuf.getScreenAsString());
    }

    @Test
    public void testScrollUp() {
        TerminalBuffer termbuf = new TerminalBuffer(10, 3, 1000);
        termbuf.write("Line1");
        termbuf.cursorNextLine();
        termbuf.write("Line2");
        termbuf.cursorNextLine();
        termbuf.write("Line3");
        termbuf.cursorNextLine();

        String expected =
                "Line2     \n" +
                "Line3     \n" +
                "          \n";
        assertEquals(expected, termbuf.getScreenAsString());
        assertEquals("Line1     ", termbuf.getScrollback().getFirst().toString());


    }

    @Test
    public void testEntireBufferIncludesScrollback() {
        TerminalBuffer buf = new TerminalBuffer(5, 2, 10);

        buf.write("ABCDE");
        buf.write("FGHIJ");
        buf.write("KLMNO");

        String all = buf.getEntireBufferAsString();

        assertTrue(all.contains("ABCDE"));
    }

    @Test
    public void testExtremeScrollback() {
        TerminalBuffer buf = new TerminalBuffer(5, 2, 1);

        buf.write("ABCDE");
        buf.write("FGHIJ");
        buf.write("KLMNO");
        buf.write("PQRST");

        String all = buf.getEntireBufferAsString();

        assertFalse(all.contains("ABCDE"));
        assertTrue(all.contains("FGHIJ"));
    }

    @Test
    public void testClearScreen() {
        TerminalBuffer buf = new TerminalBuffer(5, 2, 10);

        buf.write("ABCDE");
        buf.write("FGHIJ");

        buf.clearScreen();

        String expected =
                "     \n" +
                "     \n";
        assertEquals(expected, buf.getScreenAsString());
    }

    @Test
    public void testClearScreenAndScrollback() {
        TerminalBuffer buf = new TerminalBuffer(5, 2, 10);

        buf.write("ABCDE");
        buf.write("FGHIJ");
        buf.write("KLMNO");
        buf.write("PQRST");

        buf.clearScreenAndScrollback();

        String expected =
                "     \n" +
                "     \n";
        assertEquals(expected, buf.getScreenAsString());
        assertTrue(buf.getScrollback().isEmpty());

    }

    @Test
    public void testFillLineException() {
        TerminalBuffer buf = new TerminalBuffer(5, 2, 10);

        assertThrows(IndexOutOfBoundsException.class, () -> buf.fillLine(-1, 'A'));
        assertThrows(IndexOutOfBoundsException.class, () -> buf.fillLine(5, 'B'));
    }

    @Test
    public void testFillLine() {
        TerminalBuffer buf = new TerminalBuffer(5, 2, 10);
        buf.fillLine(0, 'A');
        buf.fillLine(1, 'B');

        String expected =
                "AAAAA\n" +
                "BBBBB\n";
        assertEquals(expected, buf.getScreenAsString());
    }

    @Test
    public void testGetLineAsString(){
        TerminalBuffer buf = new TerminalBuffer(5, 2, 10);
        buf.write("Hello");
        buf.cursorNextLine();
        buf.write("World");

        assertEquals("Hello", buf.getLineAsString(0));
        assertEquals("World", buf.getLineAsString(1));
    }

    @Test
    public void testInsertEmptyLineAtBottom() {
        TerminalBuffer buf = new TerminalBuffer(5, 2, 10);
        buf.write("Hello");
        buf.cursorNextLine();
        buf.write("World");

        assertEquals("Hello", buf.getLineAsString(0));
        assertEquals("World", buf.getLineAsString(1));

        buf.insertEmptyLineAtBottom();
        buf.insertEmptyLineAtBottom();

        String expected =
                "     \n" +
                "     \n";
        assertEquals(expected, buf.getScreenAsString());
    }

    @Test
    public void testGetCharAt() {
        TerminalBuffer buf = new TerminalBuffer(5, 2, 10);
        buf.write("Hello");
        buf.cursorNextLine();
        buf.write("World");

        assertEquals('H', buf.getCharAt(0, 0));
        assertEquals('e', buf.getCharAt(0, 1));
        assertEquals('W', buf.getCharAt(1, 0));
    }

    @Test
    public void testGetAttributesAt() {
        TerminalBuffer buf = new TerminalBuffer(5, 2, 10);
        CellAttributes attrs = new CellAttributes(Color.RED, Color.BLUE, EnumSet.of(Style.BOLD));
        buf.getCurrentAttributes().setForeground(Color.RED);
        buf.getCurrentAttributes().setBackground(Color.BLUE);
        buf.getCurrentAttributes().addStyle(Style.BOLD);

        buf.write("Hello");
        buf.cursorNextLine();
        buf.write("World");

        assertEquals(attrs, buf.getAttributesAt(0, 0));
        assertEquals(attrs, buf.getAttributesAt(0, 1));
        assertEquals(attrs, buf.getAttributesAt(1, 0));
    }

    @Test
    public void testResetAttributes() {
        TerminalBuffer buf = new TerminalBuffer(5, 2, 10);
        buf.setForeground(Color.RED);
        buf.setBackground(Color.BLUE);
        buf.addStyle(Style.BOLD);

        buf.write("Hello");
        buf.cursorNextLine();
        buf.write("World");

        buf.resetAttributes();

        assertEquals(new CellAttributes(), buf.getCurrentAttributes());
    }

}

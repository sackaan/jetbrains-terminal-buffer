# Terminal Buffer Implementation

## Overview

This project implements a simplified terminal text buffer — the core data structure used by terminal emulators to store and manipulate displayed text.

The buffer models:

- A fixed-size visible screen (width x height)
- A scrollback history of lines that have scrolled off the screen
- A cursor position
- Character attributes (foreground color, background color, styles)


---

## Architecture & Design

The system is structured into several core components:

### 1. `Cell`

Represents a single character cell in the terminal grid.

Each cell stores:
- Character
- Foreground color (16 colors + default)
- Background color
- Style flags (bold, italic, underline)

`CellAttributes` is implemented as a value object and supports equality comparison.

---

### 2. `Line`

Represents a single row of the terminal screen.

Responsibilities:
- Store a fixed number of `Cell` objects
- Support overwrite and insert operations within the row
- Clear or fill the row

`Line` does not know about the cursor, screen height, or scrollback — it is purely row-level logic.

---

### 3. `TerminalBuffer`

The central state machine of the terminal.

Responsibilities:
- Maintain screen and scrollback
- Maintain cursor position
- Maintain current attributes
- Handle writing and insert operations
- Handle wrapping and scrolling
- Provide read-only access to content

This class orchestrates interactions between lines and maintains correct terminal behavior.

---

## Wrapping Behavior (Deferred Wrap)

The implementation uses **deferred wrapping** via a `wrapPending` flag.

When a character is written to the last column:
- The cursor does not immediately move to the next line.
- Instead, wrapping is deferred until the next character is written.

This prevents double scrolling issues.

---

## Scrolling & Scrollback

When writing reaches the last line and needs to advance:

1. The top visible line is removed from the screen.
2. It is appended to scrollback.
3. If scrollback exceeds its maximum size, the oldest line is discarded.
4. A new empty line is added at the bottom.

Scrollback is exposed as an unmodifiable view.

---

## Supported Operations

### Setup
- Configurable width
- Configurable height
- Configurable scrollback maximum size

### Cursor
- Move up, down, left, right (clamped to screen bounds)
- Set cursor position

### Editing
- Write (overwrite mode)
- Insert (line-level insert)
- Fill a line
- Insert empty line at bottom
- Clear screen
- Clear screen and scrollback

### Content Access
- Get character at global position (scrollback + screen)
- Get attributes at global position
- Get line as string
- Get full screen content
- Get full buffer content (scrollback + screen)

---

## Trade-offs & Design Decisions

### 1. Separation of Concerns
- `Line` handles row-level operations only.
- `TerminalBuffer` handles wrapping, scrolling, and cursor logic.
- `Cursor` stores position only.

---

### 2. Scrollback Implementation
Scrollback is implemented using `ArrayDeque` for efficient FIFO behavior.

For global row access, scrollback is temporarily converted to a list.  
This is simple and sufficient, though not optimal for very large histories.

---

### 3. Deferred Wrapping
Using `wrapPending` improves correctness but slightly increases state complexity.

An alternative would be immediate wrapping, but that can cause incorrect scroll behavior.

---

### 4. Attribute Handling
Attributes are stored in `CellAttributes`, treated as a value object with proper `equals()` and `hashCode()` implementations.

---

## Limitations & Possible Improvements

If extended further, the following improvements could be made:

- Optimize scrollback random access
- Add configurable auto-wrap mode
- Improve performance by avoiding temporary list allocation
- Make buffer thread-safe

---

## Build Instructions

This project uses **Maven**.

To build and run tests:

```bash
mvn clean test
```
package org.emmazarate.gameoflife.model;

import javafx.scene.control.Cell;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BoundedBoardTest {

    private Board board;

    @BeforeEach
    void setUp() {
        this.board = new BoundedBoard(5, 3);
    }

    @Test
    void copy_sameSizeAsOriginal() {
        Board copy = board.copy();

        assertEquals(5, copy.getWidth());
        assertEquals(3, copy.getHeight());
    }

    @Test
    void copy_deepCopy() { // Shallow copy - new Object that points to same Object attributes, Deep copy has its own attribute values
        Board copy = board.copy();

        copy.setState(3, 2, CellState.ALIVE);

        assertEquals(CellState.ALIVE, copy.getState(3, 2));
    }

    @Test
    void copy_contentsAreSame() {
        board.setState(0, 0, CellState.ALIVE);
        board.setState(0, 1, CellState.ALIVE);
        board.setState(0, 2, CellState.ALIVE);

        Board copy = board.copy();

        for (int x = 0; x < copy.getWidth(); x++) {
            for (int y = 0; y < copy.getHeight(); y++) {
                assertEquals(board.getState(x, y), copy.getState(x, y));
            }
        }
    }

    @Test
    void setState_getState_setAndGetDoNotFailOutOfBounds() {

        // No assertion, expecting no errors
        board.setState(-1, 0, CellState.ALIVE);
        board.setState(5, 0, CellState.ALIVE);
        board.setState(0, -1, CellState.ALIVE);
        board.setState(0, -3, CellState.ALIVE);

        board.getState(-1, 0);
        board.getState(5, 0);
        board.getState(0, -1);
        board.getState(0, -3);
    }

    @Test
    void setState_getState_returnsUpdatedState() {
        board.setState(4, 1, CellState.ALIVE);

        assertEquals(CellState.ALIVE, board.getState(4, 1));
    }
}
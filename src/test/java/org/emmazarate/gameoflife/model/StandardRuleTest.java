package org.emmazarate.gameoflife.model;

import javafx.scene.control.Cell;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StandardRuleTest {

    private Board board;
    private SimulationRule simulationRule;

    @BeforeEach
    void setUp() {
        board = new BoundedBoard(3, 3);
        simulationRule = new StandardRule();
    }

    @Test
    void getNextState_alive_noNeighborsResultsInCellDeath() {
        board.setState(1, 1, CellState.ALIVE);


        CellState nextState = simulationRule.getNextState(1, 1, board);

        assertEquals(CellState.DEAD, nextState);
    }

    @Test
    void getNextState_alive_oneNeighborResultsInCellDeath() {
        board.setState(1, 1, CellState.ALIVE);
        board.setState(0, 0, CellState.ALIVE);

        CellState nextState = simulationRule.getNextState(1, 1, board);

        assertEquals(CellState.DEAD, nextState);
    }

    @Test
    void getNextState_alive_twoNeighborsResultsInCellAlive() {
        board.setState(1, 1, CellState.ALIVE);
        board.setState(2, 2, CellState.ALIVE);
        board.setState(1, 2, CellState.ALIVE);

        CellState nextState = simulationRule.getNextState(1, 1, board);

        assertEquals(CellState.ALIVE, nextState);
    }

    @Test
    void getNextState_alive_threeNeighborsResultsInCellAlive() {
        board.setState(1, 1, CellState.ALIVE);
        board.setState(0, 2, CellState.ALIVE);
        board.setState(2, 1, CellState.ALIVE);
        board.setState(2, 2, CellState.ALIVE);

        CellState nextState = simulationRule.getNextState(1, 1, board);

        assertEquals(CellState.ALIVE, nextState);
    }

    @Test
    void getNextState_alive_fourNeighborsResultsInCellAlive() {
        board.setState(1, 1, CellState.ALIVE);
        board.setState(0, 2, CellState.ALIVE);
        board.setState(2, 1, CellState.ALIVE);
        board.setState(0, 1, CellState.ALIVE);
        board.setState(1, 0, CellState.ALIVE);

        CellState nextState = simulationRule.getNextState(1, 1, board);

        assertEquals(CellState.DEAD, nextState);
    }

    @Test
    void getNextState_alive_eightNeighborsResultsInDeadCellState() {
        for (int y = 0; y < board.getHeight(); y++) {
            for (int x = 0; x < board.getWidth(); x++) {
                board.setState(x, y, CellState.ALIVE);
            }
        }

        CellState nextState = simulationRule.getNextState(1, 1, board);

        assertEquals(CellState.DEAD, nextState);
    }

    @Test
    void getNextState_dead_noNeighbors() {
        board.setState(1, 1, CellState.DEAD);

        CellState nextState = simulationRule.getNextState(1, 1, board);

        assertEquals(CellState.DEAD, nextState);
    }

    @Test
    void getNextState_dead_twoNeighbors() {
        board.setState(1, 1, CellState.DEAD);
        board.setState(2, 2, CellState.ALIVE);
        board.setState(1, 2, CellState.ALIVE);

        CellState nextState = simulationRule.getNextState(1, 1, board);

        assertEquals(CellState.DEAD, nextState);
    }

    @Test
    void getNextState_dead_threeNeighbors() {
        board.setState(1, 1, CellState.ALIVE);
        board.setState(2, 2, CellState.ALIVE);
        board.setState(1, 2, CellState.ALIVE);
        board.setState(0, 1, CellState.ALIVE);

        CellState nextState = simulationRule.getNextState(1, 1, board);

        assertEquals(CellState.ALIVE, nextState);
    }

    @Test
    void getNextState_dead_fourNeighbors() {
        board.setState(1, 1, CellState.ALIVE);
        board.setState(2, 2, CellState.ALIVE);
        board.setState(1, 2, CellState.ALIVE);
        board.setState(0, 1, CellState.ALIVE);
        board.setState(0, 2, CellState.ALIVE);

        CellState nextState = simulationRule.getNextState(1, 1, board);

        assertEquals(CellState.DEAD, nextState);
    }

    @Test
    void getNextState_dead_eightNeighborsResultsInDeadCellState() {
        for (int y = 0; y < board.getHeight(); y++) {
            for (int x = 0; x < board.getWidth(); x++) {
                board.setState(x, y, CellState.ALIVE);
            }
        }

        board.setState(1, 1, CellState.ALIVE);

        CellState nextState = simulationRule.getNextState(1, 1, board);

        assertEquals(CellState.DEAD, nextState);
    }
}

package org.emmazarate.gameoflife.model;

public interface SimulationRule {
    CellState getNextState(int x, int y, Board board);
}

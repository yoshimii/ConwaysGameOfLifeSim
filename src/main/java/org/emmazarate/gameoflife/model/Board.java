package org.emmazarate.gameoflife.model;

public interface Board {

    Board copy();

    CellState getState(int x, int y);

    void setState(int x, int y, CellState cellState);

    int getWidth();

    int getHeight();
}

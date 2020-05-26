package org.emmazarate.gameoflife.viewmodel;

import org.emmazarate.gameoflife.model.Board;
import org.emmazarate.gameoflife.util.Property;

public class BoardViewModel {

    private Property<Board> board = new Property<>();

    public Property<Board> getBoard() {
        return board;
    }
}

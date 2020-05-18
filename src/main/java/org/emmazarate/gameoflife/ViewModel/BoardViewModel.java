package org.emmazarate.gameoflife.ViewModel;
import org.emmazarate.gameoflife.model.Board;

import java.util.LinkedList;
import java.util.List;

public class BoardViewModel {

    private Board board;
    private List<SimpleChangeListener<Board>> boardListeners;

    public BoardViewModel() {
        this.boardListeners = new LinkedList<>(); // Listeners are good for linear search
    }

    public void listenToBoard(SimpleChangeListener<Board> listener) {
        this.boardListeners.add(listener);
    }

    public void setBoard(Board board) {
        this.board = board;

        notifyBoardListeners();
    }

    private void notifyBoardListeners() {
        for (SimpleChangeListener<Board> boardListener : boardListeners) {
            boardListener.valueChanged(this.board);
        }
    }

    public Board getBoard() {
        return board;
    }
}

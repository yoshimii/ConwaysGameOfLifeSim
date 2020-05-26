package org.emmazarate.gameoflife.viewmodel;

import org.emmazarate.gameoflife.model.Board;
import org.emmazarate.gameoflife.model.CellPosition;
import org.emmazarate.gameoflife.model.CellState;
import org.emmazarate.gameoflife.util.Property;

import java.util.LinkedList;
import java.util.List;

public class EditorViewModel {

    private Property<CellState> drawMode = new Property<>(CellState.ALIVE);
    private Property<CellPosition> cursorPosition = new Property<>();

    private BoardViewModel boardViewModel;
    private Board editorBoard;
    private boolean drawingEnabled = true;

    public EditorViewModel(BoardViewModel boardViewModel, Board editorBoard) {
        this.boardViewModel = boardViewModel;
        this.editorBoard = editorBoard;
    }

    public void onAppStateChanged(ApplicationState state) {
        if (state == ApplicationState.EDITING) {
            drawingEnabled = true;
            this.boardViewModel.getBoard().set(editorBoard);
        } else {
            drawingEnabled = false;
        }
    }

    public void boardPressed(CellPosition cursorPosition) {
        if (drawingEnabled) {
            this.editorBoard.setState(cursorPosition.getX(), cursorPosition.getY(), drawMode.get()); // changing state on initial board
            this.boardViewModel.getBoard().set(this.editorBoard); // telling the view model that initial board is updated
        }
    }

    public Property<CellState> getDrawMode() {
        return drawMode;
    }

    public Property<CellPosition> getCursorPosition() {
        return cursorPosition;
    }

    public Board getBoard() {
        return editorBoard;
    }
}

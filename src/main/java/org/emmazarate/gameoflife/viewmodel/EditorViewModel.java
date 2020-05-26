package org.emmazarate.gameoflife.viewmodel;

import org.emmazarate.gameoflife.model.Board;
import org.emmazarate.gameoflife.model.CellState;

import java.util.LinkedList;
import java.util.List;

public class EditorViewModel {

    private CellState drawMode = CellState.ALIVE;
    private List<SimpleChangeListener<CellState>> drawModeListeners;

    private BoardViewModel boardViewModel;
    private Board editorBoard;
    private boolean drawingEnabled = true;

    public EditorViewModel(BoardViewModel boardViewModel, Board editorBoard) {
        this.boardViewModel = boardViewModel;
        this.editorBoard = editorBoard;
        this.drawModeListeners = new LinkedList<>();
    }

    public void listenToDrawMode(SimpleChangeListener<CellState> listener) {
        drawModeListeners.add(listener);
    }

    public void onAppStateChanged(ApplicationState state) {
        if (state == ApplicationState.EDITING) {
            drawingEnabled = true;
            this.boardViewModel.setBoard(editorBoard);
        } else {
            drawingEnabled = false;
        }
    }

    public void setDrawMode(CellState drawMode) {
        this.drawMode = drawMode;
        notifyDrawModeListeners();
    }

    private void notifyDrawModeListeners() {
        for (SimpleChangeListener<CellState> drawModeListener: drawModeListeners) {
            drawModeListener.valueChanged(drawMode);
        }
    }

    public void boardPressed(int simX, int simY) {
        if (drawingEnabled) {
            this.editorBoard.setState(simX, simY, drawMode); // changing state on initial board
            this.boardViewModel.setBoard(this.editorBoard); // telling the view model that initial board is updated
        }
    }
}

package org.emmazarate.gameoflife;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import org.emmazarate.gameoflife.model.CellState;
import org.emmazarate.gameoflife.viewmodel.EditorViewModel;

public class MainView extends BorderPane {

    private final EditorViewModel editorViewModel;

    public MainView(EditorViewModel editorViewModel) {
        this.editorViewModel = editorViewModel;
        this.setOnKeyPressed(this::onKeyPressed);
    }

    private void onKeyPressed(KeyEvent keyEvent) {
        if(keyEvent.getCode() == KeyCode.D) {
            this.editorViewModel.getDrawMode().set(CellState.ALIVE);
        } else if(keyEvent.getCode() == KeyCode.E){
            this.editorViewModel.getDrawMode().set(CellState.DEAD);
        }
    }

}

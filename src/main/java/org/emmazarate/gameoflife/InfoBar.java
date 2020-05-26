package org.emmazarate.gameoflife;
import org.emmazarate.gameoflife.viewmodel.EditorViewModel;
import org.emmazarate.gameoflife.model.CellState;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

public class InfoBar extends HBox {

    private static String drawModeFormat = "Draw Mode: %s";
    private static String cursorPositionFormat = "Cursor: (%d, %d)";

    private Label cursor;
    private Label editingTool;
    public InfoBar(EditorViewModel editorViewModel) {
        editorViewModel.listenToDrawMode(this::setDrawMode);
        this.editingTool = new Label("Draw mode: Drawing");
        this.cursor = new Label("Cursor: (0, 0)");

        Pane spacer = new Pane();
        spacer.setMinSize(0,0);
        spacer.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        HBox.setHgrow(spacer, Priority.ALWAYS);

        this.setCursorPosition(0, 0);

        this.getChildren().addAll(this.editingTool, spacer, this.cursor);
    }

    private void setDrawMode(CellState drawMode) {
        String drawModeString;
        if (drawMode == CellState.ALIVE) {
            drawModeString = "Drawing";
        } else {
            drawModeString = "Erasing";
        }

        this.editingTool.setText(String.format(drawModeFormat, drawModeString));
    }

    public void setCursorPosition(int x, int y) {
        this.cursor.setText(String.format(cursorPositionFormat, x, y));
    }
}

package org.emmazarate.gameoflife.view;
import org.emmazarate.gameoflife.model.Board;
import org.emmazarate.gameoflife.model.CellPosition;
import org.emmazarate.gameoflife.model.CellState;
import org.emmazarate.gameoflife.viewmodel.BoardViewModel;
import org.emmazarate.gameoflife.viewmodel.EditorViewModel;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;

public class SimulationCanvas extends Pane {

    private Canvas canvas;

    private Affine affine;
    private EditorViewModel editorViewModel;
    private BoardViewModel boardViewModel;

    public SimulationCanvas(EditorViewModel editorViewModel, BoardViewModel boardViewModel) {
        this.editorViewModel = editorViewModel;
        this.boardViewModel = boardViewModel;
        boardViewModel.getBoard().listen(this::draw);
        editorViewModel.getCursorPosition().listen(cellPosition -> draw(boardViewModel.getBoard().get()));

        this.canvas = new Canvas(400, 400);
        this.canvas.setOnMousePressed(this::handleDraw);
        this.canvas.setOnMouseDragged(this::handleDraw);
        this.canvas.setOnMouseMoved(this::handleCursorMoved);
        this.canvas.widthProperty().bind(this.widthProperty());
        this.canvas.heightProperty().bind(this.heightProperty());
        this.getChildren().add(this.canvas);

        this.affine = new Affine();
        this.affine.appendScale(400 / 10f, 400 / 10f);
    }

    private void handleCursorMoved(MouseEvent event) {
        CellPosition cursorPosition = this.getSimulationCoordinates(event);
        this.editorViewModel.getCursorPosition().set(cursorPosition);
    }

    @Override
    public void resize(double v, double v1) {
        super.resize(v, v1);
        draw(boardViewModel.getBoard().get());
    }

    private void handleDraw(MouseEvent event) {
        CellPosition cursorPosition = this.getSimulationCoordinates(event);
        this.editorViewModel.boardPressed(cursorPosition);
    }

    private CellPosition getSimulationCoordinates(MouseEvent mouseEvent) {
        double mouseX = mouseEvent.getX();
        double mouseY = mouseEvent.getY();

        try {
            Point2D simCoordinate = this.affine.inverseTransform(mouseX, mouseY);
            return new CellPosition((int) simCoordinate.getX(), (int) simCoordinate.getY());
        } catch (NonInvertibleTransformException e) {
            throw new RuntimeException("Non invertible transform");
        }
    }

    private void draw(Board board) {
        GraphicsContext g = this.canvas.getGraphicsContext2D();
        g.setTransform(this.affine);

        // canvas color
        g.setFill(Color.LIGHTGRAY);
        g.fillRect(0, 0, 400, 400);

        this.drawSimulation(board);

        // cursor hover cell color
        if (editorViewModel.getCursorPosition().isPresent()) {
            CellPosition cursor = editorViewModel.getCursorPosition().get();
            g.setFill(new Color(1, 1, 0, 0.5));
            g.fillRect(cursor.getX(), cursor.getY(), 1, 1);
        }

        //grid lines
        g.setStroke(Color.RED);
        g.setLineWidth(0.1f);
        for (int x = 0; x <= board.getWidth(); x++) {
            g.strokeLine(x, 0, x, board.getHeight());
        }

        for (int y = 0; y <= board.getWidth(); y++) {
            g.strokeLine(0, y, board.getWidth(), y);
        }
    }

    private void drawSimulation(Board simulationToDraw) {
        GraphicsContext g = this.canvas.getGraphicsContext2D();

        //cell color
        g.setFill(Color.GREEN);
        for (int x = 0; x < simulationToDraw.getWidth(); x++) {
            for (int y = 0; y < simulationToDraw.getHeight(); y++) {
                if(simulationToDraw.getState(x,y) == CellState.ALIVE)
                    g.fillRect(x,y,1,1);
            }
        }
    }
}

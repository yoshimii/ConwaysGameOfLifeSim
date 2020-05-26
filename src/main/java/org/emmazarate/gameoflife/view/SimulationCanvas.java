package org.emmazarate.gameoflife.view;
import org.emmazarate.gameoflife.model.Board;
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
        boardViewModel.listenToBoard(this::draw);

        this.canvas = new Canvas(400, 400);
        this.canvas.setOnMousePressed(this::handleDraw);
        this.canvas.setOnMouseDragged(this::handleDraw);
        this.canvas.widthProperty().bind(this.widthProperty());
        this.canvas.heightProperty().bind(this.heightProperty());
        this.getChildren().add(this.canvas);

        this.affine = new Affine();
        this.affine.appendScale(400 / 10f, 400 / 10f);
    }

    @Override
    public void resize(double v, double v1) {
        super.resize(v, v1);
        draw(boardViewModel.getBoard());
    }

    private void handleDraw(MouseEvent event) {

        Point2D simCoordinate = this.getSimulationCoordinates(event);

        int simX = (int) simCoordinate.getX();
        int simY = (int) simCoordinate.getY();

        System.out.println(simX + "," + simY);

        this.editorViewModel.boardPressed(simX, simY);
    }

    private Point2D getSimulationCoordinates(MouseEvent mouseEvent) {
        double mouseX = mouseEvent.getX();
        double mouseY = mouseEvent.getY();

        try {
            return this.affine.inverseTransform(mouseX, mouseY);
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

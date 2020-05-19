package org.emmazarate.gameoflife;
import org.emmazarate.gameoflife.ViewModel.ApplicationViewModel;
import org.emmazarate.gameoflife.ViewModel.BoardViewModel;
import org.emmazarate.gameoflife.ViewModel.EditorViewModel;
import org.emmazarate.gameoflife.ViewModel.SimulationViewModel;
import org.emmazarate.gameoflife.model.Board;
import org.emmazarate.gameoflife.model.CellState;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;

public class MainView extends VBox {

    private InfoBar infoBar;
    private Canvas canvas;

    private Affine affine;

    private EditorViewModel editorViewModel;
    private BoardViewModel boardViewModel;

    public MainView(ApplicationViewModel applicationViewModel, BoardViewModel boardViewModel, EditorViewModel editorViewModel, SimulationViewModel simulationViewModel) {
        this.boardViewModel = boardViewModel;
        this.editorViewModel = editorViewModel;

        this.boardViewModel.listenToBoard(this::onBoardChanged);


        this.canvas = new Canvas(400, 400);
        this.canvas.setOnMousePressed(this::handleDraw);
        this.canvas.setOnMouseDragged(this::handleDraw);
        this.canvas.setOnMouseMoved(this::handleMouseMoved);

        this.setOnKeyPressed(this::onKeyPressed);

        Toolbar toolbar = new Toolbar(editorViewModel, applicationViewModel, simulationViewModel);

        this.infoBar = new InfoBar(editorViewModel);
        this.infoBar.setCursorPosition(0, 0);

        Pane spacer = new Pane();
        spacer.setMinSize(0,0);
        spacer.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        VBox.setVgrow(spacer, Priority.ALWAYS);

        this.getChildren().addAll(toolbar, this.canvas, spacer, infoBar);

        this.affine = new Affine();
        this.affine.appendScale(400 / 10f, 400 / 10f);
    }

    private void onBoardChanged(Board board) {
        draw(board);
    }

    private void handleMouseMoved(MouseEvent event) {
        Point2D simCoordinate = this.getSimulationCoordinates(event);

        this.infoBar.setCursorPosition((int) simCoordinate.getX(), (int) simCoordinate.getY());
    }

    private void onKeyPressed(KeyEvent keyEvent) {
        if(keyEvent.getCode() == KeyCode.D) {
            this.editorViewModel.setDrawMode(CellState.ALIVE);
        } else if(keyEvent.getCode() == KeyCode.E){
            this.editorViewModel.setDrawMode(CellState.DEAD);
        }
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
            Point2D simCoordinate = this.affine.inverseTransform(mouseX, mouseY);
            return simCoordinate;
        } catch (NonInvertibleTransformException e) {
            throw new RuntimeException("Non invertible transform");
        }
    }

    public void draw(Board board) {
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
            g.strokeLine(x, 0, x, 10);
        }

        for (int y = 0; y <= board.getHeight(); y++) {
            g.strokeLine(0, y, 10, y);
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

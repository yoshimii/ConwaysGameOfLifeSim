package org.emmazarate.gameoflife;
import org.emmazarate.gameoflife.ViewModel.ApplicationState;
import org.emmazarate.gameoflife.ViewModel.ApplicationViewModel;
import org.emmazarate.gameoflife.ViewModel.BoardViewModel;
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

    private InfoBar infoBar = new InfoBar();
    private Canvas canvas;

    private Affine affine;

    private Board initialBoard;

    private CellState drawMode = CellState.ALIVE;

    private ApplicationViewModel applicationViewModel;
    private BoardViewModel boardViewModel;

    private boolean isDrawingEnabled = true;

    public MainView(ApplicationViewModel applicationViewModel, BoardViewModel boardViewModel, Board initialBoard) {
        this.applicationViewModel = applicationViewModel;
        this.boardViewModel = boardViewModel;
        this.initialBoard = initialBoard;

        this.applicationViewModel.listenToApplicationState(this::onApplicationStateChanged);
        this.boardViewModel.listenToBoard(this::onBoardChanged);


        this.canvas = new Canvas(400, 400);
        this.canvas.setOnMousePressed(this::handleDraw);
        this.canvas.setOnMouseDragged(this::handleDraw);
        this.canvas.setOnMouseMoved(this::handleMouseMoved);

        this.setOnKeyPressed(this::onKeyPressed);

        Toolbar toolbar = new Toolbar(this, applicationViewModel, boardViewModel);

        this.infoBar = new InfoBar();
        this.infoBar.setDrawMode(this.drawMode);
        this.infoBar.setCursorPosition(0, 0);

        Pane spacer = new Pane();
        spacer.setMinSize(0,0);
        spacer.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        VBox.setVgrow(spacer, Priority.ALWAYS);

        this.getChildren().addAll(toolbar, this.canvas, spacer, infoBar);

        this.affine = new Affine();
        this.affine.appendScale(400 / 10f, 400 / 10f);
    }

    private void onApplicationStateChanged(ApplicationState state) {
        if(state == ApplicationState.EDITING) {
            this.isDrawingEnabled = true;
            this.boardViewModel.setBoard(this.initialBoard);
        } else if (state == ApplicationState.SIMULATING){
            this.isDrawingEnabled = false;
        } else {
            throw new IllegalArgumentException("Unsupported ApplicationState" + state.name());
        }
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
            this.drawMode = CellState.ALIVE;
            System.out.println("Alive draw mode");
        } else if(keyEvent.getCode() == KeyCode.E){
            this.drawMode = CellState.DEAD;
            System.out.println("Dead draw mode");
        }
    }

    private void handleDraw(MouseEvent event) {

        if (!isDrawingEnabled) {
            return;
        }

        Point2D simCoordinate = this.getSimulationCoordinates(event);

        int simX = (int) simCoordinate.getX();
        int simY = (int) simCoordinate.getY();

        System.out.println(simX + "," + simY);

        this.initialBoard.setState(simX, simY, drawMode); // changing state on initial board
        this.boardViewModel.setBoard(this.initialBoard); // telling the view model that initial board is updated
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

    public void setDrawMode(CellState newDrawMode) {
        this.drawMode = newDrawMode;
        this.infoBar.setDrawMode(newDrawMode);
    }
}

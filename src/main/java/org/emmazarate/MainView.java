package org.emmazarate;

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

import java.nio.file.attribute.PosixFileAttributes;

public class MainView extends VBox {

    public static final int EDITING = 0;
    public static final int  SIMULATING = 1;

    private InfoBar infoBar = new InfoBar();
    private Canvas canvas;

    private Affine affine;
    private Simulation initialSimulation;
    private Simulator simulator;

    private Simulation simulation;

    private int drawMode = Simulation.ALIVE;

    private int applicationState = EDITING;

    public MainView() {
        this.canvas = new Canvas(400, 400);
        this.canvas.setOnMousePressed(this::handleDraw);
        this.canvas.setOnMouseDragged(this::handleDraw);
        this.canvas.setOnMouseMoved(this::handleMouseMoved);

        this.setOnKeyPressed(this::onKeyPressed);

        Toolbar toolbar = new Toolbar(this);

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

        this.initialSimulation = new Simulation(10, 10);
        this.simulation = Simulation.copy(this.initialSimulation);
    }

    private void handleMouseMoved(MouseEvent event) {
        Point2D simCoordinate = this.getSimulationCoordinates(event);

        this.infoBar.setCursorPosition((int) simCoordinate.getX(), (int) simCoordinate.getY());
    }

    private void onKeyPressed(KeyEvent keyEvent) {
        if(keyEvent.getCode() == KeyCode.D) {
            this.drawMode = Simulation.ALIVE;
            System.out.println("Alive draw mode");
        } else if(keyEvent.getCode() == KeyCode.E){
            this.drawMode = Simulation.DEAD;
            System.out.println("Dead draw mode");
        }
    }

    private void handleDraw(MouseEvent event) {

        if (this.applicationState == SIMULATING) {
            return;
        }

        Point2D simCoordinate = this.getSimulationCoordinates(event);

        int simX = (int) simCoordinate.getX();
        int simY = (int) simCoordinate.getY();

        System.out.println(simX + "," + simY);

        this.initialSimulation.setState(simX, simY, drawMode);
    draw();
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

    public void draw() {
        GraphicsContext g = this.canvas.getGraphicsContext2D();
        g.setTransform(this.affine);
        
        // canvas color
        g.setFill(Color.LIGHTGRAY);
        g.fillRect(0, 0, 400, 400);

        if(this.applicationState == EDITING) {
            drawSimulation(this.initialSimulation);
        } else {
            drawSimulation(this.simulation);
        }
        
        //grid lines
        g.setStroke(Color.RED);
        g.setLineWidth(0.1f);
        for (int x = 0; x <= this.simulation.width; x++) {
            g.strokeLine(x, 0, x, 10);
        }

        for (int y = 0; y <= this.simulation.height; y++) {
            g.strokeLine(0, y, 10, y);
        }
    }

    private void drawSimulation(Simulation simulationToDraw) {
        GraphicsContext g = this.canvas.getGraphicsContext2D();

        //cell color
        g.setFill(Color.PINK);
        for (int x = 0; x < simulationToDraw.width; x++) {
            for (int y = 0; y < simulationToDraw.height; y++) {
                if(simulationToDraw.getState(x,y) == Simulation.ALIVE)
                    g.fillRect(x,y,1,1);
            }
        }
    }

    public Simulation getSimulation() {
        return this.simulation;
    }

    public void setDrawMode(int newDrawMode) {
        this.drawMode = newDrawMode;
        this.infoBar.setDrawMode(newDrawMode);
    }

    public void setApplicationState(int applicationState) {
        if (applicationState == this.applicationState) {
            return;
        }

        if (applicationState == SIMULATING) {
            this.simulation = Simulation.copy(this.initialSimulation);
            this.simulator = new Simulator(this, this.simulation);
        }

        this.applicationState = applicationState;

        System.out.println("Application State: " + this.applicationState);
    }

    public Simulator getSimulator() {
        return simulator;
    }
}

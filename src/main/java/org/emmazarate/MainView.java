package org.emmazarate;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;

public class MainView extends VBox {
    private Canvas canvas;

    private Affine affine;

    private Simulation simulation;

    private int drawMode = Simulation.ALIVE;

    public MainView() {
        this.canvas = new Canvas(400, 400);
        this.canvas.setOnMousePressed(this::handleDraw);
        this.canvas.setOnMouseDragged(this::handleDraw);

        this.setOnKeyPressed(this::onKeyPressed);

        Toolbar toolbar = new Toolbar(this);

        this.getChildren().addAll(toolbar, this.canvas);

        this.affine = new Affine();
        this.affine.appendScale(400 / 10f, 400 / 10f);

        this.simulation = new Simulation(10, 10);
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

    private void handleDraw(MouseEvent mouseEvent) {
        double mouseX = mouseEvent.getX();
        double mouseY = mouseEvent.getY();

        try {
            Point2D simCoordinate = this.affine.inverseTransform(mouseX, mouseY);
            int simX = (int) simCoordinate.getX();
            int simY = (int) simCoordinate.getY();

            System.out.println(simX + "," + simY);

            this.simulation.setState(simX, simY, drawMode);

        } catch (NonInvertibleTransformException e) {
            System.out.println("Could not invert transform");
            e.printStackTrace();
        }

        draw();
    }

    public void draw() {
        GraphicsContext g = this.canvas.getGraphicsContext2D();
        g.setTransform(this.affine);
        
        // canvas color
        g.setFill(Color.LIGHTGRAY);
        g.fillRect(0, 0, 400, 400);
        
        //cell color
        g.setFill(Color.PINK);
        for (int x = 0; x < simulation.width; x++) {
            for (int y = 0; y < simulation.height; y++) {
                if(this.simulation.getState(x,y) == Simulation.ALIVE)
                g.fillRect(x,y,1,1);
            }
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

    public Simulation getSimulation() {
        return this.simulation;
    }

    public void setDrawMode(int newDrawMode) {
        this.drawMode = newDrawMode;
    }
}

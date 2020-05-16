package org.emmazarate;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;

public class Toolbar extends ToolBar {
    private MainView mainView;
    public Toolbar(MainView mainView) {
        this.mainView = mainView;
        Button draw = new Button("Draw");
        draw.setOnAction(this::handleDraw);
        Button erase = new Button("Erase");
        erase.setOnAction(this::handleErase);
        Button step = new Button("Step");
        step.setOnAction(this::handleStep);
        Button reset = new Button("Reset");
        reset.setOnAction(this::handleReset);
        Button start = new Button("Start");
        start.setOnAction(this::handleStart);
        Button stop = new Button("Stop");
        stop.setOnAction(this::handleStop);

        this.getItems().addAll(draw, erase, reset, step, start, stop);
    }

    private void handleStart(ActionEvent actionEvent) {
        this.mainView.setApplicationState(MainView.SIMULATING);
        this.mainView.getSimulator().start();
    }

    private void handleStop(ActionEvent actionEvent) {
        this.mainView.getSimulator().stop();
    }

    private void handleReset(ActionEvent actionEvent) {
        this.mainView.setApplicationState(MainView.EDITING);
        this.mainView.draw();
    }

    private void handleDraw(ActionEvent actionEvent) {
        System.out.println("Draw pressed");
        this.mainView.setDrawMode(Simulation.ALIVE);

    }

    private void handleErase(ActionEvent actionEvent) {
        System.out.println("Erase pressed");
        this.mainView.setDrawMode(Simulation.DEAD);
    }

    private void handleStep(ActionEvent actionEvent) {
        System.out.println("Step pressed");

        this.mainView.setApplicationState(MainView.SIMULATING);

        this.mainView.getSimulation().step();
        mainView.draw();
    }
}

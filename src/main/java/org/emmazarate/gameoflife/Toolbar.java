package org.emmazarate.gameoflife;
import org.emmazarate.gameoflife.ViewModel.*;
import org.emmazarate.gameoflife.model.CellState;
import org.emmazarate.gameoflife.model.StandardRule;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;


public class Toolbar extends ToolBar {

    private ApplicationViewModel applicationViewModel;
    private SimulationViewModel simulationViewModel;
    private EditorViewModel editorViewModel;

    public Toolbar(EditorViewModel editorViewModel, ApplicationViewModel applicationViewModel, SimulationViewModel simulationViewModel) {
        this.editorViewModel = editorViewModel;
        this.applicationViewModel = applicationViewModel;
        this.simulationViewModel = simulationViewModel;
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
        switchToSimulateState();
        this.simulationViewModel.start();
    }

    private void handleStop(ActionEvent actionEvent) {
        simulationViewModel.stop();
    }

    private void handleReset(ActionEvent actionEvent) {
        this.applicationViewModel.setCurrentState(ApplicationState.EDITING);
    }

    private void handleDraw(ActionEvent actionEvent) {
        System.out.println("Draw pressed");
        this.editorViewModel.setDrawMode(CellState.ALIVE);

    }

    private void handleErase(ActionEvent actionEvent) {
        System.out.println("Erase pressed");
        this.editorViewModel.setDrawMode(CellState.DEAD);
    }

    private void handleStep(ActionEvent actionEvent) {
        System.out.println("Step pressed");

        switchToSimulateState();

        this.simulationViewModel.doStep();
    }

    private void switchToSimulateState() {
        this.applicationViewModel.setCurrentState(ApplicationState.SIMULATING);
    }
}

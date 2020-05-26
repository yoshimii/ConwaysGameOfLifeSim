package org.emmazarate.gameoflife.viewmodel;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import org.emmazarate.gameoflife.Simulation;
import org.emmazarate.gameoflife.model.StandardRule;


public class SimulationViewModel {
// business logic class, only deals with model objects or other business logic components
    private Timeline timeline;
    private BoardViewModel boardViewModel;
    private ApplicationViewModel applicationViewModel;
    private EditorViewModel editorViewModel;
    private Simulation simulation;

    public SimulationViewModel(BoardViewModel boardViewModel, ApplicationViewModel applicationViewModel, EditorViewModel editorViewModel) {
        this.boardViewModel = boardViewModel;
        this.applicationViewModel = applicationViewModel;
        this.editorViewModel = editorViewModel;

        this.timeline = new Timeline(new KeyFrame(Duration.millis(500), event -> this.doStep()));
        this.timeline.setCycleCount(Timeline.INDEFINITE);

        this.simulation = new Simulation(editorViewModel.getBoard(), new StandardRule());
    }

    public void handle(SimulatorEvent event) {
        switch (event.getEventType()) {
            case START:
                start();
                break;
            case STOP:
                stop();
                break;
            case STEP:
                doStep();
                break;
            case RESET:
               reset();
               break;
        }
    }

    public void doStep() { // ActionEvent can represent a Button fire or KeyFrame completion and more
// Abstraction -
        if (applicationViewModel.getApplicationState().get() != ApplicationState.SIMULATING) {
            applicationViewModel.getApplicationState().set(ApplicationState.SIMULATING);
        }
        this.simulation.step();
        this.boardViewModel.getBoard().set(this.simulation.getBoard());
    }

    public void start() {
        this.timeline.play();
    }

    public void stop() {
        this.timeline.stop();
    }

    public void reset() {
        this.simulation = new Simulation(editorViewModel.getBoard(), new StandardRule());
        this.applicationViewModel.getApplicationState().set(ApplicationState.EDITING);
    }
}

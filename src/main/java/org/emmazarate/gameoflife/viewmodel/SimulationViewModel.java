package org.emmazarate.gameoflife.viewmodel;
import org.emmazarate.gameoflife.Simulation;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import org.emmazarate.gameoflife.model.StandardRule;


public class SimulationViewModel {
// business logic class, only deals with model objects or other business logic components
    private Timeline timeline;
    private BoardViewModel boardViewModel;
    private Simulation simulation;

    public SimulationViewModel(BoardViewModel boardViewModel) {
        this.boardViewModel = boardViewModel;
        this.timeline = new Timeline(new KeyFrame(Duration.millis(500), event -> this.doStep()));
        this.timeline.setCycleCount(Timeline.INDEFINITE);
    }

    public void onAppStateChanged(ApplicationState state) {
        if (state == ApplicationState.SIMULATING) {
            this.simulation = new Simulation(boardViewModel.getBoard(), new StandardRule());
        }

    }

    public void doStep() { // ActionEvent can represent a Button fire or KeyFrame completion and more
// Abstraction - 
        this.simulation.step();
        this.boardViewModel.setBoard(this.simulation.getBoard());
    }

    public void start() {
        this.timeline.play();
    }

    public void stop() {
        this.timeline.stop();
    }
}

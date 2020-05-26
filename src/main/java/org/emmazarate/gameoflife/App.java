package org.emmazarate.gameoflife;
import org.emmazarate.gameoflife.util.event.EventBus;
import org.emmazarate.gameoflife.view.SimulationCanvas;
import org.emmazarate.gameoflife.viewmodel.*;
import org.emmazarate.gameoflife.model.Board;
import org.emmazarate.gameoflife.model.BoundedBoard;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * JavaFX Conway's Game of Life App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) {
        EventBus eventBus = new EventBus();

        ApplicationViewModel applicationViewModel = new ApplicationViewModel();
        BoardViewModel boardViewModel = new BoardViewModel();
        Board board = new BoundedBoard(1200, 800);
        EditorViewModel editorViewModel = new EditorViewModel(boardViewModel, board);
        SimulationViewModel simulationViewModel = new SimulationViewModel(boardViewModel, applicationViewModel, editorViewModel);
        eventBus.listenFor(SimulatorEvent.class, simulationViewModel::handle);

        applicationViewModel.getApplicationState().listen(editorViewModel::onAppStateChanged);

        boardViewModel.getBoard().set(board);

        SimulationCanvas simulationCanvas = new SimulationCanvas(editorViewModel, boardViewModel);
        Toolbar toolbar = new Toolbar(editorViewModel, eventBus);
        InfoBar infoBar = new InfoBar(editorViewModel);

        MainView mainView = new MainView(editorViewModel);
        mainView.setTop(toolbar);
        mainView.setCenter(simulationCanvas);
        mainView.setBottom(infoBar);

        Scene scene = new Scene(mainView, 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
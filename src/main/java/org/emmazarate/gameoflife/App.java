package org.emmazarate.gameoflife;
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
        ApplicationViewModel applicationViewModel = new ApplicationViewModel(ApplicationState.EDITING);
        BoardViewModel boardViewModel = new BoardViewModel();
        Board board = new BoundedBoard(1200, 800);
        EditorViewModel editorViewModel = new EditorViewModel(boardViewModel, board);
        SimulationViewModel simulationViewModel = new SimulationViewModel(boardViewModel);

        applicationViewModel.listenToApplicationState(editorViewModel::onAppStateChanged);
        applicationViewModel.listenToApplicationState(simulationViewModel::onAppStateChanged);

        boardViewModel.setBoard(board);

        SimulationCanvas simulationCanvas = new SimulationCanvas(editorViewModel, boardViewModel);
        Toolbar toolbar = new Toolbar(editorViewModel, applicationViewModel, simulationViewModel);
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
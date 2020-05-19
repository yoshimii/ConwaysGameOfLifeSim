package org.emmazarate.gameoflife;
import org.emmazarate.gameoflife.ViewModel.*;
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
        Board board = new BoundedBoard(10, 10);
        EditorViewModel editorViewModel = new EditorViewModel(boardViewModel, board);
        SimulationViewModel simulationViewModel = new SimulationViewModel(boardViewModel);
        applicationViewModel.listenToApplicationState(editorViewModel::onAppStateChanged);
        applicationViewModel.listenToApplicationState(simulationViewModel::onAppStateChanged);

        MainView mainView = new MainView(applicationViewModel, boardViewModel, editorViewModel, simulationViewModel );
        Scene scene = new Scene(mainView, 640, 480);
        stage.setScene(scene);
        stage.show();

        boardViewModel.setBoard(board);
    }

    public static void main(String[] args) {
        launch();
    }

}
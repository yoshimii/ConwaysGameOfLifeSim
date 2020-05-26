package org.emmazarate.gameoflife.viewmodel;

import java.util.LinkedList;
import java.util.List;

public class ApplicationViewModel {

    private ApplicationState currentState;
    private List<SimpleChangeListener<ApplicationState>> applicationStateListeners;

    public ApplicationViewModel(ApplicationState currentState) {
        this.currentState = currentState;
        this.applicationStateListeners = new LinkedList<>();
    }

    public void listenToApplicationState(SimpleChangeListener<ApplicationState> listener) {
        this.applicationStateListeners.add(listener);
    }

    public void setCurrentState(ApplicationState newState) {
        if (newState != this.currentState){
            this.currentState = newState;
            notifyApplicationStateListeners();
        }
    }

    private void notifyApplicationStateListeners() {
        for (SimpleChangeListener<ApplicationState> applicationStateListener : applicationStateListeners) {
            applicationStateListener.valueChanged(this.currentState);
        }
    }
}

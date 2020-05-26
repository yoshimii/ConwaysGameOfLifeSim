package org.emmazarate.gameoflife.viewmodel;

import org.emmazarate.gameoflife.util.Property;

import java.util.LinkedList;
import java.util.List;

public class ApplicationViewModel {

    private Property<ApplicationState> applicationState = new Property<>(ApplicationState.EDITING);

    public ApplicationViewModel() {
    }

    public Property<ApplicationState> getApplicationState() {
        return applicationState;
    }
}

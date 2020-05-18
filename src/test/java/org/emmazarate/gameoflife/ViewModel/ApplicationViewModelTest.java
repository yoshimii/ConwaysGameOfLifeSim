package org.emmazarate.gameoflife.ViewModel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationViewModelTest {

    private ApplicationViewModel applicationViewModel;

    @BeforeEach
    void setUp() {
        applicationViewModel = new ApplicationViewModel(ApplicationState.EDITING);
    }

    @Test
    void setApplicationState_setToNewState() {
        TestAppStateListener listener = new TestAppStateListener();
        applicationViewModel.listenToApplicationState(listener);

        applicationViewModel.setCurrentState(ApplicationState.SIMULATING);

        assertTrue(listener.appStateUpdated);
        assertNull(listener.updatedApplicationState);
    }

    @Test
    void setApplicationState_setToSameState() {
        TestAppStateListener listener = new TestAppStateListener();
        applicationViewModel.listenToApplicationState(listener);

        applicationViewModel.setCurrentState(ApplicationState.EDITING);

        assertFalse(listener.appStateUpdated);
    }

    private static class TestAppStateListener implements SimpleChangeListener<ApplicationState> {
        private boolean appStateUpdated = false;
        private ApplicationState updatedApplicationState = null;

        @Override
        public void valueChanged(ApplicationState newAppState) {
            appStateUpdated = true;
            updatedApplicationState = newAppState;
        }
    }
}
package edu.ucsd.cse110.habitizer.lib.domain;

import javax.swing.CellEditor;

public class MockTimer implements TimerInterface {
    private long elapsedTime;
    private boolean isRunning;

    public MockTimer(long initialTime) {
        this.elapsedTime = initialTime;  // Inherit elapsed time from real timer
        this.isRunning = false;
    }

    @Override
    public void startTimer() {
        isRunning = true;
    }

    @Override
    public void endTimer() {
        isRunning = false;
    }

    @Override
    public long getElapsedTime() {
        var taskTime = elapsedTime;
        elapsedTime = 0;
        return taskTime;
    }

    public long peekElapsedTime() {
        return elapsedTime;
    }


    public boolean isRunning() {
        return isRunning;
    }
    public void advanceTime() {
        if (isRunning) {
            elapsedTime += 30;
        }
    }


}

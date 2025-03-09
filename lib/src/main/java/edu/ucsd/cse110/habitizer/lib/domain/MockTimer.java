package edu.ucsd.cse110.habitizer.lib.domain;

import javax.swing.CellEditor;

public class MockTimer implements TimerInterface {
    private long elapsedTime;
    private boolean isRunning;
    private boolean isPaused;


    public MockTimer(long initialTime) {
        this.elapsedTime = initialTime;  // Inherit elapsed time from real timer
        this.isRunning = false;
        this.isPaused = false;

    }

    @Override
    public void startTimer() {
        isRunning = true;
        isPaused = false;
    }

    @Override
    public void endTimer() {
        isRunning = false;
        isPaused = false;
    }

    @Override
    public long getElapsedTime() {
        long taskTime = elapsedTime;
        elapsedTime = 0;
        return taskTime;
    }

    public long peekElapsedTime() {
        return elapsedTime;
    }

    public void pauseTimer() {
        // Stop advancing time when paused
        isRunning = false;
        isPaused = true;

    }

    public void resumeTimer() {
        isRunning = true;
        isPaused = false;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public boolean isRunning() {
        return isRunning;
    }
    public void advanceTime() {
        if (isRunning) {
            elapsedTime += 15;
        }
    }


}

package edu.ucsd.cse110.habitizer.lib.domain;

public class MockTimer implements TimerInterface {
    private long startTime;
    private long prevTime;
    private long pausedTime;
    private boolean isPaused;
    private long advanceOffset;
    private long currentTime; // added to simulate time
    private boolean hasStarted;  // flag to indicate if the timer has been started

    public MockTimer(long initialTime) {
        this.currentTime = initialTime;
        this.startTime = 0;
        this.prevTime = 0;
        this.pausedTime = 0;
        this.isPaused = false;
        this.advanceOffset = 0;
        this.hasStarted = false;
    }

    @Override
    public void startTimer() {
        this.startTime = currentTime;
        this.prevTime = currentTime;
        this.hasStarted = true;
        this.isPaused = false;
        this.advanceOffset = 0;
    }

    @Override
    public void endTimer() {
        this.startTime = 0;
        this.prevTime = 0;
        this.pausedTime = 0;
        this.isPaused = false;
        this.advanceOffset = 0;
        this.hasStarted = false;
    }

    @Override
    public long getElapsedTime() {
        if (!hasStarted) {
            return 0;
        }
        if (isPaused) {
            return pausedTime - startTime + advanceOffset;
        }
        long elapsedTime = currentTime - prevTime;
        prevTime = currentTime;
        return elapsedTime;
    }

    @Override
    public long peekElapsedTime() {
        if (!hasStarted) {
            return 0;
        }
        if (isPaused) {
            return pausedTime - startTime + advanceOffset;
        }
        return currentTime - startTime + advanceOffset;
    }

    @Override
    public long peekTaskElapsedTime() {
        if (!hasStarted) {
            return 0;
        }
        if (isPaused) {
            return pausedTime - prevTime + advanceOffset;
        }
        return currentTime - prevTime + advanceOffset;
    }

    @Override
    public void pauseTimer() {
        if (!isPaused) {
            this.pausedTime = currentTime;
            this.isPaused = true;
        }
    }

    @Override
    public void resumeTimer() {
        if (isPaused) {
            long pausedElapsed = pausedTime - startTime + advanceOffset;
            startTime = currentTime - pausedElapsed;
            prevTime = currentTime;
            advanceOffset = 0;
            isPaused = false;
        }
    }

    @Override
    public void advanceTime() {
        if (isPaused) {
            advanceOffset += 15;
        } else {
            currentTime += 15;
        }
    }

    // Additional helper methods for tests.
    public boolean isPaused() {
        return isPaused;
    }

    public boolean isRunning() {
        return hasStarted && !isPaused;
    }
}

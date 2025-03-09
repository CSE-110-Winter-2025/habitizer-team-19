package edu.ucsd.cse110.habitizer.lib.domain;

//Getting Time logic: https://stackoverflow.com/questions/5175728/how-to-get-the-current-date-time-in-java

public class Timer implements TimerInterface {
    private long startTime;
    private long prevTime;
    private long pausedTime;
    private boolean isPaused;
    private long advanceOffset;


    public Timer() {
        this.startTime = 0;
        this.prevTime = 0;
        this.pausedTime = 0;
        this.isPaused = false;
        this.advanceOffset = 0;
    }

    public void startTimer() {
        //Convert from the given milliseconds to seconds
        long current = System.currentTimeMillis() / 1000;
        this.startTime = current;
        this.prevTime = current;
        this.isPaused = false;
        this.advanceOffset = 0;
    }

    public void endTimer() {
        this.startTime = 0;
        this.prevTime = 0;
        this.pausedTime = 0;
        this.isPaused = false;
        this.advanceOffset = 0;
    }

    public long getElapsedTime() {
        if (startTime == 0) {
            return 0;
        }
        if (isPaused) {
            return pausedTime - startTime + advanceOffset;
        }
        long currentTime = System.currentTimeMillis() / 1000;
        long elapsedTime = currentTime - prevTime;
        prevTime = currentTime;
        return elapsedTime;
    }

    public void pauseTimer() {
        if (!isPaused) {
            this.pausedTime = System.currentTimeMillis() / 1000;
            this.isPaused = true;
        }
    }

    public void resumeTimer() {
        if (isPaused) {
            long currentTime = System.currentTimeMillis() / 1000;
            long pausedElapsed = pausedTime - startTime + advanceOffset;
            startTime = currentTime - pausedElapsed;
            prevTime = currentTime - (pausedTime - prevTime + advanceOffset);
            advanceOffset = 0;
            isPaused = false;
        }

    }

    public long peekElapsedTime() {
        if (startTime == 0) {
            return 0;
        }
        if (isPaused) {
            return pausedTime - startTime + advanceOffset;
        }
        return (System.currentTimeMillis() / 1000) - startTime + advanceOffset;
    }

    public void advanceTime() {
        if (isPaused) {
            advanceOffset += 15;
        }
    }
}

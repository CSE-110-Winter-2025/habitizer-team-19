package edu.ucsd.cse110.habitizer.lib.domain;

//Getting Time logic: https://stackoverflow.com/questions/5175728/how-to-get-the-current-date-time-in-java

public class Timer implements TimerInterface {
    private long startTime;
    private long prevTime;
    private long pausedTime;
    private boolean isPaused;


    public Timer(){
        this.startTime = 0;
        this.prevTime = 0;
        this.pausedTime = 0;
        this.isPaused = false;
    }

    public void startTimer() {
        //Convert from the given milliseconds to seconds
        this.startTime = System.currentTimeMillis()/1000;
        this.prevTime = System.currentTimeMillis()/1000;
        this.isPaused = false;
    }

    public void endTimer(){
        this.startTime = 0;
        this.prevTime = 0;
        this.pausedTime = 0;
        this.isPaused = false;
    }

    public long getElapsedTime() {
        if (startTime == 0){
            return 0; //return 0 if the timer has not been started
        }
        if (isPaused) {
            return pausedTime - startTime;
        }
        long elapsedTime = System.currentTimeMillis()/1000 - prevTime;
        this.prevTime = System.currentTimeMillis()/1000;
        return elapsedTime;
    }

    public void pauseTimer() {
        if (!isPaused) {
            this.pausedTime = System.currentTimeMillis()/1000;
            this.isPaused = true;
        }
    }

    public void resumeTimer() {
        if (isPaused) {
            long currentTime = System.currentTimeMillis()/1000;
            this.prevTime += currentTime - pausedTime;
            this.isPaused = false;
        }
    }

    public long getPrevTime(){
        return this.prevTime;
    }

}

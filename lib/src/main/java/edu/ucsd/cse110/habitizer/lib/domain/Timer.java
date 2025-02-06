package edu.ucsd.cse110.habitizer.lib.domain;

import java.time.Duration;
import java.time.Instant;

public class Timer {
    private Instant startTime;

    private Instant endTime;

    private Time elapsedTime;

    public Timer(){
        this.startTime = null;
        this.endTime = null;
        this.elapsedTime = null;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public void startTimer(){
        startTime = Instant.now();
    }

    public void endTimer(){
        endTime = Instant.now();
    }

    public Time calculateElapsedTime(){
        if(startTime != null && endTime != null){
            long elapsedSeconds = Duration.between(startTime, endTime).getSeconds();
            elapsedTime = new Time(elapsedSeconds);
            return elapsedTime;
        } else{
            throw new IllegalArgumentException("Timer did not work");
        }
    }

}

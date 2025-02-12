package edu.ucsd.cse110.habitizer.lib.domain;

//Getting Time logic: https://stackoverflow.com/questions/5175728/how-to-get-the-current-date-time-in-java

public class Timer {
    private long startTime;

    public Timer(){
        this.startTime = 0;
    }

    public void startTimer() {
        //Convert from the given milliseconds to seconds
        this.startTime = System.currentTimeMillis()/1000;
    }

    public long getElapsedTime() {
        if (startTime == 0){
            return 0; //return 0 if the timer has not been started
        }
        return (System.currentTimeMillis()/1000)-this.startTime;
    }
}

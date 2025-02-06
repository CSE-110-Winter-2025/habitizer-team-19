package edu.ucsd.cse110.habitizer.lib.domain;

import androidx.annotation.NonNull;

public class Task {

    private @NonNull Timer timer;
    private @NonNull String title;
    private @NonNull boolean isCompleted;
    private @NonNull boolean isSkipped;
    private Time elapsedTime;

    public Task(String title){
        this.title = title;
        this.isCompleted = false;
        this.isSkipped = false;
        this.timer = new Timer();
        this.elapsedTime = new Time(0);
    }

    public void restartTimer(){
        timer.startTimer();
    }

    public boolean isCompleted(){
        return isCompleted;
    }

    public boolean isSkipped() {
        return isSkipped;
    }

    public void complete(){
        isCompleted = true;
        timer.endTimer();
        elapsedTime = timer.calculateElapsedTime();
    }

    public void skip(){
        isSkipped = true;
        timer.endTimer();
    }

}

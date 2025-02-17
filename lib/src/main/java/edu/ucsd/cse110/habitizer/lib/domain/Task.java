package edu.ucsd.cse110.habitizer.lib.domain;

import androidx.annotation.NonNull;

public class Task {
    private @NonNull String name;
    private int CompletionStatus;
    //0: not completed or skipped, 1: completed, 2: skipped. Someone could assign these values to strings that we can use instead
    private long elapsedTime;
    //any negative value (or just -1) gets displayed as "-" as in skipped or not completed

    public Task(@NonNull String name){
        this.name = name;
        this.CompletionStatus = 0;
        this.elapsedTime = -1;
    }

    public String getName() {
        return this.name;
    }

    public long getElapsedTime() {
        return this.elapsedTime;
    }

    public void setElapsedTime(long elapsedTime){
        this.elapsedTime = elapsedTime;
    }

    public String getElapsedTimeToString() {
        if(elapsedTime == -1){
            return "--:--:--";
        }
        var hours = this.elapsedTime / 3600;
        var minutes = (this.elapsedTime % 3600) / 60;
        var seconds = this.elapsedTime % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public int getCompletionStatus() {
        return this.CompletionStatus;
    }

    public void complete(){
        CompletionStatus = 1;
    }

    public void skip(){
        CompletionStatus = 2;
    }

    public void reset() {
        CompletionStatus = 0;
        elapsedTime = -1;
    }

    public void newName(String newName) {
        this.name = newName;
    }

}



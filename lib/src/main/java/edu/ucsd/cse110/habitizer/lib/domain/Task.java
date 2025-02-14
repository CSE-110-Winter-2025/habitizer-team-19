package edu.ucsd.cse110.habitizer.lib.domain;

import androidx.annotation.NonNull;

public class Task {
    private @NonNull String name;
    private int CompletionStatus;
    //0: not completed or skipped, 1: completed, 2: skipped. Someone could assign these values to strings that we can use instead
    private long elapsedTime;
    //any negative value (or just -1) gets displayed as "-" as in skipped or not completed

    private long prevTaskTime;

    public Task(@NonNull String name){
        this.name = name;
        this.CompletionStatus = 0;
        this.elapsedTime = -1;
        this.prevTaskTime = -1;
    }

    public String getName() {
        return this.name;
    }

    public long getElapsedTime() {
        return this.elapsedTime;
    }

    public int getCompletionStatus() {
        return this.CompletionStatus;
    }

    /*
    public long getPrevTaskTime() {

    }

     */

    public void complete(long time){
        CompletionStatus = 1;
        elapsedTime = time; // in what units? seconds? milliseconds?
    }

    public void skip(){
        CompletionStatus = 2;
    }

    public void reset() {
        CompletionStatus = 0;
        elapsedTime = 0;
    }

    public void newName(String newName) {
        this.name = newName;
    }

}

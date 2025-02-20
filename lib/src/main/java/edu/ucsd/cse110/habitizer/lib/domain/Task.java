package edu.ucsd.cse110.habitizer.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Task {

    //Data Field
    private @Nullable Integer id;
    private @NonNull String name;
    private @NonNull Integer completionStatus; // 0: not completed or skipped, 1: completed, 2: skipped
    private long elapsedTime; // -1 means skipped or not completed


    // Constructors
    public Task(@NonNull String name){
        this.id = null;
        this.name = name;
        this.completionStatus = 0;
        this.elapsedTime = -1;
    }
    public Task(@Nullable Integer id, @NonNull String name) {
        this.id = id;
        this.name = name;
        this.completionStatus = 0;
        this.elapsedTime = -1;
    }

    //Getters and Setters
    public @Nullable Integer id(){return id;}

    public @NonNull String getName(){return name;}

    public @NonNull Integer getCompletionStatus(){return completionStatus;}
    public void setCompletionStatus(Integer i){this.completionStatus = i;}
    public long getElapsedTime(){return elapsedTime;}
    public void setElapsedTime(long elapsedTime){this.elapsedTime = elapsedTime;}

    // Other Methods
    public Task withId(int id){
        return new Task(id,this.name);
    }

    public void reset(){
        this.completionStatus = 0;
        this.elapsedTime = -1;
    }

//    // Task Properties
//    public String getName() {
//        return this.name;
//    }
//
//    public void setName(@NonNull String newName) {
//        this.name = newName;
//    }
//
//    public int getCompletionStatus() {
//        return this.completionStatus;
//    }
//
//    // Task Completion Management
//    public void complete() {
//        completionStatus = 1;
//    }
//
//    public void skip() {
//        completionStatus = 2;
//    }
//
//    public void reset() {
//        completionStatus = 0;
//        elapsedTime = -1;
//    }
//
//    // Task Time Management
//    public long getElapsedTime() {
//        return this.elapsedTime;
//    }
//
//    public void setElapsedTime(long elapsedTime) {
//        this.elapsedTime = elapsedTime;
//    }
//
//    public String getElapsedTimeToString() {
//        return (elapsedTime == -1) ? "--:--:--" : formatElapsedTime(elapsedTime);
//    }
//
//    // Helper Methods
//    private String formatElapsedTime(long timeInSeconds) {
//        long hours = timeInSeconds / 3600;
//        long minutes = (timeInSeconds % 3600) / 60;
//        long seconds = timeInSeconds % 60;
//        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
//    }
}

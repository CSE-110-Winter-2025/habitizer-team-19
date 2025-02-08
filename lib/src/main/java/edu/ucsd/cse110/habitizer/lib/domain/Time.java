package edu.ucsd.cse110.habitizer.lib.domain;

import androidx.annotation.NonNull;


public class Time {
    private @NonNull int hours;
    private @NonNull int minutes;
    private @NonNull int seconds;

    private @NonNull long totalSeconds;

    public Time(long totalSeconds){
        this.hours = (int) (totalSeconds / 3600);
        this.minutes = (int) ((totalSeconds % 3600) / 60);
        this.seconds = (int) (totalSeconds % 60);
        this.totalSeconds = totalSeconds;
    }

    public Time(int hours,int minutes, int seconds){
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
        this.totalSeconds = (hours * 3600) + (minutes * 60) + (seconds);
    }


    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    @Override
    public String toString() {
        return hours+":"+minutes+":"+seconds;
    }
}

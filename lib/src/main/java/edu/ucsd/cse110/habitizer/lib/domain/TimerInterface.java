package edu.ucsd.cse110.habitizer.lib.domain;

public interface TimerInterface {
    void startTimer();
    void endTimer();
    long getElapsedTime();
    void pauseTimer();
    void resumeTimer();
}

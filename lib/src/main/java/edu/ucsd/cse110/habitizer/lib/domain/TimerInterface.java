package edu.ucsd.cse110.habitizer.lib.domain;

public interface TimerInterface {
    void startTimer();
    void endTimer();
    long getElapsedTime();
    long peekElapsedTime();
    void pauseTimer();
    void resumeTimer();

    void advanceTime();

    long peekTaskElapsedTime();
}

package edu.ucsd.cse110.habitizer.lib.domain;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TimerTest {
    private Timer timer;
    private MockTimer mockTimer;

    @Before
    public void setUp() {
        timer = new Timer();
        mockTimer = new MockTimer(10);
    }

    @Test
    public void testTimerStartsCorrectly() {
        timer.startTimer();
        assertTrue("Timer should start running", timer.getElapsedTime() >= 0);
    }

    @Test
    public void testElapsedTimeRoundingForTasks() {
        mockTimer.startTimer();
        for (int i = 0; i < 7; i++) {
            mockTimer.advanceTime();
        }

        long elapsedTime = mockTimer.getElapsedTime();
        assertEquals("Elapsed time should round up to 4m", 120, ((elapsedTime + 59) / 60) * 60);
    }

    @Test
    public void testPauseTimerStopsElapsedTime() {
        timer.startTimer();
        long initialElapsed = timer.getElapsedTime();

        timer.pauseTimer();
        long elapsedAfterPause = timer.getElapsedTime();

        assertEquals("Elapsed time should not change after pause", initialElapsed, elapsedAfterPause);
    }

    @Test
    public void testResumeTimerContinuesFromPausedTime() throws InterruptedException {
        timer.startTimer();
        timer.pauseTimer();
        long pausedElapsed = timer.getElapsedTime();

        timer.resumeTimer();
        Thread.sleep(1000);
        long elapsedAfterResume = timer.getElapsedTime();

        assertTrue("Elapsed time should increment after resume", elapsedAfterResume > pausedElapsed);
    }

    @Test
    public void testMultiplePauseResumeCycles() throws InterruptedException {
        timer.startTimer();
        Thread.sleep(2000);
        timer.pauseTimer();
        long firstPauseTime = timer.getElapsedTime();

        timer.resumeTimer();
        Thread.sleep(1000);
        timer.pauseTimer();
        long secondPauseTime = timer.getElapsedTime();
        assertTrue("Timer should accumulate time across pauses", secondPauseTime > firstPauseTime);
    }

    @Test
    public void testResumeWithoutPauseDoesNothing() throws InterruptedException {
        timer.startTimer();
        long initialElapsed = timer.getElapsedTime();

        timer.resumeTimer();
        Thread.sleep(1000);
        long elapsedAfterResume = timer.getElapsedTime();

        assertTrue("Elapsed time should increment normally", elapsedAfterResume > initialElapsed);
    }
}

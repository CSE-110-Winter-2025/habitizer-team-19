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
        assertEquals("Elapsed time should round up to 4m", 240, ((elapsedTime + 59) / 60) * 60);
    }

    @Test
    public void testSwitchToMockTimer() {
        mockTimer.startTimer();
        assertTrue("Mock timer should be running", mockTimer.isRunning());
        assertEquals("Mock timer should start with 10s elapsed", 10, mockTimer.getElapsedTime());
    }

    @Test
    public void testManualTimeAdvance() {
        mockTimer.startTimer();
        mockTimer.advanceTime();
        mockTimer.advanceTime();

        assertEquals("Mock timer should advance by 60s", 70, mockTimer.getElapsedTime());
    }

    @Test
    public void testMultipleAdvanceTimeCalls() {
        mockTimer.startTimer();
        for (int i = 0; i < 5; i++) {
            mockTimer.advanceTime();
        }
        assertEquals("Mock timer should advance by 150s", 160, mockTimer.getElapsedTime());
    }

    @Test
    public void testAdvanceTimeDoesNothingWhenStopped() {
        mockTimer.advanceTime();
        assertEquals("Mock timer should not advance when stopped", 10, mockTimer.getElapsedTime());
    }
}

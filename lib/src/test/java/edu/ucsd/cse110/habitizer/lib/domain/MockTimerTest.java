package edu.ucsd.cse110.habitizer.lib.domain;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MockTimerTest {
    private MockTimer timer;

    @Before
    public void setUp() {
        timer = new MockTimer(10);
    }

    @Test
    public void testStartTimer() {
        timer.startTimer();
        assertEquals("After start, peekElapsedTime should be 0", 0, timer.peekElapsedTime());
    }

    @Test
    public void testEndTimer() {
        timer.startTimer();
        timer.advanceTime();
        assertEquals("Before end, peekElapsedTime should be 15", 15, timer.peekElapsedTime());
        timer.endTimer();
        assertEquals("After endTimer, peekElapsedTime should be 0", 0, timer.peekElapsedTime());
    }

    @Test
    public void testGetElapsedTime_Running() {
        timer.startTimer();
        timer.advanceTime();
        long elapsed = timer.getElapsedTime();
        assertEquals("getElapsedTime should return 15", 15, elapsed);
        assertEquals("Subsequent getElapsedTime should return 0", 0, timer.getElapsedTime());
    }

    @Test
    public void testPeekElapsedTime_Running() {
        timer.startTimer();
        timer.advanceTime();
        long peek1 = timer.peekElapsedTime();
        assertEquals("peekElapsedTime should return 15", 15, peek1);
        long peek2 = timer.peekElapsedTime();
        assertEquals("peekElapsedTime should be consistent", peek1, peek2);
    }

    @Test
    public void testPauseTimer() {
        timer.startTimer();
        timer.advanceTime();
        timer.pauseTimer();
        assertEquals("After pause, peekElapsedTime should equal elapsed at pause", 15, timer.peekElapsedTime());
    }

    @Test
    public void testResumeTimer() {
        timer.startTimer();
        timer.advanceTime();
        timer.pauseTimer();
        assertEquals("Paused elapsed should be 15", 15, timer.peekElapsedTime());
        timer.resumeTimer();
        timer.advanceTime();
        assertEquals("After resume and advancing time, total elapsed should be 30", 30, timer.peekElapsedTime());
        long elapsedAfterResume = timer.getElapsedTime();
        assertEquals("After resume and advancing time, elapsed should be 15", 15, elapsedAfterResume);
    }

    @Test
    public void testAdvanceTime_Running() {
        timer.startTimer();
        long initialPeek = timer.peekElapsedTime();
        timer.advanceTime();
        long newPeek = timer.peekElapsedTime();
        assertEquals("advanceTime while running should increase elapsed time by 15",
                15, newPeek - initialPeek);
    }

    @Test
    public void testAdvanceTime_Paused() {
        timer.startTimer();
        timer.advanceTime();
        timer.pauseTimer();
        long elapsedAtPause = timer.peekElapsedTime();
        timer.advanceTime();
        long newElapsed = timer.peekElapsedTime();
        assertEquals("advanceTime while paused should add 15 seconds offset",
                elapsedAtPause + 15, newElapsed);
    }

    @Test
    public void testPeekTaskElapsedTime_Running() {
        timer.startTimer();
        timer.advanceTime();
        long taskElapsed = timer.peekTaskElapsedTime();
        assertEquals("peekTaskElapsedTime while running should return 15", 15, taskElapsed);
    }

    @Test
    public void testPeekTaskElapsedTime_Paused() {
        timer.startTimer();
        timer.advanceTime();
        timer.pauseTimer();
        long taskElapsed = timer.peekTaskElapsedTime();
        assertEquals("peekTaskElapsedTime while paused should return 15", 15, taskElapsed);
    }

    @Test
    public void testMockTimerPreservesElapsedTime() {
        timer.startTimer();
        assertEquals("After start, getElapsedTime should be 0", 0, timer.getElapsedTime());
    }

    @Test
    public void testMockTimerStartsCorrectly() {
        timer.startTimer();
        assertTrue("Timer should be running", timer.isRunning());
        assertEquals("Elapsed time should be 0 immediately after start", 0, timer.getElapsedTime());
    }

    @Test
    public void testMockTimerAdvanceTimeOnceCorrectly() {
        timer.startTimer();
        timer.advanceTime();
        assertFalse("Timer should be running before ending", !timer.isRunning());
        assertEquals("After one advance, getElapsedTime should be 15", 15, timer.getElapsedTime());
    }

    @Test
    public void testMockTimerEndsCorrectly() {
        timer.startTimer();
        timer.endTimer();
        assertFalse("Timer should not be running after end", timer.isRunning());
        assertEquals("After ending, getElapsedTime should be 0", 0, timer.getElapsedTime());
    }

    @Test
    public void testMockTimerAdvancesTimeTwiceCorrectly() {
        timer.startTimer();
        timer.advanceTime();
        timer.advanceTime();
        assertEquals("After advancing twice, getElapsedTime should be 30", 30, timer.getElapsedTime());
    }

    @Test
    public void testMockTimerAdvanceFailsWhenStopped() {
        timer.advanceTime();
        assertEquals("Advance time should have no effect if timer not started", 0, timer.peekElapsedTime());
    }

    @Test
    public void testMockTimerMultipleAdvances() {
        timer.startTimer();
        for (int i = 0; i < 5; i++) {
            timer.advanceTime();
        }
        assertEquals("After 5 advances, getElapsedTime should be 75", 75, timer.getElapsedTime());
    }

    @Test
    public void testPauseTimerStopsMockTimer() {
        timer.startTimer();
        timer.pauseTimer();
        assertFalse("Timer should be paused", timer.isRunning());
    }

    @Test
    public void testResumeTimerStartsMockTimer() {
        timer.startTimer();
        timer.pauseTimer();
        timer.resumeTimer();
        assertTrue("Timer should resume and be running", timer.isRunning());
    }

    @Test
    public void testElapsedTimeIsZeroWhenPaused() {
        timer.startTimer();
        timer.pauseTimer();
        assertEquals("Elapsed time should be 0 when paused immediately", 0, timer.getElapsedTime());
    }

    @Test
    public void testAdvanceTimeWhenPausedAddsOffset() {
        timer.startTimer();
        timer.pauseTimer();
        timer.advanceTime();
        assertEquals("Advance time while paused should add 15 seconds", 15, timer.getElapsedTime());
    }

}

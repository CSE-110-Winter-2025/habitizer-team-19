package edu.ucsd.cse110.habitizer.lib.domain;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MockTimerTest {
    private MockTimer mockTimer;

    @Before
    public void setUp() {
        mockTimer = new MockTimer(10);
    }

    @Test
    public void testMockTimerPreservesElapsedTime() {
        assertEquals(10, mockTimer.getElapsedTime());
    }

    @Test
    public void testMockTimerStartsCorrectly() {
        mockTimer.startTimer();
        assertTrue(mockTimer.isRunning());
        assertEquals(10, mockTimer.getElapsedTime());
    }

    @Test
    public void testMockTimerAdvanceTimeOnceCorrectly() {
        mockTimer.startTimer();
        mockTimer.advanceTime();
        mockTimer.endTimer();
        assertFalse(mockTimer.isRunning());
        assertEquals(40, mockTimer.getElapsedTime());
    }

    @Test
    public void testMockTimerEndsCorrectly() {
        mockTimer.startTimer();
        mockTimer.endTimer();
        assertFalse(mockTimer.isRunning());
        assertEquals(10, mockTimer.getElapsedTime());
    }

    @Test
    public void testMockTimerAdvancesTimeTwiceCorrectly() {
        mockTimer.startTimer();
        mockTimer.advanceTime();
        mockTimer.advanceTime();
        assertEquals(70, mockTimer.getElapsedTime());

    }

    @Test
    public void testMockTimerAdvanceFailsWhenStopped() {
        mockTimer.advanceTime();
        assertEquals(10, mockTimer.getElapsedTime());
    }

    @Test
    public void testMockTimerMultipleAdvances() {
        mockTimer.startTimer();
        for (int i = 0; i < 5; i++) {
            mockTimer.advanceTime();
        }
        assertEquals(160, mockTimer.getElapsedTime());
    }
}
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
    public void testMockTimerEndsCorrectly() {
        mockTimer.startTimer();
        mockTimer.endTimer();
        assertFalse(mockTimer.isRunning());
        assertEquals(10, mockTimer.getElapsedTime());
    }
}
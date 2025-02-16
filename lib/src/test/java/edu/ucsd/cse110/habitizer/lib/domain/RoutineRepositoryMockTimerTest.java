package edu.ucsd.cse110.habitizer.lib.domain;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class RoutineRepositoryMockTimerTest {
    private RoutineRepository routineRepository;

    @Before
    public void setUp() {
        routineRepository = RoutineRepository.rM;
        routineRepository.resetToRealTimer(); // Reset before every test
        routineRepository.switchToMockTimer();
    }

    @Test
    public void testManualTimeAdvanceBy30Seconds() {
        routineRepository.advanceTime();
        assertEquals(30, routineRepository.getElapsedTime());
    }

    @Test
    public void testManualTimeAdvanceMultipleTimes() {
        routineRepository.advanceTime();
        routineRepository.advanceTime();
        assertEquals(60, routineRepository.getElapsedTime());
    }

    @Test
    public void testRoutineElapsedTimeRounding() {
        routineRepository.advanceTime();
        routineRepository.advanceTime();
        routineRepository.advanceTime(); // Total: 90 sec (1m30s)

        long roundedTime = (routineRepository.getElapsedTime() + 59) / 60; // Round up
        assertEquals(2, roundedTime); // Should round to "2m"
    }

    @Test
    public void testAdvanceTimeFailsWhenTimerNotRunning() {
        routineRepository.end(); // Stop the timer
        routineRepository.advanceTime();
        assertEquals(0, routineRepository.getElapsedTime()); // Elapsed time should not change
    }
}

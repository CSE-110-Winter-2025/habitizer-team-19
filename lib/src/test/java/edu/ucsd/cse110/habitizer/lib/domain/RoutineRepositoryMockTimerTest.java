package edu.ucsd.cse110.habitizer.lib.domain;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class RoutineRepositoryMockTimerTest {
//    private RoutineRepository routineRepository;

    @Before
    public void setUp() {
//        routineRepository = RoutineRepository.rM;
//        routineRepository.resetToRealTimer();
//        routineRepository.switchToMockTimer();
    }

    @Test
    public void testManualTimeAdvanceBy30Seconds() {
//        routineRepository.advanceTime();
//        assertEquals(30, routineRepository.getElapsedTime());
    }

    @Test
    public void testManualTimeAdvanceMultipleTimes() {
//        routineRepository.advanceTime();
//        routineRepository.advanceTime();
//        assertEquals(60, routineRepository.getElapsedTime());
    }

    @Test
    public void testRoutineElapsedTimeRounding() {
//        routineRepository.advanceTime();
//        routineRepository.advanceTime();
//        routineRepository.advanceTime();
//
//        long roundedTime = (routineRepository.getElapsedTime() + 59) / 60;
//        assertEquals(2, roundedTime);
    }

    @Test
    public void testAdvanceTimeFailsWhenTimerNotRunning() {
//        routineRepository.endRoutine();
//        routineRepository.advanceTime();
//        assertEquals(0, routineRepository.getElapsedTime());
    }

    @Test
    public void testResetTimerElapsedTime() {
//        routineRepository.advanceTime();
//        routineRepository.getElapsedTime();
//        assertEquals(0, routineRepository.getElapsedTime());
    }
}

package edu.ucsd.cse110.habitizer.lib.domain;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class AsynchronousRoutineTimerTest {
    private MockTimer timer;

    // format seconds into "HH:MM:SS"
    private String getElapsedTimeString(long time) {
        long hours = time / 3600;
        long minutes = (time % 3600) / 60;
        long seconds = time % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    // Helper method (flooring elapsed time to full minutes)
    private long getRoutineDisplayTime(long elapsedTime) {
        return (elapsedTime / 60) * 60;
    }

    @Before
    public void setUp() {
        timer = new MockTimer(0);
    }

    @Test
    public void testBDDScenario_ManualAdvanceWhilePaused() {
        timer.startTimer();
        timer.pauseTimer();

        long elapsedAtPause = timer.peekElapsedTime();

        assertEquals("Elapsed time at pause should be 0", 0, elapsedAtPause);

        timer.advanceTime();
        timer.advanceTime();
        long elapsedAfterAdvances = timer.peekElapsedTime();
        assertEquals("After advancing 30 seconds, elapsed time should be 30", 30, elapsedAfterAdvances);

        long routineDisplayTime = getRoutineDisplayTime(elapsedAfterAdvances);
        assertEquals("After advancing 30 seconds, display should still be 0 minutes", 0, routineDisplayTime);
        assertEquals("Display string should be 00:00:00", "00:00:00", getElapsedTimeString(routineDisplayTime));

        timer.advanceTime();
        timer.advanceTime();
        assertEquals("Before end, peekElapsedTime should be 60", 60, timer.peekElapsedTime());
        elapsedAfterAdvances = timer.peekElapsedTime();
        routineDisplayTime = getRoutineDisplayTime(elapsedAfterAdvances);

        assertEquals("After advancing a total of 60 seconds, display should be 1 minute", 60, routineDisplayTime);
        assertEquals("Display string should be 00:01:00", "00:01:00", getElapsedTimeString(routineDisplayTime));
    }
}

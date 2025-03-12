package edu.ucsd.cse110.habitizer.lib.domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class AsynchronousTaskTimerTest {
    private MockTimer timer;
    private Routine routine;
    private Task task1, task2;

    @Before
    public void setUp() {
        timer = new MockTimer(0);

        task1 = new Task("Brush Teeth");
        task2 = new Task("Shower");

        routine = new Routine("Morning Routine", 3600, new ArrayList<>(List.of(task1, task2)));
    }

    // Helper Function (copied directly from MainViewModel class)
    public static long taskDisplay(long elapsedTime){
        if(elapsedTime<60) {
            return ((elapsedTime-1)/5)*5+5;
        }
        else{
            return ((elapsedTime + 59) / 60) * 60;
        }
    }


    @Test
    public void testRoutineCompletionWithRoundedElapsedTime() {
        timer.startTimer();
        assertTrue("Timer should be running", timer.isRunning());

        timer.advanceTime();
        timer.advanceTime();
       /* long testtasktime = timer.peekElapsedTime() +2;
        assertEquals(32,testtasktime);
        assertEquals("Rounded elapsed time should be up to nearest 5 seconds", 35, taskDisplay(testtasktime));*/
        timer.advanceTime();
        timer.advanceTime();
        timer.advanceTime();

        long elapsedTime = timer.peekElapsedTime();
        assertEquals("Elapsed time should be 75s before rounding", 75, elapsedTime);

        long roundedElapsedTime = taskDisplay(elapsedTime);
        assertEquals("Rounded elapsed time should be 120 seconds (next minute)", 120, roundedElapsedTime);

        task1.setElapsedTime(roundedElapsedTime);
        task1.setCompletionStatus(1);

        assertEquals("Task completion status should be 1 (Completed)", Integer.valueOf(1), task1.getCompletionStatus());
        assertEquals("Task elapsed time should be rounded to 120s", 120, task1.getElapsedTime());
    }

    @Test
    public void testTaskTimeRelativeToPrevious(){

    }

    //5 second rounding
    @Test
    public void testTaskTimeRoundUp(){
        timer.startTimer();
        assertTrue("Timer should be running", timer.isRunning());

        timer.advanceTime();
        timer.advanceTime();
        long testtasktime = timer.peekElapsedTime() +2;
        assertEquals(32,testtasktime);
        assertEquals("Rounded elapsed time should be up to nearest 5 seconds", 35, taskDisplay(testtasktime));
        timer.advanceTime();
        timer.advanceTime();
        timer.advanceTime();

        long elapsedTime = timer.peekElapsedTime();
        assertEquals("Elapsed time should be 75s before rounding", 75, elapsedTime);

        long roundedElapsedTime = taskDisplay(elapsedTime);
        assertEquals("Rounded elapsed time should be 120 seconds (next minute)", 120, roundedElapsedTime);
    }
}

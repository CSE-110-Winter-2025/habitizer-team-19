package edu.ucsd.cse110.habitizer.lib.domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TaskTest {
    private Task task;

    @Before
    public void setUp() {
        task = new Task("Initial Task");
        task.setElapsedTime(1000); // Hardcoded elapsed time
    }

    @Test
    public void testInitialState() {
        assertEquals("Initial Task", task.getName());
        assertEquals(0, task.getCompletionStatus());
        assertEquals(1000, task.getElapsedTime());
    }

    @Test
    public void testComplete() {
        task.complete();
        assertEquals(1, task.getCompletionStatus());
        assertEquals(1000, task.getElapsedTime());
    }

    @Test
    public void testSkip() {
        task.skip();
        assertEquals(2, task.getCompletionStatus());
        assertEquals(1000, task.getElapsedTime());
    }

    @Test
    public void testReset() {
        task.complete();
        task.reset();
        assertEquals(0, task.getCompletionStatus());
        assertEquals(-1, task.getElapsedTime());
    }

    @Test
    public void testNewName() {
        task.newName("Updated Task");
        assertEquals("Updated Task", task.getName());
    }

    @Test
    public void testCompleteAfterSkip() {
        task.skip();
        assertEquals(2, task.getCompletionStatus());
        assertEquals(1000, task.getElapsedTime());

        task.complete();
        assertEquals(1, task.getCompletionStatus());
        assertEquals(1000, task.getElapsedTime());
    }

    @Test
    public void testSkipAfterComplete() {
        task.complete();
        assertEquals(1, task.getCompletionStatus());
        assertEquals(1000, task.getElapsedTime());

        task.skip();
        assertEquals(2, task.getCompletionStatus());
        assertEquals(1000, task.getElapsedTime());
    }
}

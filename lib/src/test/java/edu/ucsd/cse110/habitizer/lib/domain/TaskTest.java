package edu.ucsd.cse110.habitizer.lib.domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TaskTest {
    private Task task;

    @Before
    public void setUp() throws Exception {
        //Initialize a Task object before each test
        task = new Task("Initial Task");
    }

    @Test
    public void testInitialState() {
        //verify the initial state of the Task object
        assertEquals("Initial Task", task.getName());
        assertEquals(0, task.getCompletionStatus());
        assertEquals(-1, task.getElapsedTime());
    }

    @Test
    public void testComplete() {
        task.complete(); //1000 seconds
        assertEquals(1, task.getCompletionStatus());
        assertEquals(1000, task.getElapsedTime());
    }

    @Test
    public void testSkip() {
        task.skip();
        assertEquals(2, task.getCompletionStatus());
        assertEquals(-1, task.getElapsedTime());
    }

    @Test
    public void testReset() {
        //mark completed
        task.complete();
        assertEquals(1, task.getCompletionStatus());
        assertEquals(1000, task.getElapsedTime());

        //reset after previously completed
        task.reset();
        assertEquals(0, task.getCompletionStatus());
        assertEquals(0, task.getElapsedTime());

        //mark skipped
        task.skip();
        assertEquals(2, task.getCompletionStatus());

        //reset after previously skipped
        task.reset();
        assertEquals(0, task.getCompletionStatus());
        assertEquals(0, task.getElapsedTime());
    }

    @Test
    public void newName() {
        task.newName("Updated Task");
        assertEquals("Updated Task", task.getName());
    }

    @Test
    public void testCompleteAfterSkip() {
        task.skip();
        assertEquals(2, task.getCompletionStatus());
        assertEquals(-1, task.getElapsedTime());

        task.complete();
        assertEquals(1, task.getCompletionStatus());
        assertEquals(500, task.getElapsedTime());
    }

    @Test
    public void testSkipAfterComplete(){
        task.complete();
        assertEquals(1, task.getCompletionStatus());
        assertEquals(500, task.getElapsedTime());

        task.skip();
        assertEquals(2, task.getCompletionStatus());
        assertEquals(500, task.getElapsedTime()); //previous completed time stays, do we want this? or reset back to -1?
    }
}
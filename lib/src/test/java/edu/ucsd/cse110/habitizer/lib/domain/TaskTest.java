package edu.ucsd.cse110.habitizer.lib.domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TaskTest {

    //Unit Tests

    @Test
    public void testWithId(){
        Task task = new Task("Task 1");
        task.withId(0);

        var actual = task.id();
        var expected = (Integer) 0;

        assertEquals(expected,actual);
    }

    @Test
    public void testReset(){
        Task task = new Task(0,"Task");
        task.setCompletionStatus(1);
        task.reset();

        var actual = task.getCompletionStatus();
        var expected = (Integer) 0;

        assertEquals(expected,actual);
    }

    // Integration Tests

}

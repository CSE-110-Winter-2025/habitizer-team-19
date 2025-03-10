package edu.ucsd.cse110.habitizer.lib.domain;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RoutineTest{

    //Unit Tests

    @Test
    public void testAddTask(){
        ArrayList<Task> tasks = new ArrayList<>(){
            {
                add(new Task("Brush Teeth"));
                add(new Task("Shower"));
            }
        };

        Routine routine = new Routine(0,"Morning", 60*60, tasks);
        Task task = new Task("Eat Breakfast");
        routine.addTask(task);
        var actual = routine.getTasks();

        tasks.add(task);
        var expected = tasks;

        assertEquals(expected,actual);
    }

    @Test
    public void testAddAllTask(){
        ArrayList<Task> tasks1 = new ArrayList<>(){
            {
                add(new Task(0,"Brush Teeth"));
                add(new Task(1,"Shower"));
            }
        };
        ArrayList<Task> tasks2 = new ArrayList<>(){
            {
                add(new Task(2,"Task 1"));
                add(new Task(3,"Task 2"));
            }
        };
        Routine routine = new Routine(0,"Morning", 60*60, tasks1);

        routine.addAllTask(tasks2);
        tasks1.addAll(tasks2);

        var actual = routine.getTasks();
        var expected = tasks1;

        assertEquals(expected,actual);

    }

    @Test
    public void testRemoveTask(){
        String taskName = "Shower";
        Task task = new Task(1,taskName);
        ArrayList<Task> tasks = new ArrayList<>(){
            {
                add(new Task(0,"Brush Teeth"));
                add(task);
            }
        };
        Routine routine = new Routine(0,"Morning", 60*60, tasks);
        routine.removeTask(1);
        var actual = routine.getTasks();

        tasks.remove(task);
        assertEquals(tasks,actual);
    }

    @Test
    public void testWithId(){
        Routine routine = new Routine("Morning", 60*60);
        routine = routine.withId(0);

        var actual = routine.id();
        var expected = (Integer) 0;

        assertEquals(expected,actual);
    }

    @Test
    public void testRest(){
        ArrayList<Task> tasks = new ArrayList<>(){
            {
                add(new Task(0,"Brush Teeth"));
                add(new Task(1,"Shower"));
            }
        };
        tasks.get(0).setCompletionStatus(1);
        tasks.get(1).setCompletionStatus(2);

        Routine routine = new Routine(0,"Morning", 60*60, tasks);
        routine.reset();
        var actual = routine.getTasks();

        tasks.get(0).reset();
        tasks.get(1).reset();

        assertEquals(tasks,actual);
    }

    @Test
    public void testSwapElement(){
        ArrayList<Task> tasks = new ArrayList<>(){
            {
                add(new Task(0,"Brush Teeth"));
                add(new Task(1,"Shower"));
            }
        };
        Routine routine = new Routine(0,"Morning", 60*60, tasks);

        routine.swapElement(0,1);
        Collections.swap(tasks,0,1);

        var actual = routine.getTasks();

        assertEquals(tasks,actual);
    }

    @Test
    public void testRenameRoutine() {
        Routine routine = new Routine(0,"Morning", 60*60, new ArrayList<>());
        String originalName = routine.getName();

        String newName = "Friday Evening";
        routine.setName(newName);
        assertNotEquals("Different than original", originalName, routine.getName());
        assertEquals("New name updated correctly", newName, routine.getName());
    }

    // Integration Tests

}
package edu.ucsd.cse110.habitizer.lib.domain;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class RoutineTest{

    @Test
    public void testAddTask() {
        ArrayList<Task> tasks = new ArrayList<>(){
            {
                add(new Task("Brush Teeth"));
                add(new Task("Shower"));
            }
        };

        Routine routine = new Routine("Morning", 60*60, tasks);
        Task task = new Task("Eat Breakfast");
        routine.addTask(task);
        var actual = routine.getTasks();

        tasks.add(task);
        var expected = tasks;

        assertEquals(expected,actual);
    }

    @Test
    public void testRemoveTask() {

        String taskName = "Shower";
        Task task = new Task(taskName);
        ArrayList<Task> tasks = new ArrayList<>(){
            {
                add(new Task("Brush Teeth"));
                add(task);
            }
        };
        Routine routine = new Routine("Morning", 60*60, tasks);
        routine.removeTask(taskName);
        var actual = routine.getTasks();

        tasks.remove(task);
        assertEquals(tasks,actual);
    }

    @Test
    public void testGetName() {
        String routineName = "Morning";
        Routine routine = new Routine("Morning", 60*60, new ArrayList<Task>(List.of(
                new Task("Brush Teeth"),
                new Task("Shower")
        )));

        var actual = routine.getName();

        assertEquals(routineName,actual);
    }

    @Test
    public void testGetGoalTime() {
        long goalTime = 60*60;
        Routine routine = new Routine("Morning", 60*60, new ArrayList<Task>(List.of(
                new Task("Brush Teeth"),
                new Task("Shower")
        )));

        var actual = routine.getGoalTime();

        assertEquals(goalTime,actual);
    }

    @Test
    public void testNewGoalTime() {
        Routine routine = new Routine("Morning", 60*60, new ArrayList<Task>(List.of(
                new Task("Brush Teeth"),
                new Task("Shower")
        )));
        long goalTime = 60;
        routine.newGoalTime(60);
        var actual = routine.getGoalTime();

        assertEquals(goalTime,actual);
    }

    @Test
    public void testNewName() {
        Routine routine = new Routine("Morning", 60*60, new ArrayList<Task>(List.of(
                new Task("Brush Teeth"),
                new Task("Shower")
        )));
        String name = "Night";
        routine.newName("Night");
        var actual = routine.getName();

        assertEquals(name,actual);
    }

    @Test
    public void testReset() {
        ArrayList<Task> tasks = new ArrayList<>(){
            {
                add(new Task("Brush Teeth"));
                add(new Task("Shower"));
            }
        };
        tasks.get(0).complete();
        tasks.get(1).skip();

        Routine routine = new Routine("Morning", 60*60, tasks);
        routine.reset();
        var actual = routine.getTasks();

        tasks.get(0).reset();
        tasks.get(1).reset();

        assertEquals(tasks,actual);

    }

    @Test
    public void testGetTasks() {
        ArrayList<Task> tasks = new ArrayList<>(){
            {
                add(new Task("Brush Teeth"));
                add(new Task("Shower"));
            }
        };

        Routine routine = new Routine("Morning", 60*60, tasks);
        var actual = routine.getTasks();

        assertEquals(tasks,actual);
    }
}
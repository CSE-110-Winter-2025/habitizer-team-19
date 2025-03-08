package edu.ucsd.cse110.habitizer.lib.domain;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;

public class RoutineRepositoryTest {

    //Unit Tests

    @Test
    public void testGetRoutineCount(){
        var data = new InMemoryDataSource();
        List<Routine> routines = List.of(
                new Routine(0,"Morning", 60*60, new ArrayList<Task>(List.of(
                        new Task(0,"Brush Teeth"),
                        new Task(1,"Shower")
                ))),
                new Routine(1,"Evening", 60*60*3, new ArrayList<Task>(List.of(
                        new Task(2,"Homework"),
                        new Task(3,"Dinner")
                ))));
        data.putRoutines(routines);
        RoutineRepository rM = new RoutineRepository(data);

        var actual = rM.getRoutineCount();
        var expected = (int) 2;

        assertEquals(expected,actual);
    }

    @Test
    public void testFindRoutine(){
        var data = new InMemoryDataSource();
        List<Routine> routines = List.of(
                new Routine(0,"Morning", 60*60, new ArrayList<Task>(List.of(
                        new Task(0,"Brush Teeth"),
                        new Task(1,"Shower")
                ))),
                new Routine(1,"Evening", 60*60*3, new ArrayList<Task>(List.of(
                        new Task(2,"Homework"),
                        new Task(3,"Dinner")
                ))));
        data.putRoutines(routines);
        RoutineRepository rM = new RoutineRepository(data);

        var actual = rM.findRoutine(0);

        var expected = data.getRoutineSubject(0);

        assertEquals(expected,actual);
    }

    @Test
    public void testFindAllRoutines(){
        var data = new InMemoryDataSource();
        List<Routine> routines = List.of(
                new Routine(0,"Morning", 60*60, new ArrayList<Task>(List.of(
                        new Task(0,"Brush Teeth"),
                        new Task(1,"Shower")
                ))),
                new Routine(1,"Evening", 60*60*3, new ArrayList<Task>(List.of(
                        new Task(2,"Homework"),
                        new Task(3,"Dinner")
                ))));
        data.putRoutines(routines);
        RoutineRepository rM = new RoutineRepository(data);

        var actual = rM.findAllRoutines();

        var expected = data.getAllRoutinesSubject();

        assertEquals(expected,actual);
    }

    @Test
    public void testSaveRoutine() {
        var data = new InMemoryDataSource();
        List<Routine> routines = List.of(
                new Routine(0, "Morning", 60 * 60, new ArrayList<Task>(List.of(
                        new Task(0, "Brush Teeth"),
                        new Task(1, "Shower")
                ))),
                new Routine(1, "Evening", 60 * 60 * 3, new ArrayList<Task>(List.of(
                        new Task(2, "Homework"),
                        new Task(3,"Dinner")
                ))));
        data.putRoutines(routines);
        RoutineRepository rM = new RoutineRepository(data);

        Routine routine = new Routine(2,"Lunch", 60*60, new ArrayList<Task>(List.of(
                new Task(4,"Eat Sandwich")
        )));

        rM.saveRoutine(routine);
        var actual = rM.findAllRoutines();

        data.putRoutine(routine);
        var expected = data.getAllRoutinesSubject();

        assertEquals(expected,actual);
    }

    @Test
    public void testSaveTask(){
        var data = new InMemoryDataSource();
        List<Routine> routines = List.of(
                new Routine(0, "Morning", 60 * 60, new ArrayList<Task>(List.of(
                        new Task(0, "Brush Teeth"),
                        new Task(1, "Shower")
                ))),
                new Routine(1, "Evening", 60 * 60 * 3, new ArrayList<Task>(List.of(
                        new Task(2, "Homework"),
                        new Task(3,"Dinner")
                ))));
        data.putRoutines(routines);
        RoutineRepository rM = new RoutineRepository(data);

        Task task = new Task(4,"Task");
        rM.saveTask(1,task);

        var actual = rM.findAllRoutines();

        data.putTask(1,task);
        var expected = data.getAllRoutinesSubject();

        assertEquals(expected,actual);
    }

    @Test
    public void testRemoveRoutine(){
        var data = new InMemoryDataSource();
        List<Routine> routines = List.of(
                new Routine(0, "Morning", 60 * 60, new ArrayList<Task>(List.of(
                        new Task(0, "Brush Teeth"),
                        new Task(1, "Shower")
                ))),
                new Routine(1, "Evening", 60 * 60 * 3, new ArrayList<Task>(List.of(
                        new Task(2, "Homework"),
                        new Task(3,"Dinner")
                ))));
        data.putRoutines(routines);
        RoutineRepository rM = new RoutineRepository(data);

        rM.removeRoutine(0);
        data.removeRoutine(0);

        var actual = rM.findAllRoutines();
        var expected = data.getAllRoutinesSubject();

        assertEquals(expected,actual);
    }

    @Test
    public void testRemoveTask(){
        var data = new InMemoryDataSource();
        List<Routine> routines = List.of(
                new Routine(0, "Morning", 60 * 60, new ArrayList<Task>(List.of(
                        new Task(0, "Brush Teeth"),
                        new Task(1, "Shower")
                ))),
                new Routine(1, "Evening", 60 * 60 * 3, new ArrayList<Task>(List.of(
                        new Task(2, "Homework"),
                        new Task(3,"Dinner")
                ))));
        data.putRoutines(routines);
        RoutineRepository rM = new RoutineRepository(data);

        rM.removeTask(0,1);
        data.removeTask(0,1);

        var actual = rM.findAllRoutines();
        var expected = data.getAllRoutinesSubject();

        assertEquals(expected,actual);
    }

    @Test
    public void moveTaskUp(){
        var data = new InMemoryDataSource();
        List<Routine> routines = List.of(
                new Routine(0, "Morning", 60 * 60, new ArrayList<Task>(List.of(
                        new Task(0, "Brush Teeth"),
                        new Task(1, "Shower")
                ))),
                new Routine(1, "Evening", 60 * 60 * 3, new ArrayList<Task>(List.of(
                        new Task(2, "Homework"),
                        new Task(3,"Dinner")
                ))));
        data.putRoutines(routines);
        RoutineRepository rM = new RoutineRepository(data);

        rM.moveTaskUp(1,3);
        data.moveTaskUp(1,3);

        var actual = rM.findAllRoutines();
        var expected = data.getAllRoutinesSubject();

        assertEquals(expected,actual);
    }

    @Test
    public void moveTaskDown(){
        var data = new InMemoryDataSource();
        List<Routine> routines = List.of(
                new Routine(0, "Morning", 60 * 60, new ArrayList<Task>(List.of(
                        new Task(0, "Brush Teeth"),
                        new Task(1, "Shower")
                ))),
                new Routine(1, "Evening", 60 * 60 * 3, new ArrayList<Task>(List.of(
                        new Task(2, "Homework"),
                        new Task(3,"Dinner")
                ))));
        data.putRoutines(routines);
        RoutineRepository rM = new RoutineRepository(data);

        rM.moveTaskUp(1,2);
        data.moveTaskUp(1,2);

        var actual = rM.findAllRoutines();
        var expected = data.getAllRoutinesSubject();

        assertEquals(expected,actual);
    }

    // Integration Tests
}
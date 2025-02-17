package edu.ucsd.cse110.habitizer.lib.domain;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.habitizer.lib.util.Subject;

public class RoutineRepositoryTest {

    private RoutineRepository setRoutineRepository(){
        var data = new InMemoryDataSource();
        List<Routine> routines = List.of(
                new Routine("Morning", 60*60, new ArrayList<Task>(List.of(
                        new Task("Brush Teeth"),
                        new Task("Shower")
                ))),
                new Routine("Evening", 60*60*3, new ArrayList<Task>(List.of(
                        new Task("Homework"),
                        new Task("Dinner")
                ))));
        for (Routine routine : routines) {
            data.putRoutine(routine);
        }

        return new RoutineRepository(data);
    }


    @Test
    public void testCount() {
        var data = new InMemoryDataSource();
        List<Routine> routines = List.of(
                new Routine("Morning", 60*60, new ArrayList<Task>(List.of(
                        new Task("Brush Teeth"),
                        new Task("Shower")
                ))),
                new Routine("Evening", 60*60*3, new ArrayList<Task>(List.of(
                        new Task("Homework"),
                        new Task("Dinner")
                ))));
        for (Routine routine : routines) {
            data.putRoutine(routine);
        }
        RoutineRepository rM = new RoutineRepository(data);

        var actual = rM.count();
        var expected = (Integer) 2;

        assertEquals(expected,actual);
    }

    @Test
    public void testFind() {
        var data = new InMemoryDataSource();
        List<Routine> routines = List.of(
                new Routine("Morning", 60*60, new ArrayList<Task>(List.of(
                        new Task("Brush Teeth"),
                        new Task("Shower")
                ))),
                new Routine("Evening", 60*60*3, new ArrayList<Task>(List.of(
                        new Task("Homework"),
                        new Task("Dinner")
                ))));
        for (Routine routine : routines) {
            data.putRoutine(routine);
        }
        RoutineRepository rM = new RoutineRepository(data);

        var actual = rM.find("Morning");

        var expected = data.getRoutineSubject("Morning");

        assertEquals(expected,actual);
    }

    @Test
    public void testFindAll() {
        var data = new InMemoryDataSource();
        List<Routine> routines = List.of(
                new Routine("Morning", 60*60, new ArrayList<Task>(List.of(
                        new Task("Brush Teeth"),
                        new Task("Shower")
                ))),
                new Routine("Evening", 60*60*3, new ArrayList<Task>(List.of(
                        new Task("Homework"),
                        new Task("Dinner")
                ))));
        for (Routine routine : routines) {
            data.putRoutine(routine);
        }
        RoutineRepository rM = new RoutineRepository(data);

        var actual = rM.findAll();

        var expected = data.getAllRoutinesSubject();

        assertEquals(expected,actual);
    }

    @Test
    public void testSave() {
        var data = new InMemoryDataSource();
        List<Routine> routines = List.of(
                new Routine("Morning", 60*60, new ArrayList<Task>(List.of(
                        new Task("Brush Teeth"),
                        new Task("Shower")
                ))),
                new Routine("Evening", 60*60*3, new ArrayList<Task>(List.of(
                        new Task("Homework"),
                        new Task("Dinner")
                ))));
        for (Routine routine : routines) {
            data.putRoutine(routine);
        }
        RoutineRepository rM = new RoutineRepository(data);

        Routine routine = new Routine("Lunch", 60*60, new ArrayList<Task>(List.of(
                new Task("Eat Sandwich")
        )));

        rM.save(routine);
        var actual = rM.findAll();

        data.putRoutine(routine);
        var expected = data.getAllRoutinesSubject();

        assertEquals(expected,actual);

    }
}
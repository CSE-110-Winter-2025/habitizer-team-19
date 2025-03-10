package edu.ucsd.cse110.habitizer.lib.data;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.util.Subject;

public class InMemoryDataSourceTest {
    private InMemoryDataSource dataSource;

    @Before
    public void setUp() {
        dataSource = new InMemoryDataSource();
    }

    @Test
    public void testPutRoutine() {
        Routine routine = new Routine("Morning", 3600, new ArrayList<>());
        dataSource.putRoutine(routine);

        var retrievedRoutine = dataSource.getRoutine(0);
        assertNotNull("Routine should be added", retrievedRoutine);
        assertEquals("Routine name should match", "Morning", retrievedRoutine.getName());
    }

    @Test
    public void testPutRoutines() {
        List<Routine> routines = List.of(
                new Routine("Morning", 3600, new ArrayList<>()),
                new Routine("Evening", 7200, new ArrayList<>())
        );

        dataSource.putRoutines(routines);

        assertEquals("Routines should be stored", 2, dataSource.getRoutines().size());
        assertEquals("First routine name should match", "Morning", dataSource.getRoutine(0).getName());
        assertEquals("Second routine name should match", "Evening", dataSource.getRoutine(1).getName());
    }

    @Test
    public void testPutTask() {
        Routine routine = new Routine("Morning", 3600, new ArrayList<>());
        dataSource.putRoutine(routine);

        Task task = new Task("Brush Teeth");
        dataSource.putTask(0, task);

        var retrievedRoutine = dataSource.getRoutine(0);
        assertEquals("Task should be added to routine", 1, retrievedRoutine.getTasks().size());
        assertEquals("Task name should match", "Brush Teeth", retrievedRoutine.getTasks().get(0).getName());
    }

    @Test
    public void testPutTasks() {
        Routine routine = new Routine("Morning", 3600, new ArrayList<>());
        dataSource.putRoutine(routine);

        List<Task> tasks = List.of(new Task("Brush Teeth"), new Task("Shower"));
        dataSource.putTasks(0, tasks);

        var retrievedRoutine = dataSource.getRoutine(0);
        assertEquals("Tasks should be added to routine", 2, retrievedRoutine.getTasks().size());
        assertEquals("First task should match", "Brush Teeth", retrievedRoutine.getTasks().get(0).getName());
        assertEquals("Second task should match", "Shower", retrievedRoutine.getTasks().get(1).getName());
    }

    @Test
    public void testRemoveRoutine() {
        Routine routine = new Routine("Morning", 3600, new ArrayList<>());
        dataSource.putRoutine(routine);

        assertEquals("Should have 1 routine before removal", 1, dataSource.getRoutines().size());

        dataSource.removeRoutine(0);

        assertEquals("Routine should be removed", 0, dataSource.getRoutines().size());
    }

    @Test
    public void testRemoveTask() {
        Routine routine = new Routine("Morning", 3600, new ArrayList<>());
        dataSource.putRoutine(routine);

        Task task = new Task("Brush Teeth");
        dataSource.putTask(0, task);

        assertEquals("Routine should have 1 task before removal", 1, dataSource.getRoutine(0).getTasks().size());

        dataSource.removeTask(0, 0);

        assertEquals("Task should be removed from routine", 0, dataSource.getRoutine(0).getTasks().size());
    }

    @Test
    public void testMoveTaskUp() {
        Routine routine = new Routine("Morning", 3600, new ArrayList<>());
        dataSource.putRoutine(routine);

        Task task1 = new Task(0, "Brush Teeth");
        Task task2 = new Task(1, "Shower");

        dataSource.putTask(0, task1);
        dataSource.putTask(0, task2);

        dataSource.moveTaskUp(0, 1);

        List<Task> tasks = dataSource.getRoutine(0).getTasks();
        assertEquals("Shower should be first after moving up", "Shower", tasks.get(0).getName());
        assertEquals("Brush Teeth should be second", "Brush Teeth", tasks.get(1).getName());
    }

    @Test
    public void testMoveTaskDown() {
        Routine routine = new Routine("Morning", 3600, new ArrayList<>());
        dataSource.putRoutine(routine);

        Task task1 = new Task(0, "Brush Teeth");
        Task task2 = new Task(1, "Shower");

        dataSource.putTask(0, task1);
        dataSource.putTask(0, task2);

        dataSource.moveTaskDown(0, 0);

        List<Task> tasks = dataSource.getRoutine(0).getTasks();
        assertEquals("Shower should be first after moving down", "Shower", tasks.get(0).getName());
        assertEquals("Brush Teeth should be second", "Brush Teeth", tasks.get(1).getName());
    }

    @Test
    public void testSwapTask() {
        Routine routine = new Routine("Morning", 3600, new ArrayList<>());
        dataSource.putRoutine(routine);

        Task task1 = new Task(0, "Brush Teeth");
        Task task2 = new Task(1, "Shower");

        dataSource.putTask(0, task1);
        dataSource.putTask(0, task2);

        dataSource.swapTask(0, 0, 1);

        List<Task> tasks = dataSource.getRoutine(0).getTasks();
        assertEquals("Shower should be first after swapping", "Shower", tasks.get(0).getName());
        assertEquals("Brush Teeth should be second", "Brush Teeth", tasks.get(1).getName());
    }

    @Test
    public void testGetAllRoutinesSubject() {
        dataSource.putRoutine(new Routine("Morning", 3600, new ArrayList<>()));
        dataSource.putRoutine(new Routine("Evening", 7200, new ArrayList<>()));

        Subject<List<Routine>> subject = dataSource.getAllRoutinesSubject();

        assertNotNull("All routines subject should not be null", subject);
        assertEquals("Should contain 2 routines", 2, subject.getValue().size());
    }

    @Test
    public void testGetRoutineSubject() {
        Routine routine = new Routine("Morning", 3600, new ArrayList<>());
        dataSource.putRoutine(routine);

        Subject<Routine> subject = dataSource.getRoutineSubject(0);

        assertNotNull("Routine subject should not be null", subject);
        assertEquals("Routine name should match", "Morning", subject.getValue().getName());
    }
}
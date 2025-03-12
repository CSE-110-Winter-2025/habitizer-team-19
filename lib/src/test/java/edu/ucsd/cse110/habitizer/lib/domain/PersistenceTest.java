package edu.ucsd.cse110.habitizer.lib.domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;

public class PersistenceTest {
    private InMemoryDataSource data;
    private RoutineRepository routineRepository;
    private Routine morningRoutine, eveningRoutine;
    private Task task1, task2, task3;

    @Before
    public void setUp() {
        data = new InMemoryDataSource();
        routineRepository = new RoutineRepository(data);

        task1 = new Task(0, "Brush Teeth");
        task2 = new Task(1, "Shower");
        task3 = new Task(2, "Make Coffee");

        List<Task> morningTasks = new ArrayList<>(List.of(task1, task2));
        morningRoutine = new Routine(0, "Morning Routine", 3600, morningTasks);

        List<Task> eveningTasks = new ArrayList<>(List.of(task3));
        eveningRoutine = new Routine(1, "Evening Routine", 7200, eveningTasks);

        routineRepository.saveRoutine(morningRoutine);
        routineRepository.saveRoutine(eveningRoutine);
    }

    @Test
    public void testRoutinePersistenceAfterRestart() {
        InMemoryDataSource newData = InMemoryDataSource.fromList(data.getRoutines());
        RoutineRepository newRoutineRepository = new RoutineRepository(newData);

        List<Routine> restoredRoutines = newRoutineRepository.findAllRoutines().getValue();

        assertNotNull("Routines should be restored", restoredRoutines);
        assertEquals("Routine count should remain the same", 2, restoredRoutines.size());
        assertEquals("Morning Routine should persist", "Morning Routine", restoredRoutines.get(0).getName());
        assertEquals("Evening Routine should persist", "Evening Routine", restoredRoutines.get(1).getName());
    }

    @Test
    public void testTaskPersistenceAfterRestart() {
        InMemoryDataSource newData = InMemoryDataSource.fromList(data.getRoutines());
        RoutineRepository newRoutineRepository = new RoutineRepository(newData);

        Routine restoredRoutine = newRoutineRepository.findRoutine(morningRoutine.id()).getValue();

        assertNotNull("Routine should persist", restoredRoutine);
        assertEquals("Routine should retain tasks", 2, restoredRoutine.getTasks().size());
        assertEquals("Task should persist", "Brush Teeth", restoredRoutine.getTasks().get(0).getName());
    }

    @Test
    public void testTaskDeletionPersistence() {
        routineRepository.removeTask(morningRoutine.id(), task1.id());

        InMemoryDataSource newData = InMemoryDataSource.fromList(data.getRoutines());
        RoutineRepository newRoutineRepository = new RoutineRepository(newData);

        Routine restoredRoutine = newRoutineRepository.findRoutine(morningRoutine.id()).getValue();

        assertNotNull("Routine should persist", restoredRoutine);
        assertEquals("Deleted task should not reappear", 1, restoredRoutine.getTasks().size());
        assertEquals("Remaining task should be correct", "Shower", restoredRoutine.getTasks().get(0).getName());
    }

    @Test
    public void testRoutineDeletionPersistence() {
        routineRepository.removeRoutine(morningRoutine.id());

        InMemoryDataSource newData = InMemoryDataSource.fromList(data.getRoutines());
        RoutineRepository newRoutineRepository = new RoutineRepository(newData);

        List<Routine> restoredRoutines = newRoutineRepository.findAllRoutines().getValue();

        assertEquals("Deleted routine should not reappear", 1, restoredRoutines.size());
        assertEquals("Remaining routine should be correct", "Evening Routine", restoredRoutines.get(0).getName());
    }

    @Test
    public void testRoutineModificationPersistence() {
        morningRoutine.setName("Updated Morning Routine");
        routineRepository.saveRoutine(morningRoutine);

        InMemoryDataSource newData = InMemoryDataSource.fromList(data.getRoutines());
        RoutineRepository newRoutineRepository = new RoutineRepository(newData);

        Routine restoredRoutine = newRoutineRepository.findRoutine(morningRoutine.id()).getValue();

        assertNotNull("Routine should persist", restoredRoutine);
        assertEquals("Routine name should be updated", "Updated Morning Routine", restoredRoutine.getName());
    }
}

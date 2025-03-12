package edu.ucsd.cse110.habitizer.lib.domain;

import static org.junit.Assert.*;

        import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;

public class TaskDeletionTest {
    private InMemoryDataSource data;
    private RoutineRepository routineRepository;
    private Routine routine;
    private Task task1, task2, task3;

    @Before
    public void setUp() {
        data = new InMemoryDataSource();
        routineRepository = new RoutineRepository(data);

        task1 = new Task(0, "Brush Teeth");
        task2 = new Task(1, "Shower");
        task3 = new Task(2, "Make Coffee");

        List<Task> tasks = new ArrayList<>(List.of(task1, task2, task3));
        routine = new Routine(0, "Morning Routine", 3600, tasks);
        routineRepository.saveRoutine(routine);
    }

    @Test
    public void testDeleteTask() {
        routineRepository.removeTask(routine.id(), task2.id());

        Routine updatedRoutine = routineRepository.findRoutine(routine.id()).getValue();

        assertNotNull("Routine should still exist after deleting task", updatedRoutine);
        assertFalse("Deleted task should not be in routine", updatedRoutine.getTasks().contains(task2));
        assertEquals("Routine should now have 2 tasks", 2, updatedRoutine.getTasks().size());
    }

    @Test
    public void testDeleteAllTasksFromRoutine() {
        routineRepository.removeTask(routine.id(), task1.id());
        routineRepository.removeTask(routine.id(), task2.id());
        routineRepository.removeTask(routine.id(), task3.id());

        Routine updatedRoutine = routineRepository.findRoutine(routine.id()).getValue();

        assertNotNull("Routine should still exist", updatedRoutine);
        assertTrue("Routine should have no tasks left", updatedRoutine.getTasks().isEmpty());
    }

    @Test
    public void testDeleteNonExistentTask() {
        routineRepository.removeTask(routine.id(), 99);

        Routine updatedRoutine = routineRepository.findRoutine(routine.id()).getValue();

        assertNotNull("Routine should still exist", updatedRoutine);
        assertEquals("Routine should still have all original tasks", 3, updatedRoutine.getTasks().size());
    }

    @Test
    public void testDeleteTaskPersistence() {
        routineRepository.removeTask(routine.id(), task1.id());

        // Simulate app restart
        InMemoryDataSource newData = InMemoryDataSource.fromList(data.getRoutines());
        RoutineRepository newRoutineRepository = new RoutineRepository(newData);

        Routine restoredRoutine = newRoutineRepository.findRoutine(routine.id()).getValue();

        assertNotNull("Routine should persist after restart", restoredRoutine);
        assertFalse("Deleted task should not be restored", restoredRoutine.getTasks().contains(task1));
        assertEquals("Routine should have 2 remaining tasks", 2, restoredRoutine.getTasks().size());
    }
}

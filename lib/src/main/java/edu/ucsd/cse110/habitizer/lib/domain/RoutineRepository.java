package edu.ucsd.cse110.habitizer.lib.domain;

import java.util.List;
import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.habitizer.lib.util.Subject;

public class RoutineRepository {

    // Data Field
    private final InMemoryDataSource dataSource;


    // Constructor
    public RoutineRepository(InMemoryDataSource dataSource) {
        this.dataSource = dataSource;
    }

    // Routine Data Management
    public int getRoutineCount() {
        return dataSource.getRoutines().size();
    }

    public Subject<Routine> findRoutine(int id) {
        return dataSource.getRoutineSubject(id);
    }

    public Subject<List<Routine>> findAllRoutines() {
        return dataSource.getAllRoutinesSubject();
    }

    public void saveRoutine(Routine routine) {
        dataSource.putRoutine(routine);
    }

    public void saveTask(int routineId, Task task){
        dataSource.putTask(routineId,task);
    }

    public void removeRoutine(int id){
        dataSource.removeRoutine(id);
    }

    public void removeTask(int routineId, int taskId){
        dataSource.removeTask(routineId,taskId);
    }

    public void moveTaskUp(int routineId, int taskId){
        dataSource.moveTaskUp(routineId, taskId);
    }

    public void moveTaskDown(int routineId, int taskId){
        dataSource.moveTaskDown(routineId, taskId);
    }

}

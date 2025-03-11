package edu.ucsd.cse110.habitizer.lib.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.util.Subject;

public class InMemoryDataSource {

    //Data Field
    private final Map<Integer, Routine> routines
            = new HashMap<>();
    private final Map<Integer, Subject<Routine>> routineSubjects
            = new HashMap<>();
    private final Subject<List<Routine>> allRoutinesSubject
            = new Subject<>();
    private int nextRoutineId = 0;
    private int nextTaskId = 0;

    // Constructor
    public InMemoryDataSource() {}

    // Default Initialization
    public final static List<Routine> DEFAULT_ROUTINES = List.of(
            new Routine("Morning", 45*60, new ArrayList<Task>(List.of(
                    new Task(1,"Shower"),
                    new Task(3,"Dress"),
                    new Task(2,"Brush Teeth"),
                    new Task(4,"Make Coffee"),
                    new Task(5,"Make Lunch"),
                    new Task(6,"Dinner Prep"),
                    new Task(7,"Pack Bag")
            ))),

            new Routine("Evening", 60*60*3, new ArrayList<Task>(List.of(
                    new Task(8,"Charge Devices"),
                    new Task(9,"Preparing Dinner"),
                    new Task(10,"Eat Dinner"),
                    new Task(11,"Wash Dishes"),
                    new Task(12,"Pack Bag"),
                    new Task(13,"Sleep")
            )))
    );
    public static InMemoryDataSource fromDefault() {
        var data = new InMemoryDataSource();
        for (Routine routine : DEFAULT_ROUTINES) {
            data.putRoutine(routine);
        }
        return data;
    }

    public static InMemoryDataSource fromList(List<Routine> list) {
        var data = new InMemoryDataSource();
        for (Routine routine : list) {
            data.putRoutine(routine);
        }
        return data;
    }

    //Getters and Setters
    public List<Routine> getRoutines() {
        return List.copyOf(routines.values());
    }

    public Routine getRoutine(int id) {
        return routines.get(id);
    }

    public Subject<Routine> getRoutineSubject(int id) {
        if (!routineSubjects.containsKey(id)) {
            var subject = new Subject<Routine>();
            subject.setValue(getRoutine(id));
            routineSubjects.put(id, subject);
        }
        return routineSubjects.get(id);
    }
    public Subject<List<Routine>> getAllRoutinesSubject() {
        return allRoutinesSubject;
    }


    // Other Methods
    public void putRoutine(Routine routine) {
        var fixedRoutine = preInsert(routine);
        routines.put(fixedRoutine.id(),fixedRoutine);

        if(routineSubjects.containsKey(fixedRoutine.id())){
            routineSubjects.get(fixedRoutine.id()).setValue(fixedRoutine);
        }
        allRoutinesSubject.setValue(getRoutines());
    }

    public void putRoutines(List<Routine> routines){
        var fixedRoutines = routines.stream()
                .map(this::preInsert)
                .collect(Collectors.toList());

        fixedRoutines.forEach(routine -> this.routines.put(routine.id(), routine));

        fixedRoutines.forEach(routine -> {
            if(routineSubjects.containsKey(routine.id())){
                routineSubjects.get(routine.id()).setValue(routine);
            }
        });
        allRoutinesSubject.setValue(getRoutines());
    }

    public void putTask(int routineId, Task task){
        var fixedRoutine = routines.get(routineId);
        var fixedTask = preInsert(task);
        fixedRoutine.addTask(fixedTask);
        routines.replace(routineId,fixedRoutine);

        if(routineSubjects.containsKey(fixedRoutine.id())){
            routineSubjects.get(fixedRoutine.id()).setValue(fixedRoutine);
        }
        allRoutinesSubject.setValue(getRoutines());
    }

    public void putTasks(int routineId, List<Task> tasks){
        var fixedRoutine = routines.get(routineId);
        var fixedTasks = tasks.stream()
                .map(this::preInsert)
                .collect(Collectors.toList());
        fixedRoutine.addAllTask(fixedTasks);
        routines.replace(routineId,fixedRoutine);

        if(routineSubjects.containsKey(fixedRoutine.id())){
            routineSubjects.get(fixedRoutine.id()).setValue(fixedRoutine);
        }
        allRoutinesSubject.setValue(getRoutines());
    }

    public void removeRoutine(int id){
        routines.remove(id);

        if(routineSubjects.containsKey(id)){
            routineSubjects.get(id).setValue(null);
        }
        allRoutinesSubject.setValue(getRoutines());
    }

    public void removeTask(int routineId, int taskId){
        var fixedRoutine = routines.get(routineId);
        fixedRoutine.removeTask(taskId);
        routines.replace(routineId,fixedRoutine);

        if(routineSubjects.containsKey(fixedRoutine.id())){
            routineSubjects.get(fixedRoutine.id()).setValue(fixedRoutine);
        }
        allRoutinesSubject.setValue(getRoutines());
    }

    public void swapTask(int routineId, int taskId, int direction){
        // direction: 1 is down, -1 is up

        var fixedRoutine = routines.get(routineId);
        var taskIndex = fixedRoutine.getTaskIndex(taskId);
        fixedRoutine.swapElement(taskIndex, taskIndex + direction);

        routines.replace(routineId,fixedRoutine);

        if(routineSubjects.containsKey(fixedRoutine.id())){
            routineSubjects.get(fixedRoutine.id()).setValue(fixedRoutine);
        }
        allRoutinesSubject.setValue(getRoutines());
    }

    public void moveTaskUp(int routineId,int taskId){
        var fixedRoutine = routines.get(routineId);
        var taskIndex = fixedRoutine.getTaskIndex(taskId);
        if(taskIndex != 0){
            swapTask(routineId,taskId,-1);
        }
    }

    public void moveTaskDown(int routineId, int taskId){
        var fixedRoutine = routines.get(routineId);
        var taskIndex = fixedRoutine.getTaskIndex(taskId);
        if(taskIndex < fixedRoutine.getTaskCount() - 1){
            swapTask(routineId,taskId,1);
        }
    }


    // Setting ID private functions
    private Routine preInsert(Routine routine){
        var id = routine.id();
        if(id == null){
            routine = routine.withId(nextRoutineId++);
        } else if (id > nextRoutineId) {
            nextRoutineId = id + 1;
        }
        return routine;
    }

    private Task preInsert(Task task){
        var id = task.id();
        if(id == null){
            task = task.withId(nextTaskId++);
        } else if (id > nextTaskId) {
            nextTaskId = id + 1;
        }
        return task;
    }





}

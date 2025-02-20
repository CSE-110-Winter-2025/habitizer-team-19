package edu.ucsd.cse110.habitizer.app;


import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.RoutineRepository;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.domain.TimerInterface;
import edu.ucsd.cse110.habitizer.lib.util.Subject;

public class MainViewModel extends ViewModel {

    // Insert States:
    private final RoutineRepository routineRepository;

    private TimerInterface timer;
    private long totalElapsedTime = 0;
    private long routineDisplayTime = 0;
    private int hasStarted = 0;

    private final Subject<List<Routine>> routineSubjectList;
    private final Subject<Integer> currentRoutineId ;
    private final Subject<List<Task>> currentRoutineTasks ;
    private final List<Subject<Task>> currentTaskSubjects;
    private final Subject<Integer> currentRoutineState ; //0: initial, 1: routine started, 2:routine ended

    private final Subject<Boolean> fragmentState; //True: Routine List, False: Task List


    //
    public static final ViewModelInitializer<MainViewModel> initializer =
            new ViewModelInitializer<>(
                    MainViewModel.class,
                    creationExtras -> {
                        var app = (HabitizerApplication) creationExtras.get(APPLICATION_KEY);
                        assert app != null;
                        return new MainViewModel(app.getRoutineRepository());
                    });


    public MainViewModel(RoutineRepository routineRepository) {
        this.routineRepository = routineRepository;
        this.routineSubjectList = new Subject<>();
        this.currentRoutineId = new Subject<>();
        this.currentRoutineTasks = new Subject<>();
        this.currentRoutineState = new Subject<>();
        this.fragmentState = new Subject<>();
        this.currentTaskSubjects = new ArrayList<>();

        //Initialize
        currentRoutineId.setValue(Integer.valueOf(-1));
        currentRoutineState.setValue(0);
        fragmentState.setValue(true);

        //Observers

        //When the routine list changes (or is first loaded) update values
        routineRepository.findAllRoutines().observe(routines ->{
            if(routines == null) return;
            routineSubjectList.setValue(routines);
        });

        //When the user selects a routine from the routine list. The current routine gets updated
        currentRoutineId.observe(id -> {
            if(id == null || id == -1) {
                currentRoutineTasks.setValue(new ArrayList<>()); // No routine selected (User sees routine list view)
                currentTaskSubjects.clear();
            } else {
                var routine = routineRepository.findRoutine(id).getValue();
                if (routine != null) {
                    currentRoutineTasks.setValue(routine.getTasks());
                    updateTaskSubjects(routine.getTasks()); // observe each task individually
                }
            }
        });
    }

    public Subject<List<Task>> getCurrentRoutineTasks() {
        return currentRoutineTasks;
    }

    public Subject<Task> getTaskSubject(int index) {
        if (index >= 0 && index < currentTaskSubjects.size()) {
            return currentTaskSubjects.get(index);
        }
        return null;
    }


    // Update the task subjects for each task in the routine
    private void updateTaskSubjects(List<Task> tasks) {
        currentTaskSubjects.clear();

        for (Task task : tasks) {
            Subject<Task> taskSubject = new Subject<>();
            taskSubject.setValue(task);  // Set initial value of task
            currentTaskSubjects.add(taskSubject);

            // Now you can observe each individual task
            taskSubject.observe(this::updateTaskUI);
        }
    }

    private void updateTaskUI(Task task) {
        // Implement UI update logic, for example, notify the adapter in a ListView
    }

    // Add a new task to the routine
    public void addTask(Task newTask) {
        Integer routineId = currentRoutineId.getValue();
        if (routineId == null || routineId == -1) {
            return;  // No routine selected
        }

        // Save the new task using the repository
        routineRepository.saveTask(routineId, newTask);

        // Update the task list and notify observers
        var routine = routineRepository.findRoutine(routineId).getValue();
        if (routine != null) {
            currentRoutineTasks.setValue(routine.getTasks()); // Refresh the task list
        }
    }

    // Remove a task from the routine
    public void removeTask(Integer taskId) {
        Integer routineId = currentRoutineId.getValue();
        if (routineId == null || routineId == -1 || taskId == null) {
            return;
        }

        routineRepository.removeTask(routineId,taskId);

        // Update the task list and notify observers
        var routine = routineRepository.findRoutine(routineId).getValue();
        if (routine != null) {
            currentRoutineTasks.setValue(routine.getTasks()); // Refresh the task list
        }
    }

    // Update a task in the routine
    public void updateTask(Task updatedTask) {
        Integer routineId = currentRoutineId.getValue();
        if (routineId == null || routineId == -1) {
            return;  // No routine selected
        }

        var routine = routineRepository.findRoutine(routineId).getValue();
        if (routine != null) {
            List<Task> tasks = routine.getTasks();
            int index = -1;
            for (int i = 0; i < tasks.size(); i++) {
                if (Objects.equals(tasks.get(i).id(), updatedTask.id())) {
                    index = i;
                    break;
                }
            }
            if (index != -1) {
                tasks.set(index, updatedTask); // Update the task in the list
                routineRepository.saveRoutine(routine); // Save the updated routine

                // Refresh the task list and notify observers
                currentRoutineTasks.setValue(routine.getTasks());
            }
        }
    }


//    public Subject<List<Routine>> getRoutines() {
//        return routineRepository.findAllRoutines();
//    }
//
//    public Subject<List<Task>> getTasks(String routineName){
//        var tasks = new Subject<List<Task>>();
//        tasks.setValue(Objects.requireNonNull(routineRepository.findRoutine(routineName).getValue()).getTasks());
//        System.out.println("test");
//        return tasks;
//    }
//
//    public Subject<RoutineRepository> getRepository(){
//        var repository = new Subject<RoutineRepository>();
//        repository.setValue(routineRepository);
//        return repository;
//    }
//
//    public void pushTask (Task task) {
//        var routine = routineRepository.findRoutine(selecetedRoutine);
//        assert routine.getValue() != null;
//        routine.getValue().addTask(task);
//    }
//
//    public void removeTask(String name) {
//        assert routineRepository.findRoutine(selecetedRoutine).getValue() != null;
//        routineRepository.findRoutine(selecetedRoutine).getValue().removeTask(name);
//    }
//
//    public void setSelectedRoutine(String selectedRoutine) {
//        this.selecetedRoutine = selectedRoutine;
//    }
}

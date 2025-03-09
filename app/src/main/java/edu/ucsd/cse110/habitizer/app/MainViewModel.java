package edu.ucsd.cse110.habitizer.app;
import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.ucsd.cse110.habitizer.lib.domain.MockTimer;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.RoutineRepository;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.domain.Timer;
import edu.ucsd.cse110.habitizer.lib.domain.TimerInterface;
import edu.ucsd.cse110.habitizer.lib.util.Subject;
import android.os.Handler;
import android.os.Looper;


public class MainViewModel extends ViewModel {

    // Insert States:
    private final RoutineRepository routineRepository;

    private TimerInterface timer;
    private long totalElapsedTime = 0;
    private long routineDisplayTime = 0;

    private boolean paused = false;

    private final Subject<List<Routine>> routineSubjectList;
    private final Subject<Integer> currentRoutineId ;
    private final Subject<List<Task>> currentRoutineTasks ;
    private final List<Subject<Task>> currentTaskSubjects;
    private final Subject<Integer> routineState; //0: initial, 1: routine started, 2:routine ended

    private final Subject<Boolean> fragmentState; //True: Routine List, False: Task List
    private final Subject<String> routineElapsedTimeFormatted = new Subject<>();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());



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
        this.routineState = new Subject<>();
        this.fragmentState = new Subject<>();
        this.currentTaskSubjects = new ArrayList<>();
        this.timer = new Timer();

        //Initialize
        currentRoutineId.setValue(Integer.valueOf(-1));
        routineState.setValue(0);
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

    public Subject<List<Routine>> getRoutines() {
        return routineRepository.findAllRoutines();
    }

    public void setCurrentRoutineId(Integer id){
        currentRoutineId.setValue(id);
    }

    public void addRoutine(){
        Routine routine = new Routine("New Routine",0);
        routineRepository.saveRoutine(routine);
    }



    // Update the task subjects for each task in the routine
    private void updateTaskSubjects(List<Task> tasks) {
        currentTaskSubjects.clear();

        for (Task task : tasks) {
            Subject<Task> taskSubject = new Subject<>();
            taskSubject.setValue(task);  // Set initial value of task
            currentTaskSubjects.add(taskSubject);

            // Now you can observe each individual task
//            taskSubject.observe(this::updateTaskUI);
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

    // Routine State Management
    public void startRoutine() {
        setRoutineState(1);
        totalElapsedTime = 0;
        routineDisplayTime = 0;

        routineElapsedTimeFormatted.setValue("00:00:00");

        startTimer();
    }

    public void endRoutine() {
        long elapsed = timer.peekElapsedTime();

        // Round UP to the next full minute
        routineDisplayTime = ((elapsed + 59) / 60) * 60;

        endTimer();
        setRoutineState(2);

        // Ensure UI updates on the main thread
        mainHandler.post(() -> routineElapsedTimeFormatted.setValue(getRoutineElapsedTimeString()));
    }

    public void setRoutineState(int status) {
        routineState.setValue(status);
    }

    public Subject<Integer> getRoutineState() {
        return routineState;
    }

    public void resetAllRoutines() {
        routineRepository.findAllRoutines().observe(routines -> {
            assert routines != null;
            for (Routine routine : routines) {
                routine.reset();
            }
        });
        setRoutineState(0);
        routineElapsedTimeFormatted.setValue("--:--:--");

    }

    // Timer Logic:
    public void startTimer() {
        this.timer = new Timer();
        timer.startTimer();
        routineDisplayTime = 0; // Reset routine display time

        new Thread(() -> {
            while (routineState.getValue() == 1) {
                long elapsed = timer.peekElapsedTime(); // Fetch elapsed time without resetting task timers
                long newRoutineTime = (elapsed / 60) * 60; // Round to nearest full minute

                if (newRoutineTime > routineDisplayTime) {
                    routineDisplayTime = newRoutineTime;
                    // Ensure UI updates on the main thread
                    mainHandler.post(() -> routineElapsedTimeFormatted.setValue(getRoutineElapsedTimeString()));
                }

                try {
                    Thread.sleep(1000); // Update every second
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }


    public void endTimer() {
        timer.endTimer();
    }

    public void pauseTimer() {
        if (timer != null) {
            timer.pauseTimer();
        }
    }

    public void resumeTimer() {
        if (timer != null) {
            timer.resumeTimer();
        }
    }

    public void resetToRealTimer() {
        this.timer = new Timer();
        this.routineState.setValue(0);
        totalElapsedTime = 0;
        routineDisplayTime = 0;
    }

    public void advanceTime() {
        if (timer != null) {
            timer.advanceTime();
        }
    }

    public static long taskDisplay(long elapsedTime){
        if(elapsedTime<60) {
            return ((elapsedTime-1)/5)*5+5;
        }
        else{
            return ((elapsedTime + 59) / 60) * 60;
        }
    }

    public long getElapsedTime() {
        return timer.getElapsedTime();
    }

    public TimerInterface getTimer() {
        return timer;
    }

    public void completeTask(@NonNull Task task) {


        long elapsedTime = getElapsedTime();
        totalElapsedTime += elapsedTime;
        long roundedTaskTime = taskDisplay(elapsedTime);

        routineDisplayTime += getRoundedRoutineElapsedTime(elapsedTime);
        task.setElapsedTime(roundedTaskTime);
        task.setCompletionStatus(1);
    }

    public void skipTask(@NonNull Task task){
        task.setCompletionStatus(2);
    }

    public long getTotalElapsedTime() {
        return totalElapsedTime;
    }

    public String getRoutineElapsedTimeString() {
        if (routineDisplayTime <= 0) {
            return "00:00:00";
        }
        long hours = routineDisplayTime / 3600;
        long minutes = (routineDisplayTime % 3600) / 60;
        long seconds = routineDisplayTime % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    // Helper Methods
    public long getRoundedRoutineElapsedTime(long elapsedTime) {
        return (elapsedTime / 60) * 60;
    }

    public Subject<String> getRoutineElapsedTimeFormatted() {
        return routineElapsedTimeFormatted;
    }
}

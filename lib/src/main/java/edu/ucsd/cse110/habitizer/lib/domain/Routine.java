package edu.ucsd.cse110.habitizer.lib.domain;

import androidx.annotation.NonNull;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



import java.util.Objects;

import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.util.Subject;

public class Routine {

    private @NonNull String name;

    private long goalTimeSeconds;

    private final @NonNull ArrayList<Task> tasks;

    public Routine(@NonNull String name, long goalTimeSeconds, @NonNull ArrayList<Task> tasks){
        this.name = name;
        this.goalTimeSeconds = goalTimeSeconds;
        this.tasks = new ArrayList<>(tasks);
    }

//    public void startRoutine(){
//        timer.startTimer();
//        for(Task task: tasks ){
//            task.restartTimer();
//        }
//        isStarted = true;
//    }
//
//    public void endRoutine(){
//        if(!isStarted){
//            throw new IllegalArgumentException("Ended before Start Routine.");
//        }
//        timer.endTimer();
//        for(Task task: tasks ){
//            if(!task.isCompleted() && !task.isSkipped()){
//                task.skip();
//            }
//        }
//
//        actualTime = timer.calculateElapsedTime();
//    }

    public void addTask(Task task){
        tasks.add(task);
    }

    public void removeTask(String name) {
        //Use the name to find and remove the task
    }

    @NonNull
    public String getName() {
        return name;
    }

    public long getGoalTime() {
        return goalTimeSeconds;
    }

    public void newGoalTime(long newTime) {
        this.goalTimeSeconds = newTime;
    }

    public void newName(String newName) {
        this.name = newName;
    }

    public void reset() {
        for(Task task: tasks) {
            task.reset();
        }
    }


    public List<Task> getTasks() {
        return tasks;
    }
}

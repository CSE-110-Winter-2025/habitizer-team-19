package edu.ucsd.cse110.habitizer.lib.domain;

import androidx.annotation.NonNull;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;



import java.util.Objects;
public class Routine {

    private final @NonNull String title;

    private final @NonNull Time goalTime;

    private final @NonNull List<Task> tasks;

    private final @NonNull Timer timer;

    private @NonNull Time actualTime;

    private @NonNull boolean isStarted;

    public Routine(String title,int goalHours, int goalMinutes){
        this.title = title;
        this.goalTime = new Time(goalHours,goalMinutes,0);
        this.tasks = new ArrayList<Task>();
        this.timer = new Timer();
        this.actualTime = new Time(0);
        this.isStarted = false;
    }

    public void startRoutine(){
        timer.startTimer();
        for(Task task: tasks ){
            task.restartTimer();
        }
        isStarted = true;
    }

    public void endRoutine(){
        if(!isStarted){
            throw new IllegalArgumentException("Ended before Start Routine.");
        }
        timer.endTimer();
        for(Task task: tasks ){
            if(!task.isCompleted() && !task.isSkipped()){
                task.skip();
            }
        }

        actualTime = timer.calculateElapsedTime();
    }

    public void addTask(Task task){
        tasks.add(task);
    }





}


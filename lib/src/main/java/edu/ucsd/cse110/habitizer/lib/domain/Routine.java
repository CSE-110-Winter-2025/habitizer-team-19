package edu.ucsd.cse110.habitizer.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Routine {

    private final @Nullable Integer id;
    private final @NonNull String name;
    private final long goalTimeSeconds;
    private final @NonNull List<Task> tasks;


    public Routine(@NonNull String name, long goalTimeSeconds){
        this.id = null;
        this.name = name;
        this.goalTimeSeconds = goalTimeSeconds;
        this.tasks = new ArrayList<>();
    }
    public Routine(@Nullable Integer id, @NonNull String name, long goalTimeSeconds, @NonNull List<Task> tasks) {
        this.id = id;
        this.name = name;
        this.goalTimeSeconds = goalTimeSeconds;
        this.tasks = new ArrayList<>(tasks);
    }


    public @NonNull Integer id(){
        assert id != null;
        return id;
    }
    public @NonNull String getName(){return name;}
    public long getGoalTimeSeconds(){return goalTimeSeconds;}
    public @NonNull List<Task> getTasks() {
        return tasks;
    }

    public void addTask(Task task){
        tasks.add(task);
    }
    public void addAllTask(List<Task> tasks){
        this.tasks.addAll(tasks);
    }
    public void removeTask(int id){
        tasks.removeIf(task -> Objects.equals(task.id(), id));
    }

    public Routine withId(int id){
        return new Routine(id,this.name,this.goalTimeSeconds,this.tasks);
    }

    // Reset Routine
    public void reset() {
        tasks.forEach(Task::reset);
    }



    //    public String getGoalTimeToString() {
//        long hours = goalTimeSeconds / 3600;
//        long minutes = (goalTimeSeconds % 3600) / 60;
//        long seconds = goalTimeSeconds % 60;
//        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
//    }
}

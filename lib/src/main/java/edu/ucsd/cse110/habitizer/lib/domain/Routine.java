package edu.ucsd.cse110.habitizer.lib.domain;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Objects;

public class Routine {

    private @NonNull String name;
    private long goalTimeSeconds;
    private int totalTaskClicked = 0;
    private final @NonNull ArrayList<Task> tasks;

    public Routine(@NonNull String name, long goalTimeSeconds, @NonNull ArrayList<Task> tasks) {
        this.name = name;
        this.goalTimeSeconds = goalTimeSeconds;
        this.tasks = new ArrayList<>(tasks);
    }

    // Task Management
    public void addTask(Task task) {
        tasks.add(task);
    }

    public void removeTask(String taskName) {
        tasks.removeIf(task -> Objects.equals(task.getName(), taskName));
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    // Routine Properties
    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String newName) {
        this.name = newName;
    }

    public long getGoalTime() {
        return goalTimeSeconds;
    }

    public void setGoalTime(long newTime) {
        this.goalTimeSeconds = newTime;
    }

    public String getGoalTimeToString() {
        long hours = goalTimeSeconds / 3600;
        long minutes = (goalTimeSeconds % 3600) / 60;
        long seconds = goalTimeSeconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    // Reset Routine
    public void reset() {
        tasks.forEach(Task::reset);
    }
}

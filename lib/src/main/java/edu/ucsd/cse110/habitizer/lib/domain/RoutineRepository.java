package edu.ucsd.cse110.habitizer.lib.domain;

import java.util.List;
import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.habitizer.lib.util.Subject;

public class RoutineRepository {

    public static final RoutineRepository rM = new RoutineRepository(InMemoryDataSource.fromDefault());

    private final InMemoryDataSource dataSource;
    private TimerInterface timer;
    private long totalElapsedTime = 0;
    private long routineDisplayTime = 0;
    private int hasStarted = 0;

    public RoutineRepository(InMemoryDataSource dataSource) {
        this.dataSource = dataSource;
        this.timer = new Timer();
    }

    // Timer Management
    public void startTimer() {
        this.timer = new Timer();
        timer.startTimer();
    }

    public void endTimer() {
        timer.endTimer();
    }

    public void switchToMockTimer() {
        long currentTime = timer.getElapsedTime();
        this.timer = new MockTimer(currentTime);
        this.timer.startTimer();
    }

    public void resetToRealTimer() {
        this.timer = new Timer();
        this.hasStarted = 0;
        totalElapsedTime = 0;
        routineDisplayTime = 0;
    }

    public void advanceTime() {
        if (timer instanceof MockTimer) {
            ((MockTimer) timer).advanceTime();
        }
    }

    public long getElapsedTime() {
        return timer.getElapsedTime();
    }

    public TimerInterface getTimer() {
        return timer;
    }

    // Routine State Management
    public void startRoutine() {
        totalElapsedTime = 0;
        routineDisplayTime = 0;
        startTimer();
        setRoutineStatus(1);
    }

    public void endRoutine() {
        endTimer();
        setRoutineStatus(2);
    }

    public void setRoutineStatus(int started) {
        this.hasStarted = started;
    }

    public int getRoutineStatus() {
        return this.hasStarted;
    }

    public void resetAllRoutines() {
        dataSource.getAllRoutinesSubject().observe(routines -> {
            assert routines != null;
            for (Routine routine : routines) {
                routine.reset();
            }
        });
        setRoutineStatus(0);
    }

    // Routine Data Management
    public int getRoutineCount() {
        return dataSource.getRoutines().size();
    }

    public Subject<Routine> findRoutine(String name) {
        return dataSource.getRoutineSubject(name);
    }

    public Subject<List<Routine>> findAllRoutines() {
        return dataSource.getAllRoutinesSubject();
    }

    public void saveRoutine(Routine routine) {
        dataSource.putRoutine(routine);
    }

    // Task Completion & Time Tracking
    public void completeTask(Task task) {
        long elapsedTime = timer.getElapsedTime();
        totalElapsedTime += elapsedTime;
        long roundedTaskTime = ((elapsedTime + 59) / 60) * 60;
        routineDisplayTime += getRoundedRoutineElapsedTime(elapsedTime);
        task.setElapsedTime(roundedTaskTime);
    }

    public void setElapsedTime(Task task) {
        task.setElapsedTime(timer.getElapsedTime());
    }

    public long getTotalElapsedTime() {
        return totalElapsedTime;
    }

    public String getRoutineElapsedTimeString() {
        if (routineDisplayTime <= 0) {
            return "--:--:--";
        }
        long hours = routineDisplayTime / 3600;
        long minutes = (routineDisplayTime % 3600) / 60;
        long seconds = routineDisplayTime % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public String getTotalElapsedTimeToString() {
        if (totalElapsedTime <= 0) {
            return "--:--:--";
        }
        long roundedTime = ((totalElapsedTime + 59) / 60) * 60;
        long hours = roundedTime / 3600;
        long minutes = (roundedTime % 3600) / 60;
        long seconds = roundedTime % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    // Helper Methods
    public long getRoundedRoutineElapsedTime(long elapsedTime) {
        return (elapsedTime / 60) * 60;
    }
}

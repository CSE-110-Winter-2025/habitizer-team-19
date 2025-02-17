package edu.ucsd.cse110.habitizer.lib.domain;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.habitizer.lib.util.Subject;
import edu.ucsd.cse110.habitizer.lib.domain.MockTimer;

public class RoutineRepository {

    public static final RoutineRepository rM = new RoutineRepository(InMemoryDataSource.fromDefault());

    private final InMemoryDataSource dataSource;
    private TimerInterface timer;

    private int hasStarted = 0;

    public RoutineRepository(InMemoryDataSource dataSource) {
        this.dataSource = dataSource;
        this.timer = new Timer();
    }

    public void switchToMockTimer() {
        long currentTime = timer.getElapsedTime();
        this.timer = new MockTimer(currentTime);
        this.timer.startTimer();
    }

    public void resetToRealTimer() {
        this.timer = new Timer();  // Reset to real timer
        this.hasStarted = 0;       // Reset routine state
    }


    public void start(){
        startTimer();
        setHasStarted(1);
    }

    public void end(){
        endTimer();
        setHasStarted(2);
    }

    public void setHasStarted(int started){
        this.hasStarted = started;
    }

    public int getHasStarted(){
        return this.hasStarted;
    }

    public Integer count() {
        return dataSource.getRoutines().size();
    }

    public Subject<Routine> find(String name) {
        return dataSource.getRoutineSubject(name);
    }

    public Subject<List<Routine>> findAll() {
        return dataSource.getAllRoutinesSubject();
    }

    public void save(Routine routine) {
        dataSource.putRoutine(routine);
    }

    public void startTimer() {
        timer.startTimer();
    }

    public void endTimer(){
        timer.endTimer();
    }

    public long getElapsedTime() {
        return timer.getElapsedTime();
    }

    public void setElapsedTime(Task task){
        task.setElapsedTime(timer.getElapsedTime());
    }

    public void resetRoutines(){
        dataSource.getAllRoutinesSubject().observe(routines -> {
            assert routines != null;
            for (Routine routine : routines) {
                routine.reset();
            }
        });
        setHasStarted(0);
    }

    public void advanceTime() {
        if(timer instanceof MockTimer) {
            ((MockTimer) timer).advanceTime();
        }
    }


    public TimerInterface getTimer(){
        return timer;
    }
}

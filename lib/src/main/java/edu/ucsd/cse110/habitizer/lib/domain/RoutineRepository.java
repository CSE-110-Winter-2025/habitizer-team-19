package edu.ucsd.cse110.habitizer.lib.domain;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.habitizer.lib.util.Subject;

public class RoutineRepository {

    public static final RoutineRepository rM = new RoutineRepository(InMemoryDataSource.fromDefault());

    private final InMemoryDataSource dataSource;
    private final Timer timer;

    private boolean hasStarted = false;

    public void setHasStarted(boolean started){
        this.hasStarted = started;
    }

    public boolean getHasStarted(){
        return this.hasStarted;
    }

    public void start(){
        startTimer();
        setHasStarted(true);
    }

    public void end(){
        endTimer();;
        setHasStarted(false);
    }

    public RoutineRepository(InMemoryDataSource dataSource) {
        this.dataSource = dataSource;
        this.timer = new Timer();
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
    }



    public Timer getTimer(){
        return timer;
    }
}

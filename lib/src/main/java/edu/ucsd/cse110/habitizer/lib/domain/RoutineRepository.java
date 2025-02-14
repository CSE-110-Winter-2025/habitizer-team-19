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

    public long getElapsedTime() {
        return timer.getElapsedTime();
    }
    /*
    public long getTimeDifference(long seconds) {
        return seconds - ;
    }
    */


}

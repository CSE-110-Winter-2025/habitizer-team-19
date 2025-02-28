package edu.ucsd.cse110.habitizer.app;

import android.app.Application;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.RoutineRepository;

public class HabitizerApplication extends Application {

    private InMemoryDataSource dataSource;

    private RoutineRepository routineRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        this.dataSource =InMemoryDataSource.fromDefault();
        this.routineRepository = new RoutineRepository(dataSource);
    }

    public RoutineRepository getRoutineRepository(){
        return routineRepository;
    }

}

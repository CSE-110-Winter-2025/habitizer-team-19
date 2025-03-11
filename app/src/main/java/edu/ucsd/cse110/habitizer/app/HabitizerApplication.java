package edu.ucsd.cse110.habitizer.app;

import android.app.Application;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.List;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.RoutineRepository;

public class HabitizerApplication extends Application {

    private InMemoryDataSource dataSource;

    private RoutineRepository routineRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        //Load from file instead if possible
        try (FileInputStream fis = this.getApplicationContext().openFileInput("repoData");
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            List<Routine> routineList = (List<Routine>) ois.readObject();
            this.dataSource = InMemoryDataSource.fromList(routineList);
        } catch (Exception e) {
            this.dataSource = InMemoryDataSource.fromDefault();
        }
        this.routineRepository = new RoutineRepository(dataSource);
    }

    public RoutineRepository getRoutineRepository(){
        return routineRepository;
    }

}

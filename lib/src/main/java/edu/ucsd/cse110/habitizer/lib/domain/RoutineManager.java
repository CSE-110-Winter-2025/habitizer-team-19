package edu.ucsd.cse110.habitizer.lib.domain;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class RoutineManager {
    //We only ever will have one list of routines, this makes all our top level stuff static so we can access it globally via RoutineManager.rM (abbreviated for simplicity)
    //This also gives us a convenient place to hardcode our two existing routines - I'm just making an example tho, we'll add more complete values later
    static final RoutineManager rM = new RoutineManager(new ArrayList<Routine>(List.of(
            new Routine("Morning", 60*60, new ArrayList<Task>(List.of(
                    new Task("Brush Teeth"),
                    new Task("Shower")
            ))),
            new Routine("Evening", 60*60*3, new ArrayList<Task>(List.of(
                    new Task("Homework"),
                    new Task("Dinner")
            )))
    )));
    private final @NonNull ArrayList<Routine> routines;

    private final Timer timer;
    private RoutineManager(@NonNull ArrayList<Routine> routines) {
        this.routines = routines;
        this.timer = new Timer();
    }

}

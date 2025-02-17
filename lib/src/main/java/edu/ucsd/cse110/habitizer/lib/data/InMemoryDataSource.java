package edu.ucsd.cse110.habitizer.lib.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.util.Subject;

public class InMemoryDataSource {
    private final Map<String, Routine> routines
            = new HashMap<>();
    private final Map<String, Subject<Routine>> routineSubjects
            = new HashMap<>();
    private final Subject<List<Routine>> allRoutinesSubject
            = new Subject<>();

    public InMemoryDataSource() {
    }

    public List<Routine> getRoutines() {
        return List.copyOf(routines.values());
    }

    public Routine getRoutine(String name) {
        return routines.get(name);
    }

    public Subject<Routine> getRoutineSubject(String name) {
        if (!routineSubjects.containsKey(name)) {
            var subject = new Subject<Routine>();
            subject.setValue(getRoutine(name));
            routineSubjects.put(name, subject);
        }
        return routineSubjects.get(name);
    }

    public Subject<List<Routine>> getAllRoutinesSubject() {
        return allRoutinesSubject;
    }

    public void putRoutine(Routine routine) {
        routines.put(routine.getName(), routine);
        if (routineSubjects.containsKey(routine.getName())) {
            routineSubjects.get(routine.getName()).setValue(routine);
        }
        allRoutinesSubject.setValue(getRoutines());
    }

    public final static List<Routine> DEFAULT_ROUTINES = List.of(
            new Routine("Morning", 45*60, new ArrayList<Task>(List.of(
                    new Task("Shower"),
                    new Task("Brush Teeth"),
                    new Task("Dress"),
                    new Task("Make Coffee"),
                    new Task("Make Lunch"),
                    new Task("Dinner Prep"),
                    new Task("Pack Bag")
            ))),

            new Routine("Evening", 60*60*3, new ArrayList<Task>(List.of(
                    new Task("Charge Devices"),
                    new Task("Preparing Dinner"),
                    new Task("Eat Dinner"),
                    new Task("Wash Dishes"),
                    new Task("Pack Bag"),
                    new Task("Sleep")
            )))
    );




    public static InMemoryDataSource fromDefault() {
        var data = new InMemoryDataSource();
        for (Routine routine : DEFAULT_ROUTINES) {
            data.putRoutine(routine);
        }

        return data;
    }
}

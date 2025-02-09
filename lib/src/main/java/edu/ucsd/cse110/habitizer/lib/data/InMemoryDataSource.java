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
            new Routine("Morning", 60*60, new ArrayList<Task>(List.of(
                    new Task("Brush Teeth"),
                    new Task("Shower")
            ))),
            new Routine("Evening", 60*60*3, new ArrayList<Task>(List.of(
                    new Task("Homework"),
                    new Task("Dinner")
            ))));

    public static InMemoryDataSource fromDefault() {
        var data = new InMemoryDataSource();
        for (Routine routine : DEFAULT_ROUTINES) {
            data.putRoutine(routine);
        }
        return data;
    }
}

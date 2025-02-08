package edu.ucsd.cse110.habitizer.lib.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.util.Subject;

public class InMemoryDataSource {

    private final Map<Integer, Routine> routines
            = new HashMap<>();
    private final Map<Integer, Subject<Routine>> routineSubjects
            = new HashMap<>();
    private final Subject<List<Routine>> allRoutinesSubject
            = new Subject<>();

    public InMemoryDataSource() {
    }

    public List<Routine> getRoutines() {
        return List.copyOf(routines.values());
    }

    public Routine getRoutine(int id) {
        return routines.get(id);
    }

    public Subject<Routine> getRoutineSubject(int id) {
        if (!routineSubjects.containsKey(id)) {
            var subject = new Subject<Routine>();
            subject.setValue(getRoutine(id));
            routineSubjects.put(id, subject);
        }
        return routineSubjects.get(id);
    }

    public Subject<List<Routine>> getAllRoutinesSubject() {
        return allRoutinesSubject;
    }

    public void putRoutine(Routine routine) {
        routines.put(routine.id(), routine);
        if (routineSubjects.containsKey(routine.id())) {
            routineSubjects.get(routine.id()).setValue(routine);
        }
        allRoutinesSubject.setValue(getRoutines());
    }

    public final static List<Routine> DEFAULT_ROUTINES = List.of(
            new Routine(0,"Morning", 1,30 ),
            new Routine(1,"Night",0,45 )
    );

    public static InMemoryDataSource fromDefault() {
        var data = new InMemoryDataSource();
        for (Routine routine : DEFAULT_ROUTINES) {
            data.putRoutine(routine);
        }
        return data;
    }
}

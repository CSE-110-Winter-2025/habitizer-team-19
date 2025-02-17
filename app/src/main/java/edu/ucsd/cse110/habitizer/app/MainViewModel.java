package edu.ucsd.cse110.habitizer.app;


import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import java.util.List;
import java.util.Objects;

import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.RoutineRepository;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.util.Subject;

public class MainViewModel extends ViewModel {

    // Insert States:
    private final RoutineRepository routineRepository;

    private String selecetedRoutine = "";

    //
    public static final ViewModelInitializer<MainViewModel> initializer =
            new ViewModelInitializer<>(
                    MainViewModel.class,
                    creationExtras -> {
                        var app = (HabitizerApplication) creationExtras.get(APPLICATION_KEY);
                        assert app != null;
                        return new MainViewModel(app.getRoutineRepository());
                    });


    public MainViewModel(RoutineRepository routineRepository) {
        this.routineRepository = routineRepository;

    }

    public Subject<List<Routine>> getRoutines() {
        return routineRepository.findAllRoutines();
    }

    public Subject<List<Task>> getTasks(String routineName){
        var tasks = new Subject<List<Task>>();
        tasks.setValue(Objects.requireNonNull(routineRepository.findRoutine(routineName).getValue()).getTasks());
        System.out.println("test");
        return tasks;
    }

    public Subject<RoutineRepository> getRepository(){
        var repository = new Subject<RoutineRepository>();
        repository.setValue(routineRepository);
        return repository;
    }

    public void pushTask (Task task) {
        var routine = routineRepository.findRoutine(selecetedRoutine);
        assert routine.getValue() != null;
        routine.getValue().addTask(task);
    }

    public void removeTask(String name) {
        assert routineRepository.findRoutine(selecetedRoutine).getValue() != null;
        routineRepository.findRoutine(selecetedRoutine).getValue().removeTask(name);
    }

    public void setSelectedRoutine(String selectedRoutine) {
        this.selecetedRoutine = selectedRoutine;
    }
}

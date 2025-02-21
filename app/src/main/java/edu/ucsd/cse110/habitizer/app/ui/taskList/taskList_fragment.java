package edu.ucsd.cse110.habitizer.app.ui.taskList;

import static edu.ucsd.cse110.habitizer.lib.domain.RoutineRepository.*;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.ucsd.cse110.habitizer.app.MainActivity;
import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentTaskListBinding;
import edu.ucsd.cse110.habitizer.app.ui.taskList.dialog.confirmDeleteTaskDialogFragment;
import edu.ucsd.cse110.habitizer.lib.domain.RoutineRepository;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link taskList_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class taskList_fragment extends Fragment{

    private MainViewModel activityModel;

    private FragmentTaskListBinding view;

    private taskList_adapter adapter;

    public taskList_fragment() {
        // Required empty public constructor
    }

    public static taskList_fragment newInstance(Integer id) {
        taskList_fragment fragment = new taskList_fragment();
        Bundle args = new Bundle();

        args.putInt("routineId", id);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if(getArguments() != null){
            Integer routineId = getArguments().getInt("routineId");

            var modelOwner = requireActivity();
            var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
            var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
            this.activityModel = modelProvider.get(MainViewModel.class);

            this.adapter = new taskList_adapter(requireContext(), List.of(),activityModel, taskId -> {
                var dialogFragment = confirmDeleteTaskDialogFragment.newInstance(taskId);
                dialogFragment.show(getParentFragmentManager(), "ConfirmDeleteTaskDialogFragment");
            });

            activityModel.setCurrentRoutineId(routineId);


            activityModel.getCurrentRoutineTasks().observe(tasks -> {
                if (tasks == null) return;
                adapter.clear();
                adapter.addAll(new ArrayList<>(tasks)); // remember the mutable copy here!
                adapter.notifyDataSetChanged();
            });

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = FragmentTaskListBinding.inflate(inflater, container, false);
        view.taskList.setAdapter(adapter);
        view.StopTimerButton.setEnabled(false);

        adapter.setOnTaskComplete(totalTime -> {
            view.TotalElapsedTime.setText("Total Elapsed Time: " + activityModel.getRoutineElapsedTimeString());
        });

        adapter.setOnAllTasksDone(() -> {
            // Only do this if the routine is currently running
            if (activityModel.getRoutineStatus() == 1) {
                activityModel.endRoutine();
                adapter.setButtonsEnabled(false);
                view.TotalElapsedTime.setText("Total Elapsed Time: " + activityModel.getTotalElapsedTimeToString());
                view.StartRoutineButton.setText("Ended Routine");
                view.StartRoutineButton.setEnabled(false);
                view.StopTimerButton.setEnabled(false);
                view.AdvanceTimerButton.setEnabled(false);
            }
        });

        view.StartRoutineButton.setOnClickListener(v -> {
            if(activityModel.getRoutineStatus() == 0){
                activityModel.startRoutine();
                view.StopTimerButton.setEnabled(true);
                adapter.setRemoveEnabled(false);
                adapter.setTimerEnabled(true);
                adapter.setButtonsEnabled(true);
                ((MainActivity) requireActivity()).setRoutineRunning(true);
                view.StartRoutineButton.setText("End Routine");
            } else if(activityModel.getRoutineStatus() == 1){
                activityModel.endRoutine();
                adapter.setButtonsEnabled(false);
                view.TotalElapsedTime.setText("Total Elapsed Time: " + activityModel.getTotalElapsedTimeToString());
                view.StartRoutineButton.setText("Ended Routine");
                view.StartRoutineButton.setEnabled(false);
                view.StopTimerButton.setEnabled(false);
                view.AdvanceTimerButton.setEnabled(false);
            }
            else if(activityModel.getRoutineStatus() == 2){
                activityModel.resetToRealTimer();
                activityModel.resetAllRoutines();
                ((MainActivity) requireActivity()).setRoutineRunning(false);
                MainActivity mainActivity = (MainActivity) requireActivity();

                mainActivity.swapFragmentRoutineList();
            }
        });

        // Initially disable the Advance Time button
        view.AdvanceTimerButton.setEnabled(false);

        // Stop Real Timer and Switch to Mock Timer
        view.StopTimerButton.setOnClickListener(v -> {
            activityModel.switchToMockTimer();
            view.AdvanceTimerButton.setEnabled(true);
            view.StopTimerButton.setEnabled(false);

        });

        // Manually Advance Time (only works in mock mode)
        view.AdvanceTimerButton.setOnClickListener(v -> {
            activityModel.advanceTime();
        });

        return view.getRoot();
    }

    public void refreshData() {
        adapter.clear();
        adapter.addAll(new ArrayList<>(activityModel.getCurrentRoutineTasks().getValue())); // remember the mutable copy here!
        adapter.notifyDataSetChanged();
    }
}
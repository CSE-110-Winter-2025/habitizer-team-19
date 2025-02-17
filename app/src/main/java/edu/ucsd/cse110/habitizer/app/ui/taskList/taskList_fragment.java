package edu.ucsd.cse110.habitizer.app.ui.taskList;

import static edu.ucsd.cse110.habitizer.lib.domain.RoutineRepository.rM;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.habitizer.app.MainActivity;
import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentTaskListBinding;
import edu.ucsd.cse110.habitizer.app.ui.taskList.dialog.confirmDeleteTaskDialogFragment;
import edu.ucsd.cse110.habitizer.lib.domain.RoutineRepository;

public class taskList_fragment extends Fragment {

    private MainViewModel activityModel;
    private FragmentTaskListBinding view;
    private taskList_adapter adapter;

    public taskList_fragment() {
        // Required empty public constructor
    }

    public static taskList_fragment newInstance(String selectedRoutine) {
        taskList_fragment fragment = new taskList_fragment();
        Bundle args = new Bundle();
        args.putString("selectedRoutine", selectedRoutine);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeViewModel();
        initializeAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = FragmentTaskListBinding.inflate(inflater, container, false);
        setupTaskList();
        setupRoutineButtons();
        return view.getRoot();
    }

    // Initialization Methods
    private void initializeViewModel() {
        if (getArguments() != null) {
            String selectedRoutine = getArguments().getString("selectedRoutine");

            var modelOwner = requireActivity();
            var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
            var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
            this.activityModel = modelProvider.get(MainViewModel.class);

            activityModel.setSelectedRoutine(selectedRoutine);
            observeTaskChanges(selectedRoutine);
        }
    }

    private void initializeAdapter() {
        this.adapter = new taskList_adapter(requireContext(), List.of(), taskName -> {
            var dialogFragment = confirmDeleteTaskDialogFragment.newInstance(taskName);
            dialogFragment.show(getParentFragmentManager(), "ConfirmDeleteTaskDialogFragment");
        });
    }

    private void observeTaskChanges(String selectedRoutine) {
        activityModel.getTasks(selectedRoutine).observe(tasks -> {
            if (tasks == null) return;
            adapter.clear();
            adapter.addAll(new ArrayList<>(tasks)); // Mutable copy
            adapter.notifyDataSetChanged();
        });
    }

    // UI Setup Methods
    private void setupTaskList() {
        view.taskList.setAdapter(adapter);
        view.StopTimerButton.setEnabled(false);

        adapter.setOnTaskComplete(totalTime -> view.TotalElapsedTime.setText("Total Elapsed Time: " + rM.getRoutineElapsedTimeString()));

        adapter.setOnAllTasksDone(() -> {
            if (rM.getRoutineStatus() == 1) {
                handleRoutineEnd();
            }
        });
    }

    private void setupRoutineButtons() {
        view.StartRoutineButton.setOnClickListener(v -> handleStartEndRoutine());
        view.StopTimerButton.setOnClickListener(v -> handleStopTimer());
        view.AdvanceTimerButton.setOnClickListener(v -> rM.advanceTime());
        view.AdvanceTimerButton.setEnabled(false);
    }

    private void handleRoutineEnd() {
        rM.endRoutine();
        adapter.setButtonsEnabled(false);
        view.TotalElapsedTime.setText("Total Elapsed Time: " + rM.getTotalElapsedTimeToString());
        view.StartRoutineButton.setText("Ended Routine");
        view.StartRoutineButton.setEnabled(false);
        view.StopTimerButton.setEnabled(false);
        view.AdvanceTimerButton.setEnabled(false);
    }

    private void handleStartEndRoutine() {
        if (rM.getRoutineStatus() == 0) {
            rM.startRoutine();
            view.StopTimerButton.setEnabled(true);
            adapter.setRemoveEnabled(false);
            adapter.setTimerEnabled(true);
            adapter.setButtonsEnabled(true);
            ((MainActivity) requireActivity()).setRoutineRunning(true);
            view.StartRoutineButton.setText("End Routine");

        } else if (rM.getRoutineStatus() == 1) {
            handleRoutineEnd();

        } else if (rM.getRoutineStatus() == 2) {
            rM.resetToRealTimer();
            rM.resetAllRoutines();
            ((MainActivity) requireActivity()).setRoutineRunning(false);
            ((MainActivity) requireActivity()).swapFragmentRoutineList();
        }
    }

    private void handleStopTimer() {
        rM.switchToMockTimer();
        view.AdvanceTimerButton.setEnabled(true);
        view.StopTimerButton.setEnabled(false);
    }

    // Data Refresh
    public void refreshData(String routineName) {
        adapter.clear();
        adapter.addAll(new ArrayList<>(rM.findRoutine(routineName).getValue().getTasks())); // Mutable copy
        adapter.notifyDataSetChanged();
    }
}
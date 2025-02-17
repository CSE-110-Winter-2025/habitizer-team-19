package edu.ucsd.cse110.habitizer.app.ui.routineList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.habitizer.app.MainActivity;
import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentRoutineListBinding;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;

public class routineList_fragment extends Fragment {

    private MainViewModel activityModel;
    private FragmentRoutineListBinding view;
    private routineList_adapter adapter;

    public routineList_fragment() {
        // Required empty public constructor
    }

    public static routineList_fragment newInstance() {
        routineList_fragment fragment = new routineList_fragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeViewModel();
        initializeAdapter();
        observeRoutineChanges();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = FragmentRoutineListBinding.inflate(inflater, container, false);
        setupRoutineList();
        return view.getRoot();
    }

    // Initialization Methods
    private void initializeViewModel() {
        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);
    }

    private void initializeAdapter() {
        this.adapter = new routineList_adapter(requireContext(), List.of());
    }

    private void observeRoutineChanges() {
        activityModel.getRoutines().observe(routines -> {
            if (routines == null) return;
            adapter.clear();
            adapter.addAll(new ArrayList<>(routines)); // Mutable copy
            adapter.notifyDataSetChanged();
        });
    }

    // UI Setup Methods
    private void setupRoutineList() {
        view.routineList.setAdapter(adapter);
        view.routineList.setOnItemClickListener((parent, view, position, id) -> onRoutineSelected(position));
    }

    private void onRoutineSelected(int position) {
        Routine selectedRoutine = adapter.getItem(position);
        if (selectedRoutine != null) {
            String selectedRoutineTitle = selectedRoutine.getName();
            String selectedRoutineGoalTime = "Goal Time: " + selectedRoutine.getGoalTimeToString();
            MainActivity mainActivity = (MainActivity) requireActivity();
            mainActivity.swapFragmentTaskList(selectedRoutineTitle, selectedRoutineGoalTime);
        }
    }
}

package edu.ucsd.cse110.habitizer.app.ui.routineList;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.habitizer.app.MainActivity;
import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.app.R;
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
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);

        this.adapter = new routineList_adapter(requireContext(), List.of());
        activityModel.getRoutines().observe(routines -> {
            if (routines == null) return;
            adapter.clear();
            adapter.addAll(new ArrayList<>(routines)); // remember the mutable copy here!
            adapter.notifyDataSetChanged();
        });;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,@Nullable ViewGroup container,
                            @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = FragmentRoutineListBinding.inflate(inflater, container, false);
        view.routineList.setAdapter(adapter);

        view.routineList.setOnItemClickListener((parent, view, position, id) -> {
            Routine selectedRoutine = adapter.getItem(position);
            if (selectedRoutine != null) {
                String selectedRoutineTitle = selectedRoutine.getName();
                MainActivity mainActivity = (MainActivity) requireActivity();

                mainActivity.swapFragmentTaskList(selectedRoutineTitle);
            }
        });

        return view.getRoot();
    }
}
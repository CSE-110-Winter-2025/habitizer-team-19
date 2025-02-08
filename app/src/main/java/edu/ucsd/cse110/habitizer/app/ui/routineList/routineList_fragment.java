package edu.ucsd.cse110.habitizer.app.ui.routineList;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.app.R;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentRoutineListBinding;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link routineList_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.view = FragmentRoutineListBinding.inflate(inflater, container, false);
        view.routineList.setAdapter(adapter);
        return view.getRoot();
    }
}
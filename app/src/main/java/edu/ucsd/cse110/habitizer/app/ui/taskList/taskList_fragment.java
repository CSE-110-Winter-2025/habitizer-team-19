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

import edu.ucsd.cse110.habitizer.app.MainActivity;
import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentTaskListBinding;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.RoutineRepository;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.util.Observer;


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


        if(getArguments() != null){
            String selectedRoutine = getArguments().getString("selectedRoutine");

            var modelOwner = requireActivity();
            var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
            var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
            this.activityModel = modelProvider.get(MainViewModel.class);

            this.adapter = new taskList_adapter(requireContext(), List.of());


            activityModel.getTasks(selectedRoutine).observe(tasks -> {
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
        
        view.StartRoutineButton.setOnClickListener(v -> {
            if(rM.getHasStarted()){
                rM.end();
                adapter.setButtonsEnabled(false);
                view.StartRoutineButton.setText("Start Routine");
            } else{
                rM.start();
                adapter.setButtonsEnabled(true);
                view.StartRoutineButton.setText("End Routine");
            }
        });
        return view.getRoot();


    }


}
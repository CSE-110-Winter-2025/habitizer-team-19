package edu.ucsd.cse110.habitizer.app.ui.taskList;

import static edu.ucsd.cse110.habitizer.lib.domain.RoutineRepository.*;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.habitizer.app.databinding.ListItemTaskBinding;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.RoutineRepository;
import edu.ucsd.cse110.habitizer.lib.domain.Task;

public class taskList_adapter extends ArrayAdapter<Task> {

    private boolean buttonsEnabled = false;

    public void setButtonsEnabled(boolean enabled){
        this.buttonsEnabled = enabled;
        notifyDataSetChanged();
    }

    public taskList_adapter(Context context, List<Task> tasks){
        super(context,0,new ArrayList<>(tasks));
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        var task = getItem(position);
        assert task != null;

        ListItemTaskBinding binding;

        if (convertView != null) {
            binding = ListItemTaskBinding.bind(convertView);
        } else {
            var layoutInflater = LayoutInflater.from(getContext());
            binding = ListItemTaskBinding.inflate(layoutInflater, parent, false);
        }

        binding.completeButton.setEnabled(buttonsEnabled);
        binding.skipButton.setEnabled(buttonsEnabled);

        binding.taskTitle.setText(task.getName());
        binding.taskTime.setText(task.getElapsedTimeToString());
        binding.completeButton.setOnClickListener(v->{
            task.complete();
            rM.setElapsedTime(task);
            binding.taskTime.setText(task.getElapsedTimeToString());
            binding.completeButton.setEnabled(false);
            binding.skipButton.setEnabled(false);
        });
        binding.skipButton.setOnClickListener(v->{
            task.skip();
            binding.skipButton.setEnabled(false);
        });


        return binding.getRoot();

    }


}
package edu.ucsd.cse110.habitizer.app.ui.taskList;

import static edu.ucsd.cse110.habitizer.lib.domain.RoutineRepository.*;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.habitizer.app.databinding.ListItemTaskBinding;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.RoutineRepository;
import edu.ucsd.cse110.habitizer.lib.domain.Task;

public class taskList_adapter extends ArrayAdapter<Task> {

    private boolean buttonsEnabled = false;
    private boolean removeEnabled = true;
    private boolean timerEnabled = false;

    Consumer<String> onDeleteClick;

    private Consumer<Long> onTaskComplete;

    private Runnable onAllTasksDone;

    public void setOnAllTasksDone(Runnable onAllTasksDone) {
        this.onAllTasksDone = onAllTasksDone;
    }

    public void setOnTaskComplete(Consumer<Long> onTaskComplete) {
        this.onTaskComplete = onTaskComplete;
    }

    private void checkAllTasksDone() {
        // If *any* task is still not completed or skipped, return early
        for (int i = 0; i < getCount(); i++) {
            Task t = getItem(i);
            if (t.getCompletionStatus() == 0) {
                return; // Means at least one task is still "in progress"
            }
        }

        // If we reach here, all tasks are done (either completed or skipped)
        if (onAllTasksDone != null) {
            onAllTasksDone.run();
        }
    }


    public void setRemoveEnabled(boolean enabled){
        this.removeEnabled = enabled;
    }
    public void setTimerEnabled(boolean enabled){
        this.timerEnabled = enabled;
    }
    public void setButtonsEnabled(boolean enabled){
        this.buttonsEnabled = enabled;
        notifyDataSetChanged();
    }

    public taskList_adapter(Context context, List<Task> tasks, Consumer<String> onDeleteClick){
        super(context,0,new ArrayList<>(tasks));
        this.onDeleteClick = onDeleteClick;
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



        binding.completeButton.setVisibility(buttonsEnabled ? View.VISIBLE : View.GONE);
        binding.skipButton.setVisibility(buttonsEnabled ? View.VISIBLE : View.GONE);
        binding.taskTime.setVisibility(timerEnabled ? View.VISIBLE : View.GONE);
        binding.taskDeleteButton.setVisibility(removeEnabled ? View.VISIBLE : View.GONE);

        binding.taskTitle.setText(task.getName());
        binding.taskTime.setText(task.getElapsedTimeToString());
        binding.completeButton.setOnClickListener(v->{
            task.complete();
            // rM.setElapsedTime(task);
            rM.completeTask(task);
            binding.taskTime.setText(task.getElapsedTimeToString());
            binding.taskTime.setVisibility(timerEnabled ? View.VISIBLE : View.GONE);
            binding.completeButton.setEnabled(false);
            binding.skipButton.setEnabled(false);

            if (onTaskComplete != null) {
                onTaskComplete.accept(rM.getTotalElapsedTime());
            }
            checkAllTasksDone();
        });
        binding.skipButton.setOnClickListener(v->{
            task.skip();
            binding.skipButton.setEnabled(false);
            checkAllTasksDone();
        });
        binding.taskDeleteButton.setOnClickListener(v -> {
            var name = task.getName();
            onDeleteClick.accept(name);
            notifyDataSetChanged();
        });

        return binding.getRoot();

    }

}
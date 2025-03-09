package edu.ucsd.cse110.habitizer.app.ui.taskList;



import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.app.databinding.ListItemTaskBinding;
import edu.ucsd.cse110.habitizer.lib.domain.Task;

public class taskList_adapter extends ArrayAdapter<Task> {

    private MainViewModel activityModel;
    private boolean buttonsEnabled = false;
    private boolean removeEnabled = true;
    private boolean timerEnabled = false;
    private Consumer<Integer> onDeleteClick;
    private Consumer<Long> onTaskComplete;
    private Runnable onAllTasksDone;
    private boolean paused = false;

    public taskList_adapter(Context context, List<Task> tasks, MainViewModel activityModel, Consumer<Integer> onDeleteClick) {
        super(context, 0, new ArrayList<>(tasks));
        this.activityModel = activityModel;
        this.onDeleteClick = onDeleteClick;
    }

    // Setters for Callbacks
    public void setOnAllTasksDone(Runnable onAllTasksDone) {
        this.onAllTasksDone = onAllTasksDone;
    }

    public void setOnTaskComplete(Consumer<Long> onTaskComplete) {
        this.onTaskComplete = onTaskComplete;
    }

    // UI State Setters
    public void setRemoveEnabled(boolean enabled) {
        this.removeEnabled = enabled;
    }

    public void setTimerEnabled(boolean enabled) {
        this.timerEnabled = enabled;
    }

    public void setButtonsEnabled(boolean enabled) {
        this.buttonsEnabled = enabled;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        var task = getItem(position);
        assert task != null;


        ListItemTaskBinding binding = getBinding(convertView, parent);
        updateTaskUI(binding, task);
        setupClickListeners(binding, task);

        return binding.getRoot();
    }

    // Inflate or Bind View
    private ListItemTaskBinding getBinding(View convertView, ViewGroup parent) {
        if (convertView != null) {
            return ListItemTaskBinding.bind(convertView);
        } else {
            var layoutInflater = LayoutInflater.from(getContext());
            return ListItemTaskBinding.inflate(layoutInflater, parent, false);
        }
    }

    // Update UI based on state
    private void updateTaskUI(ListItemTaskBinding binding, Task task) {
        binding.taskTitle.setText(task.getName());
        binding.taskTime.setText(task.getElapsedTimeToString());

        binding.completeButton.setVisibility(buttonsEnabled ? View.VISIBLE : View.GONE);
        binding.completeButton.setEnabled(!paused);
        binding.skipButton.setVisibility(buttonsEnabled ? View.VISIBLE : View.GONE);
        binding.taskTime.setVisibility(timerEnabled ? View.VISIBLE : View.GONE);
        binding.taskDeleteButton.setVisibility(removeEnabled ? View.VISIBLE : View.GONE);

        binding.taskMoveUpButton.setVisibility(buttonsEnabled ? View.GONE : View.VISIBLE);
        binding.taskMoveDownButton.setVisibility(buttonsEnabled ? View.GONE : View.VISIBLE);

    }

    // Setup Click Listeners
    private void setupClickListeners(ListItemTaskBinding binding, Task task) {
        binding.taskTitle.setOnClickListener(v -> showRenameTaskDialog(task));
        binding.completeButton.setOnClickListener(v -> completeTask(binding, task));
        binding.skipButton.setOnClickListener(v -> skipTask(binding, task));
        binding.taskDeleteButton.setOnClickListener(v -> deleteTask(task));
        binding.taskMoveUpButton.setOnClickListener(v -> moveTaskUp(task));
        binding.taskMoveDownButton.setOnClickListener(v -> moveTaskDown(task));
    }

    // Rename Task Dialog
    private void showRenameTaskDialog(Task task) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Rename Task");

        final EditText input = new EditText(getContext());
        input.setText(task.getName());
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            String newName = input.getText().toString().trim();
            if (!newName.isEmpty() && !newName.equals(task.getName())) {
                task.setName(newName);
                notifyDataSetChanged();
            } else {
                Toast.makeText(getContext(), "Invalid name", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    // Complete Task
    private void completeTask(@NonNull ListItemTaskBinding binding, @NonNull Task task) {
        System.out.println("Loading data: " + task.getName());
        activityModel.completeTask(task);

        binding.taskTime.setText(task.getElapsedTimeToString());
        binding.taskTime.setVisibility(timerEnabled ? View.VISIBLE : View.GONE);
        binding.completeButton.setEnabled(false);
        binding.skipButton.setEnabled(false);

        if (onTaskComplete != null) {
            onTaskComplete.accept(activityModel.getTotalElapsedTime());
        }
        checkAllTasksDone();
    }

    // Skip Task
    private void skipTask(@NonNull ListItemTaskBinding binding,@NonNull Task task) {
        activityModel.skipTask(task);

        binding.skipButton.setEnabled(false);
        checkAllTasksDone();
    }

    public void Pause(boolean status) {
        paused = status;
        notifyDataSetChanged();
    }

    // Delete Task
    private void deleteTask(@NonNull Task task) {
        onDeleteClick.accept(task.id());
        notifyDataSetChanged();
    }

    // Check if all tasks are completed or skipped
    private void checkAllTasksDone() {
        for (int i = 0; i < getCount(); i++) {
            Task t = getItem(i);
            assert t != null;
            if (t.getCompletionStatus() == 0) {
                return;
            }
        }
        if (onAllTasksDone != null) {
            onAllTasksDone.run();
        }
    }

    private void moveTaskUp(Task task){
        activityModel.moveTaskUp(task.id());
    }

    private void moveTaskDown(Task task){
        activityModel.moveTaskDown(task.id());
    }

}
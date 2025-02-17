package edu.ucsd.cse110.habitizer.app.ui.routineList;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.habitizer.app.MainActivity;
import edu.ucsd.cse110.habitizer.app.databinding.ListItemRoutineBinding;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;

public class routineList_adapter extends ArrayAdapter<Routine> {

    public routineList_adapter(Context context, List<Routine> routines) {
        super(context, 0, new ArrayList<>(routines));
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        var routine = getItem(position);
        assert routine != null;

        ListItemRoutineBinding binding = getBinding(convertView, parent);
        updateRoutineUI(binding, routine);
        setupClickListeners(binding, routine);

        return binding.getRoot();
    }

    // Inflate or Bind View
    private ListItemRoutineBinding getBinding(View convertView, ViewGroup parent) {
        if (convertView != null) {
            return ListItemRoutineBinding.bind(convertView);
        } else {
            var layoutInflater = LayoutInflater.from(getContext());
            return ListItemRoutineBinding.inflate(layoutInflater, parent, false);
        }
    }

    // Update UI with routine details
    private void updateRoutineUI(ListItemRoutineBinding binding, Routine routine) {
        binding.routineTitle.setText(routine.getName());
        binding.goalTime.setText("Goal Time: " + routine.getGoalTimeToString());
    }

    // Setup Click Listeners for Routine Selection and Editing
    private void setupClickListeners(ListItemRoutineBinding binding, Routine routine) {
        binding.routineTitle.setOnClickListener(v -> navigateToTaskList(routine));
        binding.goalTime.setOnClickListener(v -> showEditGoalTimeDialog(routine));
    }

    // Navigate to Task List for the selected routine
    private void navigateToTaskList(Routine routine) {
        ((MainActivity) getContext()).swapFragmentTaskList(
                routine.getName(), "Goal Time: " + routine.getGoalTimeToString()
        );
    }

    // Show dialog to edit the routine goal time
    private void showEditGoalTimeDialog(Routine routine) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Edit Goal Time");

        final EditText input = new EditText(getContext());
        input.setHint("HH:MM:SS");
        input.setText(routine.getGoalTimeToString());
        builder.setView(input);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String newTimeStr = input.getText().toString().trim();
            if (isValidTimeFormat(newTimeStr)) {
                long newTime = convertTimeStringToSeconds(newTimeStr);
                routine.setGoalTime(newTime);
                notifyDataSetChanged();
            } else {
                Toast.makeText(getContext(), "Invalid time format (use HH:MM:SS)", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    // Validate time format
    private boolean isValidTimeFormat(String time) {
        return time.matches("^\\d{2}:\\d{2}:\\d{2}$");
    }

    // Convert time string (HH:MM:SS) to seconds
    private long convertTimeStringToSeconds(String time) {
        String[] parts = time.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        int seconds = Integer.parseInt(parts[2]);
        return (hours * 3600) + (minutes * 60) + seconds;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}

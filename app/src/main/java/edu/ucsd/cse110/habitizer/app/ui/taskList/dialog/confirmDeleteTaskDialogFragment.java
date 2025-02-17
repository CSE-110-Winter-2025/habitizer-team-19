package edu.ucsd.cse110.habitizer.app.ui.taskList.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentDialogCreateTaskBinding;
import edu.ucsd.cse110.habitizer.lib.domain.Task;

public class confirmDeleteTaskDialogFragment extends DialogFragment {
    public interface DialogListener {
        void onDialogPositiveClick();
    }

    private FragmentDialogCreateTaskBinding view;

    private MainViewModel activityModel;

    private createTaskDialogFragment.DialogListener listener;
    private static final String ARG_TASK_NAME = "task_name";
    private String taskName;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (createTaskDialogFragment.DialogListener) context; // Ensure the Activity implements the interface
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement DialogListener");
        }
    }

    confirmDeleteTaskDialogFragment() {

    }

    public static confirmDeleteTaskDialogFragment newInstance(String taskName) {
        var fragment = new confirmDeleteTaskDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TASK_NAME, taskName);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        this.view = FragmentDialogCreateTaskBinding.inflate(getLayoutInflater());

        return new AlertDialog.Builder(getActivity())
                .setTitle("Delete Task")
                .setMessage("Are you sure you want to delete this task?")
                .setPositiveButton("Delete", this::onPositiveButtonClick)
                .setNegativeButton("Cancel", this::onNegativeButtonClick)
                .create();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.taskName = requireArguments().getString(ARG_TASK_NAME);
        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);
    }

    private void onPositiveButtonClick(DialogInterface dialog, int which) {
        activityModel.removeTask(taskName);
        if (listener != null) {
            listener.onDialogPositiveClick(); // Notify the listener
        }
        dialog.dismiss();
    }

    private void onNegativeButtonClick(DialogInterface dialog, int which) {
        dialog.cancel();
    }
}

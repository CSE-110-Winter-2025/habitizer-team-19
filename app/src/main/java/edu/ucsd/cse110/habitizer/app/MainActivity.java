package edu.ucsd.cse110.habitizer.app;

import static edu.ucsd.cse110.habitizer.lib.domain.RoutineRepository.rM;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import edu.ucsd.cse110.habitizer.app.databinding.ActivityMainBinding;
import edu.ucsd.cse110.habitizer.app.ui.routineList.routineList_fragment;
import edu.ucsd.cse110.habitizer.app.ui.taskList.dialog.confirmDeleteTaskDialogFragment;
import edu.ucsd.cse110.habitizer.app.ui.taskList.dialog.createTaskDialogFragment;
import edu.ucsd.cse110.habitizer.app.ui.taskList.taskList_fragment;

public class MainActivity extends AppCompatActivity implements createTaskDialogFragment.DialogListener, confirmDeleteTaskDialogFragment.DialogListener {

    private boolean isTaskListFragmentVisible = false;
    private boolean routineRunning = false;
    private String selectedRoutine = null;
    private Toolbar toolbar;
    private TextView toolbarTitle;
    private TextView toolbarSubtitle;
    private ActivityMainBinding view;
    private Menu mMenu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.view = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(view.getRoot());

        initializeToolbar();
        swapFragmentRoutineList();
    }

    // Toolbar Initialization
    private void initializeToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Routines");

        toolbarSubtitle = findViewById(R.id.toolbar_subtitle);
        toolbarSubtitle.setVisibility(View.GONE);

        updateBackButtonVisibility();
    }

    // Fragment Management
    public void swapFragmentTaskList(@NonNull String selectedRoutineTitle, @NonNull String selectedRoutineGoalTime) {
        isTaskListFragmentVisible = true;
        selectedRoutine = selectedRoutineTitle;

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, taskList_fragment.newInstance(selectedRoutineTitle))
                .commit();

        toolbarTitle.setText(selectedRoutineTitle);
        toolbarSubtitle.setText(selectedRoutineGoalTime);
        toolbarSubtitle.setVisibility(View.VISIBLE);

        if (mMenu != null) {
            mMenu.findItem(R.id.action_bar_add_task).setVisible(true);
        }

        updateBackButtonVisibility();
    }

    public void swapFragmentRoutineList() {
        isTaskListFragmentVisible = false;
        selectedRoutine = null;

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, routineList_fragment.newInstance())
                .commit();

        toolbarTitle.setText("Routines");
        toolbarSubtitle.setVisibility(View.GONE);

        updateBackButtonVisibility();
    }

    public void setRoutineRunning(boolean val) {
        routineRunning = val;
        updateBackButtonVisibility();
    }

    // Toolbar Menu Management
    private void updateBackButtonVisibility() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(isTaskListFragmentVisible);
        }

        if (mMenu != null) {
            boolean shouldShowAddButton = isTaskListFragmentVisible && !routineRunning;
            mMenu.findItem(R.id.action_bar_add_task).setVisible(shouldShowAddButton || selectedRoutine != null);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);
        mMenu = menu;
        updateBackButtonVisibility();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            swapFragmentRoutineList();
            rM.resetAllRoutines();
            rM.resetToRealTimer();
            return true;
        } else if (item.getItemId() == R.id.action_bar_add_task) {
            var dialogFragment = createTaskDialogFragment.newInstance();
            dialogFragment.show(getSupportFragmentManager(), "createTaskDialogFragment");
        }
        return super.onOptionsItemSelected(item);
    }

    // Dialog Handling
    @Override
    public void onDialogPositiveClick() {
        taskList_fragment fragment = (taskList_fragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment != null) {
            fragment.refreshData(selectedRoutine);
        }
    }
}

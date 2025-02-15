package edu.ucsd.cse110.habitizer.app;

import static edu.ucsd.cse110.habitizer.lib.domain.RoutineRepository.rM;

import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import edu.ucsd.cse110.habitizer.app.databinding.ActivityMainBinding;
import edu.ucsd.cse110.habitizer.app.ui.routineList.routineList_fragment;
import edu.ucsd.cse110.habitizer.app.ui.taskList.dialog.createTaskDialogFragment;
import edu.ucsd.cse110.habitizer.app.ui.taskList.taskList_fragment;

public class MainActivity extends AppCompatActivity implements createTaskDialogFragment.DialogListener {

    private boolean isTaskListFragmentVisible = false;
    private Toolbar toolbar;
    private TextView toolbarTitle;

    private TextView toolbarSubtitle;
    private ActivityMainBinding view;

    private Menu mMenu;
    private boolean routineRunning = false;

    private String selectedRoutine = null;



    private void updateBackButtonVisibility() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(isTaskListFragmentVisible);
        if(mMenu != null) {
            mMenu.findItem(R.id.action_bar_add_task).setVisible(isTaskListFragmentVisible && !routineRunning);
        }
    }
    public void swapFragmentTaskList(@NonNull String selectedRoutineTitle, @NonNull String selectedRoutineGoalTime) {
        isTaskListFragmentVisible = true;
        selectedRoutine = selectedRoutineTitle;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, taskList_fragment.newInstance(selectedRoutineTitle))
                .commit();

        toolbarTitle.setText(selectedRoutineTitle);
        toolbarSubtitle.setVisibility(View.VISIBLE);
        toolbarSubtitle.setText(selectedRoutineGoalTime);
        updateBackButtonVisibility();
    }

    public void swapFragmentRoutineList(){
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.view = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(view.getRoot());

        // Initialize Toolbar and Title
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);

            toolbarSubtitle = findViewById(R.id.toolbar_subtitle);
            toolbarSubtitle.setVisibility(View.GONE);
        }

        updateBackButtonVisibility();

        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Routines");

        // Initial Fragment
        swapFragmentRoutineList();
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
            rM.resetRoutines();
            return true;
        } else if (item.getItemId() == R.id.action_bar_add_task) {
            var dialogFragment = createTaskDialogFragment.newInstance();
            dialogFragment.show(getSupportFragmentManager(), "createTaskDialogFragment");
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onDialogPositiveClick() {
        taskList_fragment fragment = (taskList_fragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment != null) {
            fragment.refreshData(selectedRoutine);
        }
    }
}


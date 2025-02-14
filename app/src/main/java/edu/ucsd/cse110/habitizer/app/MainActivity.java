package edu.ucsd.cse110.habitizer.app;

import static edu.ucsd.cse110.habitizer.lib.domain.RoutineRepository.rM;

import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import edu.ucsd.cse110.habitizer.app.databinding.ActivityMainBinding;
import edu.ucsd.cse110.habitizer.app.ui.routineList.routineList_fragment;
import edu.ucsd.cse110.habitizer.app.ui.taskList.taskList_fragment;

public class MainActivity extends AppCompatActivity {

    private boolean isTaskListFragmentVisible = false;
    private Toolbar toolbar;
    private TextView toolbarTitle;

    private TextView toolbarSubtitle;
    private ActivityMainBinding view;

    private void updateBackButtonVisibility() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(isTaskListFragmentVisible);

    }
    public void swapFragmentTaskList(@NonNull String selectedRoutineTitle, @NonNull String selectedRoutineGoalTime) {
        isTaskListFragmentVisible = true;

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, taskList_fragment.newInstance(selectedRoutineTitle))
                .commit();

        toolbarTitle.setText(selectedRoutineTitle);
        toolbarSubtitle.setVisibility(View.VISIBLE);
        toolbarSubtitle.setText(selectedRoutineGoalTime);
        updateBackButtonVisibility();
    }

    private void swapFragmentRoutineList(){
        isTaskListFragmentVisible = false;

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, routineList_fragment.newInstance())
                .commit();

        toolbarTitle.setText("Routines");
        toolbarSubtitle.setVisibility(View.GONE);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            swapFragmentRoutineList();
            rM.resetRoutines();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}


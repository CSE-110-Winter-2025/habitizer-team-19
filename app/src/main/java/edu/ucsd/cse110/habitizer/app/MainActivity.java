package edu.ucsd.cse110.habitizer.app;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import edu.ucsd.cse110.habitizer.app.databinding.ActivityMainBinding;
import edu.ucsd.cse110.habitizer.app.ui.routineList.routineList_fragment;
import edu.ucsd.cse110.habitizer.app.ui.taskList.taskList_fragment;

public class MainActivity extends AppCompatActivity {

    private boolean isTaskListFragmentVisible = false;
    private Toolbar toolbar;
    private TextView toolbarTitle;
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

        toolbarTitle.setText(selectedRoutineTitle + " " + selectedRoutineGoalTime);
        updateBackButtonVisibility();
    }

    private void swapFragmentRoutineList(){
        isTaskListFragmentVisible = false;

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, routineList_fragment.newInstance())
                .commit();

        toolbarTitle.setText(R.string.app_title);
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
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        updateBackButtonVisibility();

        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText(R.string.app_title);

        // Initial Fragment
        swapFragmentRoutineList();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            swapFragmentRoutineList();
//            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}


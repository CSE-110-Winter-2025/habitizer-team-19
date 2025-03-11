package edu.ucsd.cse110.habitizer.app;



import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.List;

import edu.ucsd.cse110.habitizer.app.databinding.ActivityMainBinding;
import edu.ucsd.cse110.habitizer.app.ui.routineList.routineList_fragment;
import edu.ucsd.cse110.habitizer.app.ui.taskList.dialog.confirmDeleteTaskDialogFragment;
import edu.ucsd.cse110.habitizer.app.ui.taskList.dialog.createTaskDialogFragment;
import edu.ucsd.cse110.habitizer.app.ui.taskList.taskList_fragment;
import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;

public class MainActivity extends AppCompatActivity implements createTaskDialogFragment.DialogListener, confirmDeleteTaskDialogFragment.DialogListener {

    private boolean isTaskListFragmentVisible = false;

    private Toolbar toolbar;
    private TextView toolbarTitle;
    private TextView toolbarSubtitle;
    private ActivityMainBinding view;
    private Menu mMenu;
    private MainViewModel activityModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.view = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(view.getRoot());

        var modelOwner = this;
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        activityModel = modelProvider.get(MainViewModel.class);

        initializeToolbar();

        //Load running routine if possible
        try (FileInputStream fis = this.getApplicationContext().openFileInput("runData");
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            String runningName = (String) ois.readObject();
            List<Routine> routineList = activityModel.getRoutines().getValue();
            Routine routine = null;
            for(Routine r: routineList) {
                if(r.getName().equals(runningName)) {
                    routine = r;
                    break;
                }
            }
            if(routine != null) {
                swapFragmentTaskList(routine.id(), routine.getName(), "Goal Time: " + routine.getGoalTimeToString());
            } else {
                swapFragmentRoutineList();
            }
        } catch (Exception e) {
            e.printStackTrace();
            swapFragmentRoutineList();
        }

    }

    @Override
    protected void onStop() {
        for(int x = 0;x<10;x++) {
            Log.e("SLURP", "GAHHHH");
        }
        activityModel.saveRoutineList(this);
        super.onStop();
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
    public void swapFragmentTaskList(Integer id,@NonNull String selectedRoutineTitle, @NonNull String selectedRoutineGoalTime) {
        isTaskListFragmentVisible = true;
        activityModel.setSelectedRoutine(selectedRoutineTitle);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, taskList_fragment.newInstance(id))
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
        activityModel.setSelectedRoutine(null);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, routineList_fragment.newInstance())
                .commit();

        toolbarTitle.setText("Routines");
        toolbarSubtitle.setVisibility(View.GONE);

        if(mMenu != null){
            mMenu.findItem(R.id.action_bar_add_routine).setVisible(true);
        }

        updateBackButtonVisibility();
    }

    public void setRoutineRunning(boolean val) {
        activityModel.setRoutineRunning(val);
        updateBackButtonVisibility();
    }

    // Toolbar Menu Management
    private void updateBackButtonVisibility() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(isTaskListFragmentVisible);
        }

        if (mMenu != null) {
            boolean shouldShowAddTaskButton = isTaskListFragmentVisible && !activityModel.isRoutineRunning();
            boolean shouldShowAddRoutineButton = !isTaskListFragmentVisible;
            mMenu.findItem(R.id.action_bar_add_task).setVisible(shouldShowAddTaskButton || activityModel.getSelectedRoutine() != null);
            mMenu.findItem(R.id.action_bar_add_routine).setVisible(shouldShowAddRoutineButton || activityModel.getSelectedRoutine() == null);
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
            activityModel.resetToRealTimer();
            activityModel.resetAllRoutines();
            swapFragmentRoutineList();
            return true;
        } else if (item.getItemId() == R.id.action_bar_add_task) {
            var dialogFragment = createTaskDialogFragment.newInstance();
            dialogFragment.show(getSupportFragmentManager(), "createTaskDialogFragment");
        } else if (item.getItemId() == R.id.action_bar_add_routine) {
            activityModel.addRoutine();
        }
        return super.onOptionsItemSelected(item);
    }

    // Dialog Handling
    @Override
    public void onDialogPositiveClick() {
        taskList_fragment fragment = (taskList_fragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment != null) {
            fragment.refreshData();
        }
    }
}

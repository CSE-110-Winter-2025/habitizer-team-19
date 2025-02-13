package edu.ucsd.cse110.habitizer.app;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import edu.ucsd.cse110.habitizer.app.databinding.ActivityMainBinding;
import edu.ucsd.cse110.habitizer.app.ui.routineList.routineList_fragment;
import edu.ucsd.cse110.habitizer.app.ui.taskList.taskList_fragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding view;

    public void swapFragmentTaskList(@NonNull String selectedRoutineTitle) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, taskList_fragment.newInstance(selectedRoutineTitle))
                .commit();

    }

    private void swapFragmentRoutineList(){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, routineList_fragment.newInstance())
                .commit();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_title);

        this.view = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(view.getRoot());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


}


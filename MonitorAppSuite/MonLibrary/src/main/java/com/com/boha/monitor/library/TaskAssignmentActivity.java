package com.com.boha.monitor.library;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.dto.ProjectSiteDTO;
import com.com.boha.monitor.library.dto.ProjectSiteTaskDTO;
import com.com.boha.monitor.library.fragments.TaskAssignmentFragment;

public class TaskAssignmentActivity extends FragmentActivity implements TaskAssignmentFragment.ProjectSiteTaskListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_assignment);

        ProjectSiteDTO site = (ProjectSiteDTO)getIntent()
                .getSerializableExtra("projectSite");
        TaskAssignmentFragment taf = (TaskAssignmentFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment);
        taf.setProjectSite(site);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.task_assignment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTaskClicked(ProjectSiteTaskDTO task) {

    }

    @Override
    public void onProjectSiteTaskAdded(ProjectSiteTaskDTO task) {

    }

    @Override
    public void onProjectSiteTaskDeleted() {

    }
}

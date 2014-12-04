package com.com.boha.monitor.library;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.dto.ProjectSiteTaskDTO;
import com.com.boha.monitor.library.dto.ProjectSiteTaskStatusDTO;
import com.com.boha.monitor.library.fragments.SubTaskStatusAssignmentFragment;

public class SubTaskStatusAssignmentActivity extends ActionBarActivity {

    ProjectSiteTaskDTO projectSiteTask;
    ProjectSiteTaskStatusDTO projectSiteTaskStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subtask_status);
        subTaskStatusAssignmentFragment = (SubTaskStatusAssignmentFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment);
        projectSiteTask = (ProjectSiteTaskDTO)getIntent().getSerializableExtra("projectSiteTask");
        projectSiteTaskStatus = (ProjectSiteTaskStatusDTO)getIntent().getSerializableExtra("projectSiteTaskStatus");
        subTaskStatusAssignmentFragment.setProjectSiteTask(projectSiteTask, projectSiteTaskStatus);

        setTitle(projectSiteTask.getProjectSiteName());
        getSupportActionBar().setSubtitle(projectSiteTask.getProjectName());
    }


    SubTaskStatusAssignmentFragment subTaskStatusAssignmentFragment;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sub_task_status, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

package com.com.boha.monitor.library;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.dialogs.StatusDialog;
import com.com.boha.monitor.library.dto.ProjectSiteDTO;
import com.com.boha.monitor.library.dto.ProjectSiteTaskDTO;
import com.com.boha.monitor.library.dto.ProjectSiteTaskStatusDTO;
import com.com.boha.monitor.library.dto.transfer.PhotoUploadDTO;
import com.com.boha.monitor.library.fragments.TaskAssignmentFragment;
import com.com.boha.monitor.library.util.ToastUtil;

public class TaskAssignmentActivity extends ActionBarActivity implements
        TaskAssignmentFragment.ProjectSiteTaskListener{

    Context ctx;
    ProjectSiteDTO site;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_assignment);
        ctx = getApplicationContext();
         site = (ProjectSiteDTO)getIntent()
                .getSerializableExtra("projectSite");
        int type = getIntent().getIntExtra("type", TaskAssignmentFragment.OPERATIONS);

         taf = (TaskAssignmentFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment);
        taf.setProjectSite(site, type);
        setTitle(site.getProjectSiteName());
        getActionBar().setSubtitle(site.getProjectName());
    }

    TaskAssignmentFragment taf;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.task_assignment, menu);
        mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {
            taf.openTaskPane();
            return true;
        }
        if (id == R.id.action_help) {
            ToastUtil.toast(ctx, ctx.getString(R.string.under_cons));
            return true;
        }
        if (id == R.id.action_camera) {
            Intent i = new Intent(this,PictureActivity.class);
            i.putExtra("projectSite",site);
            i.putExtra("type", PhotoUploadDTO.SITE_IMAGE);
            startActivity(i);
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

    @Override
    public void onStatusDialogRequested(ProjectSiteDTO projectSite, ProjectSiteTaskDTO siteTask) {
        StatusDialog d = new StatusDialog();
        d.setProjectSite(projectSite);
        d.setProjectSiteTask(siteTask);
        d.setContext(getApplicationContext());
        d.setListener(new StatusDialog.StatusDialogListener() {
            @Override
            public void onStatusAdded(ProjectSiteTaskStatusDTO taskStatus) {
                taf.updateList(taskStatus);
            }

            @Override
            public void onError(String message) {

            }
        });
        d.show(getFragmentManager(),"DIAG_STATUS");
    }

    @Override
    public void setBusy() {
        setRefreshActionButtonState(true);
    }

    @Override
    public void setNotBusy() {
        setRefreshActionButtonState(false);
    }

    @Override
    public void onCameraRequested(ProjectSiteTaskDTO siteTask, int type) {
        Intent i = new Intent(ctx, PictureActivity.class);
        i.putExtra("type", type);
        i.putExtra("projectSiteTask", siteTask);
        startActivity(i);
    }

    private Menu mMenu;
    public void setRefreshActionButtonState(final boolean refreshing) {
        if (mMenu != null) {
            final MenuItem refreshItem = mMenu.findItem(R.id.action_help);
            if (refreshItem != null) {
                if (refreshing) {
                    refreshItem.setActionView(R.layout.action_bar_progess);
                } else {
                    refreshItem.setActionView(null);
                }
            }
        }
    }

}

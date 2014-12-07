package com.com.boha.monitor.library;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.dialogs.StatusDialog;
import com.com.boha.monitor.library.dto.ProjectSiteDTO;
import com.com.boha.monitor.library.dto.ProjectSiteTaskDTO;
import com.com.boha.monitor.library.dto.ProjectSiteTaskStatusDTO;
import com.com.boha.monitor.library.dto.transfer.PhotoUploadDTO;
import com.com.boha.monitor.library.dto.transfer.RequestDTO;
import com.com.boha.monitor.library.dto.transfer.ResponseDTO;
import com.com.boha.monitor.library.fragments.SiteTaskAndStatusAssignmentFragment;
import com.com.boha.monitor.library.util.ErrorUtil;
import com.com.boha.monitor.library.util.Statics;
import com.com.boha.monitor.library.util.ToastUtil;
import com.com.boha.monitor.library.util.WebSocketUtil;

public class TaskAssignmentActivity extends ActionBarActivity implements
        SiteTaskAndStatusAssignmentFragment.ProjectSiteTaskListener{

    Context ctx;
    ProjectSiteDTO site;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_assignment);
        ctx = getApplicationContext();
         site = (ProjectSiteDTO)getIntent()
                .getSerializableExtra("projectSite");
        int type = getIntent().getIntExtra("type", SiteTaskAndStatusAssignmentFragment.OPERATIONS);

         taf = (SiteTaskAndStatusAssignmentFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment);
        taf.setProjectSite(site, type);
        setTitle(site.getProjectSiteName());
        getSupportActionBar().setSubtitle(site.getProjectName());
        getSiteStatus();
    }

    private void getSiteStatus() {
        RequestDTO w = new RequestDTO(RequestDTO.GET_SITE_STATUS);
        w.setProjectSiteID(site.getProjectSiteID());

        WebSocketUtil.sendRequest(ctx, Statics.COMPANY_ENDPOINT,w,new WebSocketUtil.WebSocketListener() {
            @Override
            public void onMessage(final ResponseDTO response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!ErrorUtil.checkServerError(ctx,response)) {
                            return;
                        }
                        taf.setProjectSiteTaskList(response.getProjectSiteTaskList());
                    }
                });
            }

            @Override
            public void onClose() {

            }

            @Override
            public void onError(final String message) {
                Log.e("TaskAssignmentActivity", "---- ERROR websocket - " + message);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.errorToast(ctx,message);
                    }
                });
            }
        });
    }
    SiteTaskAndStatusAssignmentFragment taf;
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
            taf.popupTaskList();
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
    public void onSubTaskListRequested(ProjectSiteTaskDTO task, ProjectSiteTaskStatusDTO taskStatus) {
        if (task == null) throw new UnsupportedOperationException("##onSubTaskListRequested, ProjectSiteTaskDTO is null");
        Intent i = new Intent(ctx, SubTaskStatusAssignmentActivity.class);
        task.setProjectSiteName(site.getProjectSiteName());
        task.setProjectName(site.getProjectName());
        i.putExtra("projectSiteTask", task);
        i.putExtra("projectSiteTaskStatus",taskStatus);
        startActivityForResult(i,SUBTASK_ASSIGNMENT);
    }

    static final int SUBTASK_ASSIGNMENT = 11413;
    @Override
    public void onActivityResult(int reqCode,int resCode, Intent data) {
        switch (reqCode) {
            case SUBTASK_ASSIGNMENT:
                if (resCode == RESULT_OK) {
                    getSiteStatus();
                }
                break;
        }
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
            public void onError(final String message) {
                Log.e("TaskAssignmentActivity", "---- ERROR websocket - " + message);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.errorToast(ctx,message);
                    }
                });
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

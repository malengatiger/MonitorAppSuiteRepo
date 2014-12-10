package com.com.boha.monitor.library.dialogs;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.adapters.SpinnerListAdapter;
import com.com.boha.monitor.library.dto.ProjectSiteDTO;
import com.com.boha.monitor.library.dto.ProjectSiteTaskDTO;
import com.com.boha.monitor.library.dto.ProjectSiteTaskStatusDTO;
import com.com.boha.monitor.library.dto.TaskStatusDTO;
import com.com.boha.monitor.library.dto.transfer.RequestDTO;
import com.com.boha.monitor.library.dto.transfer.ResponseDTO;
import com.com.boha.monitor.library.util.CacheUtil;
import com.com.boha.monitor.library.util.ErrorUtil;
import com.com.boha.monitor.library.util.SharedUtil;
import com.com.boha.monitor.library.util.Statics;
import com.com.boha.monitor.library.util.WebSocketUtil;

import java.util.ArrayList;
import java.util.List;

import static com.com.boha.monitor.library.util.Util.showToast;

/**
 * Created by aubreyM on 2014/10/18.
 */
public class StatusDialog extends DialogFragment {
    public interface StatusDialogListener {
        public void onStatusAdded(ProjectSiteTaskStatusDTO projectSiteTaskStatus);
        public void onError(String message);
    }
    private List<TaskStatusDTO> taskStatusList;
    private TaskStatusDTO taskStatus;
    private ProjectSiteDTO projectSite;
    private ProjectSiteTaskDTO projectSiteTask;
    StatusDialogListener listener;
    Context context;
    TextView txtProjectSite, txtTaskName;
    ProgressBar progressBar;
    Button btnCancel, btnSave;
    ProjectSiteTaskStatusDTO projectSiteTaskStatus;
    View view;
    Spinner spinner;
    static final String LOG = StatusDialog.class.getSimpleName();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.status_selection_dialog, container);

        txtProjectSite = (TextView)view.findViewById(R.id.SSD_siteName);
        txtTaskName = (TextView)view.findViewById(R.id.SSD_taskName);

        progressBar = (ProgressBar) view.findViewById(R.id.SSD_progress);
        progressBar.setVisibility(View.GONE);


        btnCancel = (Button) view.findViewById(R.id.SSD_btnCancel);
        btnSave = (Button) view.findViewById(R.id.SSD_btnSave);
        spinner = (Spinner)view.findViewById(R.id.SSD_spinner);
        txtProjectSite.setText(projectSite.getProjectSiteName());
        txtTaskName.setText(projectSiteTask.getTask().getTaskName());
        getDialog().setTitle(context.getString(R.string.app_name));
        setSpinner();

        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dismiss();

            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
               sendStatusRequest();
            }
        });
        return view;
    }


    private void setSpinner() {

        CacheUtil.getCachedData(context,CacheUtil.CACHE_DATA,new CacheUtil.CacheUtilListener() {
            @Override
            public void onFileDataDeserialized(ResponseDTO response) {
                if (response != null) {
                    taskStatusList = response.getCompany().getTaskStatusList();
                    List<String> names = new ArrayList<String>();
                    names.add(context.getString(R.string.select_status));
                    for (TaskStatusDTO t: taskStatusList) {
                        names.add(t.getTaskStatusName());
                    }
                    SpinnerListAdapter adapter = new SpinnerListAdapter(context, R.layout.xxsimple_spinner_item,names,SpinnerListAdapter.TASK_LIST, false);
                    spinner.setAdapter(adapter);
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (position == 0) {
                                taskStatus = null;
                            } else {
                                taskStatus = taskStatusList.get(position - 1);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });


                }
            }

            @Override
            public void onDataCached() {

            }

            @Override
            public void onError() {

            }
        });
    }

    public void setProjectSite(ProjectSiteDTO projectSite) {
        this.projectSite = projectSite;
    }

    public void setProjectSiteTask(ProjectSiteTaskDTO projectSiteTask) {
        this.projectSiteTask = projectSiteTask;
    }

    private void sendStatusRequest() {
        projectSiteTaskStatus = new ProjectSiteTaskStatusDTO();

        if (taskStatus == null) {
            showToast(context, context.getString(R.string.select_status));
            return;
        }
        projectSiteTaskStatus.setTaskStatus(taskStatus);
        projectSiteTaskStatus.setCompanyStaffID(SharedUtil.getCompanyStaff(context).getCompanyStaffID());
        projectSiteTaskStatus.setProjectSiteTaskID(projectSiteTask.getProjectSiteTaskID());

        RequestDTO w = new RequestDTO();
        w.setRequestType(RequestDTO.ADD_PROJECT_SITE_TASK_STATUS);
        w.setProjectSiteTaskStatus(projectSiteTaskStatus);

        progressBar.setVisibility(View.VISIBLE);
        WebSocketUtil.sendRequest(context, Statics.COMPANY_ENDPOINT,w,new WebSocketUtil.WebSocketListener() {
            @Override
            public void onMessage(ResponseDTO response) {
                if (!ErrorUtil.checkServerError(context,response)) {
                    return;
                }
                projectSiteTaskStatus = response.getProjectSiteTaskStatusList().get(0);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        listener.onStatusAdded(projectSiteTaskStatus);
                        dismiss();
                    }
                });
            }

            @Override
            public void onClose() {

            }

            @Override
            public void onError(final String message) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        listener.onError(message);
                    }
                });

            }
        });

    }
    public void setContext(Context context) {
        this.context = context;
    }

    public void setListener(StatusDialogListener listener) {
        this.listener = listener;
    }
}

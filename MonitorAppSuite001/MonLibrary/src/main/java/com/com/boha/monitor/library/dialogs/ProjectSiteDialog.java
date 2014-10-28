package com.com.boha.monitor.library.dialogs;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.dto.ProjectDTO;
import com.com.boha.monitor.library.dto.ProjectSiteDTO;
import com.com.boha.monitor.library.dto.transfer.RequestDTO;
import com.com.boha.monitor.library.dto.transfer.ResponseDTO;
import com.com.boha.monitor.library.util.ErrorUtil;
import com.com.boha.monitor.library.util.Statics;
import com.com.boha.monitor.library.util.ToastUtil;
import com.com.boha.monitor.library.util.WebSocketUtil;

/**
 * Created by aubreyM on 2014/10/18.
 */
public class ProjectSiteDialog extends DialogFragment {
    public interface ProjectSiteDialogListener {
        public void onProjectSiteAdded(ProjectSiteDTO project);
        public void onProjectSiteUpdated(ProjectSiteDTO project);
        public void onError(String message);
    }
    ProjectSiteDialogListener listener;
    Context context;
    TextView txtProject, txtNumber;
    EditText editProjectSiteName, editErf;
    ProgressBar progressBar;
    Button btnCancel, btnSave, btnDelete;
    ProjectSiteDTO projectSite;
    ProjectDTO project;
    View view;
    int action;
    static final String LOG = ProjectSiteDialog.class.getSimpleName();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.project_site_dialog, container);
        editProjectSiteName = (EditText) view.findViewById(R.id.PSD_editName);
        editErf = (EditText) view.findViewById(R.id.PSD_editStand);

        txtNumber = (TextView)view.findViewById(R.id.PSD_number);
        if (project.getProjectSiteList() != null) {
            txtNumber.setText("" + project.getProjectSiteList().size());
        } else {
            txtNumber.setText("0");
        }
        txtProject = (TextView)view.findViewById(R.id.PSD_project);
        txtProject.setText(project.getProjectName());

        progressBar = (ProgressBar) view.findViewById(R.id.PSD_progress);
        progressBar.setVisibility(View.GONE);


        btnCancel = (Button) view.findViewById(R.id.PSD_btnCancel);
        btnSave = (Button) view.findViewById(R.id.PSD_btnSave);
        btnDelete= (Button) view.findViewById(R.id.PSD_btnDelete);

        getDialog().setTitle(context.getResources().getString(R.string.app_name));


        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dismiss();

            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                switch (action) {
                    case ProjectDTO.ACTION_ADD:
                        registerProjectSite();
                        break;
                    case ProjectDTO.ACTION_UPDATE:
                        updateSite();
                        break;
                }
            }
        });
        if (action == ProjectSiteDTO.ACTION_UPDATE) {
            fillForm();
        } else {
            btnDelete.setVisibility(View.GONE);
        }
        return view;
    }

    private void fillForm() {
        editProjectSiteName.setText(projectSite.getProjectSiteName());
        editErf.setText(projectSite.getStandErfNumber());

    }
    private void registerProjectSite() {
        projectSite = new ProjectSiteDTO();
        projectSite.setProjectID(project.getProjectID());
        if (editProjectSiteName.getText().toString().isEmpty()) {
            ToastUtil.toast(context,context.getResources().getString(R.string.enter_name));
            return;
        }

        projectSite.setProjectSiteName(editProjectSiteName.getText().toString());
        projectSite.setStandErfNumber(editErf.getText().toString());

        RequestDTO w = new RequestDTO();
        w.setRequestType(RequestDTO.REGISTER_PROJECT_SITE);
        w.setProjectSite(projectSite);

        progressBar.setVisibility(View.VISIBLE);
        WebSocketUtil.sendRequest(context, Statics.COMPANY_ENDPOINT,w,new WebSocketUtil.WebSocketListener() {
            @Override
            public void onMessage(ResponseDTO response) {
                if (!ErrorUtil.checkServerError(context,response)) {
                    return;
                }
                projectSite = response.getProjectSiteList().get(0);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        listener.onProjectSiteAdded(projectSite);
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

    private void updateSite() {

    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public void setListener(ProjectSiteDialogListener listener) {
        this.listener = listener;
    }

    public void setProject(ProjectDTO project) {
        this.project = project;
    }

    public void setProjectSite(ProjectSiteDTO projectSite) {
        this.projectSite = projectSite;
    }
}

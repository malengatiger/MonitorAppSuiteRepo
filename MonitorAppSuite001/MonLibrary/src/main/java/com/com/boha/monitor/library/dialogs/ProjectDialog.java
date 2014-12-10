package com.com.boha.monitor.library.dialogs;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.adapters.SpinnerListAdapter;
import com.com.boha.monitor.library.dto.ClientDTO;
import com.com.boha.monitor.library.dto.ProjectDTO;
import com.com.boha.monitor.library.dto.transfer.RequestDTO;
import com.com.boha.monitor.library.dto.transfer.ResponseDTO;
import com.com.boha.monitor.library.util.CacheUtil;
import com.com.boha.monitor.library.util.ErrorUtil;
import com.com.boha.monitor.library.util.SharedUtil;
import com.com.boha.monitor.library.util.Statics;
import com.com.boha.monitor.library.util.Util;
import com.com.boha.monitor.library.util.WebSocketUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Dialog to add, update and delete Monitor projects
 * Created by aubreyM on 2014/10/18.
 */
public class ProjectDialog extends DialogFragment {
    public interface ProjectDialogListener {
        public void onProjectAdded(ProjectDTO project);
        public void onProjectUpdated(ProjectDTO project);
        public void onError(String message);
    }
    ProjectDialogListener listener;
    Context context;
    TextView txtCompany;
    EditText editProjectName, editDesc;
    ImageView imgDelete;
    ProgressBar progressBar;
    Button btnCancel, btnSave;
    ProjectDTO project;
    View view;
    Spinner clientSpinner;
    int action;
    static final String LOG = ProjectDialog.class.getSimpleName();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.project_edit, container);
        editProjectName = (EditText) view.findViewById(R.id.PE_projectName);
        editDesc = (EditText) view.findViewById(R.id.PE_desc);

        txtCompany = (TextView)view.findViewById(R.id.PE_groupName);
        txtCompany.setText(SharedUtil.getCompany(context).getCompanyName());

        progressBar = (ProgressBar) view.findViewById(R.id.PE_progress);
        progressBar.setVisibility(View.GONE);
        imgDelete = (ImageView)view.findViewById(R.id.PE_imgDelete);
        imgDelete.setVisibility(View.GONE);

        btnCancel = (Button) view.findViewById(R.id.PE_btnCancel);
        btnSave = (Button) view.findViewById(R.id.PE_btnSave);
        clientSpinner = (Spinner)view.findViewById(R.id.PE_spinner);
        getDialog().setTitle(context.getString(R.string.projects));
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
                switch (action) {
                    case ProjectDTO.ACTION_ADD:
                        registerProject();
                        break;
                    case ProjectDTO.ACTION_UPDATE:
                        updateProject();
                        break;
                }
            }
        });
        //if action is update, light up the delete (x) icon.
        // prepare dialog to confirm delete before message sent to server
        switch (action) {
            case ProjectDTO.ACTION_UPDATE:
                imgDelete.setVisibility(View.VISIBLE);
                imgDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder diag = new AlertDialog.Builder(getActivity());
                        diag.setTitle(context.getString(R.string.delete_confirm))
                                .setMessage(context.getString(R.string.delete_question))
                                .setPositiveButton(context.getString(R.string.yes), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        deleteProject();
                                    }
                                })
                                .setNegativeButton(context.getString(R.string.no),new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).show();
                    }
                });
                break;
        }
        return view;
    }

    private List<ClientDTO> clientList;
    private ClientDTO client;
    private void setSpinner() {

        CacheUtil.getCachedData(context,CacheUtil.CACHE_DATA,new CacheUtil.CacheUtilListener() {
            @Override
            public void onFileDataDeserialized(ResponseDTO response) {
                if (response != null) {
                    clientList = response.getCompany().getClientList();
                    List<String> names = new ArrayList<String>();
                    names.add(context.getResources().getString(R.string.select_client));
                    for (ClientDTO t: clientList) {
                        names.add(t.getClientName());
                    }
                    SpinnerListAdapter adapter = new SpinnerListAdapter(context, R.layout.xxsimple_spinner_item,names, SpinnerListAdapter.TASK_LIST, false);
                    clientSpinner.setAdapter(adapter);
                    clientSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (position == 0) {
                                client = null;
                            } else {
                                client = clientList.get(position - 1);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    if (action == ClientDTO.ACTION_UPDATE
                            || action == ClientDTO.ACTION_DELETE) {
                        fillForm();
                    }

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
    private void fillForm() {
        editProjectName.setText(project.getProjectName());
        editDesc.setText(project.getDescription());

        int index = 0;
        for (ClientDTO cs: clientList) {
            if (project.getClientID() == cs.getClientID()) {
                break;
            }
            index++;
        }
        if (index < clientList.size()) {
            clientSpinner.setSelection(index + 1);
        }

    }
    private void deleteProject() {

    }
    private void registerProject() {
        project = new ProjectDTO();
        project.setCompanyID(SharedUtil.getCompany(context).getCompanyID());
        if (editProjectName.getText().toString().isEmpty()) {
            Util.showToast(context,context.getResources().getString(R.string.enter_name));
            return;
        }

        if (editDesc.getText().toString().isEmpty()) {
            Util.showToast(context, context.getResources().getString(R.string.enter_desc));
            return;
        }


        if (client == null) {
            Util.showToast(context,context.getResources().getString(R.string.select_client));
            return;
        }

        project.setProjectName(editProjectName.getText().toString());
        project.setDescription(editDesc.getText().toString());

        project.setClientID(client.getClientID());

        RequestDTO w = new RequestDTO();
        w.setRequestType(RequestDTO.REGISTER_PROJECT);
        w.setProject(project);

        progressBar.setVisibility(View.VISIBLE);
        WebSocketUtil.sendRequest(context, Statics.COMPANY_ENDPOINT,w,new WebSocketUtil.WebSocketListener() {
            @Override
            public void onMessage(ResponseDTO response) {
                if (!ErrorUtil.checkServerError(context,response)) {
                    return;
                }
                project = response.getProjectList().get(0);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        listener.onProjectAdded(project);
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

    private void updateProject() {

    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public ProjectDialogListener getListener() {
        return listener;
    }

    public void setListener(ProjectDialogListener listener) {
        this.listener = listener;
    }

    public ProjectDTO getProject() {
        return project;
    }

    public void setProject(ProjectDTO project) {
        this.project = project;
    }
}

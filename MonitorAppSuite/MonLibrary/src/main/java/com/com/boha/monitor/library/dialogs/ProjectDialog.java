package com.com.boha.monitor.library.dialogs;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.dto.ClientDTO;
import com.com.boha.monitor.library.dto.ProjectDTO;
import com.com.boha.monitor.library.dto.transfer.RequestDTO;
import com.com.boha.monitor.library.dto.transfer.ResponseDTO;
import com.com.boha.monitor.library.util.CacheUtil;
import com.com.boha.monitor.library.util.ErrorUtil;
import com.com.boha.monitor.library.util.SharedUtil;
import com.com.boha.monitor.library.util.Statics;
import com.com.boha.monitor.library.util.ToastUtil;
import com.com.boha.monitor.library.util.WebSocketUtil;

import java.util.ArrayList;
import java.util.List;

/**
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
    ProgressBar progressBar;
    Button btnCancel, btnSave, btnDelete;
    ProjectDTO project;
    View view;
    Spinner clientSpinner;
    int action;
    static final String LOG = ProjectDialog.class.getSimpleName();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.project_edit, container);
        editProjectName = (EditText) view.findViewById(R.id.PE__projectName);
        editDesc = (EditText) view.findViewById(R.id.PE__desc);

        txtCompany = (TextView)view.findViewById(R.id.PE__groupName);
        txtCompany.setText(SharedUtil.getCompany(context).getCompanyName());

        progressBar = (ProgressBar) view.findViewById(R.id.PE__progress);
        progressBar.setVisibility(View.GONE);


        btnCancel = (Button) view.findViewById(R.id.PE__btnCancel);
        btnSave = (Button) view.findViewById(R.id.PE__btnSave);
        btnDelete= (Button) view.findViewById(R.id.PE__btnDelete);
        clientSpinner = (Spinner)view.findViewById(R.id.PE__spinner);
        getDialog().setTitle(context.getResources().getString(R.string.app_name));
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
                        registerActor();
                        break;
                    case ProjectDTO.ACTION_UPDATE:
                        updateActor();
                        break;
                }
            }
        });
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
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.xxsimple_spinner_item,names);
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
    private void registerActor() {
        project = new ProjectDTO();
        project.setCompanyID(SharedUtil.getCompany(context).getCompanyID());
        if (editProjectName.getText().toString().isEmpty()) {
            ToastUtil.toast(context,context.getResources().getString(R.string.enter_name));
            return;
        }

        if (editDesc.getText().toString().isEmpty()) {
            ToastUtil.toast(context,context.getResources().getString(R.string.enter_desc));
            return;
        }


        if (client == null) {
            ToastUtil.toast(context,context.getResources().getString(R.string.select_client));
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

    private void updateActor() {

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

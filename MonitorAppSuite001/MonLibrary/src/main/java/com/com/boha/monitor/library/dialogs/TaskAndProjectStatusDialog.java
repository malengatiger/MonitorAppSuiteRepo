package com.com.boha.monitor.library.dialogs;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.dto.ProjectDTO;
import com.com.boha.monitor.library.dto.ProjectStatusTypeDTO;
import com.com.boha.monitor.library.dto.TaskStatusDTO;
import com.com.boha.monitor.library.dto.transfer.RequestDTO;
import com.com.boha.monitor.library.dto.transfer.ResponseDTO;
import com.com.boha.monitor.library.util.ErrorUtil;
import com.com.boha.monitor.library.util.SharedUtil;
import com.com.boha.monitor.library.util.Statics;
import com.com.boha.monitor.library.util.ToastUtil;
import com.com.boha.monitor.library.util.Util;
import com.com.boha.monitor.library.util.WebSocketUtil;

/**
 * Created by aubreyM on 2014/10/18.
 */
public class TaskAndProjectStatusDialog extends DialogFragment {

    Context context;
    EditText editName, editDesc;
    ProgressBar progressBar;
    Button  btnSave;
    RadioButton radioRed, radioGreen, radioYellow;
    View view;
    TextView txtColor;
    ImageView imgDelete;
    int action, type;
    public static final int
            ACTION_ADD = 1,
            ACTION_UPDATE = 2,
            TASK_STATUS = 4, PROJECT_STATUS = 5;
    static final String LOG = TaskAndProjectStatusDialog.class.getSimpleName();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.edit_dialog, container);
        txtColor = (TextView)view.findViewById(R.id.EDD_color);
        editName = (EditText) view.findViewById(R.id.EDD_edit);
        editDesc = (EditText) view.findViewById(R.id.EDD_desc);
        radioGreen = (RadioButton) view.findViewById(R.id.EDD_radioGreen);
        radioRed = (RadioButton) view.findViewById(R.id.EDD_radioRed);
        radioYellow = (RadioButton) view.findViewById(R.id.EDD_radioYellow);
        imgDelete = (ImageView) view.findViewById(R.id.EDD_imgDelete);
        imgDelete.setVisibility(View.GONE);

        editDesc.setVisibility(View.GONE);
        progressBar = (ProgressBar) view.findViewById(R.id.EDD_progress);
        progressBar.setVisibility(View.GONE);

        btnSave = (Button) view.findViewById(R.id.EDD_btnChange);
        switch (type) {
            case TASK_STATUS:
                getDialog().setTitle(context.getString(R.string.task_status));
                break;
            case PROJECT_STATUS:
                getDialog().setTitle(context.getString(R.string.project_status));
                break;
        }




        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //flash btn
                Util.flashOnce(btnSave,100,new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        update();
                    }
                });

            }
        });

        radioGreen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    txtColor.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.xgreen_oval));
                    txtColor.setText("Green");
                    Util.flashSeveralTimes(txtColor, 200, 2, null);
                }
            }
        });
        radioYellow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    txtColor.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.xorange_oval));
                    txtColor.setText("Yellow");
                    Util.flashSeveralTimes(txtColor, 200, 2, null);
                }
            }
        });
        radioRed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    txtColor.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.xred_oval));
                    txtColor.setText("Red");
                    Util.flashSeveralTimes(txtColor, 200, 2, null);
                }
            }
        });
        switch (action) {
            case ACTION_ADD:
                btnSave.setText(context.getString(R.string.save));
                break;
            case ACTION_UPDATE:
                btnSave.setText(context.getString(R.string.change_item));
                fillForm();
                imgDelete.setVisibility(View.VISIBLE);
                imgDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder diag = new AlertDialog.Builder(getActivity());
                        diag.setTitle(context.getString(R.string.delete_confirm));
                        diag.setMessage(context.getString(R.string.delete_question))
                                .setPositiveButton(context.getString(R.string.yes), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .setNegativeButton(context.getString(R.string.no), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .show();
                    }
                });
                break;

        }
        return view;
    }

    private TaskStatusDTO taskStatus;
    private ProjectStatusTypeDTO projectStatusType;
    private ProjectDTO project;

    private void fillForm() {
        if (taskStatus != null) {
            editName.setText(taskStatus.getTaskStatusName());
            if (taskStatus.getStatusColor() != null) {
                switch (taskStatus.getStatusColor()) {
                    case TaskStatusDTO.STATUS_COLOR_YELLOW:
                        radioYellow.setChecked(true);
                        break;
                    case TaskStatusDTO.STATUS_COLOR_GREEN:
                        radioGreen.setChecked(true);
                        break;
                    case TaskStatusDTO.STATUS_COLOR_RED:
                        radioRed.setChecked(true);
                        break;
                }
            }
        }
        if (projectStatusType != null) {
            editName.setText(projectStatusType.getProjectStatusName());
            switch (projectStatusType.getStatusColor()) {
                case TaskStatusDTO.STATUS_COLOR_YELLOW:
                    radioYellow.setChecked(true);
                    break;
                case TaskStatusDTO.STATUS_COLOR_GREEN:
                    radioGreen.setChecked(true);
                    break;
                case TaskStatusDTO.STATUS_COLOR_RED:
                    radioRed.setChecked(true);
                    break;
            }
        }
        if (project != null) {
            editName.setText(project.getProjectName());
            editDesc.setText(project.getDescription());
            editDesc.setVisibility(View.VISIBLE);
            radioRed.setVisibility(View.GONE);
            radioYellow.setVisibility(View.GONE);
            radioGreen.setVisibility(View.GONE);
        }


    }

    private void update() {
        RequestDTO w = new RequestDTO();
        if (editName.getText().toString().isEmpty()) {
            Util.showToast(context, context.getString(R.string.enter_name));
            return;
        }
        Short color = null;
        if (project == null) {
            if (!radioRed.isChecked() && !radioGreen.isChecked() && !radioYellow.isChecked()) {
                Util.showToast(context, context.getString(R.string.select_status_color));
                return;
            }
            if (radioGreen.isChecked()) color = (short) TaskStatusDTO.STATUS_COLOR_GREEN;
            if (radioYellow.isChecked()) color = (short) TaskStatusDTO.STATUS_COLOR_YELLOW;
            if (radioRed.isChecked()) color = (short) TaskStatusDTO.STATUS_COLOR_RED;
        }

        switch (action) {
            case ACTION_ADD:
                if (taskStatus != null) {
                    w.setRequestType(RequestDTO.ADD_COMPANY_TASK_STATUS);
                    taskStatus.setCompanyID(SharedUtil.getCompany(context).getCompanyID());
                    taskStatus.setTaskStatusName(editName.getText().toString());
                    taskStatus.setStatusColor(color);
                    w.setTaskStatus(taskStatus);
                }
                if (projectStatusType != null) {
                    w.setRequestType(RequestDTO.ADD_COMPANY_PROJECT_STATUS_TYPE);
                    projectStatusType.setCompanyID(SharedUtil.getCompany(context).getCompanyID());
                    projectStatusType.setProjectStatusName(editName.getText().toString());
                    projectStatusType.setStatusColor(color);
                    w.setProjectStatusType(projectStatusType);
                }
                if (project != null) {
                    w.setRequestType(RequestDTO.REGISTER_PROJECT);
                    project.setCompanyID(SharedUtil.getCompany(context).getCompanyID());
                    project.setProjectName(editName.getText().toString());
                    project.setDescription(editDesc.getText().toString());
                    w.setProject(project);
                }

                break;
            case ACTION_UPDATE:
                if (taskStatus != null) {
                    w.setRequestType(RequestDTO.UPDATE_COMPANY_TASK_STATUS);
                    taskStatus.setStatusColor(color);
                    taskStatus.setTaskStatusName(editName.getText().toString());
                    w.setTaskStatus(taskStatus);
                }
                if (projectStatusType != null) {
                    w.setRequestType(RequestDTO.UPDATE_COMPANY_PROJECT_STATUS_TYPE);
                    projectStatusType.setStatusColor(color);
                    projectStatusType.setProjectStatusName(editName.getText().toString());
                    w.setProjectStatusType(projectStatusType);
                }
                break;
        }

        progressBar.setVisibility(View.VISIBLE);
        WebSocketUtil.sendRequest(context, Statics.COMPANY_ENDPOINT, w, new WebSocketUtil.WebSocketListener() {
            @Override
            public void onMessage(final ResponseDTO response) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                            if (!ErrorUtil.checkServerError(context, response)) {
                                return;
                            }
                            dismiss();
                            listener.onComplete();
                        }
                    });
                } else {
                    listener.onComplete();
                }

            }

            @Override
            public void onClose() {

            }

            @Override
            public void onError(final String message) {
                listener.onError(message);
                //dismiss();
            }
        });

    }

    public void setType(int type) {
        this.type = type;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setTaskStatus(TaskStatusDTO taskStatus) {
        this.taskStatus = taskStatus;
    }

    public void setProjectStatusType(ProjectStatusTypeDTO projectStatusType) {
        this.projectStatusType = projectStatusType;
    }

    public interface EditDialogListener {
        public void onComplete();

        public void onError(String message);
    }

    EditDialogListener listener;

    public void setListener(EditDialogListener listener) {
        this.listener = listener;
    }

    public void setProject(ProjectDTO project) {
        this.project = project;
    }

    public void setAction(int action) {
        this.action = action;
    }
}

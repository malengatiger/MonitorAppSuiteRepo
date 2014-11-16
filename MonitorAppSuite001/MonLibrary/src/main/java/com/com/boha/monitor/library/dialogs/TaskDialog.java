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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.dto.CompanyDTO;
import com.com.boha.monitor.library.dto.TaskDTO;
import com.com.boha.monitor.library.dto.transfer.RequestDTO;
import com.com.boha.monitor.library.dto.transfer.ResponseDTO;
import com.com.boha.monitor.library.util.ErrorUtil;
import com.com.boha.monitor.library.util.SharedUtil;
import com.com.boha.monitor.library.util.Statics;
import com.com.boha.monitor.library.util.ToastUtil;
import com.com.boha.monitor.library.util.WebSocketUtil;

/**
 * Add, update and delete company tasks
 * Created by aubreyM on 2014/10/18.
 */
public class TaskDialog extends DialogFragment {
    public interface TaskDialogListener {
        public void onTaskAdded(TaskDTO task);
        public void onTaskUpdated(TaskDTO task);
        public void onTaskDeleted(TaskDTO task);
        public void onError(String message);
    }
    TaskDialogListener listener;
    Context context;
    EditText editTaskName;
    NumberPicker numberPicker;
    ImageView imgDelete;
    ProgressBar progressBar;
    Button btnCancel, btnSave;
    TaskDTO task;
    String title;
    View view;
    int action;
    static final String LOG = TaskDialog.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.task_edit, container);
        editTaskName = (EditText) view.findViewById(R.id.TE_editTaskName);
        numberPicker = (NumberPicker) view.findViewById(R.id.TE_numberPicker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(100);

        imgDelete = (ImageView)view.findViewById(R.id.TE_imgDelete);
        imgDelete.setVisibility(View.GONE);

        progressBar = (ProgressBar) view.findViewById(R.id.TE_progress);
        progressBar.setVisibility(View.GONE);


        btnCancel = (Button) view.findViewById(R.id.TE_btnCancel);
        btnSave = (Button) view.findViewById(R.id.TE_btnSave);
        getDialog().setTitle(context.getString(R.string.task));

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
                    case TaskDTO.ACTION_ADD:
                        registerTask();
                        break;
                    case TaskDTO.ACTION_UPDATE:
                        updateTask();
                        break;

                }
            }
        });
        switch (action) {
            case TaskDTO.ACTION_UPDATE:
                fillForm();
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
                                        deleteTask();
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

    private void fillForm() {
        editTaskName.setText(task.getTaskName());
        numberPicker.setValue(task.getTaskNumber());
    }
    private void registerTask() {
        task = new TaskDTO();
        CompanyDTO c = new CompanyDTO();
        c.setCompanyID(SharedUtil.getCompany(context).getCompanyID());
        task.setCompanyID(c.getCompanyID());
        if (editTaskName.getText().toString().isEmpty()) {
            ToastUtil.toast(context,context.getResources().getString(R.string.enter_engineer_name));
            return;
        }

        if (numberPicker.getValue() == 0) {
            ToastUtil.toast(context, context.getString(R.string.select_seq_no));
            return;
        }

        task.setTaskName(editTaskName.getText().toString());
        task.setTaskNumber(numberPicker.getValue());

        RequestDTO w = new RequestDTO();
        w.setRequestType(RequestDTO.ADD_COMPANY_TASK);
        w.setTask(task);

        progressBar.setVisibility(View.VISIBLE);
        WebSocketUtil.sendRequest(context, Statics.COMPANY_ENDPOINT,w,new WebSocketUtil.WebSocketListener() {
            @Override
            public void onMessage(ResponseDTO response) {
                if (!ErrorUtil.checkServerError(context,response)) {
                    return;
                }
                task = response.getTaskList().get(0);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        listener.onTaskAdded(task);
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

    private void updateTask() {

    }
    private void deleteTask() {

    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void setContext(Context context) {
        this.context = context;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public void setTask(TaskDTO task) {
        this.task = task;
    }

    public void setListener(TaskDialogListener listener) {
        this.listener = listener;
    }
}

package com.com.boha.monitor.library.dialogs;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.SubTaskActivity;
import com.com.boha.monitor.library.dto.CompanyDTO;
import com.com.boha.monitor.library.dto.TaskDTO;
import com.com.boha.monitor.library.dto.TaskPriceDTO;
import com.com.boha.monitor.library.dto.transfer.RequestDTO;
import com.com.boha.monitor.library.dto.transfer.ResponseDTO;
import com.com.boha.monitor.library.util.ErrorUtil;
import com.com.boha.monitor.library.util.SharedUtil;
import com.com.boha.monitor.library.util.Statics;
import com.com.boha.monitor.library.util.ToastUtil;
import com.com.boha.monitor.library.util.Util;
import com.com.boha.monitor.library.util.WebSocketUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static com.com.boha.monitor.library.util.Util.*;

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
    EditText editTaskName, editPrice;
    NumberPicker numberPicker;
    ImageView imgDelete;
    ProgressBar progressBar;
    Button btnSubTasks, btnSave;
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
        editPrice = (EditText) view.findViewById(R.id.TE_editTaskPrice);
        numberPicker = (NumberPicker) view.findViewById(R.id.TE_numberPicker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(100);



        progressBar = (ProgressBar) view.findViewById(R.id.TE_progress);
        progressBar.setVisibility(View.GONE);


        btnSave = (Button) view.findViewById(R.id.TE_btnSave);
        getDialog().setTitle(context.getString(R.string.task));

        btnSubTasks.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dismiss();
                Intent i = new Intent(getActivity(), SubTaskActivity.class);
                i.putExtra("task", task);
                startActivity(i);

            }
        });
        if (action == TaskDTO.ACTION_ADD) {
            btnSubTasks.setVisibility(View.GONE);
        }
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

    TaskPriceDTO taskPrice;
    private void fillForm() {
        editTaskName.setText(task.getTaskName());
        numberPicker.setValue(task.getTaskNumber());
        if (task.getTaskPriceList() != null && !task.getTaskPriceList().isEmpty()) {
            taskPrice = task.getTaskPriceList().get(0);
            editPrice.setText(df.format(taskPrice.getPrice()));
        }
    }
    static final DecimalFormat df = new DecimalFormat("###,###,###,###,###,###,###,##0.00");
    private void registerTask() {
        task = new TaskDTO();
        CompanyDTO c = new CompanyDTO();
        c.setCompanyID(SharedUtil.getCompany(context).getCompanyID());
        task.setCompanyID(c.getCompanyID());
        if (editTaskName.getText().toString().isEmpty()) {
            showToast(context, context.getResources().getString(R.string.enter_engineer_name));
            return;
        }

        if (numberPicker.getValue() == 0) {
            showToast(context, context.getString(R.string.select_seq_no));
            return;
        }

        task.setTaskName(editTaskName.getText().toString());
        task.setTaskNumber(numberPicker.getValue());

        RequestDTO w = new RequestDTO();
        w.setRequestType(RequestDTO.ADD_COMPANY_TASK);
        w.setTask(task);

        if (editPrice.getText().toString().isEmpty()) {
            showToast(context, context.getString(R.string.enter_price));
            return;
        }
        taskPrice = new TaskPriceDTO();
        taskPrice.setPrice(Double.parseDouble(editPrice.getText().toString()));
        w.setTaskPrice(taskPrice);

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
        if (editTaskName.getText().toString().isEmpty()) {
            showToast(context, context.getString(R.string.enter_taskname));
            return;
        }
        TaskDTO t = new TaskDTO();
        t.setTaskID(task.getTaskID());
        t.setTaskName(editTaskName.getText().toString());
        t.setTaskNumber(numberPicker.getValue());
        //TODO - update task and price, check that price has changed
        if (!editPrice.getText().toString().isEmpty()) {
            double newPrice = Double.parseDouble(editPrice.getText().toString());
            if (taskPrice.getPrice() != newPrice) {
                TaskPriceDTO tp = new TaskPriceDTO();
                tp.setTaskID(task.getTaskID());
                tp.setPrice(newPrice);
                t.setTaskPriceList(new ArrayList<TaskPriceDTO>());
                t.getTaskPriceList().add(0,tp);
            }
        }
        RequestDTO w = new RequestDTO(RequestDTO.UPDATE_COMPANY_TASK);
        w.setTask(t);

        WebSocketUtil.sendRequest(context,Statics.COMPANY_ENDPOINT,w,new WebSocketUtil.WebSocketListener() {
            @Override
            public void onMessage(ResponseDTO response) {

            }

            @Override
            public void onClose() {

            }

            @Override
            public void onError(final String message) {
                Log.e(LOG, "---- ERROR websocket - " + message);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Util.showErrorToast(context,message);
                    }
                });
            }
        });
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

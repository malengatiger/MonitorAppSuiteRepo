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
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.dto.CompanyDTO;
import com.com.boha.monitor.library.dto.EngineerDTO;
import com.com.boha.monitor.library.dto.transfer.RequestDTO;
import com.com.boha.monitor.library.dto.transfer.ResponseDTO;
import com.com.boha.monitor.library.util.ErrorUtil;
import com.com.boha.monitor.library.util.SharedUtil;
import com.com.boha.monitor.library.util.Statics;
import com.com.boha.monitor.library.util.Util;
import com.com.boha.monitor.library.util.WebSocketUtil;

/**
 * Created by aubreyM on 2014/10/18.
 */
public class EngineerDialog extends DialogFragment {
    public interface EngineerDialogListener {
        public void onEngineerAdded(EngineerDTO engineer);
        public void onEngineerUpdated(EngineerDTO engineer);
        public void onEngineerDeleted(EngineerDTO engineer);
        public void onError(String message);
    }
    EngineerDialogListener listener;
    Context context;
    TextView txtCompany;
    EditText editFirstName, editLastName, editEmail,
            editIDNumber, editCellphone;
    ImageView imgDelete;
    Spinner spinner;
    ProgressBar progressBar;
    Button btnCancel, btnSave;
    EngineerDTO engineer;
    View view;
    int action;
    static final String LOG = EngineerDialog.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.person_edit, container);
        editFirstName = (EditText) view.findViewById(R.id.ED_PSN_firstName);
        editLastName = (EditText) view.findViewById(R.id.ED_PSN_lastName);
        editEmail = (EditText) view.findViewById(R.id.ED_PSN_email);
        editCellphone = (EditText) view.findViewById(R.id.ED_PSN_cellphone);
        editIDNumber = (EditText)view.findViewById(R.id.ED_PSN_idNumber);

        spinner = (Spinner)view.findViewById(R.id.ED_PSN_spinner);
        spinner.setVisibility(View.GONE);
        editLastName.setVisibility(View.GONE);
        editIDNumber.setVisibility(View.GONE);
        imgDelete = (ImageView)view.findViewById(R.id.ED_PSN_imgDelete);
        imgDelete.setVisibility(View.GONE);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);


        btnCancel = (Button) view.findViewById(R.id.ED_PSN_btnCancel);
        btnSave = (Button) view.findViewById(R.id.ED_PSN_btnSave);
        getDialog().setTitle(context.getResources().getString(R.string.engineer));

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
                    case EngineerDTO.ACTION_ADD:
                        registerEngineer();
                        break;
                    case EngineerDTO.ACTION_UPDATE:
                        updateEngineer();
                        break;

                }
            }
        });
        switch (action) {
            case EngineerDTO.ACTION_UPDATE:
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
                                        deleteEngineer();
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
        editFirstName.setText(engineer.getEngineerName());
        editEmail.setText(engineer.getEmail());
        editCellphone.setText(engineer.getCellphone());


    }
    private void registerEngineer() {
        engineer = new EngineerDTO();
        CompanyDTO c = new CompanyDTO();
        c.setCompanyID(SharedUtil.getCompany(context).getCompanyID());
        engineer.setCompanyID(c.getCompanyID());
        if (editFirstName.getText().toString().isEmpty()) {
            Util.showToast(context, context.getResources().getString(R.string.enter_engineer_name));
            return;
        }

        if (editEmail.getText().toString().isEmpty()) {
            Util.showToast(context,context.getResources().getString(R.string.enter_email));
            return;
        }

        if (editCellphone.getText().toString().isEmpty()) {
            Util.showToast(context,context.getResources().getString(R.string.enter_cell));
            return;
        }


        engineer.setEngineerName(editFirstName.getText().toString());
        engineer.setCellphone(editCellphone.getText().toString());
        engineer.setEmail(editEmail.getText().toString());

        RequestDTO w = new RequestDTO();
        w.setRequestType(RequestDTO.REGISTER_ENGINEER);
        w.setEngineer(engineer);

        progressBar.setVisibility(View.VISIBLE);
        WebSocketUtil.sendRequest(context, Statics.COMPANY_ENDPOINT,w,new WebSocketUtil.WebSocketListener() {
            @Override
            public void onMessage(ResponseDTO response) {
                if (!ErrorUtil.checkServerError(context,response)) {
                    return;
                }
                engineer = response.getEngineerList().get(0);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        listener.onEngineerAdded(engineer);
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

    private void updateEngineer() {

    }
    private void deleteEngineer() {

    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public void setEngineer(EngineerDTO engineer) {
        this.engineer = engineer;
    }

    public void setListener(EngineerDialogListener listener) {
        this.listener = listener;
    }
}

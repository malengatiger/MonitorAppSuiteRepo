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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.dto.BeneficiaryDTO;
import com.com.boha.monitor.library.dto.CompanyDTO;
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
public class BeneficiaryDialog extends DialogFragment {
    public interface BeneficiaryDialogListener {
        public void onBeneficiaryAdded(BeneficiaryDTO beneficiary);

        public void onBeneficiaryUpdated(BeneficiaryDTO beneficiary);

        public void onBeneficiaryDeleted(BeneficiaryDTO beneficiary);

        public void onError(String message);
    }

    BeneficiaryDialogListener listener;
    Context context;
    TextView txtCompany;
    EditText editFirstName, editLastName, editEmail,
            editIDNumber, editCellphone;
    ImageView imgDelete;
    Spinner spinner;
    ProgressBar progressBar;
    Button btnCancel, btnSave;
    BeneficiaryDTO beneficiary;
    View view;
    int action;
    String title;
    static final String LOG = BeneficiaryDialog.class.getSimpleName();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.person_edit, container);
        editFirstName = (EditText) view.findViewById(R.id.ED_PSN_firstName);
        editLastName = (EditText) view.findViewById(R.id.ED_PSN_lastName);
        editEmail = (EditText) view.findViewById(R.id.ED_PSN_email);
        editCellphone = (EditText) view.findViewById(R.id.ED_PSN_cellphone);
        editIDNumber = (EditText) view.findViewById(R.id.ED_PSN_idNumber);

        editEmail.setVisibility(View.GONE);
        editCellphone.setVisibility(View.GONE);

        spinner = (Spinner) view.findViewById(R.id.ED_PSN_spinner);
        progressBar = (ProgressBar) view.findViewById(R.id.ED_PSN_progress);
        progressBar.setVisibility(View.GONE);
        imgDelete = (ImageView) view.findViewById(R.id.ED_PSN_imgDelete);
        imgDelete.setVisibility(View.GONE);
        TextView mTitle = (TextView)view.findViewById(R.id.ED_PSN_personType);
        mTitle.setText(title);

        btnCancel = (Button) view.findViewById(R.id.ED_PSN_btnCancel);
        btnSave = (Button) view.findViewById(R.id.ED_PSN_btnSave);
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
                    case BeneficiaryDTO.ACTION_ADD:
                        registerBeneficiary();
                        break;
                    case BeneficiaryDTO.ACTION_UPDATE:
                        updateBeneficiary();
                        break;
                }
            }
        });
        switch (action) {
            case BeneficiaryDTO.ACTION_UPDATE:
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
                                        deleteBeneficiary();
                                    }
                                })
                                .setNegativeButton(context.getString(R.string.no), new DialogInterface.OnClickListener() {
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
        editFirstName.setText(beneficiary.getFirstName());
        editLastName.setText(beneficiary.getLastName());
        editEmail.setText(beneficiary.getEmail());
        editCellphone.setText(beneficiary.getCellphone());
        editIDNumber.setText(beneficiary.getiDNumber());


    }

    private void registerBeneficiary() {
        beneficiary = new BeneficiaryDTO();
        CompanyDTO c = new CompanyDTO();
        c.setCompanyID(SharedUtil.getCompany(context).getCompanyID());
        beneficiary.setCompany(c);
        if (editFirstName.getText().toString().isEmpty()) {
            ToastUtil.toast(context, context.getResources().getString(R.string.enter_firstname));
            return;
        }

        if (editLastName.getText().toString().isEmpty()) {
            ToastUtil.toast(context, context.getResources().getString(R.string.enter_lastname));
            return;
        }
        if (editIDNumber.getText().toString().isEmpty()) {
            ToastUtil.toast(context, context.getResources().getString(R.string.enter_id));
            return;
        }

        beneficiary.setFirstName(editFirstName.getText().toString());
        beneficiary.setLastName(editLastName.getText().toString());
       // beneficiary.setCellphone(editCellphone.getText().toString());
       // beneficiary.setEmail(editEmail.getText().toString());
        beneficiary.setiDNumber(editIDNumber.getText().toString());

        RequestDTO w = new RequestDTO();
        w.setRequestType(RequestDTO.REGISTER_BENEFICIARY);
        w.setBeneficiary(beneficiary);

        progressBar.setVisibility(View.VISIBLE);
        WebSocketUtil.sendRequest(context, Statics.COMPANY_ENDPOINT, w, new WebSocketUtil.WebSocketListener() {
            @Override
            public void onMessage(ResponseDTO response) {
                if (!ErrorUtil.checkServerError(context, response)) {
                    return;
                }
                beneficiary = response.getBeneficiaryList().get(0);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        listener.onBeneficiaryAdded(beneficiary);
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

    private void updateBeneficiary() {

    }

    private void deleteBeneficiary() {

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

    public void setBeneficiary(BeneficiaryDTO beneficiary) {
        this.beneficiary = beneficiary;
    }

    public void setListener(BeneficiaryDialogListener listener) {
        this.listener = listener;
    }

    List<ProjectDTO> projectList;
    ProjectDTO project;
    private void setSpinner() {

        CacheUtil.getCachedData(context, CacheUtil.CACHE_DATA, new CacheUtil.CacheUtilListener() {
            @Override
            public void onFileDataDeserialized(ResponseDTO response) {
                if (response != null) {
                    projectList = response.getCompany().getProjectList();
                    List<String> list = new ArrayList<>();
                    list.add(context.getString(R.string.select_project));
                    for (ProjectDTO p : projectList) {
                        list.add(p.getProjectName());
                    }
                    ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(context, R.layout.xxsimple_spinner_item, list);
                    adapter1.setDropDownViewResource(R.layout.xxsimple_spinner_dropdown_item);
                    if (spinner == null) {
                        spinner = (Spinner) view.findViewById(R.id.BC_projectSpinner);
                    }
                    spinner.setAdapter(adapter1);
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (position == 0) {
                                project = null;
                            } else {
                                project = projectList.get(position - 1);
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
}

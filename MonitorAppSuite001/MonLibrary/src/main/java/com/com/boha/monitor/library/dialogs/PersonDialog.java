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
import com.com.boha.monitor.library.dto.CompanyStaffDTO;
import com.com.boha.monitor.library.dto.CompanyStaffTypeDTO;
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
public class PersonDialog extends DialogFragment {
    public interface PersonDialogListener {
        public void onStaffAdded(CompanyStaffDTO companyStaff);
        public void onStaffUpdated(CompanyStaffDTO companyStaff);
        public void onError(String message);
    }
    PersonDialogListener listener;
    Context context;
    TextView txtCompany;
    EditText editFirstName, editLastName, editEmail, editCellphone;
    ProgressBar progressBar;
    Button btnCancel, btnSave;
    CompanyStaffDTO companyStaff;
    View view;
    Spinner staffTypeSpinner;
    int action;
    static final String LOG = PersonDialog.class.getSimpleName();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.person_edit, container);
        editFirstName = (EditText) view.findViewById(R.id.ED_PSN_firstName);
        editLastName = (EditText) view.findViewById(R.id.ED_PSN_lastName);
        editEmail = (EditText) view.findViewById(R.id.ED_PSN_email);
        editCellphone = (EditText) view.findViewById(R.id.ED_PSN_cellphone);
        txtCompany = (TextView)view.findViewById(R.id.ED_PSN_groupName);
        txtCompany.setText(SharedUtil.getCompany(context).getCompanyName());

        progressBar = (ProgressBar) view.findViewById(R.id.ED_PSN_progress);
        progressBar.setVisibility(View.GONE);


        btnCancel = (Button) view.findViewById(R.id.ED_PSN_btnCancel);
        btnSave = (Button) view.findViewById(R.id.ED_PSN_btnSave);
        staffTypeSpinner = (Spinner)view.findViewById(R.id.ED_PSN_spinner);
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
                    case CompanyStaffDTO.ACTION_ADD:
                        registerActor();
                        break;
                    case CompanyStaffDTO.ACTION_UPDATE:
                        updateActor();
                        break;
                }
            }
        });
        return view;
    }

    private List<CompanyStaffTypeDTO> companyStaffTypeList;
    private CompanyStaffTypeDTO companyStaffType;
    private void setSpinner() {

        CacheUtil.getCachedData(context,CacheUtil.CACHE_DATA,new CacheUtil.CacheUtilListener() {
            @Override
            public void onFileDataDeserialized(ResponseDTO response) {
                if (response != null) {
                    companyStaffTypeList = response.getCompanyStaffTypeList();
                    List<String> names = new ArrayList<String>();
                    names.add(context.getResources().getString(R.string.select_staff_type));
                    for (CompanyStaffTypeDTO t: companyStaffTypeList) {
                        names.add(t.getCompanyStaffTypeName());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.xxsimple_spinner_item,names);
                    staffTypeSpinner.setAdapter(adapter);
                    staffTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (position == 0) {
                                companyStaffType = null;
                            } else {
                                companyStaffType = companyStaffTypeList.get(position - 1);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    if (action == CompanyStaffDTO.ACTION_UPDATE) {
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
        editFirstName.setText(companyStaff.getFirstName());
        editLastName.setText(companyStaff.getLastName());
        editEmail.setText(companyStaff.getEmail());
        editCellphone.setText(companyStaff.getCellphone());

        int index = 0;
        for (CompanyStaffTypeDTO cs: companyStaffTypeList) {
            if (companyStaff.getCompanyStaffType().getCompanyStaffTypeID() == cs.getCompanyStaffTypeID()) {
                break;
            }
            index++;
        }
        if (index < companyStaffTypeList.size()) {
            staffTypeSpinner.setSelection(index + 1);
        }

    }
    private void registerActor() {
        companyStaff = new CompanyStaffDTO();
        companyStaff.setCompanyID(SharedUtil.getCompany(context).getCompanyID());
        if (editFirstName.getText().toString().isEmpty()) {
            ToastUtil.toast(context,context.getResources().getString(R.string.enter_firstname));
            return;
        }

        if (editLastName.getText().toString().isEmpty()) {
            ToastUtil.toast(context,context.getResources().getString(R.string.enter_lastname));
            return;
        }

        if (editEmail.getText().toString().isEmpty()) {
            ToastUtil.toast(context,context.getResources().getString(R.string.enter_email));
            return;
        }

        if (editCellphone.getText().toString().isEmpty()) {
            ToastUtil.toast(context,context.getResources().getString(R.string.enter_cell));
            return;
        }
        if (companyStaffType == null) {
            ToastUtil.toast(context,context.getResources().getString(R.string.select_staff_type));
            return;
        }

        companyStaff.setFirstName(editFirstName.getText().toString());
        companyStaff.setLastName(editLastName.getText().toString());
        companyStaff.setCellphone(editCellphone.getText().toString());
        companyStaff.setEmail(editEmail.getText().toString());
        companyStaff.setCompanyStaffType(companyStaffType);

        RequestDTO w = new RequestDTO();
        w.setRequestType(RequestDTO.REGISTER_COMPANY_STAFF);
        w.setCompanyStaff(companyStaff);

        progressBar.setVisibility(View.VISIBLE);
        WebSocketUtil.sendRequest(context, Statics.COMPANY_ENDPOINT,w,new WebSocketUtil.WebSocketListener() {
            @Override
            public void onMessage(ResponseDTO response) {
                if (!ErrorUtil.checkServerError(context,response)) {
                    return;
                }
                companyStaff = response.getCompanyStaff();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        listener.onStaffAdded(companyStaff);
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

    public void setCompanyStaff(CompanyStaffDTO companyStaff) {
        this.companyStaff = companyStaff;
    }

    public void setListener(PersonDialogListener listener) {
        this.listener = listener;
    }
}

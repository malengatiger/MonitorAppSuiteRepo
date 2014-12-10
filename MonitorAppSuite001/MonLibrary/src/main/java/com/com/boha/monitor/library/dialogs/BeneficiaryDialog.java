package com.com.boha.monitor.library.dialogs;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
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
import com.com.boha.monitor.library.dto.BeneficiaryDTO;
import com.com.boha.monitor.library.dto.CompanyDTO;
import com.com.boha.monitor.library.dto.ProjectDTO;
import com.com.boha.monitor.library.dto.ProjectSiteDTO;
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
    Spinner spinner, spinner2;
    ProgressBar progressBar;
    Button btnCancel, btnSave;
    BeneficiaryDTO beneficiary;
    View view;
    int action;
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
        spinner2 = (Spinner) view.findViewById(R.id.ED_PSN_spinner2);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        imgDelete = (ImageView) view.findViewById(R.id.ED_PSN_imgDelete);
        imgDelete.setVisibility(View.GONE);


        btnCancel = (Button) view.findViewById(R.id.ED_PSN_btnCancel);
        btnSave = (Button) view.findViewById(R.id.ED_PSN_btnSave);
        getDialog().setTitle(context.getResources().getString(R.string.beneficiary));
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
        editIDNumber.setText(beneficiary.getiDNumber());
        if (beneficiary.getProjectID() != null) {
            int index = 0;
            for (ProjectDTO p: projectList) {
                if (beneficiary.getProjectID().intValue() == p.getProjectID().intValue()) {
                    break;
                }
                index++;
            }
            spinner.setSelection(index + 1);
        }
        if (beneficiary.getProjectSite() != null) {
            if (project.getProjectSiteList() != null && !project.getProjectSiteList().isEmpty()) {
                int index = 0;
                for (ProjectSiteDTO s: project.getProjectSiteList()) {
                    if (beneficiary.getProjectSite().getProjectSiteID().intValue() == s.getProjectSiteID().intValue()) {
                        break;
                    }
                    index++;
                }
                spinner2.setSelection(index + 1);
            }
        }


    }

    private void registerBeneficiary() {
        beneficiary = new BeneficiaryDTO();
        CompanyDTO c = new CompanyDTO();
        c.setCompanyID(SharedUtil.getCompany(context).getCompanyID());
        beneficiary.setCompany(c);
        if (editFirstName.getText().toString().isEmpty()) {
            Util.showToast(context, context.getResources().getString(R.string.enter_firstname));
            return;
        }

        if (editLastName.getText().toString().isEmpty()) {
            Util.showToast(context, context.getResources().getString(R.string.enter_lastname));
            return;
        }
        if (editIDNumber.getText().toString().isEmpty()) {
            Util.showToast(context, context.getResources().getString(R.string.enter_id));
            return;
        }
        if (project == null) {
            Util.showToast(context, context.getString(R.string.select_project));
            return;
        }
        if (site == null) {
            Util.showToast(context, context.getString(R.string.select_site));
            return;
        }

        beneficiary.setFirstName(editFirstName.getText().toString());
        beneficiary.setLastName(editLastName.getText().toString());
        beneficiary.setProjectID(project.getProjectID());
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
                        connectBeneficiaryToSite();
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

    private void connectBeneficiaryToSite() {
        RequestDTO m = new RequestDTO(RequestDTO.CONNECT_BENEFICIARY_TO_SITE);
        m.setProjectSiteID(site.getProjectSiteID());
        m.setBeneficiaryID(beneficiary.getBeneficiaryID());
        beneficiary.setProjectSite(site);
        WebSocketUtil.sendRequest(context, Statics.COMPANY_ENDPOINT,m,new WebSocketUtil.WebSocketListener() {
            @Override
            public void onMessage(final ResponseDTO response) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!ErrorUtil.checkServerError(context,response)) {
                            return;
                        }
                        //TODO - have to update the cache!!!
                        Log.e(LOG, "####### beneficiary connected to site: " + response.getMessage());
                    }
                });

            }

            @Override
            public void onClose() {

            }

            @Override
            public void onError(final String message) {

            }
        });
    }
    private void updateBeneficiary() {

    }

    private void deleteBeneficiary() {

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
                    SpinnerListAdapter adapter1 = new SpinnerListAdapter(context, R.layout.xxsimple_spinner_item, list, SpinnerListAdapter.TASK_LIST, false);
                    if (spinner == null) {
                        spinner = (Spinner) view.findViewById(R.id.ED_PSN_spinner);
                    }
                    spinner.setAdapter(adapter1);
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (position == 0) {
                                project = null;
                            } else {
                                project = projectList.get(position - 1);
                                setSiteSpinner();
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

    private void setSiteSpinner() {
        List<String> list = new ArrayList<>();
        list.add(context.getString(R.string.select_site));
        for (ProjectSiteDTO s: project.getProjectSiteList()) {
            list.add(s.getProjectSiteName());
        }
        SpinnerListAdapter adapter = new SpinnerListAdapter(context, R.layout.xxsimple_spinner_item,list,SpinnerListAdapter.SITE_LIST,false);
        spinner2.setAdapter(adapter);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                   site = null;
                } else {
                    site = project.getProjectSiteList().get(position - 1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    ProjectSiteDTO site;
}

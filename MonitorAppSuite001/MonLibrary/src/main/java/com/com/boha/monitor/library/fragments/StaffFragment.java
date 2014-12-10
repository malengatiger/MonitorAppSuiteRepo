package com.com.boha.monitor.library.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.dto.CompanyStaffDTO;
import com.com.boha.monitor.library.dto.transfer.RequestDTO;
import com.com.boha.monitor.library.dto.transfer.ResponseDTO;
import com.com.boha.monitor.library.util.ErrorUtil;
import com.com.boha.monitor.library.util.SharedUtil;
import com.com.boha.monitor.library.util.Statics;
import com.com.boha.monitor.library.util.Util;
import com.com.boha.monitor.library.util.WebSocketUtil;


public class StaffFragment extends Fragment {

    private StaffFragmentListener listener;

    public StaffFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    View view;
    EditText editFirst, editLast, editCell, editEmail;
    ImageView imgDelete;
    Button btnSave;
    CompanyStaffDTO companyStaff;
    Context ctx;
    ProgressBar progressBar;
    RadioButton radioActive, radioInactive;
    RadioButton radioExec, radioOps, radioProj, radioSite;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.person_edit, container);
        ctx = getActivity();
        setFields();

        return view;
    }

    boolean isUpdate;
    private void sendData() {
        RequestDTO w = new RequestDTO();

        if (!isUpdate) {
            w.setRequestType(RequestDTO.REGISTER_COMPANY_STAFF);
            companyStaff = new CompanyStaffDTO();
            companyStaff.setCompanyID(SharedUtil.getCompany(ctx).getCompanyID());
        } else {
            w.setRequestType(RequestDTO.UPDATE_COMPANY_STAFF);
        }
        if (editFirst.getText().toString().isEmpty()) {
            Util.showToast(ctx, ctx.getString(R.string.enter_firstname));
            return;
        }

        if (editLast.getText().toString().isEmpty()) {
            Util.showToast(ctx, ctx.getResources().getString(R.string.enter_lastname));
            return;
        }

        if (editEmail.getText().toString().isEmpty()) {
            Util.showToast(ctx, ctx.getResources().getString(R.string.enter_email));
            return;
        }

        if (editCell.getText().toString().isEmpty()) {
            Util.showToast(ctx, ctx.getResources().getString(R.string.enter_cell));
            return;
        }

        if (radioActive.isChecked()) {
            companyStaff.setActiveFlag(1);
        }
        if (radioInactive.isChecked()) {
            companyStaff.setActiveFlag(0);
        }
        companyStaff.setFirstName(editFirst.getText().toString());
        companyStaff.setLastName(editLast.getText().toString());
        companyStaff.setCellphone(editCell.getText().toString());
        companyStaff.setEmail(editEmail.getText().toString());

        w.setCompanyStaff(companyStaff);

        progressBar.setVisibility(View.VISIBLE);
        WebSocketUtil.sendRequest(ctx, Statics.COMPANY_ENDPOINT, w, new WebSocketUtil.WebSocketListener() {
            @Override
            public void onMessage(final ResponseDTO response) {
                if (!ErrorUtil.checkServerError(ctx, response)) {
                    return;
                }
                companyStaff = response.getCompanyStaff();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        if (!ErrorUtil.checkServerError(ctx, response)) {
                            return;
                        }
                        if (radioExec.isChecked()) {
                            Util.showConfirmAppInvitationDialog(ctx, getActivity(), companyStaff, Util.EXEC);
                        }
                        if (radioOps.isChecked()) {
                            Util.showConfirmAppInvitationDialog(ctx, getActivity(), companyStaff, Util.OPS);
                        }
                        if (radioProj.isChecked()) {
                            Util.showConfirmAppInvitationDialog(ctx, getActivity(), companyStaff, Util.PROJ);
                        }
                        if (radioSite.isChecked()) {
                            Util.showConfirmAppInvitationDialog(ctx, getActivity(), companyStaff, Util.SITE);
                        }
                        if (isUpdate) {
                            listener.onStaffUpdated(companyStaff);
                        } else {
                            listener.onStaffAdded(companyStaff);
                        }
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
                        Util.showErrorToast(ctx, message);
                    }
                });

            }
        });

    }


    private void deleteStaff() {

    }

    public void setCompanyStaff(CompanyStaffDTO companyStaff) {
        this.companyStaff = companyStaff;
        if (companyStaff == null) {
            isUpdate = false;
            return;
        } else {
            isUpdate = true;
        }
        editFirst.setText(companyStaff.getFirstName());
        editLast.setText(companyStaff.getLastName());
        editCell.setText(companyStaff.getCellphone());
        editEmail.setText(companyStaff.getEmail());
        imgDelete.setVisibility(View.VISIBLE);
    }

    private void setFields() {
        editFirst = (EditText) view.findViewById(R.id.ED_PSN_firstName);
        editLast = (EditText) view.findViewById(R.id.ED_PSN_lastName);
        editCell = (EditText) view.findViewById(R.id.ED_PSN_cellphone);
        editEmail = (EditText) view.findViewById(R.id.ED_PSN_email);
        btnSave = (Button) view.findViewById(R.id.ED_PSN_btnSave);

        radioActive = (RadioButton) view.findViewById(R.id.ED_PSN_radioActive);
        radioInactive = (RadioButton) view.findViewById(R.id.ED_PSN_radioInactive);

        radioExec = (RadioButton) view.findViewById(R.id.ED_PSN_chkExec);
        radioOps = (RadioButton) view.findViewById(R.id.ED_PSN_chkOps);
        radioProj = (RadioButton) view.findViewById(R.id.ED_PSN_chkProj);
        radioSite = (RadioButton) view.findViewById(R.id.ED_PSN_chkSite);

        imgDelete = (ImageView) view.findViewById(R.id.ED_PSN_imgDelete);
        imgDelete.setVisibility(View.GONE);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(btnSave, 100, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        sendData();
                    }
                });
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (StaffFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement StaffFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface StaffFragmentListener {
        public void onStaffAdded(CompanyStaffDTO companyStaff);

        public void onStaffUpdated(CompanyStaffDTO companyStaff);
    }

}

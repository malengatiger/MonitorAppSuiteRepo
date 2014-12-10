package com.com.boha.monitor.library.fragments;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.adapters.PopupListAdapter;
import com.com.boha.monitor.library.dto.CompanyStaffDTO;
import com.com.boha.monitor.library.util.ToastUtil;
import com.com.boha.monitor.library.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AppInvitationFragment extends Fragment {

    public AppInvitationFragment() {
    }

    View view;
    CompanyStaffDTO companyStaff;
    List<CompanyStaffDTO> companyStaffList;
    RadioButton appExec, appOps, appProjMgr, appSiteMgr;
    Button btnSave;
    TextView txtStaffName, txtTitle;
    ImageView imgHero;
    List<String> nameList;
    ListPopupWindow pop;
    Context ctx;
    ProgressBar progressBar;
    View titleView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.staff_app_invite, container);
        ctx = getActivity();
        setFields();

        return view;
    }

    private void setFields() {
        titleView = view.findViewById(R.id.SIV_staffLayout);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        txtTitle = (TextView) view.findViewById(R.id.SIV_title);
        txtStaffName = (TextView) view.findViewById(R.id.SIV_staffName);
        btnSave = (Button) view.findViewById(R.id.SIV_btnSave);
        appExec = (RadioButton) view.findViewById(R.id.SIV_appExec);
        appOps = (RadioButton) view.findViewById(R.id.SIV_appOperations);
        appProjMgr = (RadioButton) view.findViewById(R.id.SIV_appProjectMgr);
        appSiteMgr = (RadioButton) view.findViewById(R.id.SIV_appSiteMgr);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashSeveralTimes(btnSave, 200, 2, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        validate();
                    }
                });
            }
        });
        titleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View x) {
                Util.flashOnce(txtStaffName, 100, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        View v = Util.getHeroView(ctx, "Select Staff");
                        pop = new ListPopupWindow(ctx);
                        pop.setAnchorView(txtTitle);
                        pop.setPromptView(v);
                        pop.setPromptPosition(ListPopupWindow.POSITION_PROMPT_ABOVE);
                        pop.setHorizontalOffset(72);
                        //pop.setWidth(600);
                        pop.setAdapter(new PopupListAdapter(ctx, R.layout.xxsimple_spinner_item, nameList, true));
                        pop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                companyStaff = companyStaffList.get(position);
                                txtStaffName.setText(companyStaff.getFullName());
                                appProjMgr.setChecked(false);
                                appSiteMgr.setChecked(false);
                                appProjMgr.setChecked(false);
                                appExec.setChecked(false);
                                Util.flashSeveralTimes(txtStaffName,200,2,null);
                                pop.dismiss();
                            }
                        });
                        pop.show();
                    }
                });


            }
        });
        if (companyStaff != null) {
            txtStaffName.setText(companyStaff.getFullName());
        }
    }



    public void setData(List<CompanyStaffDTO> staffList, int index) {
        this.companyStaffList = staffList;
        companyStaff = staffList.get(index);

        nameList = new ArrayList<>();
        for (CompanyStaffDTO s : companyStaffList) {
            nameList.add(s.getFullName());
        }
        if (txtStaffName != null) {
            txtStaffName.setText(companyStaff.getFullName());
        }
    }

    private void validate() {
        if (!appExec.isChecked() && !appOps.isChecked()
                && !appProjMgr.isChecked() && !appSiteMgr.isChecked()) {
            Util.showToast(ctx, ctx.getString(R.string.select_app));
            return;

        }
        int type = 0;

        if (appExec.isChecked()) {
            type = Util.EXEC;
        }
        if (appOps.isChecked()) {
            type = Util.OPS;
        }

        if (appProjMgr.isChecked()) {
            type = Util.PROJ;
        }
        if (appSiteMgr.isChecked()) {
            type = Util.SITE;
        }

        Util.showConfirmAppInvitationDialog(ctx, getActivity(), companyStaff, type);
    }
    static final String LOG = AppInvitationFragment.class.getSimpleName();


private List<String> links = new ArrayList<String>();
}

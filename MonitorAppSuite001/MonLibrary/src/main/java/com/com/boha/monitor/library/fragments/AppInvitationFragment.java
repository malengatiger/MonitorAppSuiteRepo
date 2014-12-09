package com.com.boha.monitor.library.fragments;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
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
import com.com.boha.monitor.library.dto.transfer.RequestDTO;
import com.com.boha.monitor.library.dto.transfer.ResponseDTO;
import com.com.boha.monitor.library.util.ErrorUtil;
import com.com.boha.monitor.library.util.SharedUtil;
import com.com.boha.monitor.library.util.Statics;
import com.com.boha.monitor.library.util.ToastUtil;
import com.com.boha.monitor.library.util.Util;
import com.com.boha.monitor.library.util.WebSocketUtil;

import java.util.ArrayList;
import java.util.Date;
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
            ToastUtil.toast(ctx, ctx.getString(R.string.select_app));
            return;

        }
        sba = new StringBuilder();
        sba.append(getHeader());

        if (appExec.isChecked()) {
            sba.append(getExecLink());
        }
        if (appOps.isChecked()) {
            sba.append(getOperationsLink());
            sba.append(getFooter());
            showConfirmDialog();
            return;
        }

        if (appProjMgr.isChecked()) {
            sba.append(getProjectManagerLink());
        }
        if (appSiteMgr.isChecked()) {
            sba.append(getSiteManagerLink());
        }
        sba.append(getFooter());

        sendInvitation();
    }
    static final String LOG = AppInvitationFragment.class.getSimpleName();
    private void sendInvitation() {

        Log.w(LOG, "before send intent, sba = \n" + sba.toString());
        final Intent shareIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + companyStaff.getEmail()));
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, ctx.getResources().getString(R.string.subject));
        shareIntent.putExtra(
                Intent.EXTRA_TEXT,
                Html.fromHtml(sba.toString())
        );
        Log.e(LOG, shareIntent.toString());
        startActivity(Intent.createChooser(shareIntent, "Email:"));

        //update app date

        CompanyStaffDTO cs = new CompanyStaffDTO();
        cs.setAppInvitationDate(new Date());
        cs.setCompanyStaffID(companyStaff.getCompanyStaffID());
        RequestDTO w = new RequestDTO();
        w.setRequestType(RequestDTO.UPDATE_COMPANY_STAFF);
        w.setCompanyStaff(cs);


        WebSocketUtil.sendRequest(ctx, Statics.COMPANY_ENDPOINT, w, new WebSocketUtil.WebSocketListener() {
            @Override
            public void onMessage(final ResponseDTO response) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!ErrorUtil.checkServerError(ctx, response)) {
                            return;
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
                        ToastUtil.errorToast(ctx, message);
                    }
                });
            }
        });

    }

    private void showConfirmDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle(ctx.getResources().getString(R.string.operations_app))
                .setMessage(ctx.getResources().getString(R.string.invite_dialog))
                .setPositiveButton(ctx.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sendInvitation();
                    }
                })
                .setNegativeButton(ctx.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
    }
    StringBuilder sba;
    private String getHeader() {
        StringBuilder sb = new StringBuilder();
        sb.append("<h2>").append(SharedUtil.getCompany(ctx).getCompanyName()).append("</h2>");
        sb.append("<p>").append(ctx.getResources().getString(R.string.invited)).append("</p>");
        if (appExec.isChecked()) {
            sb.append("<h3>").append(ctx.getString(R.string.exec_app)).append("</h3>");
        }
        if (appOps.isChecked()) {
            sb.append("<h3>").append(ctx.getString(R.string.operations_app)).append("</h3>");
        }
        if (appProjMgr.isChecked()) {
            sb.append("<h3>").append(ctx.getString(R.string.pm_app)).append("</h3>");
        }
        if (appSiteMgr.isChecked()) {
            sb.append("<h3>").append(ctx.getResources().getString(R.string.supervisor_app)).append("</h3>");
        }

        return sb.toString();
    }

    private String getFooter() {
        StringBuilder sb = new StringBuilder();
        sb.append(ctx.getString(R.string.contact_us));
        sb.append("<h2>").append(ctx.getResources().getString(R.string.enjoy)).append("</h2>");
        return sb.toString();
    }

    private String getSiteManagerLink() {
        StringBuilder sb = new StringBuilder();
        sb.append("<p>").append(ctx.getResources().getString(R.string.click_link)).append("</p>");
        sb.append("<p>").append(Statics.INVITE_SITE_MGR).append("</p>");
        return sb.toString();
    }

    private String getProjectManagerLink() {
        StringBuilder sb = new StringBuilder();
        sb.append("<p>").append(ctx.getResources().getString(R.string.click_link)).append("</p>");
        sb.append("<p>").append(Statics.INVITE_PROJECT_MGR).append("</p>");
        sb.append(getPinNote());
        return sb.toString();
    }

    private String getPinNote() {
        StringBuilder sb = new StringBuilder();
        sb.append("<p>").append(ctx.getResources().getString(R.string.pin_note)).append("</p>");
        sb.append("<h4>").append(companyStaff.getPin()).append("</h4>");
        return sb.toString();
    }

    private String getOperationsLink() {
        StringBuilder sb = new StringBuilder();
        sb.append("<p>").append(ctx.getResources().getString(R.string.click_link)).append("</p>");
        sb.append("<p>").append(Statics.INVITE_OPERATIONS_MGR).append("</p>");
        sb.append(getPinNote());
        return sb.toString();
    }

    private String getExecLink() {
        StringBuilder sb = new StringBuilder();
        sb.append("<p>").append(ctx.getResources().getString(R.string.click_link)).append("</p>");
        sb.append("<p>").append(Statics.INVITE_EXEC).append("</p>");
        sb.append(getPinNote());
        return sb.toString();
    }

private List<String> links = new ArrayList<String>();
}

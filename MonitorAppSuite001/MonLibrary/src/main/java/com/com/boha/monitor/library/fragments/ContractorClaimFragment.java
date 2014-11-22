package com.com.boha.monitor.library.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.adapters.ProjectSiteSelectionAdapter;
import com.com.boha.monitor.library.adapters.SpinnerListAdapter;
import com.com.boha.monitor.library.dto.ContractorClaimDTO;
import com.com.boha.monitor.library.dto.ContractorClaimSiteDTO;
import com.com.boha.monitor.library.dto.EngineerDTO;
import com.com.boha.monitor.library.dto.ProjectDTO;
import com.com.boha.monitor.library.dto.ProjectSiteDTO;
import com.com.boha.monitor.library.dto.TaskDTO;
import com.com.boha.monitor.library.dto.transfer.RequestDTO;
import com.com.boha.monitor.library.dto.transfer.ResponseDTO;
import com.com.boha.monitor.library.util.CacheUtil;
import com.com.boha.monitor.library.util.ErrorUtil;
import com.com.boha.monitor.library.util.SharedUtil;
import com.com.boha.monitor.library.util.Statics;
import com.com.boha.monitor.library.util.ToastUtil;
import com.com.boha.monitor.library.util.Util;
import com.com.boha.monitor.library.util.WebSocketUtil;
import com.fourmob.datetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ContractorClaimFragment extends Fragment implements PageFragment {

    private ContractorClaimFragmentListener mListener;

    public ContractorClaimFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    Context ctx;
    View view;
    TextView txtCount, txtName;
    ProjectDTO project;
    List<ProjectDTO> projectList;
    TaskDTO task;
    List<TaskDTO> taskList;
    ProjectSiteDTO site;
    List<ProjectSiteDTO> siteList;
    EngineerDTO engineer;
    List<EngineerDTO> engineerList;
    Date claimDate;
    Button btnDate, btnSave;
    CheckBox chkSelectAll;
    ImageView imgMore;
    ListView siteListView;
    Spinner taskSpinner, engineerSpinner;
    TextView txtProject;
    ContractorClaimDTO contractorClaim;
    ProjectSiteSelectionAdapter adapter;
    static final Locale locale = Locale.getDefault();
    static final SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy", locale);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_contractor_claim, container, false);
        ctx = getActivity();
        setFields();

        Bundle b = getArguments();
        if (b != null) {
            project = (ProjectDTO) b.getSerializable("project");
        }
        getData();
        return view;
    }

    private void sendData() {
        if (project == null) {
            ToastUtil.toast(ctx,ctx.getString(R.string.select_project));
            return;
        }
        if (engineer == null) {
            ToastUtil.toast(ctx,ctx.getString(R.string.select_engineer));
            return;
        }
        if (task == null) {
            ToastUtil.toast(ctx,ctx.getString(R.string.select_task));
            return;
        }
        if (claimDate == null) {
            showClaimDateDialog();
            return;
        }
        contractorClaim = new ContractorClaimDTO();
        contractorClaim.setProjectID(project.getProjectID());
        contractorClaim.setEngineerID(engineer.getEngineerID());
        contractorClaim.setTaskID(task.getTaskID());
        contractorClaim.setClaimDate(claimDate);
        contractorClaim.setContractorClaimSiteList(new ArrayList<ContractorClaimSiteDTO>());

        for (ProjectSiteDTO s: siteList) {
            if (s.isSelected()) {
                ContractorClaimSiteDTO cc = new ContractorClaimSiteDTO();
                ProjectSiteDTO ps = new ProjectSiteDTO();
                ps.setProjectSiteID(s.getProjectSiteID());
                ps.setProjectID(s.getProjectID());
                cc.setProjectSite(ps);
                contractorClaim.getContractorClaimSiteList().add(cc);
            }
        }


        RequestDTO w = new RequestDTO(RequestDTO.ADD_CONTRACTOR_CLAIM);
        w.setContractorClaim(contractorClaim);
        
        WebSocketUtil.sendRequest(ctx,Statics.COMPANY_ENDPOINT,w,new WebSocketUtil.WebSocketListener() {
            @Override
            public void onMessage(final ResponseDTO response) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!ErrorUtil.checkServerError(ctx,response)) {
                            return;
                        }
                        ToastUtil.toast(ctx, ctx.getString(R.string.claim_saved));
                    }
                });
            }

            @Override
            public void onClose() {

            }

            @Override
            public void onError(String message) {

            }
        });
    }

    DatePickerDialog dpStart;
    int mYear, mMonth, mDay;

    private void showClaimDateDialog() {
        final Calendar calendar = Calendar.getInstance();
        int xYear, xMth, xDay;
        if (mYear == 0) {
            xYear = calendar.get(Calendar.YEAR);
            xMth = calendar.get(Calendar.MONTH);
            xDay = calendar.get(Calendar.DAY_OF_MONTH);
        } else {
            xYear = mYear;
            xMth = mMonth;
            xDay = mDay;
        }
        dpStart = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePickerDialog datePickerDialog,
                                          int year, int month, int day) {

                        mYear = year;
                        mMonth = month;
                        mDay = day;

                        calendar.set(Calendar.YEAR, mYear);
                        calendar.set(Calendar.MONTH, mMonth);
                        calendar.set(Calendar.DAY_OF_MONTH, mDay);
                        calendar.set(Calendar.HOUR, 0);
                        calendar.set(Calendar.MINUTE, 0);
                        calendar.set(Calendar.SECOND, 0);
                        calendar.set(Calendar.MILLISECOND, 0);
                        claimDate = calendar.getTime();
                        btnDate.setText(sdf.format(claimDate));

                    }


                }, xYear, xMth, xDay, true
        );
        dpStart.setVibrate(true);
        dpStart.setYearRange(calendar.get(Calendar.YEAR), 2036);
        Bundle args = new Bundle();
        args.putInt("year", mYear);
        args.putInt("month", mMonth);
        args.putInt("day", mDay);

        dpStart.setArguments(args);
        dpStart.show(getFragmentManager(), "diagx");


    }

    private void getData() {

        CacheUtil.getCachedData(ctx, CacheUtil.CACHE_DATA, new CacheUtil.CacheUtilListener() {
            @Override
            public void onFileDataDeserialized(ResponseDTO response) {
                if (response != null) {
                    taskList = response.getCompany().getTaskList();
                    engineerList = response.getCompany().getEngineerList();
                    setSpinners();
                }
                CacheUtil.getCachedProjectData(ctx,CacheUtil.CACHE_PROJECT,project.getProjectID(),new CacheUtil.CacheUtilListener() {
                    @Override
                    public void onFileDataDeserialized(ResponseDTO response) {
                        if (response != null) {
                            siteList = response.getProjectSiteList();
                            setList();
                        }
                        refreshProjectData();
                    }

                    @Override
                    public void onDataCached() {

                    }

                    @Override
                    public void onError() {

                    }
                });
            }

            @Override
            public void onDataCached() {

            }

            @Override
            public void onError() {

            }
        });
    }

    private void refreshProjectData() {
        RequestDTO w = new RequestDTO(RequestDTO.GET_PROJECT_SITE_DATA);
        w.setProjectID(project.getProjectID());

        WebSocketUtil.sendRequest(ctx, Statics.COMPANY_ENDPOINT, w, new WebSocketUtil.WebSocketListener() {
            @Override
            public void onMessage(final ResponseDTO response) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!ErrorUtil.checkServerError(ctx, response)) {
                            return;
                        }
                        siteList = response.getProjectSiteList();
                        setList();
                        CacheUtil.cacheProjectData(ctx, response, CacheUtil.CACHE_PROJECT, project.getProjectID(), new CacheUtil.CacheUtilListener() {
                            @Override
                            public void onFileDataDeserialized(ResponseDTO response) {

                            }

                            @Override
                            public void onDataCached() {


                            }

                            @Override
                            public void onError() {

                            }
                        });
                    }
                });
            }

            @Override
            public void onClose() {

            }

            @Override
            public void onError(String message) {

            }
        });
    }
    private void refreshCompanyData() {
        RequestDTO w = new RequestDTO(RequestDTO.GET_COMPANY_DATA);
        w.setCompanyID(SharedUtil.getCompany(ctx).getCompanyID());

        WebSocketUtil.sendRequest(ctx, Statics.COMPANY_ENDPOINT, w, new WebSocketUtil.WebSocketListener() {
            @Override
            public void onMessage(final ResponseDTO response) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!ErrorUtil.checkServerError(ctx, response)) {
                            return;
                        }
                        CacheUtil.cacheData(ctx, response, CacheUtil.CACHE_DATA, new CacheUtil.CacheUtilListener() {
                            @Override
                            public void onFileDataDeserialized(ResponseDTO response) {

                            }

                            @Override
                            public void onDataCached() {
                                projectList = response.getCompany().getProjectList();

                                taskList = response.getCompany().getTaskList();
                                engineerList = response.getCompany().getEngineerList();
                                setSpinners();
                            }

                            @Override
                            public void onError() {

                            }
                        });
                    }
                });
            }

            @Override
            public void onClose() {

            }

            @Override
            public void onError(String message) {

            }
        });
    }

    private void setSpinners() {
        siteList = project.getProjectSiteList();
        setList();
        chkSelectAll.setChecked(false);
        txtCount.setText("0");

        txtProject.setText(project.getProjectName());
        List<String> engineers = new ArrayList<>();
        engineers.add(ctx.getString(R.string.select_engineer));
        for (EngineerDTO p : engineerList) {
            engineers.add(p.getEngineerName());
        }
        SpinnerListAdapter a2 = new SpinnerListAdapter(ctx, R.layout.xxsimple_spinner_item_blue, engineers);
        engineerSpinner.setAdapter(a2);
        engineerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    engineer = null;
                } else {
                    engineer = engineerList.get(position - 1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        List<String> tasks = new ArrayList<>();
        tasks.add(ctx.getString(R.string.select_task));
        for (TaskDTO p : taskList) {
            tasks.add(p.getTaskName());
        }
        SpinnerListAdapter a3 = new SpinnerListAdapter(ctx, R.layout.xxsimple_spinner_item, tasks);
        taskSpinner.setAdapter(a3);
        taskSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    task = null;
                } else {
                    task = taskList.get(position - 1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setFields() {
        imgMore = (ImageView)view.findViewById(R.id.CCX_imgMore);
        txtCount = (TextView) view.findViewById(R.id.CCX_siteCount);
        txtProject = (TextView) view.findViewById(R.id.CCX_projectName);
        engineerSpinner = (Spinner) view.findViewById(R.id.CCX_engineerSpinner);
        taskSpinner = (Spinner) view.findViewById(R.id.CCX_taskSpinner);
        btnDate = (Button) view.findViewById(R.id.CCX_btnDate);
        btnSave = (Button) view.findViewById(R.id.CCX_btnSave);
        siteListView = (ListView) view.findViewById(R.id.CCX_list);
        chkSelectAll = (CheckBox) view.findViewById(R.id.CCX_chkAll);
        txtCount.setText("0");
        TextView title = (TextView)view.findViewById(R.id.CCX_title);
        Statics.setRobotoFontLight(ctx,title);
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showClaimDateDialog();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData();
            }
        });
        chkSelectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int count = 0;
                if (isChecked) {
                    for (ProjectSiteDTO s: siteList) {
                        s.setSelected(true);
                    }
                } else {
                    for (ProjectSiteDTO s: siteList) {
                        s.setSelected(false);
                    }
                }
                txtCount.setText("" + count);
                adapter.notifyDataSetChanged();
            }
        });
        imgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMoreClicked();
            }
        });
    }

    private void onMoreClicked() {
        if (!isListOpen) {
            animateOff();
            isListOpen = true;

        } else {
            animateOn();
            isListOpen = false;
        }
    }
    View top, bottom;
    private void animateOn() {
        if (top == null) {
            top = view.findViewById(R.id.CCX_middle);
            bottom = view.findViewById(R.id.CCX_bottom);
        }
        top.setVisibility(View.VISIBLE);
        bottom.setVisibility(View.VISIBLE);

//        final ObjectAnimator aTop = ObjectAnimator.ofFloat(top, "scaleY", 0, 1);
//        final ObjectAnimator aBottom = ObjectAnimator.ofFloat(bottom, "scaleY", 0, 1);
//
//        List<Animator> list = new ArrayList<>();
//        list.add(aTop);
//        list.add(aBottom);
//
//        AnimatorSet set = new AnimatorSet();
//        set.playTogether(list);
//
//        set.start();
    }
    private void animateOff() {
        if (top == null) {
            top = view.findViewById(R.id.CCX_middle);
            bottom = view.findViewById(R.id.CCX_bottom);
        }
        top.setVisibility(View.GONE);
        bottom.setVisibility(View.GONE);

//        if (top == null) {
//            top = view.findViewById(R.id.CCX_top);
//            bottom = view.findViewById(R.id.CCX_bottom);
//        }
//        final ObjectAnimator aTop = ObjectAnimator.ofFloat(top, "scaleY", 1, 0);
//        final ObjectAnimator aBottom = ObjectAnimator.ofFloat(bottom, "scaleY", 1, 0);
//
//        List<Animator> list = new ArrayList<>();
//        list.add(aTop);
//        list.add(aBottom);
//
//        AnimatorSet set = new AnimatorSet();
//        set.playTogether(list);
//        set.start();

    }
    boolean isListOpen;
    private void setList() {

        adapter = new ProjectSiteSelectionAdapter(ctx, R.layout.project_site_item_select,
                siteList, new ProjectSiteSelectionAdapter.ProjectSiteSelectionAdapterListener() {
            @Override
            public void onCheckBoxChange(ProjectSiteDTO site, int index) {
                int count = 0;
                for (ProjectSiteDTO s : siteList) {
                    if (s.isSelected()) {
                        count++;
                    }
                }
                txtCount.setText("" + count);
            }
        });
        siteListView.setAdapter(adapter);

        siteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        txtCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.toast(ctx, "Under Construction");
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (ContractorClaimFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Host " + activity.getLocalClassName()
                    + " must implement ContractorClaimFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = siteListView.getEmptyView();

        if (emptyText instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    @Override
    public void animateCounts() {
        Util.animateRotationY(txtCount, 500);

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface ContractorClaimFragmentListener {
        public void onContractorClaimAdded(ContractorClaimDTO contractorClaimDTO);

        public void onContractorClaimUpdated(ContractorClaimDTO contractorClaimDTO);

        public void onContractorClaimDeleted(ContractorClaimDTO contractorClaimDTO);
    }


}

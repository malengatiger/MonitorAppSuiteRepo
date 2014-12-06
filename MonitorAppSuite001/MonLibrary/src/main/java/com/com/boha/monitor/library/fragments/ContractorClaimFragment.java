package com.com.boha.monitor.library.fragments;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
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
import com.com.boha.monitor.library.util.ErrorUtil;
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
    ImageView imgLogo;
    ObjectAnimator objectAnimator;
    static final Locale locale = Locale.getDefault();
    static final SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy", locale);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.w(LOG,"###### onCreateView");
        view = inflater.inflate(R.layout.fragment_contractor_claim, container, false);
        ctx = getActivity();
        setFields();
        return view;
    }

    @Override
    public void onResume() {
        Log.w(LOG, "############ onResume");
        super.onResume();
    }
    @Override
    public void onSaveInstanceState(Bundle b) {
        Log.e(LOG, "############ onSaveInstanceState");
        ResponseDTO r = new ResponseDTO();
        r.setEngineerList(engineerList);
        r.setTaskList(taskList);
        r.setProjectSiteList(siteList);
        b.putSerializable("response",r);
        super.onSaveInstanceState(b);
    }

    private void sendData() {

        if (project == null) {
            ToastUtil.toast(ctx, ctx.getString(R.string.select_project));
            return;
        }
        if (engineer == null) {
            ToastUtil.toast(ctx, ctx.getString(R.string.select_engineer));
            return;
        }
        if (task == null) {
            ToastUtil.toast(ctx, ctx.getString(R.string.select_task));
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

        for (ProjectSiteDTO s : siteList) {
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

        WebSocketUtil.sendRequest(ctx, Statics.COMPANY_ENDPOINT, w, new WebSocketUtil.WebSocketListener() {
            @Override
            public void onMessage(final ResponseDTO response) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!ErrorUtil.checkServerError(ctx, response)) {
                            return;
                        }
                        contractorClaim = response.getContractorClaimList().get(0);
                        mListener.onContractorClaimAdded(contractorClaim);
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

    static final String FILENAME = "temp.pdf";

    DatePickerDialog dpStart;
    int mYear, mMonth, mDay;
    static final String LOG = ContractorClaimFragment.class.getSimpleName();

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

    List<String> engineers;
    List<String> tasks;

    public void setProject(ProjectDTO p) {
        project = p;
        if (project != null) {
            txtProject.setText(project.getProjectName());
            siteList = project.getProjectSiteList();
            chkSelectAll.setChecked(false);
            txtCount.setText("0");
            setList();
        }
    }
    public void setData(List<EngineerDTO> engineers,
                        List<TaskDTO> tasks) {
        engineerList = engineers;
        taskList = tasks;
        if (engineers != null) {
            setSpinners();
        }
    }
    private void setSpinners() {
        engineers = new ArrayList<>();
        engineers.add(ctx.getString(R.string.select_engineer));
        for (EngineerDTO p : engineerList) {
            engineers.add(p.getEngineerName());
        }
        Log.w(LOG,"##### setting engineer dropdown");
        SpinnerListAdapter a2 = new SpinnerListAdapter(ctx, R.layout.xxsimple_spinner_item_blue, engineers, SpinnerListAdapter.ENGINEER_LIST, true);
        engineerSpinner.setAdapter(a2);
        engineerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.w(LOG,"##### engineer item selected: " + position);
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
        //
        tasks = new ArrayList<>();
        tasks.add(ctx.getString(R.string.select_task));
        for (TaskDTO p : taskList) {
            tasks.add(p.getTaskName());
        }
        Log.w(LOG,"##### setting task dropdown");
        SpinnerListAdapter a3 = new SpinnerListAdapter(ctx, R.layout.xxsimple_spinner_item, tasks, SpinnerListAdapter.TASK_LIST, true);
        taskSpinner.setAdapter(a3);
        taskSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.w(LOG,"##### task item selected: " + position);
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
        imgMore = (ImageView) view.findViewById(R.id.CCX_imgMore);
        imgLogo = (ImageView) view.findViewById(R.id.CCX_logo);
        imgLogo.setVisibility(View.GONE);
        txtCount = (TextView) view.findViewById(R.id.CCX_siteCount);
        txtProject = (TextView) view.findViewById(R.id.CCX_projectName);
        engineerSpinner = (Spinner) view.findViewById(R.id.CCX_engineerSpinner);
        taskSpinner = (Spinner) view.findViewById(R.id.CCX_taskSpinner);
        btnDate = (Button) view.findViewById(R.id.CCX_btnDate);
        btnSave = (Button) view.findViewById(R.id.CCX_btnSave);
        siteListView = (ListView) view.findViewById(R.id.CCX_list);
        chkSelectAll = (CheckBox) view.findViewById(R.id.CCX_chkAll);
        txtCount.setText("0");
        TextView title = (TextView) view.findViewById(R.id.CCX_title);
        Statics.setRobotoFontLight(ctx, title);
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
                    for (ProjectSiteDTO s : siteList) {
                        s.setSelected(true);
                    }
                } else {
                    for (ProjectSiteDTO s : siteList) {
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

    public void rotateLogo() {
        imgLogo.setVisibility(View.VISIBLE);
        objectAnimator = ObjectAnimator.ofFloat(imgLogo, "rotation", 0.0f, 360f);
        objectAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        objectAnimator.setDuration(200);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        objectAnimator.start();
    }

    public void stopRotatingLogo() {
        imgLogo.setVisibility(View.GONE);
        objectAnimator.cancel();
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
//        List<Animator> taskStatusList = new ArrayList<>();
//        taskStatusList.add(aTop);
//        taskStatusList.add(aBottom);
//
//        AnimatorSet set = new AnimatorSet();
//        set.playTogether(taskStatusList);
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
//        List<Animator> taskStatusList = new ArrayList<>();
//        taskStatusList.add(aTop);
//        taskStatusList.add(aBottom);
//
//        AnimatorSet set = new AnimatorSet();
//        set.playTogether(taskStatusList);
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
     * the taskStatusList is empty. If you would like to change the text, call this method
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
     * fragment to allow logoAnimator interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface ContractorClaimFragmentListener {
        public void onContractorClaimAdded(ContractorClaimDTO contractorClaimDTO);

        public void onContractorClaimUpdated(ContractorClaimDTO contractorClaimDTO);

        public void onContractorClaimDeleted(ContractorClaimDTO contractorClaimDTO);
    }


}

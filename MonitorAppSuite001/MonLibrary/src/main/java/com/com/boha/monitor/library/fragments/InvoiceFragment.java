package com.com.boha.monitor.library.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.com.boha.monitor.library.dto.InvoiceDTO;
import com.com.boha.monitor.library.dto.InvoiceItemDTO;
import com.com.boha.monitor.library.dto.ProjectDTO;
import com.com.boha.monitor.library.dto.ProjectSiteDTO;
import com.com.boha.monitor.library.dto.TaskDTO;
import com.com.boha.monitor.library.dto.TaskPriceDTO;
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

public class InvoiceFragment extends Fragment implements PageFragment {

    private InvoiceFragmentListener mListener;

    public InvoiceFragment() {
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
    TaskPriceDTO taskPrice;
    List<TaskPriceDTO> taskPriceList;
    ContractorClaimDTO claim;
    Date claimDate;
    Button btnDate, btnSave;
    CheckBox chkSelectAll;
    ImageView imgMore;
    ListView siteListView;
    Spinner taskSpinner, engineerSpinner;
    TextView txtProject;
    InvoiceDTO invoice;
    ProjectSiteSelectionAdapter adapter;
    static final Locale locale = Locale.getDefault();
    static final SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy", locale);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.w(LOG,"###### onCreateView");
        view = inflater.inflate(R.layout.fragment_invoice, container, false);
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
        super.onSaveInstanceState(b);
    }

    private void sendData() {
        if (task == null) {
            Util.showToast(ctx, ctx.getString(R.string.select_task));
            return;
        }
        if (claimDate == null) {
            showClaimDateDialog();
            return;
        }

        for (ProjectSiteDTO s : siteList) {
            if (s.isSelected()) {
                InvoiceItemDTO cc = new InvoiceItemDTO();
                ProjectSiteDTO ps = new ProjectSiteDTO();
                ps.setProjectSiteID(s.getProjectSiteID());
                ps.setProjectID(s.getProjectID());
                cc.setProjectSite(ps);
                invoice.getInvoiceItemList().add(cc);
            }
        }


        RequestDTO w = new RequestDTO(RequestDTO.ADD_INVOICE);
        w.setInvoice(invoice);

        WebSocketUtil.sendRequest(ctx, Statics.COMPANY_ENDPOINT, w, new WebSocketUtil.WebSocketListener() {
            @Override
            public void onMessage(final ResponseDTO response) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!ErrorUtil.checkServerError(ctx, response)) {
                            return;
                        }
                        invoice = response.getInvoiceList().get(0);
                        mListener.onInvoicemAdded(invoice);
                    }
                });
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
                        Util.showErrorToast(ctx,message);
                    }
                });
            }
        });
    }

    static final String FILENAME = "temp.pdf";

    DatePickerDialog dpStart;
    int mYear, mMonth, mDay;
    static final String LOG = InvoiceFragment.class.getSimpleName();

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
    public void setTaskList(List<TaskDTO> tasks) {
        taskList = tasks;
        if (tasks != null) {
            setSpinner();
        }
    }
    private void setSpinner() {
        tasks = new ArrayList<>();
        tasks.add(ctx.getString(R.string.select_task));
        for (TaskDTO p : taskList) {
            tasks.add(p.getTaskName());
        }
        Log.w(LOG,"##### setting task dropdown");
        ArrayAdapter<String> sad2 = new ArrayAdapter<String>(ctx,android.R.layout.simple_spinner_item, tasks);
        SpinnerListAdapter a3 = new SpinnerListAdapter(ctx, R.layout.xxsimple_spinner_item, tasks, SpinnerListAdapter.TASK_LIST, false);
        taskSpinner.setAdapter(a3);
        taskSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.w(LOG, "##### task item selected: " + position);
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
        imgMore = (ImageView) view.findViewById(R.id.INV_imgMore);
        txtCount = (TextView) view.findViewById(R.id.INV_siteCount);
        txtProject = (TextView) view.findViewById(R.id.INV_projectName);
        taskSpinner = (Spinner) view.findViewById(R.id.INV_taskSpinner);
        btnDate = (Button) view.findViewById(R.id.INV_btnDate);
        btnSave = (Button) view.findViewById(R.id.INV_btnSave);
        siteListView = (ListView) view.findViewById(R.id.INV_list);
        chkSelectAll = (CheckBox) view.findViewById(R.id.INV_chkAll);
        txtCount.setText("0");
        TextView title = (TextView) view.findViewById(R.id.INV_title);
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

    View top, bottom;

    private void animateOn() {
        if (top == null) {
            top = view.findViewById(R.id.INV_middle);
            bottom = view.findViewById(R.id.INV_bottom);
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
            top = view.findViewById(R.id.INV_middle);
            bottom = view.findViewById(R.id.INV_bottom);
        }
        top.setVisibility(View.GONE);
        bottom.setVisibility(View.GONE);

//        if (top == null) {
//            top = view.findViewById(R.id.INV_top);
//            bottom = view.findViewById(R.id.INV_bottom);
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
                Util.showToast(ctx, "Under Construction");
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (InvoiceFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Host " + activity.getLocalClassName()
                    + " must implement InvoiceFragmentListener");
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
    public interface InvoiceFragmentListener {
        public void onInvoicemAdded(InvoiceDTO invoice);

        public void onInvoiceUpdated(InvoiceDTO invoice);

        public void onInvoiceDeleted(InvoiceDTO invoice);
    }


}

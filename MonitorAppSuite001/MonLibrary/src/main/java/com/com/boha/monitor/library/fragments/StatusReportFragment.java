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
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.TextView;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.adapters.PopupListAdapter;
import com.com.boha.monitor.library.adapters.StatusReportAdapter;
import com.com.boha.monitor.library.dto.ProjectDTO;
import com.com.boha.monitor.library.dto.ProjectSiteDTO;
import com.com.boha.monitor.library.dto.ProjectSiteTaskStatusDTO;
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
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class StatusReportFragment extends Fragment implements PageFragment {

    public interface StatusReportListener {

    }


    private StatusReportListener mListener;

    public StatusReportFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    Context ctx;
    TextView txtCount, txtSiteName;
    ImageView heroImage;
    ProjectSiteDTO projectSite;
    ProjectDTO project;
    ImageView imgLogo;
    TextView txtProject,txtEmpty;
    ListView listView;
    LayoutInflater inflater;
    StatusReportAdapter adapter;
    Button btnStart, btnEnd;
    Date startDate, endDate;
    View view;
    static final Locale locale = Locale.getDefault();
    static final SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", locale);
    List<ProjectDTO> projectList;
    ListPopupWindow popupWindow;
    ObjectAnimator objectAnimator;

    private void rotateLogo() {
        objectAnimator = ObjectAnimator.ofFloat(imgLogo, "rotation", 0.0f, 360f);
        objectAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        objectAnimator.setDuration(200);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        objectAnimator.start();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG, "########## onCreateView");
        this.inflater = inflater;
        ctx = getActivity();
        view = inflater.inflate(R.layout.fragment_status_list, container, false);
        setFields();
        Bundle b = getArguments();
        if (b != null) {
            Log.d(LOG, "########## onCreateView, bundle not null");
            ResponseDTO r = (ResponseDTO)b.getSerializable("response");
            if (r.getCompany() == null) {
                Log.e(LOG, "########## onCreateView, company is NULL");
                return view;
            }
            projectList = r.getCompany().getProjectList();
            if (projectList != null && !projectList.isEmpty()) {
                project = projectList.get(0);
                txtProject.setText(project.getProjectName());
                getProjectStatus();
            }
        }

        return view;
    }

    private void showPopup() {
        List<String> list = new ArrayList<>();
        for (ProjectDTO p: projectList) {
            list.add(p.getProjectName());
        }
        popupWindow = new ListPopupWindow(ctx);
        popupWindow.setAnchorView(imgLogo);
        popupWindow.setHorizontalOffset(72);
        popupWindow.setWidth(700);
        popupWindow.setAdapter(new PopupListAdapter(ctx, R.layout.xxsimple_spinner_item,list,false));
        popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                project = projectList.get(position);
                txtProject.setText(project.getProjectName());
                getProjectStatus();
                popupWindow.dismiss();
            }
        });
        popupWindow.show();
    }
    private void setFields() {
        listView = (ListView) view.findViewById(R.id.STATLST_list);
        heroImage = (ImageView) view.findViewById(R.id.STATLST_heroImage);
        imgLogo = (ImageView) view.findViewById(R.id.STATLST_logo);
        txtProject = (TextView) view.findViewById(R.id.STATLST_project);
        txtCount = (TextView) view.findViewById(R.id.STATLST_txtCount);
        txtEmpty = (TextView) view.findViewById(R.id.STATLST_txtEmpty);
        btnEnd = (Button) view.findViewById(R.id.STATLST_endDate);
        btnStart = (Button) view.findViewById(R.id.STATLST_startDate);
        txtSiteName = (TextView) view.findViewById(R.id.STATLST_txtTitle);

        Statics.setRobotoFontLight(ctx,txtProject);
        Statics.setRobotoFontLight(ctx,btnEnd);
        Statics.setRobotoFontLight(ctx,btnStart);

        Calendar cal = GregorianCalendar.getInstance();
        endDate = new Date();
        for (int i = 0; i < 7; i++) {
            cal.roll(Calendar.DAY_OF_YEAR, false);
        }
        startDate = cal.getTime();
        btnStart.setText(sdf.format(startDate));
        btnEnd.setText(sdf.format(endDate));
        btnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStartDate = false;
                showDateDialog();
            }
        });
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStartDate = true;
                showDateDialog();
            }
        });

        txtCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                objectAnimator = ObjectAnimator.ofFloat(txtCount,"alpha",1,0);
                objectAnimator.setRepeatCount(ObjectAnimator.REVERSE);
                objectAnimator.setDuration(100);
                objectAnimator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        getProjectStatus();
                        txtCount.setAlpha(1.0f);
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
        });
        txtProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup();
            }
        });

    }

    boolean isHidden = false, isStartDate;
    Random random;
    List<ProjectSiteTaskStatusDTO> projectSiteTaskStatusList;

    public void getProjectStatus() {
        RequestDTO w = new RequestDTO(RequestDTO.GET_PROJECT_STATUS_IN_PERIOD);
        w.setProjectID(project.getProjectID());
        w.setEndDate(endDate);
        w.setStartDate(startDate);
        final Activity act = getActivity();
        rotateLogo();
        WebSocketUtil.sendRequest(ctx, Statics.COMPANY_ENDPOINT, w, new WebSocketUtil.WebSocketListener() {
            @Override
            public void onMessage(final ResponseDTO response) {
                if (act == null) {
                    Log.e(LOG,"-------- state ERROR - onMessage getActivity is NULL");
                    return;
                }
                act.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        objectAnimator.cancel();
                        if (!ErrorUtil.checkServerError(ctx, response)) {
                            return;
                        }
                        projectSiteTaskStatusList = response.getProjectSiteTaskStatusList();
                        if (!projectSiteTaskStatusList.isEmpty()) {
                            txtEmpty.setVisibility(View.GONE);
                        } else {
                            txtEmpty.setVisibility(View.VISIBLE);
                        }
                        txtCount.setText("" + projectSiteTaskStatusList.size());
                        setList();
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
                        Util.showErrorToast(ctx, message);
                    }
                });
            }
        });
    }

    private void setList() {
        Log.d(LOG, "########## setList");

        txtCount.setText("" + projectSiteTaskStatusList.size());
        adapter = new StatusReportAdapter(ctx, R.layout.status_report_card, projectSiteTaskStatusList);
        listView.setAdapter(adapter);
        heroImage.setImageDrawable(ctx.getResources().getDrawable(R.drawable.house));
//        random = new Random(System.currentTimeMillis());
//        if (projectSite.getPhotoUploadList() != null && !projectSite.getPhotoUploadList().isEmpty()) {
//            int index = random.nextInt(projectSite.getPhotoUploadList().size() - 1);
//            String uri = Statics.IMAGE_URL + projectSite.getPhotoUploadList()
//                    .get(0).getUri();
//            heroImage.setVisibility(View.VISIBLE);
//            ImageLoader.getInstance().displayImage(uri, heroImage);
//            heroImage.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //listener.onGalleryRequested(p,position);
//                }
//            });
//        } else {
//            heroImage.setImageDrawable(ctx.getResources().getDrawable(R.drawable.house));
//        }


    }

    DatePickerDialog dpStart;
    int mYear, mMonth, mDay;

    private void showDateDialog() {
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
                        if (isStartDate) {
                            startDate = calendar.getTime();
                            btnStart.setText(sdf.format(startDate));
                        } else {
                            endDate = calendar.getTime();
                            btnEnd.setText(sdf.format(endDate));
                        }


                    }


                }, xYear, xMth, xDay, true
        );
        dpStart.setVibrate(true);
        dpStart.setYearRange(2013, calendar.get(Calendar.YEAR));
        Bundle args = new Bundle();
        args.putInt("year", mYear);
        args.putInt("month", mMonth);
        args.putInt("day", mDay);

        dpStart.setArguments(args);
        dpStart.show(getFragmentManager(), "diagx");


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (StatusReportListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Host " + activity.getLocalClassName()
                    + " must implement StatusReportListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    TaskDTO task;
    int staffType;
    public static final int OPERATIONS = 1, PROJECT_MANAGER = 2, SITE_SUPERVISOR = 3;
    static final String LOG = StatusReportFragment.class.getSimpleName();

    @Override
    public void animateCounts() {
        Util.animateRotationY(txtCount, 500);

    }

}

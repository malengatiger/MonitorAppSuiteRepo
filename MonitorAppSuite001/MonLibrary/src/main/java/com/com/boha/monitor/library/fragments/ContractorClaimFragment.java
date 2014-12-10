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
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.TextView;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.adapters.PopupListAdapter;
import com.com.boha.monitor.library.adapters.ProjectSiteSelectionAdapter;
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
    View view, topView, middleView;
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
    TextView txtTaskName, txtEngineerName;
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
        Log.w(LOG, "###### onCreateView");
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
        b.putSerializable("response", r);
        super.onSaveInstanceState(b);
    }

    private void sendData() {

        if (project == null) {
            Util.showToast(ctx, ctx.getString(R.string.select_project));
            return;
        }
        if (engineer == null) {
            Util.showToast(ctx, ctx.getString(R.string.select_engineer));
            return;
        }
        if (task == null) {
            Util.showToast(ctx, ctx.getString(R.string.select_task));
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

    }


    private void showTaskPopup() {
        List<String> list = new ArrayList<>();
        for (TaskDTO t : taskList) {
            list.add(t.getTaskName());
        }
        View v = getActivity().getLayoutInflater().inflate(R.layout.hero_image, null);
        TextView cap = (TextView) v.findViewById(R.id.HERO_caption);
        cap.setText(ctx.getString(R.string.select_action));
        ImageView img = (ImageView) v.findViewById(R.id.HERO_image);
        img.setImageDrawable(Util.getRandomHeroImage(ctx));

        final ListPopupWindow p = new ListPopupWindow(ctx);
        p.setAnchorView(txtProject);
        p.setPromptView(v);
        p.setPromptPosition(ListPopupWindow.POSITION_PROMPT_ABOVE);
        p.setHorizontalOffset(72);
        p.setWidth(720);
        p.setAdapter(new PopupListAdapter(ctx, R.layout.xxsimple_spinner_item, list, false));
        p.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                task = taskList.get(position);
                txtTaskName.setText(task.getTaskName());
                p.dismiss();
                Util.shakeX(btnDate, 50, 5, null);
            }
        });
        p.show();
    }

    private void showEngineerPopup() {
        List<String> list = new ArrayList<>();
        for (EngineerDTO t : engineerList) {
            list.add(t.getEngineerName());
        }
        View v = getActivity().getLayoutInflater().inflate(R.layout.hero_image, null);
        TextView cap = (TextView) v.findViewById(R.id.HERO_caption);
        cap.setText(ctx.getString(R.string.select_action));
        ImageView img = (ImageView) v.findViewById(R.id.HERO_image);
        img.setImageDrawable(Util.getRandomHeroImage(ctx));

        final ListPopupWindow p = new ListPopupWindow(ctx);
        p.setAnchorView(txtProject);
        p.setPromptView(v);
        p.setPromptPosition(ListPopupWindow.POSITION_PROMPT_ABOVE);
        p.setHorizontalOffset(72);
        p.setWidth(720);
        p.setAdapter(new PopupListAdapter(ctx, R.layout.xxsimple_spinner_item, list, false));
        p.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                engineer = engineerList.get(position);
                txtEngineerName.setText(engineer.getEngineerName());
                p.dismiss();
                Util.shakeX(txtTaskName,100,3, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        Util.shakeX(btnDate, 50, 3, null);
                    }
                });

            }
        });
        p.show();
    }

    private void setFields() {
        middleView = view.findViewById(R.id.CCX_middle);
        topView = view.findViewById(R.id.CCX_topTop);
        imgMore = (ImageView) view.findViewById(R.id.CCX_imgMore);
        imgLogo = (ImageView) view.findViewById(R.id.CCX_logo);
        imgLogo.setVisibility(View.GONE);
        txtCount = (TextView) view.findViewById(R.id.CCX_siteCount);
        txtProject = (TextView) view.findViewById(R.id.CCX_projectName);
        txtEngineerName = (TextView) view.findViewById(R.id.CCX_engineerSpinner);
        txtTaskName = (TextView) view.findViewById(R.id.CCX_taskSpinner);
        btnDate = (Button) view.findViewById(R.id.CCX_btnDate);
        btnSave = (Button) view.findViewById(R.id.CCX_btnSave);
        siteListView = (ListView) view.findViewById(R.id.CCX_list);
        chkSelectAll = (CheckBox) view.findViewById(R.id.CCX_chkAll);
        txtCount.setText("0");
        TextView title = (TextView) view.findViewById(R.id.CCX_title);
        Statics.setRobotoFontLight(ctx, title);
        Statics.setRobotoFontLight(ctx, txtEngineerName);
        Statics.setRobotoFontLight(ctx, txtTaskName);
        txtEngineerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(txtEngineerName, 100, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        showEngineerPopup();
                    }
                });
            }
        });
        txtTaskName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(txtTaskName, 100, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        showTaskPopup();
                    }
                });
            }
        });

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Util.flashOnce(btnDate, 100, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        showClaimDateDialog();
                    }
                });
            }
        });
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
                if (adapter != null)
                    adapter.notifyDataSetChanged();
            }
        });
        imgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMoreClicked();
            }
        });

        Util.flashSeveralTimes(txtEngineerName, 200, 3, null);
    }

    private void onMoreClicked() {
        Util.flashOnce(imgMore, 100, new Util.UtilAnimationListener() {
            @Override
            public void onAnimationEnded() {
                if (topView.getVisibility() == View.VISIBLE) {
                    Util.collapse(topView, 1000,null);

                } else {
                    Util.expand(topView, 1000, null);
                }
            }
        });

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
        chkSelectAll.setChecked(true);
        adapter.notifyDataSetChanged();
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

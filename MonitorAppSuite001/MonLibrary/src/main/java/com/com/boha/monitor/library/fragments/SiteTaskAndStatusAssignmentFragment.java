package com.com.boha.monitor.library.fragments;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.adapters.PopupListAdapter;
import com.com.boha.monitor.library.adapters.ProjectSiteTaskAdapter;
import com.com.boha.monitor.library.adapters.SubTaskStatusAdapter;
import com.com.boha.monitor.library.adapters.TaskStatusAdapter;
import com.com.boha.monitor.library.dto.ProjectDTO;
import com.com.boha.monitor.library.dto.ProjectSiteDTO;
import com.com.boha.monitor.library.dto.ProjectSiteTaskDTO;
import com.com.boha.monitor.library.dto.ProjectSiteTaskStatusDTO;
import com.com.boha.monitor.library.dto.SubTaskDTO;
import com.com.boha.monitor.library.dto.SubTaskStatusDTO;
import com.com.boha.monitor.library.dto.TaskDTO;
import com.com.boha.monitor.library.dto.TaskStatusDTO;
import com.com.boha.monitor.library.dto.transfer.PhotoUploadDTO;
import com.com.boha.monitor.library.dto.transfer.RequestDTO;
import com.com.boha.monitor.library.dto.transfer.ResponseDTO;
import com.com.boha.monitor.library.util.CacheUtil;
import com.com.boha.monitor.library.util.ErrorUtil;
import com.com.boha.monitor.library.util.SharedUtil;
import com.com.boha.monitor.library.util.Statics;
import com.com.boha.monitor.library.util.ToastUtil;
import com.com.boha.monitor.library.util.Util;
import com.com.boha.monitor.library.util.WebSocketUtil;

import java.util.ArrayList;
import java.util.List;

public class SiteTaskAndStatusAssignmentFragment extends Fragment implements PageFragment {

    public interface ProjectSiteTaskListener {
        public void onTaskClicked(ProjectSiteTaskDTO task);

        public void onProjectSiteTaskAdded(ProjectSiteTaskDTO task);

        public void onProjectSiteTaskDeleted();

        public void onSubTaskListRequested(ProjectSiteTaskDTO task, ProjectSiteTaskStatusDTO taskStatus);

        public void onStatusDialogRequested(ProjectSiteDTO projectSite, ProjectSiteTaskDTO siteTask);

        public void setBusy();

        public void setNotBusy();

        public void onCameraRequested(ProjectSiteTaskDTO siteTask, int type);
    }


    private ProjectSiteTaskListener mListener;
    private ListView mListView;
    private ListAdapter mAdapter;
    View trafficView;

    public SiteTaskAndStatusAssignmentFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    Context ctx;
    TextView txtCount, txtTitle;
    Button btnAssign;
    ProjectSiteDTO projectSite;
    List<TaskDTO> taskList;
    ProgressBar progressBar;
    View handle, view;
    LayoutInflater inflater;
    View subTasksLayout;

    public void setProjectSite(ProjectSiteDTO projectSite, int type) {
        Log.d(LOG, "########## setProjectSite");
        this.projectSite = projectSite;
        this.staffType = type;
        //setList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG, "########## onCreateView");
        this.inflater = inflater;
        view = inflater.inflate(R.layout.fragment_assign_site_tasks, container, false);
        ctx = getActivity();
        Bundle b = getArguments();
        if (b != null) {
            projectSite = (ProjectSiteDTO) b.getSerializable("projectSite");
            projectSiteTaskList = projectSite.getProjectSiteTaskList();
        }
        handle = view.findViewById(R.id.AST_handle);
        trafficLayout = view.findViewById(R.id.TRAFF_main);
        txtCount = (TextView) view.findViewById(R.id.AST_number);
        mListView = (ListView) view.findViewById(R.id.AST_list);
        txtTitle = (TextView) view.findViewById(R.id.AST_title2);

        subTasksLayout = view.findViewById(R.id.AST_subs);
        subTasksLayout.setVisibility(View.GONE);
        tgreen = (TextView) view.findViewById(R.id.TRAFF_green);
        tyellow = (TextView) view.findViewById(R.id.TRAFF_yellow);
        tred = (TextView) view.findViewById(R.id.TRAFF_red);
        tot = (TextView) view.findViewById(R.id.TRAFF_count);

        txtCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupTaskList();
            }
        });

        Statics.setRobotoFontLight(ctx, txtTitle);
        if (projectSite != null) {
            setList();
        }

        getCachedData();
        return view;
    }

    boolean isHidden = false;

    public void setProjectSiteTaskList(List<ProjectSiteTaskDTO> projectSiteTaskList) {
        this.projectSiteTaskList = projectSiteTaskList;
        if (mListView != null) {
            setList();
        }
    }

    private void sendTaskStatus() {
        Log.w(LOG, "############## sending taskStatus");
        RequestDTO w = new RequestDTO(RequestDTO.ADD_PROJECT_SITE_TASK_STATUS);
        ProjectSiteTaskStatusDTO s = new ProjectSiteTaskStatusDTO();
        s.setProjectSiteTaskID(projectSiteTask.getProjectSiteTaskID());
        s.setCompanyStaffID(SharedUtil.getCompanyStaff(ctx).getCompanyStaffID());
        s.setTaskStatus(taskStatus);
        w.setProjectSiteTaskStatus(s);

        switch (taskStatus.getStatusColor()) {
            case TaskStatusDTO.STATUS_COLOR_GREEN:
                rotateTotal(tgreen);
                break;
            case TaskStatusDTO.STATUS_COLOR_YELLOW:
                rotateTotal(tyellow);
                break;
            case TaskStatusDTO.STATUS_COLOR_RED:
                rotateTotal(tred);
                break;
            default:
                rotateTotal(txtCount);
                break;
        }
        WebSocketUtil.sendRequest(ctx, Statics.COMPANY_ENDPOINT, w, new WebSocketUtil.WebSocketListener() {
            @Override
            public void onMessage(final ResponseDTO response) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stopRotatingTotal();
                        if (!ErrorUtil.checkServerError(ctx, response)) {
                            return;
                        }
                        projectSiteTaskStatus = response.getProjectSiteTaskStatusList().get(0);
                        for (final ProjectSiteTaskDTO s : projectSiteTaskList) {
                            if (s.getProjectSiteTaskID().intValue() == projectSiteTaskStatus.getProjectSiteTaskID().intValue()) {
                                s.getProjectSiteTaskStatusList().add(0, projectSiteTaskStatus);
                                setList();
                                if (lastIndex == 1) {
                                    mListView.setSelection(0);
                                } else
                                    mListView.setSelection(lastIndex);

                            }
                        }
                        if (hasSubTasks) {
                            setSubTaskFields();
                            setSubList();
                            subTasksLayout.setVisibility(View.VISIBLE);
                            Util.explode(subTasksLayout, 1000, new Util.UtilAnimationListener() {
                                @Override
                                public void onAnimationEnded() {
                                    Util.shrink(trafficLayout, 500, new Util.UtilAnimationListener() {
                                        @Override
                                        public void onAnimationEnded() {
                                            trafficLayout.setVisibility(View.GONE);
                                            txtTitle.setVisibility(View.GONE);
                                            txtCount.setVisibility(View.GONE);
                                        }
                                    });
                                }
                            });

                        } else {
                           showPictureReminderDialog();
                        }

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
                        ToastUtil.errorToast(ctx,message);
                    }
                });
            }
        });
    }

    private void sendTask() {
        if (task == null) {
            ToastUtil.toast(ctx, ctx.getString(R.string.select_task));
            return;
        }
        boolean found = false;
        for (ProjectSiteTaskDTO t : projectSiteTaskList) {
            if (task.getTaskID() == t.getTask().getTaskID()) {
                found = true;
                break;
            }
        }
        if (found) {
            ToastUtil.toast(ctx, ctx.getString(R.string.task_already));
            return;
        }
        ProjectSiteTaskDTO pst = new ProjectSiteTaskDTO();
        pst.setTask(task);
        pst.setProjectSiteID(projectSite.getProjectSiteID());

        RequestDTO w = new RequestDTO();
        w.setRequestType(RequestDTO.ADD_PROJECT_SITE_TASK);
        w.setProjectSiteTask(pst);

        rotateTotal(txtCount);
        WebSocketUtil.sendRequest(ctx, Statics.COMPANY_ENDPOINT, w, new WebSocketUtil.WebSocketListener() {
            @Override
            public void onMessage(final ResponseDTO response) {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stopRotatingTotal();
                        if (!ErrorUtil.checkServerError(ctx, response)) {
                            return;
                        }
                        if (projectSiteTaskList == null) {
                            projectSiteTaskList = new ArrayList<ProjectSiteTaskDTO>();
                        }
                        projectSiteTaskList.add(0, response.getProjectSiteTaskList().get(0));
                        adapter.notifyDataSetChanged();
                        txtCount.setText("" + projectSiteTaskList.size());
                    }
                });

            }

            @Override
            public void onClose() {
                Log.e(LOG, "onClose - websocket closed .....");
            }

            @Override
            public void onError(final String message) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mListener.setNotBusy();
                        ToastUtil.errorToast(ctx, message);
                    }
                });
            }
        });
    }

    private void setList() {
        Log.d(LOG, "########## setList");
        txtCount.setText("" + projectSiteTaskList.size());
        adapter = new ProjectSiteTaskAdapter(ctx, R.layout.task_item,
                projectSiteTaskList, new ProjectSiteTaskAdapter.ProjectSiteTaskAdapterListener() {
            @Override
            public void onCameraRequested(ProjectSiteTaskDTO siteTask) {
                mListener.onCameraRequested(siteTask, PhotoUploadDTO.TASK_IMAGE);

            }

            @Override
            public void onDeleteRequested(ProjectSiteTaskDTO siteTask) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setMessage(ctx.getString(R.string.delete_task_text)
                        + "\n\n" + siteTask.getTask().getTaskName())
                        .setTitle(ctx.getString(R.string.delete))
                        .setPositiveButton(ctx.getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNegativeButton(ctx.getString(R.string.no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });

        getSummary();
        tot.setText("" + (greens + yellows + reds));
        View v = inflater.inflate(R.layout.hero_image, null);
        if (mListView.getHeaderViewsCount() == 0) {
            ImageView image = (ImageView) v.findViewById(R.id.HERO_image);
            TextView caption = (TextView) v.findViewById(R.id.HERO_caption);
            image.setImageDrawable(Util.getRandomHeroImage(ctx));
            caption.setText(ctx.getString(R.string.task_status));
            mListView.addHeaderView(v);
        }
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(LOG, "##### setOnItemClickListener, position: " + position);
                lastIndex = position;
                int index = position - 1;
                if (index < 0) {
                    projectSiteTask = projectSiteTaskList.get(0);
                } else {
                    projectSiteTask = projectSiteTaskList.get(position - 1);
                }
                if (projectSiteTask.getTask().getSubTaskList() != null && !projectSiteTask.getTask().getSubTaskList().isEmpty()) {
                    hasSubTasks = true;
                } else {
                    hasSubTasks = false;
                }
                type = TASK;
                showPopup();


            }
        });
        mListView.setSelection(lastIndex);
        Util.animateRotationY(txtCount, 1000);
    }

    private boolean hasSubTasks;
    int height;
    ProjectSiteTaskDTO projectSiteTask;
    TextView tgreen;
    TextView tyellow;
    TextView tred;
    TextView tot;
    View trafficLayout;
    int reds = 0, yellows = 0, greens = 0, greys = 0;
    int lastIndex;
    ObjectAnimator objectAnimator;

    private void getSummary() {
        greens = 0;
        reds = 0;
        yellows = 0;
        greys = 0;
        for (ProjectSiteTaskDTO pst : projectSiteTaskList) {
            if (pst.getProjectSiteTaskStatusList() != null && !pst.getProjectSiteTaskStatusList().isEmpty()) {
                for (ProjectSiteTaskStatusDTO status : pst.getProjectSiteTaskStatusList()) {
                    int color = status.getTaskStatus().getStatusColor();
                    switch (color) {
                        case 1:
                            greens++;
                            break;
                        case 2:
                            yellows++;
                            break;
                        case 3:
                            reds++;
                            break;

                    }

                }

            } else {
                greys++;
            }
        }
        tgreen.setText("" + greens);
        tyellow.setText("" + yellows);
        tred.setText("" + reds);
        Util.flashTrafficLights(tred, tyellow, tgreen, 20, Util.FLASH_FAST);
    }

    private void showPopup() {
        actionsWindow = new ListPopupWindow(getActivity());
        actionsWindow.setAdapter(new TaskStatusAdapter(ctx, R.layout.task_status_item_small, taskStatusList, projectSiteTask.getTask().getTaskName()));
        actionsWindow.setAnchorView(handle);
        actionsWindow.setWidth(700);
        actionsWindow.setHorizontalOffset(72);
        actionsWindow.setModal(true);
        actionsWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                taskStatus = taskStatusList.get(position);
                switch (type) {
                    case TASK:
                        sendTaskStatus();
                        break;
                    case SUBTASK:
                        sendSubTaskStatus();
                        break;
                }
                actionsWindow.dismiss();
            }
        });
        actionsWindow.show();
    }

    ListPopupWindow actionsWindow;
    List<TaskStatusDTO> taskStatusList;
    TaskStatusDTO taskStatus;
    ProjectSiteTaskStatusDTO projectSiteTaskStatus;

    public void updateList(ProjectSiteTaskStatusDTO taskStatus) {
        for (ProjectSiteTaskDTO task : projectSiteTaskList) {
            if (task.getProjectSiteTaskID().intValue() == taskStatus.getProjectSiteTaskID().intValue()) {
                task.getProjectSiteTaskStatusList().add(0, taskStatus);
                adapter.notifyDataSetChanged();
                mListView.setSelection(lastIndex);
            }
        }
    }

    private void deleteTask(ProjectSiteTaskDTO siteTask) {
        RequestDTO req = new RequestDTO();
        //req.setRequestType(RequestDTO.DELETE_INVOIC);
    }

    private void rotateTotal(TextView v) {
        objectAnimator = ObjectAnimator.ofFloat(v, "rotate", 0.0f, 360f);
        objectAnimator.setDuration(200);
        objectAnimator.setRepeatMode(ObjectAnimator.INFINITE);
        objectAnimator.setRepeatCount(1);
        objectAnimator.start();
    }

    private void stopRotatingTotal() {
        if (objectAnimator != null) {
            objectAnimator.cancel();
        }
    }

    private void getCachedData() {

        CacheUtil.getCachedData(ctx, CacheUtil.CACHE_DATA, new CacheUtil.CacheUtilListener() {
            @Override
            public void onFileDataDeserialized(ResponseDTO response) {
                if (response != null) {
                    taskList = response.getCompany().getTaskList();
                    taskStatusList = response.getCompany().getTaskStatusList();

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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (ProjectSiteTaskListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Host " + activity.getLocalClassName()
                    + " must implement ProjectSiteTaskListener");
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
    static final String LOG = SiteTaskAndStatusAssignmentFragment.class.getSimpleName();

    @Override
    public void animateCounts() {
        Util.animateRotationY(txtCount, 500);

    }

    ListPopupWindow taskPopupWindow;
    List<String> taskStringList;

    public void popupTaskList() {
        taskStringList = new ArrayList<>();
        for (TaskDTO t : taskList) {
            taskStringList.add("" + t.getTaskNumber() + " - " + t.getTaskName());
        }
        taskPopupWindow = new ListPopupWindow(ctx);
        taskPopupWindow.setAnchorView(txtTitle);
        taskPopupWindow.setWidth(800);
        taskPopupWindow.setModal(true);
        taskPopupWindow.setAdapter(new PopupListAdapter(ctx, R.layout.xxsimple_spinner_item, taskStringList,
                ctx.getString(R.string.add_task), false));
        taskPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                task = taskList.get(position);
                Log.d(LOG, "###### task selected - position: " + position + " " + taskList.get(position).getTaskName());
                sendTask();

            }
        });
        taskPopupWindow.show();
    }

    private void addProjectSiteTaskDTO(ProjectSiteTaskDTO task) {
        if (projectSiteTaskList == null) {
            projectSiteTaskList = new ArrayList<>();
        }
        projectSiteTaskList.add(task);
        //Collections.sort(taskPriceList);
        adapter.notifyDataSetChanged();
        mListView.setSelection(lastIndex);
        txtCount.setText("" + projectSiteTaskList.size());
        try {
            Thread.sleep(1000);
            Util.animateRotationY(txtCount, 500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    ProjectDTO project;
    List<ProjectSiteTaskDTO> projectSiteTaskList;
    ProjectSiteTaskAdapter adapter;
    TextView subTxtTaskName, subTxtCount, subTxtTitle;
    ListView subList;

    private void setSubTaskFields() {
        type = SUBTASK;
        subTxtTaskName = (TextView) view.findViewById(R.id.SUBSTAT_taskName);
        subTxtCount = (TextView) view.findViewById(R.id.SUBSTAT_number);
        subTxtTitle = (TextView) view.findViewById(R.id.SUBSTAT_title2);
        subList = (ListView) view.findViewById(R.id.SUBSTAT_list);

        Statics.setRobotoFontLight(ctx, subTxtTaskName);
        Statics.setRobotoFontLight(ctx, subTxtTitle);

        subTxtCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.shrink(subTasksLayout, 500, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        subTasksLayout.setVisibility(View.GONE);
                        trafficLayout.setVisibility(View.VISIBLE);
                        Util.explode(trafficLayout, 500, new Util.UtilAnimationListener() {
                            @Override
                            public void onAnimationEnded() {
                                txtTitle.setVisibility(View.VISIBLE);
                                txtCount.setVisibility(View.VISIBLE);
                                Util.explode(mListView, 300, new Util.UtilAnimationListener() {
                                    @Override
                                    public void onAnimationEnded() {
                                        try {
                                            Thread.sleep(1000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        showPictureReminderDialog();
                                    }
                                });
                            }
                        });

                    }
                });
            }
        });

    }

    private void showPictureReminderDialog() {
        AlertDialog.Builder d = new AlertDialog.Builder(getActivity());
        d.setTitle(ctx.getString(R.string.reminder))
                .setMessage(ctx.getString(R.string.pic_reminder))
                .setPositiveButton(ctx.getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onCameraRequested(projectSiteTask, PhotoUploadDTO.TASK_IMAGE);
                    }
                })
                .setNegativeButton(ctx.getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }
    List<SubTaskStatusDTO> subTaskStatusList;
    List<SubTaskDTO> subTaskList;

    private void setSubList() {
        subTaskList = projectSiteTask.getTask().getSubTaskList();
        subTxtCount.setText("" + subTaskList.size());
        subTxtTaskName.setText(projectSiteTask.getTask().getTaskName());
        if (projectSiteTaskStatus != null) {
            switch (projectSiteTaskStatus.getTaskStatus().getStatusColor()) {
                case TaskStatusDTO.STATUS_COLOR_GREEN:
                    subTxtCount.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.xgreen_oval));
                    break;
                case TaskStatusDTO.STATUS_COLOR_RED:
                    subTxtCount.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.xred_oval));
                    break;
                case TaskStatusDTO.STATUS_COLOR_YELLOW:
                    subTxtCount.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.xorange_oval));
                    break;
            }
        }
        subTaskStatusList = projectSiteTaskStatus.getSubTaskStatusList();
        if (subTaskStatusList == null) {
            subTaskStatusList = new ArrayList<>();
        }
        //build out list
        for (SubTaskDTO subTask : subTaskList) {
            boolean found = false;
            for (SubTaskStatusDTO subStatus : subTaskStatusList) {
                if (subTask.getSubTaskID().intValue() == subStatus.getSubTaskID().intValue()) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                SubTaskStatusDTO ss = new SubTaskStatusDTO();
                ss.setSubTaskID(subTask.getSubTaskID());
                ss.setSubTaskName(subTask.getSubTaskName());
                ss.setProjectSiteTaskStatusID(projectSiteTaskStatus.getProjectSiteTaskStatusID());
                subTaskStatusList.add(ss);
            }

        }
        View v = inflater.inflate(R.layout.hero_image, null);
        if (subList.getHeaderViewsCount() == 0) {
            ImageView image = (ImageView) v.findViewById(R.id.HERO_image);
            TextView caption = (TextView) v.findViewById(R.id.HERO_caption);
            image.setImageDrawable(Util.getRandomHeroImage(ctx));
            caption.setText(ctx.getString(R.string.subtask_status));
            subList.addHeaderView(v);
        }
        subAdapter = new SubTaskStatusAdapter(ctx, R.layout.subtask_status_item, subTaskStatusList);
        subList.setAdapter(subAdapter);
        subList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lastSubIndex = position - 1;
                subTask = subTaskList.get(position - 1);
                type = SUBTASK;
                showPopup();
            }
        });
    }

    SubTaskStatusDTO subTaskStatus;

    private void sendSubTaskStatus() {
        Log.w(LOG, "############## sending subtaskStatus");
        RequestDTO w = new RequestDTO(RequestDTO.ADD_SUBTASK_STATUS);
        SubTaskStatusDTO s = new SubTaskStatusDTO();
        s.setProjectSiteTaskStatusID(projectSiteTaskStatus.getProjectSiteTaskStatusID());
        s.setSubTaskID(subTask.getSubTaskID());
        s.setCompanyStaffID(SharedUtil.getCompanyStaff(ctx).getCompanyStaffID());
        s.setTaskStatus(taskStatus);

        w.setSubTaskStatus(s);

        switch (taskStatus.getStatusColor()) {
            case TaskStatusDTO.STATUS_COLOR_GREEN:
                break;
            case TaskStatusDTO.STATUS_COLOR_YELLOW:
                break;
            case TaskStatusDTO.STATUS_COLOR_RED:
                break;
            default:
                break;
        }
        WebSocketUtil.sendRequest(ctx, Statics.COMPANY_ENDPOINT, w, new WebSocketUtil.WebSocketListener() {
            @Override
            public void onMessage(final ResponseDTO response) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!ErrorUtil.checkServerError(ctx, response)) {
                            return;
                        }
                        subTaskStatus = response.getSubTaskStatusList().get(0);
                        for (SubTaskStatusDTO ss : subTaskStatusList) {
                            if (subTaskStatus.getSubTaskID().intValue() == ss.getSubTaskID().intValue()) {
                                ss.setTaskStatus(subTaskStatus.getTaskStatus());
                                ss.setStatusDate(subTaskStatus.getStatusDate());
                                ss.setStaffName(subTaskStatus.getStaffName());
                                subAdapter.notifyDataSetChanged();
                                break;
                            }
                        }

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
                        ToastUtil.errorToast(ctx,message);
                    }
                });
            }
        });
    }

    int lastSubIndex;
    int type;
    static final int SUBTASK = 2, TASK = 1;
    SubTaskStatusAdapter subAdapter;
    SubTaskDTO subTask;
}

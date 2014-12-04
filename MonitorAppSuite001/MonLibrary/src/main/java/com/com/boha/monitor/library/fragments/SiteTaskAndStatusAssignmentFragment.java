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
import com.com.boha.monitor.library.adapters.TaskStatusAdapter;
import com.com.boha.monitor.library.dto.ProjectDTO;
import com.com.boha.monitor.library.dto.ProjectSiteDTO;
import com.com.boha.monitor.library.dto.ProjectSiteTaskDTO;
import com.com.boha.monitor.library.dto.ProjectSiteTaskStatusDTO;
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
        trafficLayout = view.findViewById(R.id.AST_middle);
        txtCount = (TextView) view.findViewById(R.id.AST_number);
        mListView = (ListView) view.findViewById(R.id.AST_list);
        txtTitle = (TextView) view.findViewById(R.id.AST_title2);
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
        Log.w(LOG,"############## sending taskStatus");
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
                                mListView.setSelection(lastIndex);
//                                AlertDialog.Builder d = new AlertDialog.Builder(getActivity());
//                                d.setTitle(ctx.getString(R.string.reminder))
//                                        .setMessage(ctx.getString(R.string.pic_reminder))
//                                        .setPositiveButton(ctx.getString(R.string.yes), new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                mListener.onCameraRequested(s, PhotoUploadDTO.TASK_IMAGE);
//                                            }
//                                        })
//                                        .setNegativeButton(ctx.getString(R.string.no), new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//
//                                            }
//                                        })
//                                        .show();
                            }
                        }
                        if (hasSubTasks) {
                            mListener.onSubTaskListRequested(projectSiteTask,projectSiteTaskStatus);
                        }

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
        View v = inflater.inflate(R.layout.hero_image,null);
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
                lastIndex = position - 1;
                projectSiteTask = projectSiteTaskList.get(position - 1);
                if (projectSiteTask.getTask().getSubTaskList() != null && !projectSiteTask.getTask().getSubTaskList().isEmpty()) {
                    hasSubTasks = true;
                } else {
                    hasSubTasks = false;
                }
                showPopup();


            }
        });
        mListView.setSelection(lastIndex);
        Util.animateRotationY(txtCount, 1000);
    }
    private boolean hasSubTasks;
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
        greens = 0; reds = 0; yellows = 0; greys = 0;
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
                sendTaskStatus();
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
        objectAnimator = ObjectAnimator.ofFloat(v,"rotate",0.0f,360f);
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
}

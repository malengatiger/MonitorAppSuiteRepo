package com.com.boha.monitor.library.fragments;

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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.adapters.ProjectSiteTaskAdapter;
import com.com.boha.monitor.library.dto.ProjectDTO;
import com.com.boha.monitor.library.dto.ProjectSiteDTO;
import com.com.boha.monitor.library.dto.ProjectSiteTaskDTO;
import com.com.boha.monitor.library.dto.TaskDTO;
import com.com.boha.monitor.library.dto.transfer.PhotoUploadDTO;
import com.com.boha.monitor.library.dto.transfer.RequestDTO;
import com.com.boha.monitor.library.dto.transfer.ResponseDTO;
import com.com.boha.monitor.library.util.CacheUtil;
import com.com.boha.monitor.library.util.ErrorUtil;
import com.com.boha.monitor.library.util.Statics;
import com.com.boha.monitor.library.util.ToastUtil;
import com.com.boha.monitor.library.util.Util;
import com.com.boha.monitor.library.util.WebSocketUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <project/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <project/>
 * Activities containing this fragment MUST implement the ProjectSiteListListener
 * interface.
 */
public class TaskAssignmentFragment extends Fragment implements PageFragment {

    public interface ProjectSiteTaskListener {
        public void onTaskClicked(ProjectSiteTaskDTO task);
        public void onProjectSiteTaskAdded(ProjectSiteTaskDTO task);
        public void onProjectSiteTaskDeleted();
        public void setBusy();
        public void setNotBusy();
        public void onCameraRequested(ProjectSiteTaskDTO siteTask, int type);
    }


    private ProjectSiteTaskListener mListener;
    private AbsListView mListView;
    private ListAdapter mAdapter;

    public TaskAssignmentFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    Context ctx;
    TextView txtCount, txtSiteName;
    Spinner spinner;
    Button btnAssign;
    ProjectSiteDTO projectSite;
    List<TaskDTO> taskList;
    ProgressBar progressBar;

    public void setProjectSite(ProjectSiteDTO projectSite, int type) {
        Log.d(LOG, "########## setProjectSite");
        this.projectSite = projectSite;
        this.staffType = type;
        setList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG, "########## onCreateView");
        View view = inflater.inflate(R.layout.fragment_assign_site_tasks, container, false);
        ctx = getActivity();
        Bundle b = getArguments();
        if (b != null) {
            projectSite = (ProjectSiteDTO) b.getSerializable("projectSite");
            projectSiteTaskList = projectSite.getProjectSiteTaskList();
        }

        txtCount = (TextView) view.findViewById(R.id.AST_number);
        txtSiteName = (TextView) view.findViewById(R.id.AST_siteName);
        spinner = (Spinner) view.findViewById(R.id.AST_spinner);
        btnAssign = (Button) view.findViewById(R.id.AST_btnAssign);
        mListView = (AbsListView) view.findViewById(R.id.AST_list);
        TextView title = (TextView) view.findViewById(R.id.AST_title);

        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isHidden) {
                    showActions();
                } else {
                    hideActions();
                }
            }
        });
        Statics.setRobotoFontLight(ctx,title);
        Statics.setRobotoFontBold(ctx, txtSiteName);
        if (projectSite != null) {
            setList();
        }
        txtSiteName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isHidden) {
                    showActions();
                } else {
                    hideActions();
                }
            }
        });
        txtCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.toast(ctx, "Under Construction");
            }
        });
        btnAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTask();
            }
        });
        setSpinner();
        return view;
    }
    boolean isHidden = false;
    private void hideActions() {
        spinner.setVisibility(View.GONE);
        btnAssign.setVisibility(View.GONE);
        txtCount.setVisibility(View.GONE);
        isHidden = true;
    }
    private void showActions() {
        spinner.setVisibility(View.VISIBLE);
        btnAssign.setVisibility(View.VISIBLE);
        txtCount.setVisibility(View.VISIBLE);
        isHidden = false;
    }

    private void sendTask() {
        if (task == null) {
            ToastUtil.toast(ctx,ctx.getString(R.string.select_task));
            return;
        }
//        boolean found = false;
//        for (ProjectSiteTaskDTO t : projectSiteTaskList) {
//            if (task.getTaskID() == t.getTask().getTaskID()) {
//                found = true;
//                break;
//            }
//        }
//        if (found) {
//            ToastUtil.toast(ctx, "Already selected, ignored.");
//            return;
//        }
        ProjectSiteTaskDTO pst = new ProjectSiteTaskDTO();
        pst.setTask(task);
        pst.setProjectSiteID(projectSite.getProjectSiteID());

        RequestDTO w = new RequestDTO();
        w.setRequestType(RequestDTO.ADD_PROJECT_SITE_TASK);
        w.setProjectSiteTask(pst);

        mListener.setBusy();
        WebSocketUtil.sendRequest(ctx,Statics.COMPANY_ENDPOINT,w,new WebSocketUtil.WebSocketListener() {
            @Override
            public void onMessage(final ResponseDTO response) {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mListener.setNotBusy();
                        if (!ErrorUtil.checkServerError(ctx,response)) {
                            return;
                        }
                        if (projectSiteTaskList == null) {
                            projectSiteTaskList = new ArrayList<ProjectSiteTaskDTO>();
                        }
                        projectSiteTaskList.add(0,response.getProjectSiteTaskList().get(0));
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
        txtSiteName.setText(projectSite.getProjectSiteName());
        projectSiteTaskList = projectSite.getProjectSiteTaskList();
        txtCount.setText("" + projectSiteTaskList.size());
        // Set the adapter

        switch (staffType) {
            case OPERATIONS:
                adapter = new ProjectSiteTaskAdapter(ctx, R.layout.task_item,
                        projectSiteTaskList);
                break;
            case PROJECT_MANAGER:
                adapter = new ProjectSiteTaskAdapter(ctx, R.layout.task_item,
                        projectSiteTaskList, new ProjectSiteTaskAdapter.ProjectSiteTaskAdapterListener() {
                    @Override
                    public void onCameraRequested(ProjectSiteTaskDTO siteTask) {
                       mListener.onCameraRequested(siteTask, PhotoUploadDTO.TASK_IMAGE);

                    }

                    @Override
                    public void onStatusRequested(ProjectSiteTaskDTO siteTask) {

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
                break;

        }

        mListView.setAdapter(adapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProjectSiteTaskDTO t = projectSiteTaskList.get(position);
                mListener.onTaskClicked(t);
            }
        });
    }
    private void deleteTask(ProjectSiteTaskDTO siteTask) {
        RequestDTO req = new RequestDTO();
        //req.setRequestType(RequestDTO.DELETE_INVOIC);
    }

    private void setSpinner() {

        CacheUtil.getCachedData(ctx, CacheUtil.CACHE_DATA, new CacheUtil.CacheUtilListener() {
            @Override
            public void onFileDataDeserialized(ResponseDTO response) {
                if (response != null) {
                    taskList = response.getCompany().getTaskList();
                    List<String> names = new ArrayList<String>();
                    names.add(ctx.getResources().getString(R.string.select_task));
                    for (TaskDTO t : taskList) {
                        names.add(t.getTaskName());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(ctx,
                            R.layout.xxsimple_spinner_item, names);
                    spinner.setAdapter(adapter);
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
    static final String LOG = TaskAssignmentFragment.class.getSimpleName();


    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyText instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    @Override
    public void animateCounts() {
        Util.animateRotationY(txtCount, 500);

    }

    public void addProjectSiteTaskDTO(ProjectSiteTaskDTO task) {
        if (projectSiteTaskList == null) {
            projectSiteTaskList = new ArrayList<>();
        }
        projectSiteTaskList.add(task);
        //Collections.sort(taskStatusList);
        adapter.notifyDataSetChanged();
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

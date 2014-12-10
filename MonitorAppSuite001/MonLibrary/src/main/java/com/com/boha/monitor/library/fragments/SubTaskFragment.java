package com.com.boha.monitor.library.fragments;

import android.animation.Animator;
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
import android.view.animation.AccelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListPopupWindow;
import android.widget.TextView;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.adapters.PopupListAdapter;
import com.com.boha.monitor.library.adapters.SubTaskAdapter;
import com.com.boha.monitor.library.dto.ProjectDTO;
import com.com.boha.monitor.library.dto.SubTaskDTO;
import com.com.boha.monitor.library.dto.TaskDTO;
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
 * A fragment representing a taskStatusList of Items.
 * <project/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <project/>
 * Activities containing this fragment MUST implement the ProjectSiteListListener
 * interface.
 */
public class SubTaskFragment extends Fragment implements PageFragment {

    private AbsListView mListView;
    private ListAdapter mAdapter;

    public SubTaskFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    Context ctx;
    TextView txtCount, txtName;
    EditText editTaskName;
    EditText editSeqNo;
    View view;
    View editLayout;
    List<SubTaskDTO> subTaskList;
    SubTaskDTO subTask;
    TextView txtTaskName, txtTitle;
    Button btnSave;
    ListPopupWindow taskPopupWindow;
    ImageView imgLogo;
    ObjectAnimator an;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.w(LOG,"########### onCreateView");
        view = inflater.inflate(R.layout.fragment_subtask, container, false);
        ctx = getActivity();
        setFields();
        Bundle b = getArguments();
        if (b != null) {
            task = (TaskDTO)b.getSerializable("task");
            setList();
        }

        getTaskData();
        return view;
    }

    private void sendSubTask() {
        subTask = new SubTaskDTO();
        if (editTaskName.getText().toString().isEmpty()) {
            Util.showToast(ctx, ctx.getString(R.string.enter_subtask));
            return;
        }
        subTask.setSubTaskName(editTaskName.getText().toString());
        if (editSeqNo.getText().toString().isEmpty()) {
            subTask.setSubTaskNumber(1);
        } else {
            subTask.setSubTaskNumber(Integer.parseInt(editSeqNo.getText().toString()));
        }

        RequestDTO w = new RequestDTO(RequestDTO.ADD_SUB_TASK);
        subTask.setTaskID(task.getTaskID());
        w.setSubTask(subTask);
        rotateLogo();
        WebSocketUtil.sendRequest(ctx,Statics.COMPANY_ENDPOINT,w, new WebSocketUtil.WebSocketListener() {
            @Override
            public void onMessage(final ResponseDTO response) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        an.cancel();
                        if (!ErrorUtil.checkServerError(ctx,response)) {
                            return;
                        }
                        if (subTaskList == null)
                            subTaskList = new ArrayList<SubTaskDTO>();

                        subTaskList.add(0,response.getSubTaskList().get(0));
                        txtCount.setText("" + subTaskList.size());
                        adapter.notifyDataSetChanged();
                        closeEditPanel();
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
    public void setTask(TaskDTO task) {
        Log.e(LOG,"###### setTask");
       this.task = task;
        if (task == null) return;
        if (mListView != null) {
            txtTaskName.setText(task.getTaskName());
            setList();
        }
    }

    public void rotateLogo() {
        imgLogo.setVisibility(View.VISIBLE);
        an = ObjectAnimator.ofFloat(imgLogo, "rotation", 0.0f, 360f);
        an.setRepeatCount(an.INFINITE);
        an.setDuration(200);
        an.setInterpolator(new AccelerateInterpolator());
        an.start();
    }
    private void setFields() {
        editLayout = view.findViewById(R.id.STE_edit);
        editLayout.setVisibility(View.GONE);
        btnSave = (Button)view.findViewById(R.id.STE_btnSave);
        txtCount = (TextView) view.findViewById(R.id.STE_txtCount);
        txtTitle = (TextView) view.findViewById(R.id.STE_title);
        txtTaskName = (TextView) view.findViewById(R.id.STE_taskName);
        editTaskName = (EditText)view.findViewById(R.id.STE_editSubTaskName);
        editSeqNo = (EditText)view.findViewById(R.id.STE_editSubTaskNumber);
        mListView = (AbsListView) view.findViewById(R.id.STE_list);
        imgLogo = (ImageView) view.findViewById(R.id.STE_imgLogo);
        Statics.setRobotoFontLight(ctx,txtTitle);

        txtTaskName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTaskPopup();
            }
        });
        txtCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                an = ObjectAnimator.ofFloat(txtCount,"alpha", 1,0);
                an.setDuration(200);
                an.setRepeatMode(ObjectAnimator.REVERSE);
                an.setRepeatCount(1);
                an.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (editLayout.getVisibility() == View.GONE) {
                            openEditPanel();
                        } else {
                            closeEditPanel();
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                an.start();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                an = ObjectAnimator.ofFloat(btnSave,"alpha", 1,0);
                an.setDuration(100);
                an.setRepeatMode(ObjectAnimator.REVERSE);
                an.setRepeatCount(1);
                an.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        sendSubTask();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                an.start();

            }
        });
    }
    List<String> list;
    private void openEditPanel() {
        editLayout.setVisibility(View.VISIBLE);
        an = an.ofFloat(editLayout, "scaleY", 0, 1);
        an.setDuration(500);
        an.setInterpolator(new AccelerateInterpolator());
        an.start();
    }
    private void closeEditPanel() {
        an = an.ofFloat(editLayout,"scaleY", 1,0);
        an.setDuration(500);
        an.setInterpolator(new AccelerateInterpolator());
        an.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                editLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        an.start();
    }
    private void showTaskPopup() {
        if (list == null) {
            list = new ArrayList<>();
            for (TaskDTO t: taskList) {
                list.add(t.getTaskName());
            }
        }
        taskPopupWindow = new ListPopupWindow(ctx);
        taskPopupWindow.setModal(true);
        taskPopupWindow.setAnchorView(txtTitle);
        taskPopupWindow.setWidth(700);
        taskPopupWindow.setAdapter(new PopupListAdapter(ctx, R.layout.xxsimple_spinner_item, list, false));
        taskPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                task = taskList.get(position);
                txtTaskName.setText(task.getTaskName());

                taskPopupWindow.dismiss();
                setList();
            }
        });
        taskPopupWindow.show();
    }
    private void setList() {

        subTaskList = task.getSubTaskList();
        if (subTaskList == null) {
            subTaskList = new ArrayList<>();
        }
        if (subTaskList.isEmpty()) {
            editLayout.setVisibility(View.VISIBLE);
            an = an.ofFloat(editLayout, "scaleY", 0, 1);
            an.setDuration(500);
            an.setInterpolator(new AccelerateInterpolator());
            an.start();
        }

        adapter = new SubTaskAdapter(ctx, R.layout.task_list_item, subTaskList, false);
        mListView.setAdapter(adapter);
        txtCount.setText("" + subTaskList.size());
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                subTask = subTaskList.get(position);
            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e(LOG,"***** onItemLongClick position: " + position);
                AlertDialog.Builder b = new AlertDialog.Builder(ctx);
                b.setTitle("Move task")
                        .setMessage("Move this task up or down")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
                return true;
            }
        });

    }

    private void getTaskData() {
        CacheUtil.getCachedData(ctx, CacheUtil.CACHE_DATA, new CacheUtil.CacheUtilListener() {
            @Override
            public void onFileDataDeserialized(ResponseDTO response) {
                if (response != null) {
                    if (response.getCompany().getTaskList() != null) {
                        taskList = response.getCompany().getTaskList();
                    } else {
                        Log.e(LOG,"######## no company tasks found");
                    }
                }
                getRemoteData();
            }

            @Override
            public void onDataCached() {

            }

            @Override
            public void onError() {

            }
        });
    }
    private void getRemoteData() {
        //TODO - refresh task data from server
        RequestDTO w = new RequestDTO();
        w.setRequestType(RequestDTO.GET_ALL_PROJECT_IMAGES);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }



    TaskDTO task;

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the taskStatusList is empty. If you would like to change the text, call this method
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


    ProjectDTO project;
    List<TaskDTO> taskList;
    SubTaskAdapter adapter;
    static final String LOG = SubTaskFragment.class.getSimpleName();
}

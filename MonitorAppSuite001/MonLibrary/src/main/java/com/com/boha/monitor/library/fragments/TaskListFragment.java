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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListPopupWindow;
import android.widget.TextView;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.adapters.TaskAdapter;
import com.com.boha.monitor.library.dto.CompanyDTO;
import com.com.boha.monitor.library.dto.ProjectDTO;
import com.com.boha.monitor.library.dto.TaskDTO;
import com.com.boha.monitor.library.dto.TaskPriceDTO;
import com.com.boha.monitor.library.dto.transfer.RequestDTO;
import com.com.boha.monitor.library.dto.transfer.ResponseDTO;
import com.com.boha.monitor.library.util.CacheUtil;
import com.com.boha.monitor.library.util.ErrorUtil;
import com.com.boha.monitor.library.util.SharedUtil;
import com.com.boha.monitor.library.util.Statics;
import com.com.boha.monitor.library.util.Util;
import com.com.boha.monitor.library.util.WebSocketUtil;

import java.util.ArrayList;
import java.util.Collections;
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
public class TaskListFragment extends Fragment implements PageFragment {

    private TaskListListener mListener;
    private AbsListView mListView;
    private ListAdapter mAdapter;

    public TaskListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    Context ctx;
    TextView txtCount, txtName;
    EditText editTaskName, editPrice;
    Button btnSave;
    EditText numberPicker;
    View view, topView;
    View editLayout;
    ListPopupWindow actionsPopupWindow;
    List<String> list;
    TextView txtTitle;
    ImageView hero;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_task_list, container, false);
        ctx = getActivity();
        hero = (ImageView)view.findViewById(R.id.FTL_hero);
        setFields();
        getTaskData();
        return view;
    }

    public void expandHeroImage() {
        //Util.expand(hero,1000,null);
    }
    public void updateSequenceNumber(TaskDTO task) {

    }

    public void openEditPanel() {
        editLayout.setVisibility(View.VISIBLE);
        ObjectAnimator an = ObjectAnimator.ofFloat(editLayout, "scaleX", 0, 1);
        an.setDuration(300);
        an.start();
    }

    public void closeEditPanel() {
        ObjectAnimator an = ObjectAnimator.ofFloat(editLayout, "alpha", 1, 0);
        an.setDuration(60);
        an.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                editLayout.setAlpha(1);
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

    int action;
    private void setFields() {
        topView = view.findViewById(R.id.FTL_top);
        editLayout = view.findViewById(R.id.FTL_editLayout);
        txtCount = (TextView) view.findViewById(R.id.FTL_count);
        txtTitle = (TextView) view.findViewById(R.id.FTL_title);
        editTaskName = (EditText) view.findViewById(R.id.TE_editTaskName);
        editPrice = (EditText) view.findViewById(R.id.TE_editTaskPrice);
        numberPicker = (EditText) view.findViewById(R.id.TE_numberPicker);
        btnSave = (Button)view.findViewById(R.id.TE_btnSave);
        editLayout.setVisibility(View.GONE);
        Statics.setRobotoFontLight(ctx, txtTitle);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(btnSave,100, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        switch (action) {
                            case TaskDTO.ACTION_ADD:
                                registerTask();
                                break;
                            case TaskDTO.ACTION_UPDATE:
                                updateTask();
                                break;
                            default:
                                action = TaskDTO.ACTION_ADD;
                                registerTask();
                                break;
                        }
                    }
                });

            }
        });
        txtCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator an = ObjectAnimator.ofFloat(txtCount,"scaleY", 0, 1);
                an.setDuration(100);
                an.setRepeatMode(ObjectAnimator.REVERSE);
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
    }
    TaskPriceDTO taskPrice;
    private void registerTask() {
        Log.w(LOG,"## registerTask");
        task = new TaskDTO();
        CompanyDTO c = new CompanyDTO();
        c.setCompanyID(SharedUtil.getCompany(ctx).getCompanyID());
        task.setCompanyID(c.getCompanyID());
        if (editTaskName.getText().toString().isEmpty()) {
            Util.showToast(ctx, ctx.getResources().getString(R.string.enter_task_name));
            return;
        }

        if (numberPicker.getText().toString().isEmpty()) {
            Util.showToast(ctx, ctx.getString(R.string.enter_task_number));
            return;
        }

        task.setTaskName(editTaskName.getText().toString());
        task.setTaskNumber(Integer.parseInt(numberPicker.getText().toString()));

        RequestDTO w = new RequestDTO();
        w.setRequestType(RequestDTO.ADD_COMPANY_TASK);
        w.setTask(task);

        if (editPrice.getText().toString().isEmpty()) {
            Util.showToast(ctx, ctx.getString(R.string.enter_price));
            return;
        }
        taskPrice = new TaskPriceDTO();
        taskPrice.setPrice(Double.parseDouble(editPrice.getText().toString()));
        w.setTaskPrice(taskPrice);

        WebSocketUtil.sendRequest(ctx, Statics.COMPANY_ENDPOINT, w, new WebSocketUtil.WebSocketListener() {
            @Override
            public void onMessage(final ResponseDTO response) {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!ErrorUtil.checkServerError(ctx, response)) {
                            return;
                        }
                        task = response.getTaskList().get(0);
                        taskList.add(0,task);
                        txtCount.setText(""+ taskList.size());
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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });

            }
        });

    }

    private void updateTask() {
        if (editTaskName.getText().toString().isEmpty()) {
            Util.showToast(ctx, ctx.getString(R.string.enter_taskname));
            return;
        }
        TaskDTO t = new TaskDTO();
        t.setTaskID(task.getTaskID());
        t.setTaskName(editTaskName.getText().toString());
        t.setTaskNumber(Integer.parseInt(numberPicker.getText().toString()));
        //TODO - update task and price, check that price has changed
        if (!editPrice.getText().toString().isEmpty()) {
            double newPrice = Double.parseDouble(editPrice.getText().toString());
            if (taskPrice == null || taskPrice.getPrice() != newPrice) {
                TaskPriceDTO tp = new TaskPriceDTO();
                tp.setTaskID(task.getTaskID());
                tp.setPrice(newPrice);
                task.getTaskPriceList().add(0,tp);
                t.setTaskPriceList(new ArrayList<TaskPriceDTO>());
                t.getTaskPriceList().add(0,tp);
                adapter.notifyDataSetChanged();
            }
        }
        RequestDTO w = new RequestDTO(RequestDTO.UPDATE_COMPANY_TASK);
        w.setTask(t);

        WebSocketUtil.sendRequest(ctx,Statics.COMPANY_ENDPOINT,w,new WebSocketUtil.WebSocketListener() {
            @Override
            public void onMessage(ResponseDTO response) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
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
    private void deleteTask() {

    }
    private void setList() {
        mListView = (AbsListView) view.findViewById(R.id.FTL_list);
        adapter = new TaskAdapter(ctx, R.layout.task_list_item, taskList);
        mListView.setAdapter(adapter);
        txtCount.setText("" + taskList.size());
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                task = taskList.get(position);

                if (list == null) {
                    list = new ArrayList<String>();
                    list.add("Go to Sub Tasks");
                    list.add("Add New Task");
                    list.add("Edit this Task");
                }
                Util.showPopupBasicWithHeroImage(ctx,getActivity(),list,txtTitle,
                        ctx.getString(R.string.select_action),
                        new Util.UtilPopupListener() {
                    @Override
                    public void onItemSelected(int index) {
                        switch (index) {
                            case 0:
                                mListener.onSubTasksRequested(task);
                                break;
                            case 1:
                                action = TaskDTO.ACTION_ADD;
                                openEditPanel();
                                break;
                            case 2:
                                action = TaskDTO.ACTION_UPDATE;
                                editTaskName.setText(task.getTaskName());
                                if (task.getTaskPriceList() != null && !task.getTaskPriceList().isEmpty()) {
                                    editPrice.setText("" + task.getTaskPriceList().get(0).getPrice());
                                } else {
                                    editPrice.setHint("0.00");
                                }
                                if (task.getTaskNumber() != null) {
                                    numberPicker.setText(""+task.getTaskNumber());
                                } else {
                                    numberPicker.setText("0");
                                }
                                openEditPanel();
                                break;
                        }
                    }
                });

            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e(LOG, "***** onItemLongClick position: " + position);
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
                        setList();
                    } else {
                        Log.e(LOG, "######## no company tasks found");
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
        try {
            mListener = (TaskListListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Host " + activity.getLocalClassName()
                    + " must implement TaskListListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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

    public void addTask(TaskDTO task) {
        if (taskList == null) {
            taskList = new ArrayList<>();
        }
        taskList.add(task);
        Collections.sort(taskList);
        adapter.notifyDataSetChanged();
        txtCount.setText("" + taskList.size());
        try {
            Thread.sleep(1000);
            Util.animateRotationY(txtCount, 500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public interface TaskListListener {
        public void onTaskClicked(TaskDTO task);

        public void onSubTasksRequested(TaskDTO task);

        public void onNewTaskRequested();
    }

    ProjectDTO project;
    List<TaskDTO> taskList;
    TaskAdapter adapter;
    static final String LOG = TaskListFragment.class.getSimpleName();
}

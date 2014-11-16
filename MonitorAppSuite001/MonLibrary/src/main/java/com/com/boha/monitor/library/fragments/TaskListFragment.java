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
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.adapters.TaskAdapter;
import com.com.boha.monitor.library.dto.ProjectDTO;
import com.com.boha.monitor.library.dto.TaskDTO;
import com.com.boha.monitor.library.dto.transfer.RequestDTO;
import com.com.boha.monitor.library.dto.transfer.ResponseDTO;
import com.com.boha.monitor.library.util.CacheUtil;
import com.com.boha.monitor.library.util.Statics;
import com.com.boha.monitor.library.util.ToastUtil;
import com.com.boha.monitor.library.util.Util;

import java.util.ArrayList;
import java.util.Collections;
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
    EditText editTaskName;
    NumberPicker numberPicker;
    View view;
    View editLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.task_list, container, false);
        ctx = getActivity();
        setFields();
        getTaskData();
        return view;
    }

    public void updateSequenceNumber(TaskDTO task) {

    }

    public void openEditPanel() {
        editLayout.setVisibility(View.VISIBLE);
    }
    private void setFields() {
        editLayout = view.findViewById(R.id.TL_editLayout);
        txtCount = (TextView) view.findViewById(R.id.TL_count);
        editTaskName = (EditText)view.findViewById(R.id.TL_editName);
        numberPicker = (NumberPicker)view.findViewById(R.id.TL_numberPicker);
        editLayout.setVisibility(View.GONE);
        TextView title = (TextView) view.findViewById(R.id.TL_title);
        Statics.setRobotoFontLight(ctx,title);
    }
    private void setList() {
        mListView = (AbsListView) view.findViewById(R.id.TL_list);
        adapter = new TaskAdapter(ctx, R.layout.task_list_item, taskList);
        mListView.setAdapter(adapter);
        txtCount.setText("" + taskList.size());
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                task = taskList.get(position);
                mListener.onTaskClicked(task);
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
        txtCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.toast(ctx, "Under Construction");
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
        public void onSequenceClicked(TaskDTO task);
    }

    ProjectDTO project;
    List<TaskDTO> taskList;
    TaskAdapter adapter;
    static final String LOG = TaskListFragment.class.getSimpleName();
}

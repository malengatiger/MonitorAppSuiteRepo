package com.com.boha.monitor.library.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.adapters.TaskStatusAdapter;
import com.com.boha.monitor.library.dto.ProjectDTO;
import com.com.boha.monitor.library.dto.TaskStatusDTO;
import com.com.boha.monitor.library.dto.transfer.ResponseDTO;
import com.com.boha.monitor.library.util.Statics;
import com.com.boha.monitor.library.util.Util;

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
public class TaskStatusListFragment extends Fragment implements PageFragment {

    private TaskStatusListListener mListener;
    private AbsListView mListView;
    private ListAdapter mAdapter;

    public TaskStatusListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    Context ctx;
    TextView txtCount, txtName;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_status_list, container, false);
        ctx = getActivity();
        Bundle b = getArguments();
        if (b != null) {
            ResponseDTO r = (ResponseDTO) b.getSerializable("response");
            taskStatusList = r.getCompany().getTaskStatusList();
        }

        txtCount = (TextView)view.findViewById(R.id.TASK_STAT_count);
        txtName = (TextView)view.findViewById(R.id.TASK_STAT_title);

        Statics.setRobotoFontLight(ctx, txtName);
        txtCount.setText("" + taskStatusList.size());
        // Set the adapter
        mListView = (AbsListView) view.findViewById(R.id.TASK_STAT_list);
        adapter = new TaskStatusAdapter(ctx, R.layout.task_status_item, taskStatusList,false);
        mListView.setAdapter(adapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                taskStatus = taskStatusList.get(position);
                mListener.onTaskStatusClicked(taskStatus);
            }
        });
        txtCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onNewTaskStatusRequested();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (TaskStatusListListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Host " + activity.getLocalClassName()
                    + " must implement TaskStatusListListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    TaskStatusDTO taskStatus;

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
        Util.animateRotationY(txtCount,500);

    }

    public void addTaskStatus(TaskStatusDTO taskStatus) {
        if (taskStatusList == null) {
            taskStatusList = new ArrayList<>();
        }
        taskStatusList.add(taskStatus);
        Collections.sort(taskStatusList);
        adapter.notifyDataSetChanged();
        txtCount.setText("" + taskStatusList.size());
        try {
            Thread.sleep(1000);
            Util.animateRotationY(txtCount,500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public interface TaskStatusListListener {
        public void onTaskStatusClicked(TaskStatusDTO taskStatus);
        public void onNewTaskStatusRequested();
    }

    ProjectDTO project;
    List<TaskStatusDTO> taskStatusList;
    TaskStatusAdapter adapter;
}
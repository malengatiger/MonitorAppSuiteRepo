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
import android.widget.ListAdapter;
import android.widget.TextView;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.adapters.TaskPriceAdapter;
import com.com.boha.monitor.library.dto.ProjectDTO;
import com.com.boha.monitor.library.dto.TaskDTO;
import com.com.boha.monitor.library.dto.TaskPriceDTO;
import com.com.boha.monitor.library.dto.transfer.RequestDTO;
import com.com.boha.monitor.library.dto.transfer.ResponseDTO;
import com.com.boha.monitor.library.util.CacheUtil;
import com.com.boha.monitor.library.util.Statics;
import com.com.boha.monitor.library.util.ToastUtil;
import com.com.boha.monitor.library.util.Util;

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
public class TaskPriceListFragment extends Fragment implements PageFragment {

    private TaskListListener mListener;
    private AbsListView mListView;
    private ListAdapter mAdapter;

    public TaskPriceListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    Context ctx;
    TextView txtCount, txtTitle;

    View view;
    View editLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_task_price_list, container, false);
        ctx = getActivity();
        setFields();
        getTaskPriceData();
        return view;
    }



    private void setFields() {
        txtCount = (TextView) view.findViewById(R.id.TPL_count);
        mListView = (AbsListView) view.findViewById(R.id.TPL_list);
        txtTitle = (TextView) view.findViewById(R.id.TPL_title);
        Statics.setRobotoFontLight(ctx,txtTitle);
    }
    private void setList() {

        adapter = new TaskPriceAdapter(ctx, R.layout.task_list_item, taskPriceList);
        mListView.setAdapter(adapter);
        txtCount.setText("" + taskPriceList.size());
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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
                Util.showToast(ctx, "Under Construction");
            }
        });
    }

    private void getTaskPriceData() {
        CacheUtil.getCachedData(ctx, CacheUtil.CACHE_DATA, new CacheUtil.CacheUtilListener() {
            @Override
            public void onFileDataDeserialized(ResponseDTO response) {
                if (response != null) {
                    if (response.getCompany().getTaskList() != null) {
                       for (TaskDTO t: response.getCompany().getTaskList()) {
                           if (t.getTaskPriceList() != null && !t.getTaskPriceList().isEmpty()) {
                               taskPriceList.add(t.getTaskPriceList().get(0));
                           }
                       }
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
    @Override
    public void animateCounts() {
        Util.animateRotationY(txtCount, 500);

    }

    public void addTaskPrice(TaskPriceDTO task) {
        if (taskPriceList == null) {
            taskPriceList = new ArrayList<>();
        }
        taskPriceList.add(task);
        //Collections.sort(taskPriceList);
        adapter.notifyDataSetChanged();
        txtCount.setText("" + taskPriceList.size());
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
    List<TaskPriceDTO> taskPriceList;
    TaskPriceAdapter adapter;
    static final String LOG = TaskPriceListFragment.class.getSimpleName();
}

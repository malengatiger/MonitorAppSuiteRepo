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
import android.widget.TextView;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.adapters.ProjectAdapter;
import com.com.boha.monitor.library.dto.ProjectDTO;
import com.com.boha.monitor.library.dto.ProjectSiteDTO;
import com.com.boha.monitor.library.dto.ProjectSiteTaskDTO;
import com.com.boha.monitor.library.dto.transfer.ResponseDTO;
import com.com.boha.monitor.library.util.Statics;
import com.com.boha.monitor.library.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p />
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p />
 * Activities containing this fragment MUST implement the ProjectListListener
 * interface.
 */
public class ProjectListFragment extends Fragment implements AbsListView.OnItemClickListener, PageFragment {


    private ProjectListListener mListener;
    private AbsListView mListView;
    private TextView txtProjectCount, txtStatusCount, txtLabel;

    public static ProjectListFragment newInstance(ResponseDTO r) {
        ProjectListFragment fragment = new ProjectListFragment();
        Bundle args = new Bundle();
        args.putSerializable("response", r);
        fragment.setArguments(args);
        return fragment;
    }
    public ProjectListFragment() {
    }

    Context ctx;
    View topView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project, container, false);
        ctx = getActivity();
        topView = view.findViewById(R.id.PROJ_LIST_layoutx);
        Bundle b = getArguments();
        if (b != null) {
            ResponseDTO r = (ResponseDTO)b.getSerializable("response");
            projectList = r.getCompany().getProjectList();
        }

        mListView = (AbsListView) view.findViewById(android.R.id.list);
        adapter = new ProjectAdapter(ctx, R.layout.project_item, projectList, new ProjectAdapter.ProjectAdapterListener() {
            @Override
            public void onEditRequested(ProjectDTO project) {
                mListener.onProjectEditDialogRequested(project);
            }

            @Override
            public void onProjectSitesRequested(ProjectDTO project) {
                mListener.onProjectSitesRequested(project);
            }
            @Override
            public void onPictureRequested(ProjectDTO project) {
                mListener.onProjectPictureRequested(project);
            }
        });
        mListView.setAdapter(adapter);
        txtProjectCount = (TextView)view.findViewById(R.id.PROJ_LIST_projectCount);
        txtStatusCount = (TextView)view.findViewById(R.id.PROJ_LIST_statusCount);
        txtLabel = (TextView)view.findViewById(R.id.PROJ_LIST_label);
        Statics.setRobotoFontLight(ctx, txtLabel);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);
        setTotals();

        return view;
    }

    private void setTotals() {
        txtProjectCount.setText("" + projectList.size());
        int count = 0;
        for (ProjectDTO p: projectList) {
            for (ProjectSiteDTO ps: p.getProjectSiteList()) {
                for (ProjectSiteTaskDTO pst: ps.getProjectSiteTaskList()) {
                    count += pst.getProjectSiteTaskStatusList().size();
                }
            }
        }
        txtStatusCount.setText("" + count);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        animateCounts();


    }
    @Override
    public void animateCounts() {
        Util.animateScaleY(txtStatusCount, 500);
        Util.animateRotationY(txtProjectCount, 500);
        //Util.animateScaleX(topView, 500);
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (ProjectListListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                + " must implement ProjectListListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            if (projectList.get(position).getProjectSiteList() == null || projectList.get(position).getProjectSiteList().isEmpty()) {
                return;
            }
            mListener.onProjectClicked(projectList.get(position));
        }
    }


    public void addProject(ProjectDTO project) {
        if (projectList == null) {
            projectList = new ArrayList<>();
        }
        projectList.add(0,project);
        //Collections.sort(clientList);
        adapter.notifyDataSetChanged();
        txtProjectCount.setText("" + projectList.size());
        try {
            Thread.sleep(1000);
            Util.animateRotationY(txtProjectCount,500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
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

    /**
    * This interface must be implemented by activities that contain this
    * fragment to allow an interaction in this fragment to be communicated
    * to the activity and potentially other fragments contained in that
    * activity.
    * <p>
    * See the Android Training lesson <a href=
    * "http://developer.android.com/training/basics/fragments/communicating.html"
    * >Communicating with Other Fragments</a> for more information.
    */
    public interface ProjectListListener {
        public void onProjectClicked(ProjectDTO project);
        public void onProjectEditDialogRequested(ProjectDTO project);
        public void onProjectSitesRequested(ProjectDTO project);
        public void onProjectPictureRequested(ProjectDTO project);
    }

    private List<ProjectDTO> projectList;
    private ProjectAdapter adapter;

}

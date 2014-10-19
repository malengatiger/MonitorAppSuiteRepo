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
import com.com.boha.monitor.library.adapters.ProjectSiteAdapter;
import com.com.boha.monitor.library.dto.ProjectDTO;
import com.com.boha.monitor.library.dto.ProjectSiteDTO;
import com.com.boha.monitor.library.util.Statics;
import com.com.boha.monitor.library.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the ProjectSiteListListener
 * interface.
 */
public class ProjectSiteListFragment extends Fragment implements AbsListView.OnItemClickListener, PageFragment {

    private ProjectSiteListListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ProjectSiteListFragment() {
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
        View view = inflater.inflate(R.layout.fragment_projectsite, container, false);
        ctx = getActivity();
        Bundle b = getArguments();
        if (b != null) {
            project = (ProjectDTO) b.getSerializable("project");
            projectSiteList = project.getProjectSiteList();
        }

        txtCount = (TextView) view.findViewById(R.id.SITE_LIST_siteCount);
        txtName = (TextView) view.findViewById(R.id.SITE_LIST_projectName);
        txtName.setText(project.getProjectName());
        Statics.setRobotoFontLight(ctx, txtName);
        txtCount.setText("" + projectSiteList.size());
        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        projectSiteAdapter = new ProjectSiteAdapter(ctx, R.layout.site_item, projectSiteList, new ProjectSiteAdapter.ProjectSiteAdapterListener() {
            @Override
            public void onEditRequested(ProjectSiteDTO site) {
                mListener.onProjectSiteEditRequested(site);
            }

            @Override
            public void onTasksRequested(ProjectSiteDTO site) {
                mListener.onProjectSiteTasksRequested(site);
            }
        });
        mListView.setAdapter(projectSiteAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);
        return view;
    }

    public void addProjectSite(ProjectSiteDTO site) {
        if (projectSiteList == null) {
            projectSiteList = new ArrayList<>();
        }
        projectSiteList.add(0,site);
        //Collections.sort(clientList);
        projectSiteAdapter.notifyDataSetChanged();
        txtCount.setText("" + projectSiteList.size());
        try {
            Thread.sleep(1000);
            Util.animateRotationY(txtCount,500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (ProjectSiteListListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ProjectSiteListListener");
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
            projectSite = projectSiteList.get(position);
            mListener.onProjectSiteClicked(projectSite);
        }
    }

    ProjectSiteDTO projectSite;

    public ProjectSiteDTO getProjectSite() {
        return projectSite;
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

    @Override
    public void animateCounts() {
        Util.animateRotationY(txtCount, 500);

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface ProjectSiteListListener {
        public void onProjectSiteClicked(ProjectSiteDTO projectSite);
        public void onProjectSiteEditRequested(ProjectSiteDTO projectSite);
        public void onProjectSiteTasksRequested(ProjectSiteDTO projectSite);
    }

    ProjectDTO project;
    List<ProjectSiteDTO> projectSiteList;
    ProjectSiteAdapter projectSiteAdapter;
}

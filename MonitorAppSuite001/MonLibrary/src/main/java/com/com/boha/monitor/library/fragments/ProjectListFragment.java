package com.com.boha.monitor.library.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
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
 * <project />
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <project />
 * Activities containing this fragment MUST implement the ProjectListListener
 * interface.
 */
public class ProjectListFragment extends Fragment implements  PageFragment {


    private ProjectListListener mListener;
    private ListView mListView;
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
    LayoutInflater inflater;
    View view;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_project, container, false);
        this.inflater = inflater;
        ctx = getActivity();
        topView = view.findViewById(R.id.PROJ_LIST_layoutx);
        Bundle b = getArguments();
        if (b != null) {
            ResponseDTO r = (ResponseDTO)b.getSerializable("response");
            projectList = r.getCompany().getProjectList();
        }


        txtProjectCount = (TextView)view.findViewById(R.id.PROJ_LIST_projectCount);
        txtLabel = (TextView)view.findViewById(R.id.PROJ_LIST_label);
        Statics.setRobotoFontLight(ctx, txtLabel);


        setTotals();
        setList();
        return view;
    }

    private void setList() {

        mListView = (ListView) view.findViewById(android.R.id.list);
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

            @Override
            public void onGalleryRequested(ProjectDTO project) {
                mListener.onGalleryRequested(project);
            }

            @Override
            public void onMapRequested(ProjectDTO project) {
                mListener.onMapRequested(project);
            }
        });
        mListView.setAdapter(adapter);
        View v = inflater.inflate(R.layout.hero_image_project, null);
        TextView stCount = (TextView)v.findViewById(R.id.HERO_statusCount);
        stCount.setText("" + statusCount);
        mListView.addHeaderView(v);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (projectList.get(position).getProjectSiteList() == null || projectList.get(position).getProjectSiteList().isEmpty()) {
                    return;
                }
                mListener.onProjectClicked(projectList.get(position));
            }
        });
    }
    int statusCount;
    private void setTotals() {
        txtProjectCount.setText("" + projectList.size());
        statusCount = 0;
        for (ProjectDTO p: projectList) {
            for (ProjectSiteDTO ps: p.getProjectSiteList()) {
                for (ProjectSiteTaskDTO pst: ps.getProjectSiteTaskList()) {
                    statusCount += pst.getProjectSiteTaskStatusList().size();
                }
            }
        }

        animateCounts();


    }
    @Override
    public void animateCounts() {

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



    public void addProject(ProjectDTO project) {
        if (projectList == null) {
            projectList = new ArrayList<>();
        }
        projectList.add(0,project);
        //Collections.sort(engineerList);
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
    * <project>
    * See the Android Training lesson <a href=
    * "http://developer.android.com/training/basics/fragments/communicating.html"
    * >Communicating with Other Fragments</a> for more information.
    */
    public interface ProjectListListener {
        public void onProjectClicked(ProjectDTO project);
        public void onProjectEditDialogRequested(ProjectDTO project);
        public void onProjectSitesRequested(ProjectDTO project);
        public void onProjectPictureRequested(ProjectDTO project);
        public void onGalleryRequested(ProjectDTO project);
        public void onMapRequested(ProjectDTO project);
    }

    private List<ProjectDTO> projectList;
    private ProjectAdapter adapter;

}

package com.com.boha.monitor.library.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.ImagePagerActivity;
import com.com.boha.monitor.library.adapters.ProjectSiteAdapter;
import com.com.boha.monitor.library.dto.ProjectDTO;
import com.com.boha.monitor.library.dto.ProjectSiteDTO;
import com.com.boha.monitor.library.dto.transfer.RequestDTO;
import com.com.boha.monitor.library.dto.transfer.ResponseDTO;
import com.com.boha.monitor.library.util.ErrorUtil;
import com.com.boha.monitor.library.util.Statics;
import com.com.boha.monitor.library.util.Util;
import com.com.boha.monitor.library.util.WebSocketUtil;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <project/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <project/>
 * Activities containing this fragment MUST implement the ProjectSiteListListener
 * interface.
 */
public class ProjectSiteListFragment extends Fragment implements  PageFragment {

    private ProjectSiteListListener mListener;
    private AbsListView mListView;
    public ProjectSiteListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    Context ctx;
    TextView txtCount, txtName;
    int lastIndex;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_projectsite, container, false);
        ctx = getActivity();
        Bundle b = getArguments();
        if (b != null) {
            project = (ProjectDTO) b.getSerializable("project");
        }

        txtCount = (TextView) view.findViewById(R.id.SITE_LIST_siteCount);
        txtName = (TextView) view.findViewById(R.id.SITE_LIST_projectName);
        txtName.setText(project.getProjectName());
        Statics.setRobotoFontLight(ctx, txtName);
        setList();
        return view;
    }

    private void setList() {
        txtCount.setText("" + project.getProjectSiteList().size());
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        projectSiteAdapter = new ProjectSiteAdapter(ctx, R.layout.site_item,
                project.getProjectSiteList(), new ProjectSiteAdapter.ProjectSiteAdapterListener() {
            @Override
            public void onEditRequested(ProjectSiteDTO site, int index) {
                projectSite = project.getProjectSiteList().get(index);
                lastIndex = index;
                mListener.onProjectSiteEditRequested(site);
            }

            @Override
            public void onGalleryRequested(ProjectSiteDTO site, int index) {
                projectSite = project.getProjectSiteList().get(index);
                lastIndex = index;
                Intent i = new Intent(getActivity(), ImagePagerActivity.class);
                i.putExtra("projectSite",site);
                i.putExtra("type", ImagePagerActivity.SITE);
                startActivity(i);
            }

            @Override
            public void onCameraRequested(ProjectSiteDTO site, int index) {
                projectSite = project.getProjectSiteList().get(index);
                lastIndex = index;
                mListener.onCameraRequested(site);
            }

            @Override
            public void onTasksRequested(ProjectSiteDTO site, int index) {
                projectSite = project.getProjectSiteList().get(index);
                lastIndex = index;
                mListener.onProjectSiteTasksRequested(site);
            }
        });
        mListView.setAdapter(projectSiteAdapter);
        mListView.setSelection(lastIndex);
        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (null != mListener) {
                    lastIndex = position;
                    projectSite = project.getProjectSiteList().get(position);
                    mListener.onProjectSiteClicked(projectSite);
                }
            }
        });
    }
    int index;
    public void refreshPhotoList() {
        if (projectSite == null) throw new UnsupportedOperationException("ProjectSiteDTO is null");
        RequestDTO w = new RequestDTO();
        w.setRequestType(RequestDTO.GET_SITE_IMAGES);
        w.setProjectSiteID(projectSite.getProjectSiteID());

        WebSocketUtil.sendRequest(ctx,Statics.COMPANY_ENDPOINT,w,new WebSocketUtil.WebSocketListener() {
            @Override
            public void onMessage(final ResponseDTO response) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!ErrorUtil.checkServerError(ctx,response)) {
                            return;
                        }
                        projectSite.setPhotoUploadList(response.getPhotoUploadList());

                        setList();
                        index = 0;
                        for (ProjectSiteDTO ps: project.getProjectSiteList()) {
                            if (ps.getProjectSiteID() == projectSite.getProjectSiteID()) {
                                break;
                            }
                            index++;
                        }
                        mListView.setSelection(index);
                        mListener.onPhotoListUpdated(projectSite);
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
    public void addProjectSite(ProjectSiteDTO site) {
        if (project.getProjectSiteList() == null) {
            project.setProjectSiteList(new ArrayList<ProjectSiteDTO>());
        }
        project.getProjectSiteList().add(0, site);
        projectSiteAdapter.notifyDataSetChanged();
        txtCount.setText("" + project.getProjectSiteList().size());
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
     * <project/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface ProjectSiteListListener {
        public void onProjectSiteClicked(ProjectSiteDTO projectSite);
        public void onProjectSiteEditRequested(ProjectSiteDTO projectSite);
        public void onProjectSiteTasksRequested(ProjectSiteDTO projectSite);
        public void onCameraRequested(ProjectSiteDTO projectSite);
        public void onPhotoListUpdated(ProjectSiteDTO projectSite);
    }

    ProjectDTO project;
    ProjectSiteAdapter projectSiteAdapter;
}

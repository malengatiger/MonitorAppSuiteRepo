package com.com.boha.monitor.library.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.com.boha.monitor.library.dto.ProjectSiteTaskDTO;
import com.com.boha.monitor.library.dto.ProjectSiteTaskStatusDTO;
import com.com.boha.monitor.library.dto.transfer.RequestDTO;
import com.com.boha.monitor.library.dto.transfer.ResponseDTO;
import com.com.boha.monitor.library.util.CacheUtil;
import com.com.boha.monitor.library.util.ErrorUtil;
import com.com.boha.monitor.library.util.Statics;
import com.com.boha.monitor.library.util.Util;
import com.com.boha.monitor.library.util.WebSocketUtil;

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
    static final String LOG = ProjectSiteListFragment.class.getSimpleName();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_projectsite, container, false);
        Log.i(LOG,"------------- onCreateView");
        ctx = getActivity();
        Bundle b = getArguments();
        if (b != null) {
            Log.e(LOG,"++++ getting project object from getArguments");
            project = (ProjectDTO) b.getSerializable("project");
            lastIndex = b.getInt("index",0);
        }
        if (savedInstanceState != null) {
            lastIndex = savedInstanceState.getInt("lastIndex",0);
            Log.e(LOG,"++++ lastIndex in savedInstanceState: " + lastIndex);
        }
        txtCount = (TextView) view.findViewById(R.id.SITE_LIST_siteCount);

        Statics.setRobotoFontLight(ctx, txtCount);
        getProjectSites();
        return view;
    }

    private void getProjectSites() {
        CacheUtil.getCachedProjectData(ctx,CacheUtil.CACHE_PROJECT,project.getProjectID(), new CacheUtil.CacheUtilListener() {
            @Override
            public void onFileDataDeserialized(ResponseDTO response) {
                if (response != null) {
                    if (response.getProjectList() != null && !response.getProjectList().isEmpty()) {
                        project = response.getProjectList().get(0);
                        setList();
                    }
                }
                getServerData();
            }

            @Override
            public void onDataCached() {

            }

            @Override
            public void onError() {
                getServerData();
            }
        });

    }
    private void getServerData() {
        RequestDTO w = new RequestDTO(RequestDTO.GET_PROJECT_DATA);
        w.setProjectID(project.getProjectID());

        WebSocketUtil.sendRequest(ctx,Statics.COMPANY_ENDPOINT,w,new WebSocketUtil.WebSocketListener() {
            @Override
            public void onMessage(final ResponseDTO response) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!ErrorUtil.checkServerError(ctx,response)) {
                            return;
                        }
                        project = response.getProjectList().get(0);
                        setList();

                        CacheUtil.cacheProjectData(ctx,response,CacheUtil.CACHE_PROJECT,project.getProjectID(),new CacheUtil.CacheUtilListener() {
                            @Override
                            public void onFileDataDeserialized(ResponseDTO response) {

                            }

                            @Override
                            public void onDataCached() {

                            }

                            @Override
                            public void onError() {

                            }
                        });
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
    private void setList() {
        txtCount.setText("" + project.getProjectSiteList().size());
        for (ProjectSiteDTO site: project.getProjectSiteList()) {
            List<ProjectSiteTaskStatusDTO> taskStatusList = new ArrayList<>();
            for (ProjectSiteTaskDTO task: site.getProjectSiteTaskList()) {
                taskStatusList.addAll(task.getProjectSiteTaskStatusList());
            }
            Collections.sort(taskStatusList);
            site.setStatusCount(taskStatusList.size());
            if (taskStatusList.size() > 0) {
                ProjectSiteTaskStatusDTO x = taskStatusList.get(0);
                site.setLastTaskStatus(x);
                Log.e(LOG, "task: " + site.getProjectSiteName() + " status: " + taskStatusList.size() + " " + x.getTaskStatus().getTaskStatusName());
            } else {
                //Log.w(LOG,"########## no status found, site: " + site.getProjectSiteName());
            }

        }
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        projectSiteAdapter = new ProjectSiteAdapter(ctx, R.layout.site_item,
                project.getProjectSiteList(), new ProjectSiteAdapter.ProjectSiteAdapterListener() {
            @Override
            public void onEditRequested(ProjectSiteDTO site, int index) {
                projectSite = project.getProjectSiteList().get(index);
                lastIndex = index;
                mListener.onProjectSiteEditRequested(site, index);
            }

            @Override
            public void onGalleryRequested(ProjectSiteDTO site, int index) {
                projectSite = project.getProjectSiteList().get(index);
                lastIndex = index;
                mListener.onGalleryRequested(site,index);

            }

            @Override
            public void onCameraRequested(ProjectSiteDTO site, int index) {
                projectSite = project.getProjectSiteList().get(index);
                lastIndex = index;
                mListener.onCameraRequested(site,index);
            }

            @Override
            public void onTasksRequested(ProjectSiteDTO site, int index) {
                projectSite = project.getProjectSiteList().get(index);
                lastIndex = index;
                mListener.onProjectSiteTasksRequested(site,index);
            }

            @Override
            public void onDeleteRequested(ProjectSiteDTO site, int index) {

            }

            @Override
            public void onStatusListRequested(ProjectSiteDTO site, int index) {
                mListener.onStatusListRequested(site,index);
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
                    mListener.onProjectSiteClicked(projectSite, position);
                }
            }
        });
    }
    int index;

    public void refreshData(ProjectSiteDTO site) {

        Log.i(LOG,"###### refreshData");
        if (projectSite == null) throw new UnsupportedOperationException("ProjectSiteDTO is null");
        RequestDTO w = new RequestDTO();
        w.setRequestType(RequestDTO.GET_PROJECT_SITE_DATA);
        w.setProjectSiteID(site.getProjectSiteID());

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
                        mListener.onPhotoListUpdated(projectSite, index);
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
    public void refreshPhotoList(ProjectSiteDTO site) {

        Log.i(LOG,"###### refreshPhotoList");
        if (projectSite == null) throw new UnsupportedOperationException("ProjectSiteDTO is null");
        RequestDTO w = new RequestDTO();
        w.setRequestType(RequestDTO.GET_SITE_IMAGES);
        w.setProjectSiteID(site.getProjectSiteID());

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
                        mListener.onPhotoListUpdated(projectSite, index);
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
    public void setListPosition(int index) {
        lastIndex = index;
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
        public void onProjectSiteClicked(ProjectSiteDTO projectSite, int index);
        public void onProjectSiteEditRequested(ProjectSiteDTO projectSite, int index);
        public void onProjectSiteTasksRequested(ProjectSiteDTO projectSite, int index);
        public void onCameraRequested(ProjectSiteDTO projectSite, int index);
        public void onGalleryRequested(ProjectSiteDTO projectSite, int index);
        public void onPhotoListUpdated(ProjectSiteDTO projectSite, int index);
        public void onStatusListRequested(ProjectSiteDTO projectSite, int index);
    }

    ProjectDTO project;
    ProjectSiteAdapter projectSiteAdapter;
}

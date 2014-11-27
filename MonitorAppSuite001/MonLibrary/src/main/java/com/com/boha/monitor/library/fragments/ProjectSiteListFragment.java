package com.com.boha.monitor.library.fragments;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.TextView;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.adapters.ProjectSiteAdapter;
import com.com.boha.monitor.library.adapters.SpinnerListAdapter;
import com.com.boha.monitor.library.dto.ProjectDTO;
import com.com.boha.monitor.library.dto.ProjectSiteDTO;
import com.com.boha.monitor.library.dto.ProjectSiteTaskDTO;
import com.com.boha.monitor.library.dto.ProjectSiteTaskStatusDTO;
import com.com.boha.monitor.library.dto.transfer.RequestDTO;
import com.com.boha.monitor.library.dto.transfer.ResponseDTO;
import com.com.boha.monitor.library.util.ErrorUtil;
import com.com.boha.monitor.library.util.Statics;
import com.com.boha.monitor.library.util.ToastUtil;
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
public class ProjectSiteListFragment extends Fragment implements PageFragment {

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
    View view, topView;
    ImageView imgLogo;
    ObjectAnimator objectAnimator;

    static final String LOG = ProjectSiteListFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_projectsite, container, false);
        Log.i(LOG, "------------- onCreateView");
        ctx = getActivity();
        Bundle b = getArguments();
        if (b != null) {
            Log.e(LOG, "++++ getting project object from getArguments");
            project = (ProjectDTO) b.getSerializable("project");
            lastIndex = b.getInt("index", 0);
        }
        if (savedInstanceState != null) {
            lastIndex = savedInstanceState.getInt("lastIndex", 0);
            Log.e(LOG, "++++ lastIndex in savedInstanceState: " + lastIndex);
        }
        txtCount = (TextView) view.findViewById(R.id.SITE_LIST_siteCount);
        imgLogo = (ImageView) view.findViewById(R.id.SITE_LIST_imgLogo);
        topView = view.findViewById(R.id.SITE_LIST_TOP);
        imgLogo.setVisibility(View.GONE);
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        Statics.setRobotoFontLight(ctx, txtCount);
        if (project.getProjectSiteList() != null && !project.getProjectSiteList().isEmpty()) {
            setList();
        }
        return view;
    }

    public void rotateLogo() {
        imgLogo.setVisibility(View.VISIBLE);
        objectAnimator = ObjectAnimator.ofFloat(imgLogo, "rotation", 0.0f, 360f);
        objectAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        objectAnimator.setDuration(200);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        objectAnimator.start();
    }

    public void stopRotatingLogo() {
        imgLogo.setVisibility(View.GONE);
        objectAnimator.cancel();
    }


    private void setList() {
        txtCount.setText("" + project.getProjectSiteList().size());
        for (ProjectSiteDTO site : project.getProjectSiteList()) {
            List<ProjectSiteTaskStatusDTO> taskStatusList = new ArrayList<>();
            for (ProjectSiteTaskDTO task : site.getProjectSiteTaskList()) {
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

        projectSiteAdapter = new ProjectSiteAdapter(ctx, R.layout.site_item,
                project.getProjectSiteList());
        mListView.setAdapter(projectSiteAdapter);
        mListView.setSelection(lastIndex);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (null != mListener) {
                    lastIndex = position;
                    projectSite = project.getProjectSiteList().get(position);
                    mListener.onProjectSiteClicked(projectSite, position);
                    list = new ArrayList<>();
                    list.add("Task List");
                    list.add("Take Picture");
                    list.add("Status Report");
                    list.add("Site on Map");
                    list.add("Site Gallery");
                    list.add("Edit Site Details");

                    actionsWindow = new ListPopupWindow(getActivity());
                    actionsWindow.setAdapter(new SpinnerListAdapter(ctx,
                            R.layout.xxsimple_spinner_item, list, SpinnerListAdapter.INVOICE_ACTIONS, project.getProjectName(), false));
                    actionsWindow.setAnchorView(topView);
                    actionsWindow.setWidth(600);
                    actionsWindow.setHorizontalOffset(72);
                    actionsWindow.setModal(true);
                    actionsWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            switch (position) {
                                case 0:
                                    mListener.onProjectSiteTasksRequested(projectSite, lastIndex);
                                    break;
                                case 1:
                                    mListener.onCameraRequested(projectSite, lastIndex);
                                    break;
                                case 2:
                                    mListener.onStatusListRequested(projectSite,lastIndex);
                                    break;
                                case 3:
                                    ToastUtil.toast(ctx,ctx.getString(R.string.under_cons));
                                    break;
                                case 4:
                                    mListener.onGalleryRequested(projectSite,lastIndex);
                                    break;
                                case 5:
                                    mListener.onProjectSiteEditRequested(projectSite,lastIndex);
                                    break;

                            }
                            actionsWindow.dismiss();
                        }
                    });
                    actionsWindow.show();
                }
            }
        });
    }

    int index;
    List<String> list;
    ListPopupWindow actionsWindow;

    public void refreshData(ProjectSiteDTO site) {

        Log.i(LOG, "###### refreshData");
        if (projectSite == null) throw new UnsupportedOperationException("ProjectSiteDTO is null");
        RequestDTO w = new RequestDTO();
        w.setRequestType(RequestDTO.GET_PROJECT_SITE_DATA);
        w.setProjectSiteID(site.getProjectSiteID());

        WebSocketUtil.sendRequest(ctx, Statics.COMPANY_ENDPOINT, w, new WebSocketUtil.WebSocketListener() {
            @Override
            public void onMessage(final ResponseDTO response) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!ErrorUtil.checkServerError(ctx, response)) {
                            return;
                        }
                        projectSite.setPhotoUploadList(response.getPhotoUploadList());

                        setList();
                        index = 0;
                        for (ProjectSiteDTO ps : project.getProjectSiteList()) {
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

        Log.i(LOG, "###### refreshPhotoList");
        if (projectSite == null) throw new UnsupportedOperationException("ProjectSiteDTO is null");
        RequestDTO w = new RequestDTO();
        w.setRequestType(RequestDTO.GET_SITE_IMAGES);
        w.setProjectSiteID(site.getProjectSiteID());

        WebSocketUtil.sendRequest(ctx, Statics.COMPANY_ENDPOINT, w, new WebSocketUtil.WebSocketListener() {
            @Override
            public void onMessage(final ResponseDTO response) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!ErrorUtil.checkServerError(ctx, response)) {
                            return;
                        }
                        projectSite.setPhotoUploadList(response.getPhotoUploadList());

                        setList();
                        index = 0;
                        for (ProjectSiteDTO ps : project.getProjectSiteList()) {
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
            Util.animateRotationY(txtCount, 500);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow objectAnimator interaction in this fragment to be communicated
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

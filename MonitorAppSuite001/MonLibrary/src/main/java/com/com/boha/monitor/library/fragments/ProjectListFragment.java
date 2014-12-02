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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.TextView;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.adapters.ProjectAdapter;
import com.com.boha.monitor.library.adapters.SpinnerListAdapter;
import com.com.boha.monitor.library.dto.ProjectDTO;
import com.com.boha.monitor.library.dto.ProjectSiteDTO;
import com.com.boha.monitor.library.dto.ProjectSiteTaskDTO;
import com.com.boha.monitor.library.dto.transfer.ResponseDTO;
import com.com.boha.monitor.library.util.Statics;
import com.com.boha.monitor.library.util.Util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a taskStatusList of Items.
 * <project />
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <project />
 * Activities containing this fragment MUST implement the ProjectListListener
 * interface.
 */
public class ProjectListFragment extends Fragment implements PageFragment {


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
    ImageView imgLogo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.w(LOG,"######### onCreateView");
        view = inflater.inflate(R.layout.fragment_project, container, false);
        this.inflater = inflater;
        ctx = getActivity();
        topView = view.findViewById(R.id.PROJ_LIST_layoutx);
        imgLogo = (ImageView) view.findViewById(R.id.PROJ_LIST_img);
        Bundle b = getArguments();
        if (b != null) {
            Log.w(LOG,"######### onCreateView getArguments not null ...");
            ResponseDTO r = (ResponseDTO) b.getSerializable("response");
            if (r.getCompany() != null) {
                projectList = r.getCompany().getProjectList();
                statusCountInPeriod = r.getStatusCountInPeriod();
            } else {
                Log.e(LOG,"-----------------> ERROR company frome getArguments is null");
            }
        }


        txtProjectCount = (TextView) view.findViewById(R.id.PROJ_LIST_projectCount);
        mListView = (ListView) view.findViewById(android.R.id.list);
        txtLabel = (TextView) view.findViewById(R.id.PROJ_LIST_label);
        Statics.setRobotoFontLight(ctx, txtLabel);


        setTotals();
        setList();
        return view;
    }


    Integer statusCountInPeriod;
    ObjectAnimator objectAnimator;
    static final DecimalFormat df = new DecimalFormat("###,###,###,###");

    public void stopRotatingLogo() {
        if (objectAnimator != null)
            objectAnimator.cancel();
    }

    public void rotateLogo() {
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

    private void setList() {
        if (projectList == null) {
            Log.e(LOG,"-----------> projectList is null. Possible illegally called");
            return;
        }
        adapter = new ProjectAdapter(ctx, R.layout.project_item, projectList);
        mListView.setAdapter(adapter);
        View v = inflater.inflate(R.layout.hero_image_project, null);
        txtStatusCount = (TextView) v.findViewById(R.id.HERO_statusCount);
        if (statusCountInPeriod != null) {
            txtStatusCount.setText(df.format(statusCountInPeriod));
        } else {
            txtStatusCount.setText("0");
        }
        txtStatusCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onStatusReportRequested();
            }
        });
        mListView.addHeaderView(v);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) return;
                project = projectList.get(position - 1);
                list = new ArrayList<>();
                list.add("Site List");
                list.add("Claims & Invoices");
                list.add("Status Report");
                list.add("Project Map");
                list.add("Take a Picture");
                list.add("Project Gallery");
                list.add("Edit Project");

                actionsWindow = new ListPopupWindow(getActivity());
                actionsWindow.setAdapter(new SpinnerListAdapter(ctx,
                        R.layout.xxsimple_spinner_item, list, SpinnerListAdapter.INVOICE_ACTIONS, project.getProjectName(), false));
                actionsWindow.setAnchorView(txtLabel);
                actionsWindow.setWidth(600);
                actionsWindow.setModal(true);
                actionsWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0:
                                mListener.onProjectSitesRequested(project);
                                break;
                            case 1:
                                mListener.onClaimsAndInvoicesRequested(project);
                                break;
                            case 2:
                                break;
                            case 3:
                                mListener.onMapRequested(project);
                                break;
                            case 4:
                                mListener.onProjectPictureRequested(project);
                                break;
                            case 5:
                                mListener.onGalleryRequested(project);
                                break;
                            case 6:

                                mListener.onProjectEditDialogRequested(project);
                                break;
                        }
                        actionsWindow.dismiss();
                    }
                });
                actionsWindow.show();
            }
        });
    }

    ListPopupWindow actionsWindow;
    List<String> list;
    static final String LOG = ProjectListFragment.class.getSimpleName();
    ProjectDTO project;
    int statusCount;
    TextView txtName;

    private void setTotals() {
        if (projectList == null) {
            Log.e(LOG,"-----------------------> projectList is null");
            return;
        }
        txtProjectCount.setText("" + projectList.size());
        statusCount = 0;
        for (ProjectDTO p : projectList) {
            for (ProjectSiteDTO ps : p.getProjectSiteList()) {
                for (ProjectSiteTaskDTO pst : ps.getProjectSiteTaskList()) {
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
        projectList.add(0, project);
        //Collections.sort(engineerList);
        adapter.notifyDataSetChanged();
        txtProjectCount.setText("" + projectList.size());
        try {
            Thread.sleep(1000);
            Util.animateRotationY(txtProjectCount, 500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow objectAnimator interaction in this fragment to be communicated
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

        public void onClaimsAndInvoicesRequested(ProjectDTO project);
        public void onStatusReportRequested();
    }

    private List<ProjectDTO> projectList;
    private ProjectAdapter adapter;

}

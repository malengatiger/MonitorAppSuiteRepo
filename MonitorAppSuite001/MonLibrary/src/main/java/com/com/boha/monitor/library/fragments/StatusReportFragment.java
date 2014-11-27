package com.com.boha.monitor.library.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.adapters.StatusReportAdapter;
import com.com.boha.monitor.library.dto.ProjectSiteDTO;
import com.com.boha.monitor.library.dto.ProjectSiteTaskDTO;
import com.com.boha.monitor.library.dto.ProjectSiteTaskStatusDTO;
import com.com.boha.monitor.library.dto.TaskDTO;
import com.com.boha.monitor.library.util.Statics;
import com.com.boha.monitor.library.util.Util;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * A fragment representing a taskStatusList of Items.
 * <project/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <project/>
 * Activities containing this fragment MUST implement the ProjectSiteListListener
 * interface.
 */
public class StatusReportFragment extends Fragment implements PageFragment {

    public interface StatusReportListener {
        public void setBusy();
        public void setNotBusy();
    }


    private StatusReportListener mListener;
    private AbsListView mListView;
    private ListAdapter mAdapter;

    public StatusReportFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    Context ctx;
    TextView txtCount, txtSiteName;
    ImageView heroImage;
    ProjectSiteDTO projectSite;
    List<ProjectSiteTaskStatusDTO> taskStatusList;
    ListView listView;
    LayoutInflater inflater;
    StatusReportAdapter adapter;

    public void setProjectSite(ProjectSiteDTO projectSite) {
        Log.d(LOG, "########## setProjectSite");
        this.projectSite = projectSite;
        setList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG, "########## onCreateView");
        this.inflater = inflater;
        View view = inflater.inflate(R.layout.fragment_status_list, container, false);
        listView = (ListView)view.findViewById(R.id.STATLST_list);
        heroImage = (ImageView)view.findViewById(R.id.STATLST_heroImage);
        ctx = getActivity();
        Bundle b = getArguments();
        if (b != null) {
            projectSite = (ProjectSiteDTO) b.getSerializable("projectSite");
            setList();
        }

        txtCount = (TextView) view.findViewById(R.id.STATLST_txtCount);
        txtSiteName = (TextView) view.findViewById(R.id.STATLST_txtTitle);

        return view;
    }
    boolean isHidden = false;
    Random random;
    static final Locale loc = Locale.getDefault();
    static final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm", loc);

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void setList() {
        Log.d(LOG, "########## setList");
        taskStatusList = new ArrayList<>();
        for (ProjectSiteTaskDTO task: projectSite.getProjectSiteTaskList()) {
            taskStatusList.addAll(task.getProjectSiteTaskStatusList());
        }
        Collections.sort(taskStatusList);
        txtCount.setText("" + taskStatusList.size());
        txtSiteName.setText(projectSite.getProjectSiteName());

        adapter = new StatusReportAdapter(ctx,R.layout.status_report_item,taskStatusList);
        listView.setAdapter(adapter);
        random = new Random(System.currentTimeMillis());
        if (projectSite.getPhotoUploadList() != null && !projectSite.getPhotoUploadList().isEmpty()) {
            int index = random.nextInt(projectSite.getPhotoUploadList().size() - 1);
            String uri = Statics.IMAGE_URL + projectSite.getPhotoUploadList()
                    .get(0).getUri();
            heroImage.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(uri,heroImage);
            heroImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //listener.onGalleryRequested(p,position);
                }
            });
        } else {
            heroImage.setImageDrawable(ctx.getResources().getDrawable(R.drawable.under_construction));
        }



    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (StatusReportListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Host " + activity.getLocalClassName()
                    + " must implement StatusReportListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    TaskDTO task;
    int staffType;
    public static final int OPERATIONS = 1, PROJECT_MANAGER = 2, SITE_SUPERVISOR = 3;
    static final String LOG = StatusReportFragment.class.getSimpleName();

    @Override
    public void animateCounts() {
        Util.animateRotationY(txtCount, 500);

    }

}

package com.com.boha.monitor.library.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.adapters.ImageAdapter;
import com.com.boha.monitor.library.dto.ProjectDTO;
import com.com.boha.monitor.library.dto.ProjectSiteDTO;
import com.com.boha.monitor.library.dto.ProjectSiteTaskDTO;
import com.com.boha.monitor.library.dto.transfer.PhotoUploadDTO;
import com.com.boha.monitor.library.util.PhotoCache;
import com.com.boha.monitor.library.util.Statics;
import com.etsy.android.grid.StaggeredGridView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A fragment representing a taskStatusList of Items.
 * <project/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <project/>
 * Activities containing this fragment MUST implement the ProjectSiteListListener
 * interface.
 */
public class ImageGridFragment extends Fragment implements PageFragment {

    private ImageListener mListener;

    public static ImageGridFragment newInstance(PhotoCache photoCache) {
        ImageGridFragment fragment = new ImageGridFragment();
        Bundle args = new Bundle();
        args.putSerializable("photoCache", photoCache);
        fragment.setArguments(args);
        return fragment;
    }

    public ImageGridFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getArguments();
        if (b != null) {
            projectSite = (ProjectSiteDTO) b.getSerializable("projectSite");
            project = (ProjectDTO) b.getSerializable("project");
            projectSiteTask = (ProjectSiteTaskDTO) b.getSerializable("projectSiteTask");
            if (projectSite != null) pathList = projectSite.getPhotoUploadList();
            if (project != null) pathList = project.getPhotoUploadList();
            if (projectSiteTask != null) pathList = projectSiteTask.getPhotoUploadList();
        }
    }

    ProjectSiteDTO projectSite;
    ProjectDTO project;
    ProjectSiteTaskDTO projectSiteTask;
    private Context ctx;
    private PhotoCache photoCache;
    private GridView gridView;
    StaggeredGridView staggeredGridView;
    private ImageAdapter imageAdapter;
    private TextView txtCount, txtName, txtTitle, txtSubTitle, txtPeriod;
    private View header, footer;
    private List<PhotoUploadDTO> pathList;
    private static final Locale loc = Locale.getDefault();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMMM yyyy HH:mm", loc);
    static final String LOG = ImageGridFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_image_grid, container, false);
        ctx = getActivity();
        header = inflater.inflate(R.layout.header_image_grid, container, false);
        footer = inflater.inflate(R.layout.footer_image_grid, container, false);
        setFields(inflater);

        return view;
    }

    private void setFields(LayoutInflater inflater) {

        txtTitle = (TextView) header.findViewById(R.id.HIG_title);
        txtSubTitle = (TextView) header.findViewById(R.id.HIG_subtitle);
        txtCount = (TextView) header.findViewById(R.id.HIG_imageCount);
        txtPeriod = (TextView) footer.findViewById(R.id.FIG_period);
        Statics.setRobotoFontLight(ctx,txtTitle);
        Statics.setRobotoFontBold(ctx,txtSubTitle);

        gridView = (GridView) view.findViewById(R.id.grid);
        gridView.setVisibility(View.GONE);
        //
        if (pathList == null) {
            Log.e(LOG,"------- pathList is null, no photos");
            return;
        }
        staggeredGridView = (StaggeredGridView) view.findViewById(R.id.staggeredGrid);
        imageAdapter = new ImageAdapter(ctx, R.layout.image_item, pathList);


        imageAdapter.notifyDataSetChanged();
        if (projectSite != null) {
            txtTitle.setText(projectSite.getProjectName());
            txtSubTitle.setText(projectSite.getProjectSiteName());
            if (projectSite.getPhotoUploadList() != null && !projectSite.getPhotoUploadList().isEmpty()) {
                txtCount.setText("" + (projectSite.getPhotoUploadList().size()));
                Date end = projectSite.getPhotoUploadList().get(0).getDateTaken();
                int size = projectSite.getPhotoUploadList().size();
                Date start = projectSite.getPhotoUploadList().get(size - 1).getDateTaken();
                txtPeriod.setText(sdf.format(start) + " - " + sdf.format(end));
            } else {
                txtCount.setText("0");
                txtPeriod.setVisibility(View.GONE);
            }
        }
        if (project != null) {
            txtTitle.setText(project.getProjectName());
            txtSubTitle.setVisibility(View.GONE);
            if (project.getPhotoUploadList() != null) {
                txtCount.setText("" + (project.getPhotoUploadList().size()));
                Date end = project.getPhotoUploadList().get(0).getDateTaken();
                int size = project.getPhotoUploadList().size();
                Date start = project.getPhotoUploadList().get(size - 1).getDateTaken();
                txtPeriod.setText(sdf.format(start) + " - " + sdf.format(end));
            } else {
                txtCount.setText("0");
                txtPeriod.setVisibility(View.GONE);
            }
        }
        staggeredGridView.addHeaderView(header);
        staggeredGridView.addFooterView(footer);
        staggeredGridView.setAdapter(imageAdapter);
        staggeredGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e(LOG,"####### staggeredGridView position: " + position + " pathList size: " + pathList.size());
                if (position == 0) return;

                if (position > pathList.size()) return;
                if (position == pathList.size()) {
                    mListener.onImageClicked(pathList.get(position - 1), position -1);
                } else {

                    mListener.onImageClicked(pathList.get(position), position - 1);
                }
            }
        });
    }

    View view;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (ImageListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Host " + activity.getLocalClassName()
                    + " must implement ImageListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void animateCounts() {
        //Util.animateRotationY(txtCount,500);

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow logoAnimator interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <project/>
     */
    public interface ImageListener {
        public void onImageClicked(PhotoUploadDTO photo, int index);
    }


}

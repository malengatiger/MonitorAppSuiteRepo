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
import com.com.boha.monitor.library.dto.transfer.PhotoUploadDTO;
import com.com.boha.monitor.library.util.PhotoCache;

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
public class ImageGridFragment extends Fragment implements PageFragment {

    private ImageListener mListener;

    public static ImageGridFragment newInstance(PhotoCache photoCache) {
        ImageGridFragment fragment = new ImageGridFragment();
        Bundle args = new Bundle();
        args.putSerializable("photoCache", photoCache);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ImageGridFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getArguments();
        int count = 0;
        if (b != null) {
            photoCache = (PhotoCache) b.getSerializable("photoCache");
            pathList = new ArrayList<>();
            for (PhotoUploadDTO dto : photoCache.getPhotoUploadList()) {
                pathList.add(dto.getThumbFilePath());
            }
            Log.i(LOG,"**** photoCache: " + pathList.size());
        }
    }

    private Context ctx;
    private PhotoCache photoCache;
    private GridView gridView;
    private ImageAdapter imageAdapter;
    private TextView txtCount, txtName;
    private List<String> pathList;
    static final String LOG = ImageGridFragment.class.getSimpleName();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_image_grid, container, false);
        ctx = getActivity();
        setFields();

        return view;
    }
    private void setFields() {

        gridView = (GridView) view.findViewById(R.id.grid);
        imageAdapter = new ImageAdapter(ctx, R.layout.image_item, pathList);

        Log.w(LOG,"------ setting adapter to GRID");
        gridView.setAdapter(imageAdapter);
        imageAdapter.notifyDataSetChanged();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListener.onImageClicked(position);
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
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     */
    public interface ImageListener {
        public void onImageClicked(int index);
    }


}

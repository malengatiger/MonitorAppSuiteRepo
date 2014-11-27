package com.com.boha.monitor.library.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.boha.monitor.library.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;

/**
 * A fragment representing a taskStatusList of Items.
 * <project/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <project/>
 * Activities containing this fragment MUST implement the ProjectSiteListListener
 * interface.
 */
public class ImageFragment extends Fragment implements PageFragment {

    public static ImageFragment newInstance(String path) {
        ImageFragment fragment = new ImageFragment();
        Bundle args = new Bundle();
        args.putString("path", path);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ImageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        if (b != null) {
            path = b.getString("path");
        }
    }

    private Context ctx;
    private String path;
    private ImageView image;
    private TextView txtCount, txtName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, container, false);
        ctx = getActivity();

        imageLoader = ImageLoader.getInstance();
        image = (ImageView) view.findViewById(R.id.image);
        if (path != null) {

            File f = new File(path);
            imageLoader.displayImage(Uri.fromFile(f).toString(),image);

            //Picasso.with(ctx).load(f).into(image);
            if (image.getWidth() > image.getHeight()) {

                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            } else {
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


            }
        }


        return view;
    }
ImageLoader imageLoader;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void animateCounts() {
        //Util.animateScaleX(image,300);

    }

    public String getPath() {
        return path;
    }
}

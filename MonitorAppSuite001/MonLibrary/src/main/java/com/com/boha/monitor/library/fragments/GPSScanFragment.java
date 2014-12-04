package com.com.boha.monitor.library.fragments;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.MonitorMapActivity;
import com.com.boha.monitor.library.dto.ProjectSiteDTO;
import com.com.boha.monitor.library.dto.transfer.RequestDTO;
import com.com.boha.monitor.library.dto.transfer.ResponseDTO;
import com.com.boha.monitor.library.util.ErrorUtil;
import com.com.boha.monitor.library.util.Statics;
import com.com.boha.monitor.library.util.WebSocketUtil;


public class GPSScanFragment extends Fragment implements PageFragment {

    @Override
    public void animateCounts() {

    }

    public interface GPSScanFragmentListener {
        public void onStartScanRequested();

        public void onEndScanRequested();
    }

    private GPSScanFragmentListener listener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment GPSScanFragment.
     */
    public static GPSScanFragment newInstance() {
        GPSScanFragment fragment = new GPSScanFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public GPSScanFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    TextView desiredAccuracy, txtLat, txtLng, txtAccuracy, txtName;
    Button btnScan, btnSave;
    View view;
    SeekBar seekBar;
    boolean isScanning;
    ProjectSiteDTO projectSite;
    ImageView imgLogo;
    Context ctx;
    ObjectAnimator objectAnimator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_gps, container, false);
        ctx = getActivity();
        desiredAccuracy = (TextView) view.findViewById(R.id.GPS_desiredAccuracy);
        txtAccuracy = (TextView) view.findViewById(R.id.GPS_accuracy);
        txtLat = (TextView) view.findViewById(R.id.GPS_latitude);
        txtLng = (TextView) view.findViewById(R.id.GPS_longitude);
        btnSave = (Button) view.findViewById(R.id.GPS_btnSave);
        btnScan = (Button) view.findViewById(R.id.GPS_btnStop);
        seekBar = (SeekBar) view.findViewById(R.id.GPS_seekBar);
        imgLogo = (ImageView) view.findViewById(R.id.GPS_imgLogo);
        txtName = (TextView) view.findViewById(R.id.GPS_siteName);
        rotateLogo();
        btnSave.setVisibility(View.GONE);
        Statics.setRobotoFontLight(ctx, txtLat);
        Statics.setRobotoFontLight(ctx, txtLng);

        imgLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (projectSite.getLatitude() != null) {
                    Intent i = new Intent(ctx, MonitorMapActivity.class);
                    i.putExtra("projectSite", projectSite);
                    startActivity(i);
                }
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                desiredAccuracy.setText("" + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isScanning) {
                    listener.onEndScanRequested();
                    isScanning = false;
                    btnScan.setText(ctx.getString(R.string.start_scan));
                    stopRotatingLogo();
                } else {
                    listener.onStartScanRequested();
                    isScanning = true;
                    btnScan.setText(ctx.getString(R.string.stop_scan));
                    rotateLogo();
                }
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendGPSData();
            }
        });

        listener.onStartScanRequested();
        isScanning = true;
        btnScan.setText(ctx.getString(R.string.stop_scan));
        rotateLogo();
        return view;
    }

    public void stopRotatingLogo() {
        if (objectAnimator != null) {
            objectAnimator.cancel();
            Log.e(LOG, "###### stopRotatingLogo - objectAnimator.cancel");
        }
    }

    public void rotateLogo() {
        Log.w(LOG, "++++++= rotateLogo ..............");
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
        //objectAnimator.start();
    }

    private void sendGPSData() {

        RequestDTO w = new RequestDTO(RequestDTO.UPDATE_PROJECT_SITE);
        ProjectSiteDTO site = new ProjectSiteDTO();
        site.setProjectSiteID(projectSite.getProjectSiteID());
        site.setLatitude(location.getLatitude());
        site.setLongitude(location.getLongitude());
        site.setAccuracy(location.getAccuracy());
        w.setProjectSite(site);

        rotateLogo();
        WebSocketUtil.sendRequest(ctx, Statics.COMPANY_ENDPOINT, w, new WebSocketUtil.WebSocketListener() {
            @Override
            public void onMessage(final ResponseDTO response) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stopRotatingLogo();
                        if (!ErrorUtil.checkServerError(ctx, response)) {
                            return;
                        }
                        Log.w(LOG, "++++++++++++ project site location updated");
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

    public void setLocation(Location location) {
        this.location = location;
        txtLat.setText("" + location.getLatitude());
        txtLng.setText("" + location.getLongitude());
        txtAccuracy.setText("" + location.getAccuracy());

        if (location.getAccuracy() == seekBar.getProgress()
                || location.getAccuracy() < seekBar.getProgress()) {
            listener.onEndScanRequested();
            isScanning = false;
            stopRotatingLogo();
            btnScan.setText(ctx.getString(R.string.start_scan));
            btnSave.setVisibility(View.VISIBLE);
            Log.d(LOG, "----------- onEndScanRequested - stopped scanning");
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (GPSScanFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " - Host activity" + activity.getLocalClassName() + " must implement GPSScanFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    Location location;
    static final String LOG = GPSScanFragment.class.getSimpleName();

    public void setProjectSite(ProjectSiteDTO projectSite) {
        this.projectSite = projectSite;
        if (projectSite != null)
            txtName.setText(projectSite.getProjectSiteName());
    }

    public ProjectSiteDTO getProjectSite() {
        return projectSite;
    }
}
